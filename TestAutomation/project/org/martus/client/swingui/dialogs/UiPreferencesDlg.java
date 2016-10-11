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
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.martus.client.core.FontSetter;
import org.martus.client.core.ZawgyiLabelUtilities;
import org.martus.client.swingui.MartusLocalization;
import org.martus.client.swingui.UiFontEncodingHelper;
import org.martus.client.swingui.UiMainWindow;
import org.martus.client.swingui.fields.UiChoiceEditor;
import org.martus.clientside.MtfAwareLocalization;
import org.martus.clientside.UiLocalization;
import org.martus.common.MiniLocalization;
import org.martus.common.fieldspec.ChoiceItem;
import org.martus.swing.FontHandler;
import org.martus.swing.UiButton;
import org.martus.swing.UiCheckBox;
import org.martus.swing.UiLabel;
import org.martus.swing.UiParagraphPanel;
import org.martus.swing.Utilities;



public class UiPreferencesDlg extends JDialog implements ActionListener
{
	public UiPreferencesDlg(UiMainWindow mainWindow)
	{
		super(mainWindow, "", true);
		owner = mainWindow;
		UiLocalization localization = owner.getLocalization();
		
		setTitle(localization.getMenuLabel("Preferences"));

		languageDropdown = new UiChoiceEditor(localization);
		languageDropdown.setChoices(localization.getUiLanguages());
		languageDropdown.setText(localization.getCurrentLanguageCode());
		languageDropdown.addActionListener(new LanguageChangedHandler());
		
		ChoiceItem[] mdyChoices = new ChoiceItem[] {
			new ChoiceItem("ymd", buildMdyLabel("ymd")),
			new ChoiceItem("mdy", buildMdyLabel("mdy")),
			new ChoiceItem("dmy", buildMdyLabel("dmy")),
		};
		mdyDropdown = new UiChoiceEditor(localization);
		mdyDropdown.setChoices(mdyChoices);
		mdyDropdown.setText(localization.getMdyOrder());
		
		ChoiceItem[] delimiterChoices = new ChoiceItem[] {
			new ChoiceItem("/", localization.getFieldLabel("DateDelimiterSlash")),
			new ChoiceItem("-", localization.getFieldLabel("DateDelimiterDash")),
			new ChoiceItem(".", localization.getFieldLabel("DateDelimiterDot")),
		};
		delimiterDropdown = new UiChoiceEditor(localization);
		delimiterDropdown.setChoices(delimiterChoices);
		delimiterDropdown.setText("" + localization.getDateDelimiter());
		
		ChoiceItem[] calendarChoices = localization.getAvailableCalendarSystems();
		calendarDropdown = new UiChoiceEditor(localization);
		calendarDropdown.setChoices(calendarChoices);
		calendarDropdown.setText(localization.getCurrentCalendarSystem());
		
		adjustThai = new UiCheckBox();
		adjustThai.setText(localization.getFieldLabel("preferencesAdjustThai"));
		adjustThai.setSelected(localization.getAdjustThaiLegacyDates());
		
		adjustPersian = new UiCheckBox();
		adjustPersian.setText(localization.getFieldLabel("preferencesAdjustPersian"));
		adjustPersian.setSelected(localization.getAdjustPersianLegacyDates());

		useZawgyiFont = new UiCheckBox();
		useZawgyiFont.setText(localization.getFieldLabel("preferencesUseZawgyi"));
		useZawgyiFont.setSelected(owner.getUseZawgyiFont());
		determineStateOfZawgyiCheckbox(localization.getCurrentLanguageCode());
		
		allPrivate = new UiCheckBox();
		allPrivate.setText(localization.getFieldLabel("preferencesAllPrivate"));
		allPrivate.setSelected(owner.getBulletinsAlwaysPrivate());
		
		checkFieldOfficeBulletins = new UiCheckBox();
		checkFieldOfficeBulletins.setText(localization.getFieldLabel("preferencesCheckFieldOfficeBulletins"));
		checkFieldOfficeBulletins.setSelected(owner.getCheckFieldOfficeBulletins());
		
		useInternalTor = new UiCheckBox();
		useInternalTor.setText(localization.getFieldLabel("PreferencesUseInternalTor"));
		useInternalTor.setSelected(owner.getUseInternalTor());

		UiParagraphPanel preferences = new UiParagraphPanel();
		preferences.addComponents(new UiLabel(localization.getFieldLabel("language")), languageDropdown.getComponent());
		preferences.addComponents(new UiLabel(localization.getFieldLabel("mdyOrder")), mdyDropdown.getComponent());
		preferences.addComponents(new UiLabel(localization.getFieldLabel("DateDelimiter")), delimiterDropdown.getComponent());
		preferences.addComponents(new UiLabel(localization.getFieldLabel("CalendarSystem")), calendarDropdown.getComponent());
		preferences.addOnNewLine(adjustThai);
		preferences.addOnNewLine(adjustPersian);
		preferences.addOnNewLine(useZawgyiFont);

		preferences.addBlankLine();
		preferences.addOnNewLine(allPrivate);
		preferences.addOnNewLine(checkFieldOfficeBulletins);
		preferences.addOnNewLine(useInternalTor);
		preferences.addBlankLine();
		
		ok = new UiButton(localization.getButtonLabel("ok"));
		ok.addActionListener(this);
		cancel = new UiButton(localization.getButtonLabel("cancel"));
		cancel.addActionListener(this);
		preferences.addComponents(ok, cancel);
		
		getContentPane().add(preferences);
		getRootPane().setDefaultButton(ok);
		
		Utilities.centerDlg(this);
		setResizable(true);
	}
	
