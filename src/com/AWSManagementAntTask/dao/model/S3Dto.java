package com.AWSManagementAntTask.dao.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class S3Dto implements IAWSDto {

	private String name;

	private String identifier = "S3";

	private String regionName;

	private Date createTime;

	private int filecnt;

	private int filesize;

	private String owner;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getFilecnt() {
		return filecnt;
	}

	public void setFilecnt(int filecnt) {
		this.filecnt = filecnt;
	}

	public int getFilesize() {
		return filesize;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toCSVHeader() {
		return "name, regionName, filecnt, filesize, createTime";
	}

	@Override
	public String toCSV() {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		return name + "," + regionName + "," + filecnt + "," + filesize + "," + fm.format(createTime);
	}

	@Override
	public String getIdentifier() {

		return identifier;
	}
}
