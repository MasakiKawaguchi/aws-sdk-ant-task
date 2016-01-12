package com.AWSManagementAntTask.dao;

import java.util.ArrayList;
import java.util.List;

import com.AWSManagementAntTask.dao.model.AWSMATDto;
import com.AWSManagementAntTask.dao.model.IAWSDto;
import com.AWSManagementAntTask.dao.model.RDSDto;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.DBInstance;

public class RDSDao {

	/** EC2クライアントインスタンス */
	private AmazonRDS rds;

	private AWSMATDto dto;

	public RDSDao(AWSMATDto dto) {
		AWSCredentialsProvider provider = new ProfileCredentialsProvider(dto.getProfilename());
		rds = Region.getRegion(dto.getRegionid()).createClient(AmazonRDSClient.class, provider, new ClientConfiguration());
		this.dto = dto;
	}

	public AWSMATDto find() {

		List<IAWSDto> dtolist = new ArrayList<IAWSDto>();
		for (DBInstance res : rds.describeDBInstances().getDBInstances()) {
			RDSDto dto = new RDSDto();
			dto.setRegionName(this.dto.getRegionName());
			dto.setDBInstanceIdentifier(res.getDBInstanceIdentifier());
			dto.setDBName(res.getDBName());
			dto.setDBInstanceStatus(res.getDBInstanceStatus());
			dto.setInstanceCreateTime(res.getInstanceCreateTime());
			dto.setEngine(res.getEngine());
			dto.setEngineVersion(res.getEngineVersion());
			dto.setLicenseModel(res.getLicenseModel());
			dto.setStorageType(res.getStorageType());
			dto.setAllocatedStorage(res.getAllocatedStorage());
			dto.setDBInstanceClass(res.getDBInstanceClass());
			dto.setMasterUsername(res.getMasterUsername());
			dtolist.add(dto);
		}
		this.dto.setRdslist(dtolist);
		return this.dto;
	}
}
