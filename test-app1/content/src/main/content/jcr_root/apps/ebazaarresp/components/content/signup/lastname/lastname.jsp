<%@include file="/libs/foundation/global.jsp"%><%
%><%@ page import="com.day.cq.wcm.foundation.TextFormat,
                   com.day.cq.wcm.foundation.forms.FormsHelper,
                   com.day.cq.wcm.foundation.forms.LayoutHelper, 
                   com.day.cq.wcm.foundation.forms.ValidationInfo" %>
<%
    final String name =  "lastName";
    final String id = FormsHelper.getFieldId(slingRequest, resource);
    final boolean required = FormsHelper.isRequired(resource);
    final boolean readOnly = properties.get("readOnlyField", false);
    final boolean hideTitle = properties.get("hideTitle", false);
    final String constraintMessage = properties.get("constraintMessage", "The Last Name must not contain numbers or special characters");
    final String width = properties.get("width", null);
    final String rows = properties.get("rows", "1");
    final String cols = properties.get("cols", "35");
    final String maxlength = properties.get( "maxlength", "24" ); 
    String helpText = properties.get("helpText", "");
    final boolean hideHelpText = properties.get("hideHelpText", false);
    
    String title = FormsHelper.getTitle(resource, "Last Name");
    String value = FormsHelper.getValue(slingRequest, resource);
    
     ValueMap map = (ValueMap)slingRequest.getAttribute(FormsHelper.REQ_ATTR_GLOBAL_LOAD_MAP);
    if(map != null){
      value = map.get(name)!= null ? map.get(name).toString() : "";
    }else{
        value="";
    }
    final String errorLabelCSS = properties.get("errorLabelCSS", "errorLabel");
    ValidationInfo info = ValidationInfo.createValidationInfo(slingRequest);
    String[] errorsInput = info.getErrorMessages(name);
    boolean isErrorInput = errorsInput != null && errorsInput.length > 0;
        
    %><div class="form_row">
        <%if (isErrorInput){%><div class="<%=errorLabelCSS%>"><%}%>            
        <% LayoutHelper.printTitle(id, title, required, hideTitle, out); %>
        <%if (isErrorInput){%></div><%}%>        
        <div class="form_rightcol" id="<%= name %>_rightcol">
            <div id="<%= name %>_wrapper" class="form_rightcol_wrapper"><%
            if (readOnly) {
                if (value.length() == 0) {
                    // at least display a space otherwise layout may break
                    value = " ";
                }
                %><c:out value="<%= new TextFormat().format(value) %>" escapeXml="false"/><%
            }  else {
                if (rows.equals("1")) {
                    %><input type="text"class="<%= FormsHelper.getCss(properties, "form_field form_field_text") %>" id="<%= name %>" onblur="validateLastName(this.id)" name="<%= name %>" value="<c:out value="<%= value %>"/>" size="<%= cols %>" maxlength="<%= maxlength  %>" <%= (width != null ? "style='width:" + width + "px;'" : "") %>/><%
                } else {
                    %><textarea class="<%= FormsHelper.getCss(properties, "form_field form_field_textarea") %>" id="<%= name %>" name="<%= name %>" rows="<%= rows %>" cols="<%= cols %>" <%= (width != null ? "style='width:" + width + "px;'" : "") %>><c:out value="<%= value %>"/></textarea><%
                }
                if (!hideHelpText){ 
                    LayoutHelper.printDescription(helpText, out);
                }
            }
            %><div id="errorLabel2" class="form_rightcol form_error"></div></div>
        </div>
    </div>
    <%
    LayoutHelper.printDescription(FormsHelper.getDescription(resource, ""), out);
    LayoutHelper.printErrors(slingRequest, name, hideTitle, out);
%>

<cq:includeClientLib categories="nexus.tieredAccess.multiPageRegistration"/>
<script type="text/javascript">

    function validateLastName(id)
    {
       //var $CQ = $.noConflict(true);
       var lastName = document.getElementById(id).value;
          if (lastName.search("[!{}%^\\$&\*:@~#;/<>\\\\|`\\.\\[\\]\\d\\(\\)_\\-\\+=,\\\"\\'\\?]+") != -1) {
              //customMessage("<%=constraintMessage%>");
              document.getElementById(id).value = '';
              var parent = $CQ("#" + id).parent().parent().parent();
	      var id = "<%=errorLabelCSS%>";
              var div = $("#errorLabel2").html("<%=constraintMessage%>");
              //parent.children(".form_leftcol").wrap("<div class=\"<%=errorLabelCSS%>\"></div>");
          }
    }

</script>