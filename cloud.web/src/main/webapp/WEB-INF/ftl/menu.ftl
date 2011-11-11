<!DOCTYPE HTML>
<html>
<head>
<title>Cloud Manager :: Menu</title>
	<link rel="stylesheet" href="/cloud/styles/main.css" type="text/css" />
	<link rel="stylesheet" href="/cloud/styles/local.css" type="text/css" />
	<link rel="stylesheet" href="/cloud/styles/print.css" type="text/css" media="print" />
	<link type="text/css" href="/cloud/styles/css/redmond/jquery-ui-1.8.16.custom.css" rel="stylesheet" />	
	<script type="text/javascript" src="/cloud/styles/jquery/js/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="/cloud/styles/jquery/js/jquery-ui-1.8.16.custom.min.js"></script>
	<script type="text/javascript">
			$(function(){

				// Accordion
				$("#accordion").accordion({ header: "h3" });
	
				// Tabs
				$('#tabs').tabs();
	

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
				
				// Dialog Link
				$('#dialog_link').click(function(){
					$('#dialog').dialog('open');
					return false;
				});

				// Datepicker
				$('#datepicker').datepicker({
					inline: true
				});
				
				// Slider
				$('#slider').slider({
					range: true,
					values: [17, 67]
				});
				
				// Progressbar
				$("#progressbar").progressbar({
					value: 20 
				});
				
				//hover states on the static widgets
				$('#dialog_link, ul#icons li').hover(
					function() { $(this).addClass('ui-state-hover'); }, 
					function() { $(this).removeClass('ui-state-hover'); }
				);
				
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
	                <li><a href="http://www.eclipse.org/virgo" title="Admin Console" target="_top">Virgo</a></li>
				</ul>
			</div>
			<img id="right-curve" src="/cloud/images/menu-curve-right.png"/>
		</div><!-- /primary-navigation -->

		<div id="container">
		<div id="content-no-nav">
			<h1 align="left">Cloud Manager :: Menu </h1>
			<p/>
			<#if session?? >
				<table width="400" border="0">
				  <tr>
					<td colspan="4">&nbsp;<em>::</em> &nbsp;<strong>OCCI Client</strong></td>
				  </tr>
				  <tr>
					<td width="25%">&nbsp;&nbsp;<a href="onenetwork.htm?init=true&session=${session.id}">Network</a></td>
				    <td width="25%">&nbsp;&nbsp;<a href="onestorage.htm?init=true&session=${session.id}">Storage</a></td>
				    <td width="25%">&nbsp;&nbsp;<a href="onecompute.htm?init=true&session=${session.id}">Compute</a></td>
				    <td width="25%">&nbsp;&nbsp; </td>
				  </tr>
				  <tr>
					<td colspan="4">&nbsp;</td>
				  </tr>
  		  </table>
				<table width="400" border="0">
				  <tr>
					<td colspan="4">&nbsp;<em>:: </em>&nbsp;<strong>CDMI Client</strong> &nbsp;</td>
				  </tr>
				  <tr>
					<td width="25%">&nbsp;<a href="container.htm?init=true&session=${session.id}">Containers</a></td>
					<td width="45%">&nbsp;<a href="nondataobject.htm?init=true&session=${session.id}">Non-CDMI Objects</a></td>
					<td width="15%">&nbsp;</td>
					<td width="15%">&nbsp;</td>
				  </tr>
				  <tr>
					<td colspan="4">&nbsp;</td>
				  </tr>
		  </table>
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