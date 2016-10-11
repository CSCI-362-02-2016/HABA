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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileLock;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TimerTask;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.bouncycastle.crypto.engines.RSAEngine;
import org.json.JSONObject;
import org.martus.client.bulletinstore.BulletinFolder;
import org.martus.client.bulletinstore.ClientBulletinStore;
import org.martus.client.core.BackgroundUploader;
import org.martus.client.core.ConfigInfo;
import org.martus.client.core.FontSetter;
import org.martus.client.core.MartusApp;
import org.martus.client.core.MartusApp.LoadConfigInfoException;
import org.martus.client.core.MartusApp.MartusAppInitializationException;
import org.martus.client.core.MartusApp.SaveConfigInfoException;
import org.martus.client.core.MartusJarVerification;
import org.martus.client.core.RetrieveCommand;
import org.martus.client.core.SortableBulletinList;
import org.martus.client.core.TransferableBulletinList;
import org.martus.client.search.SearchTreeNode;
import org.martus.client.swingui.bulletincomponent.UiBulletinPreviewPane;
import org.martus.client.swingui.bulletintable.UiBulletinTablePane;
import org.martus.client.swingui.dialogs.UiAboutDlg;
import org.martus.client.swingui.dialogs.UiBulletinModifyDlg;
import org.martus.client.swingui.dialogs.UiConfigServerDlg;
import org.martus.client.swingui.dialogs.UiContactInfoDlg;
import org.martus.client.swingui.dialogs.UiCreateNewAccountProcess;
import org.martus.client.swingui.dialogs.UiExportBulletinsDlg;
import org.martus.client.swingui.dialogs.UiFancySearchDlg;
import org.martus.client.swingui.dialogs.UiImportBulletinsDlg;
import org.martus.client.swingui.dialogs.UiInitialSigninDlg;
import org.martus.client.swingui.dialogs.UiModelessBusyDlg;
import org.martus.client.swingui.dialogs.UiOnlineHelpDlg;
import org.martus.client.swingui.dialogs.UiPreferencesDlg;
import org.martus.client.swingui.dialogs.UiProgressWithCancelDlg;
import org.martus.client.swingui.dialogs.UiRemoveServerDlg;
import org.martus.client.swingui.dialogs.UiServerSummariesDeleteDlg;
import org.martus.client.swingui.dialogs.UiServerSummariesDlg;
import org.martus.client.swingui.dialogs.UiServerSummariesRetrieveDlg;
import org.martus.client.swingui.dialogs.UiSetFolderOrderDlg;
import org.martus.client.swingui.dialogs.UiShowScrollableTextDlg;
import org.martus.client.swingui.dialogs.UiSigninDlg;
import org.martus.client.swingui.dialogs.UiSplashDlg;
import org.martus.client.swingui.dialogs.UiStringInputDlg;
import org.martus.client.swingui.dialogs.UiTemplateDlg;
import org.martus.client.swingui.dialogs.UiWarningMessageDlg;
import org.martus.client.swingui.foldertree.UiFolderTreePane;
import org.martus.client.swingui.spellcheck.SpellCheckerManager;
import org.martus.client.swingui.tablemodels.DeleteMyServerDraftsTableModel;
import org.martus.client.swingui.tablemodels.RetrieveHQDraftsTableModel;
import org.martus.client.swingui.tablemodels.RetrieveHQTableModel;
import org.martus.client.swingui.tablemodels.RetrieveMyDraftsTableModel;
import org.martus.client.swingui.tablemodels.RetrieveMyTableModel;
import org.martus.client.swingui.tablemodels.RetrieveTableModel;
import org.martus.clientside.ClientSideNetworkGateway;
import org.martus.clientside.CurrentUiState;
import org.martus.clientside.FileDialogHelpers;
import org.martus.clientside.FormatFilter;
import org.martus.clientside.MtfAwareLocalization;
import org.martus.clientside.UiUtilities;
import org.martus.common.EnglishCommonStrings;
import org.martus.common.HeadquartersKeys;
import org.martus.common.MartusLogger;
import org.martus.common.MartusUtilities;
import org.martus.common.MartusUtilities.FileVerificationException;
import org.martus.common.MartusUtilities.ServerErrorException;
import org.martus.common.MiniLocalization;
import org.martus.common.Version;
import org.martus.common.bulletin.Bulletin;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.MartusSecurity;
import org.martus.common.database.FileDatabase.MissingAccountMapException;
import org.martus.common.database.FileDatabase.MissingAccountMapSignatureException;
import org.martus.common.fieldspec.MiniFieldSpec;
import org.martus.common.network.NetworkInterfaceConstants;
import org.martus.common.network.TorTransportWrapper;
import org.martus.common.packet.Packet;
import org.martus.common.packet.UniversalId;
import org.martus.common.packet.XmlPacketLoader;
import org.martus.swing.FontHandler;
import org.martus.swing.UiLanguageDirection;
import org.martus.swing.UiNotifyDlg;
import org.martus.swing.UiOptionPane;
import org.martus.swing.UiPasswordField;
import org.martus.swing.UiPopupMenu;
import org.martus.swing.Utilities;
import org.martus.swing.Utilities.Delay;
import org.martus.util.FileVerifier;
import org.martus.util.StreamableBase64.InvalidBase64Exception;
import org.martus.util.TokenReplacement;
import org.martus.util.TokenReplacement.TokenInvalidException;
import org.martus.util.UnicodeReader;
import org.martus.util.language.LanguageOptions;
import org.martus.util.xml.XmlUtilities;

public class UiMainWindow extends JFrame implements ClipboardOwner
{
	public UiMainWindow()
	{
		super();

		try
		{
			warnIfThisJarNotSigned();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error attempting to verify jar");
			throw new RuntimeException(e);
		}

		try
		{
			warnIfCryptoJarsNotLoaded();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unknown error attempting to locate crypto jars");
			throw new RuntimeException(e);
		}
		
		cursorStack = new Stack();
		UiModelessBusyDlg splashScreen = new UiModelessBusyDlg(new ImageIcon(UiAboutDlg.class.getResource("MartusLogo.png")));

		// Pop up a nag screen if this is an unofficial private release
//		int secondsToWait = 0;
//		try
//		{
//			MultiCalendar today = new MultiCalendar();
//			MultiCalendar startTimer = MultiCalendar.createFromGregorianYearMonthDay(2012, 6, 20);
//			secondsToWait = 2 * MultiCalendar.daysBetween(startTimer, today);
//			
//			new UiNotifyDlg(this, "Martus - Test Version", 
//					new String[] {"THIS IS AN UNOFFICIAL TEST VERSION OF MARTUS",
//					"\nAs more time passes, it will take longer and longer to start up.\n\n" +
//					"Please contact info@martus.org with any questions"}, 
//					new String[] {"OK"});
//			
//			MartusLogger.log("Test version sleeping " + secondsToWait + " seconds...");
//			Thread.sleep(secondsToWait * 1000L);
//			MartusLogger.log("Awake");
//		} 
//		catch (InterruptedException e)
//		{
//			MartusLogger.logException(e);
//		}
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setCurrentActiveFrame(this);
		try
		{
			localization = new MartusLocalization(MartusApp.getTranslationsDirectory(), getAllEnglishStrings());
			app = new MartusApp(localization);
			initializeCurrentLanguage();
		}
		catch(MartusApp.MartusAppInitializationException e)
		{
			initializationErrorExitMartusDlg(e.getMessage());
		}
		UiMainWindow.updateIcon(this);
		
		File timeoutDebug = new File(getApp().getMartusDataRootDirectory(), "timeout.1min");
		if(timeoutDebug.exists())
		{
			timeoutInXSeconds = TESTING_TIMEOUT_60_SECONDS;
			System.out.println(timeoutDebug.toString() + " detected");
		}
		MartusLogger.log("Inactivity timeout set to " + timeoutInXSeconds + " seconds");
		
		timeBetweenFieldOfficeChecksSeconds = TIME_BETWEEN_FIELD_OFFICE_CHECKS_SECONDS;
		File foCheckDebug = new File(getApp().getMartusDataRootDirectory(), "focheck.debug");
		if(foCheckDebug.exists())
		{
			timeBetweenFieldOfficeChecksSeconds = TESTING_FOCHECK_SECONDS;
			System.out.println(foCheckDebug.toString() + " detected; field office check every " + timeBetweenFieldOfficeChecksSeconds + " seconds");
		}
		
		splashScreen.endDialog();
		initalizeUiState();
		
		setGlassPane(new WindowObscurer());
	}
	
	private void warnIfCryptoJarsNotLoaded() throws Exception
	{
		URL jceJarURL = MartusJarVerification.getJarURL(Cipher.class);
		String urlString = jceJarURL.toString();
		int foundAt = urlString.indexOf("bc-jce");
		boolean foundBcJce = (foundAt >= 0);
		MartusLogger.log("warnIfCryptoJarsNotLoaded Cipher: " + urlString);

		if(Version.isRunningUnderOpenJDK())
		{
			if(foundBcJce)
			{
				String hintsToSolve = "Make sure Xbootclasspath does not contain bc-jce.jar";
				JOptionPane.showMessageDialog(null, "When running under OpenJDK, bc-jce.jar cannot be used\n\n" + hintsToSolve);
			}
		}
		else
		{
			if(!foundBcJce)
			{
				String hintsToSolve = "Xbootclasspath might be incorrect; bc-jce.jar might be missing from Martus/lib/ext";
				JOptionPane.showMessageDialog(null, "Didn't load bc-jce.jar\n\n" + hintsToSolve);
			}
		}
		
		try
		{
			URL bcprovJarURL = MartusJarVerification.getJarURL(RSAEngine.class);
			String bcprovJarName = MartusJarVerification.BCPROV_JAR_FILE_NAME;
			if(bcprovJarURL.toString().indexOf(bcprovJarName) < 0)
			{
				String hintsToSolve = "Make sure " + bcprovJarName + " is the only bcprov file in Martus/lib/ext";
				JOptionPane.showMessageDialog(null, "Didn't load " + bcprovJarName + "\n\n" + hintsToSolve);
			}
		} 
		catch (MartusCrypto.InvalidJarException e)
		{
			String hintsToSolve = "Xbootclasspath might be incorrect; " + MartusJarVerification.BCPROV_JAR_FILE_NAME + " might be missing from Martus/lib/ext";
			JOptionPane.showMessageDialog(null, "Didn't load bc-jce.jar\n\n" + hintsToSolve);
		}

	}

	private void warnIfThisJarNotSigned() throws Exception
	{
		if(!MartusApp.isRunningFromJar())
		{
			System.out.println("Skipping jar verification because we are not running from a jar");
			return;
		}

		if(!MartusApp.isJarSigned())
		{
			JOptionPane.showMessageDialog(null, "This Martus Jar is not signed, so cannot be verified");
		}
	}

	public static String[] getAllEnglishStrings()
	{
		String[] clientStrings = EnglishStrings.strings;
		int lenghtClient = clientStrings.length;
		String[] commonStrings = EnglishCommonStrings.strings;
		int lenghtCommon = commonStrings.length;
		String[] allEnglishStrings = new String[lenghtCommon+lenghtClient];
		System.arraycopy(clientStrings,0,allEnglishStrings,0,lenghtClient);
		System.arraycopy(commonStrings,0,allEnglishStrings,lenghtClient,lenghtCommon);
		return allEnglishStrings;
	}

	private void initializeCurrentLanguage()
	{
		CurrentUiState previouslySavedState = new CurrentUiState();
		previouslySavedState.load(getUiStateFile());
		
		if(previouslySavedState.getCurrentLanguage() != "")
		{	
			localization.setCurrentLanguageCode(previouslySavedState.getCurrentLanguage());
			localization.setCurrentDateFormatCode(previouslySavedState.getCurrentDateFormat());
		}
		
		if(localization.getCurrentLanguageCode()== null)
			MartusApp.setInitialUiDefaultsFromFileIfPresent(localization, new File(app.getMartusDataRootDirectory(),"DefaultUi.txt"));
		
		if(localization.getCurrentLanguageCode()== null)
		{
			localization.setCurrentLanguageCode(MtfAwareLocalization.ENGLISH);
			localization.setDateFormatFromLanguage();
		}

		if (MtfAwareLocalization.BURMESE.equals(localization.getCurrentLanguageCode()))
			FontSetter.setUIFont(FontHandler.BURMESE_FONT);
	}

