package com.cts.accelerators.migration.helpers;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

public class XmlCharacterHandler implements CharacterEscapeHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlCharacterHandler.class);

	public void escape(char[] buf, int start, int len, boolean isAttValue,
			Writer out) throws IOException {
		LOGGER.info("Inside escape.......  ");

		StringWriter buffer = new StringWriter();
		LOGGER.info("Inside escape.......  converting to string writer");

		for (int i = start; i < start + len; i++) {
			buffer.write(buf[i]);
		}
		String st = buffer.toString();
		LOGGER.info("Inside escape.......  escaping characters");
		if (!st.contains("CDATA")) {
			st = buffer.toString().replace("&", "&amp;").replace("<", "&lt;")
					.replace(">", "&gt;").replace("'", "&apos;")
					.replace("\"", "&quot;");

		}
		LOGGER.info("Inside escape.......  after escaping " + st);
		out.write(st);
	}
}