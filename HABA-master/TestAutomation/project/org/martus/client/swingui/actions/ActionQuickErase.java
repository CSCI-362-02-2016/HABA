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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.martus.client.core.MartusApp;
import org.martus.client.swingui.MartusLocalization;
import org.martus.client.swingui.UiMainWindow;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.swing.UiVBox;
import org.martus.swing.UiWrappedTextArea;
import org.martus.swing.Utilities;

public abstract class ActionQuickErase extends UiMenuAction 
{
	protected ActionQuickErase (UiMainWindow mainWindowToUse, String tag)
	{
		super(mainWindowToUse, tag);
		app = mainWindowToUse.getApp();
		martusAccounts = new Vector();
	}
	
	protected boolean confirmQuickErase(boolean uninstallAsWell)
	{
		if(!mainWindow.reSignIn())
			return false;
		if(!checkAndConfirmUnsentBulletins())
			return false;
		if(!confirmErase(uninstallAsWell))
			return false;
		return true;
	}

	protected void prepareAndDeleteMyData()
	{
		prepareToExitMartus();
		deleteAndScrubMyAccountsKeyPairAndRelatedFiles();
		eraseMyPacketData();
	}

	protected void exitMartus()
	{
		mainWindow.exitWithoutSavingState();
	}
	
	private boolean checkAndConfirmUnsentBulletins()
	{
		if(!app.isSealedOutboxEmpty() || !app.isDraftOutboxEmpty())
		{				
			if (!mainWindow.confirmDlgBeep("QuickEraseOutboxNotEmpty"))
				return false;
		}
		return true;
	}	

	private boolean confirmErase(boolean uninstall)
	{
		ConfirmQuickEraseDlg confirm = new ConfirmQuickEraseDlg(uninstall);
		return confirm.okPressed();
	}

	private void prepareToExitMartus()
	{
		mainWindow.prepareToExitMartus();
	}

	private void deleteAndScrubMyAccountsKeyPairAndRelatedFiles()
	{
		app.deleteKeypairAndRelatedFilesForAccount(app.getCurrentAccountDirectory());
	}

	private void eraseMyPacketData()
	{	
		app.deleteAllBulletinsAndUserFolders();
		mainWindow.allFolderContentsHaveChanged();
		mainWindow.folderTreeContentsHaveChanged();		
	}
	
	private class ConfirmQuickEraseDlg extends JDialog implements ActionListener
	{
		ConfirmQuickEraseDlg(boolean uninstallMartus)
		{
			super(mainWindow, "", true);
			uninstallChoosen = uninstallMartus;
			martusAccounts = app.getAllAccountDirectories();
			MartusLocalization localization = mainWindow.getLocalization();
			if(uninstallMartus)
				setTitle(localization.getWindowTitle("RemoveMartsFromThisComputer"));
			else
				setTitle(localization.getWindowTitle("DeleteMyDataFromThisComputer"));

			JPanel warningLabelPanel = new JPanel();
			warningLabelPanel.setForeground(Color.YELLOW);
			warningLabelPanel.setBackground(Color.YELLOW);
			warningLabelPanel.setBorder(new LineBorder(Color.BLACK, 5));
			String warningMsg = "<html><p align='center'><font size=8><b>" +
				localization.getFieldLabel("RemoveMartusFromSystemWarning") + 
				"</b</font></p></html>";
			JLabel warningLabel = new UiLabel(warningMsg);
			warningLabel.setBackground(Color.YELLOW);
			warningLabel.setForeground(Color.BLACK);
			warningLabelPanel.add(warningLabel);
			
			UiVBox vBox = new UiVBox();
			vBox.addCentered(warningLabelPanel);

			String warningMessage = localization.getFieldLabel("QuickEraseWillNotRemoveItems");
			warningMessage += "\n\n";
			warningMessage += localization.getFieldLabel("QuickEraseFollowingItems");
			warningMessage += "\n* ";
			warningMessage += localization.getFieldLabel("QuickEraseWillRemoveItems");
			if(uninstallMartus)
			{
				warningMessage += "\n* ";
				warningMessage += localization.getFieldLabel("RemoveMartusWillUninstall");
				if(martusAccounts.size() > 1)
				{
					warningMessage += "\n* ";
					warningMessage += localization.getFieldLabel("RemoveMartusWillRemoveAllOtherAccounts");
					JPanel multipleAccountPanel = new JPanel();
					multipleAccountPanel.setBorder(new LineBorder(Color.RED, 5));
					multipleAccountPanel.setBackground(Color.WHITE);
					
					String multipleMsg = "<html><p align='center'><font size=4><b>" +
							localization.getFieldLabel("RemoveMartusFromSystemMultipleAccountsWarning1") +
							"<br></br>"+
							localization.getFieldLabel("RemoveMartusFromSystemMultipleAccountsWarning2") +
							"</b</font></p></html>";
					JLabel multipleAccountsText = new UiLabel(multipleMsg);
					multipleAccountsText.setBackground(Color.WHITE);
					multipleAccountsText.setForeground(Color.BLACK);
					multipleAccountPanel.add(multipleAccountsText);
					vBox.addCentered(multipleAccountPanel);
				}
				warningMessage += "\n* ";
				warningMessage += localization.getFieldLabel("RemoveMartusWillDeleteMartusDirectory");
			}
			warningMessage += "\n* ";
			warningMessage += localization.getFieldLabel("QuickEraseWillExitMartus");
			
			UiWrappedTextArea text = new UiWrappedTextArea(warningMessage);
			text.setBackground(Color.WHITE);
			text.setForeground(Color.BLACK);
			text.setEditable(false);
			
			okButton = new UiButton(localization.getButtonLabel("ok"));
			okButton.addActionListener(this);
			
			JButton cancelButton = new UiButton(localization.getButtonLabel("cancel"));
			cancelButton.addActionListener(this);

			Box hBox = Box.createHorizontalBox();
			Utilities.addComponentsRespectingOrientation(hBox, new Component[] {okButton,Box.createHorizontalGlue(),cancelButton});
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.setBorder(new LineBorder(Color.RED, 20));
			panel.add(vBox, BorderLayout.CENTER);
			
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(panel, BorderLayout.NORTH);
			getContentPane().add(text, BorderLayout.CENTER);
			getContentPane().add(hBox, BorderLayout.SOUTH);
			
			Utilities.centerDlg(this);
			setResizable(true);
			setVisible(true);
		}

		public void actionPerformed(ActionEvent ae)
		{
			if(okButton.hasFocus())
			{
				if(uninstallChoosen)
				{
					if(mainWindow.confirmDlgBeep("RemoveMartus"))
						okPressed = true;
				}
				else
				{
					if(mainWindow.confirmDlgBeep("DeleteMyData"))
						okPressed = true;
				}
			}
			dispose();
		}

		public boolean okPressed()
		{
			return okPressed;
		}


		private JButton okButton;
		private boolean okPressed;
		private boolean uninstallChoosen;
	}


	final boolean WILL_UNINSTALL_MARTUS = true;
	final boolean WILL_NOT_UNINSTALL_MARTUS = false;
	MartusApp app;
	Vector martusAccounts;
}
