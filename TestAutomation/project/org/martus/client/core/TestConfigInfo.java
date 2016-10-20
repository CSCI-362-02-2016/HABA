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
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.martus.common.ContactInfo;
import org.martus.common.LegacyCustomFields;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.MockMartusSecurity;
import org.martus.common.fieldspec.StandardFieldSpecs;
import org.martus.common.network.NetworkInterfaceConstants;
import org.martus.util.StreamableBase64;
import org.martus.util.TestCaseEnhanced;

public class TestConfigInfo extends TestCaseEnhanced
{
    public TestConfigInfo(String name) throws IOException
	{
        super(name);
    }

	public void testBasics()
	{
		ConfigInfo info = new ConfigInfo();
		verifyEmptyInfo(info, "constructor");
		
		assertEquals(18, ConfigInfo.VERSION);

		info.setAuthor("fred");
		assertEquals("fred", info.getAuthor());
	
		info.setTemplateDetails(sampleTemplateDetails);
		assertEquals("Details not set?", sampleTemplateDetails, info.getTemplateDetails());

		info.clear();
		verifyEmptyInfo(info, "clear");
		assertFalse("A blank config Info can't be new", info.isNewVersion());
	}

	public void testHasEnoughContactInfo() throws Exception
	{
		ConfigInfo info = new ConfigInfo();
		info.setAuthor("fred");
		assertEquals("author isn't enough contact info?", true, info.hasEnoughContactInfo());
		info.setAuthor("");
		info.setOrganization("whatever");
		assertEquals("organization isn't enough contact info?", true, info.hasEnoughContactInfo());
	}

	public void testLoadVersions() throws Exception
	{
		for(short version = 1; version <= ConfigInfo.VERSION; ++version)
		{
			byte[] data = createFileWithSampleData(version);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
			verifyLoadSpecificVersion(inputStream, version);
		}
	}

	public void testSaveFull() throws Exception
	{
		for(short version = 1; version <= ConfigInfo.VERSION; ++version)
		{
			ConfigInfo info = new ConfigInfo();
	
			setConfigToSampleData(info, ConfigInfo.VERSION);
			verifySampleInfo(info, "testSaveFull", ConfigInfo.VERSION);
	
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			info.save(outputStream);
			outputStream.close();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

			verifyLoadSpecificVersion(inputStream, ConfigInfo.VERSION);
		}
	}

	public void testSaveEmpty() throws Exception
	{
		ConfigInfo emptyInfo = new ConfigInfo();
		ByteArrayOutputStream emptyOutputStream = new ByteArrayOutputStream();
		emptyInfo.save(emptyOutputStream);
		emptyOutputStream.close();

		emptyInfo.setAuthor("should go away");
		ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(emptyOutputStream.toByteArray());
		emptyInfo = ConfigInfo.load(emptyInputStream);
		assertEquals("should have cleared", "", emptyInfo.getAuthor());
	}

	public void testSaveNonEmpty() throws Exception
	{
		ConfigInfo info = new ConfigInfo();
		String server = "server";
		info.setServerName(server);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		info.save(outputStream);
		info.setServerName("should be reverted");

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		info = ConfigInfo.load(inputStream);
		assertEquals("should have reverted", server, info.getServerName());
	}

	public void testRemoveHQKey() throws Exception
	{
		ConfigInfo info = new ConfigInfo();
		String hqKey = "HQKey";
		info.setLegacyHQKey(hqKey);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		info.save(outputStream);
		info.clearHQKey();
		assertEquals("HQ Key Should be cleared", "", info.getLegacyHQKey());

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		info = ConfigInfo.load(inputStream);
		assertEquals("HQ key should have reverted", hqKey, info.getLegacyHQKey());
	}


