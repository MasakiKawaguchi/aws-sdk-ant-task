package com.AWSManagementAntTask.dao.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Regions;

public class AWSMATDto {

	private Map<Regions, String> regionmap;

	private Regions regionid;

	private String profilename = "default";

	private String bucketname = "aws-admin-table";

	private String filename = "AWS管理表";

	private List<IAWSDto> ec2list;

	private List<IAWSDto> rdslist;

	private List<IAWSDto> s3list;

	private List<IAWSDto> vllist;

	private List<IAWSDto> eiplist;

	public AWSMATDto() {
		this.regionmap = new HashMap<Regions, String>();
		this.regionmap.put(Regions.AP_NORTHEAST_1, "東京リージョン");
		this.regionmap.put(Regions.AP_SOUTHEAST_1, "シンガポールリージョン");
		this.regionmap.put(Regions.AP_SOUTHEAST_2, "シドニーリージョン");
		this.regionmap.put(Regions.US_EAST_1, "バージニア北部リージョン");
		this.regionmap.put(Regions.US_WEST_1, "北カリフォルニア");
		this.regionmap.put(Regions.US_WEST_2, "オレゴンリージョン");
		this.regionmap.put(Regions.SA_EAST_1, "サンパウロリージョン");
	}

	public Map<Regions, String> getRegionmap() {
		return regionmap;
	}

	public void setRegionmap(Map<Regions, String> regionmap) {
		this.regionmap = regionmap;
	}

	public Regions getRegionid() {
		return regionid;
	}

	public void setRegionid(Regions regionid) {
		this.regionid = regionid;
	}

	public String getProfilename() {
		return profilename;
	}

	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}

	public String getRegionName() {
		return regionmap.get(getRegionid());
	}

	public List<IAWSDto> getEc2list() {
		return ec2list;
	}

	public String getBucketname() {
		return bucketname;
	}

	public void setBucketname(String bucketname) {
		this.bucketname = bucketname;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setEc2list(List<IAWSDto> ec2list) {
		if (this.ec2list == null) {
			this.ec2list = ec2list;
			return;
		}
		this.ec2list.addAll(ec2list);
	}

	public List<IAWSDto> getRdslist() {
		return rdslist;
	}

	public void setRdslist(List<IAWSDto> rdslist) {
		if (this.rdslist == null) {
			this.rdslist = rdslist;
			return;
		}
		this.rdslist.addAll(rdslist);
	}

	public List<IAWSDto> getS3list() {
		return s3list;
	}

	public void setS3list(List<IAWSDto> s3list) {
		if (this.s3list == null) {
			this.s3list = s3list;
			return;
		}
		this.s3list.addAll(s3list);
	}

	public List<IAWSDto> getVllist() {
		return vllist;
	}

	public void setVllist(List<IAWSDto> vllist) {
		if (this.vllist == null) {
			this.vllist = vllist;
			return;
		}
		this.vllist.addAll(vllist);
	}

	public List<IAWSDto> getEiplist() {
		return eiplist;
	}

	public void setEiplist(List<IAWSDto> eiplist) {
		if (this.eiplist == null) {
			this.eiplist = eiplist;
			return;
		}
		this.eiplist.addAll(eiplist);
	}
}
