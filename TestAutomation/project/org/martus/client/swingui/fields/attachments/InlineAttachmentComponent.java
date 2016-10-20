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

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.martus.client.bulletinstore.ClientBulletinStore;
import org.martus.common.bulletin.AttachmentProxy;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.MartusCrypto.CryptoException;
import org.martus.common.database.ReadableDatabase;
import org.martus.common.packet.AttachmentPacket;
import org.martus.common.packet.Packet.InvalidPacketException;
import org.martus.common.packet.Packet.SignatureVerificationException;
import org.martus.common.packet.Packet.WrongPacketTypeException;
import org.martus.swing.UiLabel;
import org.martus.util.StreamableBase64.InvalidBase64Exception;

class InlineAttachmentComponent extends UiLabel
{
	public InlineAttachmentComponent(ClientBulletinStore store, AttachmentProxy proxy) throws Exception
	{
		File tempFile = obtainFileForAttachment(store, proxy);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(tempFile.getAbsolutePath());
		ImageIcon icon = new ImageIcon(image);
		setIcon(icon);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	private File obtainFileForAttachment(ClientBulletinStore store, AttachmentProxy proxy) throws IOException, InvalidBase64Exception, InvalidPacketException, SignatureVerificationException, WrongPacketTypeException, CryptoException
	{
		File attachmentAlreadyAvailableAsFile = proxy.getFile();
		if(attachmentAlreadyAvailableAsFile != null)
			return attachmentAlreadyAvailableAsFile;
		
		AttachmentPacket pendingPacket = proxy.getPendingPacket();
		if(pendingPacket != null)
		{
			File tempFileAlreadyAvailable = pendingPacket.getRawFile();
			if(tempFileAlreadyAvailable != null)
				return tempFileAlreadyAvailable;
		}
		
		ReadableDatabase db = store.getDatabase();
		MartusCrypto security = store.getSignatureVerifier();
		File tempFile = ViewAttachmentHandler.extractAttachmentToTempFile(db, proxy, security);
		return tempFile;
	}
	
	public boolean isValid()
	{
		Icon icon = getIcon();
		if(icon == null)
			return false;
		
		return (icon.getIconHeight() > 0);
	}
}