/**
 * 
 */
package com.cts.accelerators.core.dto;

import java.util.Map;

/**
 * @author 432087
 * 
 */
public class ReplicationServiceResponse implements AcceleratorServiceResponse {
	private Map<String, Boolean> replicationStatusMap;
	private Map<String, Boolean> deactivationStatusMap;

	/**
	 * @return the deactivationStatusMap
	 */
	public Map<String, Boolean> getDeactivationStatusMap() {
		return deactivationStatusMap;
	}

	/**
	 * @param deactivationStatusMap
	 *            the deactivationStatusMap to set
	 */
	public void setDeactivationStatusMap(
			Map<String, Boolean> deactivationStatusMap) {
		this.deactivationStatusMap = deactivationStatusMap;
	}

	/**
	 * @return the replicationStatusMap
	 */
	public Map<String, Boolean> getReplicationStatusMap() {
		return replicationStatusMap;
	}

	/**
	 * @param replicationStatusMap
	 *            the replicationStatusMap to set
	 */
	public void setReplicationStatusMap(
			Map<String, Boolean> replicationStatusMap) {
		this.replicationStatusMap = replicationStatusMap;
	}
}
