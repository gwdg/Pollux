/**
 * Copyright (c) 2011, X-Juvi
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of X-Juvi
 * 	  nor the names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL X-Juvi
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author         Miguel Rojas (miguel.rojas@uni-dortmund.de), Florian Feldhaus (florian.feldhaus@uni-dortmund.de)
 * @version        1.0
 * @lastrevision   15.11.2011
 */
package cloud.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cloud.services.IDirectory;
import cloud.services.cdmi.ICdmiManager;
import cloud.services.cdmi.SCDMIResponse;
import cloud.services.one.occi.ICompute;
import cloud.services.one.occi.INetwork;
import cloud.services.one.occi.IOCCIOneManager;
import cloud.services.one.occi.IStorage;
import cloud.web.SessionManager.Session;

/**
 * This class represents the main web-controller of the client.  This is in charge of 
 * processing all request from the html-GUI (GET and POST) and delegating of task to
 * services provided by OCCI and CDMI Client.
 * 
 * Two major helper classes IOCCIOneManager and ICdmiManager assist this controller
 * by processing OCCI and CDMI calls.  Note that those references are initialized by
 * Spring using <@Autowired> annotation.
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
@Controller
public class CloudWebController 
{
	@Autowired
	protected IDirectory directory;   

	@Autowired
	protected IOCCIOneManager one;   

	@Autowired
	protected ICdmiManager cdmi;   

	/**
	 * Helper Method for attending the login.htm request.  This method
	 * creates a Session-ID and holds it for serving future client-request.
	 * This Session-ID will allow to save environment variables (such as
	 * CDMI server address, OCCI server address, etc.) associated
	 * to the current user.  
	 * 
	 * This controller returns a modelview object back, so the FreeMarkerResolver
	 * is able to build the View for this request.
	 */
    @RequestMapping( "/login.htm" )
    public ModelAndView login() 
    {
        ModelAndView result = new ModelAndView();
        SessionManager sm = SessionManager.getInstance();
        Session session = sm.createSession();
        
        result.addObject( "login" );
        result.addObject( session );
        
        return result;
    }
    
    //--- LOGIN --------------------------------------------------------------------
    
    /**
     * Helper Method for making the modelview of the xmenu.htm page.
     * This method retrieves the basic server parameters (from the login page) 
     * and save them to the associated Session-ID.
     * 
     * This method supports also the option 'goback' located in all dynamic
     * pages of the Web-Client and shows the user newly the main menu.
     * 
     * This controller returns a modelview object back, so the FreeMarkerResolver
     * is able to build the view for this request.
     */
    @RequestMapping( value="/xmenu.htm")
    public ModelAndView menu( WebRequest request )
    {
        ModelAndView result = new ModelAndView();
        
        String username = request.getParameter( "username" );
        String pwd      = request.getParameter( "password" );
        String occi     = request.getParameter( "occi"     );
        String cdmi     = request.getParameter( "cdmi"     );
        String zession  = request.getParameter( "session"  );
        
        SessionManager sm = SessionManager.getInstance();
        if ( zession != null && !zession.equals( "" ) )  // coming from an internal page
        {
            Session session = sm.session( zession );
            if ( session == null )  // invalidated session
            {
                // forward to login
                return new ModelAndView( "redirect:login.htm"); 
            }
            
            result.addObject( session );
        }
        else
        {
            // DO LOGIN or SESSION was invalidated
            //if ( sm.checkSession( username, pwd ) )
            {
                Session session = sm.session( username );
                if ( session == null )  // invalidated session
                {
                    // forward to login
                    return new ModelAndView( "redirect:login.htm"); 
                }
                
                session.properties.put( Session.OCCI, occi );
                session.properties.put( Session.CDMI, cdmi );
                result.addObject( session );
            }
        }
        
        return result;
    }
    
    //----------------------------------------------------------------------------------
    //--- CDMI Web Interface -----------------------------------------------------------
    //----------------------------------------------------------------------------------

    //--- CONTAINER --------------------------------------------------------------------
    
    /**
     * Helper Method for making the modelview of the container.htm page.  This method
     * shows a html-form (for creating cdmi-'containers' in the CDMI server) 
     * and lists all available cdmi-containers.
     * 
     * This controller returns a modelview object back, so the FreeMarkerResolver
     * is able to build the initial view for this request.
     */
    @RequestMapping( value="/container.htm", method=RequestMethod.GET, params="init" ) // from menu
    public ModelAndView cdmiContainer( WebRequest request, @RequestParam( "session" ) String session, @RequestParam( "init" ) String init )
    {
        ModelAndView result = new ModelAndView();
        try
        {
            SessionManager sm = SessionManager.getInstance();
            Session s = sm.session( request.getParameter( "session" ) );
            
            // add a list with available CONTAINERs
            addContainers( result, s );
            result.addObject( s );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Helper Method for making the modelview of the container.htm page. This 
     * method process the html-form (for creating cdmi-'containers') and retrieves
     * the cdmi-container name.  Afterwards the container will be created
     * via @Autowired::cdmi Bean.
     * 
     * This controller returns a modelview object back, so the FreeMarkerResolver
     * is able to build the view for this request.  Note that the list of
     * available 'containers' will contain the recent created 'container'.
     */
    @RequestMapping( value="/container.htm", method=RequestMethod.POST )
    public ModelAndView cdmiContainer( WebRequest request )
    {
        ModelAndView result = new ModelAndView();
        
        try
        {
            SessionManager sm = SessionManager.getInstance();
            Session s = sm.session( request.getParameter( "session" ) );
            String server = s.properties.get( Session.CDMI );
            String URI = "http://"+server+"/";
            String container = request.getParameter( "container" );
            if ( container != null && !container.equals( "" ))
            {
                cdmi.createContainer( URI, container );
            }

            // add a list with available CONTAINERs
            addContainers( result, s );
            result.addObject( s );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Helper Method for making the modelview of the container.htm delete-action.  
     * This method process the html-form (for deleting a cdmi-'container') and 
     * retrieves the cdmi-container name.  Afterwards the container will be deleted
     * via @Autowired::cdmi Bean.
     * 
     * This controller returns a modelview object back, so the FreeMarkerResolver
     * is able to build the view for this request.  Note that the list of
     * available 'containers' will be updated and the recent deleted 'container' will
     * not longer appear.
     */
    @RequestMapping( value="/container.htm", params="delete" )
    public ModelAndView cdmiContainer( WebRequest request, @RequestParam( "delete" ) String delete )
    {
        ModelAndView result = new ModelAndView();
        SessionManager sm = SessionManager.getInstance();
        Session s = sm.session( request.getParameter( "session" ) );
        
        if ( delete != null && !delete.equals( "" ) )
        {
            String server = "http://"+s.properties.get( Session.CDMI );
            cdmi.deleteContainer( server, delete );
        }
        
        // add a list with available CONTAINERs
        
        addContainers( result, s );
        result.addObject( s );
        
        return result;
    }
    
    /**
     * Helper method for making a list of available cdmi-containers.  This
     * list will be available in the 'container.html' view.
     */
    protected void addContainers( ModelAndView mv, Session s )
    {
        try
        {
            String URI = "http://"+s.properties.get( Session.CDMI );
            Vector<SCDMIResponse> available = cdmi.containers( URI, "/" );
            if ( available != null && available.size() > 0 )
            {
                Vector<Container> items = new Vector<Container>( 3, 2 );
                for ( SCDMIResponse it : available )
                {
                    Container nc = new Container();
                    nc.response = it;
                    items.add( nc );
                }
                mv.addObject( items );
            }
        }
        catch ( Exception e )
        {
        }
    }
    
    //--- DATAOBJECT --------------------------------------------------------------------
    
    /**
     * Helper Method for making the modelview of the nondataobject.htm page.  This method
     * shows a html-form (for uploading of cdmi-'objects'/vm images in the CDMI server) 
     * and lists all available vm images.
     * 
     * This controller returns a modelview object back, so the FreeMarkerResolver
     * is able to build the initial view for this request.
     */
    @RequestMapping( value="/nondataobject.htm", method=RequestMethod.GET, params="init" ) // from menu
    public ModelAndView cdmiNonDataObject( WebRequest request, @RequestParam( "session" ) String session, @RequestParam( "init" ) String init )
    {
        ModelAndView result = new ModelAndView();
        try
        {
            SessionManager sm = SessionManager.getInstance();
            Session s = sm.session( request.getParameter( "session" ) );
            
            // add a list with available CONTAINERs
            addContainers( result, s );
            
            // add a list with available NoN-DataObjects filtered by Container
            addNonDataObjects( result, s );
            
            result.addObject( s );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
    * Helper Method for making the modelview of the nondataobject.htm page.  This method
    * process the html-form action (for uploading cdmi-'objects'/vm images) and retrieves
    * the vm image file.  The image will be stored locally to the webserver and 
    * afterwards it will be uploaded into the cdmi server via @Autowired::cdmi Bean.
    * 
    * This controller returns a modelview object back, so the FreeMarkerResolver
    * is able to build the view for this request.  Note that the list of
    * available 'vm images' will contain the recent uploaded 'vm image'.
    */
    @RequestMapping( value="/nondataobject.htm", method=RequestMethod.POST )
    public ModelAndView cdmiNonDataObject( @ModelAttribute( "file" ) FileUploadContainer file, DefaultMultipartHttpServletRequest request )
    {
        ModelAndView result = new ModelAndView();
        
        try
        {
            SessionManager sm = SessionManager.getInstance();
            Session s = sm.session( request.getParameter( "session" ) );
            String server = s.properties.get( Session.CDMI );
            String URI = "http://"+server;
    
            DefaultMultipartHttpServletRequest req = (DefaultMultipartHttpServletRequest)request;
            MultipartFile mpf = req.getFile( "file" );
            String filename = mpf.getOriginalFilename();
            String container = request.getParameter( "cdmicontainer" );
            if ( container != null && !container.equals( "" ) && filename != null && !filename.equals( "" ) )
            {
                String newFile = System.getProperty( "java.io.tmpdir" )+File.separator+(System.currentTimeMillis()+filename);
                File f = new File( newFile );
                mpf.transferTo( f );
                    
                cdmi.createNonDataObject( URI, container, filename, f.getAbsolutePath() );
                
                FileUtils.forceDeleteOnExit( f ); 
            }

            // add a list with available CONTAINERs
            addContainers( result, s );
            
            // add a list with available NoN-DataObjects filtered by Container
            addNonDataObjects( result, s );
            
            result.addObject( s );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
    * Helper Method for making the modelview of the nondataobject.htm delete-action.  
    * This method process the html-form (for deleting a vm image) and retrieves
    * the vm image name.  Afterwards the vm image will be deleted from the cdmi
    * server via @Autowired::cdmi Bean.
    * 
    * This controller returns a modelview object back, so the FreeMarkerResolver
    * is able to build the view for this request.  Note that the list of
    * available 'vm images' will be updated and the recent deleted 'image' will
    * not longer appear.
    */
    @RequestMapping( value="/nondataobject.htm", params="delete" )
    public ModelAndView cdmiNonDataObject( WebRequest request, @RequestParam( "delete" ) String delete )
    {
        ModelAndView result = new ModelAndView();
        SessionManager sm = SessionManager.getInstance();
        Session s = sm.session( request.getParameter( "session" ) );
        
        if ( delete != null && !delete.equals( "" ) )
        {
            String server = "http://"+s.properties.get( Session.CDMI );
            cdmi.deleteNonDataObject( server, delete );
        }
        
        // add a list with available CONTAINERs
        addContainers( result, s );
        // add a list with available NoN-DataObjects filtered by Container
        addNonDataObjects( result, s );
        
        result.addObject( s );
        
        return result;
    }
    
    /**
    * Helper method for making a list of available vm-images.  This
    * list will be available in the 'nondataobject.html' view.
    */
    protected void addNonDataObjects( ModelAndView mv, Session s )
    {
        try
        {
            String URI = "http://"+s.properties.get( Session.CDMI );
            Directory dr = nonDataObjectsAsArray( URI );
            if ( dr != null )
                mv.addObject( dr );
        }
        catch ( Exception e )
        {
        }
    }
    
    // Returns an array with all 'cdmi vm-images'.  For internal use.
    protected Directory nonDataObjectsAsArray( String URI )
    {
        try
        {
            Vector<SCDMIResponse> _aContainers = cdmi.containers( URI, "/" );
            Vector<SCDMIResponse> _aFiles      = cdmi.filesFrom ( URI, "iso;zip;img" );
            if ( _aFiles != null && _aFiles.size() > 0 )
            {
                Directory directory = new Directory();
                for ( SCDMIResponse it : _aContainers )
                {
                    // initializes all containers
                    directory.addContainer( it.getBody().get( "objectURI" ) );
                }
                
                // add all files filtered by container
                for ( SCDMIResponse it : _aFiles )
                {
                    Directory.Entry e = directory.new Entry();
                    e.uri = it.content;
                    directory.addEntry( e );
                }
                
                return directory;
            }
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }
    
    /**
    * Helper method for making a list of available cdmi-container.  This
    * list will be available in the 'container.html' view.
    */
    protected void addFiles( ModelAndView mv, Session s )
    {
        try
        {
            String URI = "http://"+s.properties.get( Session.CDMI );
            Vector<SCDMIResponse> available = cdmi.containers( URI, "/" );
            if ( available != null && available.size() > 0 )
            {
                Vector<Container> items = new Vector<Container>( 3, 2 );
                for ( SCDMIResponse it : available )
                {
                    Container nc = new Container();
                    nc.response = it;
                    items.add( nc );
                }
                mv.addObject( items );
            }
        }
        catch ( Exception e )
        {
        }
    }
    
    //----------------------------------------------------------------------------------
    //--- OCCI Web Interface -----------------------------------------------------------
    //----------------------------------------------------------------------------------

    //--- NETWORK ----------------------------------------------------------------------
    
    /**
     * Helper Method for making the modelview of the onenetwork.htm page.  This method
     * shows a html-form (for creating network resources through the OCCI server) 
     * and lists all available networks.
     * 
     * This controller returns a modelview object back, so the FreeMarkerResolver
     * is able to build the initial view for this request.
     */
    @RequestMapping( value="/onenetwork.htm", method=RequestMethod.GET, params="init" ) // from menu
    public ModelAndView oneNetwork( WebRequest request, @RequestParam( "session" ) String session, @RequestParam( "init" ) String init )
    {
        ModelAndView result = new ModelAndView();
        try
        {
            SessionManager sm = SessionManager.getInstance();
            Session s = sm.session( request.getParameter( "session" ) );
            
            // add a list with available NETWORKs
            addNetworks( result, s );
            result.addObject( s );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
    * Helper Method for making the modelview of the onenetwork.htm page.  This method
    * process the html-form action (for creating network resources) and retrieves
    * the network parameters.  Afterwards the network will be created
    * via @Autowired::occi Bean.
    * 
    * This controller returns a modelview object back, so the FreeMarkerResolver
    * is able to build the view for this request.  Note that the list of
    * available 'networks' will contain the recent created 'network'.
    */
    @RequestMapping( value="/onenetwork.htm", method=RequestMethod.POST )
    public ModelAndView oneNetwork( WebRequest request )
    {
        ModelAndView result = new ModelAndView();
        
        try
        {
            SessionManager sm = SessionManager.getInstance();
            Session s = sm.session( request.getParameter( "session" ) );
            String occi = s.properties.get( Session.OCCI );
            
            INetwork net = new INetwork();
            String URI = "http://"+occi;
            net.content.put( INetwork.URI       , URI                                     );
            net.content.put( INetwork.TITLE     , request.getParameter( "title"         ) );
            net.content.put( INetwork.SUMMARY   , request.getParameter( "summary"       ) );
            net.content.put( INetwork.ADDRESS   , request.getParameter( "address"       ) );
            net.content.put( INetwork.ALLOCATION, request.getParameter( "allocation"    ) );
            net.content.put( INetwork.VLAN      , request.getParameter( "vlan"          ) );
            
            String title = request.getParameter( "title" );
            if ( title != null && !title.equals( "" ))
            {
                one.createNetwork( net );
            }

            // add a list with available NETWORKs
            addNetworks( result, s );
            result.addObject( s );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Helper Method for making the modelview of the onenetwork.htm delete-action.  
     * This method process the html-form (for deleting a network resource) and 
     * retrieves the network id.  Afterwards the network will be deleted
     * via @Autowired::occi Bean.
     * 
     * This controller returns a modelview object back, so the FreeMarkerResolver
     * is able to build the view for this request.  Note that the list of
     * available 'networks' will be updated and the recent deleted 'network' will
     * not longer appear.
     */
    @RequestMapping( value="/onenetwork.htm", params="delete" )
    public ModelAndView oneNetwork( WebRequest request, @RequestParam( "delete" ) String delete )
    {
        if ( delete != null && !delete.equals( "" ) )
        {
            INetwork tobeDeleted = new INetwork();
            tobeDeleted.content.put( INetwork.ID, delete );
            one.deleteNetwork( tobeDeleted );
        }
        
        // add a list with available NETWORKs
        ModelAndView result = new ModelAndView();
        SessionManager sm = SessionManager.getInstance();
        Session s = sm.session( request.getParameter( "session" ) );
        
        addNetworks( result, s );
        result.addObject( s );
        
        return result;
    }
    
    /**
    * Helper method for making a list of available network resources.  This
    * list will be available in the 'onenetwork.html' view.
    */
    protected void addNetworks( ModelAndView mv, Session s )
    {
        try
        {
            String URI = "http://"+s.properties.get( Session.OCCI );
            INetwork[] available = one.getNetworks( URI );
            
            if ( available != null ) 
            {
                IElementDecorator[] decorators = new IElementDecorator[ available.length ];
                
                for ( int i = 0; i < decorators.length; i++ )
                {
                    decorators[ i ] = new IElementDecorator( available[ i ] );
                    decorators[ i ].config( new String[]{ "occi.core.title", "Title" }, 
                                            new String[][]{
                                                { "occi.core.summary"      , "Summary"    },
                                                { "occi.network.address"   , "Address"    },
                                                { "occi.network.allocation", "Allocation" },
                                                { "occi.core.id"           , "ID"         }
                                            } );
                }
                
                mv.addObject( decorators );
            }
        }
        catch ( Exception e )
        {
        }
    }
    
    //--- STORAGE ---------------------------------------------------------------
    
    /**
     * Helper Method for making the modelview of the onestorage.htm page.  This method
     * shows a html-form (for creating storage resources through the OCCI server) 
     * and lists all available storages.
     * 
     * This controller returns a modelview object back, so the FreeMarkerResolver
     * is able to build the initial view for this request.
     */
    @RequestMapping( value="/onestorage.htm", method=RequestMethod.GET, params="init" ) // from menu
    public ModelAndView oneStorage( WebRequest request, @RequestParam( "session" ) String session, @RequestParam( "init" ) String init )
    {
        ModelAndView result = new ModelAndView();
        try
        {
            SessionManager sm = SessionManager.getInstance();
            Session s = sm.session( request.getParameter( "session" ) );
            
            // add a list with available STORAGEs
            addStorages( result, s );
            result.addObject( s );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
    * Helper Method for making the modelview of the onestorage.htm page.  This method
    * process the html-form action (for uploading occi vm images) and retrieves
    * the vm image file.  The image will be stored locally to the webserver and 
    * afterwards it will be uploaded into the occi server via @Autowired::occi Bean.
    * 
    * This controller returns a modelview object back, so the FreeMarkerResolver
    * is able to build the view for this request.  Note that the list of
    * available 'storages' will contain the recent uploaded 'vm image'.
    */
    @RequestMapping( value="/onestorage.htm", method=RequestMethod.POST )
    public ModelAndView oneStorage( @ModelAttribute( "file" ) FileUploadContainer file, DefaultMultipartHttpServletRequest request )
    {
        ModelAndView result = new ModelAndView();
        
        try
        {
            SessionManager sm = SessionManager.getInstance();
            Session s = sm.session( request.getParameter( "session" ) );
            String occi = s.properties.get( Session.OCCI );
            
            IStorage storage = new IStorage();
            String URI = "http://"+occi;
            
            String cdmiLink = request.getParameter( "cdmi" );
            if ( cdmiLink != null && cdmiLink.startsWith( "http://" ) )  // cdmi storage
            {
                // use link
                storage.content.put( IStorage.URI      , URI                                     );
                storage.content.put( IStorage.TITLE    , request.getParameter( "title"         ) );
                storage.content.put( IStorage.SUMMARY  , request.getParameter( "summary"       ) );
                storage.content.put( IStorage.CDMI_LINK, cdmiLink                                );
                
                one.createStorage( storage );
            }
            else  // occi storage
            {
                DefaultMultipartHttpServletRequest req = (DefaultMultipartHttpServletRequest)request;
                MultipartFile mpf = req.getFile( "file" );
                String str = mpf.getOriginalFilename();
                
                try
                {
                    if ( str != null && !str.equals( "" ) )
                    {
                        String newFile = System.getProperty( "java.io.tmpdir" )+File.separator + str;
                        File f = new File( newFile );
                        System.out.println( String.format( "%s :: loading image locally under '%s'", this, newFile ) );
                        mpf.transferTo( f );
                        
                        storage.content.put( IStorage.URI      , URI                                     );
                        storage.content.put( IStorage.TITLE    , request.getParameter( "title"         ) );
                        storage.content.put( IStorage.SUMMARY  , request.getParameter( "summary"       ) );
                        storage.content.put( IStorage.FILE     , newFile                                 );
                        storage.content.put( IStorage.FILE_SIZE, ""+mpf.getSize()                        );
                        
                        one.createStorage( storage );
                        
                        FileUtils.forceDeleteOnExit( f ); 
                    }
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
            
            // build a list with available STORAGEs
            addStorages( result, s );
            result.addObject( s );
        }
        catch ( Exception e )
        {
        }
        
        return result;
    }
    
    /**
     * Helper Method for making the modelview of the onestorage.htm delete-action.  
     * This method process the html-form action (for deleting a storage/vm image) and 
     * retrieves the vm image id.  Afterwards the storage will be deleted
     * via @Autowired::occi Bean.
     * 
     * This controller returns a modelview object back, so the FreeMarkerResolver
     * is able to build the view for this request.  Note that the list of
     * available 'storages' will be updated and the recent deleted 'storage' will
     * not longer appear.
     */
    @RequestMapping( value="/onestorage.htm", params="delete" )
    public ModelAndView oneStorage( WebRequest request, @RequestParam( "delete" ) String delete )
    {
        if ( delete != null && !delete.equals( "" ) )
        {
            IStorage tobeDeleted = new IStorage();
            tobeDeleted.content.put( IStorage.ID, delete );
            one.deleteStorage( tobeDeleted );
        }
        
        // add a list with available STORAGEs
        ModelAndView result = new ModelAndView();
        SessionManager sm = SessionManager.getInstance();
        Session s = sm.session( request.getParameter( "session" ) );
        
        addStorages( result, s );
        result.addObject( s );
        
        return result;
    }
    
    /**
    * Helper method for making a list of available vm-images.  This
    * list will be available as in the 'onestorage.html' view as in
    * the 'onecompute.html' view.
    */
    protected void addStorages( ModelAndView mv, Session s )
    {
        try
        {
            addCDMIStorages4OCCI( mv, s );
            
            String URI = "http://"+s.properties.get( Session.OCCI );
            IStorage[] available = one.getStorages( URI );
            
            if ( available != null ) 
            {
                IElementDecorator[] decorators = new IElementDecorator[ available.length ];
                
                for ( int i = 0; i < decorators.length; i++ )
                {
                    decorators[ i ] = new IElementDecorator( available[ i ] );
                    decorators[ i ].config( new String[]{ "occi.core.title", "Title" }, 
                                            new String[][]{
                                                { "occi.core.summary"  , "Summary" },
                                                { "occi.storage.size"  , "Size"    },
                                                { "occi.storage.state" , "State"   },
                                                { "occi.core.id"       , "ID"      }
                                            } );
                }
                
                mv.addObject( decorators );
            }
        }
        catch ( Exception e )
        {
        }
    }
    
    //--- COMPUTE ---------------------------------------------------------------
    
    /**
     * Helper Method for making the modelview of the onecompute.htm page.  This method
     * shows a html-form (for creating compute resources through the OCCI server) 
     * and lists all available computes.
     * 
     * This controller returns a modelview object back, so the FreeMarkerResolver
     * is able to build the initial view for this request.
     */
    @RequestMapping( value="/onecompute.htm", method=RequestMethod.GET, params="init" ) // from menu
    public ModelAndView oneCompute( WebRequest request, @RequestParam( "session" ) String session, @RequestParam( "init" ) String init )
    {
        ModelAndView result = new ModelAndView();
        try
        {
            SessionManager sm = SessionManager.getInstance();
            Session s = sm.session( request.getParameter( "session" ) );
            
            // add a list with available COMPUTEs
            addResources( result, s );
            result.addObject( s );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        } 
        
        return result;
    }
    
    /**
    * Helper Method for making the modelview of the onecompute.htm page.  
    * This method process the html-form actions (for starting, stopping,
    * connecting to, delete vms).  Then the VM will be notified about the
    * action in the cloud manager via @Autowired::occi Bean.
    * 
    * This controller returns a modelview object back, so the FreeMarkerResolver
    * is able to build the view for this request.  Note that the list of
    * available 'vms' will contain the recent created 'vm'.
    */
    @RequestMapping( value="/onecompute.htm", params="action" )
    public ModelAndView oneCompute( WebRequest request, @RequestParam( "action" ) String action )
    {
        if ( action != null && !action.equals( "" ) )
        {
            String vm = request.getParameter( "vm" );
            if ( action.equals( "start" ) )
            {
                ICompute tobeStarted = new ICompute();
                tobeStarted.content.put( ICompute.ID, vm );
                one.startCompute( tobeStarted );
            }
            else if ( action.equals( "stop" ) )
            {
                ICompute tobeStoped = new ICompute();
                tobeStoped.content.put( ICompute.ID, vm );
                one.stopCompute( tobeStoped );
            }
            else if ( action.equals( "delete" ) )
            {
                ICompute tobeDeleted = new ICompute();
                tobeDeleted.content.put( ICompute.ID, vm );
                one.deleteCompute( tobeDeleted );
            }
        }
        
        ModelAndView result = new ModelAndView();
        SessionManager sm = SessionManager.getInstance();
        Session s = sm.session( request.getParameter( "session" ) );
        
        addResources( result, s );
        result.addObject( s );
        
        return result;
    }
    
    /**
    * Helper Method for making the modelview of the onecompute.htm page.  
    * This method process the html-form create-action.  Afterwards the VM 
    * features will be fetched from the html-form and a new VM will be 
    * created in the cloud manager via @Autowired::occi Bean.
    * 
    * This controller returns a modelview object back, so the FreeMarkerResolver
    * is able to build the view for this request.  Note that the list of
    * available 'vms' will contain the recent created 'vm'.
    */
    @RequestMapping( value="/onecompute.htm", method=RequestMethod.POST )
    public ModelAndView oneCompute( WebRequest request )
    {
        SessionManager sm = SessionManager.getInstance();
        Session s = sm.session( request.getParameter( "session" ) );
        String occi = s.properties.get( Session.OCCI );
        
        ICompute compute = new ICompute();
        String URI = "http://"+occi;
        
        try
        {
            compute.content.put( ICompute.URI         , URI                                    );
            compute.content.put( ICompute.TITLE       , request.getParameter( "title"        ) );
            compute.content.put( ICompute.SUMMARY     , request.getParameter( "summary"      ) );
            compute.content.put( ICompute.ARCHITECTURE, request.getParameter( "architecture" ) );
            compute.content.put( ICompute.CORES       , request.getParameter( "cores"        ) );
            compute.content.put( ICompute.MEMORY      , request.getParameter( "memory"       ) );
            compute.content.put( ICompute.NETWORK     , request.getParameter( "network"      ) );
            compute.content.put( ICompute.STORAGE     , request.getParameter( "storage"      ) );
            
            String title = request.getParameter( "title" );
            if ( title != null && !title.equals( "" ))
            {
                one.createCompute( compute );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        ModelAndView result = new ModelAndView();
        addResources( result, s );
        result.addObject( s );
        
        return result;
    }
    
    /**
    * Helper method for making a list of available networks vm-images.  
    * Those lists will be available as scrollboxes in the 'onecompute.html' view
    * so the user can select one of them during creation of VMs.
    */
    protected void addResources( ModelAndView mv, Session s )
    {
        try
        {
            String URI = "http://"+s.properties.get( Session.OCCI );
            ICompute[] available = one.getComputes( URI );
            
            if ( available != null ) 
            {
                IElementDecorator[] decorators = new IElementDecorator[ available.length ];
                
                for ( int i = 0; i < decorators.length; i++ )
                {
                    decorators[ i ] = new IElementDecorator( available[ i ] );
                    decorators[ i ].config( new String[]{ "occi.core.title", "Title" }, 
                                            new String[][]{
                                                { "occi.core.summary"          , "Summary"      },
                                                { "occi.compute.architecture"  , "Architecture" },
                                                { "occi.compute.cores"         , "Cores"        },
                                                { "occi.compute.memory"        , "Memory"       },
                                                { "occi.core.id"               , "ID"           },
                                                { "occi.compute.state"         , "State"        },
                                                { "occi.infrastructure.network", "Network"      },
                                                { "occi.infrastructure.storage", "Storage"      }
                                            } );
                }
                
                mv.addObject( decorators );
            }
           
            addNetworks4VMs( mv, s );
            addStorages4VMs( mv, s );
        }
        catch ( Exception e )
        {
        }
    }
    
    protected void addNetworks4VMs( ModelAndView mv, Session s )
    {
        try
        {
            String URI = "http://"+s.properties.get( Session.OCCI );
            INetwork[] available = one.getNetworks( URI );
            
            if ( available != null ) 
            {
                mv.addObject( available );
            }
        }
        catch ( Exception e )
        {
        }
    }
    
    protected void addStorages4VMs( ModelAndView mv, Session s )
    {
        try
        {
            String occiURI = "http://"+s.properties.get( Session.OCCI );
            IStorage[] occiStorage = one.getStorages( occiURI );

            String cdmiURI = "http://"+s.properties.get( Session.CDMI );
            Directory cdmiStorage = nonDataObjectsAsArray( cdmiURI );

            if ( occiStorage != null ) mv.addObject( occiStorage );
            if ( cdmiStorage != null ) mv.addObject( cdmiStorage );
        }
        catch ( Exception e )
        {
        }
    }
    
    protected void addCDMIStorages4OCCI( ModelAndView mv, Session s )
    {
        try
        {
            String cdmiURI = "http://"+s.properties.get( Session.CDMI );
            Directory cdmiStorage = nonDataObjectsAsArray( cdmiURI );
            
            if ( cdmiStorage != null ) mv.addObject( cdmiStorage );
        }
        catch ( Exception e )
        {
        }
    }
    
    //--- TEMPLATE ---------------------------------------------------------------
    
    /**
     * TOBE IMPLEMENTED
     */
    @RequestMapping( value="/onetemplate.htm" )
    // ModelAndView
    public void oneTemplate(){}
    
    @RequestMapping( value="/onetemplate.htm", params="form" )
    public ModelAndView oneTemplate( @RequestParam("url") String url )
    {
        ModelAndView result = new ModelAndView();
        
        return result;
    }
    
    //--- Fileupload Support -------------------------------------------------------------
    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( byte[].class, 
                                     new ByteArrayMultipartFileEditor() );
    }
    
    public static class FileUploadContainer
    {
        public FileUploadContainer(){}
        public FileUploadContainer( byte[] file ){ this.file = file; }
        public void init( byte[] file ){ this.file = file; }
        
        private byte[] file;

        public void setFile( byte[] file ) 
        {
            this.file = file;
        }

        public byte[] getFile() 
        {
            return file;
        }
    }
    
    //-----------------------------------------------------------------------------------
    //--- DECORATOR CLASSES for displaying Elements in Web Views    ---------------------
    //-----------------------------------------------------------------------------------
    
    public class Container
    {
        public String id  ;
        public String name;
        
        public SCDMIResponse response;
        
        public String getId  (){ return id;   }
        public String getName(){ return name; }
        public SCDMIResponse getResponse(){ return response; }
    }
    
    public class Directory
    {
        public Directory()
        {
            content = new Hashtable<String, Vector<Entry>>();
        }
        
        public int getEntries()
        {
            int result = 0;
            
            Enumeration<String> keys = content.keys();
            while ( keys.hasMoreElements() )
            {
                String K = keys.nextElement();
                Vector<Entry> subc = content.get( K );
                result += subc.size();
            }
            
            return result;
        }
        
        public ArrayList<String> getKeys()
        { 
            ArrayList<String> result = new ArrayList<String>();
            Enumeration<String> keys = content.keys();
            while ( keys.hasMoreElements() )
            {
                String K = keys.nextElement();
                result.add( K );
            }
            
            return result;
        }
        
        public Hashtable<String, Vector<Entry>> getContent()
        {
            return content;
        }
        
        public void addContainer( String container )
        {
            Vector<Entry> target = content.get( container );
            if ( target == null )
            {
                target = new Vector<Entry>();
                content.put( container, target );
            }
        }
        
        public void addEntry( Entry e )
        {
            ArrayList<String> keys = getKeys();
            for ( String k : keys )
            {
                if ( e.uri.indexOf( k+"/" ) != -1 )
                {
                    content.get( k ).add( e );
                }
            }
        }
        
        public Hashtable<String, Vector<Entry>> content;
        
        public class Entry
        {
            public String uri ;
            public String getUri (){ return uri; }
        }
    }
    
    public static void main( String[] args )
    {
        String a = "http://localhost:2364/WubiDirectory/images.zip";
        String b = "/X-WubiClub"+ "/";
        
        System.out.println( a.contains( b ) );
        
    }
}