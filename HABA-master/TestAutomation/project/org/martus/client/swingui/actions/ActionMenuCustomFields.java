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

package org.martus.client.swingui.actions;

import java.awt.event.ActionEvent;

import org.martus.client.bulletinstore.ClientBulletinStore;
import org.martus.client.core.MartusApp;
import org.martus.client.core.MartusApp.SaveConfigInfoException;
import org.martus.client.swingui.UiMainWindow;
import org.martus.client.swingui.dialogs.UiCustomFieldsDlg;
import org.martus.common.FieldCollection;
import org.martus.common.FieldSpecCollection;
import org.martus.common.MartusConstants;
import org.martus.common.FieldCollection.CustomFieldsParseException;
import org.martus.common.fieldspec.BulletinFieldSpecs;
import org.martus.common.fieldspec.StandardFieldSpecs;

public class ActionMenuCustomFields extends UiMenuAction
{
	public ActionMenuCustomFields(UiMainWindow mainWindowToUse)
	{
		super(mainWindowToUse, "CustomFields");
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		if(!mainWindow.confirmDlg("EnterCustomFields"))
			return;
		
		MartusApp app = mainWindow.getApp();
		ClientBulletinStore store = app.getStore();
		BulletinFieldSpecs existingSpecs = new BulletinFieldSpecs();
		existingSpecs.setTopSectionSpecs(store.getTopSectionFieldSpecs());
		existingSpecs.setBottomSectionSpecs(store.getBottomSectionFieldSpecs());
		
		BulletinFieldSpecs newSpecs = getCustomizedFieldsFromUser(existingSpecs);
		if(newSpecs == null)
			return;
			
		FieldSpecCollection topSectionSpecs = newSpecs.getTopSectionSpecs();
		FieldSpecCollection bottomSectionSpecs = newSpecs.getBottomSectionSpecs();
		store.setTopSectionFieldSpecs(topSectionSpecs);
		store.setBottomSectionFieldSpecs(bottomSectionSpecs);
		app.getConfigInfo().setCustomFieldLegacySpecs(MartusConstants.deprecatedCustomFieldSpecs);
		app.getConfigInfo().setCustomFieldTopSectionXml(topSectionSpecs.toXml());
		app.getConfigInfo().setCustomFieldBottomSectionXml(bottomSectionSpecs.toXml());

		try
		{
			app.saveConfigInfo();
		}
		catch (SaveConfigInfoException e)
		{
			mainWindow.notifyDlg("ErrorSavingConfig");
		}
	}

	private BulletinFieldSpecs getCustomizedFieldsFromUser(BulletinFieldSpecs existingSpecs)
	{
		while(true)
		{
			UiCustomFieldsDlg inputDlg = new UiCustomFieldsDlg(mainWindow, existingSpecs);
			inputDlg.setFocusToInputField();
			inputDlg.setVisible(true);
			String newTopSectionCustomFieldXml = inputDlg.getTopSectionXml();
			if(newTopSectionCustomFieldXml == null)
				return null;
			String newBottomSectionCustomFieldXml = inputDlg.getBottomSectionXml();
			if(newBottomSectionCustomFieldXml == null)
				return null;
			
			if(newTopSectionCustomFieldXml.length() == 0 || newBottomSectionCustomFieldXml.length() == 0)
			{
				if(mainWindow.confirmDlg("UndoCustomFields"))
				{
					existingSpecs.setTopSectionSpecs(StandardFieldSpecs.getDefaultTopSetionFieldSpecs());
					existingSpecs.setBottomSectionSpecs(StandardFieldSpecs.getDefaultBottomSectionFieldSpecs());
				}					
			}
			else
			{
				FieldSpecCollection newTopSectionSpecs = null;
				FieldSpecCollection newBottomSectionSpecs = null;
				try
				{
					newTopSectionSpecs = FieldCollection.parseXml(newTopSectionCustomFieldXml);
					newBottomSectionSpecs = FieldCollection.parseXml(newBottomSectionCustomFieldXml);
				}
				catch(CustomFieldsParseException e)
				{
					e.printStackTrace();
				}
				BulletinFieldSpecs newFieldSpecs = new BulletinFieldSpecs();
				newFieldSpecs.setTopSectionSpecs(newTopSectionSpecs);
				newFieldSpecs.setBottomSectionSpecs(newBottomSectionSpecs);
				return newFieldSpecs;
			}
		}
	}
}
