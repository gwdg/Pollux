<#import "spring.ftl" as spring />
<!DOCTYPE HTML>
<html>
<head>
<title>Cloud :: CDMI - VM Images</title>
    <link type="text/css" rel="stylesheet" href="<@spring.url '/jqueryui/css/redmond/jquery-ui-1.8.16.custom.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<@spring.url '/styles/main.css'/>" />
    <link type="text/css" rel="stylesheet" href="<@spring.url '/styles/local.css'/>" />
    <link type="text/css" rel="stylesheet" href="<@spring.url '/styles/print.css'/>" media="print" />
    <script type="text/javascript" src="<@spring.url '/jqueryui/js/jquery-1.6.2.min.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jqueryui/js/jquery-ui-1.8.16.custom.min.js'/>"></script>
    <script type="text/javascript">
        // Accordion
        $(function(){
            $("#accordion").accordion({ header: "h3" });
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
			<h1>Cloud Manager :: CDMI VM Images &nbsp;&nbsp;<a href="/cloud/app/xmenu.htm?session=${session.id}"><img src="../images/go_home.png" border="0" longdesc="home"></a></h1>
			<div class="xcontainer">
			<form name="nondataobjectForm" method="POST" action="nondataobject.htm" enctype="multipart/form-data">
				<input name="session" type="hidden" value="${session.id}" />
				  <table width="320" border="0">
					<tr>
					  <td width="85"><div align="right"><strong><em>CDMI</em></strong>:&nbsp; </div></td>
					  <td width="235">http://${session.properties[ 'server.cdmi' ]}</td>
					</tr>
					<tr>
					  <td width="85"><div align="right">Container:&nbsp; </div></td>
					  <td width="235">
						<#if containerList?? && (containerList?size > 0)>
							<select name="cdmicontainer" id="cdmicontainer">
								<option value="-1">Select one...</option>
						  		<#list containerList as item>
						  			<#if item.response.code != -1>
										<option value="${item.response.body['objectURI']}">${item.response.body['objectURI']}</option>
									</#if>
								</#list>
							</select>
			   			</#if>
					  </td>
					</tr>
					<tr>
					  <td width="85"><div align="right">Object:&nbsp; </div></td>
					  <td width="235"><input name="file" type="file" size="30" /></td>
					</tr>
					<tr>
					  <td>&nbsp;</td>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <td>&nbsp;</td>
					  <td><div align="right">
						<input type="submit" name="command" value="upload"/>
					  </div></td>
					</tr>
			  </table>
			  <p>&nbsp;</p>
		    </form>
	  	  	</div>
		  
			<#if directory?? && (directory.entries > 0)>
                <h2>Available CDMI VM Images (${directory.entries})</h2>
                <div class="xcontainer">
                <div id="accordion">
                <#list directory.keys as container>
                    <h3><a href="#">${container}&nbsp;</a></h3>
                    <div>
                        <p>
                            <#if directory.content[ container ]?? && (directory.content[ container ]?size > 0) >
                                  <table width="400" border="0">
                                  <#list directory.content[ container ] as file>
                                        <tr class="iattribute">
                                          <td>${file.uri}  >> <a href="nondataobject.htm?session=${session.id}&delete=${file.uri}"><img src="../images/erase.png" border="0" longdesc="delete"></a></td>
                                        </tr>
                                  </#list>
                                  </table>
                            </#if>
                            <br>
                        </p>
                    </div>
                </#list>
                </div>
                </div><!-- End xcontainer -->
			<#else>
				<br>
				No Images found.
				<br>
				<br>
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