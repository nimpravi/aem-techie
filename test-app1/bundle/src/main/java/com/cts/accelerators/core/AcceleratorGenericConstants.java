package com.cts.accelerators.core;

/**
 * This class contains all the generic constants used.
 * 
 * @author Cognizant
 * 
 */
public class AcceleratorGenericConstants {

	// Service Request Types
	public static String REQUEST_TYPE = "requestType";
	public static String COMPLETE_MIGRATION_PROCESS = "CompleteMigration";
	public static String CONTENT_BACKUP = "ContentBackup";
	public static String EXTRACT_SOURCE = "ExtractSource";
	public static String GENERATE_SAMPLE_XSD = "GenerateXSD";
	public static String SHOW_MIGRATION_REPORT = "ShowReport";
	public static String CONTENT_TRANSFORMER = "ContentTransformer";
	public static String CONTENT_IMPORTER = "ContentImporter";
	public static String DAM_BULK_UPLOAD = "damBulkUpload";

	// Common Form Constants
	public static String FILE_DESTINATION_PATH = "fileDestinationPath";
	public static String SOURCE_ROOT_PATH = "sourceRootPath";
	public static String DESTINATION_ROOT_PATH = "destinationRootPath";
	public static String MOVE_TO = "moveTo";

	// XSD Form Constants
	public static String XSL = "XSL";
	public static String XML = "xml";
	public static String XSD_FILE_NAME = "Migration.xsd";

	// Transformer request types
	public static String TRANSFORMATION_ORDER = "transformationOrder";
	public static String MAPPING_FILE_PATH = "mappingFilePath";
	public static String TRANFORMATION_TYPE = "transformationType";

	// Dam bulk upload request types
	public static String HTTP = "http://";
	public static String URL = "URL";
	public static String IMAGE_FOLDER_NAME = "images";
	public static String SUCCESS_DESC = "Successfully moved";
	public static String SUCCESS_UPDATED_DESC = "Successfully updated the file properties";
	public static String FAILURE_DESC = "Failed to move the file please check the logs";
	public static String CURL_COMMAND = "curl";
	public static String EXE_COMMAND = "cmd.exe";
	public static String CURL_C = "/c";
	public static String CURL_CD = "cd ";
	public static String QUOTE = "\"";
	public static String AMP = " && ";
	public static String FILE_SYSTEM_1 = "File System";

	// Importer request types
	public static String ENABLE_DUPLICATION = "enableDuplication";
	public static String DISABLE_UPDATE = "disableUpdate";
	public static String ENABLE_DUPLICATE_PAGE_CREATION = "enableDuplication";
	public static String IMPORT_ORDER = "importOrder";
	public static String PAGE_PATH = "pagePath";
	public static String DAM_ASSET_PATH = "damPath";
	public static String TAXONOMY_PATH = "taxonomyPath";
	public static String ENABLE_REPLICATION = "enableReplication";
	public static String DISABLE_AUTO_UPDATE = "disableUpdate";
	public static String IMPORT_TYPE_DAM = "dam";
	public static String IMPORT_TYPE_TAG = "tag";
	public static String IMPORT_TYPE_PAGE = "page";

	// Migration Form Constants
	public static String BACKUP = "backup";
	public static String TRANSFORM = "transform";
	public static String IMPORT = "import";

	// Report Form Constants

	public static String DEFAULT_REPORT_PATH = "Reports";
	public static String REPORT_RECORDS = "reportRecords";
	public static String RECORDS = "records";

	public static String REPORT_PATH = "reportPath";
	public static String DAM_REPORT = "dam_report";
	public static String DAM_REPORT_HEADERS = "ID,SourcePath,DestinationPath,ModifiedDate,ModifiedUser,Status,StatusDesc";

	public static String TRANSFORMATION_REPORT = "transformation_report";
	public static String TRANSFORMATION_REPORT_HEADERS = "SourcePath,DestinationPath,XSLFilePath,ModifiedDate,Status,StatusDesc";

	public static String IMPORT_REPORT = "import_report";
	public static String IMPORT_REPORT_HEADERS = "SourcePath,DestinationPath,Status,StatusDesc";

	public static String TAXONOMY_REPORT = "taxonomy_report";
	// public static String TAXONOMY_REPORT_HEADERS =
	// "TagID,ModifiedDate,Status,StatusDesc";

	public static String TAXONOMY_REPORT_HEADERS = "TagID,ParentID,ComponentID,Status,StatusDesc";

	public static String REPLICATION_REPORT = "replication_report";
	public static String REPLICATION_REPORT_HEADERS = "PagePath,PageTitle,PublishStatus,PublishDate,Status,StatusDesc";

	public static String BACKUP_REPORT = "backup_report";
	public static String BACKUP_REPORT_HEADERS = "SourcePath,DestinationPath,Status,StatusDesc";

