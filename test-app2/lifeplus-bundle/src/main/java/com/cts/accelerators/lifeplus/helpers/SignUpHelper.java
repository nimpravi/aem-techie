package com.cts.accelerators.lifeplus.helpers;

import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.AuthorizableExistsException;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.lifeplus.core.LifePlusGenericConstants;
import com.cts.accelerators.lifeplus.customDTO.MemberDTO;
import com.cts.accelerators.lifeplus.customDTO.ProfileDTO;
import com.cts.accelerators.lifeplus.services.NewAccountValidationService;
import com.cts.accelerators.lifeplus.services.dto.NewAccountValidationServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.NewAccountValidationServiceResponse;

public class SignUpHelper {
	private static final String CLASS_NAME = SignUpHelper.class.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SignUpHelper.class);

	public static JSONObject createNewUser(SlingHttpServletRequest slingRequest,
			Connection con) {
		
		String methodName = "createNewUser";
		
		LOGGER.info(" || " + methodName + " || START");
		JSONObject jsonResponse = new JSONObject();
		ResultSet rs;
		int member_id = 0;
		
		MemberDTO user = new MemberDTO();
		
		String email = slingRequest.getParameter("email");
		user.setEmail(email);
		String password = slingRequest.getParameter("password");
		user.setPassword(password);
		user.setSecurityQuestion(slingRequest.getParameter("secQues"));
		user.setSecurityAnswer(slingRequest.getParameter("secAns"));
		
		boolean isEmailExist;
		Statement stmt = null;
		MemberHelper member = new MemberHelper();
		isEmailExist = member.isEmailExist(user.getEmail(), con);
		user.setProfile_type("Lifeplus");
		user.setMember_type(slingRequest.getParameter("memberType"));
		if (!isEmailExist) {

			String sql = "INSERT INTO "
					+ LifePlusGenericConstants.SCHEMA_NAME
					+ "."
					+ LifePlusGenericConstants.MEMBER_TABLE
					+ "(email,password,profileType,membership_type,security_question,security_answer) VALUES(\""
					+ user.getEmail() + "\",\"" + user.getPassword() + "\",\"" + user.getProfile_type()+ "\",\"" + user.getMember_type()+ "\",\""
					+ user.getSecurityQuestion() + "\",\"" + user.getSecurityAnswer()
					+ "\");";
			LOGGER.info(" || "+ methodName + " || sql || " + sql);

			try {
				stmt = con.createStatement();
				stmt.executeUpdate(sql);
				LOGGER.debug(" || " + methodName + " || after creating user");
				
				//retrieve member_id and store in cq user profile
				sql = "SELECT member_id FROM "
					  + LifePlusGenericConstants.MEMBER_TABLE
					  + " WHERE email=\""
					  + user.getEmail()
					  + "\";";
				LOGGER.info(" || "+ methodName + " || sql 1 || " + sql);
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					member_id = rs.getInt("member_id");
				}
				LOGGER.info(" || "+ methodName + " || member_id || " + member_id);
				jsonResponse=createNewCQUser(slingRequest, member_id).getJsonResponse();
			} catch (SQLException e) {
				LOGGER.error(" || " + methodName + " || SQL EXCEPTION OCCURED || ",e);
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					LOGGER.error(" || " + methodName + " || SQL EXCEPTION OCCURED || ",e);
				}
			}
		} else {
			try {
				jsonResponse
						.put("key", LifePlusGenericConstants.ALREADY_EXISTS);
			} catch (JSONException e) {
				LOGGER.error(" || " + methodName + " || JSON EXCEPTION OCCURED || "+e.getMessage());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}
	
	public static NewAccountValidationServiceResponse createNewCQUser(SlingHttpServletRequest slingRequest,
			 int member_id) {
		String methodName = "createNewCQUser";
		LOGGER.info(" || "+ methodName + " || START");
		LOGGER.info(" || "+ methodName + " || member_id || "+member_id);

		Session session = null;
		String userId = slingRequest.getParameter("email");
		String password = slingRequest.getParameter("password");
		String secQues = slingRequest.getParameter("secQues");
		String secAns = slingRequest.getParameter("secAns");
		NewAccountValidationServiceResponse newAccountValidationServiceResponse=null;
		//TidyJSONWriter writer = null;
		Group lifeplusGroup;
		final String loginID = userId;
		try {
			
			//writer = new TidyJSONWriter(slingResponse.getWriter());
			
			// Ensure that the currently logged on user has admin privileges.
			//session = ConnectionManager.getSession();
			session=slingRequest.getResourceResolver().adaptTo(Session.class);
			JackrabbitSession jsession = (JackrabbitSession) session;
			UserManager uManager = jsession.getUserManager();
			
			lifeplusGroup = (Group) uManager
					.getAuthorizableByPath("/home/groups/l/lifeplus");
			
			Principal principal = new Principal() {
				public String getName() {
					return loginID;
				}
			};
						
			// Add a new user to Adobe CQ
			User user = uManager.createUser(userId, password,principal,"/home/users/lifeplus");
			if (null != user) {
				lifeplusGroup.addMember(user);
			}
			Node node = session.getNode(user.getPath());
					
			//create profile node, then set member_id property to that node 
			Node profileNode = null; 
			if(node.hasNode("profile")) {
				profileNode = node.getNode("profile");
			} else {
				profileNode = node.addNode("profile");
				profileNode.setPrimaryType("nt:unstructured");
				profileNode.setProperty("sling:resourceType", "cq/security/components/profile");
			}
			profileNode.setProperty("member_id", member_id);
			if(StringUtils.isNotEmpty(slingRequest.getParameter("first_name"))){
				profileNode.setProperty("givenName", slingRequest.getParameter("first_name"));
			}
			if(StringUtils.isNotEmpty(slingRequest.getParameter("last_name"))){
				profileNode.setProperty("familyName", slingRequest.getParameter("last_name"));
			}
			session.save();
			
			BundleContext bundleContext = FrameworkUtil.getBundle(SlingSettingsService.class).getBundleContext();  
			NewAccountValidationService newAccountValidationService = (NewAccountValidationService) bundleContext.getService(bundleContext.getServiceReference(NewAccountValidationService.class.getName()));
			NewAccountValidationServiceRequest newAccountValidationServiceRequest=new NewAccountValidationServiceRequest();
			newAccountValidationServiceRequest.setEmail(userId);
			String protocol=slingRequest.getProtocol();
			newAccountValidationServiceRequest.setInitialURL((protocol.contains(AcceleratorGenericConstants.FORWARD_SLASH)?protocol.substring(0,protocol.indexOf(AcceleratorGenericConstants.FORWARD_SLASH)).toLowerCase():protocol.toLowerCase())+"://"+slingRequest.getServerName()+":"+slingRequest.getServerPort());
			newAccountValidationServiceRequest.setMember_id(member_id);
			newAccountValidationServiceRequest.setUserNode(profileNode);
			newAccountValidationServiceResponse=(NewAccountValidationServiceResponse)newAccountValidationService.execute(newAccountValidationServiceRequest);
			
			LOGGER.debug(" || "+methodName+" || node path ||"+node.getPath());
			
			/*// Send the data back to the client using a TidyJSONWriter object
			writer.setTidy("true".equals(slingRequest.getParameter("tidy")));
			writer.object();
			writer.key("key").value(LifePlusGenericConstants.SUCCESS);
			writer.endObject();*/

		} catch (AuthorizableExistsException e) {
			LOGGER.error("||"+methodName+" || Exception occured::",e);
			LOGGER.debug("User already exists");
			//writer.setTidy("true".equals(slingRequest.getParameter("tidy")));
			/*try {
				writer.object();
				writer.key("key").value(LifePlusGenericConstants.ALREADY_EXISTS);
				writer.endObject();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/

		} catch (Exception e) {
			LOGGER.error("||"+methodName+" ||Exception occured::",e);
		} /*finally {
			session.logout();
			session = null;
		}*/
		LOGGER.info(" || "+ methodName + " || END");
		return newAccountValidationServiceResponse;
	}
	public static JSONObject createNewFBGPlusUser(SlingHttpServletRequest slingRequest,
			 Connection con) {
		
		String methodName = "createNewFBGPlusUser";
		LOGGER.info(" || " + methodName + " || START");
		JSONObject jsonResponse = new JSONObject();
		ResultSet rs;
		int member_id = 0;
		
		MemberDTO user = new MemberDTO();
		ProfileDTO profileDTO=new ProfileDTO();
		
		String email = slingRequest.getParameter("email");
		user.setEmail(email);
		user.setPassword(slingRequest.getParameter("password"));
		
		String profile_type=slingRequest.getParameter("profileType");
		user.setProfile_type(profile_type);
		
		boolean isEmailExist;
		Statement stmt = null;
		MemberHelper member = new MemberHelper();
		isEmailExist = member.isEmailExist(user.getEmail(), con);
		String member_type=slingRequest.getParameter("memberType");
		user.setMember_type(member_type);
		if (!isEmailExist) {

			String sql = "INSERT INTO "
					+ LifePlusGenericConstants.SCHEMA_NAME
					+ "."
					+ LifePlusGenericConstants.MEMBER_TABLE
					+ "(email,profileType,membership_type) VALUES(\""
					+ user.getEmail() + "\",\"" + user.getMember_type() + "\",\""
					+ user.getProfile_type()+"\");";
			LOGGER.info(" || "+ methodName + " || sql || " + sql);
			try {
				stmt = con.createStatement();
				stmt.executeUpdate(sql);
					
				//retrieve member_id and store in cq user profile
				sql = "SELECT member_id FROM "
					  + LifePlusGenericConstants.MEMBER_TABLE
					  + " WHERE email=\""
					  + user.getEmail()
					  + "\";";
				
				LOGGER.info(" || "+ methodName + " || sql 1 || " + sql);
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					member_id = rs.getInt("member_id");
				}
				profileDTO.setEmail(user.getEmail());
				profileDTO.setAddress(slingRequest.getParameter("address"));
				profileDTO.setFirst_name(slingRequest.getParameter("first_name"));
				profileDTO.setLast_name(slingRequest.getParameter("last_name"));
				profileDTO.setMember_id(member_id);
				profileDTO.setSex(slingRequest.getParameter("gender"));
				String location=slingRequest.getParameter("location");
				if(StringUtils.isNotEmpty(location)){
					if(location.contains(",")){
						String[]locationArray=location.split(",");
						if(locationArray.length==2){
							profileDTO.setCity(locationArray[0].trim());
							profileDTO.setCountry(locationArray[1].trim());
						}
					}
				}
				if(StringUtils.isNotEmpty(slingRequest.getParameter("picture"))){
					profileDTO.setMember_image_path(slingRequest.getParameter("picture"));
				}

				sql = "INSERT INTO "
						+ LifePlusGenericConstants.SCHEMA_NAME
						+ "."
						+ LifePlusGenericConstants.PROFILE_TABLE
						+ "(member_id,email_id,first_name,last_name,sex,address,city,country,member_image_path) VALUES(\""
						+ profileDTO.getMember_id() + "\",\"" + profileDTO.getEmail() + "\",\""
						+profileDTO.getFirst_name() + "\",\""+profileDTO.getLast_name()+ "\",\""+profileDTO.getSex()+ "\",\""+profileDTO.getAddress()+ "\",\""+profileDTO.getCity()+ "\",\""+profileDTO.getCountry()+ "\",\""+profileDTO.getMember_image_path()+"\");";
				LOGGER.info(" || "+ methodName + " || Profile Table SQL || " + sql);
				stmt.executeUpdate(sql);
				LOGGER.info(" || "+ methodName + " || member_id || " + member_id);
				NewAccountValidationServiceResponse newAccountValidationServiceResponse=createNewCQUser(slingRequest, member_id);
				LOGGER.debug(" || " + methodName + " || after creating user");
				if(newAccountValidationServiceResponse.getJsonResponse().has("key") && newAccountValidationServiceResponse.getJsonResponse().getString("key").equals(LifePlusGenericConstants.SUCCESS))
				{
					jsonResponse.put("key", LifePlusGenericConstants.NEW_FB_GPLUS_USER_CREATION);
					if(user.getProfile_type().equalsIgnoreCase("facebook")){
						jsonResponse.put(LifePlusGenericConstants.USER_LOGIN_TYPE, "facebook");
						jsonResponse
								.put(LifePlusGenericConstants.FB_USER_LOGIN_NAME,user.getEmail());
						jsonResponse.put(LifePlusGenericConstants.FB_USER_PASSWORD,user.getPassword());
					}
					else if(user.getProfile_type().equalsIgnoreCase("googleplus")){
						jsonResponse.put(LifePlusGenericConstants.USER_LOGIN_TYPE, "googleplus");
						jsonResponse
						.put(LifePlusGenericConstants.GPLUS_USER_LOGIN_NAME,user.getEmail());
						jsonResponse.put(LifePlusGenericConstants.GPLUS_USER_PASSWORD,user.getPassword());
					}
				}
				else
				{
					jsonResponse.put("key",LifePlusGenericConstants.USER_CREATION_FAILURE);
				}
			} catch (SQLException e) {
				LOGGER.error(" || " + methodName + " || SQL EXCEPTION OCCURED || ",e);
			} catch (JSONException e) {
				LOGGER.error(" || " + methodName + " || JSON EXCEPTION OCCURED || ",e);
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					LOGGER.error(" || " + methodName + " || SQL EXCEPTION OCCURED || ",e);
				}
			}
		} else {
			try {
				if(user.getProfile_type().equalsIgnoreCase("facebook")){
					jsonResponse.put(LifePlusGenericConstants.USER_LOGIN_TYPE, "facebook");
					jsonResponse
							.put(LifePlusGenericConstants.FB_USER_LOGIN_NAME,user.getEmail());
					jsonResponse.put(LifePlusGenericConstants.FB_USER_PASSWORD,user.getPassword());
				}
				else if(user.getProfile_type().equalsIgnoreCase("googleplus")){
					jsonResponse.put(LifePlusGenericConstants.USER_LOGIN_TYPE, "googleplus");
					jsonResponse
					.put(LifePlusGenericConstants.GPLUS_USER_LOGIN_NAME,user.getEmail());
					jsonResponse.put(LifePlusGenericConstants.GPLUS_USER_PASSWORD,user.getPassword());
				}
			} catch (JSONException e) {
				LOGGER.error(" || " + methodName + " || JSON EXCEPTION OCCURED || ",e);
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

}
