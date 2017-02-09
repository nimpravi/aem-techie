$('document').ready(function(){
var mar=2000,lastImg=1050,count=1,noOfImgs,timer,width,banners=[],bannerImgs,container;
   var interval='false',aniDuration,carousalType,checkFirst=1;
    interval = parseInt($('#playDelay').val(),10);
    if(interval == '0'){
    	interval = 'false';
    }
    aniDuration = parseInt($('#transTime').val(),10);
    carousalType= $('#transType').val();
    noOfImgs= $('.carousel_box').length;
for(i=1;i<=noOfImgs;i++)
$('#imgButs').append('<li id="imgId_'+i+'"><a></a></li>');
if(carousalType == 'slide'){
$('.carousel_box').each(function(){
	banners.push($(this).html());
});
$('.carousel_box').hide();
container=$('<div class="imgContainer"></div>');
bannerImgs=$('<div class="carousel_box"></div>').html(banners[(banners.length-2)]);
container.append(bannerImgs);
bannerImgs=$('<div class="carousel_box"></div>').html(banners[(banners.length-1)]);
container.append(bannerImgs);
for(i=0;i<banners.length;i++){
bannerImgs=$('<div class="carousel_box"></div>').html(banners[i]);
container.append(bannerImgs);
}
bannerImgs=$('<div class="carousel_box"></div>').html(banners[0]);
container.append(bannerImgs);
bannerImgs=$('<div class="carousel_box"></div>').html(banners[1]);
container.append(bannerImgs);
$('#carousel').append(container);
}
else{
	$('.carousel_box').hide();
	$('.bannerTxt').hide();
	$('.bannerTxt:eq(0)').show();
	$('.carousel_box:eq(0)').show();
	$('.carousel_box').addClass('fadeCarouasl');
}
var w=$('#carousel').width();
$('.carousel_box').css('width',w);
$('.banner').css('width',w);
$('.imgContainer').css('left',-(w+w)+'px');
w*=(noOfImgs+4);
if(carousalType == 'slide')
$('.imgContainer').css('width',w);
$('#imgButs li:eq(0)').css('color','grey');
width=$('.carousel_box').css('width');
width=parseInt(width.split('px')[0]);
lastImg=width;
mar=width+width;
if(interval != 'false')
init();
function init(){
	timer=setInterval(next,interval);
} 
for(i=1;i<=noOfImgs;i++)
lastImg+=width;

$('#carousel_prev').on('click', function(ev) {
	prev();
	if(interval != 'false'){
	clearInterval(timer);
    init();
	}
});
$('#carousel_next').on('click', function(ev) {
	next();
	if(interval != 'false'){
	clearInterval(timer);
	init();
	}
});
function next(){
	if(mar>=(lastImg+width)){
	mar=width+width;
	if(carousalType == 'slide') 
	$('.imgContainer').css('left',-(width+width)+'px');
	}
	mar+=width;
	count++;
	if(count>noOfImgs)
	count=1;
 
	if(carousalType == 'slide'){
	  $('.imgContainer').animate({'left':-(mar)+'px'},aniDuration,function(){
		   $('#imgButs li').css('color','#fff'); 
           $('#imgId_'+count).css('color','grey');
	  });
    }
    else{
	  $('.carousel_box').fadeOut(aniDuration);
	  $('.bannerTxt').fadeOut(aniDuration);
	  $('.carousel_box:eq('+(count-1)+')').fadeIn(aniDuration,function(){
		   $('#imgButs li').css('color','#fff'); 
           $('#imgId_'+count).css('color','grey');
			
	  });
	  $('.bannerTxt:eq('+(count-1)+')').fadeIn(aniDuration);
    }
}
function prev(){
  if(mar<=(width+width)){
  mar=lastImg+width;
  if(carousalType == 'slide')
  $('.imgContainer').css('left',-(mar)+'px');
  }
  count--;
  if(count<1)
  count=noOfImgs;
  mar-=width;
  if(carousalType == 'slide'){
   $('.imgContainer').animate({'left':-(mar)+'px'},aniDuration,function(){
	      $('#imgButs li').css('color','#fff');
          $('#imgId_'+count).css('color','grey')
	  });
  }
  else{
	 $('.carousel_box').fadeOut(aniDuration);
	  $('.bannerTxt').fadeOut(aniDuration);
	  $('.carousel_box:eq('+(count-1)+')').fadeIn(aniDuration,function(){
	      $('#imgButs li').css('color','#fff');
          $('#imgId_'+count).css('color','grey')
	  });
	  $('.bannerTxt:eq('+(count-1)+')').fadeIn(aniDuration);
  }
;
}

$('#imgButs li').click(function(){

	if(mar>=(lastImg+width) && $(this).attr('id').split('_')[1]==1)
	mar=lastImg+width;
	else{
	mar=width;
	$imgId=$(this).attr('id').split('_')[1];
	for(i=1;i<=$imgId;i++)
	mar+=width;
	}
	count=$(this).attr('id').split('_')[1];
	if(carousalType == 'slide')
	$('.imgContainer').animate({'left':-(mar)+'px'},aniDuration,function(){
	    
		$('#imgButs li').css('color','#fff');
	    $('#imgButs li:eq('+(count-1)+')').css('color','grey');
	  
     });
    else{
	  if( $('.carousel_box:eq('+(count-1)+')').css('display') == "none"){
	  $('.carousel_box').fadeOut(aniDuration);
	  $('.bannerTxt').fadeOut(aniDuration);
	  }
	  $('.carousel_box:eq('+(count-1)+')').fadeIn(aniDuration,function(){
	    
		$('#imgButs li').css('color','#fff');
	    $('#imgButs li:eq('+(count-1)+')').css('color','grey');
	  
     });
	  $('.bannerTxt:eq('+(count-1)+')').fadeIn(aniDuration);
	  
    }
  
	if(interval != 'false'){
	clearInterval(timer);
	init();
	}
});
});