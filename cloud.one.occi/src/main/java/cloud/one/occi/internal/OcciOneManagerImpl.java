/**
 * Copyright (c) 2011, Pollux
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Pollux
 * 	  nor the names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Pollux
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
package cloud.one.occi.internal;

import java.io.File;
import java.util.Hashtable;

import org.springframework.stereotype.Component;

import cloud.services.one.occi.ICompute;
import cloud.services.one.occi.INetwork;
import cloud.services.one.occi.IOCCIOneManager;
import cloud.services.one.occi.IStorage;
import cloud.services.one.occi.ITemplate;

/**
 * This class represents the main Spring Java Bean of the OCCI-Client.  
 * This JavaBean provides utilities for creating, deleting and handling
 * of cloud resources (Network, Storage and VMs).  
 * 
 * OcciOneManagerImpl is in charge of making http-calls to services 
 * exposed by a OCCI-server. The results of those calls will be
 * normally handled by a GUI interface. 
 * 
 * Each operation (create, delete) associated to network, storage and vm 
 * is supported by a helper class which contains the attributes of the
 * respective resource (INetwork, IStorage, ICompute respectively).  
 * 
 * Note that this JavaBean and helper classes are fully independent of 
 * any WebController.
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
@Component( "occi-one" )
public class OcciOneManagerImpl implements IOCCIOneManager 
{
    // --------   NETWORK    --------
    @Override
    public String createNetwork( INetwork data )
    {
        try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            Hashtable<String, String> body    = new Hashtable<String, String>();
            
            headers.put( "Accept", "*/*" );
            headers.put( "Category", "network;scheme=\"http://schemas.ogf.org/occi/infrastructure#\";class=\"kind\";," +
                                     "ipnetwork;scheme=\"http://schemas.ogf.org/occi/infrastructure/network#\";class=\"kind\";" );
            
            String attr = "occi.core.title=\"%s\","         +
                          "occi.core.summary=\"%s\","       +
                          "occi.network.address=\"%s\","    +
                          "occi.network.allocation=\"%s\"," +
                          "occi.network.vlan=%s";
            
            headers.put( "X-OCCI-Attribute", String.format( attr,
                                                            data.getContent().get( INetwork.TITLE      ),
                                                            data.getContent().get( INetwork.SUMMARY    ),
                                                            data.getContent().get( INetwork.ADDRESS    ),
                                                            data.getContent().get( INetwork.ALLOCATION ),
                                                            data.getContent().get( INetwork.VLAN       )
                                                            ) );
            
            String URI = data.getContent().get( INetwork.URI ) + "/network/";
            OcciResponse response = HttpUtils.post( URI, 
                                                    headers, 
                                                    body );
            String result = null;
            if ( response.content != null ) result = response.content[ 0 ];
            return result;
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }

    @Override
    public String deleteNetwork( INetwork data )
    {
        try
        {
            String ID = data.getContent().get( INetwork.ID );
            OcciResponse response = HttpUtils.delete( HttpUtils.GWDGURI+HttpUtils.network+ID );
            return ""+response.code;
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }

    // --------   STORAGE    --------
    /*
     * T
     * @see cloud.services.one.occi.IOCCIOneManager#createStorage(cloud.services.one.occi.IStorage)
     */
    @Override
    public String createStorage( IStorage data )
    {
        try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            Hashtable<String, String> body    = new Hashtable<String, String>();
            
            OcciResponse response = null;
            
            String cdmiLink = data.getContent().get( IStorage.CDMI_LINK );
            String title = data.getContent().get( IStorage.TITLE );
            if ( cdmiLink != null && cdmiLink.startsWith( "http" ) )
            {
                headers.put( "Accept"      , "text/occi" );
                headers.put( "Content-Type", "text/occi" );
                headers.put( "Category"    , "storage;scheme=\"http://schemas.ogf.org/occi/infrastructure#\";class=\"kind\";" );
                
                String link = "<%s>;rel=" + "\"http://schemas.ogf.org/occi/core#link\";category=\"http://schemas.ogf.org/occi/infrastructure#storagelink\";";
                
                headers.put( "Link", String.format( link,
                                                    cdmiLink
                            ) );
                
                String attr = "occi.core.title=\"%s\","  +
                              "occi.core.summary=\"%s\"" ;
                
                headers.put( "X-OCCI-Attribute", String.format( attr,
                                                                data.getContent().get( IStorage.TITLE   ),
                                                                data.getContent().get( IStorage.SUMMARY )
                ) );
                
                String URI = data.getContent().get( IStorage.URI ) + "/storage/";
                response = HttpUtils.post( URI, headers, body );
            }
            else if(title.equalsIgnoreCase(HttpUtils.OCCIStorage))
            {
                headers.put( "Accept"      , "text/occi" );
                headers.put( "Content-Type", "text/occi" );
                headers.put( "Category", "storage;scheme=\"http://schemas.ogf.org/occi/infrastructure#\";class=\"kind\";" );
                
                String attr = "occi.core.title=\"%s\","  +
                "occi.core.summary=\"%s\"" ;
                
                headers.put( "X-OCCI-Attribute", String.format( attr,
                                                                data.getContent().get( IStorage.TITLE   ),
                                                                data.getContent().get( IStorage.SUMMARY )
                ) );
                
                File[] files = new File[]{ new File( data.getContent().get( IStorage.FILE ) ) };
                String URI = data.getContent().get( IStorage.URI ) + "/storage/";
                response = HttpUtils.multipartPost( URI, headers, 
                                                                 null, files );
            }
            else if(title.equalsIgnoreCase(HttpUtils.NFSStorage) ){
            	headers.put( "Accept"      , "text/occi" );
                headers.put( "Content-Type", "text/occi" );
                headers.put( "Category", "nfsstorage;scheme=\"http://schemas.ogf.org/gwdg#\";class=\"kind\";" );
                
                String attr = "occi.storage.export=\"%s\","  +
                "occi.storage.size=\"%s\"" ;
                
                headers.put( "X-OCCI-Attribute", String.format( attr,
                                                                data.getContent().get( IStorage.EXPORT   ),
                                                                data.getContent().get( IStorage.FILE_SIZE )
                ) );
                
                //File[] files = new File[]{ new File( data.getContent().get( IStorage.FILE ) ) };
                String URI = data.getContent().get( IStorage.URI ) + "/nfsstorage/";
                response = HttpUtils.post( URI, headers, body );
            }
            else {
            	System.out.println("The storage is not created, because the storage type is not specified.");
            	return null;
            }
            
            
            String result = null;
            if ( response.content != null ) result = response.content[ 0 ];
            
            return result;
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }

    @Override
    /*
     * T
     */
    public String deleteStorage( IStorage data, String storageType )
    {
        try
        {
        	if(storageType.equalsIgnoreCase(HttpUtils.NFSStorage)){
        		String ID = data.getContent().get( IStorage.ID );
                OcciResponse response = HttpUtils.delete( HttpUtils.GWDGURI+HttpUtils.nfsstorage+ ID );
                return ""+response.code;
        	}
        	// other kinds of storage will not be annotated with storage type.
        	else{
        		String ID = data.getContent().get( IStorage.ID );
                OcciResponse response = HttpUtils.delete( HttpUtils.GWDGURI+HttpUtils.storage+ ID );
                return ""+response.code;
            }
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }

    // --------   COMPUTE    --------

    @Override
    public String createCompute( ICompute data )
    {
        try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            Hashtable<String, String> body    = new Hashtable<String, String>();
            
            headers.put( "Accept"      , "text/occi" );
            headers.put( "Content-Type", "text/occi" );
            headers.put( "Category"    , "compute;scheme=\"http://schemas.ogf.org/occi/infrastructure#\";class=\"kind\";" );
            
            String attr = "occi.core.title=\"%s\","           +
            "occi.core.summary=\"%s\","         +
            "occi.compute.architecture=\"%s\"," +
            "occi.compute.cores=\"%s\","        +
            "occi.compute.memory=%s"            
            +
            "net_tx"+
            "net_rx"+
            "cpu"+
            "memory"
            ;
            
            headers.put( "X-OCCI-Attribute", String.format( attr,
                                                            data.getContent().get( ICompute.TITLE        ),
                                                            data.getContent().get( ICompute.SUMMARY      ),
                                                            data.getContent().get( ICompute.ARCHITECTURE ),
                                                            data.getContent().get( ICompute.CORES        ),
                                                            data.getContent().get( ICompute.MEMORY       )
            ) );
            
            //String link = "";
            String networkData = data.getContent().get( ICompute.NETWORK );
            String storageData = data.getContent().get( ICompute.STORAGE );
            
            INetwork[] networkArray = getNetworkArray( networkData );
            IStorage[] storageArray = getStorageArray( storageData );
            
            String networkPattern           = "</network/%s>;rel=" + "\"http://schemas.ogf.org/occi/core#link\";category=\"http://schemas.ogf.org/occi/infrastructure#networkinterface%s\";";
            String networkExtPattern        = " http://schemas.ogf.org/occi/infrastructure/networkinterface#ipnetworkinterface";
            String networkMacPattern        = "occi.networkinterface.mac=\"%s\";";
            String networkInterfacePattern  = "occi.networkinterface.interface=\"%s\";";
            String networkIPPattern         = "occi.networkinterface.address=\"%s\";";
            String networkGatewayPattern    = "occi.networkinterface.gateway=\"%s\";";
            String networkAllocPattern      = "occi.networkinterface.allocation=\"%s\";";
            String cdmiStoragePattern       = "<%s>;rel=" + "\"http://schemas.ogf.org/occi/core#link\";category=\"http://schemas.ogf.org/occi/infrastructure#storagelink\";";
            String occiStoragePattern       = "</storage/%s>;rel=" + "\"http://schemas.ogf.org/occi/infrastructure#storage\";category=\"http://schemas.ogf.org/occi/infrastructure#storagelink\";";
            String nfsStoragePattern        = "</nfsstorage/%s>;rel=" + "\"http://schemas.ogf.org/gwdg#nfsstorage\";category=\"http://schemas.ogf.org/occi/infrastructure#storagelink\";http://schemas.ogf.org/occi/infrastructure#storagelink\";occi.storagelink.state=\"active\";occi.storagelink.mountpoint=\"%s\";occi.storagelink.deviceid=\"nfs\";";

            StringBuffer sbNetwork = new StringBuffer(); 
            for ( int i = 0; i < networkArray.length; i++ )
            {
                // Network interface
                INetwork in        = networkArray[ i ];
                String nID         = in.getAttributes().get( INetwork.ID         );
                String nMac        = in.getAttributes().get( INetwork.MAC        );
                String nInterface  = in.getAttributes().get( INetwork.INTERFACE  );
                String nIP         = in.getAttributes().get( INetwork.ADDRESS    );
                String nGateway    = in.getAttributes().get( INetwork.GATEWAY    );
                String nAlloc      = in.getAttributes().get( INetwork.ALLOCATION );
                
                if ( ( nIP      != null && !nIP   .equals( "" ) ) ||
                     ( nGateway != null && !nMac  .equals( "" ) ) ||
                     ( nAlloc   != null && !nAlloc.equals( "" ) ) )
                {
                    sbNetwork.append( String.format( networkPattern, nID, networkExtPattern ) );
                }
                else
                {
                    sbNetwork.append( String.format( networkPattern, nID, "" ) );
                }
                
                if ( nMac != null && !nMac.equals( "" ) )
                    sbNetwork.append( String.format( networkMacPattern, nMac ) );
                
                if ( nInterface != null && !nInterface.equals( "" ) )
                    sbNetwork.append( String.format( networkInterfacePattern, nInterface ) );
                
                if ( nIP != null && !nIP.equals( "" ) )
                    sbNetwork.append( String.format( networkIPPattern, nIP ) );
                
                if ( nGateway != null && !nMac.equals( "" ) )
                    sbNetwork.append( String.format( networkGatewayPattern, nGateway ) );
                
                if ( nAlloc != null && !nAlloc.equals( "" ) )
                    sbNetwork.append( String.format( networkAllocPattern, nAlloc ) );
                
               if ( i < networkArray.length-1 ) // more networks ?
                   sbNetwork.append( "," );
            }
            
            StringBuffer sbStorage = new StringBuffer(); 
            for ( int i = 0; i < storageArray.length; i++ )
            {
                // Storage
                IStorage storage = storageArray[ i ];
                String uri = storage.getAttributes().get( IStorage.URI );
                
                if ( uri != null && !uri.equals( "" ) ) // it's a CDMI storage
                {
                    sbStorage.append( String.format( cdmiStoragePattern, uri ) );
                }
                else {
					String title = storage.getAttributes().get(IStorage.TITLE);
					if (title.equalsIgnoreCase(HttpUtils.OCCIStorage)) { 
						// it's an OCCI-image
						String id = storage.getAttributes().get(IStorage.ID);
						sbStorage.append(String.format(occiStoragePattern, id));
					} else if (title.equalsIgnoreCase(HttpUtils.NFSStorage)) { 
						// it is a NFS-image
						String id = storage.getAttributes().get(IStorage.ID);
						String mout_point = storage.getAttributes().get(IStorage.MOUNT_POINT);
						sbStorage.append(String.format(nfsStoragePattern, id, mout_point));
					}

				}
                
                if ( i < storageArray.length-1 ) // more storages ?
                    sbStorage.append( "," );
            }
            
            StringBuffer links = new StringBuffer();
            String networkSET = sbNetwork.toString().trim();
            System.out.println("Network: "+sbNetwork);
            String storageSET = sbStorage.toString().trim();
            
            links.append( networkSET );
            if ( !links.toString().equals( "" ) )
                links.append( "," );
            links.append( storageSET );
            
            headers.put( "Link", links.toString() ); 

            String URI = data.getContent().get( INetwork.URI ) + "/compute/";
            OcciResponse response = HttpUtils.post( URI, 
                                                    headers, 
                                                    body );
            
            String result = null;
            if ( response.content != null ) result = response.content[ 0 ];
            return result;
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }
    
    @Override
    public String deleteCompute( ICompute data )
    {
        try
        {
            String ID = data.getContent().get( ICompute.ID );
            OcciResponse response = HttpUtils.delete( HttpUtils.GWDGURI+HttpUtils.compute+ID );
            return ""+response.code;
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }

    @Override
    public String startCompute( ICompute data )
    {
        try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            Hashtable<String, String> body    = new Hashtable<String, String>();
            
            headers.put( "Accept"      , "text/occi" );
            headers.put( "Content-Type", "text/occi" );
            headers.put( "Category", "start; scheme=\"http://schemas.ogf.org/occi/infrastructure/compute/action#\";class=\"action\"" );
            
            headers.put( "X-OCCI-Attribute", "method=\"poweron\"" );
            
            String URI = HttpUtils.GWDGURI + HttpUtils.compute + data.getContent().get( ICompute.ID ) + "?action=start";
            OcciResponse response = HttpUtils.post( URI, headers, body );
            String result = null;
            if ( response.content != null ) result = response.content[ 0 ];
            return result;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public String restartCompute(ICompute data){
    	try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            Hashtable<String, String> body    = new Hashtable<String, String>();
            
            headers.put( "Accept"      , "text/occi" );
            headers.put( "Content-Type", "text/occi" );
            headers.put( "Category", "restart; scheme=\"http://schemas.ogf.org/occi/infrastructure/compute/action#\";class=\"action\"" );
            
            headers.put( "X-OCCI-Attribute", "method=\"cold\"" );
            
            String URI = HttpUtils.GWDGURI + HttpUtils.compute +data.getContent().get( ICompute.ID ) + "?action=restart";
            OcciResponse response = HttpUtils.post( URI, headers, body );
            String result = null;
            if ( response.content != null ) result = response.content[ 0 ];
            return result;
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }
    

    @Override
    public String stopCompute( ICompute data )
    {
        try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            Hashtable<String, String> body    = new Hashtable<String, String>();
            
            headers.put( "Accept"      , "text/occi" );
            headers.put( "Content-Type", "text/occi" );
            headers.put( "Category", "stop; scheme=\"http://schemas.ogf.org/occi/infrastructure/compute/action#\";class=\"action\"" );
            
            headers.put( "X-OCCI-Attribute", "method=\"poweroff\"" );
            
            String URI = HttpUtils.GWDGURI + HttpUtils.compute + data.getContent().get( ICompute.ID ) + "?action=stop";
            OcciResponse response = HttpUtils.post( URI, headers, body );
            String result = null;
            if ( response.content != null ) result = response.content[ 0 ];
            return result;
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }

    // --------   TEMPLATE    --------
    @Override
    public String createTemplate( ITemplate data )
    {
        // TODO: tobeimplemented
        return null;
    }

    @Override
    public String deleteTemplate( ITemplate data )
    {
        // TODO: tobeimplemented
        return null;
    }
    
    // --------   LISTs    --------
    
    public INetwork[] getNetworks( String uri )
    { 
        INetwork[] result = null;
        try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            headers.put( "Accept", "*/*" );
            headers.put( "Category", "network;scheme=\"http://schemas.ogf.org/occi/infrastructure#\";class=\"kind\";" );
            
            String URI = uri + "/network/";
            OcciResponse response = HttpUtils.get( URI, 
                                                   headers );

            if ( response.content != null && response.content.length > 0 && response.code != 500 )
            {
                result = new INetwork[ response.content.length ];
                int i = 0;
                for ( String s : response.content )
                {
                    result[ i ] = new INetwork();
                    String networkURI = s.substring( s.indexOf( "network" ) + 8 );
                    result[ i ].getContent().put( INetwork.ID , networkURI       );
                    result[ i ].getContent().put( INetwork.URI, URI + networkURI );
                    
                    
                    i++;
                }
                OcciExecutorCompletionService engine = new OcciExecutorCompletionService();
                engine.fetchNetworkInformation( headers, result );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /*
     * T
     * get all the storage by providing URI of OCCI server
     */
    public IStorage[] getStorages( String uri )
    { 
        IStorage[] result = null;
        try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            headers.put( "Accept", "*/*" );
            headers.put( "Category", "storage;scheme=\"http://schemas.ogf.org/occi/infrastructure#\";class=\"kind\";" );
            
            String URI = uri + "/storage/";
            OcciResponse response = HttpUtils.get( URI, 
                                                   headers );

            if ( response.content != null && response.content.length > 0 && response.code != 500 )
            {
                result = new IStorage[ response.content.length ];
                int i = 0;
                for ( String s : response.content )
                {
                    result[ i ] = new IStorage();
                    String storageURI = s.substring( s.indexOf( "storage" ) + 8 );
                    result[ i ].getContent().put( IStorage.ID, storageURI        );
                    result[ i ].getContent().put( IStorage.URI, URI + storageURI );
                    System.out.println("Storage :  "+ storageURI);
                    
                    i++;
                }
                
                OcciExecutorCompletionService engine = new OcciExecutorCompletionService();
                engine.fetchStorageInformation( headers, result );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /*
     * T
     * get all the storage by providing URI of OCCI server
     */
    public IStorage[] getNFSStorages( String uri )
    { 
        IStorage[] result = null;
        try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            headers.put( "Accept", "*/*" );
            headers.put( "Category", "nfsstorage;scheme=\"http://schemas.ogf.org/gwdg#\";class=\"kind\";" );
            
            String URI = uri + HttpUtils.nfsstorage;
            OcciResponse response = HttpUtils.get( URI, 
                                                   headers );

            if ( response.content != null && response.content.length > 0 && response.code != 500 )
            {
                result = new IStorage[ response.content.length ];
                int i = 0;
                for ( String s : response.content )
                {
                    result[ i ] = new IStorage();
                    String storageURI = s.substring( s.indexOf( "nfsstorage" ) + 11 );
                    result[ i ].getContent().put( IStorage.ID, storageURI        );
                    result[ i ].getContent().put( IStorage.URI, URI + storageURI );
                    result[ i ].getContent().put( IStorage.TITLE, HttpUtils.NFSStorage );
                    System.out.println("Storage :  "+ storageURI);
                    
                    i++;
                }
                
                OcciExecutorCompletionService engine = new OcciExecutorCompletionService();
                engine.fetchStorageInformation( headers, result );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
	 * Get the OCCI Storage ID with name.
	 */

	public String getStorageId(String storageName){
		try {
			System.out.println("START SEARCHING FOR STORAGE"+storageName);
			IStorage[] storages = this.getStorages(HttpUtils.GWDGURI);
			String storageID = null;
			if (storages != null){
				for (IStorage s : storages) {
					if(s.getAttributes().get("occi.core.title").equalsIgnoreCase(storageName)){
					storageID = s.getAttributes().get("occi.core.id");
					System.out.println("--   STORAGE IS FOUND   --"+storageID);
					break;
					}
					else continue;
				}
				return storageID;
			}
			else {
				System.out.println("NO STORAGE FOUND FOR "+storageName);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    public ICompute [] getComputes ( String uri )
    { 
        ICompute[] result = null;
        try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            headers.put( "Accept", "*/*" );
            headers.put( "Category", "compute; scheme=\"http://schemas.ogf.org/occi/infrastructure#\";class=\"kind\";" );
            
            String URI = uri + "/compute/";
            OcciResponse response = HttpUtils.get( URI, 
                                                   headers );

            if ( response.content != null && response.content.length > 0 && response.code != 500 )
            {
                result = new ICompute[ response.content.length ];
                int i = 0;
                for ( String s : response.content )
                {
                    result[ i ] = new ICompute();
                    String computeURI = s.substring( s.indexOf( "compute" ) + 8 );
                    result[ i ].getContent().put( ICompute.ID, computeURI        );
                    result[ i ].getContent().put( ICompute.URI, URI + computeURI );
                    
                    i++;
                }
                OcciExecutorCompletionService engine = new OcciExecutorCompletionService();
                engine.fetchVmInformation( headers, result );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public ITemplate[] getTemplates( String uri )
    { 
        ITemplate[] result = null;
        return result; 
    }
    
    protected INetwork[] getNetworkArray( String networkSet )
    {
        try
        {
            String separator = "_NET_";
            String[] elements = networkSet.split( separator );
            INetwork[] result = new INetwork[ elements.length ];
            for ( int i = 0; i < elements.length; i++ )
            {
                // example:  TestNetwork(627ba25a-09e8-11e1-997c-00163e211147);Interface=Intx;MAC=MACx;IP=IPx;Gateway=GWx;Allocation=ALLOx;
                
                result[ i ]       = new INetwork();
                String Name       = attr( elements[ i ], null         );
                String MAC        = attr( elements[ i ], "MAC"        );
                String Interface  = attr( elements[ i ], "Interface"  );
                String IP         = attr( elements[ i ], "IP"         );
                String Gateway    = attr( elements[ i ], "Gateway"    );
                String Allocation = attr( elements[ i ], "Allocation" );
                
                if ( Name != null )
                {
                    String id    = Name.substring( Name.indexOf( "(" )+1, Name.indexOf( ")" ) );
                    String title = Name.substring( 0, Name.indexOf( "(" ) );
                    result[ i ].getAttributes().put( INetwork.ID   , id    );
                    result[ i ].getAttributes().put( INetwork.TITLE, title );
                }
                if ( MAC != null )
                    result[ i ].getAttributes().put( INetwork.MAC, MAC );
                if ( Interface != null )
                    result[ i ].getAttributes().put( INetwork.INTERFACE, Interface );
                if ( IP != null )
                    result[ i ].getAttributes().put( INetwork.ADDRESS, IP );
                if ( Gateway != null )
                    result[ i ].getAttributes().put( INetwork.GATEWAY, Gateway );
                if ( Allocation != null )
                    result[ i ].getAttributes().put( INetwork.ALLOCATION, Allocation );
            }
            
            return result;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    protected IStorage[] getStorageArray( String storageSet )
    {
        try
        {
            //String separator = "_STORAGE_";
            String[] elements = storageSet.split( HttpUtils.storageSeparator );
            IStorage[] result = new IStorage[ elements.length ];
            for ( int i = 0; i < elements.length; i++ )
            {
                // example-1:  http://129.217.211.163:2364/derby/ttylinux.img;Mountpoint=/here;
                // example-2:  OCCIStorage(ID);Mountpoint=/data/test2;
            	// example-3:  NFSStorage(ID);Mountpoint=/data/test1;
                
                result[ i ]       = new IStorage();
                String Name       = attr( elements[ i ], null         );
                String Mountpoint = attr( elements[ i ], "Mountpoint" );
                
                if ( Name != null )
                {
                    String id    = "";
                    String title = "";
                    String uri   = "";
                    
                    if ( Name.indexOf( HttpUtils.CDMIStorage ) == 0 ) // CDMI
                    {
                        uri = Name;
                        result[ i ].getAttributes().put( IStorage.URI, uri );
                    }
                    else if ( Name.indexOf( HttpUtils.OCCIStorage ) == 0 ) // OCCI
                    {
                        title = Name.substring( 0, Name.indexOf( "(" ) );
                        id    = Name.substring( Name.indexOf( "(" )+1, Name.indexOf( ")" ) );
                        result[ i ].getAttributes().put( IStorage.ID   , id    );
                        result[ i ].getAttributes().put( IStorage.TITLE, title );
                    }
                    else if ( Name.indexOf( HttpUtils.NFSStorage ) == 0 ) // NFS
                    {
                        title = Name.substring( 0, Name.indexOf( "(" ) );
                        id    = Name.substring( Name.indexOf( "(" )+1, Name.indexOf( ")" ) );
                        result[ i ].getAttributes().put( IStorage.ID   , id    );
                        result[ i ].getAttributes().put( IStorage.TITLE, title );
                    }
                    
                }
                if ( Mountpoint != null )
                    result[ i ].getAttributes().put( IStorage.MOUNT_POINT, Mountpoint );
            }
            
            return result;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    
    protected static String attr( String data, String key )
    {
        if ( key == null ) // returns first attribute
        {
            String result = data.substring( 0, data.indexOf( ";" ) );
            return result;
        }
        
        int indexOf = data.indexOf( key );
        if ( indexOf != -1 )
        {
            String result = data.substring( indexOf+key.length()+1, data.indexOf( ";", indexOf+key.length() ) );
            return result;
        }
        
        return null;
    }
    
    /**
     * T
	 * Get the Network ID of Network with name "GWDG-Cloud (968)"
	 * the purpose is before creating a VM, we have to check if 
	 * the ID (HEX Code) GWDG Network has changed or not. We always 
	 * have to fetch the most updated one. 
	 * 
	 * reason, when we restart the occi server, a new network ID will
	 * be assigned to "GWDG-Cloud (968)"
	 */

	public String getNetworkId(String networkName){
		try {
			System.out.println("--  GETTING SPECIFIC NETWORK  --");
			INetwork[] networks = this.getNetworks(HttpUtils.GWDGURI);
			String networkID = null;
			if (networks != null){
				for (INetwork s : networks) {
					if(s.getAttributes().get("occi.core.title").equalsIgnoreCase(networkName)){
					networkID = s.getAttributes().get("occi.core.id");
					System.out.println("--   GWDG NETWORK IS FOUND   --"+networkID);
					break;
					}
					else continue;
				}
				return networkID;
			}
			else {
				System.out.println("NO NETWORK FOUND FOR "+networkName);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * get network transmission value
	 */
    public String getNetworkTransmission( String uri, String computeID)
    { 
        try
        {
            Hashtable<String, String> headers = new Hashtable<String, String>();
            headers.put( "Accept", "*/*" );
            headers.put( "Category", "net_tx;scheme=\"http://example.com/occi/infrastructure/metric/compute/net_tx#\";class=\"mixin\";" );
            
            
            String URI = uri + "/compute/"+computeID;
            OcciResponse response = HttpUtils.get( URI, 
                                                   headers );

            if ( response.content != null && response.content.length > 0 && response.code != 500 )
            {
            	System.out.println(response.content);
                /*result = new INetwork[ response.content.length ];
                int i = 0;
                for ( String s : response.content )
                {
                    result[ i ] = new INetwork();
                    String networkURI = s.substring( s.indexOf( "network" ) + 8 );
                    result[ i ].getContent().put( INetwork.ID , networkURI       );
                    result[ i ].getContent().put( INetwork.URI, URI + networkURI );
                    
                    
                    i++;
                }
                OcciExecutorCompletionService engine = new OcciExecutorCompletionService();
                engine.fetchNetworkInformation( headers, result );*/
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static void main( String[] args )
    {
        /*try
        {
            String example = "TestNetwork(627ba25a-09e8-11e1-997c-00163e211147);MAC=MACx;IP=IPx;Gateway=GWx;Allocation=ALLOx;";
            System.out.println( attr( example, null ) );
            System.out.println( attr( example, "MAC" ) );
            if ( true ) return;            
            
            OcciOneManagerImpl occi = new OcciOneManagerImpl();
            ICompute[] computes = occi.getComputes( "http://129.217.211.147:3000" );
            if ( computes != null )
                for ( ICompute s : computes )
                {
                    System.out.println( s );
                }
            else
            {
                System.out.println( "nix" );
            }
            
            INetwork[] networks = occi.getNetworks( "http://129.217.211.147:3000" );
            if ( networks != null )
                for ( INetwork s : networks )
                {
                    System.out.println( s );
                }
            else
            {
                System.out.println( "nix" );
            }
            
            IStorage[] storages = occi.getStorages( "http://134.76.9.66:3000" );
            if ( storages != null )
                for ( IStorage s : storages )
                {
                    System.out.println( s );
                }
            else
            {
                System.out.println( "nix" );
            }
            
            
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }*/
    	
    	try
        {
    		OcciOneManagerImpl occi = new OcciOneManagerImpl();
    		
    		// creating compute
    		/*
            ICompute vm = new ICompute();
            vm.content.put( ICompute.URI         , HttpUtils.GWDGURI            );
            vm.content.put( ICompute.TITLE       , "MyVM_29.02.2012"         );
            vm.content.put( ICompute.SUMMARY     , "VM-LK" );
            vm.content.put( ICompute.ARCHITECTURE, "x64"          );
            vm.content.put( ICompute.CORES       , "1"            );
            vm.content.put( ICompute.MEMORY      , "1"           );
            vm.content.put(ICompute.NETWORK, "TestNetwork("+occi.getNetworkId("GWDG-Cloud (968)")+");MAC=MACx;IP=IPx;Gateway=GWx;Allocation=ALLOx;");
			//vm.content.put( ICompute.STORAGE   ,"OCCIStorage(29c8b9a6-a410-5b77-83d3-1bff60ec2c33);Mountpoint=/data/test2;"+HttpUtils.storageSeparator+"NFSStorage(885b1c90-3ade-11e1-8286-a4b197fffe50);Mountpoint=/data/test1;"+HttpUtils.storageSeparator+"NFSStorage(886e14c6-3ade-11e1-8286-a4b197fffe50);Mountpoint=/data/test3;");
			//vm.content.put( ICompute.STORAGE     , "OCCIStorage("+occi.getStorageId("OCCI Ubuntu 11.10 Server (x86_64) (09.01.2012)")+");Mountpoint=/data/test2;");
            //vm.content.put( ICompute.STORAGE   ,"NFSStorage(25363ae2-62e2-11e1-b639-003048c6acf2);Mountpoint=/data/test2;"+HttpUtils.storageSeparator+"NFSStorage(bd6a0d3e-62ce-11e1-b639-003048c6acf2);Mountpoint=/data/test1;"+HttpUtils.storageSeparator+"OCCIStorage("+occi.getStorageId("OCCI Ubuntu 11.10 Server (x86_64) (09.01.2012)")+");Mountpoint=/data/test3;");
            vm.content.put( ICompute.STORAGE   ,"NFSStorage(fe85a4de-62e4-11e1-82e4-003048c6acf2);Mountpoint=/data/test2;");
            System.out.print(occi.createCompute( vm ));
            */
    		
    		// displaying computes
    		/*ICompute [] computes = occi.getComputes(HttpUtils.GWDGURI);
    		for(ICompute compute : computes){
    				System.out.println(compute.getContent());
    		}*/
    		
    		// network transmission ---Ali 
    		//occi.getNetworkTransmission(HttpUtils.GWDGURI, "d57f19a0-61e8-11e1-91a3-003048c6acf2");
            
    		
    		// Storage query
            /*IStorage[] storages = occi.getStorages( HttpUtils.GWDGURI );
            if ( storages != null )
                for ( IStorage s : storages )
                {
                    System.out.println( s);
                }
            else
            {
                System.out.println( "nix" );
            }
            
            occi.getStorageId("OpenSUSE 11.4 Install (x86_64)");*/
    		
    		
    		// Creating Storage (NFS)
    		/*IStorage storage = new IStorage();
    		storage.content.put( IStorage.TITLE      ,  HttpUtils.NFSStorage );
            storage.content.put( IStorage.URI      ,  HttpUtils.GWDGURI );
            storage.content.put( IStorage.EXPORT    , "134.76.9.66:/srv/cloud/nfs-exports/test2");
            storage.content.put( IStorage.FILE_SIZE, "1024");
            
            System.out.println( "--  CREATING STORAGE ------------------" );
            String res = occi.createStorage( storage );
            System.out.println( "\t |- ID: '"+res+"'" );*/
            
            
    		// Display and delete NFSStorage
    		/*IStorage[] storageArray = occi.getNFSStorages(HttpUtils.GWDGURI);

    		for(IStorage s : storageArray ){
    	    System.out.println( "--  DELETING STORAGE ------------------" );
            String del = occi.deleteStorage( s, s.getContent().get(IStorage.TITLE) );
            System.out.println( "\t |- RES: '"+del );
    		}*/
    		
    		// Display NFSStorage
    		occi.getNFSStorages(HttpUtils.GWDGURI);
    		
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
	
    }
}
