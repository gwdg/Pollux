<#import "spring.ftl" as spring />
<!DOCTYPE HTML>
<html>
<head>
<title>Cloud Manager :: VM</title>
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
		
			// Cores-Slider
			$( "#cores-slider" ).slider({
				range: "max",
				min: 1,
				max: 4,
				value: 2,
				slide: function( event, ui ) {
					$( "#cores" ).val( ui.value );
				}
			});
			$( "#cores" ).val( $( "#cores-slider" ).slider( "value" ) );

			// Memory-Slider
			$( "#memory-slider" ).slider({
				range: "max",
				min: 2,
				max: 32,
				value: 4,
				step: 2,
				slide: function( event, ui ) {
					$( "#memory" ).val( ui.value );
				}
			});
			$( "#memory" ).val( $( "#memory-slider" ).slider( "value" ) );
		
			// Architecture
			$( "#machine" ).selectable({
			   selected: function(event, ui) {
					$( ".ui-selected", this ).each(function() {
						var index = $( "#machine li" ).index( this );
						var txt = "";
						if ( index == 0 )
							txt = "x86";
						else
							txt = "x64";
							
						$( "#architecture" ).val( txt );
					});
			   }
			});
				
			// VNC
			$( "#vnc-console" ).dialog({
				autoOpen: false,
				height: 510,
				width: 770,
				modal: true,
				show: 'clip',
				close: function() {
				}
			});
				
			// Form
			$('#onecomputeForm').submit(function() {
		  		// alert($(this).serialize());
		  		return true;
			});				
		});
		
		function showconsole( vmID ){
			$( "#vnc-target" ).attr( "src", vmID );
			$( "#vnc-console" ).dialog( "open" );
		}
	</script>
	<style>
		#feedback { font-size: x-small; }
		#machine .ui-selecting { background: #FECA40; }
		#machine .ui-selected { background: #F39814; color: white; }
		#machine { list-style-type: none; margin: 0; padding: 0; }
		#machine li { margin: 3px; padding: 1px; float: left; width: 30px; height: 20px; font-size: x-small; text-align: center; }
	</style>
</head>
<body class="main tundra">
	<div id="vnc-console" title="VM Console">
		<iframe id="vnc-target" width="740" height="460" src="http://www.youtube.com/embed/xTSE3bohck8?rel=0" frameborder="0" allowfullscreen></iframe>
	</div>
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
		
			<h1 align="left">OCCI:: Compute  &nbsp;&nbsp;<a href="/cloud/app/xmenu.htm?session=${session.id}"><img src="../images/go_home.png" border="0" longdesc="home"></a></h1>
			<div class="xcontainer">
			<form id="onecomputeForm" method="POST" action="onecompute.htm" > 
				<input name="session" type="hidden" value="${session.id}" />
				  <table width="410" border="0">
					<tr>
					  <td width="85"><div align="right"><strong><em>OCCI</em></strong>:&nbsp; </div></td>
					  <td width="325">http://${session.properties[ 'server.occi' ]}</td>
					</tr>
					<tr>
					  <td width="85"><div align="right"><strong><em>CDMI</em></strong>:&nbsp; </div></td>
					  <td width="325">http://${session.properties[ 'server.cdmi' ]}</td>
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
					  <td width="325">
					  		<input id="architecture" name="architecture" type="hidden" value="" />
							<ol id="machine" >
								<li class="ui-state-default" value="x86">x86</li>
								<li class="ui-state-default" value="x64">x64</li>
							</ol>
					  </td>
					</tr>
					<p><tr>
						<td width="85"><div align="right"><label for="cores">Cores:</label></div></td>
						<td width="325"><input type="text" id="cores" name="cores" style="border:0; color:#f6931f; font-weight:bold;" /></td>
					</tr></p>
					<tr>
						<td width="85"></td>
						<td width="325">
							<table width="100" border="0">
								<tr><td><div id="cores-slider"></div></td></tr>
							</table>
						</td>
					</tr>
					<tr>
					</tr>
					<p><tr>
						<td width="85"><div align="right"><label for="memory">Memory (GB):</label></div></td>
						<td width="325"><input type="text" id="memory" name="memory" style="border:0; color:#f6931f; font-weight:bold;" /></td>
					</tr></p>
					<tr>
						<td width="85"></td>
						<td width="325">
							<table width="160" border="0">
								<tr><td><div id="memory-slider"></div></td></tr>
							</table>
						</td>
					</tr>
					<tr>
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
							  	<optgroup label="OCCI Storage">
									<option value="-1">--select one--</option>
									<#if IStorageList?? && (IStorageList?size > 0) >
								  		<#list IStorageList as storage>
											<option value="${storage.content[ 'id' ]}">${storage.attributes[ 'occi.core.title' ]}</option>
										</#list>
									</#if>
								</optgroup>
							  	<optgroup label="CDMI Storage">
									<#if directory?? && (directory.entries > 0)>
										<#list directory.keys as container>
											<#if directory.content[ container ]?? && (directory.content[ container ]?size > 0) >
												<#list directory.content[ container ] as file>
													<option value="${file.uri}">${file.uri}</option>
												</#list>
											</#if>
										</#list>
									</#if>
								</optgroup>
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
	  	  </div>
		  
			<#if IElementDecoratorList?? && (IElementDecoratorList?size > 0) >
				<h2>Available VMs  (${IElementDecoratorList?size}) &nbsp;&nbsp;<a href="onecompute.htm?init=true&session=${session.id}"><img src="../images/refresh.png" border="0" alt="delete"></a></h2>
			    <div class="demo">
			    <div id="accordion">
				  		<#list IElementDecoratorList as compute>
				  		    <#assign element    = compute.element>
				  		    <#assign caption    = compute.caption>
				  		    <#assign attributes = compute.values>
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
  								  <br>
								  <table width="300" border="0">
									<tr class="iattribute">
									  <td align="center" valign="middle">
		                                    <a href="/cloud/app/onecompute.htm?session=${session.id}&action=start&vm=${element.content[ 'uri' ]}"><img src="../images/start.png" border="0" alt="start"></a>&nbsp;
		                                    <a href="/cloud/app/onecompute.htm?session=${session.id}&action=stop&vm=${element.content[ 'uri' ]}"><img src="../images/stop.png" border="0" alt="stop"></a>&nbsp;
											<#if element.attributes[ 'opennebula.vm.web_vnc' ]?? >
											  <#assign web = element.attributes[ 'opennebula.vm.web_vnc' ] >		                                    
		                                        <a href="javascript:showconsole('${web}')"><img src="../images/console.png" border="0" ></a>&nbsp;
		                                    </#if>
		                                    &nbsp;&nbsp;&nbsp;<a href="/cloud/app/onecompute.htm?session=${session.id}&action=delete&vm=${element.content[ 'uri' ]}"><img src="../images/erase.png" border="0" alt="delete"></a>
									  </td>
									</tr>
					  			  </table>
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