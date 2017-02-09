<%@include file="/libs/foundation/global.jsp"%><%
%><%@ page import="java.util.LinkedHashMap,
        java.util.List,
        java.util.Map,
        java.util.ArrayList,
        java.util.Arrays,
        java.util.Collections,
        java.util.Iterator,
        com.day.cq.wcm.foundation.TextFormat,
        com.day.cq.wcm.foundation.forms.FormsHelper,
        com.day.cq.wcm.foundation.forms.LayoutHelper,
        com.day.cq.wcm.foundation.forms.ValidationInfo" %><%
    
    final String name = "title";
    final String id = FormsHelper.getFieldId(slingRequest, resource);
    final boolean required = FormsHelper.isRequired(resource);
    final boolean hideTitle = properties.get("hideTitle", false);   
    final String title = FormsHelper.getTitle(resource, "Title");
    List<String> values = FormsHelper.getValuesAsList(slingRequest, resource);
    Map<String, String> displayValues = FormsHelper.getOptions(slingRequest, resource);
    if ( displayValues == null ) {
        displayValues = new LinkedHashMap<String, String>();       
    }
    final boolean multiSelect = FormsHelper.hasMultiSelection(resource);
    final String width = properties.get("width", "");
    
     String helpText = properties.get("helpText", "");
    final boolean hideHelpText = properties.get("hideHelpText", false);
    final String errorLabelCSS = properties.get("errorLabelCSS", "errorLabel");
    if(values.size()<=0 ){
	    ValueMap map = (ValueMap)slingRequest.getAttribute(FormsHelper.REQ_ATTR_GLOBAL_LOAD_MAP);
	    if(map != null){
	    String[] initValues = map.get(name)!= null ? map.get(name,String[].class) : null;
	    if(initValues != null){
	        values = Arrays.asList(initValues);
	        }
	    }else{
	         values=null;
	    }
    }
    ValidationInfo info = ValidationInfo.createValidationInfo(slingRequest);
    String[] errorsInput = info.getErrorMessages(name);
    boolean isErrorInput = errorsInput != null && errorsInput.length > 0;
%>
    <div class="form_row">
       <%if (isErrorInput){%><div class="<%=errorLabelCSS%>"><%}%>            
        <% LayoutHelper.printTitle(id, title, required, hideTitle, out); %>
        <%if (isErrorInput){%></div><%}%>
       <div class="form_rightcol">
        <%
        if ( multiSelect ) {
            %><select class="<%= FormsHelper.getCss(properties, "form_field form_field_select") %>" id="<%=id %>" name="<%=name%>" multiple="multiple" <%= (width.length()>0?"style='width:"+width+"px;'":"") %>><%
        } else {
            %><select class="<%= FormsHelper.getCss(properties, "form_field form_field_select") %>" id="<%=id %>" name="<%=name%>" <%= (width.length()>0?"style='width:"+width+"px;'":"") %>><%
        }
        for(String key : displayValues.keySet()) {
            final String v = key;
            final String t = displayValues.get(key);
            if ( values!=null && values.contains(v) ) {
                %><option value="<%=v%>" selected><%=t%></option><%
            } else {
                %><option value="<%=v%>"><%=t%></option><%                        
            }
        }
        %></select>
          </div>
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