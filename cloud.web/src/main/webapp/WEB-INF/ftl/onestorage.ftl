<#import "spring.ftl" as spring />
<!DOCTYPE HTML>
<html>
<head>
<title>Cloud Manager :: Storage</title>
	<link type="text/css" rel="stylesheet" href="<@spring.url '/jqueryui/css/redmond/jquery-ui-1.8.16.custom.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<@spring.url '/styles/main.css'/>" />
	<link type="text/css" rel="stylesheet" href="<@spring.url '/styles/local.css'/>" />
	<link type="text/css" rel="stylesheet" href="<@spring.url '/styles/print.css'/>" media="print" />
	<script type="text/javascript" src="<@spring.url '/jqueryui/js/jquery-1.6.2.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jqueryui/js/jquery-ui-1.8.16.custom.min.js'/>"></script>
	<script type="text/javascript">
			$(function(){

				// Accordion
				$(function() {
					$( "#accordion" ).accordion();
				});
	
				// Progressbar
				$("#progressbar").progressbar({
					value: 20 
				});
				
				//hover states on the static widgets
				$('#dialog_link, ul#icons li').hover(
					function() { $(this).addClass('ui-state-hover'); }, 
					function() { $(this).removeClass('ui-state-hover'); 
				});
				
				$(function() {
					$( "#file_tabs" ).accordion();
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
			<h1 align="left">OCCI:: Storage  &nbsp;&nbsp;<a href="/cloud/app/xmenu.htm?session=${session.id}"><img src="../images/go_home.png" border="0" longdesc="home"></a></h1>
			<form name="onestorageForm" method="POST" action="onestorage.htm" enctype="multipart/form-data"> 
				<input name="session" type="hidden" value="${session.id}" />
				  <table width="400" border="0">
					<tr>
					  <td width="75"><div align="right"><strong><em>OCCI</em></strong>:&nbsp; </div></td>
					  <td width="325">http://${session.properties[ 'server.occi' ]}</td>
					</tr>
					<tr>
					  <td width="75"><div align="right">Title:&nbsp; </div></td>
					  <td width="325"><input name="title"  type="text" size="30" value=""/></td>
					</tr>
					<tr>
					  <td width="75"><div align="right">Summary:&nbsp; </div></td>
					  <td width="325"><input name="summary"  type="text" size="30" value=""/></td>
					</tr>
					<tr>
					  <td colspan="2">
						<div id="file_tabs">
							<h3><a href="#">OCCI Image</a></h3>
    							<div>
									<table width="400" border="0">
										<tr>
										  <td width="75"><div align="right">File:&nbsp; </div></td>
										  <td width="325"><input name="file"  type="file" size="30" /></td>
										</tr>
									</table>
								</div>
    						<h3><a href="#">CDMI Link</a></h3>
    							<div>
									<table width="400" border="0">
										<tr>
										  <td width="75"><div align="right">CDMI:&nbsp; </div></td>
										  <td width="325">
											<select name="cdmi">
												<option value="-1">--select one--</option>
												<#if directory?? && (directory.entries > 0)>
													<#list directory.keys as container>
														<#if directory.content[ container ]?? && (directory.content[ container ]?size > 0) >
															<#list directory.content[ container ] as file>
																<option value="${file.uri}">${file.uri}</option>
															</#list>
														</#if>
													</#list>
												</#if>
											</select>
										  </td>
										</tr>
									</table>
								</div>
    						<h3><a href="#">Open URL</a></h3>
    							<div>
									<table width="400" border="0">
										<tr>
										  <td width="75"><div align="right">URI:&nbsp; </div></td>
										  <td width="325"><input name="url"  type="text" size="30" /></td>
										</tr>
									</table>
								</div>
						</div>
					  </td>
					</tr>
					<tr>
					  <td>&nbsp;</td>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <td>&nbsp;</td>
					  <td><div align="left">
						<input type="submit" name="command" value="create"/>
					  </div></td>
					</tr>
			  </table>
				  <p>&nbsp;</p>
		  </form>
		  
			<#if IElementDecoratorList?? && (IElementDecoratorList?size > 0) >
				<h2>Available Storage  (${IElementDecoratorList?size})</h2>
					    <div class="xcontainer">
					    <div id="accordion">
				  		<#list IElementDecoratorList as storage>
				  		    <#assign element    = storage.element>
				  		    <#assign caption    = storage.caption>
				  		    <#assign attributes = storage.values>
				  			<h3><a href="#">${caption[ 1 ]}&nbsp;</a></h3>
							<div>
								<p>
								  <table width="340" border="0">
							  		<#list attributes as attr>
										<tr class="iattribute">
										  <td align="right" width="30%">${attr[ 0 ]}:</td>
										  <td width="70%">${attr[ 1 ]}</td>
										</tr>
									</#list>
					  			  </table>
								  <br><a href="/cloud/app/onestorage.htm?session=${session.id}&delete=${element.content[ 'uri' ]}"><img src="../images/erase.png" border="0" longdesc="delete"></a>
								</p>
							</div>
						</#list>
					</div>
					</div><!-- End xcontainer -->
			<#else>
				<br>
				<br>
				<br>
			</#if>
		  </div><!-- /content -->
		</div> <!-- /container -->
    
    <div id="footer-wrapper">
        <div id="footer-left">&copy; Copyright 2011 ::  Miguel Rojas (email.miguel.rojas@googlemail.com) &amp; Florian Feldhaus (florian.feldhaus@uni-dortmund.de).</div>
        <div id="footer-right"></div> 
    </div> <!-- /footer-wrapper -->

  </div> <!-- /page -->

</body>
</html>