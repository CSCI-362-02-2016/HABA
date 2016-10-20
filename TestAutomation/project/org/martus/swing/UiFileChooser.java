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

To the extent this copyrighted software code is used in the 
Miradi project, it is subject to a royalty-free license to 
members of the Conservation Measures Partnership when 
used with the Miradi software as specified in the agreement 
between Benetech and WCS dated 5/1/05.
*/

package org.martus.swing;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class UiFileChooser extends JFileChooser
{
	private UiFileChooser()
	{
	}

	private UiFileChooser(String title, File currentlySelectedFile, File currentDirectory, String buttonLabel, FileFilter filterToUse)
	{
		super();
		setComponentOrientation(UiLanguageDirection.getComponentOrientation());
		if(title != null)
			setDialogTitle(title);

		if(currentlySelectedFile != null)
			setSelectedFile(currentlySelectedFile);
		if(currentDirectory != null)
			setCurrentDirectory(currentDirectory);
		if(currentlySelectedFile == null && currentDirectory == null)
			setCurrentDirectory(getHomeDirectoryFile());
		
		if(buttonLabel != null)
			setApproveButtonText(buttonLabel);
		if(filterToUse != null)
			setFileFilter(filterToUse);
	}
	
	static public File displayChooseDirectoryDialog(Component owner, String title)
	{
		UiFileChooser chooser = new UiFileChooser();
		chooser.setDialogTitle(title);
		chooser.setFileSelectionMode(DIRECTORIES_ONLY);
		int result = chooser.showDialog(owner, null);
		if(result == APPROVE_OPTION)
			return chooser.getSelectedFile();
		
		return null;
	}
	
	static public FileDialogResults displayFileSaveDialog(Component owner, String title, File directory, String defaultFilename, FileFilter filterToUse)
	{
		File file = null;
		if(defaultFilename != null && defaultFilename.length() > 0)
			file = new File(directory, defaultFilename);
		// NOTE: Save dialog always uses L&F-specific approve button text
		UiFileChooser chooser = new UiFileChooser(title, file, directory, null, filterToUse);
		return getFileResults(chooser.showSaveDialog(owner), chooser);
	}
	
	static public FileDialogResults displayFileOpenDialog(Component owner, String title, File currentDirectory, String buttonLabel, FileFilter filterToUse)
	{
		return displayFileOpenDialog(owner, title, null, currentDirectory, buttonLabel, filterToUse);
	}
	
	static public FileDialogResults displayFileOpenDialog(Component owner, String title, File currentlySelectedFile, File currentDirectory, String buttonLabel, FileFilter filterToUse)
	{
		UiFileChooser chooser = new UiFileChooser(title, currentlySelectedFile, currentDirectory, buttonLabel, filterToUse);
		return getFileResults(chooser.showOpenDialog(owner), chooser);
	}
	
	private static FileDialogResults getFileResults(int result, UiFileChooser chooser)
	{
		if(result != JFileChooser.APPROVE_OPTION)
			return new FileDialogResults();
		return new FileDialogResults(chooser.getSelectedFile(), false);
	}

	static public class FileDialogResults
	{
		FileDialogResults()
		{
			this(null, true);
		}
		
		FileDialogResults(File choosenFileToUse, boolean wasCancelChoosen)
		{
			cancelChoosen = wasCancelChoosen;
			choosenFile = choosenFileToUse;
		}
		
		public boolean wasCancelChoosen()
		{
			return cancelChoosen;
		}

		public File getChosenFile()
		{
			return choosenFile;
		}
		
		public File getCurrentDirectory()
		{
			if(choosenFile == null)
				return null;
			return choosenFile.getParentFile();
		}

		private File choosenFile;
		private boolean cancelChoosen;
	}
	
	static public File getHomeDirectoryFile()
	{
		return new File(System.getProperty("user.home"));
	}
	
	static public File getHomeDirectoryFile(String fileName)
	{
		File homeDir = getHomeDirectoryFile();
		if(fileName != null && fileName.length()>0)
			return new File(homeDir, fileName);
		return homeDir;
	}

	static public final File NO_FILE_SELECTED = null;
}
