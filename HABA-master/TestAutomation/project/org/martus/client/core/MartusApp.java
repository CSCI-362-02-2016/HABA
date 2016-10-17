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

package org.martus.client.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.json.JSONObject;
import org.martus.client.bulletinstore.BulletinFolder;
import org.martus.client.bulletinstore.ClientBulletinStore;
import org.martus.client.bulletinstore.ClientBulletinStore.AddOlderVersionToFolderFailedException;
import org.martus.client.bulletinstore.ClientBulletinStore.BulletinAlreadyExistsException;
import org.martus.client.reports.ReportFormatFilter;
import org.martus.client.search.BulletinSearcher;
import org.martus.client.search.SearchTreeNode;
import org.martus.client.swingui.EnglishStrings;
import org.martus.client.swingui.UiConstants;
import org.martus.client.test.MockClientSideNetworkHandler;
import org.martus.clientside.ClientSideNetworkGateway;
import org.martus.clientside.ClientSideNetworkHandlerUsingXmlRpc;
import org.martus.clientside.ClientSideNetworkHandlerUsingXmlRpcWithUnverifiedServer;
import org.martus.clientside.MtfAwareLocalization;
import org.martus.clientside.PasswordHelper;
import org.martus.common.BulletinSummary;
import org.martus.common.BulletinSummary.WrongValueCount;
import org.martus.common.Exceptions.ServerCallFailedException;
import org.martus.common.Exceptions.ServerNotAvailableException;
import org.martus.common.FieldCollection;
import org.martus.common.FieldCollection.CustomFieldsParseException;
import org.martus.common.FieldDeskKeys;
import org.martus.common.FieldSpecCollection;
import org.martus.common.HeadquartersKey;
import org.martus.common.HeadquartersKeys;
import org.martus.common.LegacyCustomFields;
import org.martus.common.MartusLogger;
import org.martus.common.MartusUtilities;
import org.martus.common.MartusUtilities.BulletinNotFoundException;
import org.martus.common.MartusUtilities.FileVerificationException;
import org.martus.common.MartusUtilities.NotYourBulletinErrorException;
import org.martus.common.MartusUtilities.PublicInformationInvalidException;
import org.martus.common.MartusUtilities.ServerErrorException;
import org.martus.common.MiniLocalization;
import org.martus.common.ProgressMeterInterface;
import org.martus.common.Version;
import org.martus.common.bulletin.Bulletin;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.MartusCrypto.DecryptionException;
import org.martus.common.crypto.MartusCrypto.EncryptionException;
import org.martus.common.crypto.MartusCrypto.MartusSignatureException;
import org.martus.common.crypto.MartusCrypto.NoKeyPairException;
import org.martus.common.crypto.MartusSecurity;
import org.martus.common.database.FileDatabase.MissingAccountMapException;
import org.martus.common.database.FileDatabase.MissingAccountMapSignatureException;
import org.martus.common.fieldspec.ChoiceItem;
import org.martus.common.fieldspec.MiniFieldSpec;
import org.martus.common.fieldspec.StandardFieldSpecs;
import org.martus.common.network.ClientSideNetworkInterface;
import org.martus.common.network.NetworkInterfaceConstants;
import org.martus.common.network.NetworkResponse;
import org.martus.common.network.NonSSLNetworkAPI;
import org.martus.common.network.NonSSLNetworkAPIWithHelpers;
import org.martus.common.network.ServerSideNetworkInterface;
import org.martus.common.network.TorTransportWrapper;
import org.martus.common.packet.BulletinHeaderPacket;
import org.martus.common.packet.BulletinHistory;
import org.martus.common.packet.FieldDataPacket;
import org.martus.common.packet.Packet;
import org.martus.common.packet.Packet.InvalidPacketException;
import org.martus.common.packet.Packet.SignatureVerificationException;
import org.martus.common.packet.Packet.WrongAccountException;
import org.martus.common.packet.Packet.WrongPacketTypeException;
import org.martus.common.packet.UniversalId;
import org.martus.jarverifier.JarVerifier;
import org.martus.swing.FontHandler;
import org.martus.util.DatePreference;
import org.martus.util.DirectoryUtils;
import org.martus.util.Stopwatch;
import org.martus.util.StreamCopier;
import org.martus.util.StreamableBase64;
import org.martus.util.StreamableBase64.InvalidBase64Exception;
import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;
import org.martus.util.inputstreamwithseek.ByteArrayInputStreamWithSeek;
import org.martus.util.inputstreamwithseek.FileInputStreamWithSeek;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.ZipEntryInputStreamWithSeekThatClosesZipFile;



public class MartusApp
{
	
	public MartusApp(MtfAwareLocalization localizationToUse) throws MartusAppInitializationException
	{
		this(null, determineMartusDataRootDirectory(), localizationToUse);
	}

	public MartusApp(MartusCrypto cryptoToUse, File dataDirectoryToUse, MtfAwareLocalization localizationToUse) throws MartusAppInitializationException
	{
		localization = localizationToUse;

		try
		{
			martusDataRootDirectory = dataDirectoryToUse;

			transport = TorTransportWrapper.create();
			File torDirectory = getOrchidDirectory();
			torDirectory.mkdirs();
			transport.setTorDataDirectory(torDirectory);
			
			if(cryptoToUse == null)
				cryptoToUse = new MartusSecurity();

			verifyJarsIfPossible();

			configInfo = new ConfigInfo();
			currentUserName = "";
			maxNewFolders = MAXFOLDERS;
			store = new ClientBulletinStore(cryptoToUse);
			fieldExpansionStates = new HashMap();
			gridExpansionStates = new HashMap();
			if(shouldUseUnofficialTranslations())
				localization.includeOfficialLanguagesOnly = false;
			currentRetrieveCommand = new RetrieveCommand();
			
		}
		catch(MartusCrypto.CryptoInitializationException e)
		{
			throw new MartusAppInitializationException("ErrorCryptoInitialization");
		}
//		catch (MartusCrypto.InvalidJarException e)
//		{
//			throw new MartusAppInitializationException("Invalid jar file: " + e.getMessage());
//		}
//		catch (IOException e)
//		{
//			throw new MartusAppInitializationException("Error verifying jars: " + e.getMessage());
//		}
		catch (Exception e)
		{
			throw new MartusAppInitializationException("Error verifying jars: " + e.getMessage());
		}

		UpdateDocsIfNecessaryFromMLPFiles();
	}

	public File getOrchidDirectory()
	{
		return new File(getMartusDataRootDirectory(), "orchid");
	}

	public TorTransportWrapper getTransport()
	{
		return transport;
	}

	public static boolean isRunningFromJar() throws MalformedURLException
	{
		return getJarURL() != null;
	}

	public static boolean isJarSigned() throws IOException
	{
		return getSignatureFileJarEntry() != null;
	}

	private void verifyJarsIfPossible() throws Exception
	{
		if(isRunningFromJar())
			MartusJarVerification.verifyJars();
	}
	
	private static URL getJarURL() throws MalformedURLException
	{
		Class c = MartusApp.class;
		String name = c.getName();
		int lastDot = name.lastIndexOf('.');
		String classFileName = name.substring(lastDot + 1) + ".class";
		URL url = c.getResource(classFileName);
		String wholePath = url.toString();
		int bangAt = wholePath.indexOf('!');
		if(bangAt < 0)
			return null;
		
		String jarPart = wholePath.substring(0, bangAt+2);
		URL jarURL = new URL(jarPart);
		return jarURL;
	}
	
	private static JarEntry getSignatureFileJarEntry() throws IOException
	{
		URL jarUrl = getJarURL();
		System.out.println("Checking sig of " + jarUrl);
		JarURLConnection jarConnection = (JarURLConnection)jarUrl.openConnection();
		JarFile jf = jarConnection.getJarFile();
		JarEntry jarEntry = jf.getJarEntry("META-INF/SSMTSJAR.SF");
		System.out.println("Found sig entry: " + jarEntry);
		return jarEntry;
	}

