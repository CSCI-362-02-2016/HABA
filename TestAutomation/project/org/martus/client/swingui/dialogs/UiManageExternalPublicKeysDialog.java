package org.martus.client.swingui.dialogs;
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.martus.client.swingui.ExternalPublicKeysTableModel;
import org.martus.client.swingui.SelectableExternalPublicKeyEntry;
import org.martus.client.swingui.UiMainWindow;
import org.martus.client.swingui.filefilters.PublicInfoFileFilter;
import org.martus.clientside.UiLocalization;
import org.martus.common.ExternalPublicKey;
import org.martus.common.HeadquartersKeys;
import org.martus.common.MartusLogger;
import org.martus.common.crypto.MartusCrypto;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTable;
import org.martus.swing.UiVBox;
import org.martus.swing.UiWrappedTextArea;
import org.martus.swing.Utilities;

abstract public class UiManageExternalPublicKeysDialog extends JDialog
{
	public UiManageExternalPublicKeysDialog(UiMainWindow owner, String title) throws Exception
	{
		super(owner);
		setTitle(title);
		setModal(true);

		mainWindow = owner;
		localization = mainWindow.getLocalization();

		JButton add = new UiButton(localization.getButtonLabel("ConfigureHQsAdd"));
		add.addActionListener(new AddHandler());
		remove = new UiButton(localization.getButtonLabel("ConfigureHQsRemove"));
		remove.addActionListener(new RemoveHandler());
		renameLabel = new UiButton(getEditLabelButtonName());
		renameLabel.addActionListener(createRenameHandler());
		view = new UiButton(localization.getButtonLabel("ConfigurePublicKeysView"));
		view.addActionListener(createViewHandler());

		String[] dialogText = getDialogText();
		UiVBox vBox = new UiVBox();
		for (String text : dialogText)
		{
			vBox.addCentered(new UiWrappedTextArea(text));
			vBox.addSpace();
		}

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10,10,10,10));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(vBox);
		
		model = createModel();
		table = createTable(model);
		
		addExistingKeysToTable();
		enableDisableButtons();
		
		UiScrollPane scroller = new UiScrollPane(table);
		panel.add(scroller);
		panel.add(new UiLabel(" "));
		
		Box hBox = Box.createHorizontalBox();
		JButton save = new UiButton(localization.getButtonLabel("save"));
		save.addActionListener(createSaveHandler());
		JButton cancel = new UiButton(localization.getButtonLabel("cancel"));
		cancel.addActionListener(createCancelHandler());
		Utilities.addComponentsRespectingOrientation(hBox, new Component[]{add,remove,renameLabel,view,Box.createHorizontalGlue(),save,cancel});
		panel.add(hBox);
		
		getContentPane().add(panel);
		getRootPane().setDefaultButton(cancel);
		Utilities.centerDlg(this);
		setResizable(true);
	}

	abstract String getEditLabelButtonName();
	abstract String[] getDialogText();
	abstract ExternalPublicKeysTableModel createModel();
	abstract String getImportKeyDialogCategory();
	abstract boolean confirmPublicCode(String publicCode);
	abstract boolean confirmImportKey();
	abstract ExternalPublicKey createKeyWithLabel(String publicKeyString, String label);
	abstract void addExistingKeysToTable() throws Exception;
	abstract void addEntryToModel(SelectableExternalPublicKeyEntry entry);
	abstract SelectableExternalPublicKeyEntry createSelectableEntry(ExternalPublicKey publicKey);
	abstract void updateConfigInfo();
	abstract String askUserForNewLabel(String publicCode, String previousValue);
	abstract void notifyNoneSelected();
	abstract boolean confirmRemoveKey();
	abstract void notifyKeyAlreadyExists();
	
	ViewHandler createViewHandler()
	{
		return new ViewHandler();
	}

	RenameHandler createRenameHandler()
	{
		return new RenameHandler();
	}

	SaveHandler createSaveHandler()
	{
		return new SaveHandler();
	}

	CancelHandler createCancelHandler()
	{
		return new CancelHandler();
	}

	protected UiTable createTable(ExternalPublicKeysTableModel modelToUse) 
	{
		UiTable newTable = new UiTable(modelToUse);
		newTable.setRenderers(modelToUse);
		newTable.createDefaultColumnsFromModel();
		newTable.addKeyListener(new TableListener());
		newTable.setColumnSelectionAllowed(false);
		newTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		newTable.setShowGrid(true);
		newTable.resizeTable(DEFAULT_VIEABLE_ROWS);
		
		return newTable;
	}
	
	ExternalPublicKeysTableModel getModel()
	{
		return model;
	}

	void enableDisableButtons()
	{
		boolean enableButtons = false;
		if(table.getRowCount()>0)
			enableButtons = true;
		remove.setEnabled(enableButtons);
		renameLabel.setEnabled(enableButtons);
		view.setEnabled(enableButtons);
	}
	
	ExternalPublicKey importPublicKey() throws Exception
	{
		FileFilter filter = createFileFilter();
		File importFile = mainWindow.doFileOpenDialogWithDirectoryMemory(getImportKeyDialogCategory(), filter);
		if(importFile == null)
			return null;
		
		String publicKeyString = mainWindow.getApp().extractPublicInfo(importFile);
		String publicCode = MartusCrypto.computePublicCode(publicKeyString);
		if(doesPublicCodeAlreadyExist(publicKeyString))
		{
			notifyKeyAlreadyExists();
			return null;
		}

		if(confirmPublicCode(publicCode))
		{
			if(!confirmImportKey())
				return null;
		}
		else
			return null;
		String label = askUserForNewLabel(MartusCrypto.computeFormattedPublicCode(publicKeyString), "");
		return createKeyWithLabel(publicKeyString, label);
	}

	private PublicInfoFileFilter createFileFilter()
	{
		return new PublicInfoFileFilter(mainWindow.getLocalization());
	}

	void addKeyToTable(ExternalPublicKey publicKey)
	{
		SelectableExternalPublicKeyEntry entry = createSelectableEntry(publicKey);
		HeadquartersKeys defaultHQKeys = mainWindow.getApp().getDefaultHQKeysWithFallback();
		boolean isDefault = defaultHQKeys.containsKey(publicKey.getPublicKey());
		entry.setSelected(isDefault);
		addEntryToModel(entry);
	}

	private boolean doesPublicCodeAlreadyExist(String publicKeyString)
	{
		for(int i = 0; i < table.getRowCount(); ++i)
		{
			ExternalPublicKey rowKey = model.getPublicKey(i);
			String rowKeyString = rowKey.getPublicKey();
			if(rowKeyString.equals(publicKeyString))
			{
				return true;
			}
		}
		
		return false;
	}

	class TableListener implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() ==  KeyEvent.VK_TAB && !e.isControlDown())
			{
				e.consume();
				if(e.isShiftDown())
					table.transferFocusBackward();
				else 
					table.transferFocus();
			}
		}

		public void keyReleased(KeyEvent e)
		{
		}

		public void keyTyped(KeyEvent e)
		{
		}
	}

	class AddHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			try
			{
				ExternalPublicKey publicKey = importPublicKey();
				if(publicKey==null)
					return;
				addKeyToTable(publicKey);
			}
			catch (Exception e)
			{
				mainWindow.notifyDlg("PublicInfoFileError");
			}
		}
	}

	class CancelHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			dispose();
		}
	}
	
	class SaveHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			updateConfigInfo();
			dispose();
		}
	}
	
	class RemoveHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			if(table.getSelectedRowCount()==0)
			{
				notifyNoneSelected();
				return;
			}
			if(!confirmRemoveKey())
				return;
			
			int rowCount = model.getRowCount();
			for(int i = rowCount-1; i >=0 ; --i)
			{
				if(table.isRowSelected(i))
					model.removeRow(i);
			}
		}
	}

	class RenameHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			if(table.getSelectedRowCount()==0)
			{
				notifyNoneSelected();
				return;
			}
			int rowCount = model.getRowCount();
			for(int i = rowCount-1; i >=0 ; --i)
			{
				if(table.isRowSelected(i))
				{
					String newLabel = askUserForNewLabel(model.getPublicCode(i), model.getLabel(i));
					if(newLabel== null)
						break;
					getModel().setLabel(i, newLabel);
				}
			}
		}

	}
	
	class ViewHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(table.getSelectedRowCount()==0)
			{
				notifyNoneSelected();
				return;
			}
			int rowCount = model.getRowCount();
			for(int i = rowCount-1; i >=0 ; --i)
			{
				if(table.isRowSelected(i))
				{
					ExternalPublicKey key = model.getPublicKey(i);
					Map<String, String> tokenReplacement = new HashMap<String, String>();
					tokenReplacement.put("#Label#", key.getLabel());
					String publicCode = mainWindow.getLocalization().getFieldLabel("Unknown");
					try
					{
						publicCode = key.getPublicCode();
					}
					catch(Exception e)
					{
						MartusLogger.logException(e);
						mainWindow.unexpectedErrorDlg();
					}
					tokenReplacement.put("#PublicCode#", publicCode);
					tokenReplacement.put("#PublicKey#", key.getPublicKey());
					mainWindow.notifyDlg("ViewKeyDetails", tokenReplacement);
				}
			}
		}
		
	}
	

	private static final int DEFAULT_VIEABLE_ROWS = 5;

	UiMainWindow mainWindow;
	UiTable table;
	UiLocalization localization;

	ExternalPublicKeysTableModel model;
	private JButton remove;
	private JButton renameLabel;
	private JButton view;
}