	public void testGetContactInfo() throws Exception
	{
		MartusCrypto signer = MockMartusSecurity.createClient();
		ContactInfo contactInfo = new ContactInfo(sampleAuthor, "org", "email", "web", samplePhone, sampleAddress);
		Vector contactInfoVector = contactInfo.getSignedEncodedVector(signer);
		assertEquals("Not encoded?",NetworkInterfaceConstants.BASE_64_ENCODED,contactInfoVector.get(0));
		assertEquals("Wrong contactinfo size", 10, contactInfoVector.size());
		String publicKey = (String)contactInfoVector.get(1);

		assertEquals("Not the publicKey?", signer.getPublicKeyString(), publicKey);
		int contentSize = ((Integer)(contactInfoVector.get(2))).intValue();
		assertEquals("Not the encoded correct size?", contentSize + 4, contactInfoVector.size());
		assertEquals("encoded Author not correct?", sampleAuthor, new String(StreamableBase64.decode((String)contactInfoVector.get(3))));
		assertEquals("encoded Address not correct?", sampleAddress,  new String(StreamableBase64.decode((String)contactInfoVector.get(8))));
		assertEquals("encoded phone not correct?", samplePhone,  new String(StreamableBase64.decode((String)contactInfoVector.get(7))));
		
		Vector decodedContactInfo = ContactInfo.decodeContactInfoVectorIfNecessary(contactInfoVector);
		Vector alreadyDecodedContactInfo = ContactInfo.decodeContactInfoVectorIfNecessary(decodedContactInfo);
		assertEquals("Backward compatibility test, a decoded vector should be equal", decodedContactInfo, alreadyDecodedContactInfo);
		
		assertNotEquals("Still encoded?", NetworkInterfaceConstants.BASE_64_ENCODED, decodedContactInfo.get(0));
		assertEquals("contentSize not the same?", contentSize, ((Integer)decodedContactInfo.get(1)).intValue());
		assertEquals("decoded Author not correct?", sampleAuthor, decodedContactInfo.get(2));
		assertEquals("decoded Address not correct?", sampleAddress,  decodedContactInfo.get(7));
		assertEquals("decoded phone not correct?", samplePhone,  decodedContactInfo.get(6));
		
		assertTrue("Signature failed with signature in vector?", signer.verifySignatureOfVectorOfStrings(decodedContactInfo, publicKey));

		String signature = (String)decodedContactInfo.remove(decodedContactInfo.size()-1);
		assertTrue("Signature failed with signature removed from vector?", signer.verifySignatureOfVectorOfStrings(decodedContactInfo, publicKey, signature));

		
	}
	
	public void testIsServerConfigured()
	{
		ConfigInfo newInfo = new ConfigInfo();
		assertFalse("Didn't set up a server should not exist", newInfo.isServerConfigured());
		newInfo.setServerName("tmp Server");
		assertFalse("server publick key not set yet", newInfo.isServerConfigured());
		newInfo.setServerPublicKey("some key");
		assertTrue("Server should be setup now", newInfo.isServerConfigured());
	}
	
	public void testLongStrings() throws Exception
	{
		ConfigInfo info = new ConfigInfo();
		info.setCustomFieldTopSectionXml(longString);
		info.setCustomFieldBottomSectionXml(longString);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		info.save(out);
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		ConfigInfo loaded = ConfigInfo.load(in);
		assertEquals("Didn't restore long string for top?", info.getCustomFieldTopSectionXml(), loaded.getCustomFieldTopSectionXml());
		assertEquals("Didn't restore long string for bottom?", info.getCustomFieldBottomSectionXml(), loaded.getCustomFieldBottomSectionXml());
	}

	private static String createLongSampleString() 
	{
		StringBuffer big = new StringBuffer(100 * 100 * 10);
		for(int i = 0; i < 100; ++i)
			for(int j = 0; j < 100; ++j)
				big.append("abcdefghij");
		return big.toString();
	}
	