	public static String CSV_EXTENSION = "csv";
	public static String CSV_SEPARATOR = ",";
	public static String DESCRIPTION = "description";
	public static String STANDALONE_REPORT_FOLDER = "IndividualReports";
	public static String ALL_REPORT_FOLDER = "MigrationReports";
	public static String REPORT_CRX_TEMP_PATH = "/etc/temp/report";
	public static String REPORT_PROPERTY_FILE_PATH = "/etc/reports/";

	public static String ALL_REPORT_TYPES = "all_reports";
	public static String SPECIFIC_REPORT = "specificReport";

	public static String INDIVIDUAL_OR_MIGARTION_REPORTS = "individualOrMigration";
	public static String MIGRATION_REPORTS = "MigrationReports";
	public static String INDIVIDUAL_REPORTS = "IndividualReports";
	public static String REPORT_DATE = "reportDate";
	public static String REPORT_TYPE = "reportType";
	public static String REPORT_DETAILS_TYPE = "reportDetailType";
	public static String DETAILS = "details";
	public static String ALL_MIGRATION_REPORTS = "AllMigrationReports";

	public static String DAM_FILES_MAPPING_PROPERTIES = "damFilesMapping.properties";
	public static String TAG_FILES_MAPPING_PROPERTIES = "tagFilesMapping.properties";
	public static String PAGE_FILES_MAPPING_PROPERTIES = "pageFilesMapping.properties";

	// 1Step Form constants
	public static String READ_FROM_CONFIG = "readFromConfigs";

	public static String CRX = "CRX";
	public static String FILE_SYSTEM = "fileSystem";
	public static String STATUS = "status";
	public static String STATUS_SUCCESS = "success";
	public static String STATUS_FAILURE = "failure";
	public static String RECORD_SEPRARTOR = ",";
	public static String FOLDER_NAME = "folderName";
	public static String FOLDER_ID = "folderId";
	public static String FOLDER_PATH = "folderPath";
	public static String FILES = "files";
	public static String FILE_PATH = "filePath";
	public static String FILE_NAME = "fileName";
	public static String FILE_REFERENCE = "fileReference";
	public static String FIRST_REPORT_SUMMARY = "firstReportSummary";
	public static String FAILURE_REPORT = "FailureReport";
	public static String TOTAL_RECORDS = "total_records";
	public static String COUNT = "count";
	public static String YES = "yes";
	public static String TRUE = "true";
	public static String TAGIDS = "tagIds";

	// Utils Constants
	public static String PARSYS = "parsys";
	public static String PATH = "path";
	public static String ORDER_BY = "orderby";
	public static String ORDER_BY_SORT = "orderby.sort";
	public static String P_LIMIT = "p.limit";
	public static String JCR_CONTENT_LAST_MODIFIED = "@jcr:content/jcr:lastModified";
	public static String DESC = "desc";
	public static String P_LIMIT_VALUE = "1000000";

	// Misc
	public static String FORWARD_SLASH = "/";
	public static String BACKWARD_SLASH = "\\";
	public static String UNDERSCORE = "_";
	public static String DOT = ".";
	public static String TEMP_FILE_PATH = "/etc/temp";
	public static String PRIMARY_TYPE_NT_UNSTRUCTURED = "nt:unstructured";
	public static String SLING_RESOURCE_TYPE = "sling:resourceType";
	public static String TEXT_IS_RICH = "textIsRich";
	public static String CQ_TEMPLATE = "cq:template";
	public static String CQ_DESIGNPATGH = "cq:designPath";
	public static String CQ_PAGE = "cq:Page";
	public static String CQ_PAGECONTENT = "cq:PageContent";
	public static String PARSYS_COMPONENT_PATH = "foundation/components/parsys";
	public static String IPARSYS_COMPONENT_PATH = "foundation/components/iparsys";
	public static String CQ_USER = "admin";
	public static String CQ_PASSWORD = "admin";
	public static String MIME_TYPE = "application/octet-stream";
	public static String FILE_TYPE = "nt:file";
	public static String FOLDER_TYPE = "nt:folder";
	public static String JCR_CONTENT = "jcr:content";
	public static String MIX_REFERENCABLE = "mix:referenceable";
	public static String NT_RESOURCE = "nt:resource";
	public static String JCR_MIME_TYPE = "jcr:mimeType";
	public static String JCR_DATA = "jcr:data";
	public static String JCR_LAST_MODIFIED = "jcr:lastModified";
	public static String UTF_8 = "UTF-8";
	public static String TAG_FIELD_SEPARATOR = ":";
	public static String DAM_ASSET = "dam:Asset";
	public static String DAM_ASSET_CONTENT = "dam:AssetContent";
	public static String METADATA = "metadata";
	public static String RELATED = "related";
	public static String RENDITIONS = "renditions";
	public static String ORIGINAL = "original";
	public static String IMAGE = "image";
	public static String REPLICATION_TYPE_ACTIVATE = "activate";
	public static String REPLICATION_TYPE_DEACTIVATE = "deactivate";
	public static String FILEPATH = "filepath";
	public static String DOWNLOAD_LINK = "downloadLink";
	public static String MMDDYYYY_FORMAT = "MM/dd/yyyy";
	public static String DDMMYYYY_FORMAT = "ddMMyyyy";
	public static String YYYYMMDD_FORMAT = "yyyy/MM/dd HH:mm:ss";
	public static String ITEMNAME = "_ITEMNAME_";
	public static String USER_DIR = "user.dir";
	public static String EXTRACT_SOURCE_CONTENT = "extractSourceContent";
	public static String PROFILE_EMAIL = "./profile/email";
	public static String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static String NEW_LINE = "\n";
	public static String EQUAL_TO = "=";
	public static String BYTES_VALUE_PATTERN = "[0-9]+";
	public static String tagIDPath = "/etc/tags/";

