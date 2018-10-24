package com.cts.accelerators.migration.ootbcomps.designdto;

import com.cts.accelerators.migration.ootbcomps.dto.CoreDTO;

public class ToolbarDTO extends CoreDTO{
	private String absParent;
	private String toolbar;
	private String list;

	/**
	 * @return the absParent
	 */
	public String getAbsParent() {
		return absParent;
	}

	/**
	 * @param absParent
	 *            the absParent to set
	 */
	public void setAbsParent(String absParent) {
		this.absParent = absParent;
	}

	/**
	 * @return the toolbar
	 */
	public String getToolbar() {
		return toolbar;
	}

	/**
	 * @param toolbar
	 *            the toolbar to set
	 */
	public void setToolbar(String toolbar) {
		this.toolbar = toolbar;
	}

	/**
	 * @return the list
	 */
	public String getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(String list) {
		this.list = list;
	}

}
