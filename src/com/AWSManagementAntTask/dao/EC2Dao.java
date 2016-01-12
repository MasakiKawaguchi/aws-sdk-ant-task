package com.AWSManagementAntTask.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.AWSManagementAntTask.dao.model.AWSMATDto;
import com.AWSManagementAntTask.dao.model.EC2Dto;
import com.AWSManagementAntTask.dao.model.ElasticIPDto;
import com.AWSManagementAntTask.dao.model.IAWSDto;
import com.AWSManagementAntTask.dao.model.VolumeDto;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Address;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.Volume;
import com.amazonaws.services.ec2.model.VolumeAttachment;

public class EC2Dao {

	/** EC2クライアントインスタンス */
	private AmazonEC2 ec2;

	private AWSMATDto dto;

	public EC2Dao(AWSMATDto dto) {
		AWSCredentialsProvider provider = new ProfileCredentialsProvider(dto.getProfilename());
		ec2 = Region.getRegion(dto.getRegionid()).createClient(AmazonEC2Client.class, provider, new ClientConfiguration());
		this.dto = dto;
	}

	public AWSMATDto find() {
		findEC2();
		findVolume();
		return dto;
	}

	private AWSMATDto findEC2() {

		List<IAWSDto> dtolist = new ArrayList<IAWSDto>();
		for (Reservation res : ec2.describeInstances().getReservations()) {
			for (Instance ins : res.getInstances()) {
				EC2Dto dto = new EC2Dto();
				dto.setRegionName(this.dto.getRegionName());
				dto.setInstanceId(ins.getInstanceId());
				dto.setInstanceType(ins.getInstanceType());
				dto.setAvailabilityZone(ins.getPlacement().getAvailabilityZone());
				dto.setPlatform(ins.getPlatform());
				dto.setPrivateDnsName(ins.getPrivateDnsName());
				dto.setPrivateIpAddress(ins.getPrivateIpAddress());
				dto.setRootDeviceType(ins.getRootDeviceType());
				dto.setStatus(ins.getState().getName());
				dto.setLaunchTime(ins.getLaunchTime());
				dto.setOwnerId(res.getOwnerId());
				for (Tag tag : ins.getTags()) {
					if ("Name".equalsIgnoreCase(tag.getKey())) {
						dto.setName(tag.getValue());
					}
					//					if ("StartDate".equalsIgnoreCase(tag.getKey())) {
					//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					//						Date cdDate = null;
					//						try {
					//							cdDate = sdf.parse(tag.getValue());
					//						} catch (ParseException e) {
					//							domain.setWarnMsg("missing date format.");
					//						}
					//						domain.setStartDate(cdDate);
					//					}
					//					if ("EndDate".equalsIgnoreCase(tag.getKey())) {
					//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					//						Date cdDate = null;
					//						try {
					//							cdDate = sdf.parse(tag.getValue());
					//						} catch (ParseException e) {
					//							domain.setWarnMsg("missing date format.");
					//						}
					//						domain.setEndDate(cdDate);
					//					}
				}
				// エラーチェック
				//				if (domain.getEndDate() == null) {
				//					domain.setWarnMsg("require enddate.");
				//				}
				//				Date today = new Date();
				//				if (domain.getEndDate() != null && today.after(domain.getEndDate())
				//				        && StringUtils.equals("running", domain.getStatus())) {
				//					domain.setWarnMsg("instance expire.");
				//				}
				//System.out.println(domain.toString());
				dtolist.add(dto);
			}
		}
		this.dto.setEc2list(dtolist);
		return this.dto;
	}

	private AWSMATDto findVolume() {

		List<IAWSDto> dtolist = new ArrayList<IAWSDto>();
		for (Volume res : ec2.describeVolumes().getVolumes()) {
			VolumeDto dto = new VolumeDto();
			dto.setRegionName(this.dto.getRegionName());
			dto.setVolumeId(res.getVolumeId());
			dto.setVolumeType(res.getVolumeType());
			dto.setSize(res.getSize());
			dto.setState(res.getState());
			dto.setCreateTime(res.getCreateTime());
			dto.setAvailabilityZone(res.getAvailabilityZone());
			for (VolumeAttachment ins : res.getAttachments()) {
				dto.setInstanceId(ins.getInstanceId());
			}
			// エラーチェック
			if (StringUtils.equals("available", dto.getState())) {
				dto.setWarnMsg("not use this volume.");
			}
			for (IAWSDto awsdto : this.dto.getEc2list()) {
				EC2Dto ec2dto = (EC2Dto) awsdto;
				if (StringUtils.contains(dto.getInstanceId(), ec2dto.getInstanceId())) {
					dto.setInstanceName(ec2dto.getName());
				}
				if (StringUtils.contains(dto.getInstanceId(), ec2dto.getInstanceId())
				        && !StringUtils.equals("running", ec2dto.getStatus())) {
					dto.setWarnMsg("not use this volume's instance.");
				}
			}
			dtolist.add(dto);
		}
		this.dto.setVllist(dtolist);
		findElasticIp();
		return this.dto;
	}

	private AWSMATDto findElasticIp() {

		List<IAWSDto> dtolist = new ArrayList<IAWSDto>();
		for (Address res : ec2.describeAddresses().getAddresses()) {
			ElasticIPDto dto = new ElasticIPDto();
			dto.setRegionName(this.dto.getRegionName());
			dto.setPublicIp(res.getPublicIp());
			dto.setDomain(res.getDomain());
			dto.setAllocationId(res.getAllocationId());
			dto.setAssociationId(res.getAssociationId());
			dto.setInstanceId(res.getInstanceId());
			dto.setNetworkInterfaceId(res.getNetworkInterfaceId());
			dto.setNetworkInterfaceOwnerId(res.getNetworkInterfaceOwnerId());
			dto.setPrivateIpAddress(res.getPrivateIpAddress());
			for (IAWSDto awsdto : this.dto.getEc2list()) {
				EC2Dto ec2dto = (EC2Dto) awsdto;
				if (StringUtils.contains(dto.getInstanceId(), ec2dto.getInstanceId())) {
					dto.setInstanceName(ec2dto.getName());
				}
			}
			dtolist.add(dto);
		}
		this.dto.setEiplist(dtolist);
		return this.dto;
	}
}
