package com.AWSManagementAntTask.dao.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class EC2Dto implements IAWSDto {

	public EC2Dto() {}

	private String identifier = "EC2";

	private String warnMsg;

	private String regionName;

	private String instanceId;

	private String name;

	private String platform;

	private String instanceType;

	private String rootDeviceType;

	private String status;

	private String privateDnsName;

	private String privateIpAddress;

	private Date launchTime;

	private String availabilityZone;

	private String ownerId;

	private Date startDate;

	private Date endDate;

	@Override
	public String getIdentifier() {
		return identifier;
	}

	public String getWarnMsg() {
		if (warnMsg == null) {
			return StringUtils.EMPTY;
		}
		return warnMsg;
	}

	public void setWarnMsg(String warnMsg) {
		this.warnMsg = warnMsg;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	public String getRootDeviceType() {
		return rootDeviceType;
	}

	public void setRootDeviceType(String rootDeviceType) {
		this.rootDeviceType = rootDeviceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPrivateDnsName() {
		return privateDnsName;
	}

	public void setPrivateDnsName(String privateDnsName) {
		this.privateDnsName = privateDnsName;
	}

	public String getPrivateIpAddress() {
		return privateIpAddress;
	}

	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}

	public Date getLaunchTime() {
		return launchTime;
	}

	public void setLaunchTime(Date launchTime) {
		this.launchTime = launchTime;
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toCSVHeader() {
		return new StringBuffer()
		        .append("warnMsg").append(",")
		        .append("name").append(",")
		        .append("regionName").append(",")
		        .append("launchTime").append(",")
		        .append("endDate").append(",")
		        .append("status").append(",")
		        .append("instanceId").append(",")
		        .append("instanceType").append(",")
		        .append("platform").append(",")
		        .append("instanceId").append(",")
		        .append("ownerId").toString();
	}

	@Override
	public String toCSV() {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		return new StringBuffer()
		        .append(getWarnMsg()).append(",")
		        .append(name).append(",")
		        .append(regionName).append(",")
		        .append(fm.format(launchTime)).append(",")
		        .append(endDate).append(",")
		        .append(status).append(",")
		        .append(instanceId).append(",")
		        .append(instanceType).append(",")
		        .append(platform).append(",")
		        .append(instanceId).append(",")
		        .append(ownerId).toString();
	}
}
