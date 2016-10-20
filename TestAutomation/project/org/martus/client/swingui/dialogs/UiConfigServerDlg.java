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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.martus.client.core.ConfigInfo;
import org.martus.client.core.MartusApp;
import org.martus.client.swingui.UiMainWindow;
import org.martus.clientside.UiLocalization;
import org.martus.common.MartusLogger;
import org.martus.common.crypto.MartusCrypto;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.swing.UiParagraphPanel;
import org.martus.swing.UiTextField;
import org.martus.swing.Utilities;
import org.martus.util.StreamableBase64.InvalidBase64Exception;

public class UiConfigServerDlg extends JDialog implements ActionListener
{
	public UiConfigServerDlg(UiMainWindow owner, ConfigInfo infoToUse)
	{
		super(owner, "", true);
		serverPublicKey = "";

		info = infoToUse;
		mainWindow = owner;
		app = owner.getApp();
		UiLocalization localization = mainWindow.getLocalization();
		
		setTitle(localization.getWindowTitle("ConfigServer"));
		fieldIPAddress = new UiTextField(25);
		fieldPublicCode = new UiTextField(25);

		UiParagraphPanel panel = new UiParagraphPanel();
		panel.addComponents(new UiLabel(localization.getFieldLabel("ServerNameEntry")), fieldIPAddress);
		serverIPAddress = info.getServerName();
		fieldIPAddress.setText(serverIPAddress);
		fieldIPAddress.requestFocus();

		panel.addComponents(new UiLabel(localization.getFieldLabel("ServerPublicCodeEntry")), fieldPublicCode);
		String knownServerPublicKey = info.getServerPublicKey();
		String knownServerPublicCode = "";
		try
		{
			if(knownServerPublicKey.length() > 0)
				knownServerPublicCode = MartusCrypto.computeFormattedPublicCode(knownServerPublicKey);
		}
		catch (InvalidBase64Exception e)
		{
		}
		fieldPublicCode.setText(knownServerPublicCode);

		panel.addBlankLine();

		ok = new UiButton(localization.getButtonLabel("ok"));
		ok.addActionListener(this);
		JButton cancel = new UiButton(localization.getButtonLabel("cancel"));
		cancel.addActionListener(this);
		panel.addComponents(ok, cancel);

		getContentPane().add(panel);
		getRootPane().setDefaultButton(ok);
		Utilities.centerDlg(this);
		setResizable(true);
		setVisible(true);
	}

	public boolean getResult()
	{
		return result;
	}

	public String getServerIPAddress()
	{
		return serverIPAddress;
	}

	public String getServerPublicKey()
	{
		return serverPublicKey;
	}

	public void actionPerformed(ActionEvent ae)
	{
		result = false;
		if(ae.getSource() == ok)
		{
			String name = fieldIPAddress.getText();
			String publicCode = fieldPublicCode.getText();
			if(!ValidateInformation(name, publicCode))
				return;
			result = true;
		}
		dispose();
	}

	private boolean ValidateInformation(String serverName, String userEnteredPublicCode)
	{
		if(serverName.length() == 0)
			return errorMessage("InvalidServerName");

		String normalizedPublicCode = MartusCrypto.removeNonDigits(userEnteredPublicCode);
		if(normalizedPublicCode.length() == 0)
			return errorMessage("InvalidServerCode");

		String serverKey = null;
		String serverPublicCode = null;
		try
		{
			if(!app.isNonSSLServerAvailable(serverName))
				return errorMessage("ConfigNoServer");

			serverKey = app.getServerPublicKey(serverName);
			serverPublicCode = MartusCrypto.computePublicCode(serverKey);
		}
		catch(Exception e)
		{
			MartusLogger.logException(e);
			return errorMessage("ServerInfoInvalid");
		}
		if(!serverPublicCode.equals(normalizedPublicCode))
			return errorMessage("ServerCodeWrong");

		serverIPAddress = serverName;
		serverPublicKey = serverKey;
		return true;
	}

	private boolean errorMessage(String messageTag)
	{
		mainWindow.notifyDlg(messageTag);
		return false;
	}


	MartusApp app;
	UiMainWindow mainWindow;
	ConfigInfo info;

	JButton ok;
	UiTextField fieldIPAddress;
	UiTextField fieldPublicCode;

	String serverIPAddress;
	String serverPublicKey;

	boolean result;
}
