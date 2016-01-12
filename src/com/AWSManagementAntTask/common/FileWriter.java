package com.AWSManagementAntTask.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;

import com.AWSManagementAntTask.dao.model.AWSMATDto;
import com.AWSManagementAntTask.dao.model.IAWSDto;

public class FileWriter {

	private PrintWriter pw;

	public FileWriter(AWSMATDto dto) throws BuildException {
		try {
			pw = new PrintWriter(new BufferedWriter(new java.io.FileWriter(new File(dto.getFilename() + ".csv"))));
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	public void write(List<IAWSDto> domainlist) throws BuildException {

		Boolean flg = true;
		for (IAWSDto domain : domainlist) {
			if (flg) {
				pw.println(StringUtils.EMPTY);
				pw.println(domain.getIdentifier());
				pw.println(domain.toCSVHeader());
				flg = false;
			}
			pw.println(domain.toCSV());
		}
	}

	public void finish() {
		pw.close();
	}
}
