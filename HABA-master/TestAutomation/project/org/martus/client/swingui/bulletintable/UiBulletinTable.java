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

package org.martus.client.swingui.bulletintable;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;

import org.martus.client.bulletinstore.BulletinFolder;
import org.martus.client.bulletinstore.ClientBulletinStore;
import org.martus.client.bulletinstore.ClientBulletinStore.AddOlderVersionToFolderFailedException;
import org.martus.client.bulletinstore.ClientBulletinStore.BulletinAlreadyExistsException;
import org.martus.client.core.MartusApp;
import org.martus.client.core.TransferableBulletinList;
import org.martus.client.swingui.MartusLocalization;
import org.martus.client.swingui.UiClipboardUtilities;
import org.martus.client.swingui.UiMainWindow;
import org.martus.client.swingui.WorkerThread;
import org.martus.client.swingui.foldertree.FolderNode;
import org.martus.clientside.UiLocalization;
import org.martus.common.FieldSpecCollection;
import org.martus.common.bulletin.Bulletin;
import org.martus.common.packet.UniversalId;
import org.martus.swing.UiNotifyDlg;
import org.martus.swing.UiTable;


public class UiBulletinTable extends UiTable implements ListSelectionListener, DragGestureListener, DragSourceListener
{
    public UiBulletinTable(UiMainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		model = new BulletinTableModel(mainWindow.getApp());
		setModel(model);
		setStatusColumnWidth();
		setSentColumnWidth();
		setEventDateWidth();
		
		addMouseListener(new TableMouseAdapter());
		keyListener = new TableKeyAdapter();
		addKeyListener(keyListener);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().addMouseListener(new TableHeaderMouseAdapter());

		int mode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
		getSelectionModel().setSelectionMode(mode);
		getSelectionModel().addListSelectionListener(this);

		dragSource.createDefaultDragGestureRecognizer(this,
							DnDConstants.ACTION_COPY_OR_MOVE, this);

		dropAdapter = new UiBulletinTableDropAdapter(this, mainWindow);
	}
    
    public void setStatusColumnWidth()
    {
    	int width = getColumnHeaderWidth(COLUMN_STATUS);

    	MartusLocalization localization = mainWindow.getLocalization();
		int draftWidth = getRenderedWidth(COLUMN_STATUS, localization.getStatusLabel(Bulletin.STATUSDRAFT));
    	if(draftWidth > width)
    		width = draftWidth;
    	
    	int sealedWidth = getRenderedWidth(COLUMN_STATUS, localization.getStatusLabel(Bulletin.STATUSSEALED));
    	if(sealedWidth > width)
    		width = sealedWidth;
    	setColumnMaxWidth(COLUMN_STATUS, width);
    }

    public void setSentColumnWidth()
    {
    	int width = getColumnHeaderWidth(COLUMN_SENT);

    	MartusLocalization localization = mainWindow.getLocalization();
		int sentYes = getRenderedWidth(COLUMN_SENT, localization.getFieldLabel(ClientBulletinStore.WAS_SENT_YES));
    	if(sentYes > width)
    		width = sentYes;
    	
    	int sentNo = getRenderedWidth(COLUMN_SENT, localization.getFieldLabel(ClientBulletinStore.WAS_SENT_NO));
    	if(sentNo > width)
    		width = sentNo;
    	setColumnMaxWidth(COLUMN_SENT, width);
    }
    
    public void setEventDateWidth()
    {
       	int width = getColumnHeaderWidth(COLUMN_EVENTDATE);
    	int dateWidth = getRenderedWidth(COLUMN_EVENTDATE, mainWindow.getLocalization().convertStoredDateToDisplay("2004-12-23"));
    	if(dateWidth > width)
    		width = dateWidth;
    	setColumnMaxWidth(COLUMN_EVENTDATE, width);
    }
    
    
	public UiBulletinTableDropAdapter getDropAdapter()
	{
		return dropAdapter;
	}

	public ClientBulletinStore getStore()
	{
		return mainWindow.getStore();
	}

	public BulletinFolder getFolder()
	{
		return model.getFolder();
	}

	public void setFolder(BulletinFolder folder)
	{
		model.setFolder(folder);
	}

