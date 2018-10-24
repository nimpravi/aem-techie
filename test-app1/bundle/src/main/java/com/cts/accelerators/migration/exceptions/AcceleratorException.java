/**
 * 
 */
package com.cts.accelerators.migration.exceptions;

/**
 * @author 357125
 * This is an exception class
 * 
 */
public class AcceleratorException extends Exception {

	private static final long serialVersionUID = 1L;

	private AcceleratorFaultCode faultCode = null;
	private String className = null;
	private String methodName = null;

	/**
	 * @return the faultCode
	 */
	public AcceleratorFaultCode getFaultCode() {
		return faultCode;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param faultCode
	 */
	public AcceleratorException(AcceleratorFaultCode faultCode) {
		super(faultCode.toString());
		this.faultCode = faultCode;
	}

	/**
	 * @param cause
	 */
	public AcceleratorException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param faultCode
	 * @param cause
	 */
	public AcceleratorException(AcceleratorFaultCode faultCode, Throwable cause) {
		super(faultCode.toString(), cause);
		this.faultCode = faultCode;
	}

	/**
	 * @param message
	 */
	public AcceleratorException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AcceleratorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param faultCode
	 * @param className
	 * @param methodName
	 * @param cause
	 */
	public AcceleratorException(AcceleratorFaultCode faultCode,
			String className, String methodName, Throwable cause) {
		super(faultCode.toString(), cause);
		this.faultCode = faultCode;
		this.className = className;
		this.methodName = methodName;
	}

	/**
	 * @param message
	 * @param className
	 * @param methodName
	 * @param cause
	 */
	public AcceleratorException(String message, String className,
			String methodName, Throwable cause) {
		super(message, cause);
		this.className = className;
		this.methodName = methodName;
	}

	/**
	 * @param faultCode
	 * @param className
	 * @param methodName
	 */
	public AcceleratorException(AcceleratorFaultCode faultCode,
			String className, String methodName) {
		super(faultCode.toString());
		this.faultCode = faultCode;
		this.className = className;
		this.methodName = methodName;
	}

	@Override
	public String toString() {
		return this.faultCode.getErrorCode();
	}

}
