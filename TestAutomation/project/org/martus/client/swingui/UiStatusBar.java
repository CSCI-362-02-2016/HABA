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

package org.martus.client.swingui;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.martus.swing.UiLanguageDirection;
import org.martus.swing.Utilities;

public class UiStatusBar extends JPanel
{

	public UiStatusBar(MartusLocalization localization)
	{
		super();
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS) );
		setComponentOrientation(UiLanguageDirection.getComponentOrientation());

		Box statusBarBox = Box.createHorizontalBox();
		backgroundProgressMeter = new UiProgressMeter(null, localization);
		backgroundProgressMeter.hideProgressMeter();
		torProgressMeter = new UiProgressMeter(null, localization);
		torProgressMeter.hideProgressMeter();
		Utilities.addComponentsRespectingOrientation(statusBarBox, new Component[]{backgroundProgressMeter, Box.createHorizontalGlue(), torProgressMeter});
		add(statusBarBox);
	}
	
	public UiProgressMeter getBackgroundProgressMeter()
	{
		return backgroundProgressMeter;
	}
	
	public UiProgressMeter getTorProgressMeter()
	{
		return torProgressMeter;
	}
	
	public void setStatusMessageTag(String tag)
	{
		UiProgressMeter r = getBackgroundProgressMeter();	
		r.setStatusMessage(tag);
		r.hideProgressMeter();
	}

	private UiProgressMeter backgroundProgressMeter;
	private UiProgressMeter torProgressMeter;
}
