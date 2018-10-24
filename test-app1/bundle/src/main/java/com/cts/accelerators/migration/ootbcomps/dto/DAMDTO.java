package com.cts.accelerators.migration.ootbcomps.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DamContents")
@XmlSeeAlso({CoreDTO.class, DamPropertyDTO.class})
public class DAMDTO extends CoreDTO {

	@XmlElement
	private String sourcePath;

	@XmlElement
	private String fileName;

	@XmlElement
	private String destinationPath;
	
	@XmlElement
	private String port;
	
	@XmlElement
	private String host;
	
	@XmlElement
	private String userName;
	
	@XmlElement
	private String password;
	
	@XmlElement
	private String proxyport;
	
	@XmlElement
	private String proxyaddress;
	
	@XmlElement
	private String id;

	@XmlElement(name = "CustomProperties")
	private List<DamPropertyDTO> customMetaProperties;

	/**
	 * @return the sourcePath
	 */
	public String getSourcePath() {
		return sourcePath;
	}

	/**
	 * @param sourcePath the sourcePath to set
	 */
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the destinationPath
	 */
	public String getDestinationPath() {
		return destinationPath;
	}

	/**
	 * @param destinationPath the destinationPath to set
	 */
	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the proxyport
	 */
	public String getProxyport() {
		return proxyport;
	}

	/**
	 * @param proxyport the proxyport to set
	 */
	public void setProxyport(String proxyport) {
		this.proxyport = proxyport;
	}

	/**
	 * @return the proxyaddress
	 */
	public String getProxyaddress() {
		return proxyaddress;
	}

	/**
	 * @param proxyaddress the proxyaddress to set
	 */
	public void setProxyaddress(String proxyaddress) {
		this.proxyaddress = proxyaddress;
	}

	/**
	 * @return the customMetaProperties
	 */
	public List<DamPropertyDTO> getCustomMetaProperties() {
		return customMetaProperties;
	}

	/**
	 * @param customMetaProperties the customMetaProperties to set
	 */
	public void setCustomMetaProperties(List<DamPropertyDTO> customMetaProperties) {
		this.customMetaProperties = customMetaProperties;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
