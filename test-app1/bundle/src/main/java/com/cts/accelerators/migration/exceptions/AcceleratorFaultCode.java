/**
 * 
 */
package com.cts.accelerators.migration.exceptions;

/**
 * @author 357125 This method contains different fault codes that may occur in
 *         project
 * 
 */
public class AcceleratorFaultCode {

	// GENERIC FAULT CODES
	public static final AcceleratorFaultCode NAMING_ERROR = new AcceleratorFaultCode(
			"naming.error");
	public static final AcceleratorFaultCode FILE_NOT_FOUND = new AcceleratorFaultCode(
			"file.not.found");
	public static final AcceleratorFaultCode CLASS_NOT_FOUND = new AcceleratorFaultCode(
			"class.not.found");
	public static final AcceleratorFaultCode INTERNAL_ERROR = new AcceleratorFaultCode(
			"internal.server.error");
	public static final AcceleratorFaultCode DEF_EXCEPTION = new AcceleratorFaultCode(
			"def.not.found");
	public static final AcceleratorFaultCode MALFORMED_STRING = new AcceleratorFaultCode(
			"malformed.string");
	public static final AcceleratorFaultCode PATH_NOT_FOUND_EXCEPTION = new AcceleratorFaultCode(
			"nopath.error");
	public static final AcceleratorFaultCode PARSE_EXCEPTION = new AcceleratorFaultCode(
			"parse.error");
	public static final AcceleratorFaultCode ILLEGAL_NAME_EXCEPTION = new AcceleratorFaultCode(
			"illegal.name.error");
	public static final AcceleratorFaultCode INVALID_DATE_EXCEPTION = new AcceleratorFaultCode(
			"invalid.date.error");
	public static final AcceleratorFaultCode PROCESSOR_EXCEPTION = new AcceleratorFaultCode(
			"processor.error");
	public static final AcceleratorFaultCode NAMESPACE_EXCEPTION = new AcceleratorFaultCode(
			"namespace.error");
	public static final AcceleratorFaultCode SPELL_CHECK_EXCEPTION = new AcceleratorFaultCode(
			"spellcheck.error");
	public static final AcceleratorFaultCode CONNECTION_ERROR = new AcceleratorFaultCode(
			"connection.error");
	public static final AcceleratorFaultCode INSTANTIATION_ERROR = new AcceleratorFaultCode(
			"class.instantiation.error");
	public static final AcceleratorFaultCode CONF_FILE_MISSING_ERROR = new AcceleratorFaultCode(
			"conf.file.not.found");

	// IO CODES
	public static final AcceleratorFaultCode IOEXCEPTION_STRING = new AcceleratorFaultCode(
			"input.string");
	public static final AcceleratorFaultCode SLING_IO_EXCEPTION = new AcceleratorFaultCode(
			"sling.io.error");

	// TRANSFORM CODES
	public static final AcceleratorFaultCode TRANSFORM_EXCEPTION = new AcceleratorFaultCode(
			"transform.error");
	public static final AcceleratorFaultCode TRANS_UTILITY_EXCEPTION = new AcceleratorFaultCode(
			"trans.utility.error");
	public static final AcceleratorFaultCode SAX_EXCEPTION = new AcceleratorFaultCode(
			"sax.error");

	// EMAIL CODES
	public static final AcceleratorFaultCode EMAIL_SERVICE_EXCEPTION = new AcceleratorFaultCode(
			"email.service.error");
	public static final AcceleratorFaultCode EXACT_TARGET_EXCEPTION = new AcceleratorFaultCode(
			"exact.target.error");

	// ILLEGAL ACCESS CODES
	public static final AcceleratorFaultCode ILLEGAL_CLASS_ACCESS_ERROR = new AcceleratorFaultCode(
			"class.illegal.access.error");
	public static final AcceleratorFaultCode ILLEGAL_METHOD_ACCESS_EXCEPTION = new AcceleratorFaultCode(
			"method.invocation.error");

	// WCM CODES
	public static final AcceleratorFaultCode WCM_EXCEPTION = new AcceleratorFaultCode(
			"wcm_error");

	// REPLICATION CODE
	public static final AcceleratorFaultCode REPLICATION_EXCEPTION = new AcceleratorFaultCode(
			"replication.error");

	// WORKFLOW CODES
	public static final AcceleratorFaultCode WORKFLOW_EXCEPTION = new AcceleratorFaultCode(
			"workflow.error");
	public static final AcceleratorFaultCode WORKFLOW_VALIDATION_EXCEPTION = new AcceleratorFaultCode(
			"workflow.validation.error");

	// ASSET CODES
	public static final AcceleratorFaultCode ASSET_EXCEPTION = new AcceleratorFaultCode(
			"asset.error");
	public static final AcceleratorFaultCode ASSET_HANDLER_EXCEPTION = new AcceleratorFaultCode(
			"asset.handler.error");
	public static final AcceleratorFaultCode ASSET_IO_EXCEPTION = new AcceleratorFaultCode(
			"asset.io.error");

	// REPOSITORY CODE
	public static final AcceleratorFaultCode REPOSITORY_EXCEPTION = new AcceleratorFaultCode(
			"repository.error");
	public static final AcceleratorFaultCode UNSUPPORTED_REPOSITORY_OPERATION_EXCEPTION = new AcceleratorFaultCode(
			"unsupported.repository.operation");

	// REPOSITORY ACCESS RELATED CODES
	public static final AcceleratorFaultCode ACCESS_DENIED_EXCEPTION = new AcceleratorFaultCode(
			"access.denied.error");
	public static final AcceleratorFaultCode ACCESS_CONTROL_EXCEPTION = new AcceleratorFaultCode(
			"access.control.error");

