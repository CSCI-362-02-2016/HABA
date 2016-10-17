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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.martus.client.bulletinstore.BulletinFolder;
import org.martus.client.bulletinstore.ClientBulletinStore;
import org.martus.client.core.BackgroundRetriever;
import org.martus.client.core.BackgroundUploader;
import org.martus.client.core.MartusApp;
import org.martus.clientside.ClientSideNetworkGateway;
import org.martus.common.BulletinSummary;
import org.martus.common.BulletinSummary.WrongValueCount;
import org.martus.common.Exceptions.ServerCallFailedException;
import org.martus.common.Exceptions.ServerNotAvailableException;
import org.martus.common.MartusLogger;
import org.martus.common.ProgressMeterInterface;
import org.martus.common.bulletin.Bulletin;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.database.DatabaseKey;
import org.martus.common.network.NetworkInterfaceConstants;
import org.martus.common.network.NetworkResponse;
import org.martus.common.packet.UniversalId;

class BackgroundTimerTask extends TimerTask
{
	public BackgroundTimerTask(UiMainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		ProgressMeterInterface progressMeter = mainWindow.statusBar.getBackgroundProgressMeter();
		uploader = new BackgroundUploader(mainWindow.getApp(), progressMeter);
		retriever = new BackgroundRetriever(mainWindow.getApp(), progressMeter);
		if(mainWindow.isServerConfigured())
			setWaitingForServer();
	}
	
	public void forceRecheckOfUidsOnServer()
	{
		gotUpdatedOnServerUids = false;
	}

	public void setWaitingForServer()
	{
		mainWindow.setStatusMessageTag(UiMainWindow.STATUS_CONNECTING);
		waitingForServer = true;
	}
	
