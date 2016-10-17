/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2007, Beneficent
Technology, Inc. (Benetech).

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
package org.martus.client.swingui.fields.attachments;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.martus.client.bulletinstore.ClientBulletinStore;
import org.martus.client.core.BulletinXmlExporter;
import org.martus.client.swingui.UiMainWindow;
import org.martus.common.MartusLogger;
import org.martus.common.bulletin.AttachmentProxy;
import org.martus.common.bulletin.BulletinLoader;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.MartusCrypto.CryptoException;
import org.martus.common.database.ReadableDatabase;
import org.martus.common.packet.Packet.InvalidPacketException;
import org.martus.common.packet.Packet.SignatureVerificationException;
import org.martus.common.packet.Packet.WrongPacketTypeException;
import org.martus.swing.Utilities;
import org.martus.util.StreamableBase64.InvalidBase64Exception;

class ViewAttachmentHandler extends AbstractViewOrSaveAttachmentHandler
{
	public ViewAttachmentHandler(UiMainWindow mainWindowToUse, AbstractAttachmentPanel panelToUse)
	{
		super(mainWindowToUse);
		panel = panelToUse;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		panel.showImageInline();
		if(panel.isImageInline)
			return;
		
		if(!Utilities.isMSWindows() && !Utilities.isMacintosh() && !UiMainWindow.isAlphaTester)
		{
			getMainWindow().notifyDlg("ViewAttachmentNotAvailable");
			return;
		}
		
		getMainWindow().setWaitingCursor();
		try
		{
			AttachmentProxy proxy = panel.getAttachmentProxy();
			String proxyAuthor = getProxyAuthor(proxy);
			if(proxyAuthor != null && !getMainWindow().getApp().getAccountId().equals(proxyAuthor))
			{
				String actionName = getMainWindow().getLocalization().getFieldLabel("ViewAttachmentAction");
				if(!confirmViewOrSaveNotYourAttachment(proxyAuthor, actionName))
					return;
			}

			File temp = getAttachmentAsFile(proxy);
			launchExternalAttachmentViewer(temp);
		}
		catch(Exception e)
		{
			MartusLogger.logException(e);
			System.out.println("Unable to view attachment:" + e);
			notifyUnableToView();
		}
		getMainWindow().resetCursor();
	}

	private File getAttachmentAsFile(AttachmentProxy proxy) throws IOException, InvalidBase64Exception, InvalidPacketException, SignatureVerificationException, WrongPacketTypeException, CryptoException 
	{
		if(proxy.getFile() != null)
			return proxy.getFile();
		
		ClientBulletinStore store = getMainWindow().getApp().getStore();
		ReadableDatabase db = store.getDatabase();
		MartusCrypto security = store.getSignatureVerifier();
		File temp = extractAttachmentToTempFile(db, proxy, security);
		return temp;
	}

	private void launchExternalAttachmentViewer(File temp) throws IOException, InterruptedException 
	{
		Runtime runtime = Runtime.getRuntime();

		String[] launchCommand = getLaunchCommandForThisOperatingSystem(temp.getPath());
		
		Process processView=runtime.exec(launchCommand);
		int exitCode = processView.waitFor();
		if(exitCode != 0)
		{
			MartusLogger.logError("Error viewing attachment: " + exitCode);
			String launchCommandAsString = "";
			for (String part : launchCommand)
			{
				launchCommandAsString += part;
				launchCommandAsString += ' ';
			}
			MartusLogger.logError(launchCommandAsString);
			dumpOutputToConsole("stdout", processView.getInputStream());
			dumpOutputToConsole("stderr", processView.getErrorStream());
			notifyUnableToView();
		}
	}

	private void dumpOutputToConsole(String streamName, InputStream capturedOutput) throws IOException
	{
		if(capturedOutput.available() <= 0)
			return;
		
		System.out.println("Captured output from " + streamName + ":");
		while(capturedOutput.available() > 0)
		{
			int got = capturedOutput.read();
			if(got < 0)
				break;
			System.out.print((char)got);
		}
		System.out.println();
	}

	private String[] getLaunchCommandForThisOperatingSystem(String fileToLaunch)
	{
		if(Utilities.isMSWindows())
			return new String[] {"cmd", "/C", AttachmentProxy.escapeFilenameForWindows(fileToLaunch)};
		
		else if(Utilities.isMacintosh())
			return new String[] {"open", fileToLaunch};

		else if(UiMainWindow.isAlphaTester) 
			return new String[] {"firefox", fileToLaunch};
		
		throw new RuntimeException("Launch not supported on this operating system");
	}

	private void notifyUnableToView()
	{
		getMainWindow().notifyDlg("UnableToViewAttachment");
	}
	
	static File extractAttachmentToTempFile(ReadableDatabase db, AttachmentProxy proxy, MartusCrypto security) throws IOException, InvalidBase64Exception, InvalidPacketException, SignatureVerificationException, WrongPacketTypeException, CryptoException
	{
		String fileName = proxy.getLabel();
		
		File temp = File.createTempFile(BulletinXmlExporter.extractFileNameOnly(fileName), BulletinXmlExporter.extractExtentionOnly(fileName));
		temp.deleteOnExit();
	
		BulletinLoader.extractAttachmentToFile(db, proxy, security, temp);
		return temp;
	}

	AbstractAttachmentPanel panel;
}