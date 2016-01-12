package com.AWSManagementAntTask.logic;

import com.AWSManagementAntTask.common.ExcelWriter;
import com.AWSManagementAntTask.dao.model.AWSMATDto;

public class ExcelBuilder implements IBuilder {

	private ExcelWriter writer;

	private AWSMATDto dto;

	public ExcelBuilder(AWSMATDto dto) {
		this.dto = dto;
		writer = new ExcelWriter(dto.getFilename());
	}

	@Override
	public void export() {
		writer.write(dto);
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