	// LOGIN CODE
	public static final AcceleratorFaultCode LOGIN_EXCEPTION = new AcceleratorFaultCode(
			"login.error");

	// QUERY CODES
	public static final AcceleratorFaultCode INVALID_QUERY_EXCEPTION = new AcceleratorFaultCode(
			"invalid.query.error");
	public static final AcceleratorFaultCode QUERY_SYNTAX_EXCEPTION = new AcceleratorFaultCode(
			"query.syntax.error");

	// REPOSITORY ITEM CODES
	public static final AcceleratorFaultCode ITEM_EXISTS_EXCEPTION = new AcceleratorFaultCode(
			"item.already.exists.error");
	public static final AcceleratorFaultCode ITEM_NOT_FOUND_EXCEPTION = new AcceleratorFaultCode(
			"item.not.found");

	// VERSION CODE
	public static final AcceleratorFaultCode VERSION_EXCEPTION = new AcceleratorFaultCode(
			"version.error");

	// NODETYPE CODES
	public static final AcceleratorFaultCode NODETYPE_EXISTS_EXCEPTION = new AcceleratorFaultCode(
			"nodetype.already.exists.error");
	public static final AcceleratorFaultCode NO_SUCH_NODETYPE_EXCEPTION = new AcceleratorFaultCode(
			"no.nodetype.error");
	public static final AcceleratorFaultCode INVALID_NODETYPE_DEF_EXCEPTION = new AcceleratorFaultCode(
			"invalid.nodetype.def.error");

	// SERVLET CODE
	public static final AcceleratorFaultCode SERVLET_EXCEPTION = new AcceleratorFaultCode(
			"servlet.error");

	// SLING CODES
	public static final AcceleratorFaultCode SLING_EXCEPTION = new AcceleratorFaultCode(
			"sling.error");
	public static final AcceleratorFaultCode RESOURCE_NOT_FOUND_EXCEPTION = new AcceleratorFaultCode(
			"resource.not.found.error");

	// IMPORT CODE
	public static final AcceleratorFaultCode IMPORT_EXCEPTION = new AcceleratorFaultCode(
			"import.error");

	// SERVICE CODES
	public static final AcceleratorFaultCode UNKNOWN_SERVICE_EXCEPTION = new AcceleratorFaultCode(
			"unknown.service.found");

	// INVALID TAG FORMAT CODE
	public static final AcceleratorFaultCode INVALID_TAG_FORMAT_EXCEPTION = new AcceleratorFaultCode(
			"invalid.tag.format.error");

	// TRANSFORMATION FAULT CODES
	public static final AcceleratorFaultCode TRANS_FACTORY_EXCEPTION = new AcceleratorFaultCode(
			"transformation.factory.new.transformer.error");

	public static final AcceleratorFaultCode CONSTRAINT_VOILATION_EXCEPTION = new AcceleratorFaultCode(
			"constraint.violation.error");

	// EMPTY/INVALID MANDATORY PARAMETERS
	public static final AcceleratorFaultCode EMPTY_TEMPLATE_NAME_OR_RESOURCE_TYPE = new AcceleratorFaultCode(
			"empty.template.or.resourcetype");
	public static final AcceleratorFaultCode EMPTY_DAM_HIERARCHY = new AcceleratorFaultCode(
			"empty.dam.hierarchy.or.parentpath");
	public static final AcceleratorFaultCode EMPTY_PAGE_HIERARCHY = new AcceleratorFaultCode(
			"empty.page.hierarchy.or.parentpath");
	public static final AcceleratorFaultCode EMPTY_TAG_HIERARCHY = new AcceleratorFaultCode(
			"empty.tag.hierarchy.or.parentpath");
	public static final AcceleratorFaultCode EMPTY_REPLICATION_PATH = new AcceleratorFaultCode(
			"empty.replication.path");
	public static final AcceleratorFaultCode INVALID_REPLICATION_ACTION = new AcceleratorFaultCode(
			"invalid.replication.action");
	public static final AcceleratorFaultCode INVALID_JCR_PATH = new AcceleratorFaultCode(
			"invalid.jcr.path");

	public static final AcceleratorFaultCode LOCK_EXCEPTION = new AcceleratorFaultCode(
			"lock.error");

	public static final AcceleratorFaultCode JSON_EXCEPTION = new AcceleratorFaultCode(
			"JSON.error");

	public static final AcceleratorFaultCode JAXB_EXCEPTION = new AcceleratorFaultCode(
			"JAXB.error");
	public static final AcceleratorFaultCode ACCELERATOR_EXCEPTION = new AcceleratorFaultCode(
			"Accelerator.error");

	// ReferentialIntegrity CODE
	public static final AcceleratorFaultCode REFERENTIALINTEGRITY_EXCEPTION = new AcceleratorFaultCode(
			"ReferentialIntegrity.error");

	// Invalid ItemState CODE
	public static final AcceleratorFaultCode INVALID_ITEM_STATE_EXCEPTION = new AcceleratorFaultCode(
			"InvalidItemState.error");

	public static final AcceleratorFaultCode STRING_INDEX_OUT_OF_BOUND = new AcceleratorFaultCode(
			"string.index.out.of.bounds.error");

	public static final AcceleratorFaultCode VALUE_FORMAT_EXCEPTION = new AcceleratorFaultCode(
			"value.format.exception");

	private String errorCode = null;

	/**
	 * @param errorCode
	 */
	public AcceleratorFaultCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
