/**
 * 
 */
package com.cts.accelerators.core.dto;

/**
 * @author 432087
 * 
 */
public class ReplicationServiceRequest implements AcceleratorServiceRequest {

	private String replicationAgent;
	private String replicationPath;
	private boolean replicateChildNodes;
	private String replicationAction;

	/**
	 * @return the replicationAction
	 */
	public String getReplicationAction() {
		return replicationAction;
	}

	/**
	 * @param replicationAction
	 *            the replicationAction to set
	 */
	public void setReplicationAction(String replicationAction) {
		this.replicationAction = replicationAction;
	}

	/**
	 * @return the replicationAgent
	 */
	public String getReplicationAgent() {
		return replicationAgent;
	}

	/**
	 * @param replicationAgent
	 *            the replicationAgent to set
	 */
	public void setReplicationAgent(String replicationAgent) {
		this.replicationAgent = replicationAgent;
	}

	/**
	 * @return the replicationPath
	 */
	public String getReplicationPath() {
		return replicationPath;
	}

	/**
	 * @param replicationPath
	 *            the replicationPath to set
	 */
	public void setReplicationPath(String replicationPath) {
		this.replicationPath = replicationPath;
	}

	/**
	 * @return the replicateChildNodes
	 */
	public boolean isReplicateChildNodes() {
		return replicateChildNodes;
	}

	/**
	 * @param replicateChildNodes
	 *            the replicateChildNodes to set
	 */
	public void setReplicateChildNodes(boolean replicateChildNodes) {
		this.replicateChildNodes = replicateChildNodes;
	}

}
