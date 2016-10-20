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
package org.martus.client.swingui.dialogs;

import org.martus.client.swingui.ExternalPublicKeysTableModel;
import org.martus.client.swingui.HeadquartersManagementTableModel;
import org.martus.client.swingui.SelectableExternalPublicKeyEntry;
import org.martus.client.swingui.SelectableHeadquartersEntry;
import org.martus.client.swingui.UiMainWindow;
import org.martus.common.ExternalPublicKey;
import org.martus.common.HeadquartersKey;
import org.martus.common.HeadquartersKeys;
import org.martus.common.crypto.MartusCrypto;
import org.martus.swing.UiTable;
import org.martus.util.StreamableBase64.InvalidBase64Exception;


public class UiManageHeadquartersKeysDialog extends UiManageExternalPublicKeysDialog
{
	public UiManageHeadquartersKeysDialog(UiMainWindow owner) throws Exception
	{
		super(owner, owner.getLocalization().getWindowTitle("ConfigureHQs"));
		
	}

	@Override
	void addExistingKeysToTable() throws Exception
	{
		HeadquartersKeys local = mainWindow.getApp().getAllHQKeys();
		for(int i = 0; i<local.size();++i)
			addKeyToTable(local.get(i));
	}

	@Override
	protected UiTable createTable(ExternalPublicKeysTableModel hqModel)
	{
		UiTable newTable = super.createTable(hqModel);
		newTable.setMaxColumnWidthToHeaderWidth(0);
		return newTable;
	}
	
	@Override
	HeadquartersManagementTableModel createModel()
	{
		return new HeadquartersManagementTableModel(mainWindow.getApp());
	}

	@Override
	String getEditLabelButtonName()
	{
		return localization.getButtonLabel("ConfigureHQsReLabel");
	}

	@Override
	String[] getDialogText()
	{
		String[] dialogText = new String[]
		{
			localization.getFieldLabel("HQsSetAsProxyUploader"),
			localization.getFieldLabel("HQsSetAsDefault"),
			localization.getFieldLabel("ConfigureHQsCurrentHQs")
		};
		return dialogText;
	}
	
	@Override
	void notifyNoneSelected()
	{
		mainWindow.notifyDlg("NoHQsSelected");
	}
	
	@Override
	boolean confirmRemoveKey()
	{
		return mainWindow.confirmDlg("ClearHQInformation");
	};
	
	@Override
	void notifyKeyAlreadyExists()
	{
		mainWindow.notifyDlg("HQKeyAlradyExists");
	}
	
	@Override
	void addEntryToModel(SelectableExternalPublicKeyEntry entry)
	{
		getHeadquartersModel().addNewHeadQuarterEntry((SelectableHeadquartersEntry)entry);
	}

	@Override
	SelectableExternalPublicKeyEntry createSelectableEntry(ExternalPublicKey publicKey)
	{
		SelectableHeadquartersEntry entry = new SelectableHeadquartersEntry((HeadquartersKey)publicKey);
		return entry;
	}
	
	void updateConfigInfo()
	{
		enableDisableButtons();
		mainWindow.setAndSaveHQKeysInConfigInfo(getHeadquartersModel().getAllKeys(), getHeadquartersModel().getAllSelectedHeadQuarterKeys());
	}
	
	@Override
	HeadquartersKey createKeyWithLabel(String publicKeyString, String label)
	{
		return new HeadquartersKey(publicKeyString, label);
	}

	@Override
	boolean confirmPublicCode(String publicCode)
	{
		return confirmPublicCode(publicCode, "ImportPublicCode", "AccountCodeWrong");
	}

	@Override
	boolean confirmImportKey()
	{
		return mainWindow.confirmDlg("SetImportPublicKey");
	}

	@Override
	String getImportKeyDialogCategory()
	{
		String windowTitle = "ImportHeadquartersPublicKey";
		return windowTitle;
	}

	@Override
	String askUserForNewLabel(String publicCode, String previousValue)
	{
		String label = mainWindow.getStringInput("GetHQLabel", "", publicCode, previousValue);
		if(label == null)
			return null;
		return getUniqueLabel(publicCode, label);
	}

	private String getUniqueLabel(String publicCode, String label) 
	{
		HeadquartersKeys hQKeys = getHeadquartersModel().getAllKeys();
		for(int i = 0; i < hQKeys.size(); ++i)
		{
			HeadquartersKey hqKey = hQKeys.get(i);
			try 
			{
				if(hqKey.getPublicCode().equals(publicCode))
					continue;
			} 
			catch (InvalidBase64Exception e) 
			{
			}
			String hqConfiguredLabel = hqKey.getLabel();
			if(hqConfiguredLabel.length() >0 && label.equals(hqConfiguredLabel))
			{
				mainWindow.notifyDlg("HeadquarterLabelDuplicate");
				return null;
			}
		}
		return label;
	}
	
	boolean confirmPublicCode(String rawPublicCode, String baseTag, String errorBaseTag)
	{
		String userEnteredPublicCode = "";
		while(true)
		{
			userEnteredPublicCode = mainWindow.getStringInput(baseTag, "", "", userEnteredPublicCode);
			if(userEnteredPublicCode == null)
				return false; // user hit cancel
			String normalizedPublicCode = MartusCrypto.removeNonDigits(userEnteredPublicCode);

			if(rawPublicCode.equals(normalizedPublicCode))
				return true;

			mainWindow.notifyDlg(errorBaseTag);
		}
	}

	HeadquartersManagementTableModel getHeadquartersModel()
	{
		return (HeadquartersManagementTableModel) getModel();
	}
}
