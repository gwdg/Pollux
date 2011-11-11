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
package cloud.cdmi.internal.extensions.cdmi.datatypes;

import java.io.File;
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
public class NonCDMIDataObject implements CDMIObject, CDMICRUDInterface
{
    public NonCDMIDataObject( String cdmiEPR )
    {
        this.cdmiEPR = cdmiEPR;
    }
    
    @Override
    public CDMIResponse create( Object... params ) throws Exception
    {
        if ( params == null || params.length < 3 )
        {
            throw new CDMIOperationException( String.format( "Missing mandatory parameters '%s'", Arrays.toString( params ) ) );
        }
        
        String   containerName  = "";
        String   dataObjectName = "";
        String   fileName       = "";
        String   mimetype       = null;
        
        if ( params.length == 3 )
        {
            containerName  = params[ 0 ].toString();
            dataObjectName = params[ 1 ].toString();
            fileName       = params[ 2 ].toString();
            mimetype       = "text/plain";
        }
        else
        {
            containerName  = params[ 0 ].toString();
            dataObjectName = params[ 1 ].toString();
            fileName       = params[ 2 ].toString();
            mimetype       = params[ 3 ].toString();
        }
        
        String URL = cdmiEPR + containerName + "/" + dataObjectName;
        Hashtable<String, String> headers = new Hashtable<String, String>();
        headers.put( "Content-Type", mimetype );
        
        return RestfulUtils.xput( URL, headers, new File( fileName ) );
    }

    /* (non-Javadoc)
     * @see de.udo.one.extensions.cdmi.datatypes.CDMICRUDInterface#read(java.lang.Object[])
     */
    @Override
    // @Params ( String:containerName, String:dataObjectName, String target, String[]:filter = null )
    public CDMIResponse read( Object... params ) throws Exception
    {
        if ( params == null || params.length < 3 )
        {
            throw new CDMIOperationException( String.format( "Missing mandatory parameters '%s'", Arrays.toString( params ) ) );
        }
        
        String   containerName            = "";
        String   dataObjectName           = "";
        String   target                   = "";
        String   filter                   = null;
        Hashtable<String, String> headers = new Hashtable<String, String>();
        
        if ( params.length == 3 )
        {
            containerName  = params[ 0 ].toString();
            dataObjectName = params[ 1 ].toString();
            target         = params[ 2 ].toString();
        }
        else if ( params.length == 4 )
        {
            containerName  = params[ 0 ].toString();
            dataObjectName = params[ 1 ].toString();
            target         = params[ 2 ].toString();
            filter         = (String)params[ 3 ];
        }
        
        String URL = cdmiEPR + containerName + "/" + dataObjectName;
        if ( filter != null )
            URL += "?" + filter;
        headers.put( "target", target );
        
        return RestfulUtils.xget( URL, headers );
    }

    @Override
    public CDMIResponse update( Object... params ) throws Exception
    {
        return create( params );
    }

    @Override
    public CDMIResponse delete( Object... params ) throws Exception
    {
        if ( params == null || params.length < 1 || !(params[ 0 ] instanceof String) )
        {
            throw new CDMIOperationException( String.format( "Missing mandatory parameters '%s'", Arrays.toString( params ) ) );
        }
        
        Hashtable<String, String> headers = new Hashtable<String, String>();
        return RestfulUtils.delete( (String)params[ 0 ], headers );
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
            
            result.put( "Accept"                      , "application/vnd.org.snia.cdmi.dataobject+json" );
            result.put( "Content-Type"                , "application/vnd.org.snia.cdmi.dataobject+json" );
            result.put( "X-CDMI-Specification-Version", "1.0" );
            
            return result;
        }
    }
    
    public static class DataObjectRequestHeaders implements RequestHeaders
    {
        public String[] optional ( CDMIOperation op )
        { 
            String result[] = null;
            
            switch ( op )
            {
                case CREATE:
                    result = new String[] 
                    {
                        "X-CDMI-Partial"
                    };
                break;
                case READ:
                    result = new String[] 
                    {
                        "Range"
                    };
                break;
                case UPDATE:
                    result = new String[] 
                    {
                        "X-CDMI-Partial"
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
    
    public static class DataObjectRequestBody implements RequestBody
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
    
    public static class DataObjectResponseHeaders implements ResponseHeaders
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
                    // not required
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
    
    public static class DataObjectResponseBody implements ResponseBody
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
    
    protected static final DataObjectRequestHeaders  requestHeaders  = new DataObjectRequestHeaders ();
    protected static final DataObjectRequestBody     requestBody     = new DataObjectRequestBody    ();
    protected static final DataObjectResponseHeaders responseHeaders = new DataObjectResponseHeaders();
    protected static final DataObjectResponseBody    responseBody    = new DataObjectResponseBody   ();
}
