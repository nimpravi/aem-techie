package com.cts.accelerators.migration.ootbcomps.designdto;

import com.cts.accelerators.migration.ootbcomps.dto.CoreDTO;

public class UserInfoDTO extends CoreDTO{
	private String loginPage;
	private String profilePage;
	private String signupPage;

	/**
	 * @return the loginPage
	 */
	public String getLoginPage() {
		return loginPage;
	}

	/**
	 * @param loginPage
	 *            the loginPage to set
	 */
	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	/**
	 * @return the profilePage
	 */
	public String getProfilePage() {
		return profilePage;
	}

	/**
	 * @param profilePage
	 *            the profilePage to set
	 */
	public void setProfilePage(String profilePage) {
		this.profilePage = profilePage;
	}

	/**
	 * @return the signupPage
	 */
	public String getSignupPage() {
		return signupPage;
	}

	/**
	 * @param signupPage
	 *            the signupPage to set
	 */
	public void setSignupPage(String signupPage) {
		this.signupPage = signupPage;
	}
}
