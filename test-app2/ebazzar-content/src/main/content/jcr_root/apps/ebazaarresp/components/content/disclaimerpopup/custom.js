$(document).ready(function(){
    
    $('.show-popup').click(function(event){
        event.preventDefault(); 
         
        var docHeight = $(document).height(); 
        var scrollTop = $(window).scrollTop(); 
        $('.overlay-bg').show().css({'height' : docHeight}); 
        $('.overlay-content').css({'top': scrollTop+20+'px'}); 
    });
 
    
    $('.close-btn').click(function(){
        $('.overlay-bg').hide(); 
    });
 
   
    $('.overlay-bg').click(function(){
        $('.overlay-bg').hide();
    })
   
    $('.overlay-content').click(function(){
        return false;
    });
 
});