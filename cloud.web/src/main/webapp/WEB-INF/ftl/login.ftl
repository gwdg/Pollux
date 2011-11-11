<#import "spring.ftl" as spring />
<!DOCTYPE HTML>
<html>
<head>
<title>Cloud Manager :: Login</title>
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
	
				// Tabs
				$('#tabs').tabs();
	
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
	                <li><a href="/cloud" title="Admin Console">Cloud</a></li>
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
			<h1 align="left">Cloud Manager :: Authentication </h1>
			<p/>
			<form name="menuForm" method="POST" action="xmenu.htm">
				<div align="left">
				  <table width="200" border="0">
                    <tr>
                      <td>Username: </td>
                      <td><input name="username" type="text" value="<#if session??>${session.id}<#else></#if>"/></td>
                    </tr>
                    <tr>
                      <td>Password: </td>
                      <td><input name="password" type="password"/></td>
                    </tr>
                    <tr>
                      <td>OCCI Server: </td>
                      <td><input name="occi" type="text"/></td>
                    </tr>
                    <tr>
                      <td>CDMI Server: </td>
                      <td><input name="cdmi" type="text"/></td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td><div align="right">
                        <input name="command" value="login" type="submit"/>
                      </div></td>
                    </tr>
                  </table>
			  </div>
			</form>
		</div> <!-- /content -->
		</div> <!-- /container -->
    
    <div id="footer-wrapper">
        <div id="footer-left">&copy; Copyright 2011 ::  Miguel Rojas (email.miguel.rojas@googlemail.com) &amp; Florian Feldhaus (florian.feldhaus@uni-dortmund.de).</div>
        <div id="footer-right"></div> 
    </div> <!-- /footer-wrapper -->

  </div> <!-- /page -->

</body>
</html>