/*
 * Copyright 1997-2010 Day Management AG
 * Barfuesserplatz 6, 4001 Basel, Switzerland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Day Management AG, ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Day.
 */
package com.cts.store;

import java.util.Iterator;

import com.day.cq.wcm.api.Page;

public class LeftNavigation {

	private Page homePage;
	private Page level1Page;
	private Page level2Page;
	private Page level3Page;

	public Page getLevel2Page() {
		return level2Page;
	}

	public void setLevel2Page(Page level2Page) {
		this.level2Page = level2Page;
	}

	public Page getLevel3Page() {
		return level3Page;
	}

	public void setLevel3Page(Page level3Page) {
		this.level3Page = level3Page;
	}

	public Page getHomePage() {
		return homePage;
	}

	public void setHomePage(Page homePage) {
		this.homePage = homePage;
	}

	public Page getLevel1Page() {
		return level1Page;
	}

	public void setLevel1Page(Page level1Page) {
		this.level1Page = level1Page;
	}

	public Iterator<Page> getLevel1Pages() {
		Iterator<Page> childIterator = level1Page.listChildren();
		return childIterator;
	}

	public Iterator<Page> getLevel2Pages() {
		Iterator<Page> childIterator = level2Page.listChildren();
		return childIterator;
	}
	
	public Iterator<Page> getLevel3Pages() {
		Iterator<Page> childIterator = level3Page.listChildren();
		return childIterator;
	}
}