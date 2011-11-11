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
package cloud.cdmi.internal.extensions.cdmi;

import java.util.Hashtable;

import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIContainer;
import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIDataObject;
import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIOperation;
import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIResponse;
import cloud.cdmi.internal.extensions.cdmi.utils.CDMIUtils;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class CDMIFactory
{
    public static String[] filesOfContainer( String cdmiServer, String containerName, String filter )
    {
        try
        {
            CDMIContainer container = new CDMIContainer( cdmiServer );
            Hashtable<String, String> headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.READ );
            String filterField = "children";

            CDMIResponse response = container.read( containerName, headers, filterField );
            if ( response.code != 404 ) // no error
                return CDMIUtils.tokensFromArray( response.content, filterField, filter );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static String[] getFile( String cdmiServer, String containerName, String fileName )
    {
        try
        {
            String filter = "objectID;objectURI";
            CDMIDataObject data = new CDMIDataObject( cdmiServer );
            CDMIResponse md = data.read( containerName, fileName, CDMIDataObject.Adapter.defaultHeaders( CDMIOperation.READ ), filter );
            
            if ( md != null )
            {
                String id  = md.body.get( "objectID"  );
                String uri = md.body.get( "objectURI" );
                
                String[] result = { id, uri };
                
                return result;
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static String[] cloneFile( String cdmiServer, String containerName, String fileName )
    {
        try
        {
            String filter = "objectID;objectURI";
            CDMIDataObject data = new CDMIDataObject( cdmiServer );
            CDMIResponse md = data.read( containerName, fileName, CDMIDataObject.Adapter.defaultHeaders( CDMIOperation.READ ), filter );
            
            if ( md != null )
            {
                String id  = md.body.get( "objectID"  );
                String uri = md.body.get( "objectURI" );
                
                // FIXME:  send cdmi-command, so the file ('filename') can be cloned.
                // TODO:  return new id and uri
                Hashtable<String, String> body = new Hashtable<String, String>();
                body.put( "copy", uri );
                CDMIResponse clone = data.create( containerName, fileName, CDMIDataObject.Adapter.defaultHeaders( CDMIOperation.CREATE ), body );
                
                id  = clone.body.get( "objectID"  );
                uri = clone.body.get( "objectURI" );
                
                String[] result = { id, uri };
                
                return result;
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return null;
    }
}
