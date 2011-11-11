<!DOCTYPE HTML>
<html>
<head>
<title>Cloud Manager :: Menu</title>
  <link rel="stylesheet" href="/cloud/styles/main.css" type="text/css" />
  <link rel="stylesheet" href="/cloud/styles/local.css" type="text/css" />
  <link rel="stylesheet" href="/cloud/styles/print.css" type="text/css" media="print" />
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
					<li><a href="/admin" title="Admin Console">Admin Console</a></li>
					<li><a href="http://www.eclipse.org/virgo" title="Admin Console">Virgo</a></li>
				</ul>
			</div>
			<img id="right-curve" src="/cloud/images/menu-curve-right.png"/>
		</div><!-- /primary-navigation -->

		<div id="container">
		<div id="content-no-nav">
			<h1 align="left">Cloud Manager :: Menu </h1>
			<p/>
			<#if stringList?? && (stringList?size > 0)>
				<table id="results" width="310" border="0">
				  <tr>
					<td colspan="4">&nbsp;<em>::</em> &nbsp;<strong>OpenNebula</strong></td>
				  </tr>
				  <tr>
					<td width="25%"><div align="center"><a href="onenetwork.htm">Network</a></div></td>
				    <td width="25%"><div align="center"><a href="onestorage.htm">Storage</a></div></td>
				    <td width="25%"><div align="center"><a href="onecompute.htm">Compute</a></div></td>
				    <td width="25%"><div align="center"><a href="onetemplate.htm">Template</a></div></td>
				  </tr>
				  <tr>
					<td colspan="4">&nbsp;</td>
				  </tr>
				  <tr>
					<td colspan="4">&nbsp;<em>:: </em>&nbsp;<strong>CDMI Server</strong> &nbsp;</td>
				  </tr>
				  <tr>
					<td colspan="2"><blockquote><a href="container.htm">Containers</a> </blockquote></td>
					<td colspan="2"><blockquote><a href="dataobject.htm">DataObjects</a> </blockquote></td>
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
        <div id="footer-left">&copy; Copyright 2011 TU-Dortmund ITMC ::  Miguel Rojas (miguel.rojas@uni-dortmund.de).</div>
        <div id="footer-right"></div> 
    </div> <!-- /footer-wrapper -->

  </div> <!-- /page -->

</body>
</html>