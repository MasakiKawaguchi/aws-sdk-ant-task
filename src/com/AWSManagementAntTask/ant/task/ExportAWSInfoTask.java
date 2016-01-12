package com.AWSManagementAntTask.ant.task;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.AWSManagementAntTask.dao.model.AWSMATDto;
import com.AWSManagementAntTask.logic.CSVBuilder;
import com.AWSManagementAntTask.logic.Director;
import com.AWSManagementAntTask.logic.ExcelBuilder;
import com.AWSManagementAntTask.logic.IBuilder;
import com.AWSManagementAntTask.logic.S3ExcelBuilser;

public class ExportAWSInfoTask extends Task {

	private String format = "excel";

	public void execute() throws BuildException {

		IBuilder builder;
		builder = new ExcelBuilder(new AWSMATDto());
		if (StringUtils.equals(format, "s3excel")) {
			builder = new S3ExcelBuilser(new AWSMATDto());
		}
		if (StringUtils.equals(format, "excel")) {
			builder = new ExcelBuilder(new AWSMATDto());
		}
		if (StringUtils.equals(format, "csv")) {
			builder = new CSVBuilder(new AWSMATDto());
		}
		Director directore = new Director(builder);
		directore.construct();
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
