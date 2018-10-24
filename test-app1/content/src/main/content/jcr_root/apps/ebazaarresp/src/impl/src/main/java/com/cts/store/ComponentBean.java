package com.cts.store;

public class ComponentBean {
	
	private String componentGroup;
	private String componentDescription;
	private String iconPath;
	private String thumbnailPath;
	private String nodePath;
	private String wikiLink;
	
	public String getWikiLink() {
		return wikiLink;
	}
	public void setWikiLink(String wikiLink) {
		this.wikiLink = wikiLink;
	}
	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	
	public String getComponentGroup() {
		return componentGroup;
	}
	public void setComponentGroup(String componentGroup) {
		this.componentGroup = componentGroup;
	}
	public String getComponentDescription() {
		return componentDescription;
	}
	public void setComponentDescription(String componentDescription) {
		this.componentDescription = componentDescription;
	}
	

}
