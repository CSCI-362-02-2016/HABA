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

import org.martus.client.swingui.UiMainWindow;
import org.martus.client.swingui.UiProgressMeter;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class UiProgressRetrieveBulletinsDlg extends UiProgressRetrieveDlg
{
	public UiProgressRetrieveBulletinsDlg(UiMainWindow window, String tag)
	{
		super(window, tag);
		chunkCountMeter = new UiProgressMeter(this, window.getLocalization());
		
		chunkCountMeter.setStatusMessage("ChunkProgressStatusMessage");
		chunkCountMeter.updateProgressMeter(0, 1);
		UiVBox vBox = new UiVBox();
		vBox.addSpace();
		vBox.addCentered(progressMeter);
		vBox.addCentered(chunkCountMeter);
		vBox.addSpace();
		vBox.addCentered(cancel);
		vBox.addSpace();
		getContentPane().add(vBox);
		Utilities.centerDlg(this);
	}
	
	public UiProgressMeter getChunkCountMeter()
	{
		return chunkCountMeter;
	}


	private UiProgressMeter chunkCountMeter;
}
