package com.cts.accelerators.lifeplus.controllers;

/**
 * This is the main controller class which is invoked for any kind of database connection request from life+ portal.
 * 
 * @author Cognizant
 */
import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cts.accelerators.lifeplus.core.DatabaseConnectionHelper;
import com.cts.accelerators.lifeplus.core.LifePlusGenericConstants;
import com.cts.accelerators.lifeplus.customDTO.MemberDTO;
import com.cts.accelerators.lifeplus.helpers.BookAppointmentHelper;
import com.cts.accelerators.lifeplus.helpers.DoctorTimetableHelper;
import com.cts.accelerators.lifeplus.helpers.GoogleMapHelper;
import com.cts.accelerators.lifeplus.helpers.PredictiveSearchHelper;
import com.cts.accelerators.lifeplus.helpers.FindDoctorResultsHelper;
import com.cts.accelerators.lifeplus.helpers.ScheduleTimeHelper;
import com.cts.accelerators.lifeplus.helpers.SignUpHelper;
import com.cts.accelerators.lifeplus.helpers.TestimonialsHelper;
import com.cts.accelerators.lifeplus.services.CustomPersonalizationContentRetrieverService;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverRequest;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverResponse;
import com.cts.accelerators.migration.exceptions.AcceleratorException;

@SlingServlet(paths = "/bin/dbconnection", methods = "POST", metatype = false)
public class DataBaseServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = DataBaseServlet.class.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataBaseServlet.class);

	@Reference
	CustomPersonalizationContentRetrieverService retrieverService;

	@Override
	protected void doGet(SlingHttpServletRequest slingRequest,
			SlingHttpServletResponse slingResponse) {
		String methodName = "doGet";
		LOGGER.info("|| " + methodName + " || START");
		LOGGER.debug("|| " + methodName + " || " + CLASS_NAME
				+ " || Redirecting to doPost");
		doPost(slingRequest, slingResponse);
		LOGGER.info("|| " + methodName + " || END");
	}

	@Override
	protected void doPost(SlingHttpServletRequest slingRequest,
			SlingHttpServletResponse slingResponse) {
		String methodName = "doPost";
		JSONObject responseReceived = new JSONObject();
		Connection con = null;
		DatabaseConnectionHelper dataBaseHelper = new DatabaseConnectionHelper();
		LOGGER.info(" || " + methodName + " || START");
		con = dataBaseHelper.getConnection();
		try {
			if (null != con) {
				responseReceived = identifyPageRequest(slingRequest,
						slingResponse, con);
				if (null != responseReceived) {
					slingResponse.getWriter()
							.write(responseReceived.toString());
				} else {
					slingResponse.getWriter().write("Response was null");
				}
			} else {
				slingResponse.getWriter().write(
						"Unable to connect Database Please");
			}
		} catch (IOException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					+ e.getMessage());
		} catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					+ e.getMessage());
		}

		LOGGER.info(" || " + methodName + " || responseReceived.toString() || "
				+ responseReceived.toString());
		LOGGER.info(" || " + methodName + " || END");
	}

	private JSONObject identifyPageRequest(SlingHttpServletRequest request,
			SlingHttpServletResponse slingResponse, Connection con)
			throws JSONException {
		String methodName = "identifyPageRequest";
		LOGGER.info(" || " + methodName + " || START");

		JSONObject response = null;
		LOGGER.info(" || " + methodName + " || requestType || "
				+ request.getParameter(LifePlusGenericConstants.REQUEST_TYPE));

		if (LifePlusGenericConstants.SIGN_UP.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
			response = SignUpHelper.createNewUser(request, slingResponse, con);
		} 

else if (LifePlusGenericConstants.FBGPLUSLOGIN.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
			response = SignUpHelper.createNewFBGPlusUser(request,
					slingResponse, con);
		} else if (LifePlusGenericConstants.PREDICTIVESEARCHRESULTS
				.equals(request
						.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
			response = PredictiveSearchHelper.PredictiveSearch(request, con);
		} else if (LifePlusGenericConstants.DOCTOR_RESULTS.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
			response = FindDoctorResultsHelper.SearchDoctors(request, con);
		} else if (LifePlusGenericConstants.DOCTOR_TIMETABLE.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
			response = DoctorTimetableHelper.DoctorTimings(request, con);
		} else if (LifePlusGenericConstants.SCHEDULETIME.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
			response = ScheduleTimeHelper.appointmentScheduleTiming(request,
					con);
		} else if (LifePlusGenericConstants.BOOKAPPOINTMENT.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {

			response = BookAppointmentHelper.bookDocAppointment(request, con);
		}

		else if (LifePlusGenericConstants.TESTIMONIALS.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
			// response = TestimonialsHelper.getTestimonials(request, con);
			ContentRetrieverRequest retrieverRequest = new ContentRetrieverRequest();
			ContentRetrieverResponse retrieverReponse = new ContentRetrieverResponse();

			retrieverRequest.setDescription("");
			retrieverRequest.setMemberId(request
					.getParameter(LifePlusGenericConstants.MEMBER_ID));
			retrieverRequest.setInterest(request
					.getParameter(LifePlusGenericConstants.INTEREST));
			if (null == request.getParameter(LifePlusGenericConstants.INTEREST)
					|| request.getParameter(LifePlusGenericConstants.INTEREST)
							.equalsIgnoreCase("")) {
				retrieverRequest
						.setStatus(LifePlusGenericConstants.STATUS_FAILURE);
				retrieverRequest.setDescription(retrieverRequest
						.getDescription()
						+ "The Type of testimonial (interest) is not set; ");
			}
			if (null == request
					.getParameter(LifePlusGenericConstants.TESTIMONIALS_COUNT)
					|| request.getParameter(
							LifePlusGenericConstants.TESTIMONIALS_COUNT)
							.equalsIgnoreCase("")) {
				retrieverRequest.setTestimonialCount(4);
				retrieverRequest
						.setDescription(retrieverRequest.getDescription()
								+ "Testimonial count was not set -- Set by default to 4; ");
			} else {
				retrieverRequest
						.setTestimonialCount(Integer.parseInt(request
								.getParameter(LifePlusGenericConstants.TESTIMONIALS_COUNT)));
			}
			try {
				retrieverReponse = (ContentRetrieverResponse) retrieverService
						.execute(retrieverRequest);
				response = retrieverReponse.getJsonResponse();
			} catch (AcceleratorException e) {
				LOGGER.error(" || " + methodName + " || EXCEPTION");
			}
		} else if (LifePlusGenericConstants.GOOGLE_MAPS.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
			response = GoogleMapHelper.getGoogleMapsData(request, con);
		} else if (LifePlusGenericConstants.LAB_MANAGEMENT.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
		} else if (LifePlusGenericConstants.HOSP_MANAGEMENT.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
		} else if (LifePlusGenericConstants.PHARMACY_MANAGEMENT.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
		} else if (LifePlusGenericConstants.INSURANCE_MANAGEMENT.equals(request
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE))) {
		}

		LOGGER.info(" || " + methodName + " || response || " + response);
		LOGGER.info(" || " + methodName + " || END");
		return response;
	}

}
