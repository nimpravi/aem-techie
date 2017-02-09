/*this method will check if atleast one banner has been set for careercarousel*/
function successSubmit(dialog){

    var mt = dialog.findByType("mtmulticompositefield")[0].find("fieldLabel","Description");
    if(mt == 0){
    	CQ.Ext.MessageBox.alert(CQ.I18n.getMessage("Error"), CQ.I18n
                .getMessage("Please configure atleast one Banner for Carousel"));
        return false;
        }
	return true;
}
//*End of method