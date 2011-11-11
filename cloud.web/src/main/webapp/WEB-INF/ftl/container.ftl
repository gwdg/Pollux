<#import "spring.ftl" as spring />
<!DOCTYPE HTML>
<html>
<head>
<title>Cloud :: CDMI - Container</title>
	<link type="text/css" rel="stylesheet" href="<@spring.url '/jqueryui/css/redmond/jquery-ui-1.8.16.custom.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<@spring.url '/styles/main.css'/>" />
	<link type="text/css" rel="stylesheet" href="<@spring.url '/styles/local.css'/>" />
	<link type="text/css" rel="stylesheet" href="<@spring.url '/styles/print.css'/>" media="print" />
	<script type="text/javascript" src="<@spring.url '/jqueryui/js/jquery-1.6.2.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jqueryui/js/jquery-ui-1.8.16.custom.min.js'/>"></script>
	<script type="text/javascript">
			$(function(){

				// Accordion
				$("#accordion").accordion({ header: "h3" });
	
				// Dialog			
				$('#dialog').dialog({
					autoOpen: false,
					width: 600,
					buttons: {
						"Ok": function() { 
							$(this).dialog("close"); 
						}, 
						"Cancel": function() { 
							$(this).dialog("close"); 
						} 
					}
				});
				
			});
		</script>
</head>
<body class="main tundra">
	<div id="page">
		<div id="mini-header">
			<div id="mini-header-left"></div>
			<div id="mini-header-right"></div>
		</div> <!-- /mini-header -->

		<div id="primary-navigation">
			<div id="primary-left">
				<ul>
	                <li><a href="/cloud" title="Login">Cloud</a></li>
				</ul>
			</div>
			<img id="left-curve" src="/cloud/images/menu-curve-left.png"/>
			<div id="primary-right">
				<ul>
	                <li><a href="http://dl.dropbox.com/u/4837292/OCCI_CDMI_Screencast_large.mp4" title="Screencast" target="_new">Screencast</a></li>
				</ul>
			</div>
			<img id="right-curve" src="/cloud/images/menu-curve-right.png"/>
		</div><!-- /primary-navigation -->

		<div id="container">
		<div id="results-no-nav">
			<h1>Cloud Manager :: CDMI: Container &nbsp;&nbsp;<a href="/cloud/app/xmenu.htm?session=${session.id}"><img src="../images/go_home.png" border="0" longdesc="home"></a></h1>
			<div class="xcontainer">
			<form name="containerForm" method="POST" action="container.htm">
				<input name="session" type="hidden" value="${session.id}" />
				<table width="350" border="0">
					<tr>
					  <td width="85"><div align="right"><strong><em>CDMI</em></strong>:&nbsp; </div></td>
					  <td width="254">http://${session.properties[ 'server.cdmi' ]}</td>
					</tr>
					<tr>
					  <td width="85"><div align="right">Container:&nbsp; </div></td>
					  <td width="254"><input name="container"  type="text" size="30" /></td>
					</tr>
					<tr>
					  <td>&nbsp;</td>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <td>&nbsp;</td>
					  <td><div align="right">
						<input type="submit" name="command" value="create"/>
					  </div></td>
					</tr>
			  	</table>
				<p>&nbsp;</p>
		    </form>
	  	  </div>

			<#if containerList?? && (containerList?size > 0)>
				<h2>Available Containers  (${containerList?size})</h2>
					    <div class="xcontainer">
					    <div id="accordion">
				  		<#list containerList as item>
				  			<#if item.response.code != -1>
					  			<h3><a href="#">&nbsp;${item.response.body['objectURI']}&nbsp;</a></h3>
								<div>
									<p>
									     &nbsp; ${item.response.content} &nbsp; &nbsp; <a href="container.htm?session=${session.id}&delete=http://${session.properties['server.cdmi']}${item.response.body['objectURI']+'/'}"><img src="../images/erase.png" border="0" longdesc="delete"></a></td>
									</p>
								</div>
							<#else>
								<tr>
									<td>${item.response.content}</td>
									<td></td>
								</tr>
							</#if>
						</#list>
					</div>
					</div><!-- End xcontainer -->
			<#elseif containerList?? && (containerList?size == 0)>
				<h2>No results found.</h2>
			</#if>
		</div> <!-- /content -->
		</div> <!-- /container -->
    
    <div id="footer-wrapper">
        <div id="footer-left">&copy; Copyright 2011 ::  Miguel Rojas (email.miguel.rojas@googlemail.com) &amp; Florian Feldhaus (florian.feldhaus@uni-dortmund.de).</div>
        <div id="footer-right"></div> 
    </div> <!-- /footer-wrapper -->

  </div> <!-- /page -->

</body>
</html>