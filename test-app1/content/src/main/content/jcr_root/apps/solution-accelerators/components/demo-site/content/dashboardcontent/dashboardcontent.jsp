<script id="topMenu-dt-template" type="text/x-handlebars-template">
			<ul>
			{{#each objects}}
				<li class="mainnavigationitem-dt">
					<a id="top{{@index}}" href="{{#chgBlankURL}}{{url}}{{/chgBlankURL}}" class="mainNavLink-dt">{{#toUpper}}{{name}}{{/toUpper}}</a>
				</li>
			{{/each}}
			</ul>
		</script>
		<script id="secMenu-dt-template" type="text/x-handlebars-template">
			{{#each objects}}
				<div class="secNavigationitem-dt"><a href="{{#chgBlankURL}}{{url}}{{/chgBlankURL}}" class="secNavLink-dt"><img src="../images/Bullet_For_Title.png">{{#toUpper}}{{name}}{{/toUpper}}</a>
				{{#each pages}}
					<div class="pgNavigationitem-dt"><a href="{{#chgBlankURL}}{{url}}{{/chgBlankURL}}" class="pgNavLink-dt"><img src="../images/Bullet_for_content.png">{{name}}</a></div>
				{{/each}}
				</div>
			{{/each}}
		</script>
		<script id="topMenu-template" type="text/x-handlebars-template">
			<ul>
			{{#each objects}}
				<li class="mainnavigationitem">
					<a id="top{{@index}}" href="{{#chgBlankURL}}{{url}}{{/chgBlankURL}}" class="mainNavLink">{{#toUpper}}{{name}}{{/toUpper}}</a>
					{{#if sections.length}}
						<a href="#" class="leftMenuArrow" onclick="app.menu.createSecMenu({{@index}});return false;" class="btn"><img src="/content/dam/demo-site/arrow_6x11.png"></a>
					{{/if}}
				</li>
			{{/each}}
			</ul>
		</script>
		<script id="secMenu-template" type="text/x-handlebars-template">
			<ul>
			{{#each objects}}
				<li class="secnavigationitem">
					<a id="top{{@index}}" href="{{#chgBlankURL}}{{url}}{{/chgBlankURL}}" class="secNavLink">{{#toUpper}}{{name}}{{/toUpper}}</a>
					{{#if pages.length}}
						<a href="#" class="leftMenuArrow" onclick="app.menu.createPageMenu({{@index}});return false;" class="btn"><img src="/content/dam/demo-site/arrow_6x11.png"></a>
					{{/if}}
				</li>
			{{/each}}
			</ul>
		</script>

			<!-- PageContent Start-->
				<div id="pgContent" class="row">

				<div class="lowerheader">
					<div class="pageheading">
						<div><h1 id="pgTitle" class="heading propText">Dashboard</h1></div>
					</div>
					<div class="searchcontainer">
						<div class="search">
							<div class="dashboardsearch">
								<div class="geologo" onMouseOver="showPropInfo('slow')" onMouseOut="hidePropInfo()"></div>
								<div class="aboutproperty">
                                	
                                	<a href="#" class="propText" onMouseOver="showPropInfo('slow')" onMouseOut="hidePropInfo()">About Property</a>
                                </div>
								<div class="propSepLi"><span class="propSep"></span></div>
                                <!--<span class="searchText">Search</span>-->
                                <label for="searchsite" class="searchText">Search: </label>
                                <div class="searchInput">
                                	<input type="text" id="searchsite" name="searchmembersite" placeholder="Search Member Site" >
                                    <div id="searchDropDownId">
                                    	<div class="textDiv"><a href="#"><p class="firstWordSearch">William</p></a></div>
                                        <div class="textDiv"><a href="#"><p class="firstWordSearch">William</p> Pitt (Affiliate)</a></div>
                                        <div class="textDiv"><a href="#"><p class="firstWordSearch">William</p> Holden (Agent)</a></div>
                                        <div class="textDiv"><a href="#"><p class="firstWordSearch">William</p> Shew (Broker)</a></div>
                                        <div class="textDiv"><a href="#"><p class="firstWordSearch">William</p> Adams (Marketing Officer)</a></div>
                                    </div>
                                    <a href="#" class="magnify">
                                    	<img src="/content/dam/demo-site/Magnifying_class.png">
                                    </a>
                                </div>
                        	</div>
							
						</div>
					</div>

				</div>

				

				<div class="dashboard-body announcementMainTab">
        	<div class="tabs-heading tabs show-tablet-land">
            	<ul class="nav nav-tabs" id="dashboardTab">
                  <li class="active"><a href="#home" data-toggle="tab" class="announcement">Announcement</a></li>
                  <li><span id="nav-Left"></span><a href="#profile" data-toggle="tab" class="directory">Directory</a><span id="nav-Right"></span></li>
                  <li><a href="#messages" data-toggle="tab" class="newsEvents">News and Events</a></li>
				</ul>
            </div>
	        <div class="video">
            	<div></div>
            </div>
            <div id="carousel-announcement" class="carousel slide" data-ride="carousel" data-interval="0">
                <!-- Indicators -->
                  <!-- Wrapper for slides -->
                  <div class="carousel-inner">
                    <div class="item active">
						<h2 class="announcement-title announcement-title-desktop">Announcement placeholder title would come here...
                        </h2>
						<h2 class="announcement-title announcement-title-mobile">
                        	Announcement placeholder title would come here...Lorem Ipsum
                      	</h2>
                      
                      <p class="announcement-content dashboard-desktop">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque consectetur elementum ipsum eget rutrum Integer
                      </p>
                      <p class="announcement-content dashboard-mobile">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque
                      </p>
                      <p class="announcement-content dashboard-mobile-land-b">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ips dolor sit Pellentesque Lorem ipsum
                      </p>
                      
                      <a href="#" title="Read More Announcements" class="other-links">Read More</a>
                    </div>
                    <div class="item">
                      <h2 class="announcement-title announcement-title-desktop">Announcement placeholder title1 would come here...
                        </h2>
						<h2 class="announcement-title announcement-title-mobile">
                        	Announcement placeholder title1 would come here...Lorem Ipsum
                      	</h2>
                      
                      <p class="announcement-content dashboard-desktop">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque consectetur elementum ipsum eget rutrum Integer
                      </p>
                      <p class="announcement-content dashboard-mobile">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque
                      </p>
                      <p class="announcement-content dashboard-mobile-land-b">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ips dolor sit Pellentesque Lorem ipsum
                      </p>
                      
                      <a href="#" title="Read More Announcements">Read More</a>
                    </div>
                    <div class="item">
                      <h2 class="announcement-title announcement-title-desktop">Announcement placeholder title2 would come here...
                        </h2>
						<h2 class="announcement-title announcement-title-mobile">
                        	Announcement placeholder title2 would come here...Lorem Ipsum
                      	</h2>
                      
                      <p class="announcement-content dashboard-desktop">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque consectetur elementum ipsum eget rutrum Integer
                      </p>
                      <p class="announcement-content dashboard-mobile">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque
                      </p>
                      <p class="announcement-content dashboard-mobile-land-b">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ips dolor sit Pellentesque Lorem ipsum
                      </p>
                      
                      <a href="#" title="Read More Announcements" class="other-links">Read More</a>
                    </div>
                     <div class="item">
                     <h2 class="announcement-title announcement-title-desktop">Announcement placeholder title3 would come here...
                        </h2>
						<h2 class="announcement-title announcement-title-mobile">
                        	Announcement placeholder title3 would come here...Lorem Ipsum
                      	</h2>
                      
                      <p class="announcement-content dashboard-desktop">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque consectetur elementum ipsum eget rutrum Integer
                      </p>
                      <p class="announcement-content dashboard-mobile">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque
                      </p>
                      <p class="announcement-content dashboard-mobile-land-b">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ips dolor sit Pellentesque Lorem ipsum
                      </p>
                      
                      <a href="#" title="Read More Announcements" class="other-links">Read More</a>
                    </div>
                     <div class="item">
                      <h2 class="announcement-title announcement-title-desktop">Announcement placeholder title4 would come here...
                        </h2>
						<h2 class="announcement-title announcement-title-mobile">
                        	Announcement placeholder title4 would come here...Lorem Ipsum
                      	</h2>
                      
                      <p class="announcement-content dashboard-desktop">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque consectetur elementum ipsum eget rutrum Integer
                      </p>
                      <p class="announcement-content dashboard-mobile">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque
                      </p>
                      <p class="announcement-content dashboard-mobile-land-b">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ips dolor sit Pellentesque Lorem ipsum
                      </p>
                      
                      <a href="#" title="Read More Announcements" class="other-links">Read More</a>
                    </div>
                    
                  </div>
                  <div class="carrousel-nav">
                    <a class="left carousel-control" href="#carousel-announcement" data-slide="prev" title="Previous link">
                    	<img src="/content/dam/demo-site/Left_arrow.png" alt="Previous Link" class="leftImgdesktop">
                        <img src="/content/dam/demo-site/back_arrow_12x20.png" alt="Previous Link" class="leftImgtouch">
                    </a>
                    <span class="span-left"></span>
                     <ol class="carousel-indicators">
                        <li data-target="#carousel-announcement" data-slide-to="0" class="active"></li>
                        <li data-target="#carousel-announcement" data-slide-to="1"></li>
                        <li data-target="#carousel-announcement" data-slide-to="2"></li>
                        <li data-target="#carousel-announcement" data-slide-to="3"></li>
                        <li data-target="#carousel-announcement" data-slide-to="4"></li>
                    </ol>
                    <span class="span-middle"></span>
                     <a class="right carousel-control" href="#carousel-announcement" data-slide="next" title="Next link">
                     	<img src="/content/dam/demo-site/Right_arrow_blue.png" alt="Next Link" class="leftImgdesktop">
                        <img src="/content/dam/demo-site/arrow_next_mobile.png" alt="Next Link" class="leftImgtouch">
                    </a>
                    <span class="span-right"></span>
                    <a class="play" href="#" title="Pause">
                    	<img src="/content/dam/demo-site/Pause_button_blue.png" alt="Pause" class="leftImgdesktop">
                        <img src="/content/dam/demo-site/pause_mobile.png" alt="Pause" class="leftImgtouch">
                    </a>
                  </div>
        
                  <!-- Controls -->
                 
            </div>
            
            <div class="tabs-heading tabs hide-tablet-land">
            	<ul class="nav nav-tabs dashboardTabMobile">
                  <li class="active">
                  	<a href="#home" data-toggle="tab" class="announcement actionTabDevice" title="Announcement">Announcement</a>
                  	<a href="#" class="announcement actionTabDesktop" title="Announcement">Announcement</a>
                  </li>
                  <li>
                  	<span class="sepe"></span>
                  	<a href="#profile" data-toggle="tab" class="directory actionTabDevice" title="Directory">Directory</a>
                    <a href="#"  class="directory actionTabDesktop" title="Directory" onclick="showDir()">Directory</a>
                    <!--<span id="nav-RightMobile"></span>-->
                  </li>
                  <li>
                  	<span class="sepe"></span>
                  	<a href="#messages" data-toggle="tab" class="newsEvents actionTabDevice" title="News and Events">News and Events</a>
                    <a href="#" class="newsEvents actionTabDesktop" title="News and Events" onclick="showNews()">News and Events</a>
                  </li>
				</ul>
            </div>
        </div>
        
        <!--Directory tab start -->
        <div class="dashboard-body directoryBody">
        <img src="/content/dam/demo-site/Directory_Page.png">
        	<div class="tabs-heading tabs hide-tablet-land">
            	<ul class="nav nav-tabs dashboardTabMobile">
                  <li class="active">
                  	<a href="#" class="announcement actionTabDesktop" title="Announcement" onclick="showAnnounce()">Announcement</a>
                  </li>
                  <li>
                  	<span class="sepe"></span>
                  	<a href="#"  target="_self" class="directory actionTabDesktop" title="Directory" onclick="showDir()">Directory</a>
                    <!--<span id="nav-RightMobile"></span>-->
                  </li>
                  <li>
                  	<span class="sepe"></span>
                    <a href="#" target="_self" class="newsEvents actionTabDesktop" title="News and Events" onclick="showNews()">News and Events</a>
                  </li>
				</ul>
            </div>
        </div>
          <!--Directory tab end -->
          <!--newsEvents tab start -->
        <div class="dashboard-body newsEventsBody">
         <img id="newsEventImgId" src="/content/dam/demo-site/News_and_Events_Press_Releases_page.png" usemap="#Map">
 <map name="Map">
   <area shape="rect" coords="67,22,192,48" href="#" onclick="changeToPress()">
   <area shape="rect" coords="192,23,259,48" href="#" onclick="changeToBlog()">
   <area shape="rect" coords="259,23,333,48" href="#" onclick="changeToEvents()">
 </map>
        	<div class="tabs-heading tabs hide-tablet-land">
            	<ul class="nav nav-tabs dashboardTabMobile">
                  <li class="active">
                  	
                  	<a href="#" class="announcement actionTabDesktop" title="Announcement" onclick="showAnnounce()">Announcement</a>
                  </li>
                  <li>
                  	
                  	<span class="sepe"></span>
                    <a href="#"  target="_self" class="directory actionTabDesktop" title="Directory" onclick="showDir()">Directory</a>
                    <!--<span id="nav-RightMobile"></span>-->
                  </li>
                  <li>
                  	<span class="sepe"></span>
                    <a href="#" target="_self" class="newsEvents actionTabDesktop" title="News and Events" onclick="showNews()">News and Events</a>
                  </li>
				</ul>
            </div>
        </div>
        <!--newsEvents tab end -->
        <div class="dashboard-others dashboard-others-mob-land dashboard-others-tab-land dashboard-list-tab-pot">
        	<div class="title">
            	<div><h2 class="widgetHead">Listings</h2></div>
                <div>
                	<a href="#" onclick="return false" class="actionTabDevice">
	                <ul>
    	            	<li></li>
        	            <li></li>
            	        <li></li>
                	</ul>
                  	</a>
                    <a href="#" class="actionTabDesktop" onclick="showWidgetOption()">
	                <ul>
    	            	<li></li>
        	            <li></li>
            	        <li></li>
                	</ul>
                  	</a>
                  </div>
                  <div class="widgetOption">
                  	<div><a href="#">Help</a></div>
                    <div><a href="#">Refresh Widget</a></div>
                    <div><a href="" data-toggle="modal" data-target="#editDashId" onclick="editDashBoard()">Personalize Dashboard</a></div>
                  </div>
             </div>
           	<div class="content content-listings content-mob-land">
            	<div>
                	<p>Active Listings</p>
                    <p>223</p>
                </div>
		       	<div>
                	<p>Under Contract</p>
                    <p>001</p>
                </div>
                <div>
                	<p>Closed in last 30 min days</p>
                    <p>005</p>
                </div>
                <p class="price">Average List Price:</p>
                <p class="actual-price">US$ 860,580</p>
                <a href="#" class="other-links" title="View More Listings">View More</a>
            </div>
        </div>
        <div class="dashboard-others lead-router dashboard-router-tab-pot">
        	<div class="title">
            	<div><h2 class="widgetHead">Lead Router</h2></div>
                <div>
                <a href="#" onclick="showWidgetOption('1')">
	                <ul>
    	            	<li></li>
        	            <li></li>
            	        <li></li>
                	</ul>
                    </a>
                 </div>
                 <div class="widgetOption1">
                  	<div><a href="#">Help</a></div>
                    <div><a href="#">Refresh Widget</a></div>
                    <div><a href="" data-toggle="modal" data-target="#editDashId" onclick="editDashBoard('1')">Personalize Dashboard</a></div>
                  </div>
             </div>
           	<div class="content">
            	<div id="carousel-leadrouter" class="carousel slide" data-ride="carousel" data-interval="0">
                <!-- Indicators -->
                  <!-- Wrapper for slides -->
                  <div class="carousel-inner">
                    <div class="item active">
                      <span class="critical-error">
                        <img src="/content/dam/demo-site/Critical_Error.png" alt="Critical Error">
                      </span>
                      <p class="critical-error-msg">
                       You have not updated your leads
                      </p>
                      <div class="h-sep">
                      </div>
                      <p class="critical-info-msg">
                       As per your company's settings, this may affect your eligibility to receive new Leads
                      </p>
                      <a href="#" class="other-links dashboard-mobile-router">Update Leads</a>
                    </div>
                    <div class="item">
                        <span class="critical-error">
                        
                      </span>
                      <p class="critical-error-msg">
                       You have not updated your leads
                      </p>
                      <div class="h-sep">
                      </div>
                      <p class="critical-info-msg">
                       As per you company's settings, this may affect your eligibility to receive new Leads
                      </p>
                      <a href="#" class="other-links dashboard-mobile-router">Update Leads</a>
                    </div>
                    <div class="item">
                      	 <span class="critical-error">
                        
                      </span>
                      <p class="critical-error-msg">
                       You have not updated your leads
                      </p>
                      <div class="h-sep">
                      </div>
                      <p class="critical-info-msg">
                       As per you company's settings, this may affect your eligibility to receive new Leads
                      </p>
                      <a href="#" class="other-links dashboard-mobile-router">Update Leads</a>
                    </div>
                     <div class="item">
                      	 <span class="critical-error">
                        
                      </span>
                      <p class="critical-error-msg">
                       You have not updated your leads
                      </p>
                      <div class="h-sep">
                      </div>
                      <p class="critical-info-msg">
                       As per you company's settings, this may affect your eligibility to receive new Leads
                      </p>
                      <a href="#" class="other-links dashboard-mobile-router">Update Leads</a>
                    </div>
                     <div class="item">
                        <span class="critical-error">
                        
                      </span>
                      <p class="critical-error-msg">
                       You have not updated your leads
                      </p>
                      <div class="h-sep">
                      </div>
                      <p class="critical-info-msg">
                       As per you company's settings, this may affect your eligibility to receive new Leads
                      </p>
                      <a href="#" class="other-links dashboard-mobile-router">Update Leads</a>
                    </div>
                    
                  </div>
                  <div class="carrousel-nav">
                    <a class="left carousel-control" href="#carousel-leadrouter" data-slide="prev" title="Previous">
                    	<img src="/content/dam/demo-site/Left_arrow.png" alt="Previous Link" class="leftImgdesktop">
                        <img src="/content/dam/demo-site/back_arrow_12x20.png" alt="Previous Link" class="leftImgtouch">
                    </a>
                    <span class="span-left"></span>
                     <ol class="carousel-indicators">
                        <li data-target="#carousel-leadrouter" data-slide-to="0" class="active"></li>
                        <li data-target="#carousel-leadrouter" data-slide-to="1"></li>
                        <li data-target="#carousel-leadrouter" data-slide-to="2"></li>
                        <li data-target="#carousel-leadrouter" data-slide-to="3"></li>
                        <li data-target="#carousel-leadrouter" data-slide-to="4"></li>
                    </ol>
                    <span class="span-middle"></span>
                     <a class="right carousel-control" href="#carousel-leadrouter" data-slide="next" title="Next">
                     	<img src="/content/dam/demo-site/Right_arrow_blue.png" alt="Next Link" class="leftImgdesktop">
                        <img src="/content/dam/demo-site/arrow_next_mobile.png" alt="Next Link" class="leftImgtouch">
                    </a>
                   
                  </div>
        
            </div>
             <a href="#" class="other-links dashboard-desktop">Update Leads</a>
        </div>
         
      </div>
      <div class="dashboard-others rezora-dashboard">
        	<div class="title">
            	<div><h2 class="widgetHead">Rezora</h2></div>
                <div>
					<a href="#" onclick="showWidgetOption('2')">
	                <ul>
    	            	<li></li>
        	            <li></li>
            	        <li></li>
                	</ul>
                    </a>
                 </div>
                 <div class="widgetOption2">
                  	<div><a href="#">Help</a></div>
                    <div><a href="#">Refresh Widget</a></div>
                    <div><a href="" data-toggle="modal" data-target="#editDashId" onclick="editDashBoard('2')">Personalize Dashboard</a></div>
                  </div>

             </div>
           		<div class="content content-listings rezora rezora-tab">
            		<div class="tabs" id="rezora">
                		<a href="#" onclick="return false" class="left">
                        	<img src="/content/dam/demo-site/Tab_Left_Arrow.png" alt="Previous Link">
                        </a>
                         <ul class="nav nav-tabs" id="rezoraTabs">
                          <li class="active">
                          	<a href="#home" data-toggle="tab" class="currCam" onclick="changeRezoraTab('currCam',1)">Current Campaign</a>
                          </li>
                          <li>
                          	<span class="sepe"></span>
                            <a href="#profile" data-toggle="tab" class="lastCam" onclick="changeRezoraTab('lastCam','Last Campaign')" >Last Campaign</a>
                           
                          </li>
                          <li>
	                      	 <span class="sepe"></span>
                          	<a href="#messages" data-toggle="tab" class="hide-mob-land rezoraolder olderCam" onclick="changeRezoraTab('olderCam','Older')">Older</a>
                            <a href="#messages" data-toggle="tab" class="show-mob-land lorem1Cam" onclick="changeRezoraTab('lorem1Cam','Lorem Ipsum')">Lorem Ipsum</a>
                           </li>
                            <li class="rezora-tabs-tab-pot">
                          	<a href="#messages" data-toggle="tab" class="show-mob-land lorem2Cam" onclick="changeRezoraTab('lorem2Cam','Lorem Ipsum')">Lorem Ipsum</a>
                           </li>
                           <li class="rezora-tabs-tab-pot">
                          	<a href="#messages" data-toggle="tab" class="show-mob-land lorem3Cam" onclick="changeRezoraTab('lorem3Cam','Lorem Ipsum')">Lorem Ipsum</a>
                           </li>
                           <li class="rezora-tabs-tab-pot">
                          	<a href="#messages" data-toggle="tab" class="show-mob-land lorem4Cam" onclick="changeRezoraTab('lorem4Cam','Lorem Ipsum')">Lorem Ipsum</a>
                           </li>
                           <li class="rezora-tabs-tab-pot">
                          	<a href="#messages" data-toggle="tab" class="show-mob-land lorem5Cam" onclick="changeRezoraTab('lorem5Cam','Lorem Ipsum')">Lorem Ipsum</a>
                           </li>
                        </ul>
                    	<a href="#" onclick="return false" class="right">
                        	<img src="/content/dam/demo-site/Right_arrow_Rezora.png" alt="Next Link">
                        </a>
               		</div>
                   
                        <div>
                            <p>Campaign Name would be shown here</p>
                        </div>
                        <div>
                            <p>Open Rate</p>
                            <p>23.0 %</p>
                        </div>
                         <div>
                            <p>Click Rate</p>
                            <p>02.9 %</p>
                        </div>
                         <div>
                            <p>Bounce Rate</p>
                            <p>01.8 %</p>
                        </div>
                         <div>
                            <p>Spam Rate</p>
                            <p>00.1 %</p>
                        </div>
                       <div>
                            <p>Viewed on platforms</p>
                        </div>
                    	<div class="show-per">
                            <div class="main-per">
                                <div class="display-per"><p>23.0</p><p>%</p>
                                </div>
                                <p>Lorem
                                </p>
                            </div>
                            <div class="main-per">
                                <div class="display-per"><p>15.5</p><p>%</p>
                                </div>
                                <p>Ipsum</p>
                            </div>
                            <div class="main-per">
                                <div class="display-per"><p>10</p><p>%</p>
                                </div>
                                <p>Param</p>
                            </div>
                    	</div>
                   
              </div>
              <div class="content content-listings rezora tab-content">
              		<div class="tabs" id="rezora">
                		<a href="#" onclick="return false" class="left">
                        	<img src="/content/dam/demo-site/Tab_Left_Arrow.png" alt="Previous Link">
                        </a>
                         <ul class="nav nav-tabs" id="rezoraTabs">
                          <li class="active">
                          	<a href="#home" data-toggle="tab" class="currCam" onclick="changeRezoraTab('currCam',1)">Current Campaign</a>
                          </li>
                          <li><span class="sepe"></span><a href="#profile" data-toggle="tab" class="lastCam" onclick="changeRezoraTab('lastCam','Last Campaign')">Last Campaign</a>
                           
                          </li>
                          <li>
	                      	 <span class="sepe"></span>
                          	<a href="#messages" data-toggle="tab" class="hide-mob-land rezoraolder olderCam" class="olderCam" onclick="changeRezoraTab('olderCam','Older')">Older</a>
                            <a href="#messages" data-toggle="tab" class="show-mob-land lorem1Cam" onclick="changeRezoraTab('lorem1Cam','Lorem Ipsum')">Lorem Ipsum</a>
                           </li>
                            <li class="rezora-tabs-tab-pot">
                          	<a href="#messages" data-toggle="tab" class="show-mob-land lorem2Cam" onclick="changeRezoraTab('lorem2Cam','Lorem Ipsum')">Lorem Ipsum</a>
                           </li>
                           <li class="rezora-tabs-tab-pot">
                          	<a href="#messages" data-toggle="tab" class="show-mob-land lorem3Cam" onclick="changeRezoraTab('lorem3Cam','Lorem Ipsum')">Lorem Ipsum</a>
                           </li>
                           <li class="rezora-tabs-tab-pot">
                          	<a href="#messages" data-toggle="tab" class="show-mob-land lorem4Cam" onclick="changeRezoraTab('lorem4Cam','Lorem Ipsum')">Lorem Ipsum</a>
                           </li>
                           <li class="rezora-tabs-tab-pot">
                          	<a href="#messages" data-toggle="tab" class="show-mob-land lorem5Cam"  onclick="changeRezoraTab('lorem5Cam','Lorem Ipsum')">Lorem Ipsum</a>
                           </li>
                        </ul>
                    	<a href="#" onclick="return false" class="right">
                        	<img src="/content/dam/demo-site/Right_arrow_Rezora.png" alt="Next Link">
                        </a>
               		</div>
                    <div id="rezoraTabChange"></div>
              </div>
              	
        </div>
        <div class="dashboard-others favorites">
        	<div class="title">
            	<div><h2 class="widgetHead">Favorite Links</h2></div>
                <div>
                    <a href="#" onclick="showWidgetOption('3')">
                            <ul>
                                <li></li>
                                <li></li>
                                <li></li>
                            </ul>
                        </a>
                        <div class="widgetOption3">
                            <div><a href="#">Help</a></div>
                            <div><a href="#">Refresh Widget</a></div>
                            <div><a href="" data-toggle="modal" data-target="#editDashId" onclick="editDashBoard('3')">Personalize Dashboard</a></div>
                  		</div>
                  </div>
                 
             </div>
            <div class="content content-listings" id="fav">
            	<div class="tabs" id="rezoraFav">
                		<a href="#" onclick="return false" class="left fav-left-tab">
                        	<img src="/content/dam/demo-site/Left_Arrow_Fav_links.png" alt="Previous Link">
                        </a>
                        <ul class="nav nav-tabs" id="favTabs">
                          <li class="active">
                          	<a href="#home" data-toggle="tab" class="dashboard-desktop-inlinebloc" onclick="changeFavTab()">Favorite Links Category 1</a>
                            <a href="#home" data-toggle="tab" class="dashboard-mobile-inlinebloc" onclick="changeFavTab()">Category 1</a>
                           </li>
                          <li>
                          	<span id="nav-LeftList" class="sepe"></span>
                            <a href="#home" data-toggle="tab" class="dashboard-desktop-inlinebloc" onclick="changeFavTab('2')">Favorite Links Category 2</a>
                            <a href="#home" data-toggle="tab" class="dashboard-mobile-inlinebloc" onclick="changeFavTab('2')">Category 2</a>
                            
                          </li>
                          <li class="fav-hide-mob-land">
                          	<span id="nav-RightFav" class="sepe"></span>
                          	<a href="#home" data-toggle="tab" class="dashboard-desktop-inlinebloc fav-hide-mob-land" onclick="changeFavTab('3')">Favorite Links Category 3</a>
                          </li>
                          <li class="fav-hide-mob-land fav-hide-tab-pot">
							<span id="nav-RightFav1" class="sepe"></span>
                          	<a href="#home" data-toggle="tab" class="dashboard-desktop-inlinebloc" onclick="changeFavTab('4')">Favorite Links Category 4</a>
                           </li>
                        </ul>
                    	<a href="#" onclick="return false" class="right fav-right-tab">
                        	<img src="/content/dam/demo-site/Right_arrow_FavLinks_blue.png" alt="Next Link">
                        </a>
               		</div>
                    <a href="#" onclick="return false" class="main-left">
                    </a>
                    <div class="fav-options">
                    	<div class="fav-inner-options">
                            <div class="adstudio">
                                
                                <div class="orange adstudio"><a href="#">T</a><div class="updated"></div></div>
                                <p><a href="#">Ad Studio</a></p>
                            </div>
                            <div>
                                <div class="black"><a href="#">M</a></div>
                                <p><a href="#">Reside Magazine</a></p>
                            </div>
                            <div>
                                <div class="orange"><a href="#">T</a></div>
                                 <p><a href="#">eGallery</a></p>
                            </div>
                        
                            <div>
                                <div class="orange"><a href="#">T</a></div>
                                <p><a href="#">Presentation Studio</a></p>
                            </div>
                            <div>
                                <div class="orange"><a href="#">T</a></div>
                                <p><a href="#">List Hub</a></p>
                            </div>
                            <div>
                                <div class="offyellow"><a href="#">O</a></div>
                                <p><a href="#">Running Your Business</a></p>
                            </div>
                            <div>
                                <div class="offyellow"><a href="#">O</a></div>
                                <p><a href="#">Signage</a></p>
                            </div>
                            <div>
                                <div class="green"><a href="#">T</a></div>
                                <p><a href="#">Tutorial</a></p>
                            </div>
                            <div>
                                <div class="black"><a href="#">M</a></div>
                                <p><a href="#">Training</a></p>
                            </div>
                            <div>
                                <div class="black"><a href="#">M</a></div>
                                <p><a href="#">Calendar</a></p>
                            </div>
                            <div>
                                <div class="red"><a href="#">N</a></div>
                                <p><a href="#">Press Releases</a></p>
                            </div>
                            <div>
                                <div class="red"><a href="#">T</a></div>
                                <p><a href="#">Rezora</a></p>
                            </div>
                            <div>
                                <div class="lightblue"><a href="#">B</a></div>
                                <p><a href="#">Directory</a></p>
                            </div>
                            <div>
                                <div class="black"><a href="#">M</a></div>
                                <p><a href="#">Anthology</a></p>
                            </div>
                         </div>
                    </div>
                     <div class="fav-options tab2" id="favTabsId">
                     	
                     </div>
                     <a href="#" onclick="return false" class="main-right">
                    </a>
            </div>
        </div>



				</div>
			<!-- PageContent End-->

			<!-- MenuDropDown Start-->
			<div id="menuDropDown" style="opacity: 1; display: block; left: 380px; visibility: hidden;">
				<div id="pointerBG"><div id="pointer"></div></div>
				<div id="menuDDLeft"></div>
				<div id="menuDDRight"></div>
			</div>
			<!-- MenuDropDown End-->



		</div><!-- End Container -->
        

		<script>var curTopLevelIndx = 0;</script>
		<script type="text/javascript" src="../js/menu.js"></script>
		<script type="text/javascript" src="../js/dashboard.js"></script>
        
        <!--Editable Dashboard popup start-->
        <div class="modal fade" id="editDashId">
          <div class="modal-dialog">
            <div class="modal-content">
             
              <img src="/content/dam/demo-site/Desktop_Dashboard_AdministrationV3_popup.png">
              <div class="editDashAct">
                  <a href="#" onclick="showWidgetLib()" class="popup">Show/Hide Widgets</a>
                  <a href="#" class="popup">Save Changes</a>
                  <a href="#" data-dismiss="modal" class="popup">Cancel</a>
              </div>
            </div><!-- /.modal-content -->
          </div><!-- /.modal-dialog -->
		</div><!-- /.modal -->
	    <!--Editable Dashboard popup start-->
        <!--Editable Dashboard popup start-->
        <div class="modal fade" id="widgetLibId">
          <div class="modal-dialog modal-dialog-wi-li">
            <div class="modal-content">
             
              <img src="/content/dam/demo-site/Widget_Library_popup.png">
              <a href="#" data-dismiss="modal"><img src="/content/dam/demo-site/Close_button_wid.png" class="closeWidget"></a>
              <div class="widgetDashAct">
                  <a href="#" class="popup saveAct">Save Changes</a>
                 <a href="#" class="popup cnclAct" data-dismiss="modal" onclick="cnclWidLib()">Cancel</a>
              </div>
            </div><!-- /.modal-content -->
          </div><!-- /.modal-dialog -->
		</div><!-- /.modal -->
	    <!--Editable Dashboard popup start-->
<script>
$(document).ready(function() {
	$("#top0").attr("class", "mainNavLink-dt selectedTopLevelLink");
});
</script>