	public Bulletin[] getSelectedBulletins()
	{
		int[] selectedRows = getSelectedRows();
		Bulletin[] bulletins = new Bulletin[selectedRows.length];

		for (int row = 0; row < selectedRows.length; row++)
		{
			bulletins[row] = model.getBulletin(selectedRows[row]);
		}

		return bulletins;
	}
	
	public int getBulletinCount()
	{
		return getRowCount();
	}


	public UniversalId[] getSelectedBulletinUids()
	{
		int[] selectedRows = getSelectedRows();
		UniversalId[] bulletinUids = new UniversalId[selectedRows.length];

		for (int row = 0; row < selectedRows.length; row++)
		{
			bulletinUids[row] = model.getBulletinUid(selectedRows[row]);
		}

		return bulletinUids;
	}

	public Bulletin getSingleSelectedBulletin()
	{
		int[] selectedRows = getSelectedRows();
		if(selectedRows.length == 1)
			return model.getBulletin(selectedRows[0]);
		return null;
	}

	public void selectBulletin(Bulletin b)
	{
		selectRow(model.findBulletin(b.getUniversalId()));
	}

	public void selectBulletins(UniversalId[] uids)
	{
		clearSelection();
		for (int i = 0; i < uids.length; i++)
		{
			int row = model.findBulletin(uids[i]);
			if(row >= 0)
				addRowSelectionInterval(row, row);
		}
	}

	public void doSelectAllBulletins()
	{
		selectAll();
	}


	public void bulletinContentsHaveChanged(Bulletin b)
	{
		UniversalId[] selected = getSelectedBulletinUids();
		tableChanged(new TableModelEvent(model));
		selectBulletins(selected);
	}
	

	public Object getValueAt(int row, int column)
	{
		if(column == COLUMN_SENT && !mainWindow.getApp().isServerConfigured())
			return "";
		return super.getValueAt(row, column);
	}
	
	// ListSelectionListener interface
	public void valueChanged(ListSelectionEvent e)
	{
		if(!e.getValueIsAdjusting() && model != null)
		{
			mainWindow.bulletinSelectionHasChanged();
		}
		repaint();
	}

	// DragGestureListener interface
	public void dragGestureRecognized(DragGestureEvent dragGestureEvent)
	{
		if (getSelectedRowCount() == 0)
			return;

		Bulletin[] bulletinsBeingDragged = getSelectedBulletins();
		TransferableBulletinList dragable = new TransferableBulletinList(getStore(), bulletinsBeingDragged, getFolder());
		dragGestureEvent.startDrag(DragSource.DefaultCopyDrop, dragable, this);
	}

	// DragSourceListener interface
	// we don't care when we enter or exit a drop target
	public void dragEnter (DragSourceDragEvent dsde)						{}
	public void dragExit(DragSourceEvent DragSourceEvent)					{}
	public void dragOver(DragSourceDragEvent DragSourceDragEvent)			{}
	public void dropActionChanged(DragSourceDragEvent DragSourceDragEvent)	{}
	public void dragDropEnd(DragSourceDropEvent dsde)						{}

	public void doModifyBulletin()
	{
		try
		{
			Bulletin original = getSingleSelectedBulletin();
			if(original == null)
				return;

			String myAccountId = mainWindow.getApp().getAccountId();
			boolean isMine = myAccountId.equals(original.getAccount());
			boolean isSealed = original.isSealed();
			boolean isVerifiedFieldDeskBulletin = mainWindow.getApp().isVerifiedFieldDeskAccount(original.getAccount());

			if(!isMine)
			{
				if(isVerifiedFieldDeskBulletin)
				{
					if(!mainWindow.confirmDlg("CloneBulletinAsMine"))
						return;
				}
				else
				{
					if(!mainWindow.confirmDlg("CloneUnverifiedFDBulletinAsMine"))
						return;
				}
			}
			
			if(isMySealed(isMine, isSealed))
			{
				if(!mainWindow.confirmDlg("CloneMySealedAsDraft"))
					return;
			}
			
			if(original.hasUnknownTags() || original.hasUnknownCustomField())
			{
				if(!mainWindow.confirmDlg("EditBulletinWithUnknownTags"))
					return;
			}
			
			Bulletin bulletinToModify = original; 
			if(isMyDraft(isMine, isSealed))
				bulletinToModify = updateFieldSpecsIfNecessary(original);
			else if(needsCloneToEdit(isMine, isSealed))
				bulletinToModify = createCloneAndUpdateFieldSpecsIfNecessary(original);
			bulletinToModify.allowOnlyTheseAuthorizedKeysToRead(mainWindow.getApp().getAllHQKeys());
			bulletinToModify.addAuthorizedToReadKeys(mainWindow.getApp().getDefaultHQKeysWithFallback());
			mainWindow.modifyBulletin(bulletinToModify);
		}
		catch(Exception e)
		{
			mainWindow.notifyDlg("UnexpectedError");
		}

	}

