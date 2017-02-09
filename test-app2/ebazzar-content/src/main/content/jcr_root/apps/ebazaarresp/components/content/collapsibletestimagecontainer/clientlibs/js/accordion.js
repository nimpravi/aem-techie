$(document).ready(function(){
var pathname = $(location).attr('href');
keyword=pathname.split('#')[1];
	var currItem;
	$('#myAccordian .accItem .accContent').css('display','none');
	$('#myAccordian .accItem .collapsesign').html('+');
    if(keyword != ""){
    $('#'+keyword+' .collapsesign').html('-');
	$('#'+keyword+' .accContent').slideDown();
    }
	$('#myAccordian .accItem .collapsesign,#myAccordian .accItem h5').on('click',function(){
		currItem=$(this).parent().attr('id');
        $('#myAccordian .accContent').slideUp();
        $('#myAccordian .collapsesign').html('+');
        if($('#'+currItem+' .accContent').css('display') == 'none'){
        $('#'+currItem+' .accContent').slideDown();
        $('#'+currItem+' .collapsesign').html('-');
        
        }    
	});
});