	void setConfigToSampleData(ConfigInfo info, int VERSION)
	{
		info.setAuthor(sampleAuthor);
		info.setOrganization(sampleOrg);
		info.setEmail(sampleEmail);
		info.setWebPage(sampleWebPage);
		info.setPhone(samplePhone);
		info.setAddress(sampleAddress);
		info.setServerName(sampleServerName);
		info.setServerPublicKey(sampleServerKey);
		info.setTemplateDetails(sampleTemplateDetails);
		info.setLegacyHQKey(sampleHQKey);
		info.setSendContactInfoToServer(sampleSendContactInfoToServer);
		info.setServerCompliance(sampleServerCompliance);
		info.setCustomFieldLegacySpecs(sampleCustomFieldSpecs);
		info.setForceBulletinsAllPrivate(sampleForceAllPrivate);
		info.setBackedUpKeypairEncrypted(sampleBackedUpKeypairEncrypted);
		info.setBackedUpKeypairShare(sampleBackedUpKeypairShare);
		info.setAllHQKeysXml(sampleAllHQKeysXml);
		info.setBulletinVersioningAware(sampleBulletinVersioningAware);
		info.setDefaultHQKeysXml(sampleDefaultHQKeysXml);
		info.setCheckForFieldOfficeBulletins(sampleCheckForFieldOfficeBulletins);
		info.setCustomFieldTopSectionXml(sampleCustomFieldTopSectionXml);
		info.setCustomFieldBottomSectionXml(sampleCustomFieldBottomSectionXml);
		info.setUseZawgyiFont(sampleUseZawgyi);
		info.setFieldDeskKeysXml(sampleFieldDeskKeysXml);
		info.setBackedUpImprovedKeypairShare(sampleBackedUpImprovedKeypairShare);
		info.setUseInternalTor(sampleUseInternalTor);
	}

	void verifyEmptyInfo(ConfigInfo info, String label)
	{
		assertEquals(label + ": Full has contact info", false, info.hasEnoughContactInfo());
		assertEquals(label + ": sampleSource", "", info.getAuthor());
		assertEquals(label + ": sampleOrg", "", info.getOrganization());
		assertEquals(label + ": sampleEmail", "", info.getEmail());
		assertEquals(label + ": sampleWebPage", "", info.getWebPage());
		assertEquals(label + ": samplePhone", "", info.getPhone());
		assertEquals(label + ": sampleAddress", "", info.getAddress());
		assertEquals(label + ": sampleServerName", "", info.getServerName());
		assertEquals(label + ": sampleServerKey", "", info.getServerPublicKey());
		assertEquals(label + ": sampleTemplateDetails", "", info.getTemplateDetails());
		assertEquals(label + ": sampleHQKey", "", info.getLegacyHQKey());
		assertEquals(label + ": sampleSendContactInfoToServer", false, info.shouldContactInfoBeSentToServer());
		assertEquals(label + ": sampleServerComplicance", "", info.getServerCompliance());
		assertEquals(label + ": sampleCustomFieldSpecs", defaultCustomFieldSpecs, info.getCustomFieldLegacySpecs());
		assertEquals(label + ": sampleForceAllPrivate", false, info.shouldForceBulletinsAllPrivate());
		assertEquals(label + ": sampleBackedUpKeypairEncrypted", false, info.hasUserBackedUpKeypairEncrypted());
		assertEquals(label + ": sampleBackedUpKeypairShare", false, info.hasUserBackedUpKeypairShare());
		assertEquals(label + ": sampleAllHQKeysXml", "", info.getAllHQKeysXml());
		assertEquals(label + ": sampleBulletinVersioningAware", true, info.isBulletinVersioningAware());
		assertEquals(label + ": sampleDefaultHQKeysXml", "", info.getDefaultHQKeysXml());
		assertEquals(label + ": sampleCheckForFieldOfficeBulletins", false, info.getCheckForFieldOfficeBulletins());
		assertEquals(label + ": sampleCustomFieldTopSectionXml", "", info.getCustomFieldTopSectionXml());
		assertEquals(label + ": sampleCustomFieldBottomSectionXml", "", info.getCustomFieldBottomSectionXml());
		assertEquals(label + ": sampleFieldDeskKeysXml", "", info.getFieldDeskKeysXml());
		assertEquals(label + ": sampleBackedUpImprovedKeypairShare", false, info.hasBackedUpImprovedKeypairShare());
		assertEquals(label + ": sampleUseInternalTor", false, info.useInternalTor());
	}

