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
package cloud.cdmi.internal.extensions.cdmi.datatypes;

import java.util.Arrays;
import java.util.Hashtable;

import cloud.cdmi.internal.extensions.cdmi.restful.RestfulUtils;
import cloud.cdmi.internal.extensions.cdmi.validators.CDMIOperationValidator;
import cloud.cdmi.internal.extensions.cdmi.validators.CDMIOperationValidator.CDMIOperationException;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class CDMIContainer implements CDMIObject, CDMICRUDInterface
{
    public CDMIContainer( String cdmiEPR )
    {
        this.cdmiEPR = cdmiEPR;
    }
    
    @Override
    public CDMIResponse create( Object... params ) throws Exception
    {
        if ( params == null || params.length <= 2 )
        {
            throw new CDMIOperationException( String.format( "Missing mandatory parameters '%s'", Arrays.toString( params ) ) );
        }
        
        String   containerName            = "";
        Hashtable<String, String> headers = null;
        Hashtable<String, String> body    = null;
        
        if ( params.length == 3 )
        {
            containerName = params[ 0 ].toString();
            headers       = (Hashtable<String, String>)params[ 1 ];
            body          = (Hashtable<String, String>)params[ 2 ];
        }
        
        validate( CDMIOperation.CREATE, headers, body );
        String URL = cdmiEPR + containerName;
        
        return RestfulUtils.put( URL, headers, body );
    }

    @Override
    // @Params ( String:containerName, Hashtable:headers, String[]:filter = null )
    public CDMIResponse read( Object... params ) throws Exception
    {
        if ( params == null || params.length <= 1 )
        {
            throw new CDMIOperationException( String.format( "Missing mandatory parameters '%s'", Arrays.toString( params ) ) );
        }
        
        String   containerName            = "";
        String   filter                   = null;
        Hashtable<String, String> headers = null;
        
        if ( params.length == 2 )
        {
            containerName = params[ 0 ].toString();
            headers       = (Hashtable<String, String>)params[ 1 ];
        }
        else if ( params.length == 3 )
        {
            containerName = params[ 0 ].toString();
            headers       = (Hashtable<String, String>)params[ 1 ];
            filter        = (String)params[ 2 ];
        }
        
        validate( CDMIOperation.READ, headers, null );
        String URL = cdmiEPR + containerName;
        if ( filter != null )
            URL += "?" + filter;
        
        return RestfulUtils.get( URL, headers );
    }

    @Override
    public CDMIResponse update( Object... params ) throws Exception
    {
        if ( params == null || params.length <= 2 )
        {
            throw new CDMIOperationException( String.format( "Missing mandatory parameters '%s'", Arrays.toString( params ) ) );
        }
        
        String   containerName            = "";
        Hashtable<String, String> headers = null;
        Hashtable<String, String> body    = null;
        
        if ( params.length == 3 )
        {
            containerName = params[ 0 ].toString();
            headers       = (Hashtable<String, String>)params[ 1 ];
            body          = (Hashtable<String, String>)params[ 2 ];
        }
        
        validate( CDMIOperation.UPDATE, headers, body );
        String URL = cdmiEPR + containerName;
        
        return RestfulUtils.put( URL, headers, body );
    }

    @Override
    public CDMIResponse delete( Object... params ) throws Exception
    {
        if ( params == null || params.length <= 1 )
        {
            throw new CDMIOperationException( String.format( "Missing mandatory parameters '%s'", Arrays.toString( params ) ) );
        }
        
        String containerName = "";
        Hashtable<String, String> headers = null;
        
        if ( params.length == 2 )
        {
            containerName = params[ 0 ].toString();
            headers       = (Hashtable<String, String>)params[ 1 ];
        }
        
        String URL = cdmiEPR + containerName;
        
        return RestfulUtils.delete( URL, headers );
    }
    
    public void echo()
    {
    }
    
    protected void validate( CDMIOperation op, Hashtable<String, String> headers, Hashtable<String, String> body ) throws CDMIOperationException
    {
        String[] headersM = requestHeaders.mandatory( op );
        String[] bodyM    = requestBody   .mandatory( op );
        
        CDMIOperationValidator.validate( headersM, headers );            
        CDMIOperationValidator.validate( bodyM   , body    );            
    }
    
    public RequestHeaders  requestHeaders (){ return requestHeaders ; } 
    public RequestBody     requestBody    (){ return requestBody    ; }
    public ResponseHeaders responseHeaders(){ return responseHeaders; } 
    public ResponseBody    responseBody   (){ return responseBody   ; } 
    
    protected String cdmiEPR;

    //---------------------------------------------------------------------------
    //  HELPER CLASSES
    //---------------------------------------------------------------------------
    
    public static class Adapter
    {
        public static Hashtable<String, String> defaultHeaders( CDMIOperation op )
        {
            Hashtable<String, String> result = new Hashtable<String, String>();
            
            switch ( op )
            {
                case CREATE:
                break;
                case READ:
                break;
                case UPDATE:
                break;
                case DELETE:
                break;
            }
            
            result.put( "Accept"                      , "application/cdmi-container" );
            result.put( "Content-Type"                , "application/cdmi-container" );
            result.put( "X-CDMI-Specification-Version", "1.0.1" );
            
            return result;
        }
    }
    
    public static class ContainerRequestHeaders implements RequestHeaders
    {
        public String[] optional ( CDMIOperation op )
        { 
            String result[] = null;
            
            switch ( op )
            {
                case CREATE:
                    result = new String[] 
                    {
                        "X-CDMI-NoClobber"
                    };
                break;
                case READ:
                    // not required
                break;
                case UPDATE:
                    result = new String[] 
                    {
                        "X-CDMI-MustExist"
                    };
                break;
                case DELETE:
                    // not required
                break;
            }
            
            return result; 
        }
        
        public String[] mandatory( CDMIOperation op )
        { 
            String result[] = null;
            
            switch ( op )
            {
                case CREATE:
                    result = new String[] 
                    {
                        "Accept", 
                        "Content-Type", 
                        "X-CDMI-Specification-Version"
                    };
                break;
                case READ:
                    result = new String[] 
                    {
                        "Accept", 
                        "Content-Type", 
                        "X-CDMI-Specification-Version"
                    };
                break;
                case UPDATE:
                    result = new String[] 
                    {
                        "Accept", 
                        "Content-Type", 
                        "X-CDMI-Specification-Version"
                    };
                break;
                case DELETE:
                    result = new String[] 
                    {
                        "Accept", 
                        "Content-Type", 
                        "X-CDMI-Specification-Version"
                    };
                break;
            }
            return result; 
        }
    }
    
    public static class ContainerRequestBody implements RequestBody
    {
        public String[] optional ( CDMIOperation op )
        { 
            String result[] = null;
            
            switch ( op )
            {
                case CREATE:
                    result = new String[]
                    {
                        "metadata", 
                        "domainURI", 
                        "exports",
                        "deserialize",
                        "copy",
                        "move",
                        "reference"
                    };
                break;
                case READ:
                    // not required
                break;
                case UPDATE:
                    result = new String[]
                    {
                        "metadata", 
                        "domainURI", 
                        "snapshot",
                        "export"
                    };
                break;
                case DELETE:
                    // not required
                break;
            }
            
            return result; 
        }
        
        public String[] mandatory( CDMIOperation op )
        { 
            String result[] = null;
            
            switch ( op )
            {
                case CREATE:
                    // not required
                break;
                case READ:
                    // not required
                break;
                case UPDATE:
                    // not required
                break;
                case DELETE:
                    // not required
                break;
            }
            
            return result; 
        }
    }
    
    public static class ContainerResponseHeaders implements ResponseHeaders
    {
        public String[] optional ( CDMIOperation op )
        { 
            String result[] = null;
            
            switch ( op )
            {
                case CREATE:
                    // not required
                break;
                case READ:
                    // not required
                break;
                case UPDATE:
                    // not required
                break;
                case DELETE:
                    // not required
                break;
            }
            
            return result; 
        }
        
        public String[] mandatory( CDMIOperation op )
        { 
            String result[] = null;
            
            switch ( op )
            {
                case CREATE:
                    result = new String[]
                    {
                        "Content-Type", 
                        "X-CDMI-Specification-Version" 
                    };
                break;
                case READ:
                    result = new String[]
                    {
                        "Content-Type", 
                        "Location" 
                    };
                break;
                case UPDATE:
                    result = new String[] 
                    {
                        "Location"
                    };
                break;
                case DELETE:
                    // not required
                break;
            }
            
            return result; 
        }
    }
    
    public static class ContainerResponseBody implements ResponseBody
    {
        public String[] optional ( CDMIOperation op )
        { 
            String result[] = null;
            
            switch ( op )
            {
                case CREATE:
                    result = new String[]
                    {
                        "percentComplete", 
                        "exports",
                        "snapshots"
                    };
                break;
                case READ:
                    // value of this container
                break;
                case UPDATE:
                    // value of this container
                break;
                case DELETE:
                    // not required
                break;
            }
            return result; 
        }
        
        public String[] mandatory( CDMIOperation op )
        { 
            String result[] = null;
            
            switch ( op )
            {
                case CREATE:
                    result = new String[]
                    {
                        "objectURI", 
                        "objectID", 
                        "parentURI", 
                        "domainURI", 
                        "capabilitiesURI", 
                        "completionStatus", 
                        "metadata",
                        "childrenrange",
                        "children" 
                    };
                break;
                case READ:
                    // not required
                break;
                case UPDATE:
                    // not required
                break;
                case DELETE:
                    // not required
                break;
            }
            
            return result; 
        }
    }
    
    public interface CDMIContentType {

        public static final String CDMI_CAPABILITY = "application/cdmi-capabilities";
        public static final String CDMI_CONTAINER = "application/cdmi-container";
        public static final String CDMI_DOMAIN = "application/cdmi-domain";
        public static final String CDMI_OBJECT = "application/cdmi-object";
        public static final String CDMI_QUEUE = "application/cdmi-queue";
        public static final String CDMI_SPEC_VERSION = "1.0.1";
    }    
    
    protected static final ContainerRequestHeaders  requestHeaders  = new ContainerRequestHeaders ();
    protected static final ContainerRequestBody     requestBody     = new ContainerRequestBody    ();
    protected static final ContainerResponseHeaders responseHeaders = new ContainerResponseHeaders();
    protected static final ContainerResponseBody    responseBody    = new ContainerResponseBody   ();
}
