package com.cts.accelerators.migration.services.dto;

/**
 * ServiceResponse class to instantiate and carry response of NotificationService
 * 
 * @author 432087
 * 
 */
import com.cts.accelerators.core.dto.AcceleratorServiceResponse;

public class NotificationServiceResponse implements AcceleratorServiceResponse{
	private boolean notificationSuccessful;

	/**
	 * @return the notificationSuccessful
	 */
	public boolean isNotificationSuccessful() {
		return notificationSuccessful;
	}

	/**
	 * @param notificationSuccessful the notificationSuccessful to set
	 */
	public void setNotificationSuccessful(boolean notificationSuccessful) {
		this.notificationSuccessful = notificationSuccessful;
	}
}
