package com.cts.accelerators.migration.helpers;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CDATAAdapter extends XmlAdapter<String, String> {

	@Override
	public String marshal(String cdataValue) throws Exception {
		return "<![CDATA[" + cdataValue + "]]>";
	}

	@Override
	public String unmarshal(String cdataValue) throws Exception {
		return cdataValue;
	}
}