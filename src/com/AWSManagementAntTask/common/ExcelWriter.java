package com.AWSManagementAntTask.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.tools.ant.BuildException;

import com.AWSManagementAntTask.dao.model.AWSMATDto;
import com.AWSManagementAntTask.dao.model.EC2Dto;
import com.AWSManagementAntTask.dao.model.ElasticIPDto;
import com.AWSManagementAntTask.dao.model.IAWSDto;
import com.AWSManagementAntTask.dao.model.RDSDto;
import com.AWSManagementAntTask.dao.model.S3Dto;
import com.AWSManagementAntTask.dao.model.VolumeDto;

public class ExcelWriter {

	private String filename;
	private HSSFWorkbook wb;
	private Integer ROW_LIMIT = 9999;
	//	private Integer COL_ADMIN_STATE = 2;

	private Integer COL_EC2_ADMIN_STATUS = 18;
	private Integer COL_EC2_REGION_NAME = 2;
	private Integer COL_EC2_INSTANCE_ID = 14;
	private Integer COL_EC2_INSTANCE_NAME = 3;
	private Integer COL_EC2_INSTANCE_TYPE = 15;
	private Integer COL_EC2_STATUS = 16;
	private Integer COL_EC2_LAUNCH_DATE = 17;

	private Integer COL_RDS_ADMIN_STATUS = 10;
	private Integer COL_RDS_REGION_NAME = 2;
	private Integer COL_RDS_DB_IDENTIFIER = 3;

	private Integer COL_S3_ADMIN_STATUS = 10;
	private Integer COL_S3_REGION_NAME = 2;
	private Integer COL_S3_BUCKET_NAME = 3;

