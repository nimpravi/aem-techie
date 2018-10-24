/**
 * 
 */
package com.cts.accelerators.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.core.dto.AcceleratorServiceRequest;
import com.cts.accelerators.core.dto.AcceleratorServiceResponse;
import com.cts.accelerators.core.dto.ReplicationServiceRequest;
import com.cts.accelerators.core.dto.ReplicationServiceResponse;
import com.cts.accelerators.core.services.AcceleratorService;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentFilter;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationEvent;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.ReplicationStatus;

/**
 * @author 432087
 * Replication Service Implementation
 */
@Component(immediate = true, metatype = true)
@Service(ReplicationServiceImpl.class)
public class ReplicationServiceImpl implements AcceleratorService {
	@Reference
	private com.day.cq.replication.Replicator replicator;

	private Session jcrSession;
	private ReplicationOptions replicationOptions;
	private boolean activationFlag = false;
	private Map<String, Boolean> replicationStatusMap = new HashMap<String, Boolean>();
	private Map<String, Boolean> deactivationStatusMap = new HashMap<String, Boolean>();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReplicationServiceImpl.class);
	private static final String CLASS_NAME = ReplicationServiceImpl.class
			.getName();

	public String getRepositoryName() {
		return null;
	}

	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException {
		String methodName = "execute";
		LOGGER.info(" || " + methodName + " || START");
		ReplicationServiceResponse replicationServiceResponse = new ReplicationServiceResponse();
		jcrSession = ConnectionManager.getSession();
		if (serviceRequest instanceof ReplicationServiceRequest) {
			ReplicationServiceRequest replicationServiceRequest = (ReplicationServiceRequest) serviceRequest;
			String replicationAction = replicationServiceRequest
					.getReplicationAction();
			if (replicationAction
					.equalsIgnoreCase(AcceleratorGenericConstants.REPLICATION_TYPE_ACTIVATE)) {
				activationFlag = true;
				replicateResource(replicationServiceRequest);
				replicationServiceResponse
						.setReplicationStatusMap(replicationStatusMap);
			} else if (replicationAction
					.equalsIgnoreCase(AcceleratorGenericConstants.REPLICATION_TYPE_DEACTIVATE)) {
				activationFlag = false;
				replicateResource(replicationServiceRequest);
				replicationServiceResponse
						.setDeactivationStatusMap(deactivationStatusMap);
			} else {
				throw new AcceleratorException(
						AcceleratorFaultCode.INVALID_REPLICATION_ACTION,
						CLASS_NAME, methodName);
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return replicationServiceResponse;
	}

	public Map<String, Boolean> replicateResource(
			ReplicationServiceRequest replicationServiceRequest)
			throws AcceleratorException {
		String methodName = "replicateResource";
		LOGGER.info(" || " + methodName + " || START");
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Start of || activate() method||");
			}
			String replicationPath = replicationServiceRequest
					.getReplicationPath();
			if (StringUtils.isEmpty(replicationPath)) {
				throw new AcceleratorException(
						AcceleratorFaultCode.EMPTY_REPLICATION_PATH,
						CLASS_NAME, methodName);
			}
			final String replicationAgent = replicationServiceRequest
					.getReplicationAgent();
			if (null != replicationAgent
					&& StringUtils.isNotEmpty(replicationAgent)) {
				replicationOptions = new ReplicationOptions();
				replicationOptions.setFilter(new AgentFilter() {
					public boolean isIncluded(final Agent agent) {
						return replicationAgent.equals(agent.getId());
					}
				});
			} else {
				LOGGER.error(CLASS_NAME + " || " + methodName
						+ " || Replication Agent does not exist");
			}
			Node startNode = JcrUtils.getNodeIfExists(replicationPath,
					jcrSession);
			if (replicationServiceRequest.isReplicateChildNodes()) {
				replicateNode(startNode);
			} else {
				if (startNode != null) {
					String startNodePath = startNode.getPath();
					if (null != replicationOptions) {
						if (activationFlag) {
							replicator.replicate(jcrSession,
									ReplicationActionType.ACTIVATE,
									startNodePath, replicationOptions);
						} else {
							replicator.replicate(jcrSession,
									ReplicationActionType.DEACTIVATE,
									startNodePath, replicationOptions);
						}
					} else {
						if (activationFlag) {
							replicator.replicate(jcrSession,
									ReplicationActionType.ACTIVATE,
									startNodePath);
						} else {
							replicator.replicate(jcrSession,
									ReplicationActionType.DEACTIVATE,
									startNodePath);
						}
					}
					replicationStatusMap.put(startNode.getPath(), true);
				}
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(replicationStatusMap.toString());
				LOGGER.debug("End of || activate() method||");
			}
		} catch (RepositoryException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION,
					CLASS_NAME, methodName,
					e.getCause());
		} catch (ReplicationException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPLICATION_EXCEPTION,
					CLASS_NAME, methodName,
					e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return replicationStatusMap;
	}

	public void replicateNode(Node startNode) throws AcceleratorException {
		String methodName = "replicateNode";
		LOGGER.info(" || " + methodName + " || START");
		try {
			/*
			 * if (startNode.hasNode("jcr:content")) { Node jcrContent =
			 * startNode.getNode("jcr:content");
			 */
			if (startNode != null) {
				String startNodePath = startNode.getPath();
				if (null != replicationOptions) {
					if (activationFlag) {
						replicator.replicate(jcrSession,
								ReplicationActionType.ACTIVATE, startNodePath,
								replicationOptions);
					} else {
						replicator.replicate(jcrSession,
								ReplicationActionType.DEACTIVATE,
								startNodePath, replicationOptions);
					}
				} else {
					if (activationFlag) {
						replicator.replicate(jcrSession,
								ReplicationActionType.ACTIVATE, startNodePath);
					} else {
						replicator
								.replicate(jcrSession,
										ReplicationActionType.DEACTIVATE,
										startNodePath);
					}
				}
				replicationStatusMap.put(startNodePath, true);
			}
			// }
			Node child;
			for (NodeIterator children = startNode.getNodes(); children
					.hasNext(); replicateNode(child)) {
				child = children.nextNode();
			}
		}

		catch (RepositoryException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION,
					CLASS_NAME, methodName,
					e.getCause());
		} catch (ReplicationException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPLICATION_EXCEPTION,
					CLASS_NAME, methodName,
					e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
	}
}