	public void run()
	{
		if(mainWindow.mainWindowInitalizing)
		{
			MartusLogger.log("Waiting to contact server until startup is complete");
			return;
		}
		
		if(mainWindow.inConfigServer)
			return;
		if(inComplianceDialog)
			return;
		if(mainWindow.preparingToExitMartus)
			return;
		if(checkingForNewFieldOfficeBulletins)
			return;
			
		if(!getApp().isServerConfigured())
		{
			mainWindow.setStatusMessageTag(UiMainWindow.STATUS_SERVER_NOT_CONFIGURED);	
			return;
		}												
			
	
		try
		{
			if(waitingForServer)
				updateServerStatus();
			checkComplianceStatement();
			checkForNewsFromServer();
			getUpdatedListOfBulletinsOnServer();
			doRetrievingOrUploading();
			checkForNewFieldOfficeBulletins();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void updateServerStatus()
	{
		if(!mainWindow.isServerConfigured())
			mainWindow.clearStatusMessage();
		else if(isServerAvailable())
		{
			mainWindow.setStatusMessageReady();
			waitingForServer = false;
		}
		else
		{
			setWaitingForServer();
		}
	}

	private boolean isServerAvailable()
	{
		return (getApp().isSSLServerAvailable());
	}
	
	private void doRetrievingOrUploading() throws Exception
	{
		final UiProgressMeter progressMeter = mainWindow.statusBar.getBackgroundProgressMeter();
		if(retriever.hasWorkToDo())
		{
			if(!isServerAvailable())
				return;
			progressMeter.setStatusMessage(UiMainWindow.STATUS_RETRIEVING);
			doRetrieving();
			if(!retriever.hasWorkToDo())
				SwingUtilities.invokeLater(new ThreadedNotifyDlgAndUpdateReadyMessage("RetrieveCompleted"));
			
			return;
		}
		doUploading();
	}
	
	private void doRetrieving() throws Exception
	{
		String folderName = retriever.getRetrieveFolderName();
		final BulletinFolder folder = mainWindow.getApp().createOrFindFolder(folderName);
		try
		{
			retriever.retrieveNext();
		}
		catch (Exception e)
		{
			String tag = "RetrieveError";
			SwingUtilities.invokeLater(new ThreadedNotifyDlgAndUpdateReadyMessage(tag));
			e.printStackTrace();
		}
		mainWindow.folderContentsHaveChanged(folder);
	}
	
	private void doUploading()
		throws InterruptedException, InvocationTargetException
	{
		
		BackgroundUploader.UploadResult uploadResult = new BackgroundUploader.UploadResult();
		String tag = UiMainWindow.STATUS_READY;
		if(!mainWindow.isServerConfigured())
		{
			tag = UiMainWindow.STATUS_SERVER_NOT_CONFIGURED;
		}
		else
		{					
			uploadResult = uploader.backgroundUpload(); 
			mainWindow.uploadResult = uploadResult.result;	
			if(uploadResult.isHopelesslyDamaged)
			{
				ThreadedNotify damagedBulletin = new ThreadedNotify("DamagedBulletinMovedToDiscarded", uploadResult.uid);
				SwingUtilities.invokeAndWait(damagedBulletin);
				mainWindow.folderContentsHaveChanged(getStore().getFolderSealedOutbox());
				mainWindow.folderContentsHaveChanged(getStore().getFolderDraftOutbox());
				mainWindow.folderContentsHaveChanged(getApp().createOrFindFolder(getStore().getNameOfFolderDamaged()));
				mainWindow.folderTreeContentsHaveChanged();
			}
			else if(uploadResult.bulletinNotSentAndRemovedFromQueue)
			{
				tag = "UploadFailedProgressMessage"; 
				ThreadedNotify bulletinNotSent = new ThreadedNotify("UploadFailedBulletinNotSentToServer", uploadResult.uid);
				SwingUtilities.invokeAndWait(bulletinNotSent);
				updateDisplay();
			}
			else if(uploadResult.result == null)
			{
				tag = "UploadFailedProgressMessage"; 
				if(uploadResult.exceptionThrown == null)
					tag = UiMainWindow.STATUS_NO_SERVER_AVAILABLE;
			}
			else if(uploadResult.uid != null)
			{
				//System.out.println("UiMainWindow.Tick.run: " + uploadResult);
				updateDisplay();
			}
			else
				tag = "";							
		}

		if(tag.length() > 0)			
			mainWindow.setStatusMessageTag(tag);
	}
	
	private void checkForNewFieldOfficeBulletins()
	{
		if(!getApp().getConfigInfo().getCheckForFieldOfficeBulletins())
			return;
		if(System.currentTimeMillis() < nextCheckForFieldOfficeBulletins)
			return;
		if(!isServerAvailable())
			return;
		
		checkingForNewFieldOfficeBulletins = true;
		boolean foundNew = false;
		try
		{
			mainWindow.setStatusMessageTag("statusCheckingForNewFieldOfficeBulletins");
			long keepStatusUntil = System.currentTimeMillis() + 1000;
			Set fieldOfficeSummariesOnServer = getFieldOfficeSummariesOnServer();
			Iterator iter = fieldOfficeSummariesOnServer.iterator();
			while(iter.hasNext())
			{
				BulletinSummary summary = (BulletinSummary)iter.next();
				UniversalId uid = summary.getUniversalId();
				DatabaseKey key = DatabaseKey.createLegacyKey(uid);
				if(!getStore().doesBulletinRevisionExist(key))
				{
					foundNew = true;
					break;
				}
				long serverLastModified = summary.getDateTimeSaved();
				Bulletin localBulletin = getStore().getBulletinRevision(uid);
				long localLastModified = localBulletin.getLastSavedTime();
				if(serverLastModified != localLastModified)
				{
					foundNew = true;
					break;
				}
			}
			
			long remaining = keepStatusUntil - System.currentTimeMillis();
			if(remaining > 0)
				Thread.sleep(remaining);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			nextCheckForFieldOfficeBulletins = System.currentTimeMillis() + (1000 * mainWindow.timeBetweenFieldOfficeChecksSeconds);
			checkingForNewFieldOfficeBulletins = false;
		}


		if(foundNew)
			mainWindow.setStatusMessageTag("statusNewFieldOfficeBulletins");
		else
			mainWindow.setStatusMessageReady();
	}
	
	private void getUpdatedListOfBulletinsOnServer()
	{
		if(gotUpdatedOnServerUids)
			return;
		if(!isServerAvailable())
			return;
		mainWindow.setStatusMessageTag(UiMainWindow.STATUS_CONNECTING);
		System.out.println("Entering BackgroundUploadTimerTask.getUpdatedListOfBulletinsOnServer");
		String myAccountId = getApp().getAccountId();
		HashSet summariesOnServer = new HashSet(1000);
		try
		{
			summariesOnServer.addAll(getBulletinSummariesFromServer(myAccountId));
			
			summariesOnServer.addAll(getFieldOfficeSummariesOnServer());
			getStore().updateOnServerLists(summariesOnServer);

			class CurrentFolderRefresher implements Runnable
			{
				public void run()
				{
					mainWindow.allBulletinsInCurrentFolderHaveChanged();
				}
			}
			SwingUtilities.invokeLater(new CurrentFolderRefresher());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		gotUpdatedOnServerUids = true;
		
		System.out.println("Exiting BackgroundUploadTimerTask.getUpdatedListOfBulletinsOnServer");
		mainWindow.setStatusMessageReady();
	}

	Set getFieldOfficeSummariesOnServer() throws Exception 
	{
		HashSet summariesOnServer = new HashSet(1000);
		ClientSideNetworkGateway gateway = getApp().getCurrentNetworkInterfaceGateway();
		MartusCrypto security = getApp().getSecurity();
		NetworkResponse myFieldOfficesResponse = gateway.getFieldOfficeAccountIds(security, getApp().getAccountId());
		if(NetworkInterfaceConstants.OK.equals(myFieldOfficesResponse.getResultCode()))
		{
			Vector fieldOfficeAccounts = myFieldOfficesResponse.getResultVector();
			System.out.println("My FO accounts: " + fieldOfficeAccounts.size());
			for(int i = 0; i < fieldOfficeAccounts.size(); ++i)
			{
				String fieldOfficeAccountId = (String)fieldOfficeAccounts.get(i);
				summariesOnServer.addAll(getBulletinSummariesFromServer(fieldOfficeAccountId));
			}
		}
		
		return summariesOnServer;
	}
	
	private Vector getBulletinSummariesFromServer(String accountId) throws Exception
	{
		Vector summariesOnServer = new Vector();
		Vector sealedSummaries = tryToGetSealedBulletinSummariesFromServer(accountId);
		summariesOnServer.addAll(sealedSummaries);

		Vector draftSummaries = tryToGetDraftBulletinSummariesFromServer(accountId);
		summariesOnServer.addAll(draftSummaries);
		System.out.println("Adding summaries from server: " + summariesOnServer.size());
		return summariesOnServer;
	}

	private Vector tryToGetDraftBulletinSummariesFromServer(String accountId) throws Exception
	{
		ClientSideNetworkGateway gateway = getApp().getCurrentNetworkInterfaceGateway();
		MartusCrypto security = getApp().getSecurity();
		NetworkResponse myDraftResponse = gateway.getDraftBulletinIds(security, accountId, BulletinSummary.getNormalRetrieveTags());
		if(NetworkInterfaceConstants.OK.equals(myDraftResponse.getResultCode()))
			return buildBulletinSummaryVector(accountId, myDraftResponse.getResultVector());
		return new Vector();
	}

	private Vector tryToGetSealedBulletinSummariesFromServer(String accountId) throws Exception
	{
		ClientSideNetworkGateway gateway = getApp().getCurrentNetworkInterfaceGateway();
		MartusCrypto security = getApp().getSecurity();
		NetworkResponse mySealedResponse = gateway.getSealedBulletinIds(security, accountId, new Vector());
		if(NetworkInterfaceConstants.OK.equals(mySealedResponse.getResultCode()))
			return buildBulletinSummaryVector(accountId, mySealedResponse.getResultVector());
		return new Vector();
	}

	private Vector buildBulletinSummaryVector(String accountId, Vector summaryStrings) throws WrongValueCount
	{
		Vector result = new Vector();
		for(int i=0; i < summaryStrings.size(); ++i)
		{
			String summaryString = (String)summaryStrings.get(i);
			BulletinSummary summary = getApp().createSummaryFromString(accountId, summaryString);
			result.add(summary);
		}
		
		return result;
	}
		
	private void updateDisplay()
	{
		class Updater implements Runnable
		{
			public void run()
			{
				ClientBulletinStore store = getStore();
				mainWindow.folderContentsHaveChanged(store.getFolderSaved());
				mainWindow.folderContentsHaveChanged(store.getFolderSealedOutbox());
				mainWindow.folderContentsHaveChanged(store.getFolderDraftOutbox());
				BulletinFolder discardedFolder = store.findFolder(store.getNameOfFolderDamaged());
				if(discardedFolder != null)
					mainWindow.folderContentsHaveChanged(discardedFolder);
			}
		}
		Updater updater = new Updater();
		
		final boolean crashMode = false;
		if(crashMode)
		{
			updater.run();
		}
		else
		{
			try
			{
				SwingUtilities.invokeAndWait(updater);
			}
			catch (Exception notMuchWeCanDoAboutIt)
			{
				notMuchWeCanDoAboutIt.printStackTrace();
			}
		}
	}

	public void checkComplianceStatement()
	{
		if(alreadyCheckedCompliance)
			return;
		if(!isServerAvailable())
			return;
		try
		{
			ClientSideNetworkGateway gateway = getApp().getCurrentNetworkInterfaceGateway();
			String compliance = getApp().getServerCompliance(gateway);
			alreadyCheckedCompliance = true;
			if (compliance != null)
				mainWindow.setStatusMessageReady();
			
			if(!compliance.equals(getApp().getConfigInfo().getServerCompliance()))
			{
				ThreadedServerComplianceDlg dlg = new ThreadedServerComplianceDlg(compliance);
				SwingUtilities.invokeAndWait(dlg);
			}
		}
		catch (ServerCallFailedException userAlreadyKnows)
		{
			alreadyCheckedCompliance = true;			
			return;
		}
		catch (ServerNotAvailableException weWillTryAgainLater)
		{
			mainWindow.setStatusMessageTag(UiMainWindow.STATUS_NO_SERVER_AVAILABLE);
			return;
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		} 
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	public void checkForNewsFromServer()
	{
		if(alreadyGotNews)
			return;
		if(!isServerAvailable())
			return;
		Vector newsItems = getApp().getNewsFromServer();
		int newsSize = newsItems.size();
		if (newsSize > 0)
			mainWindow.setStatusMessageReady();
			
		
		for (int i = 0; i < newsSize; ++i)
		{
			HashMap tokenReplacement = new HashMap();
			tokenReplacement.put("#CurrentNewsItem#", Integer.toString(i+1));
			tokenReplacement.put("#MaxNewsItems#", Integer.toString(newsSize));

			String newsItem = (String) newsItems.get(i);
			ThreadedMessageDlg newsDlg = new ThreadedMessageDlg("ServerNews", newsItem, tokenReplacement);
			try
			{
				SwingUtilities.invokeAndWait(newsDlg);
			}
			catch (Exception e)
			{
				mainWindow.setStatusMessageTag(UiMainWindow.STATUS_NO_SERVER_AVAILABLE);
				e.printStackTrace();
			}
		}
		alreadyGotNews = true;
	}

	class ThreadedNotify implements Runnable
	{
		public ThreadedNotify(String tag, UniversalId uidToUse)
		{
			notifyTag = tag;
			uid = uidToUse;
		}

		public void run()
		{
			String bulletinTitle = "";
			if(uid != null)
				bulletinTitle = mainWindow.getStore().getBulletinRevision(uid).get(Bulletin.TAGTITLE);
			
			HashMap map = new HashMap();
			map.put("#BulletinTitle#", bulletinTitle);
			mainWindow.notifyDlg(notifyTag,map);
		}
		String notifyTag;
		UniversalId uid;
	}
		
	class ThreadedServerComplianceDlg implements Runnable
	{
		public ThreadedServerComplianceDlg(String newComplianceToUse)
		{
			UiFontEncodingHelper fontHelper = new UiFontEncodingHelper(mainWindow.getDoZawgyiConversion());
			newCompliance = fontHelper.getDisplayable(newComplianceToUse);
		}
			
		public void run()
		{
			try 
			{
				inComplianceDialog = true;
				if(mainWindow.confirmServerCompliance("ServerComplianceChangedDescription", newCompliance))
				{
					String serverAddress = getApp().getConfigInfo().getServerName();
					String serverKey = getApp().getConfigInfo().getServerPublicKey();
					getApp().setServerInfo(serverAddress, serverKey, newCompliance);
				}
				else
				{
					getApp().setServerInfo("", "", "");
					mainWindow.notifyDlg("ExistingServerRemoved");
				}
				inComplianceDialog = false;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
			
		String newCompliance;
	}
		
	class ThreadedMessageDlg implements Runnable
	{
		public ThreadedMessageDlg(String tag, String message, HashMap tokenReplacementToUse )
		{
			titleTag = tag;
			messageContents = message;
			tokenReplacement = tokenReplacementToUse;
		}

		public void run()
		{
			mainWindow.messageDlg(mainWindow, titleTag, messageContents, tokenReplacement);
		}
		String titleTag;
		String messageContents;
		HashMap tokenReplacement;
	}
	
	class ThreadedNotifyDlgAndUpdateReadyMessage implements Runnable
	{
		public ThreadedNotifyDlgAndUpdateReadyMessage(String tagToUse)
		{
			tag = tagToUse;
		}
		
		public void run()
		{
			mainWindow.notifyDlg(mainWindow, tag);
			mainWindow.setStatusMessageReady();
		}
		
		String tag;
	}
		
	MartusApp getApp()
	{
		return mainWindow.getApp();
	}
		
	ClientBulletinStore getStore()
	{
		return getApp().getStore();
	}
	
	UiMainWindow mainWindow;
	BackgroundUploader uploader;
	BackgroundRetriever retriever;

	long nextCheckForFieldOfficeBulletins;
	
	boolean waitingForServer;
	boolean alreadyCheckedCompliance;
	boolean inComplianceDialog;
	boolean alreadyGotNews;
	boolean gotUpdatedOnServerUids;
	boolean checkingForNewFieldOfficeBulletins;
}

