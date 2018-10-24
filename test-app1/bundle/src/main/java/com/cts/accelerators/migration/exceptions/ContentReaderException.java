/**
 * 
 */
package com.cts.accelerators.migration.exceptions;

/**
 * @author 357125
 * 
 */
public class ContentReaderException extends AcceleratorException {

	/**
	 * Calls the super class's constructor
	 * 
	 * @param faultCode
	 */
	public ContentReaderException(AcceleratorFaultCode faultCode) {
		super(faultCode);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param cause
	 */
	public ContentReaderException(Throwable cause) {
		super(cause);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param faultCode
	 * @param cause
	 */
	public ContentReaderException(AcceleratorFaultCode faultCode,
			Throwable cause) {
		super(faultCode, cause);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param msg
	 */
	public ContentReaderException(String msg) {
		super(msg);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param msg
	 * @param cause
	 */
	public ContentReaderException(String msg, Throwable cause) {
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
	public ContentReaderException(AcceleratorFaultCode faultCode,
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
	public ContentReaderException(String message, String className,
			String methodName, Throwable cause) {
		super(message, className, methodName, cause);
	}

}
