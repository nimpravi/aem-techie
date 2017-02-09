/**
 * 
 */
package com.cts.accelerators.migration.exceptions;

/**
 * @author 357125
 * 
 */
public class XMLTransformerException extends AcceleratorException {

	/**
	 * Calls the super class's constructor
	 * 
	 * @param faultCode
	 */
	public XMLTransformerException(AcceleratorFaultCode faultCode) {
		super(faultCode);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param cause
	 */
	public XMLTransformerException(Throwable cause) {
		super(cause);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param faultCode
	 * @param cause
	 */
	public XMLTransformerException(AcceleratorFaultCode faultCode,
			Throwable cause) {
		super(faultCode, cause);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param msg
	 */
	public XMLTransformerException(String msg) {
		super(msg);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param msg
	 * @param cause
	 */
	public XMLTransformerException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param faultCode
	 * @param className
	 * @param methodName
	 * @param cause
	 */
	public XMLTransformerException(AcceleratorFaultCode faultCode,
			String className, String methodName, Throwable cause) {
		super(faultCode, className, methodName, cause);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param message
	 * @param className
	 * @param methodName
	 * @param cause
	 */
	public XMLTransformerException(String message, String className,
			String methodName, Throwable cause) {
		super(message, className, methodName, cause);
	}

}
