<%@include file="/libs/foundation/global.jsp"%><%
%><%@ page import="com.day.cq.wcm.foundation.forms.FormsHelper,
        com.day.cq.wcm.foundation.forms.LayoutHelper,
        com.day.cq.wcm.foundation.forms.ValidationInfo" %><%
    final String name = "password";
    final String id = FormsHelper.getFieldId(slingRequest, resource);
    final boolean required = FormsHelper.isRequired(resource);
    final boolean hideTitle = properties.get("hideTitle", false);
    final String title = FormsHelper.getTitle(resource, "Password");
    final String width = properties.get("width", "");
    final String cols = properties.get("cols", "35");
    String helpText = properties.get("helpText", "");
    final boolean hideHelpText = properties.get("hideHelpText", false);
    
    final String errorLabelCSS = properties.get("errorLabelCSS", "errorLabel");
    ValidationInfo info = ValidationInfo.createValidationInfo(slingRequest);
    String[] errorsInput = info.getErrorMessages(name);
    boolean isErrorInput = errorsInput != null && errorsInput.length > 0;
    
    String value = FormsHelper.getValue(slingRequest, resource);
    if ( value == null ) {
        value = "";
    }
%>
    <div class="form_row">
      <%if (isErrorInput){%><div class="<%=errorLabelCSS%>"><%}%>            
        <% LayoutHelper.printTitle(id, title, required, hideTitle, out); %>
        <%if (isErrorInput){%></div><%}%>        
      <div class="form_rightcol"><input class="<%= FormsHelper.getCss(properties, "form_field form_field_password") %>" id="<%= id %>" name="<%=name%>" value="<c:out value="<%=value%>"/>" type="password" size="<%=cols %>" <%= (width.length()>0?"style='width:"+width+"px;'":"") %>></div>
      <%
      if (!hideHelpText){ 
            LayoutHelper.printDescription(helpText, out);
        }
      %>
    </div>
<%
    LayoutHelper.printDescription(FormsHelper.getDescription(resource, ""), out);
    LayoutHelper.printErrors(slingRequest, name, hideTitle, out);
%>
