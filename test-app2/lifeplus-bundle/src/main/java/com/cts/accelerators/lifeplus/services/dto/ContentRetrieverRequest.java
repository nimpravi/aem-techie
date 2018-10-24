package com.cts.accelerators.lifeplus.services.dto;

import java.util.List;

public class ContentRetrieverRequest {
	
	private String personalizationContentTable;
	private String personalizationType;
	private String key;
	private String personalizationGroup;
	private String contentBasedOnUserGroupTable;
	private String[] joinTables;
	private String orderBy;
	
	private String memberId;
	private String interest;
	private int testimonialCount;
	private String memberType;
	private String status;
	private String description;
	
	/**
	 * @return the personalizationContentTable
	 */
	public String getPersonalizationContentTable() {
		return personalizationContentTable;
	}
	/**
	 * @param personalizationContentTable the personalizationContentTable to set
	 */
	public void setPersonalizationContentTable(String personalizationContentTable) {
		this.personalizationContentTable = personalizationContentTable;
	}
	/**
	 * @return the personalizationType
	 */
	public String getPersonalizationType() {
		return personalizationType;
	}
	/**
	 * @param personalizationType the personalizationType to set
	 */
	public void setPersonalizationType(String personalizationType) {
		this.personalizationType = personalizationType;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the personalizationGroup
	 */
	public String getPersonalizationGroup() {
		return personalizationGroup;
	}
	/**
	 * @param personalizationGroup the personalizationGroup to set
	 */
	public void setPersonalizationGroup(String personalizationGroup) {
		this.personalizationGroup = personalizationGroup;
	}
	/**
	 * @return the contentBasedOnUserGroupTable
	 */
	public String getContentBasedOnUserGroupTable() {
		return contentBasedOnUserGroupTable;
	}
	/**
	 * @param contentBasedOnUserGroupTable the contentBasedOnUserGroupTable to set
	 */
	public void setContentBasedOnUserGroupTable(String contentBasedOnUserGroupTable) {
		this.contentBasedOnUserGroupTable = contentBasedOnUserGroupTable;
	}
	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}
	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	/**
	 * @return the joinTables
	 */
	public String[] getJoinTables() {
		return joinTables;
	}
	/**
	 * @param joinTables the joinTables to set
	 */
	public void setJoinTables(String[] joinTables) {
		this.joinTables = joinTables;
	}
	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the interest
	 */
	public String getInterest() {
		return interest;
	}
	/**
	 * @param interest the interest to set
	 */
	public void setInterest(String interest) {
		this.interest = interest;
	}
	/**
	 * @return the testimonialCount
	 */
	public int getTestimonialCount() {
		return testimonialCount;
	}
	/**
	 * @param testimonialCount the testimonialCount to set
	 */
	public void setTestimonialCount(int testimonialCount) {
		this.testimonialCount = testimonialCount;
	}
	/**
	 * @return the memberType
	 */
	public String getMemberType() {
		return memberType;
	}
	/**
	 * @param memberType the memberType to set
	 */
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
	