	private boolean isMySealed(boolean isMine, boolean isSealed)
	{
		return isMine && isSealed;
	}
	
	private boolean isMyDraft(boolean isMine, boolean isSealed)
	{
		return isMine && !isSealed;
	}
	
	private boolean needsCloneToEdit(boolean isMine, boolean isSealed)
	{
		return isSealed || !isMine;
	}

	private Bulletin updateFieldSpecsIfNecessary(Bulletin original) throws Exception
	{
		ClientBulletinStore store = mainWindow.getApp().getStore();
		if(store.bulletinHasCurrentFieldSpecs(original))
			return original;
		if(confirmUpdateFieldsDlg("UseBulletinsDraftCustomFields"))
			return original;
		FieldSpecCollection publicFieldSpecsToUse = store.getTopSectionFieldSpecs();
		FieldSpecCollection privateFieldSpecsToUse = store.getBottomSectionFieldSpecs();
		return store.createDraftClone(original, publicFieldSpecsToUse, privateFieldSpecsToUse);
	}

	private Bulletin createCloneAndUpdateFieldSpecsIfNecessary(Bulletin original) throws Exception
	{
		ClientBulletinStore store = mainWindow.getApp().getStore();
		FieldSpecCollection publicFieldSpecsToUse = store.getTopSectionFieldSpecs();
		FieldSpecCollection privateFieldSpecsToUse = store.getBottomSectionFieldSpecs();
		if(!store.bulletinHasCurrentFieldSpecs(original))
		{
			if(confirmUpdateFieldsDlg("UseBulletinsCustomFields"))
			{
				publicFieldSpecsToUse = original.getTopSectionFieldSpecs();
				privateFieldSpecsToUse = original.getBottomSectionFieldSpecs();
			}
		}

		Bulletin bulletinToModify = store.createNewDraft(original, publicFieldSpecsToUse, privateFieldSpecsToUse);
		return bulletinToModify;
	}
	
	private boolean confirmUpdateFieldsDlg(String baseTag)
	{
		MartusLocalization localization = mainWindow.getLocalization();
		String useOld = localization.getButtonLabel("UseOldCustomFields");
		String useNew = localization.getButtonLabel("UseNewCustomFields");
		String[] buttons = {useOld, useNew};
		HashMap tokenReplacement = new HashMap();
		tokenReplacement.put("#UseOldCustomFields#", useOld);
		tokenReplacement.put("#UseNewCustomFields#", useNew);

		return mainWindow.confirmCustomButtonsDlg(mainWindow, baseTag, buttons, tokenReplacement);
	}

	public void doCutBulletins()
	{
		doCopyBulletins();
		doDiscardBulletins();
	}