	void verifySampleInfo(ConfigInfo info, String label, int VERSION)
	{
		label = label + " (" + VERSION + ")";
		assertEquals(label + ": Full has contact info", true, info.hasEnoughContactInfo());
		assertEquals(label + ": sampleSource", sampleAuthor, info.getAuthor());
		assertEquals(label + ": sampleOrg", sampleOrg, info.getOrganization());
		assertEquals(label + ": sampleEmail", sampleEmail, info.getEmail());
		assertEquals(label + ": sampleWebPage", sampleWebPage, info.getWebPage());
		assertEquals(label + ": samplePhone", samplePhone, info.getPhone());
		assertEquals(label + ": sampleAddress", sampleAddress, info.getAddress());
		assertEquals(label + ": sampleServerName", sampleServerName, info.getServerName());
		assertEquals(label + ": sampleServerKey", sampleServerKey, info.getServerPublicKey());
		assertEquals(label + ": sampleTemplateDetails", sampleTemplateDetails, info.getTemplateDetails());
		assertEquals(label + ": sampleHQKey", sampleHQKey, info.getLegacyHQKey());

		if(VERSION <= ConfigInfo.VERSION)
			assertFalse(label+":Should not be a new Config file", info.isNewVersion());
			
		if(VERSION > ConfigInfo.VERSION)
			assertTrue(label+":Should be a new config file", info.isNewVersion());
		
		if(VERSION >= 2)
			assertEquals(label + ": sampleSendContactInfoToServer", sampleSendContactInfoToServer, info.shouldContactInfoBeSentToServer());
		else
			assertEquals(label + ": sampleSendContactInfoToServer", false, info.shouldContactInfoBeSentToServer());

		if(VERSION >= 3)
			; // Version 3 added no data fields

		if(VERSION >= 4)
			assertEquals(label + ": sampleServerComplicance", sampleServerCompliance, info.getServerCompliance());
		else
			assertEquals(label + ": sampleServerComplicance", "", info.getServerCompliance());

		if(VERSION >= 5)
			assertEquals(label + ": sampleCustomFieldSpecs", sampleCustomFieldSpecs, info.getCustomFieldLegacySpecs());
		else
			assertEquals(label + ": sampleCustomFieldSpecs", defaultCustomFieldSpecs, info.getCustomFieldLegacySpecs());

		if(VERSION >= 6 && VERSION < 14)
			assertEquals(label + ": sampleCustomFieldTopSectionXml", sampleLegacyCustomFieldTopSectionXml, info.getCustomFieldTopSectionXml());	
		else if(VERSION < 6)
			assertEquals(label + ": sampleCustomFieldTopSectionXml", "", info.getCustomFieldTopSectionXml());
		
		if(VERSION >= 7)
			assertEquals(label + ": sampleForceAllPrivate", sampleForceAllPrivate, info.shouldForceBulletinsAllPrivate());
		else
			assertEquals(label + ": sampleForceAllPrivate", false, info.shouldForceBulletinsAllPrivate());

		if(VERSION >= 8)
		{
			assertEquals(label + ": sampleBackedUpKeypairEncrypted", sampleBackedUpKeypairEncrypted, info.hasUserBackedUpKeypairEncrypted());
			assertEquals(label + ": sampleBackedUpKeypairShare", sampleBackedUpKeypairShare, info.hasUserBackedUpKeypairShare());
		}
		else
		{
			assertEquals(label + ": sampleBackedUpKeypairEncrypted", false, info.hasUserBackedUpKeypairEncrypted());
			assertEquals(label + ": sampleBackedUpKeypairShared", false, info.hasUserBackedUpKeypairShare());
		}
		
		if(VERSION >= 9)
			assertEquals(label + ": sampleAllHQKeysXml", sampleAllHQKeysXml, info.getAllHQKeysXml());	
		else
			assertEquals(label + ": sampleAllHQKeysXml", "", info.getAllHQKeysXml());
		
		if(VERSION >= 10)
			assertEquals(label + ": sampleBulletinVersioningAware", sampleBulletinVersioningAware, info.isBulletinVersioningAware());	
		else
			assertEquals(label + ": sampleBulletinVersioningAware", false, info.isBulletinVersioningAware());
		
		if(VERSION >= 11)
			assertEquals(label + ": sampleDefaultHQKeysXml", sampleDefaultHQKeysXml, info.getDefaultHQKeysXml());	
		else
			assertEquals(label + ": sampleDefaultHQKeysXml", "", info.getDefaultHQKeysXml());
			
		if(VERSION >= 12 && VERSION < 14)
			assertEquals(label + ": sampleCustomFieldBottomSectionXml", sampleLegacyCustomFieldBottomSectionXml, info.getCustomFieldBottomSectionXml());	
		else if(VERSION < 12)
			assertEquals(label + ": sampleCustomFieldBottomSectionXml", "", info.getCustomFieldBottomSectionXml());

		if(VERSION >= 13)
			assertEquals(label + ": sampleCheckForFieldOfficeBulletins", sampleCheckForFieldOfficeBulletins, info.getCheckForFieldOfficeBulletins());	
		else
			assertEquals(label + ": sampleCheckForFieldOfficeBulletins", false, info.getCheckForFieldOfficeBulletins());
		
		if(VERSION >= 14)
		{
			assertEquals(label + ": sampleCustomFieldTopSectionXml", sampleCustomFieldTopSectionXml, info.getCustomFieldTopSectionXml());
			assertEquals(label + ": sampleCustomFieldBottomSectionXml", sampleCustomFieldBottomSectionXml, info.getCustomFieldBottomSectionXml());
		}
		else
		{
			if(VERSION < 6)
				assertEquals(label + ": sampleCustomFieldTopSectionXml", "", info.getCustomFieldTopSectionXml());
			if(VERSION < 12)
				assertEquals(label + ": sampleCustomFieldBottomSectionXml", "", info.getCustomFieldBottomSectionXml());
		}

		if(VERSION >= 15)
			assertEquals(label + ": sampleUseZawgyi", sampleUseZawgyi, info.getUseZawgyiFont());
		else
			assertEquals(label + ": sampleUseZawgyi", false, info.getUseZawgyiFont());
		
		if(VERSION >= 16)
			assertEquals(label + ": sampleFieldDeskKeys", sampleFieldDeskKeysXml, info.getFieldDeskKeysXml());
		else
			assertEquals(label + ": sampleFieldDeskKeys", "", info.getFieldDeskKeysXml());

		if(VERSION >= 17)
			assertEquals(label + ": sampleBackedUpImprovedKeypairShare", sampleBackedUpImprovedKeypairShare, info.hasBackedUpImprovedKeypairShare());
		else
			assertEquals(label + ": sampleBackedUpImprovedKeypairShare", false, info.hasBackedUpImprovedKeypairShare());

		if(VERSION >= 18)
			assertEquals(label + ": sampleUseInternalTor", sampleUseInternalTor, info.useInternalTor());
		else
			assertEquals(label + ": sampleUseInternalTor", false, info.useInternalTor());

	}