	public File getUiStateFile()
	{
		if(app.isSignedIn())
			return app.getUiStateFileForAccount(app.getCurrentAccountDirectory());
		return new File(app.getMartusDataRootDirectory(), "UiState.dat");
	}
	
	static public void displayDefaultUnofficialTranslationMessageIfNecessary(JFrame owner, MtfAwareLocalization localization, String languageCodeToTest)
	{
		if(localization.isOfficialTranslation(languageCodeToTest))
			return;
		
		URL untranslatedURL = UiMainWindow.class.getResource("UnofficialTranslationMessage.txt");
		URL untranslatedRtoLURL = UiMainWindow.class.getResource("UnofficialTranslationMessageRtoL.txt");
		
		try
		{
			InputStream in = untranslatedURL.openStream();
			UnicodeReader reader = new UnicodeReader(in);
			String message = reader.readAll();
			reader.close();
			in = untranslatedRtoLURL.openStream();
			reader = new UnicodeReader(in);
			String messageRtoL = reader.readAll();
			reader.close();

			updateIcon(owner);
			String warningMessageLtoR = getWarningMessageAboutUnofficialTranslations(message);
			String warningMessageRtoL = getWarningMessageAboutUnofficialTranslations(messageRtoL);
			Toolkit.getDefaultToolkit().beep();
			new UiWarningMessageDlg(owner, "",warningMessageLtoR, warningMessageRtoL);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	
	public static void displayIncompatibleMtfVersionWarningMessageIfNecessary(JFrame owner, MtfAwareLocalization localization, String languageCodeToTest)
	{
		if(localization.doesTranslationVersionMatchProgramVersion(languageCodeToTest, UiConstants.versionLabel))
			return;
		updateIcon(owner);
		String langCode = localization.getCurrentLanguageCode();
		String title = localization.getLabel(langCode, "wintitle", "IncompatibleMtfVersion");
		String warningMessage = localization.getLabel(langCode, "field", "IncompatibleMtfVersion");
		String mtfVersion = localization.getLabel(langCode, "field", "IncompatibleMtfVersionTranslation");
		String programVersion = localization.getLabel(langCode, "field", "IncompatibleMtfVersionProgram");
		String buttonMessage = localization.getLabel(langCode, "button", "ok");
		Toolkit.getDefaultToolkit().beep();
		HashMap map = new HashMap();
		String mtfVersionNumber = localization.getTranslationVersionNumber(languageCodeToTest);		
		map.put("#MtfVersionNumber#", mtfVersionNumber);
		map.put("#ProgramVersionNumber#", localization.extractVersionNumber(UiConstants.versionLabel));
		map.put("#MtfLanguage#", localization.getLanguageName(languageCodeToTest));
		new UiNotifyDlg(owner, title, new String[]{warningMessage, "", mtfVersion, programVersion}, new String[]{buttonMessage}, map);
	}

	private static String getWarningMessageAboutUnofficialTranslations(String originalMessage)
	{
		String token = "#UseUnofficialTranslationFiles#";
		String replacementValue = "\"" + MartusApp.USE_UNOFFICIAL_TRANSLATIONS_NAME + "\"";
		originalMessage = replaceToken(originalMessage, token, replacementValue);
		return originalMessage;
	}

	private static String replaceToken(String originalMessage, String token, String replacementValue)
	{
		try
		{
			return TokenReplacement.replaceToken(originalMessage, token, replacementValue);
		}
		catch(TokenInvalidException e)
		{
			e.printStackTrace();
		}
		return originalMessage;
	}

	public boolean run()
	{
		setCurrentActiveFrame(this);
		
		if(Utilities.isMSWindows())
		{
			updateTitle();
			setVisible(true);
			Dimension screenSize = Utilities.getViewableScreenSize();
			setLocation(screenSize.width, screenSize.height);
		}

		String currentLanguageCode = localization.getCurrentLanguageCode();
		FontSetter.setDefaultFont(currentLanguageCode.equals(MtfAwareLocalization.BURMESE));
		displayDefaultUnofficialTranslationMessageIfNecessary(currentActiveFrame, localization, currentLanguageCode);
		displayIncompatibleMtfVersionWarningMessageIfNecessary(currentActiveFrame, localization, localization.getCurrentLanguageCode());
		
		preventTwoInstances();
		notifyClientCompliance();

		mainWindowInitalizing = true;

		if(!sessionSignIn())
			return false;
		
		try
		{
			String accountId = getApp().getSecurity().getPublicKeyString();
			MartusLogger.log("Public code: " + MartusSecurity.getFormattedPublicCode(accountId) + "\n");
		} 
		catch (InvalidBase64Exception e)
		{
			MartusLogger.logException(e);
			// NOTE: This was just informational output, so keep going
		}
		
		timeoutChecker = new java.util.Timer(true);
		TimeoutTimerTask timeoutTimerTask = new TimeoutTimerTask();
		timeoutChecker.schedule(timeoutTimerTask, 0, BACKGROUND_TIMEOUT_CHECK_EVERY_X_MILLIS);

		loadConfigInfo();
		getApp().startOrStopTorAsRequested();
		if(!createdNewAccount && !justRecovered)
			askAndBackupKeypairIfRequired();
		
		UiModelessBusyDlg waitingForBulletinsToLoad = new UiModelessBusyDlg(getLocalization().getFieldLabel("waitingForBulletinsToLoad"));
		try
		{
			if(!loadFoldersAndBulletins())
				return false;
	
			initializeViews();
			restoreState();
		}
		catch(Exception e)
		{
			MartusLogger.logException(e);
			unexpectedErrorDlg();
		}
		finally
		{
			waitingForBulletinsToLoad.endDialog();
		}

		MartusLogger.log("reloadPendingRetrieveQueue");
		reloadPendingRetrieveQueue();
		
		requestContactInfo();
		
		try
		{
			SpellCheckerManager.initializeSpellChecker(this);
		} 
		catch (MalformedURLException e)
		{
			MartusLogger.logException(e);
			notifyDlg("ErrorInitializingSpellChecker");
			System.exit(1);
		}
		
		MartusLogger.log("Ready to show main window");
		addWindowListener(new WindowEventHandler());
		if(timeoutTimerTask.waitingForSignin)
		{
			setLocation(100000, 0);
			setSize(0,0);
			setEnabled(false);
		}
		else
		{
			MartusLogger.log("Showing main window");
			setVisible(true);
			toFront();
			mainWindowInitalizing = false;
		}
		

		createBackgroundUploadTasks();

		MartusLogger.log("Initialization complete");
		return true;
    }
	
	private void loadFieldSpecCache() throws Exception
	{
		MartusLogger.logBeginProcess("loadFieldSpecCache");
		if(!getStore().loadFieldSpecCache())
		{
			if(!createdNewAccount)
				notifyDlg(this, "CreatingFieldSpecCache");

			getStore().createFieldSpecCacheFromDatabase();
		}
		MartusLogger.logEndProcess("loadFieldSpecCache");
	}
	
	private void createBackgroundUploadTasks()
	{
		uploader = new java.util.Timer(true);
		backgroundUploadTimerTask = new BackgroundTimerTask(this);
		uploader.schedule(backgroundUploadTimerTask, 0, BACKGROUND_UPLOAD_CHECK_MILLIS);

		errorChecker = new javax.swing.Timer(10*1000, new UploadErrorChecker());
		errorChecker.start();
	}

	private void loadConfigInfo()
	{
		try
		{
			app.loadConfigInfo();
			if(app.getConfigInfo().isNewVersion())
			{
				if(!confirmDlg("NewerConfigInfoFileFound"))
					exitWithoutSavingState();
				app.saveConfigInfo();
			}			
		}
		catch (LoadConfigInfoException e)
		{
			notifyDlg("corruptconfiginfo");
		}
		catch(SaveConfigInfoException e)
		{
			notifyDlg("ErrorSavingConfig");
		}
		
		if(createdNewAccount)
		{
			File bulletinDefaultDetailsFile = app.getBulletinDefaultDetailsFile();
			if(bulletinDefaultDetailsFile.exists())
				updateBulletinDetails(bulletinDefaultDetailsFile);
		}
	}

	private void requestContactInfo()
	{
		ConfigInfo info = app.getConfigInfo();
		if(!info.hasEnoughContactInfo())
			doContactInfo();
		else if(info.promptUserRequestSendToServer())
		{
			requestToUpdateContactInfoOnServerAndSaveInfo();
			info.clearPromptUserRequestSendToServer();
		}
	}
	
	private void reloadPendingRetrieveQueue()
	{
		try
		{
			app.loadRetrieveCommand();
			return;
		}
		catch(RetrieveCommand.DataVersionException e)
		{
			notifyDlg("RetrieveFileDataVersionError");
		}
		catch (Exception e)
		{
			notifyDlg("RetrieveFileError");
		}

		try
		{
			app.cancelBackgroundRetrieve();
		} 
		catch (Exception notMuchWeCanDoAboutIt)
		{
			notMuchWeCanDoAboutIt.printStackTrace();
		}
}

	private boolean sessionSignIn()
	{
		boolean wantsNewAccount = false;
		int signInType = UiSigninDlg.INITIAL;
		if(!app.doesAnyAccountExist())
			signInType = UiSigninDlg.INITIAL_NEW_RECOVER_ACCOUNT;
		
		int result = signIn(signInType); 
		if(result == UiSigninDlg.CANCEL)
			return false;
		if(result == UiSigninDlg.NEW_ACCOUNT)
			wantsNewAccount = true;
		if(result == UiSigninDlg.RECOVER_ACCOUNT_BY_SHARE)
		{	
			UiBackupRecoverSharedKeyPair recover = new UiBackupRecoverSharedKeyPair(this);
			if(!recover.recoverKeyPairFromMultipleUnencryptedFiles())
				return false;
			justRecovered = true;
		}
		if(result == UiSigninDlg.RECOVER_ACCOUNT_BY_BACKUP_FILE)
		{
			UiRecoverKeyPairFromBackup recover = new UiRecoverKeyPairFromBackup(this);
			if(!recover.recoverPrivateKey())
				return false;
			justRecovered = true;
		}

		createdNewAccount = false;
		if(wantsNewAccount)
		{
			if(!createAccount())
				return false;
			createdNewAccount = true;
		}
		
		initalizeUiState();
		
		try
		{
			app.doAfterSigninInitalization();
		}
		catch (MartusAppInitializationException e1)
		{
			initializationErrorExitMartusDlg(e1.getMessage());
		}
		catch (FileVerificationException e1)
		{
			askToRepairMissingOrCorruptAccountMapSignature(); 
		}
		catch (MissingAccountMapSignatureException e1)
		{
			askToRepairMissingOrCorruptAccountMapSignature(); 
		}
		catch (MissingAccountMapException e1)
		{
			askToRepairMissingAccountMapFile();
		}

		inactivityDetector = new UiInactivityDetector();

		return true;
	}
    
 	private void askToRepairMissingOrCorruptAccountMapSignature()
	{
		if(!confirmDlgBeep("WarnMissingOrCorruptAccountMapSignatureFile"))
			exitWithoutSavingState();
		try 
		{
			app.getStore().signAccountMap();
			app.doAfterSigninInitalization();
			
		} 
		catch (Exception e) 
		{
			initializationErrorExitMartusDlg(e.getMessage());
		}
	}

	private void askToRepairMissingAccountMapFile()
	{
		if(!confirmDlgBeep("WarnMissingAccountMapFile"))
			exitWithoutSavingState();
		try 
		{
			app.getStore().deleteAllBulletins();
			app.doAfterSigninInitalization();
		} 
		catch (Exception e) 
		{
			initializationErrorExitMartusDlg(e.getMessage());
		}
	}
	
	private void preventTwoInstances()
	{
		try
		{
			File lockFile = getLockFile();
			lockStream = new FileOutputStream(lockFile);
			lockToPreventTwoInstances = lockStream.getChannel().tryLock();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if(lockToPreventTwoInstances == null)
		{
			notifyDlg("AlreadyRunning");
			System.exit(1);
		}
	}

	private File getLockFile()
	{
		return new File(app.getMartusDataRootDirectory(), "lock");
	}
	
	public void unLock()
	{
		try
		{
			lockToPreventTwoInstances.release();
			lockStream.close();
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void prepareToExitMartus()
	{
		preparingToExitMartus = true;
	}

	private boolean loadFoldersAndBulletins() throws Exception
	{
		MartusLogger.logBeginProcess("quarantineUnreadableBulletins");
		int quarantineCount = app.quarantineUnreadableBulletins();
		MartusLogger.logEndProcess("quarantineUnreadableBulletins");
		if(quarantineCount > 0)
			notifyDlg("FoundDamagedBulletins");

		loadFieldSpecCache();

		MartusLogger.logBeginProcess("loadFolders");
		app.loadFolders();
		MartusLogger.logEndProcess("loadFolders");
		if(getStore().needsFolderMigration())
		{
			if(!confirmDlg("NeedsFolderMigration"))
				return false;
			
			try
			{
				getStore().migrateFolders();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				notifyDlg("FolderMigrationFailed");
			}
		}
		
		MartusLogger.logBeginProcess("repairOrphans");
		int orphanCount = app.repairOrphans();
		MartusLogger.logEndProcess("repairOrphans");
		if(orphanCount > 0)
			notifyDlg("FoundOrphans");

		ConfigInfo configInfo = app.getConfigInfo();
		if(!configInfo.isBulletinVersioningAware())
		{
			if(!confirmDlg("NeedsBulletinVersioningMigration"))
				return false;
			
			configInfo.setBulletinVersioningAware(true);
			saveConfigInfo();
		}
		
		return true;
	}
	
	private void askAndBackupKeypairIfRequired()
	{
		ConfigInfo info = app.getConfigInfo();
		boolean hasBackedUpEncrypted = info.hasUserBackedUpKeypairEncrypted();
		boolean hasBackedUpShare = info.hasUserBackedUpKeypairShare();
		boolean hasBackedUpImprovedShare = info.hasBackedUpImprovedKeypairShare();
		if(!hasBackedUpEncrypted || !hasBackedUpShare || !hasBackedUpImprovedShare)
		{
			String generalMsg = localization.getFieldLabel("confirmgeneralBackupKeyPairMsgcause");
			String generalMsgEffect = localization.getFieldLabel("confirmgeneralBackupKeyPairMsgeffect");
			String backupEncrypted = "";
			String backupShare = "";
			String backupImprovedShare = "";
			if(!hasBackedUpEncrypted)
				backupEncrypted = localization.getFieldLabel("confirmbackupIncompleteEncryptedNeeded");
			if(!hasBackedUpShare)
				backupShare = localization.getFieldLabel("confirmbackupIncompleteShareNeeded");
			if (hasBackedUpShare && !hasBackedUpImprovedShare)
				backupImprovedShare = localization.getFieldLabel("confirmbackupIncompleteImprovedShareNeeded");
			String[] contents = new String[] {generalMsg, "", backupEncrypted, "", getBackupShareText(backupImprovedShare, backupShare), "", generalMsgEffect};
			if(confirmDlg(getCurrentActiveFrame(), localization.getWindowTitle("askToBackupKeyPair"), contents))
			{
				if(!hasBackedUpEncrypted)
					askToBackupKeyPairEncryptedSingleFile();
				if(!hasBackedUpShare || !hasBackedUpImprovedShare)
					askToBackupKeyPareToSecretShareFiles();
			}
		}
	}

	private String getBackupShareText(String backupImprovedShareText, String backupShareText)
	{
		if (backupImprovedShareText.length() > 0)
			return backupImprovedShareText;
		else
			return backupShareText;
	}

	void notifyClientCompliance()
	{
		String productDescription = XmlUtilities.getXmlEncoded(getLocalization().getFieldLabel("SplashProductDescription"));
		// NOTE: If this program contains ANY changes that have 
		// not been officially released by Benetech, you MUST 
		// change the splash screen text as required by the 
		// Martus source code license. The easiest way to do 
		// this is to set modified=true and edit the text below. 
		final boolean modified = false;
		
		String complianceStatementAlwaysEnglish = "";
		if(modified)
		{
			complianceStatementAlwaysEnglish =
			BEGIN_HTML_TAGS + 
			"[*your product name*].  <br></br>" +
			productDescription + "<br></br>" +
			"This software is not a standard Martus(TM) program, <br></br>" +
			"because it has been modified by someone other than Benetech, <br></br>" +
			"the copyright owner and original author of the Martus software.  <br></br>" +
			"For details of what has been changed, see [*here*]." +
			END_HTML_TAGS;
		}
		else
		{
			complianceStatementAlwaysEnglish =
			BEGIN_HTML_TAGS +
			"Martus(TM)<br></br>" +
			productDescription +
			END_HTML_TAGS;
		}
		new UiSplashDlg(getCurrentActiveFrame(), getLocalization(), complianceStatementAlwaysEnglish);
	}
	public final static String BEGIN_HTML_TAGS = "<font size='5'>";
	public final static String END_HTML_TAGS = "</font>";
	
    public boolean isMainWindowInitalizing()
    {
    	return mainWindowInitalizing;
    }

    public MartusApp getApp()
    {
		return app;
	}
	
	public MartusLocalization getLocalization()
	{
		return localization;
	}

	public ClientBulletinStore getStore()
	{
		return getApp().getStore();
	}

	public void resetCursor()
	{
		setCursor((Cursor)cursorStack.pop());
	}

	public void setWaitingCursor()
	{
		cursorStack.push(getCursor());
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		return;
	}
	
	public void allBulletinsInCurrentFolderHaveChanged()
	{
		table.allBulletinsInCurrentFolderHaveChanged();
	}

	public void bulletinSelectionHasChanged()
	{
		Bulletin b = table.getSingleSelectedBulletin();
		toolBar.updateEnabledStatuses();
		preview.setCurrentBulletin(b);
	}

	public void bulletinContentsHaveChanged(Bulletin b)
	{
		table.bulletinContentsHaveChanged(b);
		preview.bulletinContentsHaveChanged(b);
	}
	
	public void allFolderContentsHaveChanged()
	{
		Vector allFolders = getStore().getAllFolders();
		for (int i = 0; i < allFolders.size(); i++)
		{	
			folderContentsHaveChanged((BulletinFolder)allFolders.get(i));
		}
		folderTreeContentsHaveChanged();
		selectSentFolder();
	}

	public void folderSelectionHasChanged(BulletinFolder f)
	{
		setWaitingCursor();
		if(defaultFoldersUnsorted)
			f.sortBy("");
		table.setFolder(f);
		resetCursor();
	}

	public void folderContentsHaveChanged(BulletinFolder f)
	{
		folders.folderContentsHaveChanged(f);
		table.folderContentsHaveChanged(f);
	}

	public void folderTreeContentsHaveChanged()
	{
		folders.folderTreeContentsHaveChanged();
	}

	public boolean isDiscardedFolderSelected()
	{
		return folders.getSelectedFolderName().equals(app.getStore().getFolderDiscarded().getName());
	}

	public boolean isCurrentFolderEmpty()
	{
		if(table.getBulletinCount() == 0)
			return true;
		return false;
	}

	public boolean canPaste()
	{
		if(UiClipboardUtilities.getClipboardTransferableBulletin() != null)
			return true;

		if(UiClipboardUtilities.getClipboardTransferableFiles() != null)
			return true;

		return false;
	}

	public boolean canModifyCurrentFolder()
	{
		BulletinFolder folder = getSelectedFolder();
		return canModifyFolder(folder);
	}

	boolean canModifyFolder(BulletinFolder folder)
	{
		if(folder == null)
			return false;
		return folder.canRename();
	}

	public void selectSentFolder()
	{
		ClientBulletinStore store = getStore();
		BulletinFolder folder = store.getFolderSaved();
		selectFolder(folder);
	}

	public void selectFolder(BulletinFolder folder)
	{
		folders.selectFolder(folder.getName());
	}

	public void selectSearchFolder()
	{
		folders.selectFolder(getStore().getSearchFolderName());
	}

	public void selectNewCurrentBulletin(int currentPosition)
	{
		if(currentPosition == -1)
			table.selectLastBulletin();
		else
			table.setCurrentBulletinIndex(currentPosition);
	}

	public boolean confirmDlgBeep(String baseTag)
	{			
		Toolkit.getDefaultToolkit().beep();
		return confirmDlg(baseTag);
	}
	
	public boolean confirmDlg(String baseTag)
	{
		return confirmDlg(getCurrentActiveFrame(), baseTag);
	}
	
	public boolean confirmDlg(JFrame parent, String baseTag)
	{
		return UiUtilities.confirmDlg(getLocalization(), parent, baseTag);
	}

	public boolean confirmDlg(JFrame parent, String baseTag, Map tokenReplacement)
	{
		return UiUtilities.confirmDlg(getLocalization(), parent, baseTag, tokenReplacement);
	}

	public boolean confirmDlg(JFrame parent, String title, String[] contents)
	{
		return UiUtilities.confirmDlg(getLocalization(), parent, title, contents);
	}

	public boolean confirmDlg(JFrame parent, String title, String[] contents, String[] buttons)
	{
		return UiUtilities.confirmDlg(parent, title, contents, buttons);
	}

	public boolean confirmCustomButtonsDlg(JFrame parent,String baseTag, String[] buttons, Map tokenReplacement)
	{
		String title = getConfirmDialogTitle(baseTag);
		String cause = getConfirmCauseText(baseTag);
		String effect = getConfirmEffectText(baseTag);
		String[] contents = {cause, "", effect};

		return confirmDlg(parent, title, contents, buttons, tokenReplacement);
	}

	public String getConfirmEffectText(String baseTag)
	{
		String effect = localization.getFieldLabel("confirm" + baseTag + "effect");
		return effect;
	}

	public String getConfirmCauseText(String baseTag)
	{
		String cause = localization.getFieldLabel("confirm" + baseTag + "cause");
		return cause;
	}

	public String getConfirmDialogTitle(String baseTag)
	{
		String title = localization.getWindowTitle("confirm" + baseTag);
		return title;
	}

	public boolean confirmDlg(JFrame parent, String title, String[] contents, String[] buttons, Map tokenReplacement)
	{
		return UiUtilities.confirmDlg(parent, title, contents, buttons, tokenReplacement);
	}

	public void notifyDlgBeep(String baseTag)
	{			
		Toolkit.getDefaultToolkit().beep();
		notifyDlg(baseTag);
	}
	
	public void notifyDlgBeep(JFrame parent, String baseTag)
	{			
		Toolkit.getDefaultToolkit().beep();
		notifyDlg(parent, baseTag);
	}
	
	public void unexpectedErrorDlg()
	{
		notifyDlg("UnexpectedError");
	}

	public void notifyDlg(String baseTag)
	{
		HashMap emptyTokenReplacement = new HashMap();
		notifyDlg(getCurrentActiveFrame(), baseTag, emptyTokenReplacement);
	}
	
	public void notifyDlg(String baseTag, Map tokenReplacement)
	{
		notifyDlg(getCurrentActiveFrame(), baseTag, tokenReplacement);
	}

	public void notifyDlg(JFrame parent, String baseTag)
	{
		HashMap emptyTokenReplacement = new HashMap();
		notifyDlg(parent, baseTag, emptyTokenReplacement);
	}

	public void notifyDlg(JFrame parent, String baseTag, Map tokenReplacement)
	{
		notifyDlg(parent, baseTag, "notify" + baseTag, tokenReplacement);
	}

	public void notifyDlg(JFrame parent, String baseTag, String titleTag)
	{
		HashMap emptyTokenReplacement = new HashMap();
		notifyDlg(parent, baseTag, titleTag, emptyTokenReplacement);
	}

	public void notifyDlg(JFrame parent, String baseTag, String titleTag, Map tokenReplacement)
	{
		UiUtilities.notifyDlg(getLocalization(), parent, baseTag, titleTag, tokenReplacement);
	}

	public void messageDlg(JFrame parent, String baseTag, String message)
	{
		messageDlg(parent, baseTag, message, new HashMap());
	}

	public void messageDlg(JFrame parent, String baseTag, String message, Map tokenReplacement)
	{
		UiUtilities.messageDlg(getLocalization(), parent, baseTag, message, tokenReplacement);
	}

	private void initializationErrorExitMartusDlg(String message)
	{
		String title = "Error Starting Martus";
		String cause = "Unable to start Martus: \n" + message;
		String ok = "OK";
		String[] buttons = { ok };
		UiOptionPane pane = new UiOptionPane(cause, UiOptionPane.INFORMATION_MESSAGE, UiOptionPane.DEFAULT_OPTION,
								null, buttons);
		JDialog dialog = pane.createDialog(null, title);
		dialog.setVisible(true);
		System.exit(1);
	}

	public String getStringInput(String baseTag, String descriptionTag, String rawDescriptionText, String defaultText)
	{
		UiStringInputDlg inputDlg = new UiStringInputDlg(this, baseTag, descriptionTag, rawDescriptionText, defaultText);
		inputDlg.setFocusToInputField();
		inputDlg.setVisible(true);
		return inputDlg.getResult();
	}

	public UiPopupMenu getPopupMenu()
	{
		UiPopupMenu menu = new UiPopupMenu();
		menu.add(menuBar.actionMenuModifyBulletin);
		menu.addSeparator();
		menu.add(menuBar.actionMenuCutBulletins);
		menu.add(menuBar.actionMenuCopyBulletins);
		menu.add(menuBar.actionMenuPasteBulletins);
		menu.add(menuBar.actionMenuSelectAllBulletins);
		menu.addSeparator();
		menu.add(menuBar.actionMenuDiscardBulletins);
		menu.addSeparator();
		menu.add(menuBar.actionMenuResendBulletins);
		return menu;
	}
	
	public AbstractAction getActionMenuPaste()
	{
		return menuBar.actionMenuPasteBulletins;
	}


	//ClipboardOwner Interface
	//TODO: This doesn't seem to be called right now--can we delete it?
	public void lostOwnership(Clipboard clipboard, Transferable contents)
	{
		System.out.println("UiMainWindow: ClipboardOwner.lostOwnership");
		TransferableBulletinList tb = TransferableBulletinList.extractFrom(contents);
		if(tb != null)
			tb.dispose();
	}


	public void setCurrentDefaultKeyboardVirtual(boolean keyboard)
	{
		uiState.setCurrentDefaultKeyboardVirtual(keyboard);
	}

	public boolean isCurrentDefaultKeyboardVirtual()
	{
		return uiState.isCurrentDefaultKeyboardVirtual();
	}

	public Dimension getBulletinEditorDimension()
	{
		return uiState.getCurrentEditorDimension();
	}

	public Point getBulletinEditorPosition()
	{
		return uiState.getCurrentEditorPosition();
	}

	public boolean isBulletinEditorMaximized()
	{
		return uiState.isCurrentEditorMaximized();
	}

	public void setBulletinEditorDimension(Dimension size)
	{
		uiState.setCurrentEditorDimension(size);
	}

	public void setBulletinEditorPosition(Point position)
	{
		uiState.setCurrentEditorPosition(position);
	}

	public void setBulletinEditorMaximized(boolean maximized)
	{
		uiState.setCurrentEditorMaximized(maximized);
	}

	public void saveCurrentUiState()
	{
		uiState.save(getUiStateFile());
	}

	public void saveState()
	{
		try
		{
			saveStateWithoutPrompting();
		}
		catch(IOException e)
		{
			notifyDlg("ErrorSavingState");
		}
	}

	void saveStateWithoutPrompting() throws IOException
	{
		String folderName = folders.getSelectedFolderName();
		BulletinFolder folder = getStore().findFolder(folderName);
		uiState.setCurrentFolder(folderName);
		copyLocalizationSettingsToUiState();
		if(folder != null)
		{
			uiState.setCurrentSortTag(folder.sortedBy());
			uiState.setCurrentSortDirection(folder.getSortDirection());
			uiState.setCurrentBulletinPosition(table.getCurrentBulletinIndex());
		}
		uiState.setCurrentPreviewSplitterPosition(previewSplitter.getDividerLocation());
		uiState.setCurrentFolderSplitterPosition(folderSplitter.getDividerLocation());
		uiState.setCurrentAppDimension(getSize());
		uiState.setCurrentAppPosition(getLocation());
		boolean isMaximized = getExtendedState()==MAXIMIZED_BOTH;
		uiState.setCurrentAppMaximized(isMaximized);
		saveCurrentUiState();
	}

	public void restoreState()
	{
		String folderName = uiState.getCurrentFolder();
		BulletinFolder folder = getStore().findFolder(folderName);

		if(folder == null)
		{
			selectSentFolder();
			return;
		}

		try
		{
			String sortTag = uiState.getCurrentSortTag();
			if(defaultFoldersUnsorted)
				sortTag = "";
			folder.sortBy(sortTag);
			if(folder.getSortDirection() != uiState.getCurrentSortDirection())
				folder.sortBy(sortTag);
			folders.selectFolder(folderName);
		}
		catch(Exception e)
		{
			System.out.println("UiMainWindow.restoreState: " + e);
		}
	}

	private void initalizeUiState()
	{
		uiState = new CurrentUiState();
		File uiStateFile = getUiStateFile();
		if(!uiStateFile.exists())
		{
			copyLocalizationSettingsToUiState();
			uiState.save(uiStateFile);
			return;
		}
		uiState.load(uiStateFile);
		localization.setCurrentDateFormatCode(uiState.getCurrentDateFormat());
		localization.setCurrentCalendarSystem(uiState.getCurrentCalendarSystem());
		localization.setAdjustThaiLegacyDates(uiState.getAdjustThaiLegacyDates());
		localization.setAdjustPersianLegacyDates(uiState.getAdjustPersianLegacyDates());
	}

	private void copyLocalizationSettingsToUiState()
	{
		uiState.setCurrentLanguage(getLocalization().getCurrentLanguageCode());
		uiState.setCurrentDateFormat(getLocalization().getCurrentDateFormatCode());
		uiState.setCurrentCalendarSystem(getLocalization().getCurrentCalendarSystem());
		uiState.setCurrentAdjustThaiLegacyDates(getLocalization().getAdjustThaiLegacyDates());
		uiState.setCurrentAdjustPersianLegacyDates(getLocalization().getAdjustPersianLegacyDates());
	}

	public void selectBulletinInCurrentFolderIfExists(UniversalId id)
	{
		String selectedFolderName = folders.getSelectedFolderName();
		BulletinFolder currentFolder = app.getStore().findFolder(selectedFolderName);
		if(currentFolder == null)
		{
			System.out.println("Current folder is null: " + selectedFolderName);
			return;
		}
		int position = currentFolder.find(id);
		if(position != -1)
			table.setCurrentBulletinIndex(position);
	}

	public void forceRebuildOfPreview()
	{
		preview.setCurrentBulletin(null);
		table.currentFolderContentsHaveChanged();
		table.selectFirstBulletin();
	}
	
	private JComponent createTopStuff()
	{
		JPanel topStuff = new JPanel(false);
		topStuff.setLayout(new GridLayout(2, 1));

		menuBar = new UiMenuBar(this);
		topStuff.add(menuBar);

		toolBar = new UiToolBar(this);
		topStuff.add(toolBar);

		return topStuff;
	}

	public void doModifyBulletin()
	{
		table.doModifyBulletin();
	}

	public void doSelectAllBulletins()
	{
		table.doSelectAllBulletins();	
	}

	public void doCutBulletins()
	{
		table.doCutBulletins();
	}

	public void doCopyBulletins()
	{
		table.doCopyBulletins();
	}

	public void doPasteBulletins()
	{
		table.doPasteBulletins();
	}
	
	public void doResendBulletins()
	{		
		if (!isServerConfigured())
		{
			notifyDlg(this, "retrievenoserver", "ResendBulletins");
			return;
		}	
		
		table.doResendBulletins();
	}

	public void doDiscardBulletins()
	{
		table.doDiscardBulletins();
	}
	
	public void doCreateFolder()
	{
		folders.createNewFolder();
	}
	
	public void doRenameFolder()
	{
		folders.renameCurrentFolder();
	}
	
	public void doDeleteFolder()
	{
		folders.deleteCurrentFolderIfPossible();
	}
	
	public void doOrganizeFolders()
	{
		Vector originalOrderFolders = folders.getAllFolders();
		UiSetFolderOrderDlg dlg = new UiSetFolderOrderDlg(this, originalOrderFolders);
		dlg.setVisible(true);
		if(!dlg.okPressed())
			return;

		Vector reOrderedFolders = new Vector();
		for(int i = originalOrderFolders.size()-1; i >=0; --i)
		{
			reOrderedFolders.add(originalOrderFolders.get(i));
		}
		try
		{
			folders.setFolderOrder(dlg.getNewFolderOrder());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public SortableBulletinList doSearch()
	{
		try
		{
			SearchTreeNode searchTree = askUserForSearchCriteria();
			if(searchTree == null)
				return null;
			
			MiniFieldSpec[] sortSpecs = new MiniFieldSpec[0];
			return doSearch(searchTree, sortSpecs);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			notifyDlg("UnexpectedError");
			return null;
		}
	}

	public SortableBulletinList doSearch(SearchTreeNode searchTree, MiniFieldSpec[] sortSpecs) throws Exception
	{
		return doSearch(searchTree, sortSpecs, new MiniFieldSpec[0], "SearchProgress");
	}
	
	public SortableBulletinList doSearch(SearchTreeNode searchTree, MiniFieldSpec[] sortSpecs, MiniFieldSpec[] extraSpecs, String progressDialogTag) throws Exception
	{
		UiProgressWithCancelDlg dlg = new UiProgressWithCancelDlg(this, progressDialogTag);
		SearchThread thread = new SearchThread(this, searchTree, sortSpecs, extraSpecs);
		doBackgroundWork(thread, dlg);
		return thread.getResults();
	}

	public void doBackgroundWork(WorkerProgressThread worker, UiProgressWithCancelDlg progressDialog) throws Exception
	{
		setWaitingCursor();
		try
		{
			worker.start(progressDialog);
			progressDialog.pack();
			Utilities.centerDlg(progressDialog);
			progressDialog.setVisible(true);
			worker.cleanup();
		}
		finally
		{
			resetCursor();
		}
	}
	
	
	public void doBackgroundWork(WorkerThread worker, String dialogTag) throws Exception
	{
		setWaitingCursor();
		try
		{
			ModalBusyDialog dlg = new ModalBusyDialog(this, dialogTag);
			worker.start(dlg);
			dlg.setVisible(true);
			worker.cleanup();
		}
		finally
		{
			resetCursor();
		}
	}
	
	static class SearchThread extends WorkerProgressThread
	{
		public SearchThread(UiMainWindow mainWindowToUse, SearchTreeNode searchTreeToUse, MiniFieldSpec[] sortSpecsToUse, MiniFieldSpec[] extraSpecsToUse)
		{
			mainWindow = mainWindowToUse;
			searchTree = searchTreeToUse;
			sortSpecs = sortSpecsToUse;
			extraSpecs = extraSpecsToUse;
		}
		
		public void doTheWorkWithNO_SWING_CALLS()
		{
			searchResults = mainWindow.getApp().search(searchTree, sortSpecs, extraSpecs, mainWindow.uiState.searchFinalBulletinsOnly(), mainWindow.uiState.searchSameRowsOnly(), getProgressMeter());
		}
		
		public SortableBulletinList getResults()
		{
			return searchResults;
		}
		
		UiMainWindow mainWindow;
		SearchTreeNode searchTree;
		MiniFieldSpec[] sortSpecs;
		MiniFieldSpec[] extraSpecs;
		SortableBulletinList searchResults;
	}
	
	public SearchTreeNode askUserForSearchCriteria() throws ParseException
	{
		UiFancySearchDlg searchDlg = new UiFancySearchDlg(this);
		searchDlg.setSearchFinalBulletinsOnly(uiState.searchFinalBulletinsOnly());
		searchDlg.setSearchSameRowsOnly(uiState.searchSameRowsOnly());
		String searchString = uiState.getSearchString();
		JSONObject search = new JSONObject();
		if(searchString.startsWith("{"))
			search = new JSONObject(searchString);
		searchDlg.setSearchAsJson(search);
		searchDlg.setVisible(true);
		if(!searchDlg.getResults())
			return null;
		

		uiState.setSearchFinalBulletinsOnly(searchDlg.searchFinalBulletinsOnly());
		uiState.setSearchSameRowsOnly(searchDlg.searchSameRowsOnly());
		uiState.setSearchString(searchDlg.getSearchAsJson().toString());
		return searchDlg.getSearchTree();
	}

	public void updateSearchFolderAndNotifyUserOfTheResults(SortableBulletinList matchedBulletinsFromSearch)
	{
		if(matchedBulletinsFromSearch == null)
			return;
		app.updateSearchFolder(matchedBulletinsFromSearch);
		ClientBulletinStore store = getStore();
		BulletinFolder searchFolder = store.findFolder(store.getSearchFolderName());
		folders.folderTreeContentsHaveChanged();
		folders.folderContentsHaveChanged(searchFolder);
		int bulletinsFound = searchFolder.getBulletinCount();
		if(bulletinsFound > 0)
		{
			selectSearchFolder();
			showNumberOfBulletinsFound(bulletinsFound, "SearchFound");
		}
		else
		{
			notifyDlg("SearchFailed");
		}
	}

	public void showNumberOfBulletinsFound(int bulletinsFound,String messageTag)
	{
		String title = getLocalization().getWindowTitle("notifySearchFound");
		String message = getLocalization().getFieldLabel(messageTag);
		String ok = getLocalization().getButtonLabel("ok");
		String[] buttons = { ok };
		message = replaceToken(message , "#NumberBulletinsFound#", (new Integer(bulletinsFound)).toString());
		UiOptionPane pane = new UiOptionPane(message, UiOptionPane.INFORMATION_MESSAGE, UiOptionPane.DEFAULT_OPTION,
								null, buttons);
		JDialog dialog = pane.createDialog(this, title);
		dialog.setVisible(true);
	}

	public void aboutMartus()
	{
		new UiAboutDlg(this);
	}

	public void showAccountInfo()
	{
		String title = getLocalization().getWindowTitle("AccountInfo");
		String userName = getLocalization().getFieldLabel("AccountInfoUserName")
						  + app.getUserName();
		String keyDescription = getLocalization().getFieldLabel("AccountInfoPublicKey");
		String keyContents = app.getAccountId();
		String codeDescription = getLocalization().getFieldLabel("AccountInfoPublicCode");
		String formattedCodeContents = null;
		try
		{
			formattedCodeContents = MartusCrypto.computeFormattedPublicCode(keyContents);
		}
		catch(InvalidBase64Exception e)
		{
		}
		String accountDirectory = getLocalization().getFieldLabel("AccountInfoDirectory") + app.getCurrentAccountDirectory();
		
		String ok = getLocalization().getButtonLabel("ok");
		String[] contents = {userName, " ", keyDescription, keyContents," ", codeDescription, formattedCodeContents, " ", accountDirectory};
		String[] buttons = {ok};

		new UiNotifyDlg(this, title, contents, buttons);
	}

	public void displayHelpMessage()
	{
		InputStream helpStream = null;
		InputStream helpStreamTOC = null;
		String currentLanguage = getLocalization().getCurrentLanguageCode();

		helpStream = app.getHelpMain(currentLanguage);
		if(helpStream != null)
			helpStreamTOC = app.getHelpTOC(currentLanguage);
		else
		{
			helpStream = app.getHelpMain(MtfAwareLocalization.ENGLISH);
			helpStreamTOC = app.getHelpTOC(MtfAwareLocalization.ENGLISH);
		}

		UiOnlineHelpDlg dlg = new UiOnlineHelpDlg(this, "Help", helpStream, "OnlineHelpMessage", helpStreamTOC, "OnlineHelpTOCMessage");
		dlg.setVisible(true);
		
		try 
		{
			if(helpStream != null)
				helpStream.close();
			
			if(helpStreamTOC != null)
				helpStreamTOC.close();
		} 
		catch (IOException e) 
		{
			System.out.println("UiMainWindow: DisplayHelpMessage:"+e.getMessage());
		}
	}

	public int getPreviewWidth()
	{
		return preview.getView().getWidth();
	}

	public void doPreferences()
	{
		saveState();
		UiPreferencesDlg dlg = new UiPreferencesDlg(this);
		dlg.setVisible(true);
		if(dlg.getResult())
		{
			app.getConfigInfo().setForceBulletinsAllPrivate(dlg.isAllPrivateChecked());
			app.getConfigInfo().setCheckForFieldOfficeBulletins(dlg.isCheckFieldOfficeBulletinsChecked());
			app.getConfigInfo().setUseZawgyiFont(dlg.isUseZawgyiFont());
			app.getConfigInfo().setUseInternalTor(dlg.isUseInternalTorChecked());
			saveConfigInfo();
			FontSetter.setDefaultFont(dlg.isUseZawgyiFont());
			initializeViews();
			restoreState();
			getTransport().updateStatus();
			backgroundUploadTimerTask.setWaitingForServer();
			setVisible(true);
		}

	}

	public boolean doContactInfo()
	{
		ConfigInfo info = app.getConfigInfo();
		UiContactInfoDlg setupContactDlg = new UiContactInfoDlg(this, info);
		boolean pressedOk = setupContactDlg.getResult();
		if(pressedOk)
			requestToUpdateContactInfoOnServerAndSaveInfo();
		// the following is required (for unknown reasons)
		// to get the window to redraw after the dialog
		// is closed. Yuck! kbs.
		repaint();
		return pressedOk;
	}
	
	public void doRemoveServer()
	{
		offerToCancelRetrieveInProgress();
		if(isRetrieveInProgress())
			return;
		
		if(!reSignIn())
			return;
		
		ConfigInfo info = app.getConfigInfo();
		UiRemoveServerDlg removeDlg = new UiRemoveServerDlg(this, info);
		if (!removeDlg.isYesButtonPressed())
			return;

		try
		{
			app.setServerInfo("","","");
			clearStatusMessage();
			repaint();
		}
		catch(Exception e)
		{
			notifyDlg("ErrorSavingConfig");
		}
	}

	
	public void doConfigureServer()
	{
		offerToCancelRetrieveInProgress();
		if(isRetrieveInProgress())
			return;
		
		
		if(!reSignIn())
			return;
		inConfigServer = true;
		try
		{
			clearStatusMessage();
			ConfigInfo previousServerInfo = app.getConfigInfo();
			UiConfigServerDlg serverInfoDlg = new UiConfigServerDlg(this, previousServerInfo);
			if(!serverInfoDlg.getResult())
				return;		
			String serverIPAddress = serverInfoDlg.getServerIPAddress();
			String serverPublicKey = serverInfoDlg.getServerPublicKey();
			ClientSideNetworkGateway gateway = ClientSideNetworkGateway.buildGateway(serverIPAddress, serverPublicKey, getTransport());
			
			if(!app.isSSLServerAvailable(gateway))
			{
				notifyDlg("ServerSSLNotResponding");
				return;
			}
		
			String newServerCompliance = getServerCompliance(gateway);
			if(!confirmServerCompliance("ServerComplianceDescription", newServerCompliance))
			{
				//TODO:The following line shouldn't be necessary but without it, the trustmanager 
				//will reject the old server, we don't know why.
				ClientSideNetworkGateway.buildGateway(previousServerInfo.getServerName(), previousServerInfo.getServerPublicKey(), getTransport());
				
				notifyDlg("UserRejectedServerCompliance");
				if(serverIPAddress.equals(previousServerInfo.getServerName()) &&
				   serverPublicKey.equals(previousServerInfo.getServerPublicKey()))
				{
					app.setServerInfo("","","");
				}
				return;
			}
			getStore().clearOnServerLists();
			boolean magicAccepted = false;
			app.setServerInfo(serverIPAddress, serverPublicKey, newServerCompliance);
			if(app.requestServerUploadRights(""))
				magicAccepted = true;
			else
			{
				while (true)
				{
					String magicWord = getStringInput("servermagicword", "", "", "");
					if(magicWord == null)
						break;
					if(app.requestServerUploadRights(magicWord))
					{
						magicAccepted = true;
						break;
					}
					notifyDlg("magicwordrejected");
				}
			}
		
			String title = getLocalization().getWindowTitle("ServerSelectionResults");
			String serverSelected = getLocalization().getFieldLabel("ServerSelectionResults") + serverIPAddress;
			String uploadGranted = "";
			if(magicAccepted)
				uploadGranted = getLocalization().getFieldLabel("ServerAcceptsUploads");
			else
				uploadGranted = getLocalization().getFieldLabel("ServerDeclinesUploads");
		
			String ok = getLocalization().getButtonLabel("ok");
			String[] contents = {serverSelected, uploadGranted};
			String[] buttons = {ok};
		
			new UiNotifyDlg(getCurrentActiveFrame(), title, contents, buttons);
			if(magicAccepted)
				requestToUpdateContactInfoOnServerAndSaveInfo();
			
			backgroundUploadTimerTask.forceRecheckOfUidsOnServer();
			getStore().clearOnServerLists();
			repaint();
			setStatusMessageReady();
		}
		catch(SaveConfigInfoException e)
		{
			e.printStackTrace();
			notifyDlg("ErrorSavingConfig");
		}
		finally
		{
			inConfigServer = false;
		}
	}
	
	private TorTransportWrapper getTransport()
	{
		return getApp().getTransport();
	}

	private void offerToCancelRetrieveInProgress()
	{
		if(!isRetrieveInProgress())
			return;
		
		if(!confirmDlg(this, "CancelRetrieve"))
			return;
		
		try
		{
			cancelRetrieve();
		}
		catch (Exception e)
		{
			notifyDlg("UnexpectedError");
		}
	}
	
	private void cancelRetrieve() throws Exception
	{
		getApp().cancelBackgroundRetrieve();
		setStatusMessageReady();
	}

	private boolean isRetrieveInProgress()
	{
		return getApp().getCurrentRetrieveCommand().getRemainingToRetrieveCount() > 0;
	}
	
	private String getServerCompliance(ClientSideNetworkGateway gateway)
	{
		try
		{
			return app.getServerCompliance(gateway);
		}
		catch (Exception e)
		{
			return "";
		}
	}

	boolean confirmServerCompliance(String descriptionTag, String newServerCompliance)
	{
		if(newServerCompliance.equals(""))
			return confirmDlg("ServerComplianceFailed");
			
		UiShowScrollableTextDlg dlg = new UiShowScrollableTextDlg(this, "ServerCompliance", "ServerComplianceAccept", "ServerComplianceReject", descriptionTag, newServerCompliance, null);
		return dlg.getResult();
	}

	private void requestToUpdateContactInfoOnServerAndSaveInfo()
	{
		saveConfigInfo();
		
		ConfigInfo configInfo = app.getConfigInfo();
		if(!configInfo.isServerConfigured())
			return;
		
		boolean sendInfo = confirmDlg("RequestToSendContactInfoToServer");
		configInfo.setSendContactInfoToServer(sendInfo);
		saveConfigInfo();
	}
	
	public void saveConfigInfo()
	{
		try
		{
			app.saveConfigInfo();
		}
		catch (MartusApp.SaveConfigInfoException e)
		{
			notifyDlg("ErrorSavingConfig");
		}
	}

	public boolean isServerConfigured()
	{
		return app.getConfigInfo().isServerConfigured();
	}

	public boolean reSignIn()
	{
		int result = signIn(UiSigninDlg.SECURITY_VALIDATE);
		if(!app.isSignedIn())
			exitWithoutSavingState();
		if(result == UiSigninDlg.SIGN_IN)
			return true;
		return false;
	}


	public void doChangeUserNamePassword()
	{
		if(!reSignIn())
			return;
		if(!getAndSaveUserNamePassword(app.getCurrentKeyPairFile()))
			return;
		try
		{
			app.getConfigInfo().setBackedUpKeypairEncrypted(false);
			app.saveConfigInfo();
		}
		catch (SaveConfigInfoException e)
		{
			notifyDlg("ErrorSavingConfig");
			e.printStackTrace();
		}
		notifyDlg("RewriteKeyPairSaved");
		askToBackupKeyPairEncryptedSingleFile();
	}

	boolean getAndSaveUserNamePassword(File keyPairFile) 
	{
		String originalUserName = app.getUserName();
		UiCreateNewAccountProcess newUserInfo = new UiCreateNewAccountProcess(this, originalUserName);
		if(!newUserInfo.isDataValid())
			return false;
		File accountsHashOfUserNameFile = app.getUserNameHashFile(keyPairFile.getParentFile());
		accountsHashOfUserNameFile.delete();
		return saveKeyPairFile(keyPairFile, newUserInfo.getUserName(), newUserInfo.getPassword());
	}

	public boolean saveKeyPairFile(File keyPairFile, String userName, char[] userPassword)
	{
		try
		{
			app.writeKeyPairFileWithBackup(keyPairFile, userName, userPassword);
			app.attemptSignIn(userName, userPassword);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			notifyDlg(currentActiveFrame, "RewriteKeyPairFailed");
			return false;
			//TODO eventually try to restore keypair from backup.
		}
		return true;
	}

	public void updateBulletinDetails(File defaultFile)
	{
		ConfigInfo info = app.getConfigInfo();
		File details = app.getBulletinDefaultDetailsFile();
		UiTemplateDlg templateDlg = new UiTemplateDlg(this, info, details);
		try
		{
			if(defaultFile != null)
			{
				templateDlg.loadFile(defaultFile);
				notifyDlg("ConfirmCorrectDefaultDetailsData");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

		templateDlg.setVisible(true);
		if(templateDlg.getResult())
		{
			try
			{
				app.saveConfigInfo();
			}
			catch (MartusApp.SaveConfigInfoException e)
			{
				System.out.println("doContactInfo: Unable to Save ConfigInfo" + e);
			}
		}
	}

	public void doRetrieveMySealedBulletins()
	{
		offerToCancelRetrieveInProgress();
		if(isRetrieveInProgress())
			return;
		
		String dlgTitleTag = "RetrieveMySealedBulletins";
		String summariesProgressTag = "RetrieveMySealedBulletinSummaries";
		String retrieverProgressTag = "RetrieveMySealedBulletinProgress";
		String folderName = app.getNameOfFolderRetrievedSealed();

		RetrieveTableModel model = new RetrieveMyTableModel(app, getLocalization());
		retrieveBulletins(model, folderName, dlgTitleTag, summariesProgressTag, retrieverProgressTag);
	}

	public void doRetrieveMyDraftBulletins()
	{
		offerToCancelRetrieveInProgress();
		if(isRetrieveInProgress())
			return;
		
		String dlgTitleTag = "RetrieveMyDraftBulletins";
		String summariesProgressTag = "RetrieveMyDraftBulletinSummaries";
		String retrieverProgressTag = "RetrieveMyDraftBulletinProgress";
		String folderName = app.getNameOfFolderRetrievedDraft();

		RetrieveTableModel model = new RetrieveMyDraftsTableModel(app, getLocalization());
		retrieveBulletins(model, folderName, dlgTitleTag, summariesProgressTag, retrieverProgressTag);
	}

	public void doRetrieveHQBulletins()
	{
		offerToCancelRetrieveInProgress();
		if(isRetrieveInProgress())
			return;
		
		String dlgTitleTag = "RetrieveHQSealedBulletins";
		String summariesProgressTag = "RetrieveHQSealedBulletinSummaries";
		String retrieverProgressTag = "RetrieveHQSealedBulletinProgress";
		String folderName = app.getNameOfFolderRetrievedFieldOfficeSealed();

		RetrieveTableModel model = new RetrieveHQTableModel(app, getLocalization());
		retrieveBulletins(model, folderName, dlgTitleTag, summariesProgressTag, retrieverProgressTag);
	}

	public void doRetrieveHQDraftsBulletins()
	{
		offerToCancelRetrieveInProgress();
		if(isRetrieveInProgress())
			return;
		
		String dlgTitleTag = "RetrieveHQDraftBulletins";
		String summariesProgressTag = "RetrieveHQDraftBulletinSummaries";
		String retrieverProgressTag = "RetrieveHQDraftBulletinProgress";
		String folderName = app.getNameOfFolderRetrievedFieldOfficeDraft();

		RetrieveTableModel model = new RetrieveHQDraftsTableModel(app, getLocalization());
		retrieveBulletins(model, folderName, dlgTitleTag, summariesProgressTag, retrieverProgressTag);
	}

	public void doDeleteServerDraftBulletins()
	{
		String dlgTitleTag = "DeleteMyDraftsFromServer";
		String summariesProgressTag = "RetrieveMyDraftBulletinSummaries";

		RetrieveTableModel model = new DeleteMyServerDraftsTableModel(app, getLocalization());
		deleteServerDrafts(model, dlgTitleTag, summariesProgressTag);
	}

	private void retrieveBulletins(RetrieveTableModel model, String folderName,
						String dlgTitleTag, String summariesProgressTag, String retrieverProgressTag)
	{
		if(isRetrieveInProgress())
		{
			notifyDlg("RetrieveInProgress");
			return;
		}
		
		try
		{
			UiServerSummariesDlg summariesDlg = new UiServerSummariesRetrieveDlg(this, model, dlgTitleTag);
			Vector uidList = displaySummariesDialog(model, dlgTitleTag, summariesProgressTag, summariesDlg);
			if(uidList == null)
				return;
			
			app.createOrFindFolder(folderName);
			app.getStore().saveFolders();
			folderTreeContentsHaveChanged();

			RetrieveCommand command = new RetrieveCommand(folderName, uidList);
			app.startBackgroundRetrieve(command);
			
			setStatusMessageTag(STATUS_RETRIEVING);
			
//			if(progressDlg.shouldExit())
//				notifyDlg("RetrieveCanceled");
		}
		catch(ServerErrorException e)
		{
			notifyDlg("ServerError");
			return;
		}
		catch(Exception e)
		{
			notifyDlg("UnexpectedError");
		}
	}

	private void deleteServerDrafts(RetrieveTableModel model,
						String dlgTitleTag, String summariesProgressTag)
	{

		try
		{
			UiServerSummariesDlg summariesDlg = new UiServerSummariesDeleteDlg(this, model, dlgTitleTag);
			Vector uidList = displaySummariesDialog(model, dlgTitleTag, summariesProgressTag, summariesDlg);
			if(uidList == null)
				return;

			setWaitingCursor();
			try
			{
				String result = app.deleteServerDraftBulletins(uidList);
				if(!result.equals(NetworkInterfaceConstants.OK))
				{
					notifyDlg("DeleteServerDraftsFailed");
					return;
				}

				notifyDlg("DeleteServerDraftsWorked");
			}
			finally
			{
				resetCursor();
			}
		}
		catch (MartusCrypto.MartusSignatureException e)
		{
			notifyDlg("UnexpectedError");
			return;
		}
		catch (Packet.WrongAccountException e)
		{
			notifyDlg("UnexpectedError");
			return;
		}
		catch(ServerErrorException e)
		{
			notifyDlg("ServerError");
			return;
		}
	}


	private Vector displaySummariesDialog(RetrieveTableModel model, String dlgTitleTag, String summariesProgressTag, UiServerSummariesDlg summariesDlg) throws ServerErrorException
	{
		RetrieveSummariesProgressMeter progressHandler = new RetrieveSummariesProgressMeter();
		setWaitingCursor();	
		boolean retrievedSummaries = retrieveSummaries(model, dlgTitleTag, progressHandler);
		resetCursor();

		if(!retrievedSummaries)
			return null;
		summariesDlg.initialize();
		progressHandler.requestCancel();
		if(!summariesDlg.getResult())
			return null;
		return summariesDlg.getUniversalIdList();
	}

	private boolean retrieveSummaries(RetrieveTableModel model, String dlgTitleTag, RetrieveSummariesProgressMeter progressHandler) throws ServerErrorException
	{
		if(!app.isSSLServerAvailable())
		{
			notifyDlg(this, "retrievenoserver", dlgTitleTag);
			return false;
		}
		model.initialize(progressHandler);
		if(progressHandler.shouldExit())
			return false;
		try
		{
			model.checkIfErrorOccurred();
		}
		catch (Exception e)
		{
			notifyDlg(this, "RetrievedOnlySomeSummaries", dlgTitleTag);
		}
		return true;
	}

	public void askToBackupKeyPairEncryptedSingleFile()
	{
		if(confirmDlg("BackupKeyPairInformation"))
			doBackupKeyPairToSingleEncryptedFile();
	}

	public void askToBackupKeyPareToSecretShareFiles()
	{
		if(confirmDlg(this,"BackupKeyPairSecretShare", UiBackupRecoverSharedKeyPair.getTokenReplacement()))
		{
			UiBackupRecoverSharedKeyPair backup = new UiBackupRecoverSharedKeyPair(this);
			backup.backupKeyPairToMultipleUnencryptedFiles();
		}
	}

	public void doBackupKeyPairToSingleEncryptedFile() 
	{
		File keypairFile = app.getCurrentKeyPairFile();
		if(keypairFile.length() > MAX_KEYPAIRFILE_SIZE)
		{
			System.out.println("keypair file too large!");
			notifyDlg("ErrorBackingupKeyPair");
			return;
		}
		
		String defaultBackupFilename = "MartusKeyPairBackup.dat";
		File newBackupFile = doFileSaveDialog("SaveKeyPair", defaultBackupFilename, new KeyPairFormatFilter());
		if(newBackupFile == null)
			return;

		try
		{
			FileInputStream input = new FileInputStream(keypairFile);
			FileOutputStream output = new FileOutputStream(newBackupFile);
	
			int originalKeyPairFileSize = (int) keypairFile.length();
			byte[] inputArray = new byte[originalKeyPairFileSize];
	
			input.read(inputArray);
			output.write(inputArray);
			input.close();
			output.close();
			if(FileVerifier.verifyFiles(keypairFile, newBackupFile))
			{
				notifyDlg("OperationCompleted");
				app.getConfigInfo().setBackedUpKeypairEncrypted(true);
				app.saveConfigInfo();
			}
			else
			{
				notifyDlg("ErrorBackingupKeyPair");
			}
		}
		catch (SaveConfigInfoException e)
		{
			e.printStackTrace();
			notifyDlg("ErrorSavingConfig");
		}
		catch (Exception e)
		{
			MartusLogger.logException(e);
			notifyDlg("ErrorSavingFile");
		}
	}
	
	class KeyPairFormatFilter extends FormatFilter
	{
		@Override
		public String getExtension()
		{
			return MartusApp.SHARE_KEYPAIR_FILENAME_EXTENSION;
		}

		@Override
		public String getDescription()
		{
			return getLocalization().getFieldLabel("KeyPairFileFilter");
		}
		
	}
	
	public void displayScrollableMessage(String titleTag, String message, String okButtonTag, Map tokenReplacement) 
	{
		new UiShowScrollableTextDlg(this, titleTag, okButtonTag, MtfAwareLocalization.UNUSED_TAG, MtfAwareLocalization.UNUSED_TAG, message, tokenReplacement, null);
	}
	
	public void setAndSaveHQKeysInConfigInfo(HeadquartersKeys allHQKeys, HeadquartersKeys defaultHQKeys)
	{
		try
		{
			app.setAndSaveHQKeys(allHQKeys, defaultHQKeys);
		}
		catch(MartusApp.SaveConfigInfoException e)
		{
			notifyDlg("ErrorSavingConfig");
		}
	}

	void initializeViews()
	{
		getContentPane().removeAll();
		getContentPane().setComponentOrientation(UiLanguageDirection.getComponentOrientation());
		updateTitle();
		
		MartusLogger.logBeginProcess("Initializing views");

		preview = new UiBulletinPreviewPane(this);
		table = new UiBulletinTablePane(this);
		folders = new UiFolderTreePane(this);
		getContentPane().add(createTopStuff(), BorderLayout.NORTH);

		previewSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, table, preview);
		previewSplitter.setDividerLocation(uiState.getCurrentPreviewSplitterPosition());

		if(LanguageOptions.isRightToLeftLanguage())
			folderSplitter = new FolderSplitPane(JSplitPane.HORIZONTAL_SPLIT, previewSplitter, folders);
		else
			folderSplitter = new FolderSplitPane(JSplitPane.HORIZONTAL_SPLIT, folders, previewSplitter);

		Dimension screenSize = Utilities.getViewableScreenSize();
		Dimension appDimension = uiState.getCurrentAppDimension();
		Point appPosition = uiState.getCurrentAppPosition();
		boolean showMaximized = false;
		if(Utilities.isValidScreenPosition(screenSize, appDimension, appPosition))
		{
			setLocation(appPosition);
			setSize(appDimension);
			if(uiState.isCurrentAppMaximized())
				showMaximized = true;
		}
		else
			showMaximized = true;
		if(showMaximized)
		{
			setSize(screenSize.width - 50 , screenSize.height - 50);
			Utilities.maximizeWindow(this);
		}

		uiState.setCurrentAppDimension(getSize());
		folderSplitter.setInitialDividerLocation(uiState.getCurrentFolderSplitterPosition());

		getContentPane().add(folderSplitter);
		statusBar = new UiStatusBar(getLocalization());		
		getTransport().setProgressMeter(statusBar.getTorProgressMeter());
		getContentPane().add(statusBar, BorderLayout.SOUTH ); 
		
		MartusLogger.logEndProcess("Initializing views");

		MartusLogger.logBeginProcess("Checking server status");
		checkServerStatus();	
		MartusLogger.logEndProcess("Checking server status");
	}

	private void updateTitle() {
		setTitle(getLocalization().getWindowTitle("main"));
	}

	class FolderSplitPane extends JSplitPane
	{
		public FolderSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) 
		{
			super(newOrientation, newLeftComponent, newRightComponent);
		}

		public void setInitialDividerLocation(int location)
		{
			super.setDividerLocation(location);
		}

		public void setDividerLocation(int location) 
		{
			super.setDividerLocation(location);
			if(previousLocation != location)
			{
				previousLocation = location;
				preview.repaint();
			}
		}
		int previousLocation = -1;
	}
	
	public void checkServerStatus()
	{		
		if (!app.isServerConfigured())
		{	
			setStatusMessageTag(STATUS_SERVER_NOT_CONFIGURED);
			return;
		}
	
		ClientSideNetworkGateway gateway = getApp().getCurrentNetworkInterfaceGateway();		
		if(app.isSSLServerAvailable(gateway))
			setStatusMessageReady();	
		else
			setStatusMessageTag(STATUS_NO_SERVER_AVAILABLE);			
	}
	
	public void clearStatusMessage()
	{
		setStatusMessageTag("");
	}
	
	public void setStatusMessageTag(String tag)
	{
		statusBar.setStatusMessageTag(tag);
	}
	
	public void setStatusMessageReady()
	{
		setStatusMessageTag(UiMainWindow.STATUS_READY);
	}

	int signIn(int mode)
	{
		int seconds = 0;
		UiModelessBusyDlg busyDlg = null;
		while(true)
		{
			Delay delay = new Delay(seconds);
			delay.start();
			Utilities.waitForThreadToTerminate(delay);
			if( busyDlg != null )
				busyDlg.endDialog();

			seconds = seconds * 2 + 1;
			if(mode == UiSigninDlg.TIMED_OUT)
			{
				//Forces this dialog to the top of all windows in system by switching from iconified to normal, then just make the main window not visible
				//cml caused problem when retrieving bulletin summaries when it times out	
				//currentActiveFrame.setState(NORMAL);
				//currentActiveFrame.setVisible(false);
			}
			UiSigninDlg signinDlg = null;
			int userChoice = UiSigninDlg.LANGUAGE_CHANGED;
			String userName = "";
			char[] userPassword = "".toCharArray();
			while(userChoice == UiSigninDlg.LANGUAGE_CHANGED)
			{	
				if(mode==UiSigninDlg.INITIAL || mode == UiSigninDlg.INITIAL_NEW_RECOVER_ACCOUNT)
					signinDlg = new UiInitialSigninDlg(getLocalization(), getCurrentUiState(), getCurrentActiveFrame(), mode, userName, userPassword);
				else
					signinDlg = new UiSigninDlg(getLocalization(), getCurrentUiState(), getCurrentActiveFrame(), mode, userName, userPassword);
				userChoice = signinDlg.getUserChoice();
				userName = signinDlg.getNameText();
				userPassword = signinDlg.getPassword();
			}
			if (userChoice != UiSigninDlg.SIGN_IN)
				return userChoice;
			try
			{
				if(mode == UiSigninDlg.INITIAL)
				{	
					app.attemptSignIn(userName, userPassword);
				}
				else
				{	
					app.attemptReSignIn(userName, userPassword);
					getCurrentActiveFrame().setState(NORMAL);
				}
				return UiSigninDlg.SIGN_IN;
			}
			catch (Exception e)
			{
				notifyDlg(getCurrentActiveFrame(), "incorrectsignin");
				busyDlg = new UiModelessBusyDlg(getLocalization().getFieldLabel("waitAfterFailedSignIn"));
			}
			finally
			{
				UiPasswordField.scrubData(userPassword);
			}
		}
	}

	private boolean createAccount()
	{
		notifyDlg("WelcomeToMartus");
		UiCreateNewAccountProcess newUserInfo = new UiCreateNewAccountProcess(this, "");
		if(!newUserInfo.isDataValid())
			return false;
		String userName = newUserInfo.getUserName();
		char[] userPassword = newUserInfo.getPassword();

		UiModelessBusyDlg waitingForKeyPair = new UiModelessBusyDlg(getLocalization().getFieldLabel("waitingForKeyPairGeneration"));
		try
		{
			app.createAccount(userName ,userPassword);
		}
		catch(Exception e)
		{
			waitingForKeyPair.endDialog();
			notifyDlg("CreateAccountFailed");
			return false;
		}
		waitingForKeyPair.endDialog();
		return true;
	}

	private boolean doUploadReminderOnExit()
	{
		boolean dontExitApplication = false;
		if(!app.isSealedOutboxEmpty())
		{
			if(confirmDlg("UploadReminder"))
				app.resetLastUploadRemindedTime();
			else
				dontExitApplication = true;
		}
		else if(!app.isDraftOutboxEmpty())
		{
			if(!confirmDlg("DraftUploadReminder"))
				dontExitApplication = true;
		}
		return dontExitApplication;
	}

	public void exitNormally()
	{
		if(createdNewAccount)
			askAndBackupKeypairIfRequired();
		if(doUploadReminderOnExit())
			return;
		
		try
		{
			MartusLogger.logBeginProcess("saveState");
			saveState();
			MartusLogger.logEndProcess("saveState");
			getStore().prepareToExitNormally();
			System.out.println("exitNormally:");
			System.out.println("    verifyPacket: " + Packet.callsToVerifyPacketSignature + 
					" calls took total " + Packet.millisInVerifyPacketSignature + " ms");
			System.out.println("    loadPacket:   " + XmlPacketLoader.callsToXmlPacketLoader + 
					" calls took total " + XmlPacketLoader.millisInXmlPacketLoader + " ms");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			notifyDlg("ErrorDuringExit");
		}

		MartusLogger.logMemoryStatistics();

		exitWithoutSavingState();
	}

	public void exitWithoutSavingState()
	{
		getStore().prepareToExitWithoutSavingState();
		try
		{
			lockToPreventTwoInstances.release();
		} 
		catch (IOException e)
		{
			MartusLogger.logException(e);
		}
		try
		{
			lockStream.close();
		} 
		catch (IOException e)
		{
			MartusLogger.logException(e);
		}
		try
		{
			getLockFile().delete();
		}
		catch (Exception e)
		{
			MartusLogger.logException(e);
		}
		System.exit(0);
	}

	public void createBulletin()
	{
		try
		{
			Bulletin b = app.createBulletin();
			modifyBulletin(b);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			notifyDlg("UnexpectedError");
		}
	}

	public void modifyBulletin(Bulletin b) throws Exception
	{
		getCurrentUiState().setModifyingBulletin(true);
		setEnabled(false);
		UiBulletinModifyDlg dlg = null;
		try
		{
			dlg = new UiBulletinModifyDlg(b, this);
			setCurrentActiveFrame(dlg);
			setVisible(false);
			dlg.setVisible(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			if(dlg != null)
				dlg.dispose();
			doneModifyingBulletin();
			throw(e);
		}
	}

	public void doneModifyingBulletin()
	{
		getCurrentUiState().setModifyingBulletin(false);
		setEnabled(true);
		setVisible(true);
		setCurrentActiveFrame(this);
	}

	public void doExportFolder()
	{
	
		BulletinFolder selectedFolder = getSelectedFolder();
		int bulletinCount = selectedFolder.getBulletinCount();
		if(bulletinCount == 0)
		{
			notifyDlg("ExportFolderEmpty");
			return;
		}
		Vector bulletins = new Vector();
		for (int i = 0; i < bulletinCount; ++i)
		{
			bulletins.add(selectedFolder.getBulletinSorted(i));
		}
		String defaultFileName = MartusUtilities.createValidFileName(selectedFolder.getLocalizedName(localization));
		if(defaultFileName.length() == 0)
			defaultFileName = localization.getFieldLabel("ExportedBulletins");
		new UiExportBulletinsDlg(this, bulletins, defaultFileName);
	}

	public BulletinFolder getSelectedFolder()
	{
		return folders.getSelectedFolder();
	}

	public void doExportBulletins()
	{
		try
		{
			Vector bulletins = getSelectedBulletins("ExportZeroBulletins");
			if(bulletins == null)
				return;
			String defaultFileName = localization.getFieldLabel("ExportedBulletins");
			if(bulletins.size()==1)
				defaultFileName = ((Bulletin)bulletins.get(0)).toFileName();
			new UiExportBulletinsDlg(this, bulletins, defaultFileName);
		} 
		catch (Exception e)
		{
			MartusLogger.logException(e);
			unexpectedErrorDlg();
		}
	}
	
	public void doImportBulletins()
	{
		new UiImportBulletinsDlg(this);
	}
	
	
	public Vector getSelectedBulletins(String tagZeroBulletinsSelected) throws Exception
	{
		UniversalId[] uids = table.getSelectedBulletinUids();
		if(uids.length == 0)
		{
			notifyDlg(tagZeroBulletinsSelected);
			return null;
		}
		return getBulletins(uids);
	}
	
	public Vector getBulletins(UniversalId[] uids) throws Exception
	{
		BulletinGetterThread thread = new BulletinGetterThread(uids);
		doBackgroundWork(thread, "PreparingBulletins");
		return thread.bulletins;
	}
	
	class BulletinGetterThread extends WorkerThread
	{
		public BulletinGetterThread(UniversalId[] uidsToGet)
		{
			uids = uidsToGet;
			bulletins = new Vector();
		}
		
		public void doTheWorkWithNO_SWING_CALLS() throws Exception
		{
			for (int i = 0; i < uids.length; i++)
			{
				UniversalId uid = uids[i];
				Bulletin b = getStore().getBulletinRevision(uid);
				bulletins.add(b);
			}
		}
	
		UniversalId[] uids;
		Vector bulletins;
	}
	
	public boolean getBulletinsAlwaysPrivate()
	{
		return app.getConfigInfo().shouldForceBulletinsAllPrivate();
	}

	public boolean getCheckFieldOfficeBulletins()
	{
		return app.getConfigInfo().getCheckForFieldOfficeBulletins();
	}

    public boolean getUseZawgyiFont()
	{
		return app.getConfigInfo().getUseZawgyiFont();
	}

	public boolean getDoZawgyiConversion()
	{
	 	return app.getConfigInfo().getDoZawgyiConversion();
	}
	
	public boolean getUseInternalTor()
	{
		return app.getConfigInfo().useInternalTor();
	}

	public boolean isAnyBulletinSelected()
	{
		return (table.getSelectedBulletinUids().length > 0);
	}

	public boolean isOnlyOneBulletinSelected()
	{
		return (table.getSelectedBulletinUids().length == 1);
	}
	
	static public String getDisplayVersionInfo(MiniLocalization localization)
	{
		String versionInfo = UiConstants.programName;
		versionInfo += " " + localization.getFieldLabel("aboutDlgVersionInfo");
		versionInfo += " " + UiConstants.versionLabel;
		return versionInfo;
	}
	
	
	class WindowEventHandler extends WindowAdapter
	{
		public void windowClosing(WindowEvent event)
		{
			exitNormally();
		}
	}

	class TimeoutTimerTask extends TimerTask
	{
		public TimeoutTimerTask()
		{
		}

		public void run()
		{
			try 
			{
				if(!hasTimedOut())
					return;
				
				MartusLogger.log(MartusLogger.getMemoryStatistics());
				MartusLogger.logBeginProcess("Save before timeout");
				try
				{
					getStore().prepareToExitNormally();
				}
				catch(Throwable e)
				{
					e.printStackTrace();
				}
				MartusLogger.logEndProcess("Save before timeout");
				System.gc();
				MartusLogger.log(MartusLogger.getMemoryStatistics());

				SwingUtilities.invokeAndWait(new ThreadedSignin());
			} 
			catch (Throwable e) 
			{
				// No problem, even out of memory, should kill this thread!
				e.printStackTrace();
			} 
		}

		boolean hasTimedOut()
		{
			if(inactivityDetector.secondsSinceLastActivity() > timeoutInXSeconds)
				return true;

			return false;
		}

		class ThreadedSignin implements Runnable
		{
			public void run()
			{
				waitingForSignin = true;
				getCurrentActiveFrame().getGlassPane().setVisible(true);
				getCurrentActiveFrame().setState(ICONIFIED);
				if(signIn(UiSigninDlg.TIMED_OUT) != UiSigninDlg.SIGN_IN)
				{
					System.out.println("Cancelled from timeout signin");
					exitWithoutSavingState();
				}
				MartusLogger.log("Restoring active frame");
				if(mainWindowInitalizing)
				{
					initializeViews();
					mainWindowInitalizing = false;
				}
				getCurrentActiveFrame().getGlassPane().setVisible(false);
				getCurrentActiveFrame().setVisible(true);
				getCurrentActiveFrame().setEnabled(true);
				getCurrentActiveFrame().setState(NORMAL);
				waitingForSignin = false;
			}
		}
		
		boolean waitingForSignin;
	}

	class UploadErrorChecker extends AbstractAction
	{
		public void actionPerformed(ActionEvent evt)
		{
			if(uploadResult == null)
				return;

			if(uploadResult.equals(NetworkInterfaceConstants.REJECTED) && !rejectedErrorShown)
			{
				notifyDlg("uploadrejected");
				rejectedErrorShown = true;
			}
			if(uploadResult.equals(MartusApp.AUTHENTICATE_SERVER_FAILED) && !authenticationErrorShown)
			{
				notifyDlg("AuthenticateServerFailed");
				authenticationErrorShown = true;
			}
			if(uploadResult.equals(BackgroundUploader.CONTACT_INFO_NOT_SENT) && !contactInfoErrorShown)
			{
				notifyDlg("contactRejected");
				contactInfoErrorShown = true;
			}
		}

		boolean authenticationErrorShown;
		boolean rejectedErrorShown;
		boolean contactInfoErrorShown;
	}
	
	public CurrentUiState getCurrentUiState()
	{
		return uiState;
	}
	
	static public Image getMartusIconImage()
	{
		URL imageURL = UiMainWindow.class.getResource("Martus.png");
		if(imageURL == null)
			return null;
		ImageIcon imageicon = new ImageIcon(imageURL);
		return imageicon.getImage();
	}
	
	static public void updateIcon(JFrame window)
	{
		Image image = getMartusIconImage();
		if(image != null)
			window.setIconImage(image);
	}

	public void setCurrentActiveFrame(JFrame currentActiveFrame)
	{
		this.currentActiveFrame = currentActiveFrame;
	}

	public JFrame getCurrentActiveFrame()
	{
		return currentActiveFrame;
	}
	
	private int getTextFieldColumns(int windowWidth) 
	{
		if(windowWidth < MINIMUM_SCREEN_WIDTH)
			return MINIMUM_TEXT_FIELD_WIDTH;
		windowWidth -= MINIMUM_SCREEN_WIDTH;
		
		int veryApproximateCharWidthInPixels = FontHandler.defaultFontSize;
		int widthToUse = MINIMUM_TEXT_FIELD_WIDTH + (windowWidth / veryApproximateCharWidthInPixels);
		return widthToUse;
	}
	
	public int getPreviewTextFieldColumns()
	{
		int dividerLocation = folderSplitter.getDividerLocation();
		int previewWindowWidth = Utilities.getViewableScreenSize().width - dividerLocation;
		if(LanguageOptions.isRightToLeftLanguage())
			previewWindowWidth = dividerLocation;
		return getTextFieldColumns(previewWindowWidth);
	}

	public int getEditingTextFieldColumns()
	{
		return getTextFieldColumns(Utilities.getViewableScreenSize().width);
	}
	
	public File doFileOpenDialog(String fileDialogCategory, FileFilter filter)
	{
		return internalDoFileOpenDialog(fileDialogCategory, null, filter);
	}
	
	public File doFileOpenDialogWithDirectoryMemory(String fileDialogCategory, FileFilter filter)
	{
		File directory = getMemorizedFileOpenDirectories().get(fileDialogCategory);
		File file = internalDoFileOpenDialog(fileDialogCategory, directory, filter);
		if(file != null)
			getMemorizedFileOpenDirectories().put(fileDialogCategory, file.getParentFile());
		return file;
	}
	
	private File internalDoFileOpenDialog(String fileDialogCategory, File directory, FileFilter filter)
	{
		String title = getLocalization().getWindowTitle("FileDialog" + fileDialogCategory);
		String okButtonLabel = getLocalization().getButtonLabel("FileDialogOk" + fileDialogCategory);
		if(directory == null)
			directory = getApp().getCurrentAccountDirectory();
		return FileDialogHelpers.doFileOpenDialog(getCurrentActiveFrame(), title, okButtonLabel, directory, filter);
	}
	
	public File doFileSaveDialogNoFilterWithDirectoryMemory(String fileDialogCategory, String defaultFilename)
	{
		File directory = getMemorizedFileOpenDirectories().get(fileDialogCategory);
		File file = internalDoFileSaveDialog(fileDialogCategory, directory, defaultFilename, null);
		if(file != null)
			getMemorizedFileOpenDirectories().put(fileDialogCategory, file.getParentFile());
		return file;
	}

	public File doFileSaveDialog(String fileDialogCategory, FormatFilter filter)
	{
		return doFileSaveDialog(fileDialogCategory, "", filter);
	}
	
	public File doFileSaveDialog(String fileDialogCategory, String defaultFilename, FormatFilter filter)
	{
		return internalDoFileSaveDialog(fileDialogCategory, null, defaultFilename, filter);
	}
	
	private File internalDoFileSaveDialog(String fileDialogCategory, File defaultDirectory, String defaultFilename, FormatFilter filter)
	{
		String title = getLocalization().getWindowTitle("FileDialog" + fileDialogCategory);
		if(defaultDirectory == null)
			defaultDirectory = getApp().getCurrentAccountDirectory();
		return FileDialogHelpers.doFileSaveDialog(getCurrentActiveFrame(), title, defaultDirectory, defaultFilename, filter, getLocalization());
	}

	private Map<String, File> getMemorizedFileOpenDirectories()
	{
		if(memorizedFileOpenDirectories == null)
			memorizedFileOpenDirectories = new HashMap<String, File>();
		return memorizedFileOpenDirectories;
	}


	public static final String STATUS_RETRIEVING = "StatusRetrieving";
	public static final String STATUS_READY = "StatusReady";
	public static final String STATUS_CONNECTING = "StatusConnecting";
	public static final String STATUS_NO_SERVER_AVAILABLE = "NoServerAvailableProgressMessage";
	public static final String STATUS_SERVER_NOT_CONFIGURED = "ServerNotConfiguredProgressMessage";
	
	private MartusApp app;
	CurrentUiState uiState;
	UiBulletinPreviewPane preview;
	private JSplitPane previewSplitter;
	private FolderSplitPane folderSplitter;
	private UiBulletinTablePane table;
	private UiFolderTreePane folders;
	private java.util.Timer uploader;
	private java.util.Timer timeoutChecker;
	private javax.swing.Timer errorChecker;
	String uploadResult;
	UiInactivityDetector inactivityDetector;

	private UiMenuBar menuBar;
	private UiToolBar toolBar;
	UiStatusBar statusBar;
	MartusLocalization localization;

	private JFrame currentActiveFrame;
	boolean inConfigServer;
	boolean preparingToExitMartus;
	private FileLock lockToPreventTwoInstances; 
	private FileOutputStream lockStream;
	public static int timeoutInXSeconds;
	public int timeBetweenFieldOfficeChecksSeconds;
	public static final int MINIMUM_TEXT_FIELD_WIDTH = 30;
	private static final int TESTING_TIMEOUT_60_SECONDS = 60;
	private static final int TESTING_FOCHECK_SECONDS = 5 * 60;
	private static final int MINIMUM_SCREEN_WIDTH = 700;
	private static final int MAX_KEYPAIRFILE_SIZE = 32000;
	private static final int BACKGROUND_UPLOAD_CHECK_MILLIS = 5*1000;
	private static final int BACKGROUND_TIMEOUT_CHECK_EVERY_X_MILLIS = 5*1000;
	private static final int TIME_BETWEEN_FIELD_OFFICE_CHECKS_SECONDS = 60 * 60;
	public static boolean defaultFoldersUnsorted;
	public static boolean isAlphaTester;

	boolean mainWindowInitalizing;
	private boolean createdNewAccount;
	private boolean justRecovered;
	private BackgroundTimerTask backgroundUploadTimerTask;
	private Stack cursorStack;

	private static Map<String, File> memorizedFileOpenDirectories;
}
