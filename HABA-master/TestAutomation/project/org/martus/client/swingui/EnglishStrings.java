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


import org.martus.common.bulletin.Bulletin;

public class EnglishStrings
{
	public static String strings[] = {
"wintitle:main=Martus Human Rights Bulletin System",
"wintitle:create=Create Bulletin",
"wintitle:options=Options",
"wintitle:HelpDefaultDetails=Help on Bulletin Details Field Default Content",
"wintitle:MartusSignIn=Martus SignIn",
"wintitle:MartusSignInValidate=Validate User",
"wintitle:MartusSignInRetypePassword=Confirm Password",
"wintitle:ServerCompliance=Server Compliance Statement",
"wintitle:ErrorBackingupKeyPair=Error",
"wintitle:askToBackupKeyPair=Key Backup Needed",
"wintitle:BulletinDetailsDialog=Bulletin Details",
"wintitle:IncompatibleMtfVersion=Incompatible Translation Version",
"wintitle:DuplicateLabelsInCustomTemplate=Warning Duplicate Labels Found",
"wintitle:ImportProgress=Importing Bulletins",
"wintitle:ExportProgress=Exporting Bulletins",
"wintitle:AddPermissions=Update headquarters access to Bulletins",

"wintitle:confirmsend=Confirm Save Bulletin",
"wintitle:confirmretrieve=Confirm Retrieve Bulletins",
"wintitle:confirmRemoveAttachment=Confirm Remove Attachments",
"wintitle:confirmOverWriteExistingFile=Confirm Replace Existing File",
"wintitle:confirmCancelModifyBulletin=Cancel Modify Bulletin",
"wintitle:confirmSetImportPublicKey=Confirm Import of Public Account Id",
"wintitle:confirmImportFieldDeskPublicKey=Import Account Public Information",
"wintitle:confirmWarningSwitchToNormalKeyboard=Security Warning",
"wintitle:confirmClearHQInformation=Confirm Removal of Headquarters",
"wintitle:confirmRemoveFieldDeskKeys=Confirm Removal of Field Desk(s)",
"wintitle:confirmCloneMySealedAsDraft=Confirm Create New Version of Sealed Bulletin",
"wintitle:confirmCloneBulletinAsMine=Confirm Create Copy of Someone Else's Bulletin",
"wintitle:confirmCloneUnverifiedFDBulletinAsMine=Confirm Create Copy of Bulletin",
"wintitle:confirmPrinterWarning=Print Configuration Warning",
"wintitle:confirmRequestToSendContactInfoToServer=Request to Send Contact Info to Server",
"wintitle:confirmUploadReminder=Upload Reminder",
"wintitle:confirmDraftUploadReminder=Unsent Draft Reminder",
"wintitle:confirmRedoWeakPassword=Weak Password Warning",
"wintitle:confirmExportPrivateData=Confirm Export Private Data",
"wintitle:confirmResetDefaultDetails=Reset Contents",
"wintitle:confirmNotYourBulletinViewAttachmentAnyways=Attachment Warning",
"wintitle:confirmServerComplianceFailed=Server Compliance Statement",
"wintitle:confirmWarnMissingOrCorruptAccountMapSignatureFile=Accountmap Signature File Missing Or Corrupt",
"wintitle:confirmWarnMissingAccountMapFile=Accountmap File Missing Or Corrupt",
"wintitle:confirmBackupKeyPairSecretShare=Backing Up Your Key Files",
"wintitle:confirmCancelShareBackup=Cancel Backup",
"wintitle:confirmEnterCustomFields=Customize Fields",
"wintitle:confirmUndoCustomFields=Customize Fields",
"wintitle:confirmRecoverUsingKeyShare=Create New Account / Restore Old Account",
"wintitle:confirmCancelShareRecover=Cancel Restore",
"wintitle:confirmRecoveredKeyShareFailedTryAgain=Restore Failed",
"wintitle:confirmErrorRecoverIvalidFileName=Invalid File",
"wintitle:confirmBackupKeyShareVerifyDisks=Verify Disks",
"wintitle:confirmCancelShareVerify=Cancel Verification",
"wintitle:confirmEditBulletinWithUnknownTags=Modify Bulletin",
"wintitle:confirmExportUnknownTags=Export Bulletins",
"wintitle:confirmKeyPairFileExistsOverWrite=Key file Exists",
"wintitle:confirmQuickEraseOutboxNotEmpty=Unsent Bulletins Reminder",
"wintitle:confirmCancelBackupRecovery=Cancel Backup Restore",
"wintitle:confirmUnableToRecoverFromBackupFile=Unable to Restore from Backup File",
"wintitle:confirmWarningPathChosenMayNotBeRemoveable=Questionable Media Chosen",
"wintitle:confirmBackupKeyPairInformation=Backing Up Your Key",
"wintitle:confirmhelpStringNotFound=Text Not Found",
"wintitle:confirmNeedsFolderMigration=Folder Migration Required",
"wintitle:confirmNeedsBulletinVersioningMigration=Bulletin Versioning Migration Required",
"wintitle:confirmUseBulletinsCustomFields=Custom Fields Selection",
"wintitle:confirmUseBulletinsDraftCustomFields=Custom Fields Selection",
"wintitle:confirmUnAuthoredBulletinDeleteBeforePaste=Bulletin Already Exists",
"wintitle:confirmRemoveMartus=Delete All Data and Remove Martus",
"wintitle:confirmDeleteMyData=Delete My Data",
"wintitle:confirmNewerConfigInfoFileFound=Release Warning",
"wintitle:confirmdeletefolder=Confirm Delete Folder",
"wintitle:confirmRetrieveNewerVersions=Confirm Retrieving Newer Versions",
"wintitle:confirmDateRageInvalid=Date Range Invalid",
"wintitle:confirmPrintAllPrivateData=Print All Private Data",
"wintitle:confirmCancelRetrieve=Cancel Retrieve",
"wintitle:confirmReportIncludePrivate=All Data Private",
"wintitle:confirmSearchProgressCancel=Cancel Search",
"wintitle:confirmReportSearchProgressCancel=Cancel Search",
"wintitle:confirmSealSelectedBulletins=Seal Bulletins",
"wintitle:confirmSealingSelectedBulletinsCancel=Sealing Bulletins",
"wintitle:confirmXmlSchemaNewerImportAnyway=Import Bulletins",
"wintitle:confirmLoadingFieldValuesFromAllBulletinsCancel=Loading Values",

"wintitle:notifyDropErrorBulletinExists=Cannot Move Bulletin(s)",
"wintitle:notifyDropErrorBulletinOlder=Cannot Move Bulletin(s)",
"wintitle:notifyDropErrors=Error Moving Bulletin(s)",
"wintitle:notifyPasteErrorBulletinAlreadyExists=Cannot Paste Bulletin(s)",
"wintitle:PasteErrorBulletinOlder=Cannot Paste Bulletin(s)",
"wintitle:notifyPasteError=Error Pasting Bulletin(s)",
"wintitle:notifyretrieveworked=Retrieve Bulletins",
"wintitle:notifyretrievefailed=Retrieve Bulletins",
"wintitle:notifyretrievenothing=Retrieve Bulletins",
"wintitle:notifyretrievenoserver=Retrieve Bulletins",
"wintitle:notifyDeleteServerDraftsWorked=Delete Drafts From Server",
"wintitle:notifyDeleteServerDraftsNone=Delete Drafts From Server",
"wintitle:notifyDeleteServerDraftsFailed=Delete Drafts From Server",
"wintitle:notifypasswordsdontmatch=Invalid Setup Information",
"wintitle:notifyusernamessdontmatch=Invalid Setup Information",
"wintitle:notifyUserNameBlank=Invalid Setup Information",
"wintitle:notifyPasswordInvalid=Invalid Setup Information",
"wintitle:notifyPasswordMatchesUserName=Invalid Setup Information",
"wintitle:notifyincorrectsignin=Incorrect Signin",
"wintitle:notifyuploadrejected=Error Sending Bulletin",
"wintitle:notifycorruptconfiginfo=Error Loading Configuration File",
"wintitle:notifyserverok=Server Selection Complete",
"wintitle:notifymagicwordok=Upload Permission Granted",
"wintitle:notifymagicwordrejected=Upload Permission Rejected",
"wintitle:notifyRewriteKeyPairFailed=Error Changing User Name or Password",
"wintitle:notifyRewriteKeyPairSaved=Changed User Name or Password",
"wintitle:notifyUnableToViewAttachment=Viewing Attachment Failed",
"wintitle:notifyUnableToSaveAttachment=Saving Attachment Failed",
"wintitle:notifySearchFailed=Search Results",
"wintitle:notifySearchFound=Search Results",
"wintitle:notifyServerError=Server Error",
"wintitle:notifyFoundOrphans=Recovered Lost Bulletins",
"wintitle:notifyFoundDamagedBulletins=Detected Damaged Bulletins",
"wintitle:notifyErrorSavingState=Error Saving State",
"wintitle:notifyErrorSavingFile=Error Saving File",
"wintitle:notifyErrorBackingupKeyPair=Error Verifying Key Pair",
"wintitle:notifyExportMyPublicKey=Public Account Id Exported",
"wintitle:notifyPublicInfoFileError=Error Importing Public Information",
"wintitle:notifyAccountCodeWrong=Incorrect Public Code",
"wintitle:notifyErrorSavingConfig=Error Saving Configuration File",
"wintitle:notifyAuthenticateServerFailed=Security Alert!",
"wintitle:notifyWelcomeToMartus=Welcome To Martus",
"wintitle:notifyUnexpectedError=Unexpected Error",
"wintitle:notifyInvalidServerName=Invalid Server Name or IP Address",
"wintitle:notifyInvalidServerCode=Invalid Server Public Code",
"wintitle:notifyServerInfoInvalid=Server Response Invalid",
"wintitle:notifyConfigNoServer=Unable to Connect to Server",
"wintitle:notifyServerCodeWrong=Incorrect Server Public Code",
"wintitle:notifyRetrieveCanceled=Bulletin Download Canceled",
"wintitle:notifyRememberPassword=Remember Your Password",
"wintitle:notifyDamagedBulletinMovedToDiscarded=Moved Damaged Bulletin",
"wintitle:notifyUploadFailedBulletinNotSentToServer=Unable to Send Bulletin To Server",
"wintitle:notifyPreviewOneBulletinOnly=Preview One Bulletin Only",
"wintitle:notifyPreviewNoBulletinsSelected=No Bulletin Selected",
"wintitle:notifyRetrievePreviewNotAvailableYet=Unable To Preview",
"wintitle:notifyRetrievedOnlySomeSummaries=Error During Retrieve",
"wintitle:notifyConfirmCorrectDefaultDetailsData=Confirm Correct Default Details Content",
"wintitle:notifyExportComplete=Export Bulletins",
"wintitle:notifyExportCompleteMissingAttachments=Export Bulletins",
"wintitle:notifyErrorDuringExit=Error During Exit",

"wintitle:notifyErrorWritingFile=Error Writing File",
"wintitle:notifyErrorReadingFile=Error Reading File",
"wintitle:notifyExportZeroBulletins=No Bulletins Selected",
"wintitle:notifyPrintZeroBulletins=No Bulletins Selected",
"wintitle:notifyNoGridRowSelected=No Row Selected",
"wintitle:notifyNonStringFieldRowSelected=Load Values",
"wintitle:notifyNoImportFileSpecified=No Folder Specified",

"wintitle:notifyUserRejectedServerCompliance=Server Compliance Statement",
"wintitle:notifyExistingServerRemoved=Server Removed",
"wintitle:notifyErrorSavingBulletin=Error Saving",
"wintitle:notifyExportFolderEmpty=Error Exporting Folder",
"wintitle:notifyErrorBackingUpKeyShare=Error Backing Up Key",				
"wintitle:notifyRecoveryProcessKeyShare=Restore Account from Key Backup",				
"wintitle:notifyRecoveredKeyShareSucceededNewUserNamePasswordRequired=Restore Succeeded",				
"wintitle:notifyVerifyKeyPairSharePassed=Verification Succeeded",				
"wintitle:notifyRecoveryOfKeyShareComplete=Restore Complete",
"wintitle:notifyOperationCompleted=Finished",
"wintitle:notifycontactRejected=Error Sending ContactInfo",
"wintitle:notifyCreateAccountFailed=Error Creating New Account",
"wintitle:notifyUserAlreadyExists=User Already Exists",
"wintitle:notifyRecoveryProcessBackupFile=Restore Key",
"wintitle:notifyRecoveryOfKeyPairComplete=Successful Key Restore",
"wintitle:notifyErrorRecoveringAccountDirectory=Error During Restore",
"wintitle:notifyServerSSLNotResponding=Unable to connect",
"wintitle:notifyAlreadyRunning=Martus Already Running",
"wintitle:notifyAttachmentNotAFile=Attachment Chosen Not a File",
"wintitle:notifyFilesWillNotBeDeleted=Files Not Deleted",
"wintitle:notifyHQKeyAlradyExists=Headquarters Key Already Exists",
"wintitle:notifyFieldDeskKeyAlreadyExists=Field Desk Account Already Exists",
"wintitle:notifyFolderMigrationFailed=Folder Migration Error",
"wintitle:notifyResendErrorNotAuthorizedToSend=Not Authorized To Send",
"wintitle:notifyResendError=Error Sending Bulletin",
"wintitle:notifyErrorRenameFolder=Unable to Rename Folder",
"wintitle:notifyErrorRenameFolderExists=Unable to Rename Folder",
"wintitle:notifyAlreadyViewingThisVersion=Bulletin Version Already Being Viewed",
"wintitle:notifyBulletinVersionNotInSystem=Bulletin Version Not Found",
"wintitle:notifyHeadquarterLabelDuplicate=Headquarter Label Invalid",
"wintitle:notifyFieldDeskLabelDuplicate=Field Desk Label",
"wintitle:notifyNoHQsSelected=No Headquarters Selected",
"wintitle:notifyNoFieldDesksSelected=No Field Desks Selected",
"wintitle:notifyErrorExportingCustomizationTemplate=Error Exporting Template",
"wintitle:notifyErrorImportingCustomizationTemplate=Error Importing Template",
"wintitle:notifyErrorImportingCustomizationTemplateFuture=Error Importing Template",
"wintitle:notifyImportingCustomizationTemplateSuccess=Importing Template Succeeded",
"wintitle:notifyExportingCustomizationTemplateSuccess=Exporting Template Succeeded",
"wintitle:notifyCreatingFieldSpecCache=Missing List of Fields",
"wintitle:notifyRetrieveError=Retrieve Error",
"wintitle:notifyRetrieveInProgress=Retrieve In Progress",
"wintitle:notifyRetrieveFileDataVersionError=Warning",
"wintitle:notifyRetrieveFileError=Error",
"wintitle:notifyImportComplete=Import Complete",
"wintitle:notifyErrorImportingBulletins=Error Importing Bulletins",
"wintitle:notifyErrorExportingBulletins=Error Exporting Bulletins",
"wintitle:notifyImportMissingAttachments=Warning Missing Attachments",
"wintitle:notifyImportBulletinsNotImported=Warning Bulletins Not Imported",
"wintitle:notifyNoReportFieldsSelected=Create Report",
"wintitle:notifyNotValidReportFormat=Reports",
"wintitle:notifyReportFormatIsOld=Reports",
"wintitle:notifyReportFormatIsTooNew=Reports",
"wintitle:notifyReportFormatDifferentLanguage=Reports",
"wintitle:notifyViewAttachmentNotAvailable=Unable to View Attachment",
"wintitle:notifyAddPermissionsZeroBulletinsOurs=Update headquarters access to Bulletins",
"wintitle:notifyAddPermissionsZeroHeadquartersSelected=No Headquarters Selected",
"wintitle:notifySealSelectedZeroBulletinsOurs=Seal Bulletins",
"wintitle:notifyErrorImportingBulletinsTooOld=Import Bulletin(s)",
"wintitle:notifyErrorImportingBulletinsTooNew=Import Bulletin(s)",
"wintitle:notifyErrorSavingDictionary=Error",
"wintitle:notifyErrorLoadingDictionary=Error",
"wintitle:notifyErrorUpdatingDictionary=Error",
"wintitle:ConfigureSpellCheck=Configure Spell Checking",

"wintitle:notifyRetrieveCompleted=Retrieve Complete",
"field:notifyRetrieveCompletedcause=Retrieving bulletins from the server is complete",

"wintitle:inputservername=Server Name",
"wintitle:inputserverpubliccode=Server Identification",
"wintitle:inputservermagicword=Request Upload Permission",
"wintitle:inputImportPublicCode=Import Account Public Information",
"wintitle:inputImportPublicKey=Import Account Public Information",
"wintitle:inputCustomFields=Customize Fields",
"wintitle:inputGetShareFileName=Default Backup File Name",
"wintitle:inputGetHQLabel=Headquarters Label",
"wintitle:inputGetFieldDeskLabel=Field Desk Label",

"wintitle:ErrorDateInFuture=Date Invalid",
"wintitle:ErrorDateRangeInverted=Date Range Invalid",
"wintitle:ErrorDateTooEarly=Date Too Early",
"wintitle:ErrorDateTooLate=Date Too Late",
"wintitle:setupsignin=Martus Setup Signin",
"wintitle:setupcontact=Martus Setup Contact Information",
"wintitle:BulletinTemplate=Details Field Default Content",
"wintitle:RetrieveMySealedBulletins=Retrieve My Sealed Bulletins",
"wintitle:RetrieveMyDraftBulletins=Retrieve My Draft Bulletins",
"wintitle:RetrieveHQSealedBulletins=Retrieve Field Desk Sealed Bulletins",
"wintitle:RetrieveHQDraftBulletins=Retrieve Field Desk Draft Bulletins",
"wintitle:DeleteMyDraftsFromServer=Delete My Drafts From Server",
"wintitle:about=About Martus",
"wintitle:AccountInfo=Account Information",
"wintitle:Help=Help on Martus",
"wintitle:RetrieveMySealedBulletinProgress=Retrieving Bulletins",
"wintitle:RetrieveMyDraftBulletinProgress=Retrieving Bulletins",
"wintitle:RetrieveHQSealedBulletinProgress=Retrieving Bulletins",
"wintitle:RetrieveHQDraftBulletinProgress=Retrieving Bulletins",
"wintitle:RetrieveMySealedBulletinSummaries=Retrieving Bulletin Summaries",
"wintitle:RetrieveMyDraftBulletinSummaries=Retrieving Bulletin Summaries",
"wintitle:RetrieveHQSealedBulletinSummaries=Retrieving Bulletin Summaries",
"wintitle:RetrieveHQDraftBulletinSummaries=Retrieving Bulletin Summaries",
"wintitle:DeleteServerDraftsBulletinSummaries=Retrieving Bulletin Summaries",
"wintitle:ConfigServer=Server Configuration",
"wintitle:ServerSelectionResults=Server Configuration Results",
"wintitle:search=Search",
"wintitle:BulletinPreview=Bulletin Preview",
"wintitle:DeleteBulletinFile=Delete Bulletin File",
"wintitle:ServerNews=Server News: Message #CurrentNewsItem# of #MaxNewsItems#",
"wintitle:LoadDefaultDetails=Load Default Details Field Content",
"wintitle:ExportBulletins=Export Bulletins",
"wintitle:SaveShareKeyPair=Saving Backup Disk",
"wintitle:SaveRecoverShareKeyPairOf=of",
"wintitle:BackupSecretShareCompleteInformation=Backup Complete",				
"wintitle:RecoverShareKeyPair=Restore from Backup Disk",				
"wintitle:ErrorPreviousBackupShareExists=Previous File Exists",
"wintitle:ErrorRecoverNoAppropriateFileFound=No Appropriate File Found",
"wintitle:ErrorRecoverShareDisk=Key backup File Error",				
"wintitle:ErrorVerifyingKeyPairShare=Verification Error",				
"wintitle:VerifyingKeyPairShare=Verifying Disk",
"wintitle:RemoveServer=Remove Server",
"wintitle:BackupKeyPairGeneralInfo=Backup Key Information",
"wintitle:BackupKeyPairToSecretShareInformation=Backup Key Information ",
"wintitle:ErrorAttachmentMissing=Attachment Missing",
"wintitle:ErrorRequiredFieldBlank=Required Field Blank",
"wintitle:helpStringNotFound=Search Text Not Found",
"wintitle:CreateCustomFieldsHelp=Help on Creating Custom Fields",
"wintitle:ErrorCustomFields=Customize Fields Error",
"wintitle:ConfigureHQs=Configure Headquarters Accounts",
"wintitle:ManageFieldDeskKeys=Configure Field Desk Accounts",
"wintitle:ExportCustomizationTemplateSaveAs=Export Customization Template",
"wintitle:SetFolderOrder=Folder Order",
"wintitle:FancySearchHelp=Search Help",

"wintitle:warningdeletefolder=Confirm Delete Folder",
"wintitle:warningDeleteSingleBulletin=Confirm Delete Bulletin",
"wintitle:warningDeleteMultipleBulletins=Confirm Delete Bulletins",

"wintitle:RunOrCreateReport=Reports",
"wintitle:ReportChooseSortFields=Reports",
"wintitle:ChooseReportToRun=Run Report...",
"wintitle:ChooseReportFields=Create Report",
"wintitle:OrganizeReportFields=Organize Report Fields",
"wintitle:PrintOptions=Print Options",
"wintitle:confirmSendWithPublicData=Confirm Save With Public Data",
"wintitle:DeleteMyDataFromThisComputer=Delete My Data From This Computer",
"wintitle:RemoveMartsFromThisComputer=Remove Martus From This Computer",
"wintitle:ResendBulletins=Resend Bulletins",
"wintitle:ImportBulletins=Import Bulletins",

"wintitle:LoadSavedSearch=Load Saved Search",
"wintitle:SaveSearch=Save Search",
"wintitle:SearchProgress=Searching...",
"wintitle:ReportSearchProgress=Searching...",
"wintitle:LoadingFieldValuesFromAllBulletins=Loading Values...",

"button:help=Help",
"button:create=Create",
"button:search=Search",
"button:print=Print",
"button:connectserver=Connect",
"button:send=Save Sealed",
"button:savedraft=Save Draft",
"button:ok=OK",
"button:inputservernameok=OK",
"button:inputserverpubliccodeok=OK",
"button:inputsearchok=Search",
"button:inputservermagicwordok=OK",
"button:inputImportPublicCodeok=Import",
"button:inputImportPublicKeyok=Import",
"button:inputLoadDefaultDetailsok=Load",
"button:inputCustomFieldsok=OK",
"button:inputGetShareFileNameok=OK",
"button:inputGetHQLabelok=OK",
"button:inputGetFieldDeskLabelok=OK",
"button:close=Close",
"button:customDefault=Restore Defaults",
"button:customHelp=Help",
"button:customImport=Import Template",
"button:customExport=Export Template",
"button:AddPermissions=Update Headquarters Access",

"button:cancel=Cancel",
"button:save=Save",
"button:browse=Browse...",
"button:retrieve=Retrieve",
"button:DeleteServerDrafts=Delete",
"button:checkall=Check All",
"button:uncheckall=Uncheck All",
"button:addattachment=Add Attachment",
"button:removeattachment=Remove",
"button:attachmentLabel=Name",
"button:attachmentSize=Size(Kb)",
"button:saveattachment=Save...",
"button:viewattachment=View",
"button:hideattachment=Hide",
"button:VirtualKeyboardSwitchToNormal=Switch to using regular keyboard",
"button:VirtualKeyboardSwitchToVirtual=Switch to using on-screen keyboard",
"button:DownloadableSummaries=Show bulletins that are only on the server.",
"button:AllSummaries=Show all bulletins on this server and on this computer.",
"button:Preview=Preview",
"button:Delete=Delete",
"button:Leave=Leave",
"button:modify=Modify",
"button:loadTemplateFromFile=Load From File",
"button:Back=Back",
"button:Continue=Continue",
"button:ResetContents=Reset Contents",
"button:ServerComplianceAccept=Accept",
"button:ServerComplianceReject=Reject",
"button:SignIn=Sign In",
"button:NewAccountTab=New Account",
"button:RecoverAccountTab=Restore Account",
"button:RecoverAccountByShare=Restore account from multiple disks",
"button:RecoverAccountByBackup=Restore account from backup key file",
"button:ConfigureHQsAdd=Add...",
"button:ConfigureHQsRemove=Remove",
"button:ConfigureHQsReLabel=Change Label",
"button:ConfigurePublicKeysView=View",
"button:EditFieldDeskLabel=Change Label",
"button:BulletinDetails=Bulletin Details...",
"button:ViewPreviousBulletinVersion=View Selected Version...",
"button:RetrieveAllVersions=Retrieve all bulletin versions",
"button:RetrieveLatestBulletinRevisionOnly=Retrieve latest bulletin version only",
"button:FolderOrderUp=Up",
"button:FolderOrderDown=Down",
"button:UseOldCustomFields=Use Old",
"button:UseNewCustomFields=Use New",
"button:CloseHelp=Close Help",
"button:SearchFinalBulletinsOnly=Only search most recent version of bulletins",
"button:SearchSameRowsOnly=Match grid column specifications in the search screen to a single row of bulletin grid data. See the search Help screen for additional guidance on this advanced option.",
"button:DeleteSelectedGridRow=Delete Selected Row",
"button:InsertEmptyGridRow=Insert Row",
"button:AppendEmptyGridRow=Append Row",
"button:PopUpTreeChoose=Choose Field...",
"button:RunReport=Use Existing Report Format",
"button:CreateTabularReport=Create New Tabular Report",
"button:CreatePageReport=Create New Page Report",
"button:SelectReport=Select",
"button:FieldLabel=Field Label",
"button:FieldType=Type",
"button:FieldTag=Tag",
"button:SaveSearch=Save This Search...",
"button:LoadSearch=Load Previous Search...",
"button:LoadSearchOkButton=Load",
"button:LoadFieldValuesFromAllBulletins=Load all possible values for selected field",

"button:AddFieldToReport=Add",
"button:RemoveFieldFromReport=Remove",
"button:MoveFieldUpInReport=Move Up",
"button:MoveFieldDownInReport=Move Down",

"button:ShowGridExpanded=Show Expanded",
"button:ShowGridNormal=Show as Grid",

"button:IncludePrivateBulletins=Include Private Data",

"menu:file=File",
"menu:CreateNewBulletin=Create New Bulletin",
"menu:printBulletin=Print Bulletin(s)",
"menu:printButton=Print",
"menu:Reports=Reports",
"menu:Charts=Charts",
"menu:ExportBulletins=Export Bulletins",
"menu:ImportBulletins=Import Bulletins",
"menu:ExportFolder=Export Folder",
"menu:exit=Exit",
"menu:edit=Edit",
"menu:search=Search",
"menu:modifyBulletin=Modify Bulletin",
"menu:SelectAllBulletins=Select All Bulletins",
"menu:CutBulletins=Cut Bulletin(s)",
"menu:CopyBulletins=Copy Bulletin(s)",
"menu:PasteBulletins=Paste Bulletin(s)",
"menu:DiscardBulletins=Discard Bulletin(s)",
"menu:DeleteBulletins=Delete Bulletin(s)",
"menu:AddPermissions=Update Headquarters Access...",
"menu:SealSelectedBulletins=Seal Bulletin(s)",
"menu:ResendBulletins=Resend Bulletin(s)",
"menu:folders=Folders",
"menu:CreateNewFolder=Create New Folder",
"menu:RenameFolder=Rename Folder",
"menu:DeleteFolder=Delete Folder",
"menu:OrganizeFolders=Organize Folders",
"menu:server=Server",
"menu:RetrieveMySealedBulletins=Retrieve My Sealed Bulletins",
"menu:RetrieveMyDraftsBulletins=Retrieve My Draft Bulletins",
"menu:DeleteMyServerDrafts=Delete My Drafts From Server",
"menu:RetrieveHQSealedBulletins=Retrieve Field Desk Sealed Bulletins",
"menu:RetrieveHQDraftBulletins=Retrieve Field Desk Draft Bulletins",
"menu:SelectServer=Select Martus Server",
"menu:options=Options",
"menu:Preferences=Preferences",
"menu:contactinfo=Contact Information",
"menu:DefaultDetailsFieldContent=Default Details Field Content",
"menu:ConfigureSpellCheck=Spell Checking",
"menu:changeUserNamePassword=Change User Name or Password",
"menu:tools=Tools",
"menu:QuickEraseDeleteMyDataOnly=Delete My Data",
"menu:QuickEraseRemoveMartus=Delete All Data And Remove Martus",
"menu:BackupMyKeyPairFile=Backup My Key",
"menu:ExportMyPublicKey=Export My Public Account Id",
"menu:ConfigureHQs=Configure Headquarters",
"menu:ManageFieldDesks=Configure Field Desks",
"menu:CustomFields=Customize Fields",
"menu:help=Help",
"menu:helpMessage=Help",
"menu:about=About Martus",
"menu:ViewMyAccountDetails=View My Account Details",
"menu:cut=Cut",
"menu:copy=Copy",
"menu:paste=Paste",
"menu:delete=Delete",
"menu:selectall=Select All",
"menu:RemoveServer=Remove Martus Server",
"menu:MoreSpellingSuggestions=More Suggestions",
"menu:AddToDictionary=Add '#NewWord#' to User Dictionary",

"field:translationVersion=English",
"field:aboutDlgVersionInfo=software version",
"field:aboutDlgMlpDateInfo=Language Pack Date",
"field:aboutDlgTranslationVersionInfo=Translation version",
"field:aboutDlgBuildDate=Built on",
"field:aboutDlgDisclaimer=Martus comes with ABSOLUTELY NO WARRANTY, and is made available under license terms in the file named license.txt in the Martus directory. This is free software, and you are welcome to redistribute it under certain conditions discussed in license.txt. ",
"field:aboutDlgCredits=Martus Software is developed by Benetech in partnership with the Information Program of the Open Society Institute, Aspiration, and the John D. and Catherine T. MacArthur Foundation.",

"field:status=Status",

"field:BulletinSize=Size (Kb)",

"field:AuthorRequired=Author (required)",
"field:OrganizationRequired=Organization (required)",
"field:TemplateDetails=Details",
"field:connecting=Connecting...",
"field:authorizing=Authorizing...",
"field:confirming=Confirming...",
"field:disconnecting=Disconnecting...",
"field:dateformat=Date Format",
"field:MayBeDamaged=Warning: Portions may be missing or damaged",
"field:NotAuthorizedToViewPrivate=The author has not given you permission to view the private portions of this bulletin.",
"field:BulletinHasUnknownStuff=Warning: Some information in this bulletin is not visible",
"field:retrieveflag=Retrieve?",
"field:DeleteFlag=Delete?",
"field:waitingForKeyPairGeneration=Please wait a minute while your account is being created...",
"field:waitingForBulletinsToLoad=Loading Martus.  Please wait...",
"field:waitAfterFailedSignIn=Please wait to sign in again ...",
"field:HelpDefaultDetails=Enter questions, details, or other information your organization wants to have answered in future bulletins created.",
"field:HelpExampleDefaultDetails=Example:",
"field:HelpExample1DefaultDetails=Were there any witnesses?",
"field:HelpExample2DefaultDetails=List Victim Names and Ages:",
"field:HelpExampleEtcDefaultDetails=etc...",
"field:PublicInformationFiles=Public Information Files (*.mpi)",
"field:BulletinXmlFileFilter=Bulletin Files (*.xml)",
"field:NormalKeyboardMsg1=Remember: Entering your password using the regular keyboard may reduce security.",
"field:NormalKeyboardMsg2=For maximum security switch to the on-screen keyboard.",
"field:RetrieveSummariesMessage=All bulletins retrieved will still remain on the server.\nYou can only retrieve bulletins that are not currently on your computer.",
"field:DeleteServerDraftsMessage=You can only delete draft bulletins from the server that are not currently on your computer.",
"field:ContactInfoRequiredFields=This information identifies your organization.\nYou must enter either an Author or Organization, and both are shown in every bulletin you create.",
"field:ContactInfoFutureUseOfFields=This information will be available to anyone who can view your public bulletin information, or anyone with administrative rights on the server. \n\nThis allows people to contact you for further information.",
"field:ContactInfoUpdateLater=You can change any of this information later, by choosing Options/Contact Info.",
"field:UploadingSealedBulletin=Sending Sealed Bulletin",
"field:UploadingDraftBulletin=Sending Draft Bulletin",
"field:StatusReady=Ready",
"field:StatusRetrieving=Retrieving...",
"field:StatusConnecting=Connecting to server",
"field:statusCheckingForNewFieldOfficeBulletins=Checking for new Field Desk bulletins...",
"field:statusNewFieldOfficeBulletins=New Field Desk bulletins found on server",
"field:RetrieveMySealedBulletinProgress=Retrieving My Sealed Bulletins",
"field:RetrieveMyDraftBulletinProgress=Retrieving My Draft Bulletins",
"field:RetrieveHQSealedBulletinProgress=Retrieving Field Desk Sealed Bulletins",
"field:RetrieveHQDraftBulletinProgress=Retrieving Field Desk Draft Bulletins",
"field:NoServerAvailableProgressMessage=Server Not Available",
"field:ServerNotConfiguredProgressMessage=Server needs to be configured.",
"field:UploadFailedProgressMessage=Upload Failed",
"field:ChunkProgressStatusMessage=Download Progress",
"field:RetrieveMySealedBulletinSummaries=Retrieving My Sealed Bulletin Summaries",
"field:RetrieveMyDraftBulletinSummaries=Retrieving My Draft Bulletin Summaries",
"field:RetrieveHQSealedBulletinSummaries=Retrieving Field Desk Sealed Bulletin Summaries",
"field:RetrieveHQDraftBulletinSummaries=Retrieving Field Desk Draft Bulletin Summaries",
"field:TorStatusActive=Embedded Tor Active",
"field:TorStatusInitializing=Embedded Tor Initializing",
"field:TorStatusDisabled=Embedded Tor Disabled",
"field:DeleteBulletinFileMsg1=This bulletin has been pasted into Martus from the file:",
"field:DeleteBulletinFileMsg2=Do you want to delete the original file, or leave it in place?",
"field:OnlineHelpMessage=Details:",
"field:OnlineHelpTOCMessage=Topics:",
"field:OnlineHelpTOCMessageDescription=Click on topic to display details.",
"field:DefaultDetailFiles=Default Details",
"field:ServerComplianceDescription=The server you have selected has provided the following statement describing its compliance with the official guidelines for the secure and reliable operation of a Martus server.  You can accept or reject this server based on its compliance statement.",
"field:ServerComplianceChangedDescription=The current server has updated its statement describing its compliance with the official guidelines for the secure and reliable operation of a Martus server.  The new statement appears below. You can accept or reject this server based on its compliance statement.",
"field:RecoverShareKeyPair=Another part of the shared key is required to complete this process.",
"field:ErrorPreviousBackupShareExists=A previous backup file exists.  You must save each file on its own removable media disk.",		
"field:ErrorRecoverNoAppropriateFileFound=No appropriate backup file was found on this disk, please try a different disk.",
"field:ErrorRecoverShareDisk=An error occurred reading this key backup file.",
"field:ErrorVerifyingKeyPairShare=An error occurred verifying this key backup file.",
"field:RecoverAccount=Restore your existing account from a backup file.",
"field:ErrorBackingupKeyPair=Unable to back up the key file on this disk.",
"field:BackupKeyPairGeneralInfo=Your key is required to read your private bulletin information or to create new bulletins.  To ensure that you'll always have access to your data, it is extremely important to keep a backup of your key.\n\nMartus offers two methods for backing up your key, either of which may be used, but we strongly recommend using both backup methods.  The first method creates a single, password encrypted key back up file onto removable media (e.g. floppy disk, cd, USB storage) or a network drive; the second method backs up the key in #TotalNumberOfFilesInBackup# separate pieces (not encrypted with your password) onto multiple floppy disks (or other removable media).\n",
"field:BackupKeyPairToSecretShareInformation=This method breaks up your key into #TotalNumberOfFilesInBackup# pieces, any #MinimumNumberOfFilesNeededForRecovery# of which will be required to reconstruct the key without requiring a password.  If you forget your login name or your password, this method is the only way you can restore your key.\n\nOnce the #TotalNumberOfFilesInBackup# files have been written to removable media disks, you should distribute them to #TotalNumberOfFilesInBackup# different people whom you will remember, but each of whom don't know who you have given the other files to.  Do not store any of the disks together.\n\nTo restore your key, first retrieve any #MinimumNumberOfFilesNeededForRecovery# of the original #TotalNumberOfFilesInBackup# disks, and then select 'Restore Account' and 'Restore account from multiple disks' when signing into Martus.\n",
"field:BulletinWasSent=Sent",
"field:" + Bulletin.PSEUDOFIELD_WAS_SENT + "=Sent",
"field:preferencesAdjustThai=Automatically adjust legacy Thai dates",
"field:preferencesAdjustPersian=Automatically adjust legacy Afghan/Persian dates",
"field:preferencesUseZawgyi=Use Zawgyi font for Burmese display and input",
"field:preferencesAllPrivate=Prevent creating public bulletins",
"field:preferencesCheckFieldOfficeBulletins=Automatically check for new Field Desk bulletins",
"field:PreferencesUseInternalTor=Use embedded Tor (Using Tor can improve security but may be slower than not using Tor)",
"field:HeadQuartersSelected=Selected?",
"field:ConfigureHeadQuartersDefault=Default?",
"field:CustomizationTemplateFileFilter=Customization Template Files (*.mct)",
"field:SearchGridHeaderField=Field(s) to search",
"field:SearchGridHeaderOp=Compare how?",
"field:SearchGridHeaderValue=Search for...",
"field:SearchOpContains=contains",
"field:SearchAnyField=--Any Field--",
"field:DuplicateLabelsInCustomTemplate=The following labels appear more than once in your template.",
"field:DuplicateLabels=Labels",
"field:DuplicateLabelsInCustomTemplateContinue=Do you want to save this template with the duplicate labels?",
"field:PrintPrivateDataMessage=You have the option to include or exclude the selected bulletin's private information in this print out. " +
	"If you choose to print private data, it will be visible to anyone who sees the print out.",
"button:PrintOnlyPublic=Print only public information",
"button:PrintPublicAndPrivate=Include private information",
"field:PrintToPrinterOrDisk=You can print the bulletin contents to a printer or to a file.",
"button:PrintToPrinter=Print to a printer",
"button:PrintToDisk=Print to a file",
"wintitle:PrintToWhichFile=Print to which file",
"field:DefaultPrintToDiskFileName=bulletins.html",
"wintitle:notifyPrintToDiskComplete=Print To Report File",
"field:notifyPrintToDiskCompletecause=Successfully saved Report",
"wintitle:notifyPrintCompleted=Reports",
"field:notifyPrintCompletedcause=Printing Complete",
"field:OrganizeReportFields=Add the fields you want to appear in your report, and then arrange them in the order you want them to be printed.",
"field:AddPermissionsOverview=This allows you to update headquarters access to existing bulletins. " +
	"Each of the bulletins in the list below will be modified to add the selected headquarters accounts. " +
	"If a bulletin is sealed, a new version will be created.",
"field:SkippingBulletinsNotOurs=NOTE: Bulletins authored by other accounts cannot be changed using this command, " +
	"so they are not included in the list above.",
"field:ChooseHeadquartersToAdd=Select the headquarters listed below that you want to add to all the bulletins listed above.",
	
"wintitle:IncludePrivateData=Include Private Data",
"field:IncludePrivateData=#TotalBulletins# bulletins were selected for printing.  #AllPrivateBulletins# of them are all private.\n What would you like to include when printing?",
"button:PublicAndPrivateData=Both Public and Private Data",
"button:PublicOnly=Public Data Only",

"wintitle:PrintPreview=Print Preview",
"button:PrintToPrinter=Print To Printer",
"button:PrintToFile=Print To File",

"wintitle:RunOrCreateChart=Charts",
"button:RunChart=Display an existing chart",
"button:CreateChart=Create a new chart template",
"wintitle:CreateChart=Charts",
"field:ChartPrivateFieldsNotice=Please note that any Martus bulletin data in charts will not be encrypted, \nand anyone who gets a copy of the chart output will be able to read all the data. \nUse caution when selecting a private field.",
"field:ChartType=Type of Chart",
"field:ChartTypeBar=Bar Chart",
"field:ChartType3DBar=3D Bar Chart",
"field:ChartTypePie=Pie Chart",
"field:ChartItemLabelBlank=(Missing field or data)",
"field:ChartFieldToCount=Field to count",
"field:ChartSubtitle=Subtitle (optional)",
"field:ChartTitle=Martus Bulletins by #SelectedField#",
"field:ChartCreatedOn=Chart produced #Date#",
"field:ChartSeriesTitle=Martus Bulletin Counts",
"field:ChartYAxisTitle=Martus Bulletin Count",
"field:ChartPieSliceLabel=#Count# bulletins = #Percent# of Martus bulletins",
"field:ChartSelectedBulletinsDisclaimerBar=Note: Chart shows number of Martus bulletins matching search criteria.",
"field:ChartSelectedBulletinsDisclaimerPie=Note: Chart shows % of Martus bulletins matching search criteria.",
"field:DefaultPrintChartToDiskFileName=chart.jpeg",
"wintitle:notifyChartCompleted=Charts",
"field:notifyChartCompletedcause=Printing complete",
"wintitle:notifyChartUnknownError=Charts",
"field:notifyChartUnknownErrorcause=An unknown error has occurred",

"wintitle:PreparingBulletins=Preparing Bulletins...",
"field:PreparingBulletins=Preparing Bulletins...",

"wintitle:SealingSelectedBulletins=Sealing Bulletins...",
"field:SealingSelectedBulletins=Sealing Bulletins...",

"wintitle:AddingPermissionsToBulletins=Update headquarters access to Bulletins",
"field:AddingPermissionsToBulletins=Updating access...",
"wintitle:confirmAddingPermissionsToBulletinsCancel=Cancel",
"field:confirmAddingPermissionsToBulletinsCancelcause=The operation will be stopped immediately.",
"field:confirmAddingPermissionsToBulletinsCanceleffect=If you do this, the bulletins already processed will have the new headquarters access, but other selected bulletins will not.",
"wintitle:notifyViewKeyDetails=View Key Details",
"field:notifyViewKeyDetailscause=Label: #Label#\n\nPublic Code: #PublicCode#\n\nPublic Account Id: #PublicKey#",
"wintitle:FileDialogAddAttachment=Add Attachment",
"button:FileDialogOkAddAttachment=Add Attachment",

"wintitle:FileDialogRestoreFromKeyPair=Restore Account",
"button:FileDialogOkRestoreFromKeyPair=Restore",

"wintitle:FileDialogImportMBA=Import Martus Bulletin Archive",
"button:FileDialogOkImportMBA=Import",

"wintitle:FileDialogImportCustomization=Import Customization Template",
"button:FileDialogOkImportCustomization=Import",

"wintitle:FileDialogImportBulletins=Import Bulletins",
"button:FileDialogOkImportBulletins=Import",

"wintitle:FileDialogImportHeadquartersPublicKey=Import Headquarters Account",
"button:FileDialogOkImportHeadquartersPublicKey=Import",

"wintitle:FileDialogImportFieldDeskPublicKey=Import Field Desk Public Information File",
"button:FileDialogOkImportFieldDeskPublicKey=Import",

"wintitle:FileDialogRecoverSharedKeyPair=Recover Shared Key",
"button:FileDialogOkRecoverSharedKeyPair=Recover",

"wintitle:FileDialogSaveReportFormat=Save Report Format",

"wintitle:FileDialogPrintToFile=Print to File",

"wintitle:FileDialogSaveKeyPair=Backup Key File",

"wintitle:FileDialogExportMBA=Export Martus Bulletin Archive",

"wintitle:FileDialogSaveAttachment=Save Attachment",

"wintitle:FileDialogExportBulletins=Export Bulletins",

"wintitle:FileDialogExportCustomization=Export Customization Template",

"wintitle:FileDialogExportPublicKey=Export Public Account Id",

"field:VirtualUserNameDescription=(Enter using regular keyboard)",
"field:VirtualPasswordDescription=Enter Password using mouse with on-screen keyboard below",
"field:VirtualKeyboardKeys=ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-+=!@#$%^&*()_,.[]{}<>\\/?|;:~",
"field:VirtualKeyboardSpace=Space",
"field:VirtualKeyboardBackSpace=Back Space",

"field:confirmquestion=Are you sure you want to do this?",
"field:confirmsendcause=You have chosen to save a completed bulletin.",
"field:confirmsendeffect=This will permanently seal the bulletin and you will not be allowed to make any further modifications to it except by creating a new version of the bulletin.",
"field:confirmdeletefoldercause=You have chosen to permanently delete a folder.  ",
"field:confirmdeletefoldereffect=Any bulletins in the folder will be moved to Discarded Bulletins.  ",
"field:confirmretrievecause=You have chosen to retrieve all bulletins from the Martus server.  ",
"field:confirmretrieveeffect=This will restore all the discarded bulletins that were sent to the server.  ",
"field:confirmRemoveAttachmentcause=You have chosen to remove the selected attachments from this bulletin.",
"field:confirmRemoveAttachmenteffect=The selected attachments will be permanently removed from this bulletin.",
"field:confirmOverWriteExistingFilecause=A file already exists with that name.",
"field:confirmOverWriteExistingFileeffect=The existing file will be deleted and replaced with the new information.",
"field:confirmCancelModifyBulletincause=You have chosen to cancel modifying this bulletin.",
"field:confirmCancelModifyBulletineffect=Any changes you have made to this bulletin will be discarded.",
"field:confirmSetImportPublicKeycause=You have chosen to allow this client the ability to view your public and PRIVATE data.",
"field:confirmSetImportPublicKeyeffect=By clicking on Yes you are authorizing this client to view all portions of your bulletins.",
"field:confirmImportFieldDeskPublicKeycause=You have chosen to treat this account as a Field Desk.",
"field:confirmImportFieldDeskPublicKeyeffect=By clicking on Yes, you will cause all bulletins created by this account to be marked as having been created by a trusted account.",
"field:confirmWarningSwitchToNormalKeyboardcause=Warning! Using the regular keyboard to enter your password greatly reduces the security of the Martus system, and could make it easier for an attacker to view your private data.",
"field:confirmWarningSwitchToNormalKeyboardeffect=If this is a Headquarters computer, it is especially important to use the on-screen keyboard, because an attacker could gain access to all the private data that you are authorized to view.",
"field:confirmClearHQInformationcause=You have chosen to remove the selected Headquarters accounts.  Any existing saved bulletins will still be visible by the old Headquarters.  If you want to disallow Headquarters viewing access to previously created Draft bulletins they must be re-saved after revoking the Headquarters.  New versions of sealed bulletins will not be able to be viewed by the removed Headquarters, but any old versions of sealed bulletins that already are on a Headquarters' computer will still be viewable by the Headquarters.",
"field:confirmClearHQInformationeffect=By clicking on Yes, any draft or sealed bulletins saved in the future will no longer be accessible by your Headquarters.",
"field:confirmRemoveFieldDeskKeyscause=You have chosen to remove the selected Field Desk account(s).",
"field:confirmRemoveFieldDeskKeyseffect=By clicking on Yes, bulletins created by those accounts will no longer be indicated as having been authored by a Field Desk",
"field:confirmCloneMySealedAsDraftcause=You have chosen to modify one of your sealed bulletins, thus creating a new version of that bulletin.",
"field:confirmCloneMySealedAsDrafteffect=Clicking on Yes will create a new version of this bulletin that initially contains all the same information.",
"field:confirmCloneBulletinAsMinecause=You have chosen to modify a bulletin that was created by someone else.",
"field:confirmCloneBulletinAsMineeffect=Clicking on Yes will create a new bulletin that contains a copy of all the same information. You will be the official author of this new bulletin, and any private data in it will only be visible by you (and your Headquarters accounts, if you have any). The original bulletin will remain unchanged.",
"field:confirmCloneUnverifiedFDBulletinAsMinecause=You have chosen to modify a bulletin that was created by a field desk account that has not been verified, so the bulletin could contain incorrect information or potentially damaging attachments. We recommend that you verify this account before continuing (See Tools > Configure Field Desks).",
"field:confirmCloneUnverifiedFDBulletinAsMineeffect=Clicking on Yes to continue with the modify operation will create a new bulletin that contains a copy of all the same information.  You will be the official author of this new bulletin, so you will become responsible for all the content. Any private data in it will only be visible to you (and your Headquarters accounts, if you have any).  The original bulletin will remain unchanged.",
"field:confirmPrinterWarningcause=Since you have changed from the default print tray your print out may be incorrect, if you also changed the size of paper used.  You must first select the paper tray and then select the paper size in that order, for both to get set correctly.  If you only wanted to change the paper tray then disregard this message and select 'No'.",
"field:confirmPrinterWarningeffect=Clicking on Yes will bring back the Printer Dialog so you can reselect your default paper tray and size.  Clicking on 'No' will print the document.",
"field:confirmRequestToSendContactInfoToServercause=Do you want to have your contact information sent to the current server?  This information may be sent to other servers, and will be viewable by anyone who has access to your public bulletin information or anyone with administrative rights on the server.",
"field:confirmRequestToSendContactInfoToServereffect=Clicking on Yes will send your contact information to the current server.",
"field:confirmUploadRemindercause=Please Note: There are bulletins that have not been sent to a server.  Do you still want to exit?",
"field:confirmUploadRemindereffect=Clicking on Yes will exit Martus, and leave the unsent bulletins.  They will be sent the next time you run Martus and connect to a server.",
"field:confirmDraftUploadRemindercause=Please Note: There are draft bulletins that have been modified and not yet sent to a server.  Do you still want to exit?",
"field:confirmDraftUploadRemindereffect=Clicking on Yes will exit Martus, and leave the unsent bulletins.  They will be sent the next time you run Martus and connect to a server.",
"field:confirmRedoWeakPasswordcause=The password you chose has fewer than 15 characters and less than 2 non-alphanumeric characters. We recommend choosing a stronger password.",
"field:confirmRedoWeakPasswordeffect=Clicking on Yes will continue with the password you just entered.",
"field:confirmExportPrivateDatacause=This will save the private information in the selected bulletins in plain text.",
"field:confirmExportPrivateDataeffect=Anyone who gets a copy of this file will be able to read all the private data.",
"field:confirmResetDefaultDetailscause=This will reset the current Default Details contents to the original contents. After resetting the contents, you should review them to be sure they are correct.",
"field:confirmResetDefaultDetailseffect=After you review the contents, you can accept them, modify them, or press cancel to keep the existing default details.",
"field:confirmNotYourBulletinViewAttachmentAnywayscause=Warning: the bulletin you are currently viewing is not yours.  Attachments can contain viruses, or malicious programs harmful to your computer.",
"field:confirmNotYourBulletinViewAttachmentAnywayseffect=Clicking on Yes will #action# the attachment. Clicking on No will cancel this action.",
"field:confirmServerComplianceFailedcause=Martus software is unable to determine whether this server complies with the official guidelines for operating a secure and reliable Martus server.",
"field:confirmServerComplianceFailedeffect=Benetech recommends that you do not use this server until a compliance statement is made available for you to read.  Continuing will select this non-compliant server anyway.",
"field:confirmWarnMissingOrCorruptAccountMapSignatureFilecause=Warning: acctmap.txt.sig file is missing or is corrupt in your account's packets directory.  If you have just upgraded to a new release of Martus this warning is harmless and you should click on No.  Otherwise this may indicate someone has tried to tamper with your data or could be caused by a hardware error.",		
"field:confirmWarnMissingOrCorruptAccountMapSignatureFileeffect=Clicking on Yes will try to generate a new acctmap.txt.sig file and continue to load Martus.  Clicking on No will exit Martus.",
"field:confirmWarnMissingAccountMapFilecause=Warning: acctmap.txt file in your account's packets directory is missing or is corrupt.  This may indicate someone has tried to tamper with your data or could be caused by a hardware error.",		
"field:confirmWarnMissingAccountMapFileeffect=Clicking on Yes will delete all of your bulletins and continue to load Martus.  Clicking on No will exit Martus so you can try to recover or repair this file manually.",
"field:confirmReportIncludePrivatecause=There are no bulletins with public data. Include private data?",
"field:confirmReportIncludePrivateeffect=Press 'Include Private Data' to continue with private data included, or press 'Cancel' to exit and return to Martus.",

"field:confirmBackupKeyPairSecretSharecause=We recommend that you now create #TotalNumberOfFilesInBackup# separate key backup files of which any #MinimumNumberOfFilesNeededForRecovery# can be used to reconstruct your key without a user name or password being required.",
"field:confirmBackupKeyPairSecretShareeffect=By choosing Yes, you will be prompted to save pieces of your key onto removable disks.",
"field:confirmCancelShareBackupcause=Do you wish to cancel this backup?",
"field:confirmCancelShareBackupeffect=By choosing Yes, you will exit this backup.",
"field:confirmEnterCustomFieldscause=You have chosen to customize the fields that will be used in any new bulletins created by this account.",
"field:confirmEnterCustomFieldseffect=Customizing fields is an advanced operation. You should only proceed if you are certain that you understand the feature.",
"field:confirmUndoCustomFieldscause=You have chosen to restore the standard set of Martus fields.",
"field:confirmUndoCustomFieldseffect=New bulletins created with this account will not have any custom fields.",
"field:confirmRecoverUsingKeySharecause=Welcome to Martus.  What would you like to do?",
"field:confirmRecoverUsingKeyShareeffect=Create a new account, or restore a previously created account?",
"field:confirmCancelShareRecovercause=Do you wish to cancel the key restore process?",
"field:confirmCancelShareRecovereffect=By choosing Yes, you will exit Martus.",
"field:confirmRecoveredKeyShareFailedTryAgaincause=Key Restore failed, we suggest you try again with a different set of files. Choose Yes to try again, No to exit Martus.",
"field:confirmRecoveredKeyShareFailedTryAgaineffect=Choosing Yes will begin the restore process again.",
"field:confirmErrorRecoverIvalidFileNamecause=The file you chose is not part of a key backup set.  These files are named '<name you picked>-#.dat' where # is the sequence number in the disk set previously created.",
"field:confirmErrorRecoverIvalidFileNameeffect=Choosing yes will bring up the file selection dialog again so you may pick a different file.",
"field:confirmBackupKeyShareVerifyDiskscause=We strongly recommend that you now verify all disks to make sure the data was written correctly.",
"field:confirmBackupKeyShareVerifyDiskseffect=By choosing Yes, the verification process will begin.",
"field:confirmCancelShareVerifycause=Do you wish to cancel the verification?",
"field:confirmCancelShareVerifyeffect=By choosing Yes, you will exit this verification step.",
"field:confirmEditBulletinWithUnknownTagscause=The bulletin you have chosen to modify contains information that this version of Martus cannot understand. It may have been created by a newer release of Martus, so you should ensure that you are running the latest release.",
"field:confirmEditBulletinWithUnknownTagseffect=If you modify this bulletin, the unknown information will be lost.",
"field:confirmExportUnknownTagscause=One or more of the bulletins you have chosen to export contain information that this version of Martus cannot understand. They may have been created by a newer release of Martus, so you should ensure that you are running the latest release.",
"field:confirmExportUnknownTagseffect=If you continue with the export, any unknown information will not be exported.",
"field:confirmQuickEraseOutboxNotEmptycause=Please Note: There are bulletins that have not been sent to a server. If you continue these bulletins will be lost.",
"field:confirmQuickEraseOutboxNotEmptyeffect=Clicking on Yes will bring you to a confirmation dialog, Clicking on No will return you to Martus without erasing anything.",
"field:confirmKeyPairFileExistsOverWritecause=A key file already exists for this account.  Do you wish to overwrite it?",
"field:confirmKeyPairFileExistsOverWriteeffect=Clicking on Yes will delete the old key file and replace it with your restored key.",
"field:confirmCancelBackupRecoverycause=Do you wish to cancel the key  restore process?",
"field:confirmCancelBackupRecoveryeffect=By choosing Yes, you will exit Martus.",
"field:confirmUnableToRecoverFromBackupFilecause=Key restore failed, we suggest you try again with a different backup file.",
"field:confirmUnableToRecoverFromBackupFileeffect=Choose Yes to begin the restore process again, No to exit Martus.",
"field:confirmWarningPathChosenMayNotBeRemoveablecause=Are you sure the directory you chose to save your backup files to is removable?  You cannot save all #TotalNumberOfFilesInBackup# files to the same location without inserting a new disk for each backup file.",
"field:confirmWarningPathChosenMayNotBeRemoveableeffect=Choosing Yes will begin the backup process as instructed.",
"field:confirmBackupKeyPairInformationcause=Save your password encrypted key backup file to a single floppy disk or other removable media.  Then store this disk in a safe place. You will use this disk, with your username and password, to restore your data if your system files are ever damaged.  To restore this password-protected key in the future, select 'Restore Account' and 'Restore account from backup key file' when signing into Martus.",		
"field:confirmBackupKeyPairInformationeffect=Choosing Yes will begin the backup process.",		
"field:confirmhelpStringNotFoundcause=The text \"#SearchString#\" was not found.  Do you wish to search from the beginning?",
"field:confirmhelpStringNotFoundeffect=Clicking on Yes will search for \"#SearchString#\" from the beginning.",
"field:confirmgeneralBackupKeyPairMsgcause=Martus has noticed that you have not completed the backup procedure of your key.  It is strongly recommended that you complete the backup procedure at this time.\n\nIf something happens to the original key file, and you don't have a backup you will not be able to open any bulletins you have created with this key.",
"field:confirmgeneralBackupKeyPairMsgeffect=Selecting Yes will guide you through the backup procedure, selecting No will skip this important step.",
"field:confirmbackupIncompleteEncryptedNeeded=A single encrypted backup file of your key has yet to be created.",
"field:confirmbackupIncompleteImprovedShareNeeded=Martus has improved the security of the multiple pieces key backup.  To keep your account secure and enable you to access your data if you forget your password, please create a new set of key backup files, and delete/destroy the old ones.",
"field:confirmbackupIncompleteShareNeeded=A multiple disk backup of your key has yet to be created.",
"field:confirmNeedsFolderMigrationcause=This account was created with an older release of Martus, which used a different set of folders. The folders need to be migrated to the new format. The migration is automatic, fast, and safe. You cannot run this release of Martus unless you allow this migration.",
"field:confirmNeedsFolderMigrationeffect=Answering Yes will allow the migration to proceed, answering No will exit Martus.",
"field:confirmNeedsBulletinVersioningMigrationcause=This account was created with an older release of Martus, which was unaware of multiple sealed bulletin versions. We need to update your system to recognize bulletins with versions.  The migration is automatic, fast, and safe. You cannot run this release of Martus unless you allow this migration.",
"field:confirmNeedsBulletinVersioningMigrationeffect=Answering Yes will allow the migration to proceed, answering No will exit Martus.",

"field:confirmUseBulletinsCustomFieldscause=The bulletin you are about to create is based on a previous bulletin whose custom fields are different from your current custom fields.  Do you wish to use the original bulletin's custom fields?",
"field:confirmUseBulletinsCustomFieldseffect=Answering '#UseOldCustomFields#' will use the old custom fields and all bulletin data.  Answering '#UseNewCustomFields#' will use your current custom fields and copy all similar fields to the new bulletin but you may lose some of the original bulletin's data.",
"field:confirmUseBulletinsDraftCustomFieldscause=The draft bulletin you are about to modify has custom fields which are different from your current custom fields.  Do you wish to use the original bulletin's custom fields?",
"field:confirmUseBulletinsDraftCustomFieldseffect=Answering '#UseOldCustomFields#' will use the old custom fields and all bulletin data.  Answering '#UseNewCustomFields#' will use your current custom fields and copy all similar fields to this new draft bulletin but you may lose some of the original bulletin's data.",
"field:confirmSendWithPublicDatacause=You have chosen to save a completed bulletin.",
"field:confirmSendWithPublicDataeffect=This will permanently seal the bulletin and you will not be allowed to make any further modifications to it " +
		"except by creating a new version of the bulletin.\n\n" +
		"The public information in this bulletin will be released for possible publication on the Martus Search Engine, " +
		"and may be viewed by people outside your organization. If you have sent your contact information to the server, that contact information will also be displayed with each public bulletin on the Martus Search Engine.\n\n" +
		"If there is information you do not want to be publicly available, " +
		"either move that data to the bottom (always Private) pane of the bulletin or " +
		"check the 'Keep ALL Information Private' box before saving the bulletin.",
"field:confirmUnAuthoredBulletinDeleteBeforePastecause=The bulletin entitled \"#Title#\" already exists in this system.  Do you wish to delete the old bulletin and replace it with this bulletin?",
"field:confirmUnAuthoredBulletinDeleteBeforePasteeffect=Answering Yes will replace the old bulletin with this bulletin.  Answering on No will leave the original bulletin and skip this file.",
"field:confirmRemoveMartuscause=You are about to delete all Martus data from this computer.",
"field:confirmRemoveMartuseffect=Answering Yes will delete all data, uninstall Martus and exit the program.  Answering No will return you to Martus with nothing deleted.",
"field:confirmDeleteMyDatacause=You are about to delete your Martus data from this computer.",
"field:confirmDeleteMyDataeffect=Answering Yes will delete your data and exit Martus.  Answering No will return you to Martus with nothing deleted.",
"field:confirmNewerConfigInfoFileFoundcause=Warning: It appears that you are trying to run an older release of Martus.  Running this release of Martus may result in reduced functionality, and some configuration settings created in the newer version will be ignored. We recommend that you upgrade your software.",  
"field:confirmNewerConfigInfoFileFoundeffect=Answering Yes will continue to use this older release of Martus.  Answering No will exit Martus so you can upgrade to the latest release.",
"field:confirmRetrieveNewerVersionscause=The following bulletin(s) selected for retrieval are newer versions of bulletins already on this computer:\n\n#Titles#",
"field:confirmRetrieveNewerVersionseffect=Each older version will be replaced by the newer version from the server.",
"field:confirmPrintAllPrivateDatacause=One or more bulletins will not be printed because all the information is private, and you marked the \"Print only public information\" box.",
"field:confirmPrintAllPrivateDataeffect=Answering '#PrintBack#' will allow you to return to the previous dialog so you can mark the \"Include private information\" box. " +
	"Answering '#PrintContinue#' will print only the public information in the bulletins and skip those bulletins which are all private.",
"field:confirmCancelRetrievecause=Bulletins are currently being retrieved from the server.",
"field:confirmCancelRetrieveeffect=This operation will cancel the current retrieval, so some of the requested bulletins may not be retrieved.",
"field:confirmSearchProgressCancelcause=The search in progress will be stopped immediately.",
"field:confirmSearchProgressCanceleffect=If you do this, the Search Results will only contain bulletins that have been found so far.",
"field:confirmReportSearchProgressCancelcause=The search in progress will be stopped immediately.",
"field:confirmReportSearchProgressCanceleffect=If you do this, the report will only include the bulletins that have been found so far.",

"field:confirmSealingSelectedBulletinsCancelcause=The operation in progress will be stopped.",
"field:confirmSealingSelectedBulletinsCanceleffect=If you do this, bulletins that have already been sealed will remain sealed, and bulletins that have not been processed yet will remain in their current state.",

"field:confirmDateRageInvalidcause=The date range you entered is invalid because the end date occurs before the begin date.",
"field:confirmDateRageInvalideffect=Answering Yes will take you back to the '#FieldLabel#' date range to fix the problem.  Answering No will swap the begin and end dates so they are in order.",

"field:confirmSealSelectedBulletinscause=This will seal all the currently selected draft bulletins.",
"field:confirmSealSelectedBulletinseffect=Any selected bulletins that are already sealed will remain unchanged.",

"field:confirmXmlSchemaNewerImportAnywaycause=This XML file was created by a newer version of Martus.",
"field:confirmXmlSchemaNewerImportAnywayeffect=If you continue with the import, some information in the bulletin(s) might not be imported.",

"field:confirmLoadingFieldValuesFromAllBulletinsCancelcause=This will stop loading all the values for this field.",
"field:confirmLoadingFieldValuesFromAllBulletinsCanceleffect=The field will be still/again be searchable as text.",

"field:notifyDropErrorBulletinExistscause=One or more bulletins cannot be moved to that folder, because it already exists in that folder.",
"field:notifyDropErrorBulletinOldercause=One or more bulletins cannot be moved to that folder, because a newer version of this bulletin already exists.",
"field:notifyDropErrorscause=An unexpected error occurred while moving the bulletin(s). One or more files may be damaged.",
"field:notifyPasteErrorBulletinAlreadyExistscause=One or more bulletins cannot be pasted in that folder, because they already exist in this folder.",
"field:notifyPasteErrorBulletinOldercause=One or more bulletins cannot be pasted in that folder, because a newer version of this bulletin already exists.",
"field:notifyPasteErrorcause=An unexpected error occurred while pasting the bulletin(s). One or more files may be damaged.",
"field:notifyretrieveworkedcause=All of the selected bulletins were successfully retrieved from the server",
"field:notifyretrievefailedcause=Error: Unable to retrieve bulletins from the server",
"field:notifyretrievenothingcause=No bulletins were selected",
"field:notifyDeleteServerDraftsWorkedcause=All of the selected draft bulletins have been deleted from the server.",
"field:notifyDeleteServerDraftsFailedcause=Error: Unable to delete all of those draft bulletins from the server. Some of them may have been deleted.",
"field:notifyDeleteServerDraftsNonecause=No bulletins were selected",
"field:notifyretrievenoservercause=The current server is not responding or may need to be configured from the server menu.",
"field:notifypasswordsdontmatchcause=You must enter the same password twice",
"field:notifyusernamessdontmatchcause=You must enter the same username twice",
"field:notifyUserNameBlankcause=User Name must not be blank",
"field:notifyPasswordInvalidcause=Not a valid password, passwords must be at least 8 characters long",
"field:notifyPasswordMatchesUserNamecause=Your password can not be your username",
"field:notifyincorrectsignincause=Username or Password incorrect",
"field:notifyuploadrejectedcause=The current Martus Server has refused to accept a bulletin",
"field:notifycorruptconfiginfocause=The configuration file may be corrupted",
"field:notifyserverokcause=The Server has been selected.",
"field:notifymagicwordokcause=The Server has accepted your request for permission to upload bulletins.",
"field:notifymagicwordrejectedcause=The Server has rejected your request. The magic word is probably not correct.",
"field:notifyRewriteKeyPairFailedcause=An error occurred.  Unable to change user name or password.  You may need to restore your backup key file.",
"field:notifyRewriteKeyPairSavedcause=Successfully saved your new username and password.",
"field:notifyUnableToSaveAttachmentcause=Unable to save the selected attachment for some reason.  Try saving it to a different file.",
"field:notifyUnableToViewAttachmentcause=Unable to view the selected attachment for some reason.",
"field:notifySearchFailedcause=Sorry, no bulletins were found.",
"field:notifyErrorDuringExitcause=An error has occurred, which may have prevented Martus from saving some configuration information. You may see a warning next time you log in.",

"field:notifyServerErrorcause=Server Error, the server may be down, please try again later",
"field:notifyFoundOrphanscause=One or more bulletins were not in any folder. These lost bulletins have been placed into the Recovered Bulletins folder.",
"field:notifyFoundDamagedBulletinscause=One or more bulletins were severely damaged, and cannot be displayed. If these bulletins were backed up to a server, you may be able to retrieve undamaged copies from there.",
"field:notifyErrorSavingStatecause=Unable to save current screen layout.",
"field:notifyExportMyPublicKeycause=The following file has been exported: #Filename#",
"field:notifyPublicInfoFileErrorcause=The file does not contain valid public information.",
"field:notifyAccountCodeWrongcause=The Public Code does not match the one you entered.",
"field:notifyErrorSavingConfigcause=Unable to save configuration file.",
"field:notifyErrorSavingFilecause=Unable to save the file. This could be because the destination is readonly or full, or because of a hardware error.",
"field:notifyErrorBackingupKeyPair=Unable to verify the backup. Please try again, possibly to a different destination.",
"field:notifyAuthenticateServerFailedcause=Martus could not authenticate the server. The server may have been compromised.  Please verify your server configuration and contact the server operator.",
"field:notifyWelcomeToMartuscause=Welcome!\n\nTo start using Martus, create an account. Choose any username you like and a password that contains at least 8 characters. Keep the following information in mind as you create your username and password:\n\n * We recommend using a password that has 15 characters.\n * Include some characters that aren't letters or numbers in your password.\n   (That is, use punctuation or other special characters.)\n * Use a password that other people won't be able to guess;\n   anyone who knows your username and password can view your private data,\n   and can create bulletins that everyone will believe were created by you.\n * Write down your username and password and store them in a safe place.\n * You'll need to enter your username and password whenever you start Martus.\n * Martus will also sometimes prompt you to re-enter your username\n   and password to prevent someone else from using your account\n   after you've logged in.\n\nIMPORTANT: You are the only one who has your username and password. If you lose either of them, you won't be able to retrieve your data. Your username and password are not stored or backed up anywhere, so they cannot be reset for you.",
"field:notifyUnexpectedErrorcause=An unexpected error has occurred. Please report this problem to help@martus.org.",
"field:notifyInvalidServerNamecause=You must have a server name or IP address.",
"field:notifyInvalidServerCodecause=You must have a server public code.",
"field:notifyServerInfoInvalidcause=The Server has responded with invalid account information.",
"field:notifyConfigNoServercause=The selected server is not responding. Before you choose a server, you must be connected to the internet, and that server must be available.",
"field:notifyServerCodeWrongcause=The Server Public Code does not match the one you entered.",
"field:notifyRetrieveCanceledcause=Bulletin download cancelled.  Some bulletins may have already been downloaded.",
"field:notifyRememberPasswordcause=Please remember your username and password. It cannot be recovered.",
"field:notifyDamagedBulletinMovedToDiscardedcause=An error occurred during upload, and the damaged bulletin:'#BulletinTitle#' has been moved to the Damaged Bulletins folder.",
"field:notifyUploadFailedBulletinNotSentToServercause=An error occurred during upload, and the bulletin:'#BulletinTitle#' has not been sent to the server.  You may try and resend the bulletin later.",
"field:notifyPreviewOneBulletinOnlycause=You may only preview one bulletin at a time.  Please only select one bulletin to preview.",
"field:notifyPreviewNoBulletinsSelectedcause=No bulletin selected.  Please select the bulletin you wish to preview.",
"field:notifyRetrievePreviewNotAvailableYetcause=Preview not yet available for this bulletin. Please wait until the information has been retrieved from the server, and then try again.",
"field:notifyRetrievedOnlySomeSummariescause=Errors occurred while retrieving bulletin summaries.  Some of the bulletins on the server will not be shown.",
"field:notifyConfirmCorrectDefaultDetailsDatacause=Please confirm that the default details retrieved are correct.",
"field:notifyExportCompletecause=Successfully exported #BulletinsExported# of #TotalBulletinsToExport# bulletins.",
"field:notifyExportCompleteMissingAttachmentscause=Exported #BulletinsExported# of #TotalBulletinsToExport# bulletins.  Unfortunately #AttachmentsNotExported# attachments were not exported due to errors.",

"field:notifyErrorWritingFilecause=An error prevented the file from being written. Check to make sure the disk is not full or write protected.",
"field:notifyErrorReadingFilecause=An error prevented the file from being read.",
"field:notifyExportZeroBulletinscause=To export bulletins, select them first, and then perform the export operation.",
"field:notifyAddPermissionsZeroHeadquartersSelectedcause=Must select at least one headquarters account",
"field:notifyPrintZeroBulletinscause=To print one or more bulletins, select them first, and then perform the print operation.",

"field:notifyUserRejectedServerCompliancecause=You have chosen not to use this server",
"field:notifyExistingServerRemovedcause=You will have to select a server for any bulletins to be backed up to that server, or to retrieve bulletins from that server.",
"field:notifyErrorSavingBulletincause=An error prevented the bulletin from being saved. Check to make sure the disk is not full or write protected.",		
"field:notifyExportFolderEmptycause=The folder you are trying to export does not contain any bulletins.  Select a folder which has bulletins before exporting the folder.",		
"field:notifyErrorBackingUpKeySharecause=An unexpected error occurred in generating the key backup files.",
"field:notifyRecoveryProcessKeySharecause=You will now have to provide #MinimumNumberOfFilesNeededForRecovery# out of the #TotalNumberOfFilesInBackup# files you previously saved when you backed up your key.",
"field:notifyRecoveredKeyShareSucceededNewUserNamePasswordRequiredcause=Key restore succeeded!  You now have to enter a user name and password.",
"field:notifyRecoveryOfKeyShareCompletecause=You have successfully restored your key from your backup files.  It is very important that you now re-distribute your backup files.  We also recommend you backup your key as a single password-encrypted file, though you do not need to recreate the multiple file backup.",
"field:notifyVerifyKeyPairSharePassedcause=Verification of all disks passed.",
"field:notifyOperationCompletedcause=Operation completed.",
"field:notifycontactRejectedcause=The current Martus Server has refused to accept your contact info",
"field:notifyCreateAccountFailedcause=An error occurred while trying to create your account.",
"field:notifyUserAlreadyExistscause=The user name you chose already exists on this system, please choose a different user name.",
"field:notifyRecoveryProcessBackupFilecause=To restore your key, first you must find the backup file you saved previously.  Using the 'Restore Key from Backup File' dialog box find the file on your computer's hard drive, a network drive, or removable media disk then click the Open button.  Martus will then ask you for your user name and password and restore your key so you can log in normally and access your account.",
"field:notifyRecoveryOfKeyPairCompletecause=Key restore is complete, you will now be logged into Martus.",
"field:notifyErrorRecoveringAccountDirectorycause=Error during key restore. This key backup copy may be damaged.",
"field:notifyServerSSLNotRespondingcause=Unable to make a secure connection with the Martus backup server.\n\nMartus can connect to a server using either of two ports.  When trying to connect to this server, neither port is accessible, probably because both ports are being blocked by a local firewall or by your ISP.\n\nWe recommend that any firewalls be configured to allow outgoing connections on both TCP port 443 and 987.  Please contact your LAN administrator or office technical support staff to verify that your firewall configuration allows this.\n\nIf the problem persists, you may need to select a different server.",
"field:notifyAlreadyRunningcause=A copy of Martus is already running on this computer. You must close that copy before starting a new copy.",
"field:notifyAttachmentNotAFilecause=The item you chose was not a file, please choose a file to add as an attachment to this bulletin.",
"field:notifyFilesWillNotBeDeletedcause=The original files have not been deleted from your computer, you must delete them manually if you wish.",
"field:notifyHQKeyAlradyExistscause=You have already added this account to your Headquarters list.",
"field:notifyFieldDeskKeyAlreadyExistscause=You have already added this account as a Field Desk.",
"field:notifyFolderMigrationFailedcause=An error occurred during the conversion of your folders. You can still use Martus, but if the Outbox or Drafts folders still exist, do not use them.",
"field:notifyResendErrorNotAuthorizedToSendcause=One or more bulletins were unable to be resent because you are not authorized to upload them.",
"field:notifyResendErrorcause=One or more bulletins were unable to be resent due to an unexpected error.",
"field:notifyErrorRenameFoldercause=Folder names cannot contain punctuation.  They also cannot begin with a space.",
"field:notifyErrorRenameFolderExistscause=You already have a folder with that name.",
"field:notifyAlreadyViewingThisVersioncause=You currently are viewing this version of the bulletin.",
"field:notifyBulletinVersionNotInSystemcause=The bulletin version you are trying to view is currently not on your computer.",
"field:notifyHeadquarterLabelDuplicatecause=The Headquarters label you entered is already assigned to another Headquarters account.  Please use a different label.",
"field:notifyFieldDeskLabelDuplicatecause=The label you entered is already assigned to another Field Desk account.  Please use a different label.",
"field:notifyNoHQsSelectedcause=Please select a Headquarters first.",
"field:notifyNoFieldDesksSelectedcause=Please select a Field Desk first.",
"field:notifyErrorExportingCustomizationTemplatecause=There was an error saving your template.",
"field:notifyErrorImportingCustomizationTemplatecause=There was an error importing this template.",
"field:notifyErrorImportingCustomizationTemplateFuturecause=The customization template you are trying to import was created by a newer version of Martus.  You need to upgrade to the latest version of Martus before you can import this template.",
"field:notifyImportingCustomizationTemplateSuccesscause=Successfully imported customization template.",
"field:notifyExportingCustomizationTemplateSuccesscause=Successfully exported customization template.",
"field:notifyCreatingFieldSpecCachecause=Martus needs to keep a list of all the fields in all the bulletins in your system.\n\nThis list does not exist, so it will be created now. This may take a few seconds per bulletin on a slower computer.",
"field:notifyRetrieveErrorcause=An error has occurred while retrieving a bulletin.",
"field:notifyRetrieveInProgresscause=A retrieve is already in progress. You cannot start another retrieve until that one has finished.",
"field:notifyRetrieveFileDataVersionErrorcause=A retrieve was in progress when you upgraded to a newer version of Martus. That retrieve will be canceled, so you should re-select any bulletins that had not yet been retrieved.",
"field:notifyRetrieveFileErrorcause=An error has prevented Martus from continuing the retrieve that was in progress. You should re-select any bulletins that had not yet been retrieved.",
"field:notifyNoGridRowSelectedcause=No row selected.",
"field:notifyNonStringFieldRowSelectedcause=Load Values is only available for String and Data-driven Dropdown fields",
"field:notifyImportCompletecause=Import complete.  Imported #BulletinsSuccessfullyImported# of #TotalBulletinsToImport# bulletins into folder: #ImportFolder#",
"field:notifyErrorImportingBulletinscause=There was an error importing bulletins into Martus.  Not all bulletins were imported.",
"field:notifyErrorExportingBulletinscause=There was an error exporting bulletins from Martus.  Not all bulletins were exported.",
"field:notifyNoImportFileSpecifiedcause=No folder specified.  You must enter a folder you wish the files to be imported into.  This folder can already exist in the system, or can be a new folder.",
"field:notifyImportMissingAttachmentscause=Not all attachments were imported.  The following bulletins had problems importing these attachments.\n\n#ImportMissingAttachments#",
"field:notifyImportBulletinsNotImportedcause=Not all bulletins were imported.  The following bulletins had problems during import.\n\n#ImportBulletinsNotImported#",
"field:notifyNoReportFieldsSelectedcause=You must select at least one field to be included in the report.",
"field:notifyNotValidReportFormatcause=This is not a valid Report Format file",
"field:notifyReportFormatIsOldcause=This report format was created with an earlier version of Martus, so it may not work correctly",
"field:notifyReportFormatIsTooNewcause=This report format was created with a newer version of Martus, so it may not work correctly",
"field:notifyReportFormatDifferentLanguagecause=This report format was created in a different language. As a result, some headings may not print properly, and some of the fields may not be available for sorting.",
"field:notifyViewAttachmentNotAvailablecause=This computer can only view JPEG, GIF, and PNG image attachments",
"field:notifyAddPermissionsZeroBulletinsOurscause=Must select at least one bulletin that was created by this account",
"field:notifySealSelectedZeroBulletinsOurscause=Must select at least one bulletin that was created by this account",

"field:notifyErrorImportingBulletinsTooOldcause=This XML file was created by an older version of Martus and cannot be read by this version.",
"field:notifyErrorImportingBulletinsTooNewcause=This XML file was created by a newer version of Martus and cannot be read by this version.  You must upgrade to the newer version of Martus to import this file.",
"field:notifyErrorSavingDictionarycause=Unknown error saving user dictionary",
"field:notifyErrorLoadingDictionarycause=Unknown error loading user dictionary for spell checking",
"field:notifyErrorUpdatingDictionarycause=Unable to update the dictionary. Be sure the word list is in the correct format.",
"field:SpellCheckUserDictionaryInstructions=The following words have been added to the user dictionary. \nYou can delete or edit them here, or you can add more words. \nEach word must be on a line by itself.",

"field:IncompatibleMtfVersion=The version of this translation is not compatible with this version of Martus.  It is recommended that you do not continue to use this translation version and go to (http://www.martus.org/downloads) for the appropriate version.",
"field:IncompatibleMtfVersionTranslation=#MtfLanguage# Translation Version: #MtfVersionNumber#",
"field:IncompatibleMtfVersionProgram=Martus Software Version: #ProgramVersionNumber#",

"field:UnverifiedFDAttachment=The account that created this bulletin has not been verified, so there is an increased chance that this attachment could be dangerous. We recommend that you verify this Field Desk account before you #action# this attachment (See Tools > Configure Field Desks). Opening this attachment could put your computer at risk. ",
"field:SaveAttachmentAction=save",
"field:ViewAttachmentAction=view",
"field:messageServerNewscause=The current server has sent this message:",
"field:messageErrorDateInFuturecause=This date occurs in the future:",
"field:messageErrorDateRangeInvertedcause=This date range has an end date that is earlier than the start date: #FieldLabel#",
"field:messageErrorDateTooEarlycause=Dates entered in '#FieldLabel#' cannot be earlier than #MinimumDate#",
"field:messageErrorDateTooLatecause=Dates entered in '#FieldLabel#' cannot be later than #MaximumDate#",
"field:messageErrorAttachmentMissingcause=The attachment could not be located.",
"field:messageErrorRequiredFieldBlankcause=This field is required and cannot be left blank: #FieldLabel#",

"field:CreateCustomFieldsHelp1=The layout of the bulletins is dictated by an XML document.  " +
	"The document must begin with <CustomFields> and end with </CustomFields>. " +
	"By default the standard bulletin fields occur at the top of the Custom Field declaration, " +
	"but they can be moved if desired.  However, there are four required fields that cannot be removed:  " +
	"'author', 'entrydate', 'language' and 'title'.\n\n",
"field:CreateCustomFieldsHelp2=For custom (non-standard) fields, " +
	"you first select the type of field you want.  " +
	"The possible choices are 'BOOLEAN', 'DATE', 'DATERANGE', 'DROPDOWN', " +
	"'GRID', 'LANGUAGE', 'MESSAGE', 'MULTILINE', 'STRING', and 'SECTION'. " +
	"\n\n" +
	"For each custom field you will need a unique identification tag.  " +
	"This tag can be any word except those already used by the system " +
	"(eg. 'author', 'summary', 'location', 'title' etc.), " +
	"and cannot contain spaces or special characters.  " +
	"Examples of choices are 'VictimsName', 'EyeColorChoice', etc." +
	"\n\n" +
	"Then you need a label which is displayed next to your custom field.  " +
	"An example might be 'Name of 1st Witness'." +
	"\n\n" +
	"You can create sections (which you can hide/unhide) in your bulletins " +
	"using a SECTION field type.  " +
	"\n\n" +
	"You can put multiple fields on a single row in your bulletin by using " +
	"<KeepWithPrevious/> in the field definition." +
	"\n\n" +
	"You can require certain fields or grid columns to be entered before " +
	"saving a bulletin by using </RequiredField> in the field definition." +
	"\n\n" +
	"You can restrict standard or custom date fields, date grid columns, date range fields, " +
	"and date range grid columns by using <MinimumDate> and/or <MaximumDate> " +
	"tags with a date in YYYY-MM-DD format. " +
	"NOTE: The year must always be a 'Gregorian' year like 2009, " +
	"even if Martus is configured to use Thai or Afghan or Persian dates. " +
	"A blank date, shown as <MaximumDate/>, means 'today', " +
	"although it may allow one day earlier or later, " +
	"due to time zone issues. " +
	"\n\n" +
	"You can populate drop-down lists (either inside or outside of a grid) " +
	"in three ways - " +
	"1) by entering a list of <Choices> values in the field definition, " +
	"2) with values that have been entered in a grid elsewhere " +
	"in your bulletin by using <DataSource> (sometimes called \"data-driven dropdowns\"), and " +
	"3) by creating a list of \"Reusable Choices\" that can be referred to " +
	"by more than one field.  " +
	"See examples below for the correct XML definition syntax to use." +
	"\n\n" +
	"You can set a default value for text and dropdown list fields by using " +
	"<DefaultValue>ddd</DefaultValue>. " +
	"For dropdowns, you must use a value already in the list of choices you defined. " +
	"For Reusable Choices dropdowns it can be a partial or complete code, with each level separated by dots " +
	"(for a location dropdown that has both Region and City levels, " +
	"you could pick the default to be at either level, e.g. either R1 or R1.C1, see example below). " +
	"Default values can be set for both standard and custom fields, " +
	"but are NOT allowed for BOOLEAN, DATE, DATERANGE, GRID, LANGUAGE, MESSAGE, and SECTION field types, " +
	"and are not allowed for dropdowns where the values in the list are based on data entered in another field." +
	"(i.e. data-driven dropdowns).  " +
	"Please note that default values are only applied when a new bulletin is created, " +
	"not when a new version of a bulletin is created, " +
	"so that the value of the field in the previous version is not overwritten.  " +
	"This means that default values entered in a bulletin using an earlier customization will be kept " +
	"even if you create a new version of the bulletin with an updated customization that has a new default value" +
	"\n\n " +
	"Additional Comments" +
	"\n\n" +
	"1. XML is case-sensitive ('Witness' is not the same as 'witness')" +
	"\n\n" + 
	"2. Quotes around type name can be single or double as long as they match " +
	"(e.g. 'STRING\" is not valid. It must be 'STRING' or \"STRING\")" +
	"\n\n" +
	"3. A Boolean field will be displayed as a checkbox when editing and Yes/No when previewed or printed." +
	"\n\n" +
	"4.  Use \"MESSAGE\" fields to give guidance on how to enter data, and to create comments/notes " +
	"that will be displayed in every bulletin." +
	"\n\n" +
	"5.  Dropdowns using a \"Reusable Choices\" list can have multiple levels " +
	"(e.g. for locations that might have Region and City), different fields can use one or more of the levels, " +
	"and the number of levels is not limited." +
	"\n\n" +
	"6. Both STRING and MULTILINE fields are text fields.  " +
	"STRING fields will expand to fit the size of the text you enter, " +
	"while MULTILINE fields have a scrollbar so that the field doesn't exceed its original height." +
	"\n\n" +
	"7.  A GRID can contain columns of various types:  BOOLEAN, DATE, DATERANGE, DROPDOWN, and STRING. " +
	"When you're entering data into a grid, press Enter to create a new line, press Tab to advance to the next cell, " +
	"and double-click to copy and paste text." +
	"\n\n" +
	"8.  You can update your customization if the information you are collecting over time changes, " +
	"for example by adding new fields.  If you are changing fields in a customization, " +
	"you should think about how you will want to search/report on bulletins created with the old customization " +
	"as well as new bulletins you create with the new customization, " +
	"and try to make the changes so that you can search/report on all bulletins at the same time.  " +
	"Changing field types may cause your searching/reporting to be more complex, " +
	"so we always recommend that you test out creating bulletins with a new customization and searching/reporting " +
	"on both old and new bulletins before officially updating the customization for your project.  " +
	"When you change customizations, Martus will do its best to update the old formatted data to the new customization " +
	"if you create a new version of a bulletin with the old customization.  " +
	"If you add completely new fields but don't change any of the old fields, " +
	"the new version of the bulletin will have all the old fields filled in as they were in the previous version, " +
	"and the new fields will be blank and you can fill them in.  If you delete fields, " +
	"the new version of the bulletin will not contain those fields, but you can go back to see the deleted fields " +
	"in the previous version if it was a sealed bulletin instead of a draft " +
	"(by hitting the Bulletin Details button in the Header section of the bulletin), " +
	"and copy any info from the old version into a different field in the new one if desired.  " +
	"You have to be very careful if you make changes to the customization definitions of old fields.  " +
	"If you keep the same tag and label, but change the type of field, " +
	"Martus may be able to transfer the old data into the new field type, but not in all cases.  " +
	"For example, if you change field type from DROPDOWN to STRING, the data will be transferred over, " +
	"but if you change from STRING to BOOLEAN, the data will be lost. If you change from a DATE to DATERANGE, " +
	"your data will be transferred, but if you change from DATERANGE to DATE, you will lose the end date from your earlier data. " +
	"DROPDOWN and GRID fields are subject to additional rules regarding the modification of dropdown options/values and grid columns. " +
	"If you add options/values to a dropdown list, the old data will be transferred over. " +
	"But if you modify or delete an option/value, all bulletins for which that option was selected will lose that data. " +
	"If you want to add columns to a grid, please make sure to do so at the end of the old grid definition and not in the middle, " +
	"or the old data will not be transferred to the new version of the bulletin. " +
	"If you change fields and lose old data in the new version as a result, " +
	"please note that you can go back to see the deleted data in the previous version " +
	"if it was a sealed bulletin instead of a draft (by hitting the Bulletin Details button in the Header section of the bulletin), " +
	"and copy any info from the old version into a different field in the new one if desired. " +
	"In this release, if you want to change the number of levels in a \"Reusable Choices\" dropdown field when updating a customization, " +
	"you should also change the field tag and/or label so that they are more easily distinguished from each other in searching and reporting.  " +
	"If you do not, the search/report results may be confusing since fields with the same label/tag will be treated differently " +
	"due to them having a different number of levels." +
	"\n\n" +
	"See examples below and if you need additional help with customization, " +
	"please email help@martus.org." +
	"\n\n",
"field:CreateCustomFieldsHelp2b= Notes on \"Reusable Choices\" dropdown fields:" +
	"\n\n " +
	"1. Dropdowns using a \"Reusable Choices\" list can have multiple levels (e.g. for locations that might have Region and City), " +
	"different fields can use one or more of the levels (e.g. if you have defined Region and City levels, " +
	"you could have a field that just uses the Region level, and another field that uses both levels), " +
	"and you do not need to define all levels for all entries " +
	"(e.g. you could have defined Neighborhood values as a lower level for some larger Cities, " +
	"but not all Cities need to have Neighborhoods defined).  " +
	"The number of levels is not limited by Martus, but please note that if you have large amounts of data in your definitions lists, " +
	"or a large number of levels, performance of certain Martus bulletin operations may be affected." +
	"\n\n " +
	"2. In this release, we recommend that if you want to change the number of levels in a \"Reusable Choices\" dropdown field " +
	"when updating a customization, you also change the field tag and/or label so that they are more easily distinguished " +
	"from each other in searching and reporting.  If you do not, the search/report results may be confusing since fields " +
	"with the same label/tag will be treated differently due to them having a different number of levels." +
	"\n\n " +
	"3. \"Reusable Choices\" codes have the same restrictions that field tags do; they can be in any language, " +
	"but cannot contain spaces, special characters, or punctuation." +
	"\n\n " +
	"4. Please make sure to not use the same codes in Reusable Choices lists if you edit your customizations " +
	"unless you are just fixing typos in the labels, because using the same code for different labels can cause confusion " +
	"when searching or reporting on those fields. Ideally you should use codes that are not numeric, " +
	"but are letters that are a meaningful abbreviation of the label so there is no confusion over what they stand for " +
	"if you update the customization at a later date (e.g. use 2 or 3 letter abbreviations for locations instead of numbers).  " +
	"See the \"Search\" Help screen for more information about how code and label choices can affect searching on these fields." +
	"\n\n " +
	"5. You cannot use a multiple level / reusable choice dropdown as a data source for another dropdown inside or outside of grids. " +
	"If you try to do this you will see an error message." +
	"\n\n " +
	"6. Please note that when you save customization XML with a Reusable Choices list for dropdown fields, " +
	"Martus will move those choice definitions to the bottom of the XML when you reload it." +
	"\n\n",
"field:CreateCustomFieldsHelp3=\n" +
	"<Field type='SECTION'>\n" +
	"<Tag>summarysection</Tag>\n" +
	"<Label>Summary Section</Label>\n" +
	"</Field>\n" +
	"\n" +
	"<Field type='STRING'>\n" +
	"<Tag>office</Tag>\n" +
	"<Label>Regional office collecting the data</Label>\n" +
	"<DefaultValue>Region 3 field office</DefaultValue>\n" +
	"</Field>\n" +
	"\n" +
	"<Field type='DROPDOWN'>\n" +
	"<Tag>BulletinSource</Tag>\n" +
	"<Label>Source of bulletin information</Label>\n" +
	"<RequiredField/>\n" +
	"<DefaultValue>Media/Press</DefaultValue>\n" +
	"<Choices>\n" +
	"<Choice>Media/Press</Choice>\n" +
	"<Choice>Legal Report</Choice>\n" +
	"<Choice>Personal Interview</Choice>\n" +
	"<Choice>Other</Choice>\n" +
	"</Choices>\n" +
	"</Field>\n\n<Field type='STRING'>\n" +
	"<Tag>SpecifyOther</Tag>\n" +
	"<Label>If Source = \"Other\", please specify:</Label>\n" +
	"</Field>\n\n<Field type='STRING'>\n" +
	"<Tag>IntervieweeName</Tag>\n" +
	"<Label>Interviewee Name</Label>\n" +
	"</Field>\n" +
	"\n" +
	"<Field type='LANGUAGE'>\n" +
	"<Tag>IntervieweeLanguage</Tag>\n" +
	"<Label>Interviewee Speaks</Label>\n" +
	"</Field>\n" +
	"\n" +
	"<Field type='DATERANGE'>\n" +
	"<Tag>InterviewDates</Tag>\n" +
	"<Label>Date(s) of interview(s)</Label>\n" +
	"</Field>\n" +
	"\n" +
	"<Field type='BOOLEAN'>\n" +
	"<Tag>Anonymous</Tag>\n" +
	"<Label>Does interviewee wish to remain anonymous?</Label>\n" +
	"</Field>\n\n<Field type='BOOLEAN'>\n" +
	"<Tag>AdditionalInfo</Tag>\n" +
	"<Label>Is interviewee willing to give additional information if needed?</Label>\n" +
	"<KeepWithPrevious/>\n" +
	"</Field>\n" +
	"\n" +
	"<Field type='BOOLEAN'>\n" +
	"<Tag>Testify</Tag>\n" +
	"<Label>Is interviewee willing to testify?</Label>\n" +
	"<KeepWithPrevious/>\n" +
	"</Field>\n" +
	"\n" +
	"<Field type='DROPDOWN'>\n" +
	"<Tag>EventLocation</Tag>\n" +
	"<Label>Event Location</Label>\n" +
	"<DefaultValue>R1</DefaultValue>\n" +
	"  <UseReusableChoices code='RegionChoices'></UseReusableChoices>\n" +
	"  <UseReusableChoices code='CityChoices'></UseReusableChoices>\n" +
	"</Field>" +
	"\n" +
	"\n" +
	"<Field type='SECTION'>\n" +
	"<Tag>peoplesection</Tag>\n" +
	"<Label>People Section</Label>\n" +
	"</Field>\n" +
	"\n" +
	"<Field type='GRID'>\n" +
	"<Tag>VictimInformationGrid</Tag>\n" +
	"<Label>Victim Information</Label>\n" +
	"<GridSpecDetails>\n" +
	"<Column type='STRING'><Tag></Tag><Label>First Name</Label></Column>\n" +
	"<Column type='STRING'><Tag></Tag><Label>Last Name</Label></Column>\n" +
	"<Column type='BOOLEAN'><Tag></Tag><Label>Is Identified?</Label></Column>\n" +
	"<Column type='DATE'><Tag></Tag><Label>Date of Birth</Label><MinimumDate>1910-01-01</MinimumDate><MaximumDate/></Column>\n" +
	"<Column type='DROPDOWN'><Tag></Tag><Label>Sex</Label><RequiredField/>\n" +
	"<Choices>\n<Choice>Male</Choice>\n" +
	"<Choice>Female</Choice>\n" +
	"<Choice>Unknown</Choice>\n" +
	"</Choices></Column>\n" +
	"<Column type='DROPDOWN'><Tag></Tag><Label>Region of Birth</Label>\n" +
	"  <UseReusableChoices code='RegionChoices'></UseReusableChoices></Column>\n" +
	"<Column type='STRING'><Tag></Tag><Label>Ethnicity</Label></Column>\n" +
	"</GridSpecDetails>\n</Field>\n\n<Field type='MESSAGE'>\n" +
	"<Tag>MessageProfession</Tag>\n" +
	"<Label>Profession History Table Note</Label>\n" +
	"<Message>If you have information about a person who has had different professions over time, " +
	"enter multiple rows with the same First and Last Names and show the date ranges " +
	"for each profession on a separate row.</Message>\n" +
	"</Field>\n" +
	"\n" +
	"<Field type='GRID'>\n" +
	"<Tag>ProfessionHistoryGrid</Tag>\n" +
	"<Label>Profession History</Label>\n" +
	"<GridSpecDetails>\n" +
	"<Column type='DROPDOWN'><Tag></Tag><Label>First Name</Label>\n" +
	" <DataSource>\n" +
	" <GridFieldTag>VictimInformationGrid</GridFieldTag>\n" +
	" <GridColumnLabel>First Name</GridColumnLabel>\n" +
	" </DataSource>\n" +
	"</Column>\n" +
	"<Column type='DROPDOWN'><Tag></Tag><Label>Last Name</Label>\n" +
	" <DataSource>\n <GridFieldTag>VictimInformationGrid</GridFieldTag>\n" +
	" <GridColumnLabel>Last Name</GridColumnLabel>\n" +
	" </DataSource>\n" +
	"</Column>\n" +
	"<Column type='STRING'><Tag></Tag><Label>Profession</Label></Column>\n" +
	"<Column type='DATERANGE'><Tag></Tag><Label>Dates of Profession</Label><MaximumDate/></Column>\n" +
	"</GridSpecDetails>\n" +
	"</Field>\n" +
	"\n" +
	"<Field type='MULTILINE'>\n" +
	"<Tag>narrative</Tag>\n" + 
	"<Label>Narrative description of events</Label>\n" + 
	"<DefaultValue>What happened in detail is as follows:</DefaultValue>\n" + 
	"</Field>\n" +
	"\n" +
	"\n" +
	"<ReusableChoices code='RegionChoices' label='Region'>\n" +
	"  <Choice code='R1' label='Region 1'></Choice>\n" +
	"  <Choice code='R2' label='Region 2'></Choice>\n" +
	"  <Choice code='R3' label='Region 3'></Choice>\n" +
	"</ReusableChoices>\n" +
	"\n" +
	"<ReusableChoices code='CityChoices' label='City'>\n" +
	"  <Choice code='R1.C1' label='City 1'></Choice>\n" +
	"  <Choice code='R1.C2' label='City 2'></Choice>\n" +
	"  <Choice code='R2.C3' label='City 3'></Choice>\n" +
	"  <Choice code='R2.C4' label='City 4'></Choice>\n" +
	"  <Choice code='R3.C5' label='City 5'></Choice>\n" +
	"  <Choice code='R3.C6' label='City 6'></Choice>\n" +
	"</ReusableChoices>",

"field:inputservermagicwordentry=If you want to request permission to upload to this server, enter the 'magic word' now:",
"field:inputImportPublicCodeentry=Enter the Public Code for the account being imported:",
"field:inputImportPublicKeyentry=Enter the Public Code for this account:",
"field:inputGetShareFileNameentry=Enter the name of the file you wish to export.",
"field:inputCustomFieldsInfo=Enter the XML representation for the layout of new and modified bulletins.",
"field:inputGetHQLabelentry=Enter the label you wish to associate with this Headquarters.",
"field:inputGetFieldDeskLabelentry=Enter the label you wish to associate with this Field Desk.",

"field:warningDeleteSingleBulletin=You have chosen to permanently delete a bulletin from the Discarded Bulletins folder. Even if this bulletin was recently cut or copied, you will not be able to paste it. If this bulletin has already been sent to a server, it will remain on the server. This action will only delete it from this computer.",
"field:warningDeleteMultipleBulletins=You have chosen to permanently delete bulletins from the Discarded Bulletins folder. Even if these bulletins were recently cut or copied, you will not be able to paste them. If they have already been sent to a server, they will remain on the server. This action will only delete them from this computer.",
"field:warningDeleteSingleUnsentBulletin=WARNING: This bulletin has not been sent to a server since it was last modified. Deleting it will prevent the latest changes from being backed up.",
"field:warningDeleteMultipleUnsentBulletins=WARNING: One or more of these bulletins have not been sent to a server since they were last modified. Deleting them will prevent the latest changes from being backed up.",
"field:warningDeleteSingleBulletinWithCopies=NOTE: A copy of this bulletin exists in one or more other folders, and those copies will not be removed.",
"field:warningDeleteMultipleBulletinsWithCopies=NOTE: Copies of one or more of these bulletins exist in one or more other folders. Those copies will not be removed.",
"field:warningUnofficialTranslation=WARNING:  This translation was not created by the Martus team, and therefore may not be trustworthy.  If you only want to use official translations then exit Martus, remove the file #UseUnofficialTranslationFiles# from your Martus directory, and restart Martus.",

"field:username=Username",
"field:password1=Password",
"field:password2=(same password again)",
"field:email=Email Address",
"field:webpage=Web Page",
"field:phone=Phone Number",
"field:address=Mailing Address",
"field:_AttachmentsTopSection=Attachments",
"field:_AttachmentsBottomSection=Attachments",
"field:username=Username",
"field:password=Password",
"field:securityServerConfigValidate=For security reasons, we must validate your username and password.",
"field:RetypeUserNameAndPassword=Please retype your username and password for verification.",
"field:CreateNewUserNamePassword=Please enter your new username and password.",
"field:HelpOnCreatingNewPassword=When choosing a password, it is important not to use a password that is easy to guess like names, important dates of events or simple words.  Try adding numbers to random letters and making the password long.  Remember your password, but do not share it.  No one else has access to the password if you forget it, so if you write it down, put it in a safe place.",
"field:timedout1=Martus is still running in the background, but has locked the screen for security.  You must sign in to Martus again to continue working.",
"field:timedout2=Any unsaved bulletins will be lost unless you sign in to Martus again and save them.",
"field:defaultFolderName=New Folder",
"field:SearchBulletinRules=For each row, select a field to search (or choose to match any field), select what kind of comparison to perform, and then enter a value to search for.\n\nNOTE: Bulletins that appear only in the Discarded folder will not be searched.",
"field:SearchBulletinAddingRules=Press Enter to create a new row in the search query.",
"field:SearchBulletinHelp=Click '#SearchHelpButton#' to get additional information on searching.",
"field:AccountInfoUserName=User Name: ",
"field:AccountInfoPublicKey=Public Account Id:",
"field:AccountInfoPublicCode=Public Code:",
"field:AccountInfoDirectory=Account Directory: ",
"field:NameOfExportedFile=Please enter a name for the file you wish to export.",
"field:ServerNameEntry=Server name or IP address:",
"field:ServerPublicCodeEntry=Server Public Identification Code:",
"field:ServerMagicWordEntry=Server Magic Word:",
"field:ServerSelectionResults=The following server has been selected:",
"field:ServerAcceptsUploads=You will be allowed to upload bulletins to this server.",
"field:ServerDeclinesUploads=You will not be allowed to upload bulletins to this server.",
"field:SearchEntry=Search for:",
"field:ExportPrivateData=Include private data in export",
"field:ExportAttachments=Include attachments in export",
"field:ExportAllVersions=Include all versions of each bulletin in export",
"field:ExportBulletinDetails=Information from the selected bulletins (listed below) will be exported to an XML file that you specify.  These files can then be used to import the Martus bulletin information into another application.\n\n  You could also use this XML file to import these bulletins back into Martus (as new unique bulletins) if desired.  Please email help@martus.org if you need assistance with this type of importing.",
"field:HowToCreateNewAccount=One or more accounts already exist on this computer. To create an additional account with a new username and passphrase, click OK.",
"field:HowToCreateInitialAccount=No accounts exist on this computer.  To create a new account click OK.  Otherwise you can click on the Restore Account tab to restore an account which was previously backed up.",
"field:UntitledBulletin=Untitled Bulletin",
"field:GetShareFileNameDescription=This file name should be unique and identifiable to you but we recommend not using your user name.  Each file will be generated with this name and its number sequence.",
"field:UnknownFieldType=WARNING: This field requires a newer release of Martus to be viewed",
"field:BackupSecretShareCompleteInformation=Backup complete, please give each disk to someone you trust, so that if you forget your user name and/or password in the future you can recreate your account.  You will need #MinimumNumberOfFilesNeededForRecovery# of these disks to recreate your account.",
"field:BackupRecoverKeyPairInsertNextDiskMessage=Please insert disk #",
"field:DeleteKeypair=Delete key File",
"field:ScrubDataBeforeDelete=Scrub data before deleting",
"field:ExitWhenComplete=Exit when complete",
"field:DonotPrompt=Do not prompt",
"field:UninstallMartus=Uninstall Martus",
"field:RemoveServerLabel1=The following server will be removed:",
"field:RemoveServerLabel2=Are you sure you want to do this?",
"field:ExportedBulletins=Exported Bulletins",
"field:ErrorCustomFields=There is an error in the custom field definitions. " +
	"Each problem is identified with the following codes:\n" +
	"   100 - Required standard field is missing\n" +
	"   101 - Custom field does not have a correct tag\n" +
	"   102 - A previous Tag already exists with this name\n" +
	"   103 - Label cannot be blank\n" +
	"   104 - Unrecognized Field Type\n" +
	"   105 - A label was found in a standard field\n" +
	"   106 - XML Parse Error (e.g. mismatched quotation marks or bracket syntax)\n" +
	"   107 - Tag or Code contains illegal characters (e.g. spaces or punctuation)\n" +
	"   108 - Duplicate entry in Dropdown field\n" +
	"   109 - No Choices in Dropdown field\n" +
	"   110 - System/Standard field tag cannot be used for custom field\n" +
	"   111 - Standard field in top pane cannot be placed in bottom (always Private) pane\n" +
	"   112 - Standard field in bottom (always Private) pane cannot be placed in the top pane\n" +
	"   113 - Data Source Grid not found in same pane as resulting dropdown field\n" +
	"   114 - Unknown Data Source Grid Column Label\n" +
	"   115 - Dropdown cannot have both choices and data source\n" +
	"   116 - Invalid date must be in ISO format YYYY-MM-DD\n" +
	"   117 - No Reusable Choices list defined for Dropdown\n" +
	"   118 - Reusable dropdown definition missing code value.  Please verify that the definition looks like the following:\n" +
	"         <UseReusableChoices code='FieldChoices'></UseReusableChoices>\n" +
	"   119 - Reusable choice must have a code and label\n" +
	"   120 - Dropdown data source cannot be a single or multiple level dropdown with Reusable Choices\n" +
	"   121 - Default value is not valid for this field\n" +
	"   122 - Reusable Choices lists cannot have the same Label\n" +
	"   123 - Lower-level reusable choice code has no matching higher-level code\n" +
	"   200 - All fields empty\n" +
	"   201 - Unrecognized Headquarters created this template\n" +
	"   202 - Security validation error\n" +
	"   203 - File Error\n" +
	"   204 - Imported XML Missing Field",
"field:ErrorCustomFieldHeader1=CODE",
"field:ErrorCustomFieldHeader2=TYPE",
"field:ErrorCustomFieldHeader3=TAG",
"field:ErrorCustomFieldHeader4=LABEL",
"field:ConfigureHQColumnHeaderPublicCode=Public Code",
"field:SplashProductDescription=Human Rights Bulletin System",
"field:HQsSetAsProxyUploader=These Headquarters accounts are allowed to view the private information in this bulletin, and to send this bulletin to a server on your behalf.",
"field:HQsSetAsDefault=Default Headquarters accounts will be selected for each new bulletin or version.",
"field:DateExact=Exact Date",
"field:DateRange=Date Range",
"field:YearUnspecified=Unknown",
"field:UnknownDateSave=Unknown",
"field:AttachmentSizeUnknown=Unknown",
"field:ConfigureHQsCurrentHQs=Current Headquarters",
"field:ConfigureFDsOverview=These Field Desk accounts are those you trust to send you their private bulletin information and attachments. " +
		"Any Field Desk bulletins you receive from accounts not on this list will be marked as from an unverified account.",
"field:ColumnGridRowNumber=Row #",
"field:SetFolderOrder=To change a folder's position, select the folder name and use the #MoveFolderUp# / #MoveFolderDown# buttons.",
"field:FancySearchHelpMsg1=---Overview---\n\n" +
	"Martus searches all versions of every bulletin (both public and private, sealed and draft) in every folder, including the Discarded Bulletins folder.  " +
		"When the search is completed, the Search Results folder lists copies of the bulletins found in your search.\n\n" +
	"You can search for words in any language.  " +
		"Martus searches are not case-sensitive in English and other purely Latin character languages, " +
		"so it doesn't matter whether a word is capitalized or not.\n\n" +
	"Searches include attachment filenames, but not content of attachments.\n\n" +
	"You can search both standard and custom fields.\n\n",
"field:FancySearchHelpMsg2=---Search tips---\n\n" +
	"Martus will find any text you enter whether it's a complete word or part of a larger word. " +
		"For example, if you search for the word prison, you'll see bulletins that include the words prison, imprison, and imprisonment. " +
		"Likewise, if you search for the word prison, " +
		"Martus will find bulletins that include attachments with names such as photos-prison.jpg and prisoners-report.doc.\n\n" + 
	"To search for an exact phrase, type it with quotation marks around the phrase  (e.g.  \"Witness Testimony\"). " +
		"If you do not put quotation marks around the phrase, Martus will search for the words individually.\n\n" + 
	"Use the word \"#Or#\" to broaden your search, or the word \"#And#\" to narrow it.  " +
		"You can use the #And# / #Or# dropdowns to specify different fields you want search across " +
		"(e.g. you want to search for bulletins that have \"Last Saved Date\" in the last week and have a certain author).  " +
		"If you want to search on multiple text values within a single bulletin field, " +
		"you can use #Or# or #And# in-between words in the \"Search For...\" entry box.  " +
		"For example, if you search \"Any Field\" for:\n" +
		"   prison #Or# jail\n" +
		"you'll see a list of bulletins that contain either word anywhere in the bulletin. " + 
		"If you search for:\n" +
		"   prison #And# assault \n" +
		"you'll see a list of bulletins that contain both words. " +
		"The keyword \"#And#\" is implied, so if you search for:\n" +
		"   prison assault \n" +
		"Martus will find bulletins the same bulletins as if you searched for:\n" +
		"   prison #And# assault\n\n" + 
	"When you use both \"#And#\" and \"#Or#\", your search terms are grouped from the beginning of your list " +
		"(either across bulletin fields or within a particular field). " +
		"For example, if you enter:\n" +
		"   prison #Or# jail #And# trial \n" +
		"in the \"Search For...\" entry box, " +
		"Martus will search for any bulletins that contain either of the words prison or jail, and also contain the word trial. " +   
		"But if you enter:\n" +
		"   prison #And# jail #Or# trial \n" +
		"Martus will search for any bulletins that contain both the words prison and jail, or contain the word trial.\n",
"field:FancySearchHelpMsg3=" + 
	"You can use the word #Or# or #And#, or you can use the English words \"#OrEnglish#\" and \"#AndEnglish#\" to search.\n\n" +  
	"You need to put spaces before and after any #Or# / #And# keywords you use in your search.\n",
"field:FancySearchHelpMsg4=" +
	"If you want all of your grid search criteria to be met in a single bulletin grid row, " +
	"please check the \"Match grid column specifications\" checkbox. " +
	"For example, if you want to search for a specific victim name in a single row in your bulletins created after a certain date, " +
	"select the checkbox and enter the following fields in the Search screen: " +
	"\"Victim Information: First Name\" = x and \"Victim Information: Last Name\" = y and \"Date Created\" >= YYYY-Mon-DD. " +
	"If you do not select the \"Match grid column specifications\" checkbox, " +
	"Martus will find bulletins created after your specified date where any row has the first name you specified " +
	"and any other row has the last name specified, but not necessarily in the same bulletin row " +
	"(you could have a row with \"First Name\" = x and \"Last Name\" = b, " +
	"and a different row with \"First Name\" = a and \"Last Name\" = y, " +
	"and Martus will find that bulletin as matching the search because you did not specify that it had to match in a single row)" +
	"\n\n",
"field:FancySearchHelpMsg5= Additional Search Notes:\n" +
	"\n " +
	"1. Because Martus searches all rows of any grid (table) fields for your criteria, " +
	"it may find bulletins where one grid row matches your criteria but other rows do not.  " +
	"For example you could have a bulletin with a grid that has a location field in it and you have multiple rows of data " +
	"in the grid with locations A, B, and C.  If you search for bulletins where location != C (does not equal C), " +
	"Martus will find that bulletin because there are 2 rows in the bulletin grid where the location is not C, " +
	"even though there is one row where the locations IS C.\n" +
	"\n " +
	"2. " +
	"In this release, if you are searching on a multi-level dropdown, all searches are exact matches, " +
	"not partial or \"starts with\" matches.  " +
	"This means that you have to pick the exact level at which you want to be searching.  " +
	"For example, an \"Event Location\" field that has three levels (Region/City/Neighborhood) " +
	"will have three entries in the search field list:  Event Location: Region, Event Location: City, and Event Location: Neighborhood.  " +
	"So if you want to find any bulletins that have an Event Location anywhere in Region X (regardless of the City), " +
	"you have to pick the \"Event Location: Region\" field to search on and pick Region X off the dropdown list choices. " +
	"If you pick Event Location: City to search on and then pick Region X but leave the City level blank, " +
	"Martus will only find entries where there was no City data entered  (City was blank), " +
	"as opposed to ANY location with Region X regardless of what data was entered at the City level.\n" +
	"\n " +
	"3. " +
	"If you do not see your search terms/dates in the final version of the bulletin displayed in the Search Results folder, " +
	"your criteria may have been matched in an earlier version of the bulletin.  " +
	"You can access previous versions by clicking the \"Bulletin Details...\" button at the top of the bulletin.  " +
	"To search only the most recent versions of bulletins, select Only Search Most Recent Versions of Bulletins " +
	"in the Search dialog box.\n" +
	"\n " +
	"4. " +
	"If you have fields in different bulletins or from different customizations that are exactly the same, " +
	"Martus will combine them in any Search and Report field lists.  " +
	"And while Martus warns you about duplicate labels where you are creating a new bulletin customization, " +
	"it is possible that over time, you may have bulletins with different customizations that ended up with the same labels " +
	"(e.g. maybe you changed a text field to a dropdown field but kept the same label).  " +
	"In these cases, Martus will display both fields in the search screen, " +
	"and try to help you figure out the difference between the fields by displaying what the field type and " +
	"tag are in the field selection lists.  Also, if you have fields with the same tag but different labels and/or field types, " +
	"Martus may use the tag and field type to try and determine when different fields were meant to be the same.  " +
	"So we encourage you to make your field tags and labels in a customization clearly related to each other to avoid any confusion. " +
	"Please see the \"Customize Fields\" Help screen for more guidelines on creating fields for searching.\n" +
	"\n " +
	"5. " +
	"You may sometimes see duplicate entries in dropdown list search criteria values in the search screen.  " +
	"If you pick a Reusable Choices dropdown field to search on, the values that are displayed as the criteria " +
	"dropdown list options are the labels for each list entry, but the codes you defined determine how many entries " +
	"there will be in the search dropdown list (see the \"Customize Fields\" Help screen for more information about " +
	"creating custom dropdown fields). So if you have different Reusable Choices codes with the same label in " +
	"different bulletin customizations in your account, the labels will show up twice in search dropdown lists " +
	"(i.e. if you used label1 for both code1 and code2, you will see 2 entries in the search dropdown that looks " +
	"like \"label1\" and your search will be on bulletins that have that label, " +
	"regardless of which code the customization had for the label).  " +
	"And if you have the same Reusable Choices code with different labels in different bulletin customizations in your account, " +
	"the search dropdown list for that code will show both values separated by a semicolon " +
	"(i.e. if you used code1 for both label1 and label2, you will see an entry in the search dropdown that looks " +
	"like \"label1; label2\" and your search will be on bulletins that have either of those labels).\n" +
	"\n" +
	"For additional help with searching, email help@martus.org.",
"field:SearchProgress=Progress: ",
"field:ReportSearchProgress=Progress: ",
"field:SearchFound=#NumberBulletinsFound# bulletins matched the search, and have been added to the Search Results folder.",
"field:ReportFound=#NumberBulletinsFound# bulletins matched the search.",
"field:ReportChooseSortFields=Choose how the bulletins will be sorted in the report. The bulletins will be ordered by the first field chosen. When two bulletins have the same value in that field, they will be sorted by the next sort field, and so on.",
"field:ReportFieldError=*Error retrieving field*",
"field:ReportDetailOnly=Print Bulletin Information",
"field:ReportDetailWithSummaries=Print Bulletin Information and Summary Counts",
"field:ReportSummariesOnly=Print Summary Counts Only",
"field:ChooseReportFields=Use Ctrl-Click or Shift-Click to select the fields (columns) that will appear in this report. ",
"field:MartusReportFormatFileFilter=Martus Report Format (.mrf)",
"field:MartusSearchSpecFileFilter=Martus Search Specification (.mss)",
"field:MBAFileFilter=Martus Bulletin Archive (*.mba)",
"field:JPEGFileFilter=JPEG (*.jpeg, *.jpg)",
"field:KeyPairFileFilter=Martus Key (*.dat)",
"field:HtmlFileFilter=HTML (*.html, *.htm)",
"field:CustomXMLTopSection=Top Pane of Bulletin",
"field:CustomXMLBottomSection=Bottom (always Private) Pane of Bulletin",
"field:WasSentYes=Yes",
"field:WasSentNo=No",
"field:NotSorted=(none)",
"field:ReportNumberOfBulletins=Total Bulletins:",

"field:BulletinViewHeading=Martus Bulletin",
"field:ViewBulletinHQInfo=#NumberOfHQs# Headquarter account(s) can view this private information",
"field:ModifyBulletinHQInfo=#NumberOfHQs# Headquarter account(s) will be able to view this private information",
"field:RemoveMartusFromSystemWarning=WARNING!",
"field:RemoveMartusFromSystemMultipleAccountsWarning1=IMPORTANT: There are other Martus accounts on this system.",
"field:RemoveMartusFromSystemMultipleAccountsWarning2=All of their data will be removed as well!",
"field:QuickEraseFollowingItems=Clicking on Ok will do the following on this computer:",
"field:QuickEraseWillNotRemoveItems=This will NOT remove any bulletins from the server, any Martus files copied outside of the Martus directory, or any of your bulletins or Martus files on another computer.",
"field:QuickEraseWillRemoveItems=Delete your Martus key, bulletins, folders, and configuration information (such as your Headquarters, server, and custom field settings).",
"field:RemoveMartusWillUninstall=Uninstall the Martus program.",
"field:RemoveMartusWillRemoveAllOtherAccounts=Delete all other Martus accounts, including their key, folders, and bulletins.",
"field:RemoveMartusWillDeleteMartusDirectory=Delete the Martus directory and all of its contents.",
"field:QuickEraseWillExitMartus=Exit Martus when complete.",

"field:BulletinDetailsAuthorPublicCode=Author Public Code",
"field:BulletinDetailsBulletinId=Bulletin ID",
"field:BulletinHeadQuartersHQLabel=Label",
"field:BulletinHeadQuartersHeadquarters=Headquarters",
"field:BulletinDetailsVersionNumber=Version #",
"field:BulletinDetailsVersionDate=Date Saved",
"field:BulletinDetailsVersionId=ID",
"field:BulletinDetailsVersionTitle=Title",
"field:BulletinDetailsHistory=History",
"field:BulletinDetailsExtendedHistory=Extended History",
"field:PreviousAuthor=Previous Author: #AUTHOR#",
"field:PreviousBulletinId=Bulletin ID: #ID#",
"field:BulletinDetailsUnknownDate=(unknown)",
"field:BulletinDetailsUnknownTitle=(unknown)",
"field:Unknown=(unknown)",
"field:BulletinDetailsInProgressDate=(in progress)",
"field:BulletinDetailsInProgressTitle=(in progress)",
"field:BulletinHeadQuartersHQInfoForView=The following Headquarters account(s) can view the private information in this bulletin, and can send this bulletin to a server.",
"field:BulletinHeadQuartersHQInfoForModify=The selected Headquarters account(s) will be able to view the private information in this bulletin, or send this bulletin to a server, after you save it.\n\nTo add a new Headquarters account use the Tools/Configure Headquarters menu option.",
"field:ImportBulletinsIntoWhichFolder=Import bulletins into which Folder?",
"field:ImportProgress=Importing",
"field:ExportProgress=Exporting",
"field:ImportExportBulletinTitle=Bulletin",
"field:DataIsHidden=(Press the + button to show the hidden information)",

"field:LoadingFieldValuesFromAllBulletins=Processing bulletin",
"field:LoadingFieldValuesFromAllBulletinsExplanation=Scanning all bulletins to create a list of all the values in #FieldName#",

"field:_Section_BulletinSectionHeader=Header",
"field:_Section_BulletinSectionHeadquarters=Headquarters",
"field:_SectionTopSection=",
"field:_SectionBottomSection=",

"field:mdyOrder=Date format sequence",
"field:DateDelimiter=Date delimiter",
"field:CalendarSystem=Calendar type",
"field:DatePartYear=Year",
"field:DatePartMonth=Month",
"field:DatePartDay=Day",
"field:DateDelimiterSlash=Slash (00/00/00)",
"field:DateDelimiterDash=Dash (00-00-00)",
"field:DateDelimiterDot=Dot (00.00.00)",
"field:CalendarSystemGregorian=Default (2005-05-31)",
"field:CalendarSystemThai=Thai Solar (2548-05-31)",
"field:CalendarSystemPersian=Persian (1384-03-10)",
"field:CalendarSystemAfghan=Afghan (1384-03-10)",

"field:FieldTypeSTRING=Text",
"field:FieldTypeBOOLEAN=Yes/No",
"field:FieldTypeDATE=Date",
"field:FieldTypeDATERANGE=Date Range",
"field:FieldTypeDROPDOWN=Dropdown List",
"field:FieldTypeLANGUAGE=Language",
"field:FieldTypeMULTILINE=Text with Scrollbar",
"field:FieldTypeMESSAGE=Message",
"field:FieldTypeGRID=Grid/Table",

"field:BackgroundSearching=Searching...",
"field:BackgroundSorting=Sorting Bulletins...",
"field:BackgroundPrinting=Preparing Report...",
"field:BackgroundWorking=Working...",

"folder:%OutBox=Unsent Bulletins",
"folder:%Sent=Saved Bulletins",
"folder:%Draft=Old Draft Bulletins",
"folder:%Discarded=Discarded Bulletins",
"folder:%RetrievedMyBulletin=Retrieved Bulletins",
"folder:%RetrievedFieldOfficeBulletin=Field Desk Bulletins",
"folder:%RetrievedMyBulletinDraft=Retrieved Draft Bulletins",
"folder:%RetrievedFieldOfficeBulletinDraft=Field Desk Draft Bulletins",
"folder:%SearchResults=Search Results",
"folder:%RecoveredBulletins=Recovered Bulletins",
"folder:%DamagedBulletins=Damaged Bulletins",

"month:jan=Jan",
"month:feb=Feb",
"month:mar=Mar",
"month:apr=Apr",
"month:may=May",
"month:jun=Jun",
"month:jul=Jul",
"month:aug=Aug",
"month:sep=Sep",
"month:oct=Oct",
"month:nov=Nov",
"month:dec=Dec",

"month:Thai1=Jan",
"month:Thai2=Feb",
"month:Thai3=Mar",
"month:Thai4=Apr",
"month:Thai5=May",
"month:Thai6=Jun",
"month:Thai7=Jul",
"month:Thai8=Aug",
"month:Thai9=Sep",
"month:Thai10=Oct",
"month:Thai11=Nov",
"month:Thai12=Dec",

"month:Persian1=Farvardin",
"month:Persian2=Ordibehesht",
"month:Persian3=Khordad",
"month:Persian4=Tir",
"month:Persian5=Mordad",
"month:Persian6=Shahrivar",
"month:Persian7=Mehr",
"month:Persian8=Aban",
"month:Persian9=Azar",
"month:Persian10=Dey",
"month:Persian11=Bahman",
"month:Persian12=Esfand",

"month:Afghan1=hamal",
"month:Afghan2=sawr",
"month:Afghan3=dʒawzɒ",
"month:Afghan4=saratɒn",
"month:Afghan5=asad",
"month:Afghan6=sonbola",
"month:Afghan7=mizɒn",
"month:Afghan8='aqrab",
"month:Afghan9=qaws",
"month:Afghan10=dʒadi",
"month:Afghan12=dalvæ",
"month:Afghan11=hut",

"keyword:and=and",
"keyword:or=or",
	};
}
