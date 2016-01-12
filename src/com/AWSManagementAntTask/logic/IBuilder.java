package com.AWSManagementAntTask.logic;

import com.AWSManagementAntTask.dao.model.AWSMATDto;

public interface IBuilder {

	public void export();

	public void finish();

	public AWSMATDto getModel();
}
