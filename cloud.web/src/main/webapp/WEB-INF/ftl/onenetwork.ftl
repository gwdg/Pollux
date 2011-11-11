<#import "spring.ftl" as spring />
<!DOCTYPE HTML>
<html>
<head>
<title>Cloud Manager :: Network</title>
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
	                <li><a href="http://dl.dropbox.com/u/4837292/OCCI_CDMI_Screencast_large.mp4" title="Screencast" target="_new">Screencast</a></li>
				</ul>
			</div>
			<img id="right-curve" src="/cloud/images/menu-curve-right.png"/>
		</div><!-- /primary-navigation -->

		<div id="container">
		<div id="results-no-nav">
			<h1 align="left">OCCI:: Network  &nbsp;&nbsp;<a href="/cloud/app/xmenu.htm?session=${session.id}"><img src="../images/go_home.png" border="0" longdesc="home"></a></h1>
			<form name="onenetworkForm" method="POST" action="onenetwork.htm"> 
				<input name="session" type="hidden" value="${session.id}" />
				  <table width="430" border="0">
					<tr>
					  <td width="100"><div align="right"><strong><em>URI</em></strong>:&nbsp; </div></td>
					  <td width="320">http://${session.properties[ 'server.occi' ]}</td>
					</tr>
					<tr>
					  <td width="100"><div align="right">Title:&nbsp; </div></td>
					  <td width="320"><input name="title"  type="text" size="30" value=""/></td>
					</tr>
					<tr>
					  <td width="100"><div align="right">Summary:&nbsp; </div></td>
					  <td width="320"><input name="summary"  type="text" size="30" value=""/></td>
					</tr>
					<tr>
					  <td width="100"><div align="right">Address:&nbsp; </div></td>
					  <td width="320"><input name="address"  type="text" size="30" value=""/> 
					    <span class="example">(i.e.192.168.0.0/24)</span></td>
					</tr>
					<tr>
					  <td width="100"><div align="right">Allocation:&nbsp; </div></td>
					  <td width="320"><input name="allocation"  type="text" value="dynamic" size="30"/></td>
					</tr>
					<tr>
					  <td width="100"><div align="right">vlan:&nbsp; </div></td>
					  <td width="320"><input name="vlan"  type="text" size="30" value="1"/></td>
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
				<h2>Available Networks (${IElementDecoratorList?size})</h2>
					    <div class="xcontainer">
					    <div id="accordion">
				  		<#list IElementDecoratorList as net>
				  		    <#assign element    = net.element>
				  		    <#assign caption    = net.caption>
				  		    <#assign attributes = net.values>
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
								  <br><a href="/cloud/app/onenetwork.htm?session=${session.id}&delete=${element.content[ 'uri' ]}"><img src="../images/erase.png" border="0" longdesc="delete"></a>
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