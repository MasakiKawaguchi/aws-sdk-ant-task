package com.AWSManagementAntTask.logic;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.AWSManagementAntTask.dao.EC2Dao;
import com.AWSManagementAntTask.dao.RDSDao;
import com.AWSManagementAntTask.dao.S3Dao;
import com.AWSManagementAntTask.dao.model.AWSMATDto;
import com.amazonaws.regions.Regions;

public class Director {
	private IBuilder builder;

	public Director(IBuilder builder) {
		this.builder = builder;
	}

	public void construct() {

		AWSMATDto dto = builder.getModel();
		Iterator<Regions> keylist = dto.getRegionmap().keySet().iterator();
		while (keylist.hasNext()) {
			Regions key = keylist.next();
			dto.setRegionid(key);
			System.out.println(StringUtils.rightPad(">> Processing Region is : " + key, 100, "-"));
			dto = new EC2Dao(dto).find();
			dto = new RDSDao(dto).find();
		}
		dto = new S3Dao(dto).find();
		builder.export();
		System.out.println(StringUtils.rightPad(">> Output processing.", 100, "-"));
		builder.finish();
		System.out.println(StringUtils.rightPad(">> Output processing is normal end.", 100, "-"));
	}
}
