package com.cts.accelerators.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.helpers.ContentImporterHelper;
import com.cts.accelerators.migration.helpers.DamUploadHelper;
import com.cts.accelerators.migration.helpers.ReportGeneratorHelper;
import com.cts.accelerators.migration.ootbcomps.designdto.BreadcrumbDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.DesignTitleDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.ToolbarDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.TopnavDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.UserInfoDTO;
import com.cts.accelerators.migration.ootbcomps.dto.*;
import com.cts.accelerators.migration.services.dto.DamUploadServiceRequest;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: AcceleratorExcelUtils
 *         Description: Utility class for Migration Project Dependency: none
 * 
 * 
 * 
 */

public class AcceleratorExcelUtils {

	public final static Logger LOGGER = LoggerFactory
			.getLogger(AcceleratorExcelUtils.class);

	public static CoreDTO setTextDTO(String componentType, String componentId,
			Workbook wb) {
		String methodName = "setTextDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		TextDTO textDTO = new TextDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		textDTO.setNodeName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.TEXT_COMPONENT_HEADERS[0])
						.getColumn(), idIndex).getContents());
		textDTO.setResourceType(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.TEXT_COMPONENT_HEADERS[1])
						.getColumn(), idIndex).getContents());
		textDTO.setText(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.TEXT_COMPONENT_HEADERS[2])
						.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return textDTO;
	}

	public static CoreDTO setLoginDTO(String componentType, String componentId,
			Workbook wb) {
		String methodName = "setLoginDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		LoginDTO loginDTO = new LoginDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		loginDTO.setNodeName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LOGIN_COMPONENT_HEADERS[0])
						.getColumn(), idIndex).getContents());
		loginDTO.setSectionLabel(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LOGIN_COMPONENT_HEADERS[1])
						.getColumn(), idIndex).getContents());
		loginDTO.setUsernameLabel(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LOGIN_COMPONENT_HEADERS[2])
						.getColumn(), idIndex).getContents());
		loginDTO.setPasswordLabel(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LOGIN_COMPONENT_HEADERS[3])
						.getColumn(), idIndex).getContents());
		loginDTO.setLoginLabel(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LOGIN_COMPONENT_HEADERS[4])
						.getColumn(), idIndex).getContents());
		loginDTO.setRedirectTo(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LOGIN_COMPONENT_HEADERS[5])
						.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return loginDTO;
	}

	public static CoreDTO setFlashDTO(String componentType, String componentId,
			Workbook wb) {
		String methodName = "setFlashDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		FlashDTO flashDTO = new FlashDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		flashDTO.setNodeName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.FLASH_COMPONENT_HEADERS[0])
						.getColumn(), idIndex).getContents());
		flashDTO.setFileReference(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.FLASH_COMPONENT_HEADERS[1])
						.getColumn(), idIndex).getContents());
		LOGGER.debug(" || "
				+ methodName
				+ " || height || "
				+ Integer
						.parseInt(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.FLASH_COMPONENT_HEADERS[2])
												.getColumn(), idIndex)
								.getContents()));
		flashDTO.setHeight(Integer.parseInt(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.FLASH_COMPONENT_HEADERS[2])
						.getColumn(), idIndex).getContents()));
		LOGGER.debug(" || " + methodName + " || height1 || "
				+ flashDTO.getHeight());
		flashDTO.setWidth(Integer.parseInt(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.FLASH_COMPONENT_HEADERS[3])
						.getColumn(), idIndex).getContents()));
		String imageDtoId = sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.FLASH_COMPONENT_HEADERS[4])
						.getColumn(), idIndex).getContents();
		ImageDTO imageDTO = (ImageDTO) setImageDTO("ImageComponent",
				imageDtoId, wb);
		flashDTO.setHtml5SmartImageDTO(imageDTO);
		flashDTO.setWmode(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.FLASH_COMPONENT_HEADERS[5])
						.getColumn(), idIndex).getContents());
		flashDTO.setBgColor(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.FLASH_COMPONENT_HEADERS[6])
						.getColumn(), idIndex).getContents());
		flashDTO.setFlashVersion(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.FLASH_COMPONENT_HEADERS[7])
						.getColumn(), idIndex).getContents());
		flashDTO.setAttrs(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.FLASH_COMPONENT_HEADERS[8])
						.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return flashDTO;
	}

	public static CoreDTO setProfileDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setProfileDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ProfileDTO profileDTO = new ProfileDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		profileDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setJcr_title(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setDeleteTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setCols(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setWidth(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[4])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setConstraintMessage(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[5])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setConstraintType(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[6])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setRequired(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[7])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setRequiredMessage(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[8])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setJcr_description(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[9])
										.getColumn(), idIndex).getContents());

		SizeFieldDTO sizeFieldDTO = new SizeFieldDTO();
		sizeFieldDTO
				.setRows(Integer
						.parseInt(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[10])
												.getColumn(), idIndex)
								.getContents()));
		sizeFieldDTO
				.setCols(Integer
						.parseInt(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[11])
												.getColumn(), idIndex)
								.getContents()));

		profileDTO.setSizeFieldDTO(sizeFieldDTO);
		profileDTO
				.setDefaultValue(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[12])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setHonoricPrefixTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[13])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[14])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setGivenNameTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[15])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setMiddleNameTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[16])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setFamilyNameTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[17])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setHonoricSuffixTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[18])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[19])
										.getColumn(), idIndex).getContents());
		profileDTO
				.setConfirmationTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PROFILE_COMPONENT_HEADERS[20])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return profileDTO;
	}

	public static CoreDTO setSearchDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setSearchDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		SearchDTO searchDTO = new SearchDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		searchDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setSearchIn(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setSearchButtonText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setStatisticsText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setNoResultsText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[4])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setSpellcheckText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[5])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setSimilarPagesText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[6])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setRelatedSearchesText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[7])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setSearchTrendsText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[8])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setResultPagesText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[9])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setPreviousText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[10])
										.getColumn(), idIndex).getContents());
		searchDTO
				.setNextText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SEARCH_COMPONENT_HEADERS[11])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return searchDTO;
	}

	public static CoreDTO setTextImageDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setTextImageDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		TextImageDTO textImageDTO = new TextImageDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		textImageDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TEXT_IMAGE_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		String imageDtoId = sh
				.getCell(
						sh.findCell(
								AcceleratorGenericConstants.TEXT_IMAGE_COMPONENT_HEADERS[1])
								.getColumn(), idIndex).getContents();
		String textDtoId = sh
				.getCell(
						sh.findCell(
								AcceleratorGenericConstants.TEXT_IMAGE_COMPONENT_HEADERS[2])
								.getColumn(), idIndex).getContents();

		ImageDTO imageDTO = (ImageDTO) setImageDTO("ImageComponent",
				imageDtoId, wb);
		TextDTO textDTO = (TextDTO) setTextDTO("TextComponent", textDtoId, wb);

		textImageDTO.setImageDTO(imageDTO);
		textImageDTO.setTextDTO(textDTO);

		LOGGER.info(" || " + methodName + " || END");
		return textImageDTO;
	}

	public static CoreDTO setVideoDTO(String componentType, String componentId,
			Workbook wb) {
		String methodName = "setVideoDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		VideoDTO videoDTO = new VideoDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		videoDTO.setNodeName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.VIDEO_COMPONENT_HEADERS[0])
						.getColumn(), idIndex).getContents());
		videoDTO.setFile(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.VIDEO_COMPONENT_HEADERS[1])
						.getColumn(), idIndex).getContents());
		videoDTO.setHeight(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.VIDEO_COMPONENT_HEADERS[2])
						.getColumn(), idIndex).getContents());
		videoDTO.setWidth(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.VIDEO_COMPONENT_HEADERS[3])
						.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return videoDTO;
	}

	public static CoreDTO setArchiveDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setArchiveDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ArchiveDTO archiveDTO = new ArchiveDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		archiveDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.ARCHIVE_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		archiveDTO
				.setDateFormat(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.ARCHIVE_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return archiveDTO;
	}

	public static CoreDTO setChartDTO(String componentType, String componentId,
			Workbook wb) {
		String methodName = "setChartDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ChartDTO chartDTO = new ChartDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		chartDTO.setNodeName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.CHART_COMPONENT_HEADERS[0])
						.getColumn(), idIndex).getContents());
		chartDTO.setChartData(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.CHART_COMPONENT_HEADERS[1])
						.getColumn(), idIndex).getContents());
		chartDTO.setChartType(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.CHART_COMPONENT_HEADERS[2])
						.getColumn(), idIndex).getContents());
		chartDTO.setChartAlt(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.CHART_COMPONENT_HEADERS[3])
						.getColumn(), idIndex).getContents());
		chartDTO.setChartWidth(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.CHART_COMPONENT_HEADERS[4])
						.getColumn(), idIndex).getContents());
		chartDTO.setChartHeight(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.CHART_COMPONENT_HEADERS[5])
						.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return chartDTO;
	}

	public static CoreDTO setBreadcrumbDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setBreadcrumbDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		BreadcrumbDTO breadcrumbDTO = new BreadcrumbDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		breadcrumbDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.BREADCRUMB_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		breadcrumbDTO
				.setAbsParent(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.BREADCRUMB_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		breadcrumbDTO
				.setRelParent(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.BREADCRUMB_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		breadcrumbDTO
				.setDelim(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.BREADCRUMB_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());
		breadcrumbDTO
				.setTrail(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.BREADCRUMB_COMPONENT_HEADERS[4])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return breadcrumbDTO;
	}

	public static CoreDTO setDesignTitleDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setDesignTitleDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		DesignTitleDTO designTitleDTO = new DesignTitleDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		designTitleDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.DESIGN_TITLE_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		designTitleDTO
				.setDefaultType(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.DESIGN_TITLE_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return designTitleDTO;
	}

	public static CoreDTO setToolbarDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setToolbarDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ToolbarDTO toolbarDTO = new ToolbarDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		toolbarDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TOOLBAR_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		toolbarDTO
				.setAbsParent(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TOOLBAR_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		toolbarDTO
				.setToolbar(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TOOLBAR_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		toolbarDTO
				.setList(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TOOLBAR_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return toolbarDTO;
	}

	public static CoreDTO setTopnavDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setTopnavDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		TopnavDTO topnavDTO = new TopnavDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		topnavDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TOPNAV_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		topnavDTO
				.setAbsParent(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TOPNAV_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return topnavDTO;
	}

	public static CoreDTO setUserInfoDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setUserInfoDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		userInfoDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.USER_INFO_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		userInfoDTO
				.setLoginPage(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.USER_INFO_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		userInfoDTO
				.setProfilePage(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.USER_INFO_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		userInfoDTO
				.setSignupPage(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.USER_INFO_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return userInfoDTO;
	}

	public static CoreDTO setCarouselDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setCarouselDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		CarouselDTO carouselDTO = new CarouselDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		carouselDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setPlaySpeed(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setTransTime(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setControlsType(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setListFrom(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[4])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setOrderBy(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[5])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setParentPage(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[6])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setLimit(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[7])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setPages(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[8])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setSearchIn(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[9])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setQuery(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[10])
										.getColumn(), idIndex).getContents());
		carouselDTO
				.setSavedquery(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CAROUSEL_COMPONENT_HEADERS[11])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return carouselDTO;
	}

	public static CoreDTO setDownloadDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setDownloadDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		DownloadDTO downloadDTO = new DownloadDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		downloadDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.DOWNLOAD_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		downloadDTO
				.setJcr_description(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.DOWNLOAD_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		downloadDTO
				.setFile(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.DOWNLOAD_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		downloadDTO
				.setFileName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.DOWNLOAD_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());
		downloadDTO
				.setFileReference(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.DOWNLOAD_COMPONENT_HEADERS[4])
										.getColumn(), idIndex).getContents());
		downloadDTO
				.setAutoUploadDelay(Integer
						.parseInt(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.DOWNLOAD_COMPONENT_HEADERS[5])
												.getColumn(), idIndex)
								.getContents()));

		LOGGER.info(" || " + methodName + " || END");
		return downloadDTO;
	}

	public static CoreDTO setExternalDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setExternalDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ExternalDTO externalDTO = new ExternalDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		externalDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.EXTERNAL_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		externalDTO
				.setTarget(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.EXTERNAL_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		externalDTO
				.setPassparams(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.EXTERNAL_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		externalDTO
				.setWidthheight(Integer
						.parseInt(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.EXTERNAL_COMPONENT_HEADERS[3])
												.getColumn(), idIndex)
								.getContents()));

		LOGGER.info(" || " + methodName + " || END");
		return externalDTO;
	}

	public static CoreDTO setImageDTO(String componentType, String componentId,
			Workbook wb) {
		String methodName = "setImageDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ImageDTO imageDTO = new ImageDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		imageDTO.setNodeName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[0])
						.getColumn(), idIndex).getContents());
		imageDTO.setFile(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[1])
						.getColumn(), idIndex).getContents());
		imageDTO.setImageCrop(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[2])
						.getColumn(), idIndex).getContents());
		imageDTO.setFileName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[3])
						.getColumn(), idIndex).getContents());
		String imageRefId = sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[4])
						.getColumn(), idIndex).getContents();
		String imageRefPath = "";
		try {
			Map<String, String> map = ReportGeneratorHelper
					.getPropertiesMap(AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH
							+ AcceleratorGenericConstants.DAM_FILES_MAPPING_PROPERTIES);
			imageRefPath = map.get(imageRefId);
		} catch (AcceleratorException e) {
			LOGGER.error(" || " + methodName + " || AcceleratorException || "
					+ e.getMessage());
		}
		imageDTO.setFileReference(imageRefPath);
		imageDTO.setImageRotate(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[5])
						.getColumn(), idIndex).getContents());
		imageDTO.setJcr_title(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[6])
						.getColumn(), idIndex).getContents());
		imageDTO.setAlt(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[7])
						.getColumn(), idIndex).getContents());
		imageDTO.setLinkURL(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[8])
						.getColumn(), idIndex).getContents());
		imageDTO.setJcr_description(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[9])
						.getColumn(), idIndex).getContents());
		imageDTO.setSize(sh
				.getCell(
						sh.findCell(
								AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[10])
								.getColumn(), idIndex).getContents());
		imageDTO.setHeight(sh
				.getCell(
						sh.findCell(
								AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[11])
								.getColumn(), idIndex).getContents());
		imageDTO.setWidth(sh
				.getCell(
						sh.findCell(
								AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[12])
								.getColumn(), idIndex).getContents());
		imageDTO.setImageText(sh
				.getCell(
						sh.findCell(
								AcceleratorGenericConstants.IMAGE_COMPONENT_HEADERS[13])
								.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return imageDTO;
	}

	public static CoreDTO setListChildrenDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setListChildrenDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ListChildrenDTO listChildrenDTO = new ListChildrenDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		listChildrenDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.LIST_CHILDREN_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		listChildrenDTO
				.setListroot(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.LIST_CHILDREN_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return listChildrenDTO;
	}

	public static CoreDTO setListDTO(String componentType, String componentId,
			Workbook wb) {
		String methodName = "setListDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ListDTO listDTO = new ListDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		listDTO.setNodeName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[0])
						.getColumn(), idIndex).getContents());
		listDTO.setListFrom(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[1])
						.getColumn(), idIndex).getContents());
		listDTO.setDisplayAs(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[2])
						.getColumn(), idIndex).getContents());
		listDTO.setOrderBy(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[3])
						.getColumn(), idIndex).getContents());
		listDTO.setLimit(Integer.parseInt(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[4])
						.getColumn(), idIndex).getContents()));
		listDTO.setFeedEnabled(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[5])
						.getColumn(), idIndex).getContents());
		listDTO.setPageMax(Integer.parseInt(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[6])
						.getColumn(), idIndex).getContents()));
		listDTO.setParentPage(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[7])
						.getColumn(), idIndex).getContents());
		listDTO.setAncestorPage(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[8])
						.getColumn(), idIndex).getContents());
		listDTO.setPages(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[9])
						.getColumn(), idIndex).getContents());
		listDTO.setFieldConfig(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[10])
						.getColumn(), idIndex).getContents());
		listDTO.setWidth(Integer.parseInt(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[11])
						.getColumn(), idIndex).getContents()));
		listDTO.setQuery(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[12])
						.getColumn(), idIndex).getContents());
		listDTO.setSavedquery(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[13])
						.getColumn(), idIndex).getContents());
		listDTO.setTagsSearchRoot(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[14])
						.getColumn(), idIndex).getContents());
		listDTO.setTags(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[15])
						.getColumn(), idIndex).getContents());
		listDTO.setTagsMatch(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.LIST_COMPONENT_HEADERS[16])
						.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return listDTO;
	}

	public static CoreDTO setEntryTextDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setEntrytextDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		EntryTextDTO entryTextDTO = new EntryTextDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		entryTextDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.ENTRY_TEXT_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		entryTextDTO
				.setText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.ENTRY_TEXT_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return entryTextDTO;
	}

	public static CoreDTO setRedirectDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setRedirectDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		RedirectDTO redirectDTO = new RedirectDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		redirectDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.REDIRECT_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		redirectDTO
				.setRedirectTarget(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.REDIRECT_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return redirectDTO;
	}

	public static CoreDTO setReferenceDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setReferenceDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ReferenceDTO referenceDTO = new ReferenceDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		referenceDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.REFERENCE_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		referenceDTO
				.setPath(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.REFERENCE_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return referenceDTO;
	}

	public static CoreDTO setSiteMapDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setSiteMapDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		SiteMapDTO siteMapDTO = new SiteMapDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		siteMapDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SITE_MAP_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		siteMapDTO
				.setRootPath(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SITE_MAP_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return siteMapDTO;
	}

	public static CoreDTO setSlideShowDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setSlideShowDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		SlideShowDTO slideShowDTO = new SlideShowDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		slideShowDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SLIDE_SHOW_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		slideShowDTO
				.setSlideshowHeight(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SLIDE_SHOW_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		slideShowDTO
				.setSlideshowWidth(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.SLIDE_SHOW_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return slideShowDTO;
	}

	public static CoreDTO setTableDTO(String componentType, String componentId,
			Workbook wb) {
		String methodName = "setTableDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		TableDTO tableDTO = new TableDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		tableDTO.setNodeName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.TABLE_COMPONENT_HEADERS[0])
						.getColumn(), idIndex).getContents());
		tableDTO.setTableData(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.TABLE_COMPONENT_HEADERS[1])
						.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return tableDTO;
	}

	public static CoreDTO setTitleDTO(String componentType, String componentId,
			Workbook wb) {
		String methodName = "setTitleDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		TitleDTO titleDTO = new TitleDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		titleDTO.setNodeName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.TITLE_COMPONENT_HEADERS[0])
						.getColumn(), idIndex).getContents());
		titleDTO.setJcr_title(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.TITLE_COMPONENT_HEADERS[1])
						.getColumn(), idIndex).getContents());
		titleDTO.setType(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.TITLE_COMPONENT_HEADERS[2])
						.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return titleDTO;
	}

	public static CoreDTO setEntryListDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setEntryListDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		EntryListDTO entryListDTO = new EntryListDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		entryListDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.ENTRY_LIST_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		entryListDTO
				.setLimit(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.ENTRY_LIST_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		entryListDTO
				.setPageMax(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.ENTRY_LIST_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		entryListDTO
				.setDateFormat(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.ENTRY_LIST_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return entryListDTO;
	}

	public static CoreDTO setCustomImageDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setCustomImageDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		CustomImageDTO customImageDTO = new CustomImageDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		customImageDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setFile(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setImageCrop(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setFileName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setIconReference(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[4])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setImageRotate(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[5])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setJcr_title(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[6])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setAlt(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[7])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setLinkURL(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[8])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setJcr_description(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[9])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setSize(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[10])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setHeight(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[11])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setWidth(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[12])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setRedirectedURL(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[13])
										.getColumn(), idIndex).getContents());
		customImageDTO
				.setRedirectText(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_IMAGE_COMPONENT_HEADERS[14])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return customImageDTO;
	}
	
	public static CoreDTO setTitleImageDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setTitleLinkDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		TitleImageDTO titleImageDTO = new TitleImageDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		titleImageDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TITLE_IMAGE_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		String imageId = sh
				.getCell(
						sh.findCell(
								AcceleratorGenericConstants.TITLE_IMAGE_COMPONENT_HEADERS[1])
								.getColumn(), idIndex).getContents();
		ImageDTO imageDTO = (ImageDTO) setImageDTO("ImageComponent", imageId, wb);
		titleImageDTO.setImageDTO(imageDTO);
		titleImageDTO
				.setTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TITLE_IMAGE_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return titleImageDTO;
	}
	
	public static CoreDTO setTitleLinkDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setTitleLinkDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		TitleLinkDTO titleLinkDTO = new TitleLinkDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		titleLinkDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TITLE_LINK_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		titleLinkDTO
				.setTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TITLE_LINK_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		titleLinkDTO
				.setType(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TITLE_LINK_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());
		titleLinkDTO
				.setLink(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.TITLE_LINK_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return titleLinkDTO;
	}

	public static CoreDTO setCustomCarouselDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setCustomCarouselDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		CustomCarouselDTO customCarouselDTO = new CustomCarouselDTO();
		CustomCarouselItemDTO customCarouselItemDTO = new CustomCarouselItemDTO();
		List<CustomCarouselItemDTO> itemDTO = new ArrayList<CustomCarouselItemDTO>();

		int no_of_items = 0;

		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		customCarouselDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_CAROUSEL_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		no_of_items = Integer
				.parseInt(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.CUSTOM_CAROUSEL_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());

		for (int i = 0; i < no_of_items; i++) {
			customCarouselItemDTO = new CustomCarouselItemDTO();
			customCarouselItemDTO
					.setTitle(sh
							.getCell(
									sh.findCell(
											AcceleratorGenericConstants.CUSTOM_CAROUSEL_ITEM_COMPONENT_HEADERS[0])
											.getColumn(), idIndex)
							.getContents());
			customCarouselItemDTO
					.setTitleDesc(sh
							.getCell(
									sh.findCell(
											AcceleratorGenericConstants.CUSTOM_CAROUSEL_ITEM_COMPONENT_HEADERS[1])
											.getColumn(), idIndex)
							.getContents());
			customCarouselItemDTO
					.setNavTitle(sh
							.getCell(
									sh.findCell(
											AcceleratorGenericConstants.CUSTOM_CAROUSEL_ITEM_COMPONENT_HEADERS[2])
											.getColumn(), idIndex)
							.getContents());
			customCarouselItemDTO
					.setNavDesc(sh
							.getCell(
									sh.findCell(
											AcceleratorGenericConstants.CUSTOM_CAROUSEL_ITEM_COMPONENT_HEADERS[3])
											.getColumn(), idIndex)
							.getContents());
			customCarouselItemDTO
					.setLink(sh
							.getCell(
									sh.findCell(
											AcceleratorGenericConstants.CUSTOM_CAROUSEL_ITEM_COMPONENT_HEADERS[4])
											.getColumn(), idIndex)
							.getContents());
			customCarouselItemDTO
					.setImagePath(sh
							.getCell(
									sh.findCell(
											AcceleratorGenericConstants.CUSTOM_CAROUSEL_ITEM_COMPONENT_HEADERS[5])
											.getColumn(), idIndex)
							.getContents());
			itemDTO.add(customCarouselItemDTO);
			idIndex++;
		}
		customCarouselDTO.setItemDto(itemDTO);
		LOGGER.info(" || " + methodName + " || END");
		return customCarouselDTO;
	}

	public static CoreDTO setImageMultiFieldDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setImageMultiFieldDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ImageMultiFieldDTO imageMultiFieldDTO = new ImageMultiFieldDTO();
		CustomImageDTO customImageDTO = new CustomImageDTO();
		ImageDTO imageDTO = new ImageDTO();
		List<ImageDTO> imageDTOList = new ArrayList<ImageDTO>();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		imageMultiFieldDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.IMAGE_MULTIFIELD_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		imageMultiFieldDTO
				.setImageLayout(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.IMAGE_MULTIFIELD_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		String customImageId = sh
				.getCell(
						sh.findCell(
								AcceleratorGenericConstants.IMAGE_MULTIFIELD_COMPONENT_HEADERS[2])
								.getColumn(), idIndex).getContents();
		customImageDTO = (CustomImageDTO) setCustomImageDTO(
				"CustomImageComponent", customImageId, wb);
		int no_of_images = Integer
				.parseInt(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.IMAGE_MULTIFIELD_COMPONENT_HEADERS[3])
										.getColumn(), idIndex).getContents());
		String imageId = "";
		for (int i = 0; i < no_of_images; i++) {
			imageId = sh
					.getCell(
							sh.findCell(
									AcceleratorGenericConstants.IMAGE_MULTIFIELD_COMPONENT_HEADERS[4])
									.getColumn(), idIndex).getContents();
			imageDTO = (ImageDTO) setImageDTO("ImageComponent", imageId, wb);
			imageDTOList.add(imageDTO);
			idIndex++;
		}
		imageMultiFieldDTO.setCustomImageDTO(customImageDTO);
		imageMultiFieldDTO.setImageDTO(imageDTOList);

		LOGGER.info(" || " + methodName + " || END");
		return imageMultiFieldDTO;
	}

	public static CoreDTO setImageTitleDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setImageTitleDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		TitleImageDTO titleImageDTO = new TitleImageDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		titleImageDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.IMAGE_TITLE_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		String imageDTOId = sh
				.getCell(
						sh.findCell(
								AcceleratorGenericConstants.IMAGE_TITLE_COMPONENT_HEADERS[1])
								.getColumn(), idIndex).getContents();
		ImageDTO imageDTO = (ImageDTO) setImageDTO("ImageComponent",
				imageDTOId, wb);
		titleImageDTO.setImageDTO(imageDTO);
		titleImageDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.IMAGE_TITLE_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());

		LOGGER.info(" || " + methodName + " || END");
		return titleImageDTO;
	}

	public static CoreDTO setImageTitleLinkDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setImageTitleLinkDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		CustomTitleImageDTO imageTitleLinkDTO = new CustomTitleImageDTO();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}
		imageTitleLinkDTO.setNodeName(sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_TITLE_LINK_COMPONENT_HEADERS[0])
						.getColumn(), idIndex).getContents());
		String titleImageDTOId = sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_TITLE_LINK_COMPONENT_HEADERS[1])
						.getColumn(), idIndex).getContents();
		TitleImageDTO titleImageDTO = (TitleImageDTO) setTitleImageDTO(
				"ImageTitleComponent", titleImageDTOId, wb);
		imageTitleLinkDTO.setTitleImageDTO(titleImageDTO);
		String textImageDTOId = sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_TITLE_LINK_COMPONENT_HEADERS[2])
						.getColumn(), idIndex).getContents();
		TextImageDTO textImageDTO = (TextImageDTO) setTextImageDTO(
				"TextImageComponent", textImageDTOId, wb);
		imageTitleLinkDTO.setTextImageDTO(textImageDTO);
		String titleLinkDTOId = sh.getCell(
				sh.findCell(
						AcceleratorGenericConstants.IMAGE_TITLE_LINK_COMPONENT_HEADERS[3])
						.getColumn(), idIndex).getContents();
		TitleLinkDTO titleLinkDTO = (TitleLinkDTO) setTitleLinkDTO(
				"TitleLinkComponent", titleLinkDTOId, wb);
		imageTitleLinkDTO.setTitleLinkDTO(titleLinkDTO);

		LOGGER.info(" || " + methodName + " || END");
		return imageTitleLinkDTO;
	}

	public static CoreDTO setListOfLinkDTO(String componentType,
			String componentId, Workbook wb) {
		String methodName = "setSearchDTO";
		LOGGER.info(" || " + methodName + " || START");

		int idIndex = 0;
		ListOfLinkDTO listOfLinkDTO = new ListOfLinkDTO();
		ListOfLinkItemDTO listOfLinkItemDTO = new ListOfLinkItemDTO();
		List<ListOfLinkItemDTO> listOfLinkItemDTOList = new ArrayList<ListOfLinkItemDTO>();
		Sheet sh = wb.getSheet(componentType);
		Cell[] idRow = sh.getColumn(0);
		for (int i = 0; i < idRow.length; i++) {
			if (idRow[i].getContents().equalsIgnoreCase(componentId)) {
				idIndex = i;
			}
		}

		listOfLinkDTO
				.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.LIST_OF_LINKS_COMPONENT_HEADERS[0])
										.getColumn(), idIndex).getContents());
		listOfLinkDTO
				.setTitle(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.LIST_OF_LINKS_COMPONENT_HEADERS[1])
										.getColumn(), idIndex).getContents());
		int no_of_links = Integer
				.parseInt(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.LIST_OF_LINKS_COMPONENT_HEADERS[2])
										.getColumn(), idIndex).getContents());

		for (int i = 0; i < no_of_links; i++) {
			listOfLinkItemDTO = new ListOfLinkItemDTO();
			listOfLinkItemDTO
					.setLink(sh
							.getCell(
									sh.findCell(
											AcceleratorGenericConstants.LIST_OF_LINKS_COMPONENT_HEADERS[3])
											.getColumn(), idIndex)
							.getContents());
			listOfLinkItemDTO
					.setText(sh
							.getCell(
									sh.findCell(
											AcceleratorGenericConstants.LIST_OF_LINKS_COMPONENT_HEADERS[4])
											.getColumn(), idIndex)
							.getContents());
			listOfLinkItemDTOList.add(listOfLinkItemDTO);
			idIndex++;
		}
		listOfLinkDTO.setItemDto(listOfLinkItemDTOList);

		LOGGER.info(" || " + methodName + " || END");
		return listOfLinkDTO;
	}

	public static List<DAMDTO> setDAMDTO(
			DamUploadServiceRequest dServiceRequest, File damFile) {
		String methodName = "setDAMDTO";
		LOGGER.info(" || " + methodName + " || START");

		DAMDTO damDTO = new DAMDTO();
		DamPropertyDTO damPropertyDTO = new DamPropertyDTO();
		List<DamPropertyDTO> customMetaProperties = new ArrayList<DamPropertyDTO>();

		List<DAMDTO> damDTOList = new ArrayList<DAMDTO>();
		try {
			FileInputStream fs = new FileInputStream(damFile);
			Workbook wb = Workbook.getWorkbook(fs);

			Sheet sh = wb.getSheet("DAMContents");
			int no_of_custom_props = 0;
			String customPropsId = "";
			int no_of_rows = sh.getRows();

			for (int damIdRowIndex = 1; damIdRowIndex < no_of_rows;) {
				damDTO = new DAMDTO();
				damDTO.setId(sh.getCell(
						sh.findCell(AcceleratorGenericConstants.DAM_HEADERS[0])
								.getColumn(), damIdRowIndex).getContents());
				damDTO.setFileName(sh.getCell(
						sh.findCell(AcceleratorGenericConstants.DAM_HEADERS[1])
								.getColumn(), damIdRowIndex).getContents());
				no_of_custom_props = Integer.parseInt(sh.getCell(
						sh.findCell(AcceleratorGenericConstants.DAM_HEADERS[2])
								.getColumn(), damIdRowIndex).getContents());
				for (int custom_prop = 0; custom_prop < no_of_custom_props; custom_prop++) {
					customMetaProperties = new ArrayList<DamPropertyDTO>();
					customPropsId = sh.getCell(
							sh.findCell(
									AcceleratorGenericConstants.DAM_HEADERS[3])
									.getColumn(), damIdRowIndex).getContents();
					sh = wb.getSheet("CustomProperties");
					for (int row = 0; row < sh.getRows(); row++) {
						if (customPropsId
								.equalsIgnoreCase(sh
										.getCell(
												sh.findCell(
														AcceleratorGenericConstants.CUSTOM_PROPERTIES_HEADERS[0])
														.getColumn(), row)
										.getContents())) {
							damPropertyDTO = new DamPropertyDTO();
							damPropertyDTO
									.setKey(sh
											.getCell(
													sh.findCell(
															AcceleratorGenericConstants.CUSTOM_PROPERTIES_HEADERS[1])
															.getColumn(), row)
											.getContents());
							damPropertyDTO
									.setValue(sh
											.getCell(
													sh.findCell(
															AcceleratorGenericConstants.CUSTOM_PROPERTIES_HEADERS[2])
															.getColumn(), row)
											.getContents());
							customMetaProperties.add(damPropertyDTO);
						}
					}
					damIdRowIndex++;
					sh = wb.getSheet("DAMContents");
				}
				damDTO.setCustomMetaProperties(customMetaProperties);

				if (damDTO != null) {
					damDTO.setDestinationPath(dServiceRequest.getCqRootPath());
					damDTO.setSourcePath(dServiceRequest.getSourceRootPath());
					damDTO.setHost(dServiceRequest.getLocalhost());
					damDTO.setPort(dServiceRequest.getPort());
					damDTO.setUserName(dServiceRequest.getUsername());
					damDTO.setPassword(dServiceRequest.getPassword());
				}
				// If the image file is referenced from a websites
				damDTO = DamUploadHelper.imageFileRefFromWebsite(
						dServiceRequest, damDTO);
				damDTOList.add(damDTO);
			}

		} catch (BiffException e) {
			LOGGER.error(" || " + methodName + " || BIFF EXCEPTION OCCURED || "
					+ e.getMessage());
		} catch (IOException e) {
			LOGGER.error(" || " + methodName + " || IO EXCEPTION OCCURED || "
					+ e.getMessage());
		} catch (AcceleratorException e) {
			LOGGER.error(" || " + methodName
					+ " || ACCELERATOR EXCEPTION OCCURED || " + e.getMessage());
		}

		LOGGER.info(" || " + methodName + " || END");
		return damDTOList;
	}

	public static List<TagDTO> setTagDTO(File tagFile) {
		String methodName = "setTagDTO";
		LOGGER.info(" || " + methodName + " || START");

		TagDTO tagDTO = new TagDTO();
		List<TagDTO> tagDTOList = new ArrayList<TagDTO>();

		FileInputStream fs;
		try {
			fs = new FileInputStream(tagFile);
			Workbook wb = Workbook.getWorkbook(fs);
			Sheet sh = wb.getSheet("TagComponents");
			int rows = sh.getRows();

			for (int tagRowIndex = 1; tagRowIndex < rows; tagRowIndex++) {
				tagDTO = new TagDTO();
				tagDTO.setParentID(sh.getCell(
						sh.findCell(AcceleratorGenericConstants.TAG_HEADERS[0])
								.getColumn(), tagRowIndex).getContents());
				tagDTO.setTagId(sh.getCell(
						sh.findCell(AcceleratorGenericConstants.TAG_HEADERS[1])
								.getColumn(), tagRowIndex).getContents());
				tagDTO.setFilterVal(sh.getCell(
						sh.findCell(AcceleratorGenericConstants.TAG_HEADERS[2])
								.getColumn(), tagRowIndex).getContents());
				tagDTO.setName(sh.getCell(
						sh.findCell(AcceleratorGenericConstants.TAG_HEADERS[3])
								.getColumn(), tagRowIndex).getContents());
				tagDTO.setJcr_title(sh.getCell(
						sh.findCell(AcceleratorGenericConstants.TAG_HEADERS[4])
								.getColumn(), tagRowIndex).getContents());
				tagDTO.setJcr_description(sh.getCell(
						sh.findCell(AcceleratorGenericConstants.TAG_HEADERS[5])
								.getColumn(), tagRowIndex).getContents());

				tagDTOList.add(tagDTO);
			}

		} catch (FileNotFoundException e) {
			LOGGER.error(" || " + methodName
					+ " || FILE NOT FOUND EXCEPTION OCCURED || "
					+ e.getMessage());
		} catch (BiffException e) {
			LOGGER.error(" || " + methodName + " || BIFF EXCEPTION OCCURED || "
					+ e.getMessage());
		} catch (IOException e) {
			LOGGER.error(" || " + methodName + " || IO EXCEPTION OCCURED || "
					+ e.getMessage());
		}

		LOGGER.info(" || " + methodName + " || END");
		return tagDTOList;
	}
}
