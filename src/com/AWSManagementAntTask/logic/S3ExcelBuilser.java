package com.AWSManagementAntTask.logic;

import com.AWSManagementAntTask.common.ExcelWriter;
import com.AWSManagementAntTask.dao.S3Dao;
import com.AWSManagementAntTask.dao.model.AWSMATDto;

public class S3ExcelBuilser implements IBuilder {

	private S3Dao s3;
	private ExcelWriter writer;
	private AWSMATDto dto;

	public S3ExcelBuilser(AWSMATDto dto) {
		this.dto = dto;
		s3 = new S3Dao(dto);
		s3.downLoadS3File();
		writer = new ExcelWriter(dto.getFilename());
	}

	@Override
	public void export() {
		writer.write(dto);
	}

	@Override
	public void finish() {
		writer.finish();
		s3.upLoadS3File();
	}

	@Override
	public AWSMATDto getModel() {

		return dto;
	}
}
