package com.cts.accelerators.migration.exceptions;

public class AcceleratorSystemException extends AcceleratorException {

	private static final long serialVersionUID = 1L;

	/**
	 * Calls the super class's constructor
	 * 
	 * @param faultCode
	 */
	public AcceleratorSystemException(AcceleratorFaultCode faultCode) {
		super(faultCode.toString());
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param cause
	 */
	public AcceleratorSystemException(Throwable cause) {
		super(cause);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param faultCode
	 * @param cause
	 */
	public AcceleratorSystemException(AcceleratorFaultCode faultCode,
			Throwable cause) {
		super(faultCode.toString(), cause);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param msg
	 */
	public AcceleratorSystemException(String msg) {
		super(msg);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param msg
	 * @param cause
	 */
	public AcceleratorSystemException(String msg, Throwable cause) {
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
	public AcceleratorSystemException(AcceleratorFaultCode faultCode,
			String className, String methodName, Throwable cause) {
		super(faultCode.toString(), cause);
	}

	/**
	 * Calls the super class's constructor
	 * 
	 * @param message
	 * @param className
	 * @param methodName
	 * @param cause
	 */
	public AcceleratorSystemException(String message, String className,
			String methodName, Throwable cause) {
		super(message, className, methodName, cause);
	}

	/**
	 * @param faultCode
	 * @param className
	 * @param methodName
	 */
	public AcceleratorSystemException(AcceleratorFaultCode faultCode,
			String className, String methodName) {
		super(faultCode.toString());
	}
}