	static public void setInitialUiDefaultsFromFileIfPresent(MtfAwareLocalization localization, File defaultUiFile)
	{
		if(!defaultUiFile.exists())
			return;
		try
		{
			String languageCode = null;
			UnicodeReader in = new UnicodeReader(defaultUiFile);
			languageCode = in.readLine();
			in.close();
			
			if(MtfAwareLocalization.isRecognizedLanguage(languageCode))
			{
				MartusLogger.log("Setting default language: " + languageCode);
				DatePreference datePref = MiniLocalization.getDefaultDatePreferenceForLanguage(languageCode);
				MartusLogger.log("Setting default date fmt: " + datePref.getDateTemplate());
				localization.setCurrentLanguageCode(languageCode);
				localization.setDateFormatFromLanguage();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setServerInfo(String serverName, String serverKey, String serverCompliance) throws SaveConfigInfoException
	{
		configInfo.setServerName(serverName);
		configInfo.setServerPublicKey(serverKey);
		configInfo.setServerCompliance(serverCompliance);
		saveConfigInfo();

		invalidateCurrentHandlerAndGateway();
	}

	public String getLegacyHQKey()
	{
		return configInfo.getLegacyHQKey();
	}
	
	public HeadquartersKeys getAllHQKeys() throws Exception
	{
		return new HeadquartersKeys(configInfo.getAllHQKeysXml());
	}

	public HeadquartersKeys getDefaultHQKeys() throws Exception
	{
		return new HeadquartersKeys(configInfo.getDefaultHQKeysXml());
	}

	public HeadquartersKeys getAllHQKeysWithFallback()
	{
		try
		{
			return getAllHQKeys();
		}
		catch (Exception e)
		{
			MartusLogger.logException(e);
			HeadquartersKey legacyKey = new HeadquartersKey(getLegacyHQKey());
			return new HeadquartersKeys(legacyKey);
		}
	}
	
	public HeadquartersKeys getDefaultHQKeysWithFallback()
	{
		try
		{
			return getDefaultHQKeys();
		}
		catch (Exception e)
		{
			MartusLogger.logException(e);
			HeadquartersKey legacyKey = new HeadquartersKey(getLegacyHQKey());
			return new HeadquartersKeys(legacyKey);
		}
	}

	public void addHQLabelsWherePossible(HeadquartersKeys keys)
	{
		for(int i = 0; i < keys.size(); ++i)
		{
			HeadquartersKey key = keys.get(i);
			key.setLabel(getHQLabelIfPresent(key));
		}
	}

	
	public String getHQLabelIfPresent(HeadquartersKey hqKey)
	{
		try
		{
			String hqLabelIfPresent = getAllHQKeys().getLabelIfPresent(hqKey);
			if(hqLabelIfPresent.length() == 0)
			{
				String publicCode = hqKey.getPublicKey();
				try
				{
					publicCode = hqKey.getPublicCode();
				}
				catch (InvalidBase64Exception e)
				{
					e.printStackTrace();
				}
				String hqNotConfigured = localization.getFieldLabel("HQNotConfigured");
				hqLabelIfPresent = publicCode + " " + hqNotConfigured;
			}
			return hqLabelIfPresent;
		}
		catch (Exception e)
		{
			MartusLogger.logException(e);
			return "";
		}
	}

	public Vector<String> getFieldDeskPublicKeyStrings() throws Exception
	{
		String xml = getConfigInfo().getFieldDeskKeysXml();
		FieldDeskKeys keys = new FieldDeskKeys(xml);
		Vector<String> keyStrings = new Vector<String>();
		for(int i = 0; i < keys.size(); ++i)
		{
			String fieldDeskPublicKeyString = keys.get(i).getPublicKey();
			keyStrings.add(fieldDeskPublicKeyString);
		}
		
		return keyStrings;
	}

	public boolean isVerifiedFieldDeskAccount(String authorPublicKeyString) throws Exception
	{
		Vector<String> fieldDeskPublicKeyStrings = getFieldDeskPublicKeyStrings();
		boolean isFieldDeskBulletin = fieldDeskPublicKeyStrings.contains(authorPublicKeyString);
		return isFieldDeskBulletin;
	}

	public ConfigInfo getConfigInfo()
	{
		return configInfo;
	}
	
	public void setAndSaveHQKeys(HeadquartersKeys allHQKeys, HeadquartersKeys defaultHQKeys) throws SaveConfigInfoException 
	{
		configInfo.setAllHQKeysXml(allHQKeys.toStringWithLabel());
		configInfo.setDefaultHQKeysXml(defaultHQKeys.toStringWithLabel());
		if(allHQKeys.isEmpty())
			configInfo.clearHQKey();
		else
			configInfo.setLegacyHQKey(allHQKeys.get(0).getPublicKey());
		saveConfigInfo();
	}

	public void saveConfigInfo() throws SaveConfigInfoException
	{
		File file = getConfigInfoFile();
		File signatureFile = getConfigInfoSignatureFile();

		try
		{
			ByteArrayOutputStream encryptedConfigOutputStream = new ByteArrayOutputStream();
			configInfo.save(encryptedConfigOutputStream);
			byte[] encryptedInfo = encryptedConfigOutputStream.toByteArray();
			encryptAndWriteFileAndSignatureFile(file, signatureFile, encryptedInfo);
		}
		catch (Exception e)
		{
			System.out.println("saveConfigInfo :" + e);
			throw new SaveConfigInfoException();
		}

		startOrStopTorAsRequested();
	}

	public void encryptAndWriteFileAndSignatureFile(File file, File signatureFile,
			byte[] plainText) throws Exception
	{
		ByteArrayInputStream encryptedInputStream = new ByteArrayInputStream(plainText);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		getSecurity().encrypt(encryptedInputStream, fileOutputStream);

		fileOutputStream.close();
		encryptedInputStream.close();

		FileInputStream in = new FileInputStream(file);
		byte[] signature = getSecurity().createSignatureOfStream(in);
		in.close();

		FileOutputStream out = new FileOutputStream(signatureFile);
		out.write(signature);
		out.close();
	}

	public void loadConfigInfo() throws LoadConfigInfoException
	{
		configInfo.clear();

		File sigFile = getConfigInfoSignatureFile();
		File dataFile = getConfigInfoFile();

		if(!dataFile.exists())
		{
			//System.out.println("MartusApp.loadConfigInfo: config file doesn't exist");
			return;
		}

		try
		{
			byte[] plainTextConfigInfo = verifyAndReadSignedFile(dataFile, sigFile);
			ByteArrayInputStream plainTextConfigInputStream = new ByteArrayInputStream(plainTextConfigInfo);
			configInfo = ConfigInfo.load(plainTextConfigInputStream);
			plainTextConfigInputStream.close();
			
			FieldSpecCollection specsTop = getCustomFieldSpecsTopSection(configInfo);
			removeSpaceLikeCharactersFromTags(specsTop);
			store.setTopSectionFieldSpecs(specsTop);
			FieldSpecCollection specsBottom = getCustomFieldSpecsBottomSection(configInfo);
			removeSpaceLikeCharactersFromTags(specsBottom);
			store.setBottomSectionFieldSpecs(specsBottom);
			
			convertLegacyHQToMultipleHQs();
			String languageCode = localization.getCurrentLanguageCode();
			if (languageCode != null && languageCode.equals(MtfAwareLocalization.BURMESE))
				configInfo.setUseZawgyiFont(true);
			FontSetter.setDefaultFont(configInfo.getUseZawgyiFont());
			FontHandler.setDoZawgyiConversion(configInfo.getDoZawgyiConversion());
		}
		catch (Exception e)
		{
			throw new LoadConfigInfoException(e);
		}
	}
	
	public void startOrStopTorAsRequested()
	{
		boolean isTorEnabled = getConfigInfo().useInternalTor();
		int newTimeout = 0;
		if(isTorEnabled)
			newTimeout = ClientSideNetworkHandlerUsingXmlRpc.TOR_GET_SERVER_INFO_TIMEOUT_SECONDS;
		else
			newTimeout = ClientSideNetworkHandlerUsingXmlRpc.WITHOUT_TOR_GET_SERVER_INFO_TIMEOUT_SECONDS;

		// NOTE: force the handler to be created if it wasn't already
		getCurrentNetworkInterfaceHandler();
		boolean isServerConfigured = (currentNetworkInterfaceHandler != null);
		if(isServerConfigured)
			currentNetworkInterfaceHandler.setTimeoutGetServerInfo(newTimeout);

		if(isTorEnabled)
			transport.start();
		else
			transport.stop();
	}

	private byte[] verifyAndReadSignedFile(File dataFile, File sigFile) throws Exception
	{
		String accountId = getSecurity().getPublicKeyString();
		if(!isSignatureFileValid(dataFile, sigFile, accountId))
			throw new SignatureVerificationException();

		InputStreamWithSeek encryptedInputStream = new FileInputStreamWithSeek(dataFile);
		ByteArrayOutputStream plainTextStream = new ByteArrayOutputStream();
		getSecurity().decrypt(encryptedInputStream, plainTextStream);

		byte[] plainText = plainTextStream.toByteArray();

		plainTextStream.close();
		encryptedInputStream.close();
		return plainText;
	}
	
	public void writeSignedUserDictionary(String string) throws Exception
	{
		encryptAndWriteFileAndSignatureFile(getDictionaryFile(), getDictionarySignatureFile(), string.getBytes("UTF-8"));
	}
	
	public String readSignedUserDictionary() throws Exception
	{
		File dictionaryFile = getDictionaryFile();
		File dictionarySignatureFile = getDictionarySignatureFile();
		
		if(!dictionaryFile.exists())
			return "";
		
		byte[] plainText = verifyAndReadSignedFile(dictionaryFile, dictionarySignatureFile);
		return new String(plainText, "UTF-8");
	}

	public static void removeSpaceLikeCharactersFromTags(FieldSpecCollection specs)
	{
		for(int i = 0; i < specs.size(); ++i)
		{
			String tag = specs.get(i).getTag();
			String stripped = stripSpaceLikeCharacters(tag);
			specs.get(i).setTag(stripped);
		}
	}
	
	public static String stripSpaceLikeCharacters(String tag)
	{
		String NORMAL_WHITESPACE = "\\s";
		String NON_BREAKING_SPACE = "\\xa0";
		String NARROW_NO_BREAK_SPACE = "\\u202f";
		String ZERO_WIDTH_NO_BREAK_SPACE = "\\ufeff";
		String WORD_JOINER = "\\u2060";
		String regex = "[" + NORMAL_WHITESPACE + NON_BREAKING_SPACE + 
				NARROW_NO_BREAK_SPACE + ZERO_WIDTH_NO_BREAK_SPACE + WORD_JOINER + "]";
		return tag.replaceAll(regex, "");
	}

	
	private boolean isSignatureFileValid(File dataFile, File sigFile, String accountId) throws FileNotFoundException, IOException, MartusSignatureException 
	{
		byte[] signature =	new byte[(int)sigFile.length()];
		FileInputStream inSignature = new FileInputStream(sigFile);
		inSignature.read(signature);
		inSignature.close();

		FileInputStream inData = new FileInputStream(dataFile);
		try
		{
			boolean verified = getSecurity().isValidSignatureOfStream(accountId, inData, signature);
			return verified;
		}
		finally
		{
			inData.close();
		}
	}

	private void convertLegacyHQToMultipleHQs() throws Exception
	{
		String legacyHQKey = configInfo.getLegacyHQKey();
		if(legacyHQKey.length()>0)
		{
			HeadquartersKeys hqKeys = getAllHQKeys();
			if(!hqKeys.containsKey(legacyHQKey))
			{
				HeadquartersKey legacy = new HeadquartersKey(legacyHQKey);
				hqKeys.add(legacy);
				try
				{
					setAndSaveHQKeys(hqKeys, hqKeys);
				}
				catch(MartusApp.SaveConfigInfoException e)
				{
					System.out.println("SaveConfigInfoException: " + e);						
				}
			}
		}
	}

	public static FieldSpecCollection getCustomFieldSpecsTopSection(ConfigInfo configInfo) throws CustomFieldsParseException
	{
		String xmlSpecs = configInfo.getCustomFieldTopSectionXml();
		if(xmlSpecs.length() > 0)
			return FieldCollection.parseXml(xmlSpecs);
			
		String legacySpecs = configInfo.getCustomFieldLegacySpecs();
		FieldSpecCollection specs = LegacyCustomFields.parseFieldSpecsFromString(legacySpecs);
		return specs;
	}

	public static FieldSpecCollection getCustomFieldSpecsBottomSection(ConfigInfo configInfo) throws CustomFieldsParseException
	{
		String xmlSpecs = configInfo.getCustomFieldBottomSectionXml();
		if(xmlSpecs.length() > 0)
			return FieldCollection.parseXml(xmlSpecs);
			
		FieldSpecCollection specs = StandardFieldSpecs.getDefaultBottomSectionFieldSpecs();
		return specs;
	}

	public void doAfterSigninInitalization() throws MartusAppInitializationException, FileVerificationException, MissingAccountMapException, MissingAccountMapSignatureException
	{
		store.doAfterSigninInitialization(getCurrentAccountDirectory());
	}
	
	public File getMartusDataRootDirectory()
	{
		return martusDataRootDirectory;
	}

	public File getCurrentAccountDirectory()
	{
		return currentAccountDirectory;
	}
	
	public File getPacketsDirectory()
	{
		return new File(getCurrentAccountDirectory(), PACKETS_DIRECTORY_NAME);
	}
	
	public File getAccountsDirectory()
	{
		return new File(getMartusDataRootDirectory(), ACCOUNTS_DIRECTORY_NAME);
	}
	
	public boolean shouldUseUnofficialTranslations()
	{
		return (new File(getMartusDataRootDirectory(), USE_UNOFFICIAL_TRANSLATIONS_NAME)).exists();
	}
	
	public File getDocumentsDirectory()
	{
		return new File(getMartusDataRootDirectory(), DOCUMENTS_DIRECTORY_NAME);
	}

	public String getCurrentAccountDirectoryName()
	{
		return getCurrentAccountDirectory().getPath() + "/";
	}

	public File getConfigInfoFile()
	{
		return getConfigInfoFileForAccount(getCurrentAccountDirectory());
	}
	
	public File getConfigInfoFileForAccount(File accountDirectory)
	{
		return new File(accountDirectory, "MartusConfig.dat");
	}

	public File getConfigInfoSignatureFile()
	{
		return getConfigInfoSignatureFileForAccount(getCurrentAccountDirectory());
	}

	public File getConfigInfoSignatureFileForAccount(File accountDirectory)
	{
		return new File(accountDirectory, "MartusConfig.sig");
	}

	public File getDictionaryFile()
	{
		return getDictionaryFileForAccount(getCurrentAccountDirectory());
	}
	
	public File getDictionaryFileForAccount(File accountDirectory)
	{
		return new File(accountDirectory, "Dictionary.dat");
	}

	public File getDictionarySignatureFile()
	{
		return getDictionarySignatureFileForAccount(getCurrentAccountDirectory());
	}

	public File getDictionarySignatureFileForAccount(File accountDirectory)
	{
		return new File(accountDirectory, "Dictionary.sig");
	}

	public File getUploadInfoFile()
	{
		return getUploadInfoFileForAccount(getCurrentAccountDirectory());
	}

	public File getUploadInfoFileForAccount(File accountDirectory)
	{
		return new File(accountDirectory, "MartusUploadInfo.dat");
	}

	public File getUiStateFileForAccount(File accountDirectory)
	{
		return new File(accountDirectory, "UserUiState.dat");
	}
	
	public File getBulletinDefaultDetailsFile()
	{
		return new File(getCurrentAccountDirectoryName(), "DefaultDetails" + DEFAULT_DETAILS_EXTENSION);
	}

	public File getRetrieveFile()
	{
		return getRetrieveFile(getCurrentAccountDirectory());
	}

	private static File getRetrieveFile(File accountDirectory)
	{
		return new File(accountDirectory, "Retrieve.dat");
	}

	public String getLegacyUploadLogFilename()
	{
		return getLegacyUploadLogFileForAccount(getCurrentAccountDirectory()).getAbsolutePath();
	}

	private File getLegacyUploadLogFileForAccount(File accountDirectory)
	{
		return new File(accountDirectory, "MartusUploadLog.txt");
	}

	public InputStream getHelpMain(String currentLanguageCode)
	{
		return getHelp(currentLanguageCode, getHelpFilename(currentLanguageCode));
	}
	
	public InputStream getHelpTOC(String currentLanguageCode)
	{
		return getHelp(currentLanguageCode, getHelpTOCFilename(currentLanguageCode));
	}
	
	public URL getUrlOfDirectoryContainingDictionaries(String currentLanguageCode)
	{
		String dictionaryName = "dictionary_" + currentLanguageCode + ".ortho";
		try 
		{
			File mlpFile = localization.getMlpkFile(currentLanguageCode);
			if(mlpFile.exists() && 
			   JarVerifier.verify(mlpFile,false) == JarVerifier.JAR_VERIFIED_TRUE)
			{
				URL url = new URL("jar:file:/" + mlpFile.getAbsolutePath() + "!" + "dictionaries/" + dictionaryName);
				return url;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		String dictionaryDirectory = "";
		URL relativeURL = EnglishStrings.class.getResource(dictionaryDirectory + dictionaryName);
		if(relativeURL == null)
			dictionaryDirectory = "/";
		return EnglishStrings.class.getResource(dictionaryDirectory);
		
	}
	
	private InputStream getHelp(String currentLanguageCode, String helpFileName)
	{
		if(!localization.isOfficialTranslation(currentLanguageCode))
			return null;

		try 
		{
			File mlpFile = localization.getMlpkFile(currentLanguageCode);
			if(mlpFile.exists() && 
			   JarVerifier.verify(mlpFile,false) == JarVerifier.JAR_VERIFIED_TRUE)
			{
				ZipFile zip = new ZipFile(mlpFile);
				ZipEntry zipEntry = zip.getEntry(helpFileName);
				ZipEntryInputStreamWithSeekThatClosesZipFile stream = new ZipEntryInputStreamWithSeekThatClosesZipFile(zip, zipEntry);
				return stream;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return EnglishStrings.class.getResourceAsStream(helpFileName);
	}
	
	public String getHelpFilename(String languageCode)
	{
		String helpFile = "MartusHelp-" + languageCode + ".txt";
		return helpFile;
	}

	public String getHelpTOCFilename(String languageCode)
	{
		String helpFile = "MartusHelpTOC-" + languageCode + ".txt";
		return helpFile;
	}
	
	public void UpdateDocsIfNecessaryFromMLPFiles()
	{
		File[] mlpFiles = GetMlpFiles();
		for(int i = 0; i < mlpFiles.length; ++i)
		{
			File mlpFile = mlpFiles[i];
			extractNewerPDFDocumentation(mlpFile);
			extractNewerReadMeDocumentation(mlpFile);
		}
	}

	private void extractNewerReadMeDocumentation(File mlpFile)
	{
		File targetDirectory = getMartusDataRootDirectory();
		String readMeFiles = "README";
		String fileExtension = ".txt";
		extractMatchingFileTypesFromJar(mlpFile, targetDirectory, readMeFiles, fileExtension);
	}

	private void extractNewerPDFDocumentation(File mlpFile)
	{
		File targetDirectory = getDocumentsDirectory();
		String anyPdfFile = "";
		String fileExtension = ".pdf";
		extractMatchingFileTypesFromJar(mlpFile, targetDirectory, anyPdfFile, fileExtension);
	}

	private void extractMatchingFileTypesFromJar(File mlpFile, File targetDirectory, String filesBeginningWith, String filesEndingWith)
	{
		if(JarVerifier.verify(mlpFile, false) != JarVerifier.JAR_VERIFIED_TRUE)
			return;
		JarFile jar = null;
		try
		{
			jar = new JarFile(mlpFile);
			Enumeration entries = jar.entries();
			while(entries.hasMoreElements())
			{
				JarEntry entry = (JarEntry) entries.nextElement();
				String jarEntryName = entry.getName();
				if(filesBeginningWith.length() > 0)
				{
					if(!jarEntryName.startsWith(filesBeginningWith))
						continue;
				}
				if(!jarEntryName.endsWith(filesEndingWith))
					continue;
				File fileOnDisk = new File(targetDirectory, jarEntryName);
				if(isFileNewerOnDisk(fileOnDisk, entry))
					continue;
					
				fileOnDisk.delete();
				targetDirectory.mkdirs();
				copyJarEntryToFile(jar, entry, fileOnDisk);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(jar != null)
					jar.close();
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}
	

	public boolean isFileNewerOnDisk(File fileToCheck, ZipEntry entry)
	{
		if(!fileToCheck.exists())
			return false;
		Date zipFileDate = new Date(entry.getTime());
		Date currentFileDate = new Date(fileToCheck.lastModified());
		return(zipFileDate.before(currentFileDate));
	}

	private void copyJarEntryToFile(JarFile jar, JarEntry entry, File outputFile) throws IOException, FileNotFoundException
	{
		InputStream in = jar.getInputStream(entry);
		FileOutputStream out = new FileOutputStream(outputFile);
		StreamCopier copier = new StreamCopier();
		copier.copyStream(in, out);
		//TODO put closes in a finally block.
		in.close();
		out.close();
		outputFile.setLastModified(entry.getTime());
	}

	public static File getTranslationsDirectory()
	{
		return determineMartusDataRootDirectory();
	}

	public File getCurrentKeyPairFile()
	{
		File dir = getCurrentAccountDirectory();
		return getKeyPairFile(dir);
	}

	public File getKeyPairFile(File dir)
	{
		return new File(dir, KEYPAIR_FILENAME);
	}	

	public static File getBackupFile(File original)
	{
		return new File(original.getPath() + ".bak");
	}
	
	public String getUserName()
	{
		return currentUserName;
	}

	public void loadFolders()
	{
		store.loadFolders();
	}

	public ClientBulletinStore getStore()
	{
		return store;
	}
	
	public RetrieveCommand getCurrentRetrieveCommand()
	{
		return currentRetrieveCommand;
	}

	public MtfAwareLocalization getLocalization()
	{
		return localization;
	}

	public void startBackgroundRetrieve(RetrieveCommand rc) throws MartusSignatureException, NoKeyPairException, EncryptionException, IOException
	{
		currentRetrieveCommand = rc;
		saveRetrieveCommand();
	}
	
	public void loadRetrieveCommand() throws Exception
	{
		final File retrieveFile = getRetrieveFile();
		if(!retrieveFile.exists())
			return;

		int size = (int)retrieveFile.length();
		byte[] bundle = new byte[size];
		FileInputStream in = new FileInputStream(retrieveFile);
		try
		{
			in.read(bundle);
		}
		finally
		{
			in.close();
		}
		startBackgroundRetrieve(parseRetrieveCommandBundle(bundle));
	}

	private void saveRetrieveCommand() throws MartusSignatureException, IOException, NoKeyPairException, EncryptionException, FileNotFoundException
	{
		byte[] retrieveCommandBytes = createRetrieveCommandBundle(getCurrentRetrieveCommand());
		FileOutputStream out = new FileOutputStream(getRetrieveFile());
		try
		{
			out.write(retrieveCommandBytes);
		}
		finally
		{
			out.close();
		}
	}
	
	public void cancelBackgroundRetrieve() throws MartusSignatureException, NoKeyPairException, EncryptionException, IOException
	{
		startBackgroundRetrieve(new RetrieveCommand());
	}
	
	public byte[] createRetrieveCommandBundle(RetrieveCommand rc) throws MartusSignatureException, IOException, NoKeyPairException, EncryptionException
	{
		try
		{
			byte[] plainText = rc.toJson().toString().getBytes("UTF-8");
			ByteArrayOutputStream encryptedBytes = new ByteArrayOutputStream();
			MartusCrypto security = store.getSignatureGenerator();
			security.encrypt(new ByteArrayInputStream(plainText), encryptedBytes);
			return security.createSignedBundle(encryptedBytes.toByteArray());
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public RetrieveCommand parseRetrieveCommandBundle(byte[] bundle) throws Exception
	{
		MartusCrypto security = store.getSignatureGenerator();
		byte[] encryptedBytes = security.extractFromSignedBundle(bundle);
		ByteArrayOutputStream plainTextBytes = new ByteArrayOutputStream();
		security.decrypt(new ByteArrayInputStreamWithSeek(encryptedBytes), plainTextBytes);
		String jsonString = new String(plainTextBytes.toByteArray(), "UTF-8");
		return new RetrieveCommand(new JSONObject(jsonString));
	}

	public Bulletin createBulletin() throws Exception
	{
		Bulletin b = store.createEmptyBulletin();
		b.set(Bulletin.TAGAUTHOR, configInfo.getAuthor());
		b.set(Bulletin.TAGORGANIZATION, configInfo.getOrganization());
		b.set(Bulletin.TAGPUBLICINFO, configInfo.getTemplateDetails());
		b.set(Bulletin.TAGLANGUAGE, getDefaultLanguageForNewBulletin());
		setDefaultHQKeysInBulletin(b);
		b.setDraft();
		b.setAllPrivate(true);
		return b;
	}

	public String getDefaultLanguageForNewBulletin()
	{
		final String preferredLanguage = getCurrentLanguage();
		ChoiceItem[] availableLanguages = localization.getLanguageNameChoices();
		for(int i=0; i < availableLanguages.length; ++i)
		{
			ChoiceItem item = availableLanguages[i];
			if(item.getCode().equals(preferredLanguage))
				return preferredLanguage;
		}
		
		return MiniLocalization.LANGUAGE_OTHER;
	}

	public void setDefaultHQKeysInBulletin(Bulletin b)
	{
		HeadquartersKeys hqKeys = getDefaultHQKeysWithFallback();
		b.setAuthorizedToReadKeys(hqKeys);
	}

	public BulletinFolder getFolderSaved()
	{
		return store.getFolderSaved();
	}

	public BulletinFolder getFolderDiscarded()
	{
		return store.getFolderDiscarded();
	}

	public BulletinFolder getFolderSealedOutbox()
	{
		return store.getFolderSealedOutbox();
	}

	public BulletinFolder getFolderDraftOutbox()
	{
		return store.getFolderDraftOutbox();
	}

	public BulletinFolder createFolderRetrieved()
	{
		String folderName = getNameOfFolderRetrievedSealed();
		return createOrFindFolder(folderName);
	}

	public BulletinFolder createFolderRetrievedFieldOffice()
	{
		String folderName = getNameOfFolderRetrievedFieldOfficeSealed();
		return createOrFindFolder(folderName);
	}

	public String getNameOfFolderRetrievedSealed()
	{
		return store.getNameOfFolderRetrievedSealed();
	}

	public String getNameOfFolderRetrievedDraft()
	{
		return store.getNameOfFolderRetrievedDraft();
	}

	public String getNameOfFolderRetrievedFieldOfficeSealed()
	{
		return store.getNameOfFolderRetrievedFieldOfficeSealed();
	}

	public String getNameOfFolderRetrievedFieldOfficeDraft()
	{
		return store.getNameOfFolderRetrievedFieldOfficeDraft();
	}

	public BulletinFolder createOrFindFolder(String name)
	{
		return store.createOrFindFolder(name);
	}

	public void setMaxNewFolders(int numFolders)
	{
		maxNewFolders = numFolders;
	}

	public BulletinFolder createUniqueFolder(String originalFolderName)
	{
		BulletinFolder newFolder = null;
		String uniqueFolderName = null;
		int folderIndex = 0;
		while (newFolder == null && folderIndex < maxNewFolders)
		{
			uniqueFolderName = originalFolderName;
			if (folderIndex > 0)
				uniqueFolderName += folderIndex;
			newFolder = store.createFolder(uniqueFolderName);
			++folderIndex;
		}
		if(newFolder != null)
			store.saveFolders();
		return newFolder;
	}
	
	public void cleanupWhenCompleteQuickErase()
	{
		store.deleteFoldersDatFile();	
	}
	
	public void deleteKeypairAndRelatedFilesForAccount(File accountDirectory)
	{
		File keyPairFile = getKeyPairFile(accountDirectory);
		DirectoryUtils.scrubAndDeleteFile(keyPairFile);
		DirectoryUtils.scrubAndDeleteFile(getBackupFile(keyPairFile));
		DirectoryUtils.scrubAndDeleteFile(getUserNameHashFile(keyPairFile.getParentFile()));
		DirectoryUtils.scrubAndDeleteFile(getConfigInfoFileForAccount(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(getConfigInfoSignatureFileForAccount(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(getUploadInfoFileForAccount(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(getUiStateFileForAccount(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(ClientBulletinStore.getFoldersFileForAccount(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(ClientBulletinStore.getCacheFileForAccount(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(ClientBulletinStore.getFieldSpecCacheFile(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(getRetrieveFile(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(getDictionaryFileForAccount(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(getDictionarySignatureFileForAccount(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(getLegacyUploadLogFileForAccount(accountDirectory));
		DirectoryUtils.scrubAndDeleteFile(new File(accountDirectory, "velocity.log"));

		File[] exportedKeys = exportedPublicKeyFiles(accountDirectory);
		for (int i = 0; i < exportedKeys.length; i++)
		{
			File file = exportedKeys[i];
			DirectoryUtils.scrubAndDeleteFile(file);
		}

		File[] reportFormats = reportFormatFiles(accountDirectory, getLocalization());
		for (int i = 0; i < reportFormats.length; i++)
		{
			File file = reportFormats[i];
			DirectoryUtils.scrubAndDeleteFile(file);
		}
	}

	private static File[] exportedPublicKeyFiles(File accountDir)
	{
		File[] mpiFiles = accountDir.listFiles(new FileFilter()
		{
			public boolean accept(File file)
			{
				return (file.isFile() && file.getName().endsWith(".mpi"));	
			}
		});
		return mpiFiles;
	}

	private static File[] reportFormatFiles(File accountDir, MiniLocalization localization)
	{
		File[] reportFormatFiles = accountDir.listFiles(new ReportFormatFilter(localization));
		return reportFormatFiles;
	}

	private File[] GetMlpFiles()
	{
		File[] mpiFiles = martusDataRootDirectory.listFiles(new FileFilter()
		{
			public boolean accept(File file)
			{
				return (file.isFile() && file.getName().endsWith(MtfAwareLocalization.MARTUS_LANGUAGE_PACK_SUFFIX));	
			}
		});
		return mpiFiles;
	}

	public boolean deleteAllBulletinsAndUserFolders()
	{
		try
		{											
			store.scrubAllData();
			store.deleteAllData();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public int quarantineUnreadableBulletins()
	{
		return store.quarantineUnreadableBulletins();
	}

	public int repairOrphans()
	{
		Set orphans = store.getSetOfOrphanedBulletinUniversalIds();
		int foundOrphanCount = orphans.size();
		if(foundOrphanCount == 0)
			return 0;

		Iterator it = orphans.iterator();
		while(it.hasNext())
		{
			UniversalId uid = (UniversalId)it.next();
			try
			{
				store.addRepairBulletinToFolders(uid);
			}
			catch (BulletinAlreadyExistsException e)
			{
				System.out.println("Orphan Bulletin already exists.");
			}
			catch (IOException shouldNeverHappen)
			{
				shouldNeverHappen.printStackTrace();
			}
		}

		store.saveFolders();
		return foundOrphanCount;
	}

	public boolean isFieldExpanded(String tag) 
	{
		Boolean isExpanded = (Boolean)fieldExpansionStates.get(tag);
		if(isExpanded == null)
			return true;
		return isExpanded.booleanValue();
	}

	public void setFieldExpansionState(String tag, boolean b) 
	{
		fieldExpansionStates.put(tag, new Boolean(b));
	}
	
	public boolean isGridExpanded(String tag)
	{
		Boolean isExpanded = (Boolean)gridExpansionStates.get(tag);
		if(isExpanded == null)
			return false;
		return isExpanded.booleanValue();
	}
	
	public void setGridExpansionState(String tag, boolean b)
	{
		gridExpansionStates.put(tag, new Boolean(b));
	}

	public Vector findBulletinInAllVisibleFolders(Bulletin b)
	{
		return findBulletinInAllVisibleFolders(b.getUniversalId());
	}

	public Vector findBulletinInAllVisibleFolders(UniversalId uid)
	{
		return store.findBulletinInAllVisibleFolders(uid);
	}

	public boolean isDraftOutboxEmpty()
	{
		if(getFolderDraftOutbox().getBulletinCount() == 0)
			return true;
		return false;
	}

	public boolean isSealedOutboxEmpty()
	{
		if(getFolderSealedOutbox().getBulletinCount() == 0)
			return true;
		return false;
	}
	
	public void discardBulletinsFromFolder(BulletinFolder folderToDiscardFrom, UniversalId[] bulletinIDsToDiscard) throws IOException 
	{
		store.getFolderDiscarded().prepareForBulkOperation();
		for (int i = 0; i < bulletinIDsToDiscard.length; i++)
		{
			UniversalId uid = bulletinIDsToDiscard[i];
			store.discardBulletin(folderToDiscardFrom, uid);
		}
		store.saveFolders();
	}

	public Date getUploadInfoElement(int index)
	{
		File file = getUploadInfoFile();
		if (!file.canRead())
			return null;
		Date date = null;
		try
		{
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
			for(int i = 0 ; i < index ; ++i)
			{
				stream.readObject();
			}
			date = (Date)stream.readObject();
			stream.close();
		}
		catch (Exception e)
		{
			System.out.println("Error reading from getUploadInfoElement " + index + ":" + e);
		}
		return date;

	}

	public Date getLastUploadedTime()
	{
		return(getUploadInfoElement(0));
	}

	public Date getLastUploadRemindedTime()
	{
		return(getUploadInfoElement(1));
	}


	public void setUploadInfoElements(Date uploaded, Date reminded)
	{
		File file = getUploadInfoFile();
		file.delete();
		try
		{
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
			stream.writeObject(uploaded);
			stream.writeObject(reminded);
			stream.close();
		}
		catch (Exception e)
		{
			System.out.println("Error writing to setUploadInfoElements:" + e);
		}

	}

	public void setLastUploadedTime(Date uploaded)
	{
		Date reminded = getLastUploadRemindedTime();
		setUploadInfoElements(uploaded, reminded);
	}

	public void setLastUploadRemindedTime(Date reminded)
	{
		Date uploaded = getLastUploadedTime();
		setUploadInfoElements(uploaded, reminded);
	}

	public void resetLastUploadedTime()
	{
		setLastUploadedTime(new Date());
	}

	public void resetLastUploadRemindedTime()
	{
		setLastUploadRemindedTime(new Date());
	}

	public SortableBulletinList search(SearchTreeNode searchNode, MiniFieldSpec[] specsForSorting, MiniFieldSpec[] extraSpecs, boolean searchFinalVersionsOnly, boolean searchSameRowsOnly, ProgressMeterInterface progressMeter)
	{
		Stopwatch stopWatch = new Stopwatch();
		stopWatch.start();
		long revisionsSearched = 0;
		BulletinSearcher matcher = new BulletinSearcher(searchNode, searchSameRowsOnly);
		matcher.setDoZawgyiConversion(configInfo.getDoZawgyiConversion());
		SortableBulletinList matchedBulletinUids = new SortableBulletinList(localization, specsForSorting, extraSpecs);

		Set uids = store.getAllBulletinLeafUids();
		int totalCount = uids.size();
		int progressCount = 0;
		for(Iterator iter = uids.iterator(); iter.hasNext();)
		{
			if(progressMeter.shouldExit())
				break;
			progressMeter.updateProgressMeter(++progressCount, totalCount);
			UniversalId leafBulletinUid = (UniversalId) iter.next();
			Vector visibleFoldersContainingThisBulletin = findBulletinInAllVisibleFolders(leafBulletinUid);
			visibleFoldersContainingThisBulletin.remove(getFolderDiscarded());
			if(visibleFoldersContainingThisBulletin.size() == 0)
				continue;
			
			Vector allRevisions = getRevisionUidsToSearch(leafBulletinUid, searchFinalVersionsOnly);		
			
			for(int j = 0; j < allRevisions.size(); ++j)
			{
				Bulletin b = store.getBulletinRevision((UniversalId)allRevisions.get(j));
				++revisionsSearched;
				if(b != null && matcher.doesMatch(new SafeReadableBulletin(b, localization), localization))
				{
					Bulletin latestRevision = store.getBulletinRevision(leafBulletinUid);
					matchedBulletinUids.add(latestRevision);
					break;
				}
			}
		}
		stopWatch.stop();
		Logger.global.info("Search took:"+stopWatch.elapsedInSeconds()+" Seconds, " + matchedBulletinUids.size() +" matches found, " +uids.size()+" leafs, "+ revisionsSearched + " revisions were searched.");
		
		return matchedBulletinUids;
	}

	private Vector getRevisionUidsToSearch(UniversalId leafBulletinUid, boolean searchFinalVersionsOnly)
	{
		Vector allRevisions = new Vector();
		allRevisions.add(leafBulletinUid);
		if(!searchFinalVersionsOnly)
		{
			String authorAccountId = leafBulletinUid.getAccountId();
			BulletinHistory history = store.getBulletinHistory(leafBulletinUid);
			for(int h=0; h<history.size(); ++h)
			{
				allRevisions.add(UniversalId.createFromAccountAndLocalId(authorAccountId, history.get(h)));
			}
		}
		return allRevisions;
	}
	
	public void updateSearchFolder(SortableBulletinList partialBulletinsToAdd)
	{
		BulletinFolder searchFolder = createOrFindFolder(store.getSearchFolderName());
		searchFolder.removeAll();
		searchFolder.prepareForBulkOperation();
		UniversalId[] uids = partialBulletinsToAdd.getUniversalIds();
		for(int i = 0; i < uids.length; ++i)
		{
			UniversalId leafBulletinUid = uids[i];
			try
			{
				store.addBulletinToFolder(searchFolder, leafBulletinUid);
			}
			catch (BulletinAlreadyExistsException safeToIgnoreException)
			{
			}
			catch (AddOlderVersionToFolderFailedException safeToIgnoreException)
			{
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		store.saveFolders();
	}

	public boolean isNonSSLServerAvailable(String serverName) throws Exception
	{
		if(serverName.length() == 0)
			return false;

		NonSSLNetworkAPI server = new ClientSideNetworkHandlerUsingXmlRpcWithUnverifiedServer(serverName, transport);
		return ClientSideNetworkHandlerUsingXmlRpcWithUnverifiedServer.isNonSSLServerAvailable(server);
	}

	public boolean isSSLServerAvailable()
	{
		if(currentNetworkInterfaceHandler == null && !isServerConfigured())
			return false;

		return isSSLServerAvailable(getCurrentNetworkInterfaceGateway());
	}
	
	public boolean isServerConfigured()
	{
		return (getServerName().length() > 0);
	}

	public boolean isSignedIn()
	{
		return getSecurity().hasKeyPair();
	}

	public String getServerPublicKey(String serverName) throws Exception
	{
		ClientSideNetworkHandlerUsingXmlRpcWithUnverifiedServer server = new ClientSideNetworkHandlerUsingXmlRpcWithUnverifiedServer(serverName, transport);
		return getServerPublicKey(server);
	}

	public String getServerPublicKey(NonSSLNetworkAPIWithHelpers server) throws
		ServerNotAvailableException,
		PublicInformationInvalidException
	{
		return server.getServerPublicKey(getSecurity());
	}

	public boolean requestServerUploadRights(String magicWord)
	{
		try
		{
			NetworkResponse response = getCurrentNetworkInterfaceGateway().getUploadRights(getSecurity(), magicWord);
			if(response.getResultCode().equals(NetworkInterfaceConstants.OK))
				return true;
		}
		catch(MartusCrypto.MartusSignatureException e)
		{
			System.out.println("MartusApp.requestServerUploadRights: " + e);
		}

		return false;
	}

	public Vector getNewsFromServer()
	{
		if(!isSSLServerAvailable())
			return new Vector();

		try
		{
			NetworkResponse response = getCurrentNetworkInterfaceGateway().getNews(getSecurity(), UiConstants.versionLabel);
			if(response.getResultCode().equals(NetworkInterfaceConstants.OK))
				return response.getResultVector();
		}
		catch (MartusSignatureException e)
		{
			System.out.println("MartusApp.getNewsFromServer :" + e);
		}
		return new Vector();
	}

	public String getServerCompliance(ClientSideNetworkGateway gateway) 
		throws ServerCallFailedException, ServerNotAvailableException
	{
		if(!isSSLServerAvailable(gateway))
			throw new ServerNotAvailableException();
		try
		{
			NetworkResponse response = gateway.getServerCompliance(getSecurity());
			if(response.getResultCode().equals(NetworkInterfaceConstants.OK))
				return (String)response.getResultVector().get(0);
		}
		catch (Exception e)
		{
			//System.out.println("MartusApp.getServerCompliance :" + e);
			MartusLogger.logException(e);
			throw new ServerCallFailedException();
		}		
		throw new ServerCallFailedException();
	}

	void moveBulletinToDamaged(BulletinFolder outbox, UniversalId uid)
	{
		System.out.println("Moving bulletin to damaged");
		BulletinFolder damaged = createOrFindFolder(store.getNameOfFolderDamaged());
		Bulletin b = store.getBulletinRevision(uid);
		store.moveBulletin(b, outbox, damaged);		
	}

	public Vector downloadFieldOfficeAccountIds() throws ServerErrorException
	{
		if(!isSSLServerAvailable())
			throw new ServerErrorException();

		ClientSideNetworkGateway networkInterfaceGateway = getCurrentNetworkInterfaceGateway();
		MartusCrypto security = getSecurity();
		String myAccountId = getAccountId();

		return networkInterfaceGateway.downloadFieldOfficeAccountIds(security, myAccountId);
	}
	
	public BulletinHeaderPacket retrieveHeaderPacketFromServer(UniversalId bulletinId) throws Exception
	{
		BulletinHeaderPacket bhp = new BulletinHeaderPacket(bulletinId);
		populatePacketFromServer(bhp, bulletinId.getLocalId());
		return bhp;
	}

	public FieldDataPacket retrieveFieldDataPacketFromServer(UniversalId bulletinId, String dataPacketLocalId) throws Exception
	{
		UniversalId packetUid = UniversalId.createFromAccountAndLocalId(bulletinId.getAccountId(), dataPacketLocalId);
		FieldDataPacket fdp = new FieldDataPacket(packetUid, StandardFieldSpecs.getDefaultTopSetionFieldSpecs());
		populatePacketFromServer(fdp, bulletinId.getLocalId());
		return fdp;
	}

	private void populatePacketFromServer(Packet packet, String bulletinLocalId) throws MartusSignatureException, ServerErrorException, UnsupportedEncodingException, InvalidBase64Exception, IOException, InvalidPacketException, WrongPacketTypeException, SignatureVerificationException, DecryptionException, NoKeyPairException
	{
		NetworkResponse response = getCurrentNetworkInterfaceGateway().getPacket(getSecurity(), packet.getAccountId(), bulletinLocalId, packet.getLocalId());
		String resultCode = response.getResultCode();
		if(!resultCode.equals(NetworkInterfaceConstants.OK))
			throw new ServerErrorException(resultCode);

		String xmlEncoded = (String)response.getResultVector().get(0);
		String xml = new String(StreamableBase64.decode(xmlEncoded), "UTF-8");
		byte[] xmlBytes = xml.getBytes("UTF-8");
		ByteArrayInputStreamWithSeek in =  new ByteArrayInputStreamWithSeek(xmlBytes);
		packet.loadFromXml(in, getSecurity());
	}

	public BulletinSummary createSummaryFromString(String accountId, String summaryAsString) throws WrongValueCount
	{
		BulletinSummary summary = BulletinSummary.createFromString(accountId, summaryAsString);
		Bulletin bulletin = store.getBulletinRevision(summary.getUniversalId());
		if (bulletin != null)
			summary.setFieldDataPacket(bulletin.getFieldDataPacket());
		return summary;
	}

	public void setFieldDataPacketFromServer(BulletinSummary summary) throws Exception
	{
		if(!FieldDataPacket.isValidLocalId(summary.getFieldDataPacketLocalId()))
			throw new ServerErrorException();
	
		summary.setFieldDataPacket(retrieveFieldDataPacketFromServer(summary.getUniversalId(), summary.getFieldDataPacketLocalId()));
	}

	public void retrieveNextBackgroundBulletin() throws Exception
	{
		RetrieveCommand rc = getCurrentRetrieveCommand();
		UniversalId uid = rc.getNextToRetrieve();
		BulletinFolder folder = createOrFindFolder(rc.getFolderName());
		try
		{
			retrieveOneBulletinToFolder(uid, folder, null);
		}
		catch(NotYourBulletinErrorException okIfPreviousVersionIsNotAuthorizedToRead)
		{
		}
		catch(BulletinNotFoundException okIfPreviousVersionIsNotOnServer)
		{
		}
		catch(AddOlderVersionToFolderFailedException okIfOlderVersionWasNotAddedToRetrievedFolder)
		{
		}
		finally
		{
			rc.markAsRetrieved(uid);
			saveRetrieveCommand();
		}
		
	}

	public void retrieveOneBulletinToFolder(UniversalId uid, BulletinFolder retrievedFolder, ProgressMeterInterface progressMeter) throws
		AddOlderVersionToFolderFailedException, Exception
	{
		File tempFile = getCurrentNetworkInterfaceGateway().retrieveBulletin(uid, getSecurity(), serverChunkSize, progressMeter);
		try
		{
			store.importZipFileBulletin(tempFile, retrievedFolder, true);
			Bulletin b = store.getBulletinRevision(uid);
			store.setIsOnServer(b);
		}
		finally
		{
			tempFile.delete();
		}
	}

	public String deleteServerDraftBulletins(Vector uidList) throws
		MartusSignatureException,
		WrongAccountException
	{
		String[] localIds = new String[uidList.size()];
		for (int i = 0; i < localIds.length; i++)
		{
			UniversalId uid = (UniversalId)uidList.get(i);
			if(!uid.getAccountId().equals(getAccountId()))
				throw new WrongAccountException();

			localIds[i] = uid.getLocalId();
		}
		NetworkResponse response = getCurrentNetworkInterfaceGateway().deleteServerDraftBulletins(getSecurity(), getAccountId(), localIds);
		return response.getResultCode();
	}

	public static class AccountAlreadyExistsException extends Exception 
	{
	}

	public static class CannotCreateAccountFileException extends IOException 
	{
	}

	public void createAccount(String userName, char[] userPassPhrase) throws
					Exception
	{
		if(doesAccountExist(userName, userPassPhrase))
			throw new AccountAlreadyExistsException();
		
		if(doesDefaultAccountExist())
			createAdditionalAccount(userName, userPassPhrase);
		else
			createAccountInternal(getMartusDataRootDirectory(), userName, userPassPhrase);
	}

	public boolean doesAccountExist(String userName, char[] userPassPhrase) throws Exception
	{
		return (getAccountDirectoryForUser(userName, userPassPhrase) != null);
	}

	public File getAccountDirectoryForUser(String userName, char[] userPassPhrase) throws Exception
	{
		Vector allAccountDirs = getAllAccountDirectories();
		MartusCrypto tempSecurity = new MartusSecurity();
		for(int i = 0; i<allAccountDirs.size(); ++i )
		{
			File testAccountDirectory = (File)allAccountDirs.get(i);
			if(isUserOwnerOfThisAccountDirectory(tempSecurity, userName, userPassPhrase, testAccountDirectory))
				return testAccountDirectory;
		}
		return null;
	}

	private void createAdditionalAccount(String userName, char[] userPassPhrase) throws Exception
	{
		File tempAccountDir = null;
		try
		{
			File accountsDirectory = getAccountsDirectory();
			accountsDirectory.mkdirs();
			tempAccountDir = File.createTempFile("temp", null, accountsDirectory);
			tempAccountDir.delete();
			tempAccountDir.mkdirs();
			createAccountInternal(tempAccountDir, userName, userPassPhrase);
			String realAccountDirName = getAccountDirectoryName(getAccountId());
			File realAccountDir = new File(accountsDirectory, realAccountDirName);

			if(tempAccountDir.renameTo(realAccountDir))
				setCurrentAccount(userName, realAccountDir);
			else
				System.out.println("createAdditionalAccount rename failed.");
		}
		catch (Exception e)
		{
			System.out.println("createAdditionalAccount failed.");
			DirectoryUtils.deleteEntireDirectoryTree(tempAccountDir);
			throw(e);
		}
	}

	public void createAccountInternal(File accountDataDirectory, String userName, char[] userPassPhrase) throws
		Exception
	{
		MartusLogger.log("Creating account with " + MartusCrypto.getBitsWhenCreatingKeyPair() + " bits");
		File keyPairFile = getKeyPairFile(accountDataDirectory);
		if(keyPairFile.exists())
			throw(new AccountAlreadyExistsException());
		getSecurity().clearKeyPair();
		getSecurity().createKeyPair();
		try
		{
			writeKeyPairFileWithBackup(keyPairFile, userName, userPassPhrase);
			attemptSignInInternal(keyPairFile, userName, userPassPhrase);
		}
		catch(Exception e)
		{
			getSecurity().clearKeyPair();
			throw(e);
		}
	}
	
	public Vector getAllAccountDirectories()
	{
		Vector accountDirectories = new Vector();
		accountDirectories.add(getMartusDataRootDirectory());
		File accountsDirectoryRoot = getAccountsDirectory();
		File[] contents = accountsDirectoryRoot.listFiles();
		if(contents== null)
			return accountDirectories;
		for (int i = 0; i < contents.length; i++)
		{
			File thisFile = contents[i];
			try
			{
				if(!thisFile.isDirectory())
				{	
					continue;
				}
				String name = thisFile.getName();
				if(name.length() != 24)
				{	
					continue;
				}
				if(MartusCrypto.removeNonDigits(name).length() != 20)
				{	
					continue;
				}
				accountDirectories.add(thisFile);
			}
			catch (Exception notAValidAccountDirectory)
			{
			}
		}
		return accountDirectories;
	}
	
	public File getAccountDirectory(String accountId) throws InvalidBase64Exception
	{
		String name = getAccountDirectoryName(accountId);
		File proposedAccountDir = new File(getAccountsDirectory(), name);
		if(proposedAccountDir.exists() && proposedAccountDir.isDirectory())
			return proposedAccountDir;
		
		File dataRootDir = getMartusDataRootDirectory();
		if(!getKeyPairFile(dataRootDir).exists())
			return dataRootDir;
		if(doesDirectoryContainAccount(dataRootDir, accountId))
			return dataRootDir;
		
		proposedAccountDir.mkdirs();
		return proposedAccountDir;
	}

	private String getAccountDirectoryName(String accountId)
		throws InvalidBase64Exception
	{
		return MartusCrypto.getFormattedPublicCode(accountId);
	}
	
	private boolean doesDirectoryContainAccount(File dir, String accountId)
	{
		File configFile = getConfigInfoFileForAccount(dir);
		File sigFile = getConfigInfoSignatureFileForAccount(dir);
		
		try 
		{
			return(isSignatureFileValid(configFile, sigFile, accountId));
		} 
		catch (Exception e) 
		{
			return false;
		}
	}

	public boolean doesAnyAccountExist()
	{
		Vector accountDirectories = getAllAccountDirectories();
		for (int i = 0; i < accountDirectories.size(); i++)
		{
			File thisDirectory = (File)accountDirectories.get(i);
			if(getKeyPairFile(thisDirectory).exists())
				return true;
		}
		return false;
	}
	
	public boolean doesDefaultAccountExist()
	{
		if(getKeyPairFile(getMartusDataRootDirectory()).exists())
			return true;

		File packetsDir = new File(getMartusDataRootDirectory(), PACKETS_DIRECTORY_NAME);
		if(!packetsDir.exists())
			return false;

		return (packetsDir.listFiles().length > 0);
	}

	public void exportPublicInfo(File exportFile) throws
		IOException,
		StreamableBase64.InvalidBase64Exception,
		MartusCrypto.MartusSignatureException
	{
		MartusUtilities.exportClientPublicKey(getSecurity(), exportFile);
	}

	public String extractPublicInfo(File file) throws
		IOException,
		StreamableBase64.InvalidBase64Exception,
		PublicInformationInvalidException
	{
		Vector importedPublicKeyInfo = MartusUtilities.importClientPublicKeyFromFile(file);
		String publicKey = (String) importedPublicKeyInfo.get(0);
		String signature = (String) importedPublicKeyInfo.get(1);
		MartusUtilities.validatePublicInfo(publicKey, signature, getSecurity());
		return publicKey;
	}

	public File getPublicInfoFile(String fileName)
	{
		fileName = MartusUtilities.toFileName(fileName);
		String completeFileName = fileName + PUBLIC_INFO_EXTENSION;
		return(new File(getCurrentAccountDirectoryName(), completeFileName));
	}

	public void attemptSignIn(String userName, char[] userPassPhrase) throws Exception
	{
		File keyPairFile = getAccountDirectoryForUser(userName, userPassPhrase);
		attemptSignInInternal(getKeyPairFile(keyPairFile), userName, userPassPhrase);
	}
	
	public void attemptReSignIn(String userName, char[] userPassPhrase) throws Exception
	{
		attemptReSignInInternal(getCurrentKeyPairFile(), userName, userPassPhrase);
	}
	
	private String getCurrentLanguage()
	{
		return localization.getCurrentLanguageCode();
	}

	public String getAccountId()
	{
		return getSecurity().getPublicKeyString();
	}
	
	public void writeKeyPairFileWithBackup(File keyPairFile, String userName, char[] userPassPhrase) throws
		CannotCreateAccountFileException
	{
		writeKeyPairFileInternal(keyPairFile, userName, userPassPhrase);
		try
		{
			writeKeyPairFileInternal(getBackupFile(keyPairFile), userName, userPassPhrase);
		}
		catch (Exception e)
		{
			System.out.println("MartusApp.writeKeyPairFileWithBackup: " + e);
		}
	}

	protected void writeKeyPairFileInternal(File keyPairFile, String userName, char[] userPassPhrase) throws
		CannotCreateAccountFileException
	{
		try
		{
			FileOutputStream outputStream = new FileOutputStream(keyPairFile);
			try
			{
				getSecurity().writeKeyPair(outputStream, PasswordHelper.getCombinedPassPhrase(userName, userPassPhrase));
			}
			finally
			{
				outputStream.close();
			}
		}
		catch(Exception e)
		{
			throw(new CannotCreateAccountFileException());
		}

	}

	public void attemptSignInInternal(File keyPairFile, String userName, char[] userPassPhrase) throws Exception
	{
		try
		{
			getSecurity().readKeyPair(keyPairFile, PasswordHelper.getCombinedPassPhrase(userName, userPassPhrase));
			setCurrentAccount(userName, keyPairFile.getParentFile());
		}
		catch(Exception e)
		{
			getSecurity().clearKeyPair();
			currentUserName = "";
			throw e;
		}
	}
	
	public void attemptReSignInInternal(File keyPairFile, String userName, char[] userPassPhrase) throws Exception
	{
		if(!userName.equals(currentUserName))
			throw new MartusCrypto.AuthorizationFailedException();
		MartusCrypto securityOfReSignin = new MartusSecurity();
		FileInputStream inputStream = new FileInputStream(keyPairFile);
		try
		{
			securityOfReSignin.readKeyPair(inputStream, PasswordHelper.getCombinedPassPhrase(userName, userPassPhrase));
		}
		finally
		{
			inputStream.close();
		}
	}

	public void setCurrentAccount(String userName, File accountDirectory) throws IOException
	{
		currentUserName = userName;
		currentAccountDirectory = accountDirectory;
		updateUserNameHashFile();
	}

	private void updateUserNameHashFile() throws IOException
	{
		File hashUserName = getUserNameHashFile(currentAccountDirectory);
		hashUserName.delete();
		String hashOfUserName = MartusCrypto.getHexDigest(currentUserName);
		UnicodeWriter writer = new UnicodeWriter(hashUserName);
		try
		{
			writer.writeln(hashOfUserName);
		}
		finally
		{
			writer.close();
		}
	}
	
	public boolean isUserOwnerOfThisAccountDirectory(MartusCrypto tempSecurity, String userName, char[] userPassPhrase, File accountDirectory) throws IOException
	{
		File thisAccountsHashOfUserNameFile = getUserNameHashFile(accountDirectory);
		if(thisAccountsHashOfUserNameFile.exists())
		{
			UnicodeReader reader = new UnicodeReader(thisAccountsHashOfUserNameFile);
			try
			{
				String hashOfUserName = reader.readLine();
				String hexDigest = MartusCrypto.getHexDigest(userName);
				if(hashOfUserName.equals(hexDigest))
					return true;
			}
			finally
			{
				reader.close();
			}
			return false;
		}

		File thisAccountsKeyPair = getKeyPairFile(accountDirectory);
		try
		{
			tempSecurity.readKeyPair(thisAccountsKeyPair, PasswordHelper.getCombinedPassPhrase(userName, userPassPhrase));
			return true;
		}
		catch (Exception cantBeOurAccount)
		{
			return false;
		}
	}

	public File getUserNameHashFile(File accountDirectory)
	{
		return new File(accountDirectory, "AccountToken.txt");
	}

	public MartusCrypto getSecurity()
	{
		return store.getSignatureGenerator();
	}

	public void setSSLNetworkInterfaceHandlerForTesting(ClientSideNetworkInterface server)
	{
		currentNetworkInterfaceHandler = server;
	}

	public void setSSLNetworkInterfaceHandlerForTesting(ServerSideNetworkInterface server)
	{
		setSSLNetworkInterfaceHandlerForTesting(new MockClientSideNetworkHandler(server));
	}

	public boolean isSSLServerAvailable(ClientSideNetworkGateway server)
	{
		try
		{
			NetworkResponse response = server.getServerInfo();
			if(!response.getResultCode().equals(NetworkInterfaceConstants.OK))
				return false;

			String version = (String)response.getResultVector().get(0);
			if(version.indexOf("MartusServer") == 0)
				return true;
		}
		catch(Exception notInterestingBecauseTheServerMightJustBeDown)
		{
			//System.out.println("MartusApp.isSSLServerAvailable: " + e);
			MartusLogger.logException(notInterestingBecauseTheServerMightJustBeDown);
		}

		return false;
	}

	public ClientSideNetworkGateway getCurrentNetworkInterfaceGateway()
	{
		if(currentNetworkInterfaceGateway == null)
		{
			currentNetworkInterfaceGateway = new ClientSideNetworkGateway(getCurrentNetworkInterfaceHandler());
		}

		return currentNetworkInterfaceGateway;
	}

	private ClientSideNetworkInterface getCurrentNetworkInterfaceHandler()
	{
		if(currentNetworkInterfaceHandler == null)
		{
			currentNetworkInterfaceHandler = createXmlRpcNetworkInterfaceHandler();
		}

		return currentNetworkInterfaceHandler;
	}

	private ClientSideNetworkInterface createXmlRpcNetworkInterfaceHandler()
	{
		String ourServer = getServerName();
		String ourServerPublicKey = getConfigInfo().getServerPublicKey();
		return ClientSideNetworkGateway.buildNetworkInterface(ourServer,ourServerPublicKey, transport);
	}

	private void invalidateCurrentHandlerAndGateway()
	{
		currentNetworkInterfaceHandler = null;
		currentNetworkInterfaceGateway = null;
	}

	private String getServerName()
	{
		return configInfo.getServerName();
	}

	private static File determineMartusDataRootDirectory()
	{
		String dir;
		if(Version.isRunningUnderWindows())
		{
			dir = "C:/Martus/";
		}
		else
		{
			String userHomeDir = System.getProperty("user.home");
			dir = userHomeDir + "/.Martus/";
		}
		File file = new File(dir);
		if(!file.exists())
		{
			file.mkdirs();
		}

		return file;
	}

	public void saveBulletin(Bulletin bulletinToSave, BulletinFolder outboxToUse) throws Exception
	{
		store.saveBulletin(bulletinToSave);
		store.ensureBulletinIsInFolder(store.getFolderSaved(), bulletinToSave.getUniversalId());
		store.ensureBulletinIsInFolder(outboxToUse, bulletinToSave.getUniversalId());
		store.removeBulletinFromFolder(store.getFolderDiscarded(), bulletinToSave);
		store.setIsNotOnServer(bulletinToSave);
		store.saveFolders();
	}

	public class SaveConfigInfoException extends Exception 
	{
	}

	public class LoadConfigInfoException extends Exception 
	{
		public LoadConfigInfoException() 
		{
		}
		
		public LoadConfigInfoException(Exception e) 
		{
			super(e);
		}
	}

	public static class MartusAppInitializationException extends Exception
	{
		MartusAppInitializationException(String message)
		{
			super(message);
		}
	}

	public File martusDataRootDirectory;
	protected File currentAccountDirectory;
	protected MtfAwareLocalization localization;
	public ClientBulletinStore store;
	private HashMap fieldExpansionStates;
	private HashMap gridExpansionStates;
	private ConfigInfo configInfo;
	public ClientSideNetworkInterface currentNetworkInterfaceHandler;
	public ClientSideNetworkGateway currentNetworkInterfaceGateway;
	public String currentUserName;
	private int maxNewFolders;
	public RetrieveCommand currentRetrieveCommand;
	private TorTransportWrapper transport;

	public static final String PUBLIC_INFO_EXTENSION = ".mpi";
	public static final String MARTUS_IMPORT_EXPORT_EXTENSION = ".xml";
	public static final String CUSTOMIZATION_TEMPLATE_EXTENSION = ".mct";
	public static final String DEFAULT_DETAILS_EXTENSION = ".txt";
	public static final String AUTHENTICATE_SERVER_FAILED = "Failed to Authenticate Server";
	public static final String SHARE_KEYPAIR_FILENAME_EXTENSION = ".dat";
	public static final String KEYPAIR_FILENAME = "MartusKeyPair.dat";
	public static final String ACCOUNTS_DIRECTORY_NAME = "accounts";
	public static final String PACKETS_DIRECTORY_NAME = "packets";
	public static final String DOCUMENTS_DIRECTORY_NAME = "Docs";
	public static final String USE_UNOFFICIAL_TRANSLATIONS_NAME = "use_unofficial_translations.txt";
	private final int MAXFOLDERS = 50;
	public int serverChunkSize = NetworkInterfaceConstants.CLIENT_MAX_CHUNK_SIZE;
}

