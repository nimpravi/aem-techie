<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>

<%@page import="com.cts.accelerators.core.util.AcceleratorDAMUtils"%>

<%@page import="com.cts.accelerators.migration.ootbcomps.dto.DAMDTO"%>

<%@page import="java.util.*"%>

<%@page import="com.cts.accelerators.migration.transformer.factory.XMLTransformer"%>


<%@page import="java.io.File"%>

<%@page import="java.lang.StringBuffer"%>


Test


<%

  AcceleratorDAMUtils damUtils= new AcceleratorDAMUtils();

DAMDTO dam= new DAMDTO();

out.println(damUtils);

out.println("<br/>");

out.println(dam);

dam.setSingleFileSourcePath("D:\\Test\\Test\\Penguins.jpg");

dam.setMultiFileUpSourcePath("D:\\Test\\Test");
dam.setDestinationPath("/content/dam/testpoc");

String path=dam.getSingleFileSourcePath();
String multiFileSourcePath =dam.getMultiFileUpSourcePath();

String dpath=dam.getDestinationPath();

out.println("<br/>");

out.println(path);

out.println("<br/>");

out.println(dpath);


dam.setUserName("admin");

String username=dam.getUserName();

out.println("<br/>");

out.println(username);


dam.setPassword("admin");

String password=dam.getPassword();

out.println("<br/>");

out.println(password);



dam.setHost("localhost");

String host=dam.getHost();

out.println("<br/>");

out.println(host);


dam.setPort("4502");

String port=dam.getPort();

out.println("<br/>");

out.println(port);


HashMap<String, String> customMetaProp = new HashMap<String,String>();


customMetaProp.put("a","13");

//damUtils.createBulkUpload(dam);

AcceleratorDAMUtils.downloadMedia("file:///C:/Users/Public/Videos/Sample Videos/Wildlife.wmv", "D:\\Test", "video","File System");

//XMLTransformer xmltr=new XMLTransformer();

//out.println(xmltr);


//	ContentTransformerServiceRequest requestt=new ContentTransformerServiceRequest();

//out.println(requestt);
//requestt.setSourceXMLPath("D:\\details.xml");


// requestt.setTransformedXMLPath("D:\\");


// requestt.setXsltPath("D:\\details_con.xslt");


// requestt.setTransformationOrder("Image,Dam,pdf");
		 

// requestt.setRootPath("D:\\");

//out.println(requestt);


//xmltr.transformContents(requestt);


//File sourcepath = new File("D:\\details.xml");


//File xslpath = new File("D:\\details_con.xslt");




//StringBuffer buff = new StringBuffer("D:\\details_con.xsl");






//StringBuffer buff =xmltr.readFileAsString("D:\\details_con.xsl");


//xmltr.createTransformedXML(sourcepath,buff,"page");
//ContentTransformerServiceResponse responsee = new ContentTransformerServiceResponse();

//out.println(responsee);

//responsee= transformContents(requestt);


//xmltr.transformContents(requestt);


%>

<cq:include path="testing" resourceType="/libs/foundation/components/parsys"/>