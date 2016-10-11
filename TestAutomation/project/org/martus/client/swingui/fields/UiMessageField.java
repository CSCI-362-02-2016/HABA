/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2005-2007, Beneficent
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
package org.martus.client.swingui.fields;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.martus.client.swingui.UiMainWindow;
import org.martus.common.MiniLocalization;
import org.martus.common.fieldspec.FieldSpec;
import org.martus.common.fieldspec.MessageFieldSpec;
import org.martus.common.utilities.BurmeseUtilities;
import org.martus.swing.UiWrappedTextArea;

public class UiMessageField extends UiViewerField
{
	public UiMessageField(FieldSpec spec, int numberColumns, UiMainWindow mainWindow)
	{
		super(mainWindow.getLocalization());
		String message = ((MessageFieldSpec)(spec)).getMessage();
		if (mainWindow.getDoZawgyiConversion())
			message = BurmeseUtilities.getDisplayable(message);
		widget = new UiWrappedTextArea(message, numberColumns);
		widget.setEditable(false);
		widget.setBackground(new JPanel().getBackground());
	}

	public JComponent getComponent()
	{
		return widget;
	}

	public String getText()
	{
		 return widget.getText();
	}

	public void setText(String newText)
	{
	}
	
	UiWrappedTextArea widget;
}
