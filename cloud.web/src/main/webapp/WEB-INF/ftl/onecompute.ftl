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
        $(function(){
            
			// Accordion
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
                
            // Network -------------------------------------------------------------------------------------
            var $net_link       = $( "#network"        );
            var $net_mac        = $( "#net_mac"        );
            var $net_interface  = $( "#net_interface"  );
            var $net_ip         = $( "#net_ip"         );
            var $net_gateway    = $( "#net_gateway"    );
            var $net_allocation = $( "#net_allocation" );
            var net_instances   = 1;
            var editNet         = false;
    
            <#noparse>
            // tabs init with a custom tab template and an "add" callback filling in the content
            var $networkTabs = $( "#networkTabs" ).tabs( {
                tabTemplate: "<li><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
                add: function( event, ui ) {
                    var key   = $net_link.val();
                    var value = $('select#network :selected').text();
                    
                    var net_mac        = $net_mac       .val();
                    var net_interface  = $net_interface .val();
                    var net_ip         = $net_ip        .val();
                    var net_gateway    = $net_gateway   .val();
                    var net_allocation = $net_allocation.val();
                    
                    var __content = value + "("+key+");";
                    if ( net_mac != "" )
                        __content += "MAC=" + net_mac + ";";
                    if ( net_interface != "" )
                        __content += "Interface=" + net_interface + ";";
                    if ( net_ip != "" )
                        __content += "IP=" + net_ip + ";";
                    if ( net_gateway != "" )
                        __content += "Gateway=" + net_gateway + ";";
                    if ( net_allocation != "" )
                        __content += "Allocation=" + net_allocation + ";";
                    
                    $( ui.panel ).append( "<p>" + __content + "</p>" );
                }
            });
            </#noparse>
            
            // modal dialog init: custom buttons and a "close" callback reseting the form inside
            var $networkDialog = $( "#networkDialog" ).dialog({
                autoOpen: false,
                modal: true,
                buttons: {
                    Add: function() {
                        addNetworkTab();
                        editNet = false;
                        $( this ).dialog( "close" );
                    },
                    Cancel: function() {
                        $( this ).dialog( "close" );
                    }
                },
                open: function() {
                    $net_ip.focus();
                },
                close: function() {
                    $('#networkForm')[ 0 ].reset();
                }
            });
    
            // addNetwork form: calls addNetwork function on submit and closes the dialog
            var $networkForm = $('#networkForm');
            $networkForm.submit(function() {
                addNetworkTab();
                $networkDialog.dialog( "close" );
                return false;
            });
    
            // addNetwork button: just opens the dialog and sets 'edit' mode
            $( "#add_net" )
                .button()
                .click(function() {
                    editNet = true;
                    $networkDialog.dialog( "open" );
                });
    
            // close icon: removing the tab on click
            // note: closable tabs gonna be an option in the future - see http://dev.jqueryui.com/ticket/3924
            $( "#networkTabs span.ui-icon-close" ).live( "click", function() {
                var index = $( "li", $networkTabs ).index( $( this ).parent() );
                $networkTabs.tabs( "remove", index-1 );  // '-1' due to 'empty pattern' (built from beginning)
            });
            
            // actual addNetwork function: adds new tab using the title input from the form above
            function addNetworkTab() {
                var net_title = "Network " + net_instances;
                $networkTabs.tabs( "add", "#NT-" + net_instances, net_title );
                net_instances++;
            }
            
            // --- END of NETWORKS ----------------------------------------------------------------------

            // --- Storage ------------------------------------------------------------------------------
            var $storage_link       = $( "#storage"            );
            var $storage_mountpoint = $( "#storage_mountpoint" );
            var storage_instances   = 1;
            var editStorage         = false;
    
            <#noparse>
            // tabs init with a custom tab template and an "add" callback filling in the content
            var $storageTabs = $( "#storageTabs" ).tabs( {
                tabTemplate: "<li><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
                add: function( event, ui ) {
                    var storageTitle = "Storage " + storage_instances;
                    var mountP = $storage_mountpoint.val();
                    var key    = $storage_link.val();
                    var value  = $('select#storage :selected').text();

                    var __content = "";
                    if ( key.indexOf( "http" ) == 0 ) // cdmi
                        __content = value + ";";
                    else
                        __content = value + "(" + key + ");";
                        
                    if ( mountP != "" )
                    {
                        __content += "Mountpoint=" + mountP + ";";
                    }
                    $( ui.panel ).append( "<p>" + __content + "</p>" );
                }
            });
            </#noparse>
            
            // modal dialog init: custom buttons and a "close" callback reseting the form inside
            var $storageDialog = $( "#storageDialog" ).dialog({
                autoOpen: false,
                modal: true,
                buttons: {
                    Add: function() {
                        addStorage();
                        editStorage = false;
                        $( this ).dialog( "close" );
                    },
                    Cancel: function() {
                        $( this ).dialog( "close" );
                    }
                },
                open: function() {
                    $storage_link.focus();
                },
                close: function() {
                    $('#storageForm')[ 0 ].reset();
                }
            });
    
            // addStorage form: calls addStorage function on submit and closes the dialog
            var $storageForm = $('#storageForm');
            $storageForm.submit(function() {
                addStorage();
                $storageDialog.dialog( "close" );
                return false;
            });
    
            // addStorage button: just opens the dialog and sets 'edit' mode
            $( "#add_storage" ).button().click(function() {
                editStorage = true;
                $storageDialog.dialog( "open" );
            });
    
            // close icon: removing the tab on click
            // note: closable tabs gonna be an option in the future - see http://dev.jqueryui.com/ticket/3924
            $( "#storageTabs span.ui-icon-close" ).live( "click", function() {
                var index = $( "li", $storageTabs ).index( $( this ).parent() );
                $storageTabs.tabs( "remove", index-1 );  // '-1' due to 'empty pattern' (built from beginning)
            });
            
            // actual addStorage function: adds new tab using the title input from the form above
            function addStorage() {
                var storage_title = "Storage " + storage_instances;
                $storageTabs.tabs( "add", "#ST-" + storage_instances, storage_title );
                storage_instances++;
            }
            
            // --- END of STORAGES ----------------------------------------------------------------------

			// Form
            $('#onecomputeForm').submit(function() {
                if ( editNet ) return false;
                if ( editStorage ) return false;

                var _nets_ = "";
                for ( idx = 1; idx < net_instances; idx++ )
                {
                    var debugging = $("#NT-" + (idx)).find(":first");
                    _nets_ += debugging.text() + "_NET_";
                }

                var _storages_ = "";
                for ( idx = 1; idx < storage_instances; idx++ )
                {
                    var debugging = $("#ST-" + (idx)).find(":first");
                    _storages_ += debugging.text() + "_STORAGE_";
                }
                
                $( "#netset" ).val( _nets_ );
                $( "#storageset" ).val( _storages_ );
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


        #networkDialog label, #networkDialog input { display:block; }
        #networkDialog label { margin-top: 0.5em; }
        #networkDialog input, #networkDialog textarea { width: 95%; }
        #networkTabs { margin-top: 1em; }
        #networkTabs li .ui-icon-close { float: left; margin: 0.4em 0.2em 0 0; cursor: pointer; }
        #add_net { cursor: pointer; }

        #storageDialog label, #storageDialog input { display:block; }
        #storageDialog label { margin-top: 0.5em; }
        #storageDialog input, #storageDialog textarea { width: 95%; }
        #storageTabs { margin-top: 1em; }
        #storageTabs li .ui-icon-close { float: left; margin: 0.4em 0.2em 0 0; cursor: pointer; }
        #add_storage { cursor: pointer; }
        
    </style>
</head>
<body class="main tundra">
    <div id="vnc-console" title="VM Console">
        <iframe id="vnc-target" width="740" height="460" src="http://www.youtube.com/embed/xTSE3bohck8?rel=0" frameborder="0" allowfullscreen></iframe>
    </div>
    
    <div id="networkDialog" title="Network details">
        <form id="networkForm">
            <fieldset class="ui-helper-reset">
                <label for="network">Network </label>
                <select name="network" id="network">
                    <option value="-1">--select one--</option>
                    <#if INetworkList?? && (INetworkList?size > 0) >
                        <#list INetworkList as net>
                            <option value="${net.content[ 'id' ]}">${net.attributes[ 'occi.core.title' ]}</option>
                        </#list>
                    </#if>
                    </select><br>
                <label for="net_mac">Mac-Address </label>
                <input type="text" name="net_mac" id="net_mac" value="" class="ui-widget-content ui-corner-all" />
                <label for="net_mac">Interface </label>
                <input type="text" name="net_interface" id="net_interface" value="" class="ui-widget-content ui-corner-all" />
                <label for="net_ip">IP Address</label>
                <input type="text" name="net_ip" id="net_ip" value="" class="ui-widget-content ui-corner-all" />
                <label for="net_gateway">Gateway </label>
                <input type="text" name="net_gateway" id="net_gateway" value="" class="ui-widget-content ui-corner-all" />
                <label for="net_allocation">Allocation </label>
                <input type="text" name="net_allocation" id="net_allocation" value="" class="ui-widget-content ui-corner-all" />
            </fieldset>
        </form>
    </div>
    
    <div id="storageDialog" title="Storage details">
        <form id="storageForm">
            <fieldset class="ui-helper-reset">
                <label for="storage">Storage </label>
                <select name="storage" id="storage">
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
                            </select><br>
                <label for="storage_mountpoint">Mountpoint </label>
                <input type="text" name="storage_mountpoint" id="storage_mountpoint" value="" class="ui-widget-content ui-corner-all" />
            </fieldset>
        </form>
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
        
            <h1 align="left">OCCI:: Compute &nbsp;&nbsp;<a href="/cloud/app/xmenu.htm?session=${session.id}"><img src="../images/go_home.png" border="0" longdesc="home"></a></h1>
            <div class="xcontainer">
            <form id="onecomputeForm" method="POST" action="onecompute.htm" > 
                <input name="session"    type="hidden"   value="${session.id}"  />
                <input name="netset"     id="netset"     type="hidden" value="" />
                <input name="storageset" id="storageset" type="hidden" value="" />
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
                      <td width="85"><div align="right">Network(s):&nbsp; </div></td>
                      <td width="325"><button id="add_net">Add...</button><br>
                            <table width="520" border="0">
                                <div id="networkTabs">
                                        <ul>
                                            <li></li>
                                        </ul>
                                        <div id="NT-0">
                                            <p></p>
                                        </div>
                                </div>
                            </table>
                      </td>
                    </tr>
                    <tr>
                      <td width="85"><div align="right">Storage(s):&nbsp; </div></td>
                      <td width="325"><button id="add_storage">Add...</button><br>
                            <table width="520" border="0">
                                <div id="storageTabs">
                                        <ul>
                                            <li></li>
                                        </ul>
                                        <div id="ST-0">
                                            <p></p>
                                        </div>
                                </div>
                            </table>
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
          
			<#assign vmIDx = 1>
            <#if IElementDecoratorList?? && (IElementDecoratorList?size > 0) >
                <h2>Available VMs  (${IElementDecoratorList?size}) &nbsp;&nbsp;<a href="onecompute.htm?init=true&session=${session.id}"><img src="../images/refresh.png" border="0" alt="delete"></a></h2>
                <div class="demo">
                <div id="accordion">
                        <#list IElementDecoratorList as compute>
                            <#assign element    = compute.element>
                            <#assign caption    = compute.caption>
                            <#assign attributes = compute.values>
                            <#assign links      = compute.links>
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
                                  <table width="560" border="0">
                                    <#if links?? && (links?size > 0) >
                                        <tr class="iattribute">
                                          <td align="right" width="10%"><b>Links:</b></td>
                                          <td width="90%">&nbsp;</td>
                                        </tr>
                                            <tr class="iattribute">
                                              <td align="right" width="10%">&nbsp;</td>
                                              <td width="90%" nowrap="nowrap">
												<div id="vmlinks-${vmIDx}">
													<ul>
														<#assign idx = 1>
				                                        <#list links as lk>
															<li><a href="#vmlinks-${vmIDx}-${idx}">${lk[ 0 ]}</a></li>
														    <#assign idx = idx + 1>
				                                        </#list>
													</ul>
													<#assign idy = 1>
			                                        <#list links as lkc>
														<div id="vmlinks-${vmIDx}-${idy}">
															<p>
																<#list lkc as lP>
																	&nbsp;&nbsp;${lP}<br>
																</#list>
															</p>
														</div>
													    <#assign idy = idy + 1>
			                                        </#list>
												</div>
                                              	
                                              </td>
                                            </tr>
                                    </#if>
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
                            <#assign vmIDx = vmIDx + 1>
                        </#list>
                    </div>
    <script type="text/javascript">
        $(function(){
			<#list 1 .. vmIDx-1 as lx >
				$( "#vmlinks-${lx}" ).tabs({
					event: "mouseover"
				});
			</#list>
        });
	</script>
	
    <style>
			<#list 1 .. vmIDx-1 as lx >
	        	#vmlinks-${lx}.ui-widget { font-size: 0.9em; }
			</#list>
        });
    </style>
	
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