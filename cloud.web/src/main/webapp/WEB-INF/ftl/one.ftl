<!DOCTYPE HTML>
<html>
<head>
<title>Cloud</title>
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
					<li><a href="http://www.eclipse.org/virgo" title="Admin Console">Virgo</a></li>
				</ul>
			</div>
			<img id="right-curve" src="/cloud/images/menu-curve-right.png"/>
		</div><!-- /primary-navigation -->

		<div id="container">
		<div id="content-no-nav">
			<h1>Cloud Manager :: OCCI connector &nbsp;&nbsp;<a href="/cloud/app/menu.htm?back=true"><img src="../images/go_home.png" border="0" longdesc="home"></a></h1>
			<p/>
			<form name="oneForm" method="GET" action="one.htm">
				<input name="form" type="hidden" value="true" />
				  <table width="327" border="0">
					<tr>
					  <td width="85"><div align="right">Server:&nbsp; </div></td>
					  <td width="232"><input name="url"  type="text" size="30"/></td>
					</tr>
					<tr>
					  <td><div align="right">Command:&nbsp;</div></td>
					  <td><textarea name="command" cols="30"></textarea></td>
					</tr>
					<tr>
					  <td>&nbsp;</td>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <td>&nbsp;</td>
					  <td><div align="right">
						<input type="submit" title="submit"/>
					  </div></td>
					</tr>
			  </table>
				  <p>&nbsp;</p>
		  </form>
			<#if result?? >
				  <table width="360" border="0">
					<tr>
					  <td width="100%"><h2>${result.content}</h2></td>
					</tr>
			       </table>
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