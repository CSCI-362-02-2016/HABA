/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2001-2007, Beneficent
Technology, Inc. (The Benetech Initiative).

Martus is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later
version with the additions and exceptions described in the
accompanying Martus license file entitled "license.txt".

It is distributed WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, including warranties of fitness of purpose or
merchantability.  See the accompanying Martus License and
GPL license for more details on the required license terms
for this software.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.

*/

package org.martus.client.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Vector;

import org.martus.client.bulletinstore.BulletinFolder;
import org.martus.client.bulletinstore.ClientBulletinStore;
import org.martus.client.core.MartusApp.SaveConfigInfoException;
import org.martus.clientside.ClientSideNetworkGateway;
import org.martus.common.ContactInfo;
import org.martus.common.MartusUtilities;
import org.martus.common.MartusUtilities.FileTooLargeException;
import org.martus.common.ProgressMeterInterface;
import org.martus.common.bulletin.Bulletin;
import org.martus.common.bulletin.BulletinZipUtilities;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.MartusCrypto.MartusSignatureException;
import org.martus.common.database.DatabaseKey;
import org.martus.common.database.ReadableDatabase;
import org.martus.common.network.NetworkInterfaceConstants;
import org.martus.common.network.NetworkResponse;
import org.martus.common.packet.Packet;
import org.martus.common.packet.UniversalId;
import org.martus.util.StreamableBase64;

public class BackgroundUploader
{
	public BackgroundUploader(MartusApp appToUse, ProgressMeterInterface progressMeterToUse)
	{
		app = appToUse;
		progressMeter = progressMeterToUse;
	}

	public UploadResult backgroundUpload()
	{
		UploadResult uploadResult = new UploadResult();
		uploadResult.result = NetworkInterfaceConstants.UNKNOWN;
	
		ClientBulletinStore store = app.getStore();
		BulletinFolder folderSealedOutbox = app.getFolderSealedOutbox();
		BulletinFolder folderDraftOutbox = app.getFolderDraftOutbox();
		if(store.hasAnyNonDiscardedBulletins(folderSealedOutbox))
			uploadResult = uploadOneBulletin(folderSealedOutbox);
		else if(store.hasAnyNonDiscardedBulletins(folderDraftOutbox))
			uploadResult = uploadOneBulletin(folderDraftOutbox);
		else if(app.getConfigInfo().shouldContactInfoBeSentToServer())
			uploadResult = sendContactInfoToServer();
		return uploadResult;
	}

	public String uploadBulletin(Bulletin b) throws Exception
	{
		ClientBulletinStore store = app.getStore();
		// FIXME: is it safe to skip if it's "probably" on the server???
		if(b.isSealed() && store.isProbablyOnServer(b.getUniversalId()))
			return NetworkInterfaceConstants.DUPLICATE;
		File tempFile = File.createTempFile("$$$MartusUploadBulletin", null);
		try
		{
			tempFile.deleteOnExit();
			UniversalId uid = b.getUniversalId();

			ReadableDatabase db = store.getDatabase();
			DatabaseKey headerKey = DatabaseKey.createKey(uid, b.getStatus());
			MartusCrypto security = app.getSecurity();
			BulletinZipUtilities.exportBulletinPacketsFromDatabaseToZipFile(db, headerKey, tempFile, security);
			
			String tag = getUploadProgressTag(b);
			progressMeter.setStatusMessage(tag);
			return uploadBulletinZipFile(uid, tempFile);
		}
		finally
		{
			tempFile.delete();
		}
	}
	
	private String getUploadProgressTag(Bulletin b)
	{
		if(b.isDraft())
			return "UploadingDraftBulletin";
		return "UploadingSealedBulletin";
	}
	
	private String uploadBulletinZipFile(UniversalId uid, File tempFile)
		throws
			FileTooLargeException,
			FileNotFoundException,
			IOException,
			MartusSignatureException
	{
		int totalSize = MartusUtilities.getCappedFileLength(tempFile);
		int offset = getOffsetToStartUploading(uid, tempFile);
		byte[] rawBytes = new byte[app.serverChunkSize];
		FileInputStream inputStream = new FileInputStream(tempFile);
		inputStream.skip(offset);
		String result = null;
		while(true)
		{
			if(progressMeter != null)
				progressMeter.updateProgressMeter(offset, totalSize);
			int chunkSize = inputStream.read(rawBytes);
			if(chunkSize <= 0)
				break;
			byte[] chunkBytes = new byte[chunkSize];
			System.arraycopy(rawBytes, 0, chunkBytes, 0, chunkSize);
		
			String authorId = uid.getAccountId();
			String bulletinLocalId = uid.getLocalId();
			String encoded = StreamableBase64.encode(chunkBytes);
		
			NetworkResponse response = app.getCurrentNetworkInterfaceGateway().putBulletinChunk(app.getSecurity(),
								authorId, bulletinLocalId, totalSize, offset, chunkSize, encoded);
			result = response.getResultCode();
			if(!result.equals(NetworkInterfaceConstants.CHUNK_OK) && !result.equals(NetworkInterfaceConstants.OK))
				break;
			offset += chunkSize;
		}
		inputStream.close();
		return result;
	}