	public boolean getResult()
	{
		return result;
	}
	
	public boolean isAllPrivateChecked()
	{
		return allPrivate.isSelected();
	}
	
	public boolean isCheckFieldOfficeBulletinsChecked()
	{
		return checkFieldOfficeBulletins.isSelected();
	}

    public boolean isUseZawgyiFont()
    {
        return useZawgyiFont.isSelected();
    }
	
	public boolean isUseInternalTorChecked()
	{
		return useInternalTor.isSelected();
	}

	private String buildMdyLabel(String mdyOrder)
	{
		MiniLocalization localization = owner.getLocalization();
		Vector dateParts = new Vector(); 
		for(int i = 0; i < mdyOrder.length(); ++i)
		{
			String tag = "Unknown";
			char thisPart = mdyOrder.charAt(i);
			switch(thisPart)
			{
				case 'y': tag = "Year"; break;
				case 'm': tag = "Month"; break;
				case 'd': tag = "Day"; break;
			}
			dateParts.add(" ");
			String translated = localization.getFieldLabel("DatePart" + tag);
			
			String storableSinceThisIsInADropdown = new UiFontEncodingHelper(FontHandler.isDoZawgyiConversion()).getStorable(translated);
			dateParts.add(storableSinceThisIsInADropdown);
		}
		dateParts.add(" ");

		String label = Utilities.createStringRespectingOrientation(dateParts);
		return label;
	}


	public void actionPerformed(ActionEvent ae)
	{
		
		if(ae.getSource() == ok)
		{
			MartusLocalization localization = owner.getLocalization();
			String languageCodeSelected = languageDropdown.getText();
			FontSetter.setDefaultFont(isUseZawgyiFont());
			UiMainWindow.displayDefaultUnofficialTranslationMessageIfNecessary(owner.getCurrentActiveFrame(), localization, languageCodeSelected);
			UiMainWindow.displayIncompatibleMtfVersionWarningMessageIfNecessary(owner.getCurrentActiveFrame(), localization, languageCodeSelected);
			localization.setMdyOrder(mdyDropdown.getText());
			String delimiter = delimiterDropdown.getText();
			localization.setDateDelimiter(delimiter.charAt(0));
			localization.setCurrentCalendarSystem(calendarDropdown.getText());
			localization.setCurrentLanguageCode(languageDropdown.getText());
			localization.setAdjustThaiLegacyDates(adjustThai.isSelected());
			localization.setAdjustPersianLegacyDates(adjustPersian.isSelected());
			result = true;
		}
		dispose();
	}

	class LanguageChangedHandler implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				String languageCode = languageDropdown.getText();
				determineStateOfZawgyiCheckbox(languageCode);
			}
		}

	private void determineStateOfZawgyiCheckbox(String languageCode)
	{
		if (languageCode.equals(MtfAwareLocalization.BURMESE))
		{
			useZawgyiFont.setSelected(true);
		}
	}


	UiMainWindow owner;
	UiChoiceEditor languageDropdown;
	private UiChoiceEditor mdyDropdown;
	private UiChoiceEditor delimiterDropdown;
	private UiChoiceEditor calendarDropdown;
	private UiCheckBox adjustThai;
	private UiCheckBox adjustPersian;
	private UiCheckBox useZawgyiFont;
	private UiCheckBox allPrivate;
	private UiCheckBox checkFieldOfficeBulletins;
	private UiCheckBox useInternalTor;
	private JButton ok;
	private JButton cancel;
	
	private boolean result;
}
