package com.AWSManagementAntTask.dao.model;

public class ElasticIPDto implements IAWSDto {

	public ElasticIPDto() {}

	private String identifier = "ElasticIP";

	private String warnMsg;

	private String regionName;

	private String publicIp;

	private String domain;

	private String allocationId;

	private String associationId;

	private String instanceId;

	private String instanceName;

	private String networkInterfaceId;

	private String networkInterfaceOwnerId;

	private String privateIpAddress;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getWarnMsg() {
		return warnMsg;
	}

	public void setWarnMsg(String warnMsg) {
		this.warnMsg = warnMsg;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public String getAssociationId() {
		return associationId;
	}

	public void setAssociationId(String associationId) {
		this.associationId = associationId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getNetworkInterfaceId() {
		return networkInterfaceId;
	}

	public void setNetworkInterfaceId(String networkInterfaceId) {
		this.networkInterfaceId = networkInterfaceId;
	}

	public String getNetworkInterfaceOwnerId() {
		return networkInterfaceOwnerId;
	}

	public void setNetworkInterfaceOwnerId(String networkInterfaceOwnerId) {
		this.networkInterfaceOwnerId = networkInterfaceOwnerId;
	}

	public String getPrivateIpAddress() {
		return privateIpAddress;
	}

	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	@Override
	public String toCSVHeader() {
		// TODO Auto-generated method stub
		return new StringBuffer().append("regionName").append(",")
		        .append("publicIp").append(",")
		        .append("instanceName").append(",")
		        .append("domain").append(",").toString();
	}

	@Override
	public String toCSV() {
		return new StringBuffer().append(regionName).append(",")
		        .append(publicIp).append(",")
		        .append(instanceName).append(",")
		        .append(domain).append(",").toString();
	}
}
