package com.AWSManagementAntTask.dao.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class VolumeDto implements IAWSDto {

	public VolumeDto() {}

	private String identifier = "Volume";

	private String warnMsg;

	private String volumeId;

	private String volumeType;

	private Integer size;

	private String state;

	private Date createTime;

	private String availabilityZone;

	private String regionName;

	private String instanceId;

	private String instanceName;

	private String name;

	public String getWarnMsg() {
		if (warnMsg == null) {
			return StringUtils.EMPTY;
		}
		return warnMsg;
	}

	public void setWarnMsg(String warnMsg) {
		this.warnMsg = warnMsg;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

	public String getVolumeType() {
		return volumeType;
	}

	public void setVolumeType(String volumeType) {
		this.volumeType = volumeType;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
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

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toCSVHeader() {

		return "warnMsg,volumeType,size(GB),state,createTime,regionName,instanceId";
	}

	@Override
	public String toCSV() {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		return getWarnMsg() + "," + volumeId + "," + volumeType + "," + size + "," + state + "," + fm.format(createTime) + "," + regionName + ","
		        + instanceId;
	}

	@Override
	public String getIdentifier() {

		return identifier;
	}
}