	// Logger messages specific to service
	public static String File_SIZE_EXCEEDS = "filesize is large as compare to expected size";

	public static String CHECKBOX_CHECKED = "checked";

	public static String JCR_TITLE = "jcr:title";

	// Page Properties
	public static String HIDE_IN_NAV = "hideInNav";
	public static String NAVIGATION_TITLE = "navTitle";
	public static String OFF_TIME = "offTime";
	public static String ON_TIME = "onTime";
	public static String PAGE_TITLE = "pageTitle";
	public static String SLING_REDIRECT = "sling_redirect";
	public static String SLING_VANITYPATH = "sling_vanityPath";
	public static String SUB_TITLE = "subtitle";

	public static Integer MAX_FEED_ITEMS = 10;
	public static String AUTO_PICK = "auto-pick";

	public static String OPEN_SQUARE_BRAC = "[";
	public static String CLOSE_SQUARE_BRAC = "]";

	// EXCEL constants
	public static String EXCEL = "EXCEL";
	public static String EXCEL_FILE_EXTENSION = "xls";

	// EXCEL header constants
	public final static String NO_OF_PAGES = "NumberOfPages";
	public final static String[] PAGE_HEADERS = { "pageId", "nodeName",
			"parentNodePath", "resourceType", "template", "jcr_title",
			"cqDesignPath" };
	public final static String[] PAGE_PROPERTIES_HEADERS = { "hideInNav",
			"navTitle", "offTime", "onTime", "pageTitle", "sling_redirect",
			"sling_vanityPath", "subtitle", "jcr_description" };
	public final static String[] AUTHOR_COMPONENT_HEADERS = {
			"NumberOfAuthorComponents", "AuthorComponents",
			"NumberOfComponents", "ContainerVariance", "ContainerName",
			"componet_type", "component_id" };
	public final static String[] TEXT_COMPONENT_HEADERS = { "name", "resource",
			"textData" };
	public final static String[] LOGIN_COMPONENT_HEADERS = { "nodeName",
			"sectionLabel", "usernameLabel", "passwordLabel", "loginLabel",
			"redirectTo" };
	public final static String[] FLASH_COMPONENT_HEADERS = { "nodeName",
			"fileReference", "height", "width", "html5SmartImageDTO", "wmode",
			"bgColor", "flashVersion", "attrs" };
	public final static String[] IMAGE_COMPONENT_HEADERS = { "nodeName",
			"file", "imageCrop", "fileName", "fileReference", "imageRotate",
			"jcr_title", "alt", "linkURL", "jcr_description", "size", "height",
			"width", "imageText" };
	public final static String[] PROFILE_COMPONENT_HEADERS = { "nodeName",
			"jcr_title", "deleteTitle", "cols", "width", "constraintMessage",
			"constraintType", "required", "requiredMessage", "jcr_description",
			"rows", "cols", "defaultValue", "honoricPrefixTitle", "title",
			"givenNameTitle", "middleNameTitle", "familyNameTitle",
			"honoricSuffixTitle", "name", "confirmationTitle" };
	public final static String[] SEARCH_COMPONENT_HEADERS = { "nodeName",
			"searchIn", "searchButtonText", "statisticsText", "noResultsText",
			"spellcheckText", "similarPagesText", "relatedSearchesText",
			"searchTrendsText", "resultPagesText", "previousText", "nextText" };
	public final static String[] TEXT_IMAGE_COMPONENT_HEADERS = { "nodeName",
			"imageDTO", "textDTO" };
	public final static String[] VIDEO_COMPONENT_HEADERS = { "nodeName",
			"file", "height", "width" };
	public final static String[] ARCHIVE_COMPONENT_HEADERS = { "nodeName",
			"dateFormat" };
	public final static String[] CHART_COMPONENT_HEADERS = { "nodeName",
			"chartData", "chartType", "chartAlt", "chartWidth", "chartHeight" };
	public final static String[] BREADCRUMB_COMPONENT_HEADERS = { "nodeName",
			"absParent", "relParent", "delim", "trail" };
	public final static String[] DESIGN_TITLE_COMPONENT_HEADERS = { "nodeName",
			"defaultType" };
	public final static String[] TOOLBAR_COMPONENT_HEADERS = { "nodeName",
			"absParent", "toolbar", "list" };
	public final static String[] TOPNAV_COMPONENT_HEADERS = { "nodeName",
			"absParent" };
	public final static String[] USER_INFO_COMPONENT_HEADERS = { "nodeName",
			"loginPage", "profilePage", "signupPage" };
	public final static String[] CAROUSEL_COMPONENT_HEADERS = { "nodeName",
			"playSpeed", "transTime", "controlsType", "listFrom", "orderBy",
			"parentPage", "limit", "pages", "searchIn", "query", "savedquery" };
	public final static String[] DOWNLOAD_COMPONENT_HEADERS = { "nodeName",
			"jcr_description", "file", "fileName", "fileReference",
			"autoUploadDelay" };
	public final static String[] EXTERNAL_COMPONENT_HEADERS = { "nodeName",
			"target", "passparams", "widthheight" };
	public final static String[] LIST_CHILDREN_COMPONENT_HEADERS = {
			"nodeName", "listroot" };
	public final static String[] LIST_COMPONENT_HEADERS = { "nodeName",
			"listFrom", "displayAs", "orderBy", "limit", "feedEnabled",
			"pageMax", "parentPage", "ancestorPage", "pages", "fieldConfig",
			"width", "query", "savedquery", "tagsSearchRoot", "tags",
			"tagsMatch" };
	public final static String[] ENTRY_TEXT_COMPONENT_HEADERS = { "nodeName",
			"text" };
	public final static String[] REDIRECT_COMPONENT_HEADERS = { "nodeName",
			"redirectTarget" };
	public final static String[] REFERENCE_COMPONENT_HEADERS = { "nodeName",
			"path" };
	public final static String[] SITE_MAP_COMPONENT_HEADERS = { "nodeName",
			"rootPath" };
	public final static String[] SLIDE_SHOW_COMPONENT_HEADERS = { "nodeName",
			"slideshowHeight", "slideshowWidth" };
	public final static String[] TABLE_COMPONENT_HEADERS = { "nodeName",
			"tableData" };
	public final static String[] TITLE_COMPONENT_HEADERS = { "nodeName",
			"jcr_title", "type" };
	public final static String[] ENTRY_LIST_COMPONENT_HEADERS = { "nodeName",
			"limit", "pageMax", "dateFormat" };
	public final static String[] CUSTOM_IMAGE_COMPONENT_HEADERS = { "nodeName",
			"file", "imageCrop", "fileName", "iconReference", "imageRotate",
			"jcr_title", "alt", "linkURL", "jcr_description", "size", "height",
			"width", "redirectedURL", "redirectText" };
	public final static String[] TITLE_IMAGE_COMPONENT_HEADERS = { "nodeName",
			"imageDTO", "title" };
	public final static String[] TITLE_LINK_COMPONENT_HEADERS = { "nodeName",
			"title", "type", "link" };
	public final static String[] CUSTOM_CAROUSEL_COMPONENT_HEADERS = {
			"nodeName", "no_of_items", "itemDto" };
	public final static String[] CUSTOM_CAROUSEL_ITEM_COMPONENT_HEADERS = {
			"title", "titleDesc", "navTitle", "navDesc", "link", "imagePath" };
	public final static String[] IMAGE_MULTIFIELD_COMPONENT_HEADERS = {
			"nodeName", "imageLayout", "customImageDTO", "no_of_images",
			"imageDTO" };
	public final static String[] IMAGE_TITLE_COMPONENT_HEADERS = { "nodeName",
			"imageDTO", "title" };
	public final static String[] IMAGE_TITLE_LINK_COMPONENT_HEADERS = {
			"nodeName", "titleImageDTO", "textImageDTO", "titleLinkDTO" };
	public final static String[] LIST_OF_LINKS_COMPONENT_HEADERS = {
			"nodeName", "title", "no_of_links", "link", "text" };
	public final static String[] DAM_HEADERS = { "id", "fileName",
			"no_of_custom_props", "CustomProperties" };
	public final static String[] CUSTOM_PROPERTIES_HEADERS = { "id", "key",
			"value" };
	public final static String[] TAG_HEADERS = { "parentID", "tagId",
			"filterVal", "name", "jcr_title", "jcr_description" };

}