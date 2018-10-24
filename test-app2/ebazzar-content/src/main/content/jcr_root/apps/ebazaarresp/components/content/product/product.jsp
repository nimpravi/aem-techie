
<%--

  Product component.

  Product Short Description

--%>
<%@include file="/libs/foundation/global.jsp"%>

<%@page import="javax.jcr.nodetype.PropertyDefinition"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%>


<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collections"%>

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%
	
	
	

    Logger logger = LoggerFactory.getLogger("Product");

    Map<String, String> productDetails = new HashMap<String, String>();
Map<String, String> productSpecs = new HashMap<String, String>();
ArrayList<String> prodSpecsList = new ArrayList<String>();
    ArrayList<String> relatedProdsPath = new ArrayList<String>();
    

    String prodPath = properties.get("productpath", "");

    String productCode = properties.get("prodCode", "");

slingRequest.setAttribute("productCode",productCode);

    String productCategory = properties.get("category", "");
slingRequest.setAttribute("productPath",productCategory);
    if(null != pageManager.getPage(productCategory)){
    NodeIterator prodcatIter = pageManager.getPage(productCategory)
            .adaptTo(Node.class).getNodes();
    while (prodcatIter.hasNext()) {
        Node jcrNode = prodcatIter.nextNode();
        NodeIterator prodIter = jcrNode.getNodes();
        while (prodIter.hasNext()) {
            Node prodjcrNode = prodIter.nextNode();
            NodeIterator prodNodeIter = prodjcrNode.getNodes();
            while (prodNodeIter.hasNext()) {
                Node prodNode = prodNodeIter.nextNode();
                String prodCode = (prodNode
                        .hasProperty("prodCode")) ? prodNode
                        .getProperty("prodCode").getValue().getString()
                        : "";
                String prodDescription = (prodNode
                        .hasProperty("prodDescription")) ? prodNode
                        .getProperty("prodDescription").getValue()
                        .getString() : "";
                String prodTitle = (prodNode
                        .hasProperty("prodTitle")) ? prodNode
                        .getProperty("prodTitle").getValue()
                        .getString() : "";
                String fileReference = (prodNode
                        .hasProperty("fileReference")) ? prodNode
                        .getProperty("fileReference").getValue()
                        .getString() : "";
                        String prodPrice = (prodNode
                                .hasProperty("prodPrice")) ? prodNode
                                .getProperty("prodPrice").getValue()
                                .getString() : "";
                                
                                String prodStoreLoc = (prodNode
                                        .hasProperty("prodStoreLoc")) ? prodNode
                                        .getProperty("prodStoreLoc").getValue()
                                        .getString() : "";
                                        String prodOffer = (prodNode
                                                .hasProperty("prodOffer")) ? prodNode
                                                .getProperty("prodOffer").getValue()
                                                .getString() : "";

                if(prodNode.hasProperty("productSpecs")){
                    prodSpecsList = new ArrayList<String>();
                Property prodSpecsProp = prodNode.getProperty("productSpecs");
        if (prodSpecsProp.getDefinition().isMultiple()) {
            Value[] prodSpecsValue = prodSpecsProp.getValues();

            for (int i = 0; i < prodSpecsValue.length; i++) {
                if (null != prodSpecsValue[i].getString()
                        && !(prodSpecsValue[i].getString().isEmpty())) {

                        prodSpecsList.add(prodSpecsValue[i]
                                .getString());

                }

            }


        }



                    else {
            if (null != prodSpecsProp.getValue().getString()
                    && !(prodSpecsProp.getValue().getString()
                            .isEmpty())) {

                    logger.error("Inside existing loop");
                    prodSpecsList.add(prodSpecsProp.getValue()
                            .getString());

            }
        }
            }




                if (prodCode.equals(productCode)) {
                    productSpecs = new HashMap<String, String>();

                    slingRequest.setAttribute("prodStoreLoc",prodStoreLoc);
                    productDetails.put("prodCode", prodCode);
                    productDetails.put("prodDescription",
                            prodDescription);
                    productDetails.put("prodTitle", prodTitle);
                    productDetails.put("prodPrice", prodPrice);
                    productDetails.put("prodStoreLoc", prodStoreLoc);
                    productDetails.put("prodOffer", prodOffer);
                    productDetails.put("fileReference", fileReference);
                    productDetails.put("viewmore",productCategory+".html?prodCode="+prodCode);
                    if(prodSpecsList.size()>0){
                        for(int i=0;i<prodSpecsList.size();i++){
String prodSpec = prodSpecsList.get(i);

                            if (prodSpec.contains(":")) {

                                 String[] parts = prodSpec.split(":");
String part1 = parts[0].trim(); 
String part2 = parts[1].trim();
                                productSpecs.put(part1,part2);


}

                        }
                    }

                }
            }
        }
    }
    }

    

%>

<%@page import="com.day.cq.dam.api.Asset"%>



<script type="text/javascript">


function productsvalue(box,value){
var panel = box.findParentByType('panel').findById('categoryProduct');
var completeNewsResults = [];

var url = "/bin/productList";

     var prodCode = "<%=properties.get("prodCode","")%>";
    $.ajax({
        url :url,
        async :true,
        data: {"category": value },
        type: "GET",
        error: function(obj) {
            completeNewsResults = obj;  
            console.log(obj); 
            CQ.Ext.Msg.show({
                title:CQ.I18n.getMessage('Validation Failed'),
                msg: CQ.I18n.getMessage('Please provide a valid url to get the latest products.'),
                buttons: CQ.Ext.Msg.OK,
                icon: CQ.Ext.Msg.ERROR
            });
        
        
        },
        success : function(obj) {
            var select2opts = [];
            completeNewsResults = obj;  
            console.log(completeNewsResults);
          //  panel.setValue(select2opts);
            panel.setOptions(select2opts); 
            
            if(completeNewsResults != ""){   
          var allNewsItems = completeNewsResults.items;
          console.log(allNewsItems); 
          $.each(allNewsItems, function(){
          select2opts.push({value: this.value, text:this.text});  
          
         });
      }
            panel.setOptions(select2opts);
            if(select2opts != ""  ){
             panel.setValue(prodCode);}
            else{
                panel.setValue(""); }
            console.log("panel beofre"+panel);   
        }
        
    });
    // end of ajax call
}



</script>

<%if(currentDesign.getPath().equals("/etc/designs/philips")){%>

   <%@include file="product1.jsp"%> 

<%}
else{%>
	<%@include file="product2.jsp"%>	
	<%
}
%>