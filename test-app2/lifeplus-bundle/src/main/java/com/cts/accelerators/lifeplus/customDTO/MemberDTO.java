package com.cts.accelerators.lifeplus.customDTO;

public class MemberDTO {

	private String email;
	private String password;
	private String securityQuestion;
	private String securityAnswer;
	private String member_type;
	private String profile_type;
	
	/**
	 * @return the member_type
	 */
	public String getMember_type() {
		return member_type;
	}
	/**
	 * @param member_type the member_type to set
	 */
	public void setMember_type(String member_type) {
		this.member_type = member_type;
	}
	/**
	 * @return the profile_type
	 */
	public String getProfile_type() {
		return profile_type;
	}
	/**
	 * @param profile_type the profile_type to set
	 */
	public void setProfile_type(String profile_type) {
		this.profile_type = profile_type;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	
}
