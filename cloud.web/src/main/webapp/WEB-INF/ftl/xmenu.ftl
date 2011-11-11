<#import "spring.ftl" as spring />
<!DOCTYPE HTML>
<html>
<head>
<title>Cloud Manager :: Menu</title>
	<link type="text/css" rel="stylesheet" href="<@spring.url '/jqueryui/css/redmond/jquery-ui-1.8.16.custom.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<@spring.url '/styles/main.css'/>" />
	<link type="text/css" rel="stylesheet" href="<@spring.url '/styles/local.css'/>" />
	<link type="text/css" rel="stylesheet" href="<@spring.url '/styles/print.css'/>" media="print" />
	<script type="text/javascript" src="<@spring.url '/jqueryui/js/jquery-1.6.2.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jqueryui/js/jquery-ui-1.8.16.custom.min.js'/>"></script>
	<script type="text/javascript">
			$(function(){
				// Network
				$( "#occiNetwork" ).button({
					text: !false,
					icons: {
						primary: "ui-icon-gear"
					}
				}).click(function(){ 
					document.clientForm.action = "onenetwork.htm";
					document.clientForm.submit();
				});
			});

			$(function(){
				// Storage
				$( "#occiStorage" ).button({
					text: !false,
					icons: {
						primary: "ui-icon-gear"
					}
				}).click(function(){ 
					document.clientForm.action = "onestorage.htm";
					document.clientForm.submit();
				});
			});
				
			$(function(){
				// Compute
				$( "#occiCompute" ).button({
					text: !false,
					icons: {
						primary: "ui-icon-gear"
					}
				}).click(function(){ 
					document.clientForm.action = "onecompute.htm";
					document.clientForm.submit();
				});
			});
				
			$(function(){
				// Containers
				$( "#cdmiContainer" ).button({
					text: !false,
					icons: {
						primary: "ui-icon-gear"
					}
				}).click(function(){ 
					document.clientForm.action = "container.htm";
					document.clientForm.submit();
				});
			});
				
			$(function(){
				// NonCDMI Objects
				$( "#cdmiNonDataObject" ).button({
					text: !false,
					icons: {
						primary: "ui-icon-gear"
					}
				}).click(function(){ 
					document.clientForm.action = "nondataobject.htm";
					document.clientForm.submit();
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
		<div id="content-no-nav">
			<h1 align="left">Cloud Manager :: Menu </h1>
			<p/>
			<#if session?? >
			<div class="xcontainer">
				<form name="clientForm" method="GET" action="">
					<input name="init" type="hidden" value="true" />
					<input name="session" type="hidden" value="${session.id}" />
					<table width="300" border="0">
					  <tr>
						<td colspan="3">&nbsp;<em>::</em> &nbsp;<strong>OCCI Client</strong></td>
					  </tr>
					  <tr>
						<td width="90" align="right"><img id="right-curve" src="/cloud/images/m_network.png"/></td>
					    <td width="210">&nbsp;&nbsp;<button id=occiNetwork>Network</button></td>
					  </tr>
					  <tr>
						<td width="90" align="right"><img id="right-curve" src="/cloud/images/m_storage.png"/></td>
					    <td width="210">&nbsp;&nbsp;<button id=occiStorage>Storage</button></td>
					  </tr>
					  <tr>
						<td width="90" align="right"><img id="right-curve" src="/cloud/images/m_compute.png"/></td>
					    <td width="210">&nbsp;&nbsp;<button id=occiCompute>Compute</button></td>
					  </tr>
	  		  		</table><br>
					<table width="300" border="0">
					  <tr>
						<td colspan="2">&nbsp;<em>:: </em>&nbsp;<strong>CDMI Client</strong> &nbsp;</td>
					  </tr>
					  <tr>
						<td width="90" align="right"><img id="right-curve" src="/cloud/images/m_container.png"/></td>
					    <td width="210">&nbsp;&nbsp;<button id=cdmiContainer>Containers</button></td>
					  </tr>
					  <tr>
						<td width="90" align="right"><img id="right-curve" src="/cloud/images/m_linux.png"/></td>
					    <td width="210">&nbsp;&nbsp;<button id=cdmiNonDataObject>VM Images</button></td>
					  </tr>
					  <tr>
						<td colspan="2">&nbsp;</td>
					  </tr>
			  		</table>
			  		</form>
		  		</div> 
			<#else>
				<h2>No username found. &nbsp;&nbsp;<a href="login.htm">Try again</a></h2>
		    </#if>		</div> 
		<!-- /content -->
		</div> <!-- /container -->
    
    <div id="footer-wrapper">
        <div id="footer-left">&copy; Copyright 2011 ::  Miguel Rojas (email.miguel.rojas@googlemail.com) &amp; Florian Feldhaus (florian.feldhaus@uni-dortmund.de).</div>
        <div id="footer-right"></div> 
    </div> <!-- /footer-wrapper -->

  </div> <!-- /page -->

</body>
</html>