	void verifyLoadSpecificVersion(ByteArrayInputStream inputStream, short VERSION) throws Exception
	{
		ConfigInfo info = new ConfigInfo();
		info = ConfigInfo.load(inputStream);
		verifySampleInfo(info, "testLoadVersion" + VERSION, VERSION);
	}

	public byte[] createFileWithSampleData(short VERSION)
		throws IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(outputStream);
		out.writeShort(VERSION);
		out.writeUTF(sampleAuthor);
		out.writeUTF(sampleOrg);
		out.writeUTF(sampleEmail);
		out.writeUTF(sampleWebPage);
		out.writeUTF(samplePhone);
		out.writeUTF(sampleAddress);
		out.writeUTF(sampleServerName);
		out.writeUTF(sampleTemplateDetails);
		out.writeUTF(sampleHQKey);
		out.writeUTF(sampleServerKey);
		if(VERSION >= 2)
		{
			out.writeBoolean(sampleSendContactInfoToServer);
		}
		if(VERSION >= 3)
			; // Version 3 added no data fields
		if(VERSION >= 4)
		{
			out.writeUTF(sampleServerCompliance);
		}
		if(VERSION >= 5)
		{
			out.writeUTF(sampleCustomFieldSpecs);
		}
		if(VERSION >= 6)
		{
			if(VERSION < 14)
				out.writeUTF(sampleLegacyCustomFieldTopSectionXml);
			else
				out.writeUTF("");
		}
		if(VERSION >= 7)
		{
			out.writeBoolean(sampleForceAllPrivate);
		}
		if(VERSION >= 8)
		{
			out.writeBoolean(sampleBackedUpKeypairEncrypted);
			out.writeBoolean(sampleBackedUpKeypairShare);
		}
		if(VERSION >= 9)
		{
			out.writeUTF(sampleAllHQKeysXml);
		}
		if(VERSION >= 10)
		{
			out.writeBoolean(sampleBulletinVersioningAware);
		}
		if(VERSION >= 11)
		{
			out.writeUTF(sampleDefaultHQKeysXml);
		}
		if(VERSION >= 12)
		{
			if(VERSION < 14)
				out.writeUTF(sampleLegacyCustomFieldBottomSectionXml);
			else
				out.writeUTF("");
		}
		if(VERSION >= 13)
		{
			out.writeBoolean(sampleCheckForFieldOfficeBulletins);
		}
		if(VERSION >= 14)
		{
			ConfigInfo.writeLongString(out, sampleCustomFieldTopSectionXml);
			ConfigInfo.writeLongString(out, sampleCustomFieldBottomSectionXml);
		}
		if(VERSION >= 15)
		{
			out.writeBoolean(sampleUseZawgyi);
		}
		if(VERSION >= 16)
		{
			ConfigInfo.writeLongString(out, sampleFieldDeskKeysXml);
		}
		if(VERSION >= 17)
		{
			out.writeBoolean(sampleBackedUpImprovedKeypairShare);
		}
		if(VERSION >= 18)
		{
			out.writeBoolean(sampleUseInternalTor);
		}
		out.close();
		return outputStream.toByteArray();
	}
	
	private final String defaultCustomFieldSpecs = LegacyCustomFields.buildFieldListString(StandardFieldSpecs.getDefaultTopSetionFieldSpecs());

	private final static String longString = createLongSampleString();
	
