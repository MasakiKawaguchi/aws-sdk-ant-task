package com.AWSManagementAntTask.dao.model;

public interface IAWSDto {

	public String getIdentifier();

	public String toCSVHeader();

	public String toCSV();
}
