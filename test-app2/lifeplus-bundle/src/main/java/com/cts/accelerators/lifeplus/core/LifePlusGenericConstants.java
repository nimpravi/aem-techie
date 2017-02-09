package com.cts.accelerators.lifeplus.core;

public class LifePlusGenericConstants {
	// Service Request Types
	public static final String WEATHER_API_KEY = "Weather Api Key";
	public static final String WEATHER_API_URL = "Weather Api Url";
	public static final String WEATHER_API_PROPERTY_TEMPERATUE = "Weather Api Property To Be Fetched - Temperature";
	public static final String WEATHER_API_PROPERTY_CLOUD_COVER = "Weather Api Property To Be Fetched - Cloud Cover";
	public static final String WEATHER_API_PROPERTY_HUMIDITY = "Weather Api Property To Be Fetched - Humidity";
	public static final String WEATHER_API_PROPERTY_FEELS_LIKE_TEMPERATURE = "Weather Api Property To Be Fetched - Feels Like Temperature";

	//Request Param for lifePlus Pages
	public static final String SIGN_UP = "signup";
	public static final String REQUEST_TYPE = "requestType";
    public static final String PREDICTIVESEARCHRESULTS = "predictivesearchresults";
	public static final String DOCTOR_RESULTS = "doctorresults";
	public static final String DOCTOR_TIMETABLE = "doctortimetable";
	public static final String LAB_MANAGEMENT = "labmanagement";
	public static final String HOSP_MANAGEMENT ="hospmanagement";
	public static final String PHARMACY_MANAGEMENT = "pharmacymanagement";
	public static final String INSURANCE_MANAGEMENT = "insurancemanagement";
	public static final String TESTIMONIALS = "testimonials";
	public static final String FBGPLUSLOGIN = "fbgpluslogin";
	public static final String GOOGLE_MAPS = "googlemaps";

	
	//DataBase name
	public static final String SCHEMA_NAME = "SolutionAccelerator";
	public static final String PATIENT_TABLE = "patient";
	public static final String DOCTOR_TABLE = "doctor";
	public static final String MEMBER_TABLE = "member";
	public static final String SPECIALITY_TABLE = "speciality";
	public static final String PROFILE_TABLE = "profile";
	
	
	// Tables for LifePlus
	public static final String DATASOURCE_NAME = "SolutionAccelerator";
	
	//signUp messages
	public static final String SUCCESS = "User Email has been added to CQ successfully. An email has been sent your email id with the activation link. Click the link to Activate.";
	public static final String ALREADY_EXISTS = "User(Email id already exists. Please use the forgot password option to proceed if you have forgot the password";
	public static final String USER_CREATION_FAILURE="User creation failed!!! Please try again later.";
	public static final String NEW_FB_GPLUS_USER_CREATION="success";
	public static final String SUCCESS_PASSWORD_RESET_LINK = "An email has been sent to your email id with password reset link. Click the link to reset password.";
	
	public static final String FB_USER_LOGIN_NAME="fbuserloginname";
	public static final String FB_USER_PASSWORD="fbuserpassword";
	public static final String GPLUS_USER_LOGIN_NAME="gplususerloginname";
	public static final String GPLUS_USER_PASSWORD="gplususerpassword";
	
	public static final String USER_LOGIN_TYPE="logintype";
	
	// Testimonials Constants
	public static final String MEMBER_ID = "member_id";
	public static final String INTEREST = "interest";
	public static final String TESTIMONIALS_COUNT = "testi_count";
	public static final String EMPTY_STRING = "";
	public static final String COLON = ":";
	public static final String CSV_SEPARATOR = ",";
	public static final String STATUS = "status";
	public static final String DESCRIPTION = "description";
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_FAILURE = "FAILURE";

	public static final String SIGNUP_VALIDATION_SERVLET="/bin/SignupValidationServlet";
	public static final String SCHEDULETIME="scheduletime";
    public static final String BOOKAPPOINTMENT="bookYourAppointment";
	public static final String PASSWORD_RESET_PAGE_LINK="/content/lifeplus/resetpassword.html";
	
}
