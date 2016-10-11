/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2006, Beneficent
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
package org.martus.client.swingui.fields;

import org.martus.client.swingui.MartusLocalization;
import org.martus.client.swingui.UiMainWindow;
import org.martus.client.swingui.dialogs.UiDialogLauncher;
import org.martus.common.bulletin.BulletinConstants;
import org.martus.common.fieldspec.DateFieldSpec;
import org.martus.common.fieldspec.DateRangeFieldSpec;
import org.martus.common.fieldspec.DropDownFieldSpec;
import org.martus.common.fieldspec.FieldSpec;
import org.martus.common.fieldspec.GridFieldSpec;

public class UiEditableFieldCreator extends UiFieldCreator
{
	public UiEditableFieldCreator(UiMainWindow mainWindowToUse, UiFieldContext contextToUse)
	{
		super(mainWindowToUse, contextToUse);
	}

	public UiField createUnknownField(FieldSpec spec)
	{
		return new UiUnknownViewer(getLocalization());
	}
	
	public UiField createNormalField(FieldSpec spec)
	{
		return new UiNormalTextEditor(getLocalization(), mainWindow.getEditingTextFieldColumns());
	}

	public UiField createMultilineField(FieldSpec spec)
	{
		return new UiMultilineTextEditor(getLocalization(), mainWindow.getEditingTextFieldColumns());
	}

	public UiField createMessageField(FieldSpec spec)
	{
		return new UiMessageField(spec, mainWindow.getEditingTextFieldColumns(), mainWindow);
	}

	public UiField createChoiceField(DropDownFieldSpec spec)
	{
		UiChoiceEditor dropDownField = new UiChoiceEditor(mainWindow.getLocalization());
		dropDownField.setSpec(getContext(), spec);
		return dropDownField;
	}
	
	public UiField createLanguageField(DropDownFieldSpec spec)
	{
		if(isBulletinLanguageField(spec))
		{
			UiChoiceEditor dropDownField = new UiBulletinLanguageChoiceEditor(mainWindow.getLocalization());
			dropDownField.setSpec(getContext(), spec);
			return dropDownField;
		}
		
		return createChoiceField(spec);
	}

	private boolean isBulletinLanguageField(DropDownFieldSpec spec)
	{
		boolean isTopLevelField = spec.getParent() == null;
		if(!isTopLevelField)
			return false;
		
		String tag = spec.getTag();
		if(!tag.equals(BulletinConstants.TAGLANGUAGE))
			return false;
		
		return true;
	}
	
	public UiField createDateField(FieldSpec spec)
	{
		return new UiDateEditor((DateFieldSpec) spec, getLocalization());
	}
	
	public UiField createFlexiDateField(DateRangeFieldSpec spec)
	{
		return new UiFlexiDateEditor(getLocalization(), spec);	
	}

	public UiField createBoolField(FieldSpec spec)
	{
		return new UiBoolEditor(getLocalization());
	}

	public UiField createGridField(GridFieldSpec fieldSpec)
	{
		MartusLocalization localization = mainWindow.getLocalization();
		fieldSpec.setColumnZeroLabel(localization.getFieldLabel("ColumnGridRowNumber"));
		UiDialogLauncher dlgLauncher = new UiDialogLauncher(mainWindow, mainWindow.getCurrentActiveFrame());
		UiGrid gridEditor = new UiGridEditor(mainWindow, fieldSpec, dlgLauncher, getContext(), mainWindow.getEditingTextFieldColumns());
		return gridEditor;
	}

}
