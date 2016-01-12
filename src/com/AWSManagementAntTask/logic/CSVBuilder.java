package com.AWSManagementAntTask.logic;

import com.AWSManagementAntTask.common.FileWriter;
import com.AWSManagementAntTask.dao.model.AWSMATDto;

public class CSVBuilder implements IBuilder {

	private FileWriter writer;
	private AWSMATDto dto;

	public CSVBuilder(AWSMATDto dto) {
		this.dto = dto;
		this.writer = new FileWriter(dto);
	}

	@Override
	public void export() {
		writer.write(dto.getEc2list());
		writer.write(dto.getVllist());
		writer.write(dto.getRdslist());
		writer.write(dto.getS3list());
	}

	@Override
	public void finish() {
		writer.finish();
	}

	@Override
	public AWSMATDto getModel() {
		return dto;
	}

}