//Version 1
	// sampleAuthor set to Zawgyi font
	final String sampleAuthor = "author";
	final String sampleOrg = "org";
	final String sampleEmail = "email";
	final String sampleWebPage = "web";
	final String samplePhone = "phone";
	final String sampleAddress = "address\nline2";
	final String sampleServerName = "server name";
	final String sampleServerKey = "server pub key";
	final String sampleTemplateDetails = "details\ndetail2";
	final String sampleHQKey = "1234324234";
//Version 2
	final boolean sampleSendContactInfoToServer = true;
//Version 3
	//nothing added just signed.
//Version 4
	final String sampleServerCompliance = "I am compliant";
//Version 5
	final String sampleCustomFieldSpecs = "language;author;custom,Custom Field;title;entrydate";
//Version 6
	final String sampleLegacyCustomFieldTopSectionXml = "<CustomFields></CustomFields>";
//Version 7
	final boolean sampleForceAllPrivate = true;
//Version 8
	final boolean sampleBackedUpKeypairEncrypted = true;
	final boolean sampleBackedUpKeypairShare = true;
//Version 9
	final String sampleAllHQKeysXml = "<HQs>ALL</HQs>";
//Version 10
	final boolean sampleBulletinVersioningAware = true;
//Version 11
	final String sampleDefaultHQKeysXml = "<HQs>defaultHQ</HQs>";
//Version 12
	final String sampleLegacyCustomFieldBottomSectionXml = "<CustomFields></CustomFields>";
//Version 13
	final boolean sampleCheckForFieldOfficeBulletins = true;
//Version 14
	final String sampleCustomFieldTopSectionXml = longString;
	final String sampleCustomFieldBottomSectionXml = longString;
//Version 15
	final boolean sampleUseZawgyi = true;
//Version 16
	final String sampleFieldDeskKeysXml = "<FieldDesks><FieldDesk><PublicKey>1234</PublicKey><Label>Test Label</Label></FieldDesk></FieldDesks>";
//Version 17
	final boolean sampleBackedUpImprovedKeypairShare = true;
//Version 18
	final boolean sampleUseInternalTor = true;
}
