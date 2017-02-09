package com.cts.accelerators.migration.helpers;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

public class MigrationEventHandler implements ValidationEventHandler {

	public boolean handleEvent(ValidationEvent validationEvent) {

		if (validationEvent.getSeverity() == ValidationEvent.FATAL_ERROR
				|| validationEvent.getSeverity() == ValidationEvent.ERROR) {
			ValidationEventLocator locator = validationEvent.getLocator();
			// Print message from valdation event
			System.out.println("Invalid xml document: " + locator.getURL());
			System.out.println("Error: " + validationEvent.getMessage());
			// Output line and column number
			System.out.println("Error at column " + locator.getColumnNumber()
					+ ", line " + locator.getLineNumber());
		}
		return true;
	}
}
