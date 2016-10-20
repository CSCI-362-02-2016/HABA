/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2013, Beneficent
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
package org.martus.client.swingui.dialogs;

import org.martus.client.core.ConfigInfo;
import org.martus.client.swingui.ExternalPublicKeysTableModel;
import org.martus.client.swingui.FieldDeskManagementTableModel;
import org.martus.client.swingui.SelectableExternalPublicKeyEntry;
import org.martus.client.swingui.SelectableFieldDeskEntry;
import org.martus.client.swingui.UiMainWindow;
import org.martus.common.ExternalPublicKey;
import org.martus.common.FieldDeskKey;
import org.martus.common.FieldDeskKeys;
import org.martus.common.crypto.MartusCrypto;
import org.martus.util.StreamableBase64.InvalidBase64Exception;

public class UiManageFieldDeskKeysDialog extends UiManageExternalPublicKeysDialog
{
	public UiManageFieldDeskKeysDialog(UiMainWindow owner) throws Exception
	{
		super(owner, owner.getLocalization().getWindowTitle("ManageFieldDeskKeys"));
		
	}

	@Override
	void addExistingKeysToTable() throws Exception
	{
		String fieldDeskKeysXml = mainWindow.getApp().getConfigInfo().getFieldDeskKeysXml();
		FieldDeskKeys local = new FieldDeskKeys(fieldDeskKeysXml);
		for(int i = 0; i<local.size();++i)
			addKeyToTable(local.get(i));
	}

	@Override
	ExternalPublicKeysTableModel createModel()
	{
		return new FieldDeskManagementTableModel(mainWindow.getApp());
	}

	@Override
	String getEditLabelButtonName()
	{
		return localization.getButtonLabel("EditFieldDeskLabel");
	}

	@Override
	String[] getDialogText()
	{
		String[] dialogText = new String[]
		{
			localization.getFieldLabel("ConfigureFDsOverview")
		};
		return dialogText;
	}
	
	@Override
	void notifyNoneSelected()
	{
		mainWindow.notifyDlg("NoFieldDesksSelected");
	}
	
	@Override
	boolean confirmRemoveKey()
	{
		return mainWindow.confirmDlg("RemoveFieldDeskKeys");
	};
	
	@Override
	void notifyKeyAlreadyExists()
	{
		mainWindow.notifyDlg("FieldDeskKeyAlreadyExists");
	}
	
	@Override
	void addEntryToModel(SelectableExternalPublicKeyEntry entry)
	{
		getFieldDeskModel().addNewFieldDeskEntry((SelectableFieldDeskEntry)entry);
	}

	@Override
	SelectableExternalPublicKeyEntry createSelectableEntry(ExternalPublicKey publicKey)
	{
		SelectableFieldDeskEntry entry = new SelectableFieldDeskEntry((FieldDeskKey)publicKey);
		return entry;
	}
	
	void updateConfigInfo()
	{
		enableDisableButtons();
		String fieldDeskKeysXml = getFieldDeskModel().getAllKeys().toStringWithLabel();
		ConfigInfo configInfo = mainWindow.getApp().getConfigInfo();
		configInfo.setFieldDeskKeysXml(fieldDeskKeysXml);
		mainWindow.saveConfigInfo();
	}
	
	@Override
	String getImportKeyDialogCategory()
	{
		return "ImportFieldDeskPublicKey";
	}

	@Override
	boolean confirmPublicCode(String publicCode)
	{
		return confirmPublicCode(publicCode, "ImportPublicKey", "AccountCodeWrong");
	}

	@Override
	boolean confirmImportKey()
	{
		return mainWindow.confirmDlg("ImportFieldDeskPublicKey");
	}

	@Override
	ExternalPublicKey createKeyWithLabel(String publicKeyString, String label)
	{
		return new FieldDeskKey(publicKeyString, label);
	}

	@Override
	String askUserForNewLabel(String publicCode, String previousValue)
	{
		String label = mainWindow.getStringInput("GetFieldDeskLabel", "", publicCode, previousValue);
		if(label == null)
			return null;
		return getUniqueLabel(publicCode, label);
	}

	private String getUniqueLabel(String publicCode, String label) 
	{
		FieldDeskKeys keys = getFieldDeskModel().getAllKeys();
		for(int i = 0; i < keys.size(); ++i)
		{
			FieldDeskKey key = keys.get(i);
			try 
			{
				if(key.getPublicCode().equals(publicCode))
					continue;
			} 
			catch (InvalidBase64Exception e) 
			{
			}
			String configuredLabel = key.getLabel();
			if(configuredLabel.length() >0 && label.equals(configuredLabel))
			{
				mainWindow.notifyDlg("FieldDeskLabelDuplicate");
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

	FieldDeskManagementTableModel getFieldDeskModel()
	{
		return (FieldDeskManagementTableModel) getModel();
	}

}
