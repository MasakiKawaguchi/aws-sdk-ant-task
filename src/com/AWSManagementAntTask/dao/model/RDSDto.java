package com.AWSManagementAntTask.dao.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RDSDto implements IAWSDto {

	public RDSDto() {}

	private String identifier = "RDS";

	private String dBInstanceIdentifier;

	private String regionName;

	private String dBName;

	private String dBInstanceStatus;

	private Date instanceCreateTime;

	private String engine;

	private String engineVersion;

	private String licenseModel;

	private String storageType;

	private Integer allocatedStorage;

	private String dBInstanceClass;

	private String masterUsername;

	public String getDBInstanceIdentifier() {
		return dBInstanceIdentifier;
	}

	public void setDBInstanceIdentifier(String dBInstanceIdentifier) {
		this.dBInstanceIdentifier = dBInstanceIdentifier;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getDBName() {
		return dBName;
	}

	public void setDBName(String dBName) {
		this.dBName = dBName;
	}

	public String getDBInstanceStatus() {
		return dBInstanceStatus;
	}

	public void setDBInstanceStatus(String dBInstanceStatus) {
		this.dBInstanceStatus = dBInstanceStatus;
	}

	public Date getInstanceCreateTime() {
		return instanceCreateTime;
	}

	public void setInstanceCreateTime(Date instanceCreateTime) {
		this.instanceCreateTime = instanceCreateTime;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getEngineVersion() {
		return engineVersion;
	}

	public void setEngineVersion(String engineVersion) {
		this.engineVersion = engineVersion;
	}

	public String getLicenseModel() {
		return licenseModel;
	}

	public void setLicenseModel(String licenseModel) {
		this.licenseModel = licenseModel;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public Integer getAllocatedStorage() {
		return allocatedStorage;
	}

	public void setAllocatedStorage(Integer allocatedStorage) {
		this.allocatedStorage = allocatedStorage;
	}

	public String getDBInstanceClass() {
		return dBInstanceClass;
	}

	public void setDBInstanceClass(String dBInstanceClass) {
		this.dBInstanceClass = dBInstanceClass;
	}

	public String getMasterUsername() {
		return masterUsername;
	}

	public void setMasterUsername(String masterUsername) {
		this.masterUsername = masterUsername;
	}

	@Override
	public String toCSVHeader() {
		return "dBInstanceIdentifier, regionName, engine, dBInstanceStatus, instanceCreateTime";
	}

	@Override
	public String toCSV() {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		return dBInstanceIdentifier + "," + regionName + "," + engine + "," + dBInstanceStatus + "," + fm.format(instanceCreateTime);
	}

	@Override
	public String getIdentifier() {

		return identifier;
	}

}
