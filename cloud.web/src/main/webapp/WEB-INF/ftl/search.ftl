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
					<li><a href="/admin" title="Admin Console">Admin Console</a></li>
					<li><a href="http://www.eclipse.org/virgo" title="Admin Console">Virgo</a></li>
				</ul>
			</div>
			<img id="right-curve" src="/cloud/images/menu-curve-right.png"/>
		</div><!-- /primary-navigation -->

		<div id="container">
		<div id="content-no-nav">
			<h1>Cloud Manager :: Search (example) </h1>
			<p/>
			<form name="searchForm" method="GET" action="search.htm">
				<input name="filter" type="text"/> <input type="submit"/>
			</form>
			<#if stringList?? && (stringList?size > 0)>
				<table id="results">
					<thead>
						<tr>
							<th>Last Name</th>
							<th>First Name</th>
							<th>&nbsp;</th>
						</tr>
					</thead>
					<tbody>
				  <#list stringList as str>
							<tr>
								<td>DATA</td>
								<td>${str}</td>
								<td><a href="entry.htm?id=${str}">view</a></td>
							</tr>
						</#list>
					</tbody>
				</table>
			<#else>
				<h2>No results found.</h2>
			</#if>
		</div> <!-- /content -->
		</div> <!-- /container -->
    
    <div id="footer-wrapper">
        <div id="footer-left">&copy; Copyright 2011 TU-Dortmund ITMC ::  Miguel Rojas (miguel.rojas@uni-dortmund.de).</div>
        <div id="footer-right"></div> 
    </div> <!-- /footer-wrapper -->

  </div> <!-- /page -->

</body>
</html>