	public int getOffsetToStartUploading(UniversalId uid, File tempFile)
	{
		ClientSideNetworkGateway gateway = app.getCurrentNetworkInterfaceGateway();
		return gateway.getOffsetToStartUploading(uid, tempFile, app.getSecurity());
	}

	BackgroundUploader.UploadResult uploadOneBulletin(BulletinFolder uploadFromFolder)
	{
		UploadResult uploadResult = new UploadResult();
	
		if(!app.isSSLServerAvailable())
			return uploadResult;
	
		int index = new Random().nextInt(uploadFromFolder.getBulletinCount());
		ClientBulletinStore store = app.getStore();
		Bulletin b = store.chooseBulletinToUpload(uploadFromFolder, index);
		uploadResult.uid = b.getUniversalId();
		try
		{
			uploadResult.result = uploadBulletin(b);
			if(uploadResult.result == null)
				return uploadResult;
			
			if(uploadResult.result.equals(NetworkInterfaceConstants.OK) || 
					uploadResult.result.equals(NetworkInterfaceConstants.DUPLICATE))
			{
				store.setIsOnServer(b);
				// TODO: Is the file this creates ever used???
				app.resetLastUploadedTime();
			}
			else
				uploadResult.bulletinNotSentAndRemovedFromQueue = true;
				
		}
		catch (Packet.InvalidPacketException e)
		{
			uploadResult.exceptionThrown = e.toString();
			uploadResult.isHopelesslyDamaged = true;
		} 
		catch (Packet.WrongPacketTypeException e)
		{
			uploadResult.exceptionThrown = e.toString();
			uploadResult.isHopelesslyDamaged = true;
		} 
		catch (Packet.SignatureVerificationException e)
		{
			uploadResult.exceptionThrown = e.toString();
			uploadResult.isHopelesslyDamaged = true;
		} 
		catch (MartusCrypto.DecryptionException e)
		{
			uploadResult.exceptionThrown = e.toString();
			uploadResult.isHopelesslyDamaged = true;
		} 
		catch (MartusUtilities.FileTooLargeException e)
		{
			uploadResult.exceptionThrown = e.toString();
			uploadResult.isHopelesslyDamaged = true;
		} 
		catch (Exception e)
		{
			uploadResult.exceptionThrown = e.toString();
			uploadResult.bulletinNotSentAndRemovedFromQueue = true;
		}
		finally
		{
			if(uploadResult.isHopelesslyDamaged)
				app.moveBulletinToDamaged(uploadFromFolder, uploadResult.uid);
			else
				uploadFromFolder.remove(uploadResult.uid);
			store.saveFolders();
		}
		return uploadResult;
	}

	public String putContactInfoOnServer(Vector info)  throws
			MartusCrypto.MartusSignatureException
	{
		ClientSideNetworkGateway gateway = app.getCurrentNetworkInterfaceGateway();
		NetworkResponse response = gateway.putContactInfo(app.getSecurity(), app.getAccountId(), info);
		return response.getResultCode();
	}

	UploadResult sendContactInfoToServer()
	{
		BackgroundUploader.UploadResult uploadResult = new BackgroundUploader.UploadResult();
		uploadResult.result = CONTACT_INFO_NOT_SENT;
		
		if(!app.isSSLServerAvailable())
			return uploadResult;
	
		String result = "";
		ConfigInfo configInfo = app.getConfigInfo();
		try
		{
			MartusCrypto signer = app.getSecurity();
			ContactInfo contactInfo = createContactInfo(configInfo);
			Vector contactInfoVector = contactInfo.getSignedEncodedVector(signer);
			result = putContactInfoOnServer(contactInfoVector);
		}
		catch (MartusCrypto.MartusSignatureException e)
		{
			System.out.println("MartusApp.sendContactInfoToServer Sig Error:" + e);
			return uploadResult;
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println("MartusApp.sendContactInfoToServer Encoding Error:" + e);
			return uploadResult;
		}
		if(!result.equals(NetworkInterfaceConstants.OK))
		{
			System.out.println("MartusApp.sendContactInfoToServer failure:" + result);
			return uploadResult;
		}
		System.out.println("Contact info successfully sent to server");
		uploadResult.result = result;
	
		try
		{
			configInfo.setSendContactInfoToServer(false);
			app.saveConfigInfo();
		}
		catch (SaveConfigInfoException e)
		{
			System.out.println("MartusApp:putContactInfoOnServer Failed to save contactinfo locally:" + e);
			e.printStackTrace();
		}
		return uploadResult;
	}

	public static class UploadResult
	{
		public UniversalId uid;
		public String result;
		public String exceptionThrown;
		public boolean isHopelesslyDamaged;
		public boolean bulletinNotSentAndRemovedFromQueue;
	}
	
	private ContactInfo createContactInfo(ConfigInfo sourceOfInfo)
	{
		return new ContactInfo(sourceOfInfo.getAuthor(),
				sourceOfInfo.getOrganization(),
				sourceOfInfo.getEmail(),
				sourceOfInfo.getWebPage(),
				sourceOfInfo.getPhone(),
				sourceOfInfo.getAddress());
	}
	
	public static final String CONTACT_INFO_NOT_SENT="Contact Info Not Sent";

	MartusApp app;
	ProgressMeterInterface progressMeter;
}
