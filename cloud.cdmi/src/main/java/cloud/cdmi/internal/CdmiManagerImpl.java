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
package cloud.cdmi.internal;

import java.util.Hashtable;
import java.util.Vector;

import org.springframework.stereotype.Component;

import cloud.cdmi.internal.extensions.cdmi.CDMIFactory;
import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIContainer;
import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIOperation;
import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIResponse;
import cloud.cdmi.internal.extensions.cdmi.datatypes.NonCDMIDataObject;
import cloud.cdmi.internal.extensions.cdmi.utils.CDMIUtils;
import cloud.services.cdmi.ICdmiManager;
import cloud.services.cdmi.SCDMIResponse;

/**
 * This class represents the main Spring Java Bean of the CDMI-Client.  
 * This JavaBean provides utilities for creating, deleting and handling
 * of cdmi resources (Containers and VM images).  
 * 
 * CdmiManagerImpl is in charge of making http-calls to services 
 * exposed by a CDMI-server. The results of those calls will be
 * normally returned back into a SCDMIResponse. 
 * 
 * Each operation (create, delete) associated to containers and vm images 
 * is supported by simple strings specifying the attributes of the
 * respective resource (Container and VM image respectively).  
 * 
 * Note that this JavaBean and helper classes are fully independent of 
 * any WebController.
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
@Component( "cdmi" )
public class CdmiManagerImpl implements ICdmiManager 
{
    // --- Container Operations --- 
    @Override
    public Vector<SCDMIResponse> containers( String server, String path )
    {
        Vector<SCDMIResponse> result = new Vector<SCDMIResponse>( 3, 2 );
        
        try
        {
            CDMIContainer container = new CDMIContainer( server );
            Hashtable<String, String> headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.READ );
            CDMIResponse read = container.read( path, headers, null /* not filter */ );
            if ( read.content == null ) return null;
            String[] children = CDMIUtils.tokensFromArray( read.content, "children", null );
            if ( children != null && children.length != 0 )
            {
                if ( !path.endsWith( "/" ) ) path += "/";
                
                String filter = "objectID;objectURI";
                for ( String ch : children )
                {
                    if ( ch.endsWith( "/" ) ) ch = ch.substring( 0, ch.length()-1 );
                    else continue;  // it is not a directory
                    CDMIResponse chInfo = container.read( path + ch, headers, filter );
                    SCDMIResponse item = new SCDMIResponse(); 
                    item.code    = chInfo.code;
                    item.body    = chInfo.body;
                    item.content = String.format( "objectURI:%s (%s)", 
                                                  chInfo.body.get( "objectURI" ),
                                                  chInfo.body.get( "objectID" )
                                                  );
                    result.add( item );
                }
            }
            else
            {
                // Empty element:  NO CONTENT
                SCDMIResponse error = new SCDMIResponse();
                error.code    = -1;
                error.content = "Container empty or not found";
                result.add( error );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            // Empty element:  INTERNAL ERROR or SERVER OFF-LINE
            SCDMIResponse error = new SCDMIResponse();
            error.code    = -1;
            error.content = "INTERNAL ERROR or SERVER OFF-LINE";
            result.add( error );
        }
        
        return result;
    }

    @Override
    public SCDMIResponse createContainer( String server, String path )
    {
        SCDMIResponse result = new SCDMIResponse();
        
        try
        {
            CDMIContainer container = new CDMIContainer( server );
            Hashtable<String, String> headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.CREATE );
            CDMIResponse read = container.create( path, headers, new Hashtable<String, String>() /* not content */ );
            SCDMIResponse item = new SCDMIResponse(); 
            item.code    = read.code;
            item.body    = read.body;
            item.content = read.content;
            result = item;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            // Empty element:  INTERNAL ERROR or SERVER OFF-LINE
            SCDMIResponse error = new SCDMIResponse();
            error.code    = -1;
            error.content = "INTERNAL ERROR or SERVER OFF-LINE";
            result = error;
        }
        
        return result;
    }

    @Override
    public SCDMIResponse deleteContainer( String server, String path )
    {
        SCDMIResponse result = new SCDMIResponse();
        
        try
        {
            CDMIContainer container = new CDMIContainer( server );
            Hashtable<String, String> headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.DELETE );
            CDMIResponse delete = container.delete( path, headers, new Hashtable<String, String>() /* not content */ );
            SCDMIResponse item = new SCDMIResponse(); 
            item.code    = delete.code;
            item.body    = delete.body;
            item.content = delete.content;
            result = item;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            // Empty element:  INTERNAL ERROR or SERVER OFF-LINE
            SCDMIResponse error = new SCDMIResponse();
            error.code    = -1;
            error.content = "INTERNAL ERROR or SERVER OFF-LINE";
            result = error;
        }
        
        return result;
    }

    // --- Non-DataObject Operations --- 
    @Override
    public Vector<SCDMIResponse> filesFrom( String server, String filter )
    {
        Vector<SCDMIResponse> result = new Vector<SCDMIResponse>();
        try
        {
            Vector<SCDMIResponse> containers = containers( server, "/" );
            for ( SCDMIResponse it : containers )
            {
                String subContainer = it.body.get( "objectURI" );
                String[] filesOfContainer = CDMIFactory.filesOfContainer( server, subContainer, filter );
                if ( filesOfContainer != null && filesOfContainer.length > 0 )
                {
                    for ( String f : filesOfContainer )
                    {
                        SCDMIResponse resp = new SCDMIResponse();
                        resp.content = server + subContainer+"/"+f;
                        result.add( resp );
                    }
                }
            }
            
            // files located on the "/"
            String[] filesOfContainer = CDMIFactory.filesOfContainer( server, "/", filter );
            if ( filesOfContainer != null && filesOfContainer.length > 0 )
            {
                for ( String f : filesOfContainer )
                {
                    SCDMIResponse resp = new SCDMIResponse();
                    resp.content = server + "/"+f;
                    result.add( resp );
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            // Empty element:  INTERNAL ERROR or SERVER OFF-LINE
            SCDMIResponse error = new SCDMIResponse();
            error.code    = -1;
            error.content = "INTERNAL ERROR or SERVER OFF-LINE";
            result.add( error );
        }
        
        return result;
    }
    
    @Override
    public SCDMIResponse createNonDataObject( String server, String container, String cdmiObjectName, String filename )
    {
        SCDMIResponse result = new SCDMIResponse();
        
        try
        {
            NonCDMIDataObject dtObj = new NonCDMIDataObject( server );
            CDMIResponse read = dtObj.create( container, cdmiObjectName, filename, "application/binary" );
            SCDMIResponse item = new SCDMIResponse(); 
            item.code    = read.code;
            item.body    = read.body;
            item.content = read.content;
            result = item;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            // Empty element:  INTERNAL ERROR or SERVER OFF-LINE
            SCDMIResponse error = new SCDMIResponse();
            error.code    = -1;
            error.content = "INTERNAL ERROR or SERVER OFF-LINE";
            result = error;
        }
        
        return result;
    }
    
    @Override
    public SCDMIResponse deleteNonDataObject( String server, String cdmiObjectName )
    {
        SCDMIResponse result = new SCDMIResponse();
        
        try
        {
            NonCDMIDataObject dtObj = new NonCDMIDataObject( server );
            CDMIResponse delete = dtObj.delete( cdmiObjectName );
            SCDMIResponse item = new SCDMIResponse(); 
            item.code    = delete.code;
            item.body    = delete.body;
            item.content = delete.content;
            result = item;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            // Empty element:  INTERNAL ERROR or SERVER OFF-LINE
            SCDMIResponse error = new SCDMIResponse();
            error.code    = -1;
            error.content = "INTERNAL ERROR or SERVER OFF-LINE";
            result = error;
        }
        
        return result;
    }
}