	public ExcelWriter(String filename) throws BuildException {
		this.filename = filename;
		POIFSFileSystem filein;
		try {
			filein = new POIFSFileSystem(new FileInputStream(filename + ".xls"));
			wb = new HSSFWorkbook(filein);
		} catch (FileNotFoundException e) {
			try {
				filein = new POIFSFileSystem(new FileInputStream(filename + "_template.xls"));
				wb = new HSSFWorkbook(filein);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				throw new BuildException(ex);
			} catch (IOException ex) {
				ex.printStackTrace();
				throw new BuildException(ex);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}

	public void finish() throws BuildException {
		try {
			wb.write(new FileOutputStream(filename + ".xls"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new BuildException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}

	public void write(AWSMATDto dto) throws BuildException {
		writeEC2(dto);
		writeVolume(dto);
		writeElasticIP(dto);
		writeRDS(dto);
		writeS3(dto);
	}

	private void writeEC2(AWSMATDto dto) throws BuildException {

		Integer keyColumnNum = COL_EC2_INSTANCE_NAME;
		String keyColumnNam = "EC2名";
		if (dto.getEc2list().isEmpty()) {
			return;
		}
		/* ※個別変更箇所 */
		HSSFSheet sheet = wb.getSheet(dto.getEc2list().get(0).getIdentifier());
		// 行頭・行末検索
		Integer startrow = 0;
		Integer endrow = 0;
		for (Integer i = 0; i < ROW_LIMIT; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			HSSFCell cell = row.getCell(keyColumnNum);
			if (cell != null && StringUtils.equals(keyColumnNam, cell.getStringCellValue())) {
				startrow = i + 1;
				break;
			}
		}
		// 追加・削除リスト作成
		List<IAWSDto> addllist = new ArrayList<IAWSDto>();
		List<Integer> dellist = new ArrayList<Integer>();
		Map<String, Integer> rows = new HashMap<String, Integer>();
		for (Integer i = startrow; i < ROW_LIMIT; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(keyColumnNum);
			String keyStr = cell.getStringCellValue().trim();
			if (StringUtils.isBlank(keyStr)) {
				endrow = i - 1;
				break;
			}
			rows.put(keyStr, i);
			Boolean flg = false;
			/* ※個別変更箇所 */
			for (IAWSDto awsdto : dto.getEc2list()) {
				EC2Dto spdto = (EC2Dto) awsdto;
				if (StringUtils.equals(spdto.getName(), keyStr)) {
					setCellValueforEC2(row, spdto);
					flg = true;
				}
			}
			if (!flg) {
				dellist.add(i);
			}
		}
		/* ※個別変更箇所 */
		for (IAWSDto awsdto : dto.getEc2list()) {
			EC2Dto spdto = (EC2Dto) awsdto;
			if (rows.get(spdto.getName()) == null) {
				addllist.add(awsdto);
			}
		}
		/* ※個別変更箇所 */
		for (IAWSDto awsdto : addllist) {
			EC2Dto spdto = (EC2Dto) awsdto;
			++endrow;
			HSSFRow row = sheet.getRow(endrow);
			setCellValueforEC2(row, spdto);
		}
		removeRow(dellist, sheet, COL_EC2_STATUS);
	}

	private void setCellValueforEC2(HSSFRow row, EC2Dto dto) {
		HSSFCell cell = row.getCell(COL_EC2_ADMIN_STATUS);
		cell.setCellValue(dto.getWarnMsg());
		cell = row.getCell(COL_EC2_REGION_NAME);
		cell.setCellValue(dto.getRegionName());
		cell = row.getCell(COL_EC2_INSTANCE_ID);
		cell.setCellValue(dto.getInstanceId());
		cell = row.getCell(COL_EC2_INSTANCE_NAME);
		cell.setCellValue(dto.getName());
		cell = row.getCell(COL_EC2_INSTANCE_TYPE);
		cell.setCellValue(dto.getInstanceType());
		cell = row.getCell(COL_EC2_STATUS);
		cell.setCellValue(dto.getStatus());
		cell = row.getCell(COL_EC2_LAUNCH_DATE);
		cell.setCellValue(new SimpleDateFormat(Const.FT_YYYYMMDD).format(dto.getLaunchTime()));
	}

	private void removeRow(List<Integer> dellist, HSSFSheet sheet, Integer COL_ADMIN_STATE) {
		Integer cnt = 0;
		for (Integer i : dellist) {
			Integer correntrow = i + cnt;
			HSSFRow row = sheet.getRow(correntrow);
			HSSFCell cell = row.getCell(COL_ADMIN_STATE);
			cell.setCellValue("not existing.");
			//sheet.removeRow(row);
			//sheet.shiftRows(correntrow + 1, correntrow + 1 + ROW_LIMIT, -1);
			//--cnt;
		}
	}

	private void writeVolume(AWSMATDto dto) throws BuildException {

		Integer COL_ADMIN_STATUS = 2;
		Integer keyColumnNum = COL_ADMIN_STATUS + 2;
		String keyColumnNam = "VolumeId";
		if (dto.getVllist().isEmpty()) {
			return;
		}
		/* ※個別変更箇所 */
		HSSFSheet sheet = wb.getSheet(dto.getVllist().get(0).getIdentifier());
		// 行頭・行末検索
		Integer startrow = 0;
		Integer endrow = 0;
		for (Integer i = 0; i < ROW_LIMIT; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			HSSFCell cell = row.getCell(keyColumnNum);
			if (cell != null && StringUtils.equals(keyColumnNam, cell.getStringCellValue())) {
				startrow = i + 1;
				break;
			}
		}
		// 追加・削除リスト作成
		List<IAWSDto> addllist = new ArrayList<IAWSDto>();
		List<Integer> dellist = new ArrayList<Integer>();
		Map<String, Integer> rows = new HashMap<String, Integer>();
		for (Integer i = startrow; i < ROW_LIMIT; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(keyColumnNum);
			String keyStr = cell.getStringCellValue().trim();
			if (StringUtils.isBlank(keyStr)) {
				endrow = i - 1;
				break;
			}
			rows.put(keyStr, i);
			Boolean flg = false;
			/* ※個別変更箇所 */
			for (IAWSDto awsdto : dto.getVllist()) {
				VolumeDto spdto = (VolumeDto) awsdto;
				if (StringUtils.equals(spdto.getVolumeId(), keyStr)) {
					HSSFCell upcell = row.getCell(COL_ADMIN_STATUS);
					upcell.setCellValue(spdto.getWarnMsg());
					upcell = row.getCell(COL_ADMIN_STATUS + 3);
					upcell.setCellValue(spdto.getVolumeType());
					upcell = row.getCell(COL_ADMIN_STATUS + 4);
					upcell.setCellValue(spdto.getSize());
					upcell = row.getCell(COL_ADMIN_STATUS + 5);
					upcell.setCellValue(spdto.getInstanceId());
					upcell = row.getCell(COL_ADMIN_STATUS + 6);
					upcell.setCellValue(spdto.getInstanceName());
					upcell = row.getCell(COL_ADMIN_STATUS + 7);
					upcell.setCellValue(spdto.getState());
					flg = true;
				}
			}
			if (!flg) {
				dellist.add(i);
			}
		}
		/* ※個別変更箇所 */
		for (IAWSDto awsdto : dto.getVllist()) {
			VolumeDto spdto = (VolumeDto) awsdto;
			if (rows.get(spdto.getVolumeId()) == null) {
				addllist.add(awsdto);
			}
		}
		/* ※個別変更箇所 */
		for (IAWSDto awsdto : addllist) {
			VolumeDto spdto = (VolumeDto) awsdto;
			++endrow;
			HSSFRow row = sheet.getRow(endrow);
			HSSFCell cell = row.getCell(COL_ADMIN_STATUS);
			cell.setCellValue(spdto.getWarnMsg());
			cell = row.getCell(COL_ADMIN_STATUS + 1);
			cell.setCellValue(spdto.getRegionName());
			cell = row.getCell(COL_ADMIN_STATUS + 2);
			cell.setCellValue(spdto.getVolumeId());
			cell = row.getCell(COL_ADMIN_STATUS + 3);
			cell.setCellValue(spdto.getVolumeType());
			cell = row.getCell(COL_ADMIN_STATUS + 4);
			cell.setCellValue(spdto.getSize());
			cell = row.getCell(COL_ADMIN_STATUS + 5);
			cell.setCellValue(spdto.getInstanceId());
			cell = row.getCell(COL_ADMIN_STATUS + 6);
			cell.setCellValue(spdto.getInstanceName());
			cell = row.getCell(COL_ADMIN_STATUS + 7);
			cell.setCellValue(spdto.getState());
			cell = row.getCell(COL_ADMIN_STATUS + 8);
			cell.setCellValue(new SimpleDateFormat(Const.FT_YYYYMMDD).format(spdto.getCreateTime()));
		}
		removeRow(dellist, sheet, COL_ADMIN_STATUS);
	}

	private void writeElasticIP(AWSMATDto dto) throws BuildException {

		Integer COL_ADMIN_STATUS = 2;
		Integer keyColumnNum = COL_ADMIN_STATUS + 2;
		String keyColumnNam = "PublicIP";
		if (dto.getVllist().isEmpty()) {
			return;
		}
		/* ※個別変更箇所 */
		HSSFSheet sheet = wb.getSheet(dto.getEiplist().get(0).getIdentifier());
		// 行頭・行末検索
		Integer startrow = 0;
		Integer endrow = 0;
		for (Integer i = 0; i < ROW_LIMIT; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			HSSFCell cell = row.getCell(keyColumnNum);
			if (cell != null && StringUtils.equals(keyColumnNam, cell.getStringCellValue())) {
				startrow = i + 1;
				break;
			}
		}
		// 追加・削除リスト作成
		List<IAWSDto> addllist = new ArrayList<IAWSDto>();
		List<Integer> dellist = new ArrayList<Integer>();
		Map<String, Integer> rows = new HashMap<String, Integer>();
		for (Integer i = startrow; i < ROW_LIMIT; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(keyColumnNum);
			String keyStr = cell.getStringCellValue().trim();
			if (StringUtils.isBlank(keyStr)) {
				endrow = i - 1;
				break;
			}
			rows.put(keyStr, i);
			Boolean flg = false;
			/* ※個別変更箇所 */
			for (IAWSDto awsdto : dto.getEiplist()) {
				ElasticIPDto spdto = (ElasticIPDto) awsdto;
				if (StringUtils.equals(spdto.getPublicIp(), keyStr)) {
					HSSFCell upcell = row.getCell(COL_ADMIN_STATUS);
					upcell.setCellValue(spdto.getWarnMsg());
					upcell = row.getCell(COL_ADMIN_STATUS + 3);
					upcell.setCellValue(spdto.getInstanceId());
					upcell = row.getCell(COL_ADMIN_STATUS + 4);
					upcell.setCellValue(spdto.getInstanceName());
					flg = true;
				}
			}
			if (!flg) {
				dellist.add(i);
			}
		}
		/* ※個別変更箇所 */
		for (IAWSDto awsdto : dto.getEiplist()) {
			ElasticIPDto spdto = (ElasticIPDto) awsdto;
			if (rows.get(spdto.getPublicIp()) == null) {
				addllist.add(awsdto);
			}
		}
		/* ※個別変更箇所 */
		for (IAWSDto awsdto : addllist) {
			ElasticIPDto spdto = (ElasticIPDto) awsdto;
			++endrow;
			HSSFRow row = sheet.getRow(endrow);
			HSSFCell cell = row.getCell(COL_ADMIN_STATUS);
			cell.setCellValue(spdto.getWarnMsg());
			cell = row.getCell(COL_ADMIN_STATUS + 1);
			cell.setCellValue(spdto.getRegionName());
			cell = row.getCell(COL_ADMIN_STATUS + 2);
			cell.setCellValue(spdto.getPublicIp());
			cell = row.getCell(COL_ADMIN_STATUS + 3);
			cell.setCellValue(spdto.getInstanceId());
			cell = row.getCell(COL_ADMIN_STATUS + 4);
			cell.setCellValue(spdto.getInstanceName());
		}
		removeRow(dellist, sheet, COL_ADMIN_STATUS);
	}

	private void writeRDS(AWSMATDto dto) throws BuildException {

		Integer keyColumnNum = COL_RDS_DB_IDENTIFIER;
		String keyColumnNam = "DB識別子";
		if (dto.getRdslist().isEmpty()) {
			return;
		}
		/* ※個別変更箇所 */
		HSSFSheet sheet = wb.getSheet(dto.getRdslist().get(0).getIdentifier());
		// 行頭・行末検索
		Integer startrow = 0;
		Integer endrow = 0;
		for (Integer i = 0; i < ROW_LIMIT; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			HSSFCell cell = row.getCell(keyColumnNum);
			if (cell != null && StringUtils.equals(keyColumnNam, cell.getStringCellValue())) {
				startrow = i + 1;
				break;
			}
		}
		// 追加・削除リスト作成
		List<IAWSDto> addllist = new ArrayList<IAWSDto>();
		List<Integer> dellist = new ArrayList<Integer>();
		Map<String, Integer> rows = new HashMap<String, Integer>();
		for (Integer i = startrow; i < ROW_LIMIT; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(keyColumnNum);
			String keyStr = cell.getStringCellValue().trim();
			if (StringUtils.isBlank(keyStr)) {
				endrow = i - 1;
				break;
			}
			rows.put(keyStr, i);
			Boolean flg = false;
			/* ※個別変更箇所 */
			for (IAWSDto awsdto : dto.getRdslist()) {
				RDSDto ｓｐdto = (RDSDto) awsdto;
				if (StringUtils.equals(ｓｐdto.getDBInstanceIdentifier(), keyStr)) {
					setCellValueforRDS(row, ｓｐdto);
					flg = true;
				}
			}
			if (!flg) {
				dellist.add(i);
			}
		}
		/* ※個別変更箇所 */
		for (IAWSDto awsdto : dto.getRdslist()) {
			RDSDto spdto = (RDSDto) awsdto;
			if (rows.get(spdto.getDBInstanceIdentifier()) == null) {
				addllist.add(awsdto);
			}
		}
		/* ※個別変更箇所 */
		for (IAWSDto awsdto : addllist) {
			RDSDto spdto = (RDSDto) awsdto;
			++endrow;
			HSSFRow row = sheet.getRow(endrow);
			setCellValueforRDS(row, spdto);
		}
		removeRow(dellist, sheet, COL_RDS_ADMIN_STATUS + 2);
	}

	private void setCellValueforRDS(HSSFRow row, RDSDto dto) {
		HSSFCell cell = row.getCell(COL_RDS_ADMIN_STATUS);
		//cell = row.getCell(COL_ADMIN_STATE);
		//cell.setCellValue(dto.getWarnMsg());
		cell = row.getCell(COL_RDS_REGION_NAME);
		cell.setCellValue(dto.getRegionName());
		cell = row.getCell(COL_RDS_DB_IDENTIFIER);
		cell.setCellValue(dto.getDBInstanceIdentifier());
		cell = row.getCell(COL_RDS_ADMIN_STATUS + 2);
		cell.setCellValue(dto.getEngine());
		cell = row.getCell(COL_RDS_ADMIN_STATUS + 3);
		cell.setCellValue(dto.getDBInstanceStatus());
		cell = row.getCell(COL_RDS_ADMIN_STATUS + 4);
		cell.setCellValue(new SimpleDateFormat(Const.FT_YYYYMMDD).format(dto.getInstanceCreateTime()));
	}

	private void writeS3(AWSMATDto dto) throws BuildException {

		/* ※個別変更箇所 */
		Integer keyColumnNum = COL_S3_BUCKET_NAME;
		String keyColumnNam = "バケット名";
		if (dto.getS3list().isEmpty()) {
			return;
		}
		/* ※個別変更箇所 */
		HSSFSheet sheet = wb.getSheet(dto.getS3list().get(0).getIdentifier());
		// 行頭・行末検索
		Integer startrow = 0;
		Integer endrow = 0;
		for (Integer i = 0; i < ROW_LIMIT; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			HSSFCell cell = row.getCell(keyColumnNum);
			if (cell != null && StringUtils.equals(keyColumnNam, cell.getStringCellValue())) {
				startrow = i + 1;
				break;
			}
		}
		// 追加・削除リスト作成
		List<IAWSDto> addllist = new ArrayList<IAWSDto>();
		List<Integer> dellist = new ArrayList<Integer>();
		Map<String, Integer> rows = new HashMap<String, Integer>();
		for (Integer i = startrow; i < ROW_LIMIT; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(keyColumnNum);
			String keyStr = cell.getStringCellValue().trim();
			if (StringUtils.isBlank(keyStr)) {
				endrow = i - 1;
				break;
			}
			rows.put(keyStr, i);
			Boolean flg = false;
			/* ※個別変更箇所 */
			for (IAWSDto awsdto : dto.getS3list()) {
				S3Dto spdto = (S3Dto) awsdto;
				if (StringUtils.equals(spdto.getName(), keyStr)) {
					setCellValueforS3(row, spdto);
					flg = true;
				}
			}
			if (!flg) {
				dellist.add(i);
			}
		}
		/* ※個別変更箇所 */
		for (IAWSDto awsdto : dto.getS3list()) {
			S3Dto ec2domain = (S3Dto) awsdto;
			if (rows.get(ec2domain.getName()) == null) {
				addllist.add(awsdto);
			}
		}
		/* ※個別変更箇所 */
		for (IAWSDto awsdto : addllist) {
			S3Dto spdto = (S3Dto) awsdto;
			++endrow;
			HSSFRow row = sheet.getRow(endrow);
			setCellValueforS3(row, spdto);
		}
		removeRow(dellist, sheet, COL_S3_ADMIN_STATUS + 4);
	}

	private void setCellValueforS3(HSSFRow row, S3Dto spdto) {
		HSSFCell cell = row.getCell(COL_S3_ADMIN_STATUS);
		//cell = row.getCell(COL_ADMIN_STATE);
		//cell.setCellValue(spdto.getWarnMsg());
		cell = row.getCell(COL_S3_REGION_NAME);
		cell.setCellValue(spdto.getRegionName());
		cell = row.getCell(COL_S3_BUCKET_NAME);
		cell.setCellValue(spdto.getName());
		cell = row.getCell(COL_S3_ADMIN_STATUS + 1);
		cell.setCellValue(spdto.getFilecnt());
		cell = row.getCell(COL_S3_ADMIN_STATUS + 2);
		cell.setCellValue(spdto.getFilesize());
		CellStyle style = cell.getCellStyle();
		DataFormat format = wb.createDataFormat();
		style.setDataFormat(format.getFormat("#,##0"));
		cell.setCellStyle(style);
		cell = row.getCell(COL_S3_ADMIN_STATUS + 3);
		cell.setCellValue(new SimpleDateFormat(Const.FT_YYYYMMDD).format(spdto.getCreateTime()));
	}
}
