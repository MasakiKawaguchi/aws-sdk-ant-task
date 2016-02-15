package com.AWSManagementAntTask.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;

import com.AWSManagementAntTask.dao.model.AWSMATDto;
import com.AWSManagementAntTask.dao.model.IAWSDto;
import com.AWSManagementAntTask.dao.model.S3Dto;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Dao {

	private AmazonS3 s3;

	private AWSMATDto dto;

	public S3Dao(AWSMATDto dto) {
		this.dto = dto;
		s3 = new AmazonS3Client(new ProfileCredentialsProvider(dto.getProfilename()));
	}

	public AWSMATDto find() {

		List<IAWSDto> dtolist = new ArrayList<IAWSDto>();
		for (Bucket bucket : s3.listBuckets()) {
			S3Dto dto = new S3Dto();
			dto.setName(bucket.getName());
			dto.setRegionName(getRegionName(s3.getBucketLocation(bucket.getName())));
			dto = setFileCnt(dto, s3.listObjects(bucket.getName()));
			dto.setCreateTime(bucket.getCreationDate());
			dto.setOwner(bucket.getOwner().getDisplayName());
			dtolist.add(dto);
		}
		this.dto.setS3list(dtolist);
		return this.dto;
	}

	private String getRegionName(String regionid) {
		Iterator<Regions> keylist = this.dto.getRegionmap().keySet().iterator();
		while (keylist.hasNext()) {
			Regions key = keylist.next();
			if (StringUtils.equals(key.getName(), regionid)) {
				return this.dto.getRegionmap().get(key);
			}
		}
		return null;
	}

	private S3Dto setFileCnt(S3Dto domain, ObjectListing objlisting) {
		int filecnt = 0;
		int filesize = 0;
		for (S3ObjectSummary obj : objlisting.getObjectSummaries()) {
			++filecnt;
			filesize += obj.getSize();
		}
		domain.setFilecnt(filecnt);
		domain.setFilesize(filesize);
		return domain;
	}

	public void downLoadS3File() throws BuildException {

		S3Object s3obj;
		exestBaucketName();
		try {
			s3obj = s3.getObject(dto.getBucketname(), dto.getFilename() + ".xls");
		} catch (AmazonS3Exception e) {
			return;
		}
		try {
			BufferedInputStream input = new BufferedInputStream(s3obj.getObjectContent());
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(dto.getFilename() + ".xls"));
			byte buf[] = new byte[256];
			int len;
			while ((len = input.read(buf)) != -1) {
				output.write(buf, 0, len);
			}
			output.flush();
			output.close();
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new BuildException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}

	public void upLoadS3File() throws BuildException {

		try {
			File file = new File(dto.getFilename() + ".xls");
			s3.putObject(dto.getBucketname(), dto.getFilename() + ".xls", file);
		} catch (AmazonClientException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}

	private void exestBaucketName() {
		Boolean flg = false;
		for (Bucket bucket : s3.listBuckets()) {
			if (StringUtils.equals(dto.getBucketname(), bucket.getName())) {
				flg = true;
			}
		}
		if (!flg) {
			s3.createBucket(dto.getBucketname(), Region.AP_Tokyo);
		}
	}
}
