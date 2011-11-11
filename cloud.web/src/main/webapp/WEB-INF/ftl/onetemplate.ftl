<!DOCTYPE HTML>
<html>
<head>
<title>Cloud Manager :: Template</title>
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
		<div id="results-no-nav">
			<h1 align="left">ONE:: Template  &nbsp;&nbsp;<a href="/cloud/app/menu.htm?back=true"><img src="../images/go_home.png" border="0" longdesc="home"></a></h1>
			<form name="onetemplateForm" method="POST" action="onetemplate.htm" > 
				<input name="form" type="hidden" value="true" />
				  <table width="410" border="0">
					<tr>
					  <td width="85"><div align="right"><strong><em>URI</em></strong>:&nbsp; </div></td>
					  <td width="325">http://
				      <input name="uri"  type="text" size="24" value="129.217.211.165:3000"/>&nbsp;<input type="submit" name="command" value="update"></td>
					</tr>
					<tr>
					  <td width="85"><div align="right">Title:&nbsp; </div></td>
					  <td width="325"><input name="title"  type="text" size="30" value=""/></td>
					</tr>
					<tr>
					  <td width="85"><div align="right">Summary:&nbsp; </div></td>
					  <td width="325"><input name="summary"  type="text" size="30" value=""/></td>
					</tr>
					<tr>
					  <td width="85"><div align="right">Architecture:&nbsp; </div></td>
					  <td width="325"><input name="architecture"  type="text" size="30" value=""/></td>
					</tr>
					<tr>
					  <td width="85"><div align="right">Cores:&nbsp; </div></td>
					  <td width="325"><input name="cores"  type="text" size="30" value=""/></td>
					</tr>
					<tr>
					  <td width="85"><div align="right">Memory:&nbsp; </div></td>
					  <td width="325"><input name="memory"  type="text" size="30" value=""/></td>
					</tr>
					<tr>
					  <td width="85"><div align="right">Network:&nbsp; </div></td>
					  <td width="325"><select name="network">
							<option value="-1">--select one--</option>
							<#if INetworkList?? && (INetworkList?size > 0) >
						  		<#list INetworkList as net>
									<option value="${net.content[ 'id' ]}">${net.attributes[ 'occi.core.title' ]}</option>
								</#list>
							</#if>
							</select>
						</td>
					</tr>
					<tr>
					  <td width="85"><div align="right">Storage:&nbsp; </div></td>
					  <td width="325"><select name="storage">
							<option value="-1">--select one--</option>
							<#if IStorageList?? && (IStorageList?size > 0) >
						  		<#list IStorageList as storage>
									<option value="${storage.content[ 'id' ]}">${storage.attributes[ 'occi.core.title' ]}</option>
								</#list>
							</#if>
							</select>
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
		  
			<#if IComputeList?? && (IComputeList?size > 0) >
				<h2>Available Compute  (${IComputeList?size})</h2>
				<table id="results">
					<thead>
					</thead>
					<tbody>
				  		<#list IComputeList as compute>
							<tr>
								<td colspan="2" class="ielement">${compute.content[ 'id' ]}&nbsp;&nbsp;<a href="/cloud/app/onecompute.htm?form=true&uri=${IUri.content}&delete=${compute.content[ 'uri' ]}"><img src="../images/erase.png" border="0" longdesc="delete"></a></td>
							</tr>
							<#if compute.attrKeys?? && (compute.attrKeys?size > 0) >
							<tr>
								<td>&nbsp;</td>
								<td>
								  <table width="200" border="0">
							  		<#list compute.attrKeys as attrs>
									<tr class="iattribute">
									  <td>${attrs}:</td>
									  <td>${compute.attributes[ attrs ]}</td>
									</tr>
									</#list>
					  			  </table>
								</td>
							</tr>
							</#if>
						</#list>
					</tbody>
				</table>
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