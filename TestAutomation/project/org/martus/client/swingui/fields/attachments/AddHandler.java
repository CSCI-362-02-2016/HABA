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
import java.awt.event.ActionListener;
import java.io.File;

import org.martus.client.swingui.UiMainWindow;
import org.martus.client.swingui.tablemodels.AttachmentTableModel;
import org.martus.clientside.FileDialogHelpers;
import org.martus.common.bulletin.AttachmentProxy;

class AddHandler implements ActionListener
{
	public AddHandler(UiMainWindow mainWindowToUse, AttachmentTableModel modelToUse, UiAttachmentEditor editorToUse)
	{
		mainWindow = mainWindowToUse;
		model = modelToUse;
		editor = editorToUse;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		File file = mainWindow.doFileOpenDialogWithDirectoryMemory("AddAttachment", FileDialogHelpers.NO_FILTER);
		if(file == null)
			return;
		
		AttachmentProxy a = new AttachmentProxy(file);
		editor.addAttachment(a);
	}

	UiMainWindow mainWindow;
	AttachmentTableModel model;
	UiAttachmentEditor editor;
}