	public void doCopyBulletins()
	{				
		CopyBulletinsThread thread = new CopyBulletinsThread();
		try
		{
			mainWindow.doBackgroundWork(thread, "BackgroundWorking");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	class CopyBulletinsThread extends WorkerThread
	{
		public CopyBulletinsThread()
		{
		}
		public void doTheWorkWithNO_SWING_CALLS()
		{
			Bulletin[] selected = getSelectedBulletins();
			BulletinFolder folder = getFolder();
			TransferableBulletinList tb = new TransferableBulletinList(getStore(), selected, folder);

			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();
			Transferable contents = clipboard.getContents(this);
			mainWindow.lostOwnership(clipboard, contents);

			clipboard.setContents(tb, mainWindow);
		}
	}

	public void doPasteBulletins()
	{				
		PasteBulletinsThread thread = new PasteBulletinsThread();
		try
		{
			mainWindow.doBackgroundWork(thread, "BackgroundWorking");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	class PasteBulletinsThread extends WorkerThread
	{
		public PasteBulletinsThread()
		{
		}

		public void doTheWorkWithNO_SWING_CALLS()
		{
			BulletinFolder folder = getFolder();
			TransferableBulletinList tb = UiClipboardUtilities.getClipboardTransferableBulletin();

			boolean worked = false;
			String resultMessageTag = null;
				
			if(tb == null)
			{			
				File[] files = UiClipboardUtilities.getClipboardTransferableFiles();
				try
				{
					if(files != null)
						dropAdapter.attemptDropFiles(files, folder);
					worked = true;
					displayNotifyDlgAndWaitForResponse(mainWindow, "OperationCompleted");
					displayNotifyDlgAndWaitForResponse(mainWindow, "FilesWillNotBeDeleted");
					//if(confirmDeletionOfFile(file.getPath()))
						//file.delete();
				}
				catch (BulletinAlreadyExistsException e)
				{
					resultMessageTag = "PasteErrorBulletinAlreadyExists";
				}
				catch (AddOlderVersionToFolderFailedException e)
				{
					resultMessageTag = "PasteErrorBulletinOlder";
				}
				catch (Exception e)
				{
					resultMessageTag = "PasteError";
				}
			}
			else
			{
				try
				{
					dropAdapter.attemptDropBulletins(tb.getBulletins(), folder);
					worked = true;
				}
				catch (BulletinAlreadyExistsException e)
				{
					resultMessageTag = "PasteErrorBulletinAlreadyExists";
				}
				catch (AddOlderVersionToFolderFailedException e)
				{
					resultMessageTag = "PasteErrorBulletinOlder";
				}
				catch (IOException e)
				{
					resultMessageTag = "PasteError";
				}
			}

			if(!worked)
			{
				Toolkit.getDefaultToolkit().beep();
				try
				{
					displayNotifyDlgAndWaitForResponse(mainWindow,resultMessageTag);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public void doResendBulletins()
	{
		BulletinFolder draftOutBox = getStore().getFolderDraftOutbox();
		BulletinFolder sealedOutBox = getStore().getFolderSealedOutbox();

		Bulletin[] selected = getSelectedBulletins();
		boolean notAllowedToSend = false;
		boolean errorIO = false;
		draftOutBox.prepareForBulkOperation();
		sealedOutBox.prepareForBulkOperation();
		for (int i = 0; i < selected.length; i++)
		{
			try
			{
				Bulletin bulletin = selected[i];
				String accountId = getStore().getAccountId();
				if(!bulletin.getBulletinHeaderPacket().isAuthorizedToUpload(accountId))
				{
					notAllowedToSend = true;
					continue;
				}

				if(bulletin.isDraft())
					draftOutBox.add(bulletin);
				if(bulletin.isSealed())
					sealedOutBox.add(bulletin);
				
			}
			catch (BulletinAlreadyExistsException harmless)
			{
			}
			catch (IOException e)
			{
				errorIO = true;
				e.printStackTrace();
			}
		}
		if(notAllowedToSend)
			mainWindow.notifyDlg("ResendErrorNotAuthorizedToSend");
		if(errorIO)
			mainWindow.notifyDlg("ResendError");
	}

	public boolean confirmDeletionOfFile(String filePath)
	{
		UiLocalization localization = mainWindow.getLocalization();
		String title = localization.getWindowTitle("DeleteBulletinFile");
		String msg1 = localization.getFieldLabel("DeleteBulletinFileMsg1");
		String msg2 = localization.getFieldLabel("DeleteBulletinFileMsg2");
		String[] contents = {msg1, filePath, msg2};

		String delete = localization.getButtonLabel("Delete");
		String leave = localization.getButtonLabel("Leave");
		String[] buttons = {delete, leave};

		UiNotifyDlg notify = new UiNotifyDlg(mainWindow, title, contents, buttons);
		String result = notify.getResult();
		if(result != null && result.equals(delete))
			return true;
		return false;
	}

	class TableHeaderMouseAdapter extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
	
			if(!e.isPopupTrigger())
			{
				JTableHeader header = (JTableHeader)e.getSource();
				int col = header.columnAtPoint(e.getPoint());
				model.sortByColumn(col);
				mainWindow.folderContentsHaveChanged(getFolder());
			}
		}
	}

	class TableKeyAdapter extends KeyAdapter
	{
		public void keyReleased(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_DELETE)
			{
				removeKeyListener(keyListener);
				doDiscardBulletins();
				DelayAddListener delay = new DelayAddListener();
				delay.start();
			}
			if(e.isControlDown())
			{

				if(e.getKeyCode() == KeyEvent.VK_A)
					doSelectAllBulletins();
				if(e.getKeyCode() == KeyEvent.VK_X)
					doCutBulletins();
				if(e.getKeyCode() == KeyEvent.VK_C)
					doCopyBulletins();
				if(e.getKeyCode() == KeyEvent.VK_V)
					doPasteBulletins();		
			}
		}
	}

	class DelayAddListener extends Thread
	{
		public DelayAddListener()
		{
		}

		public void run()
		{
			try
			{
				sleep(500);
			}
			catch(InterruptedException e)
			{
			}
			finally
			{
				addKeyListener(keyListener);
			}
		}
	}



	class TableMouseAdapter extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			if(e.isPopupTrigger())
				handleRightClick(e);
		}

		public void mouseReleased(MouseEvent e)
		{
			if(e.isPopupTrigger())
				handleRightClick(e);
		}

		public void mouseClicked(MouseEvent e)
		{
			if(e.getClickCount() == 2)
				handleDoubleClick(e);
		}


		private void handleRightClick(MouseEvent e)
		{
			int thisRow = rowAtPoint(e.getPoint());
			boolean isInsideSelection = isRowSelected(thisRow);
			if(!isInsideSelection && !e.isShiftDown() && !e.isControlDown())
				selectRow(thisRow);

			JPopupMenu menu = mainWindow.getPopupMenu();
			menu.show(UiBulletinTable.this, e.getX(), e.getY());
		}

		private void handleDoubleClick(MouseEvent e)
		{
			doModifyBulletin();
		}
	}

	public void doDiscardBulletins()
	{				
		DiscardBulletinsThread thread = new DiscardBulletinsThread();
		try
		{
			mainWindow.doBackgroundWork(thread, "BackgroundWorking");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	class DiscardBulletinsThread extends WorkerThread
	{
		public DiscardBulletinsThread()
		{
		}

		public void doTheWorkWithNO_SWING_CALLS()
		{
			boolean okToDiscard = true;
			UniversalId[] bulletinsUids = getSelectedBulletinUids();
			if(bulletinsUids.length == 0)
				return;
						
			if(bulletinsUids.length == 1)
				okToDiscard = confirmDiscardSingleBulletin(getSingleSelectedBulletin());
			else
				okToDiscard = confirmDiscardMultipleBulletins();

			if(okToDiscard)
				discardAllSelectedBulletins();			
		}

		boolean confirmDiscardSingleBulletin(Bulletin b)
		{
			BulletinFolder folderToDiscardFrom = getFolder();
			if(!isDiscardedFolder(folderToDiscardFrom))
				return true;

			MartusApp app = mainWindow.getApp();
			BulletinFolder draftOutBox = app.getFolderDraftOutbox();
			BulletinFolder sealedOutBox = app.getFolderSealedOutbox();

			Vector visibleFoldersContainingThisBulletin = app.findBulletinInAllVisibleFolders(b);
			visibleFoldersContainingThisBulletin.remove(folderToDiscardFrom);


			String tagUnsent = null;
			String tagInOtherFolders = null;
			if(visibleFoldersContainingThisBulletin.size() > 0)
			{
				tagInOtherFolders = "warningDeleteSingleBulletinWithCopies";
			}
			else
			{
				if(draftOutBox.contains(b) || sealedOutBox.contains(b))
					tagUnsent = "warningDeleteSingleUnsentBulletin";
			}
			
			String tagMain = "warningDeleteSingleBulletin";

			return confirmDeleteBulletins(tagMain, tagUnsent, tagInOtherFolders, visibleFoldersContainingThisBulletin);
		}

		boolean confirmDiscardMultipleBulletins()
		{
			BulletinFolder folderToDiscardFrom = getFolder();
			if(!isDiscardedFolder(folderToDiscardFrom))
				return true;

			MartusApp app = mainWindow.getApp();

			BulletinFolder draftOutBox = app.getFolderDraftOutbox();
			BulletinFolder sealedOutBox = app.getFolderSealedOutbox();

			boolean aBulletinIsUnsent = false;
			Vector visibleFoldersContainingAnyBulletin = new Vector();
			UniversalId[] bulletinIds = getSelectedBulletinUids();
			for (int i = 0; i < bulletinIds.length; i++)
			{
				UniversalId uid = bulletinIds[i];
				Vector visibleFoldersContainingThisBulletin = app.findBulletinInAllVisibleFolders(uid);
				visibleFoldersContainingThisBulletin.remove(folderToDiscardFrom);
				addUniqueEntriesOnly(visibleFoldersContainingAnyBulletin, visibleFoldersContainingThisBulletin);
				
				if(draftOutBox.contains(uid) || sealedOutBox.contains(uid))
					aBulletinIsUnsent = true;
			}

			String tagUnsent = null;
			if(aBulletinIsUnsent)
				tagUnsent = "warningDeleteMultipleUnsentBulletins";

			String tagInOtherFolders = null;
			if(visibleFoldersContainingAnyBulletin.size() > 0)
				tagInOtherFolders = "warningDeleteMultipleBulletinsWithCopies";
			
			String tagMain = "warningDeleteMultipleBulletins";

			return confirmDeleteBulletins(tagMain, tagUnsent, tagInOtherFolders, visibleFoldersContainingAnyBulletin);
		}

		private boolean confirmDeleteBulletins(String tagMain, String tagUnsent, String tagInOtherFolders, Vector foldersToList)
		{
			UiLocalization localization = mainWindow.getLocalization();
			String title = localization.getWindowTitle(tagMain);

			Vector strings = new Vector();
			strings.add(localization.getFieldLabel(tagMain));
			strings.add("");
			if(tagUnsent != null)
			{
				strings.add(localization.getFieldLabel(tagUnsent));
				strings.add("");
			}
			if(tagInOtherFolders != null)
			{
				strings.add(localization.getFieldLabel(tagInOtherFolders));
				strings.add(buildFolderNameList(foldersToList));
				strings.add("");
			}
			strings.add(localization.getFieldLabel("confirmquestion"));
			String[] contents = new String[strings.size()];
			strings.toArray(contents);
			try
			{
				return displayConfirmDlgAndWaitForResponse(mainWindow, title, contents);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return false;
		}
	}

	
	void discardAllSelectedBulletins()
	{		
		UniversalId[] bulletinsIDsToDiscard = getSelectedBulletinUids();
		BulletinFolder folderToDiscardFrom = getFolder();
		MartusApp app = mainWindow.getApp();
		try
		{
			app.discardBulletinsFromFolder(folderToDiscardFrom, bulletinsIDsToDiscard);
		}
		catch (IOException e)
		{
			// TODO Notify user of an error
			e.printStackTrace();
		}

		BulletinFolder discardedFolder = app.getFolderDiscarded(); 
		mainWindow.folderContentsHaveChanged(folderToDiscardFrom);
		mainWindow.folderContentsHaveChanged(discardedFolder);
		mainWindow.selectNewCurrentBulletin(getSelectedRow());
	}

	void addUniqueEntriesOnly(Vector to, Vector from)
	{
		for(int i = 0 ; i < from.size(); ++i)
		{
			Object elementToAdd = from.get(i);
			if(!to.contains(elementToAdd))
				to.add(elementToAdd);
		}
	}

	
	

	String buildFolderNameList(Vector visibleFoldersContainingThisBulletin)
	{
		UiLocalization localization = mainWindow.getLocalization();
		String names = "";
		for(int i = 0 ; i < visibleFoldersContainingThisBulletin.size() ; ++i)
		{
			BulletinFolder thisFolder = (BulletinFolder)visibleFoldersContainingThisBulletin.get(i);
			FolderNode node = new FolderNode(thisFolder.getName(), localization);
			names += " - " + node.getLocalizedName() + "\n";
		}
		return names;
	}

	boolean isDiscardedFolder(BulletinFolder f)
	{
		return f.equals(f.getStore().getFolderDiscarded());
	}

	void selectRow(int rowIndex)
	{
		if(rowIndex >= 0 && rowIndex < getRowCount())
			setRowSelectionInterval(rowIndex, rowIndex);
	}
	

	static final int COLUMN_STATUS = 0;
	static final int COLUMN_SENT = 1;
	static final int COLUMN_EVENTDATE = 2;

	UiMainWindow mainWindow;
	BulletinTableModel model;
	private DragSource dragSource = DragSource.getDefaultDragSource();
	UiBulletinTableDropAdapter dropAdapter;
	TableKeyAdapter keyListener;
}
