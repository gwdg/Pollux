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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class HttpUtils
{
    public HttpUtils()
    {
    }

    public static OcciResponse get( String url, Hashtable<String, String> headers )
    {
        OcciResponse result = OcciResponse.newInstance();
        
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet( url );
            
            for ( Enumeration<String> keys = headers.keys(); keys.hasMoreElements(); )
            {
                String k = keys.nextElement();
                request.addHeader( k, headers.get( k ) );
            }

            HttpResponse response = httpClient.execute( request );

            result.code = response.getStatusLine().getStatusCode(); 

            BufferedReader br = new BufferedReader( new InputStreamReader( (response.getEntity().getContent()) ) );

            String line;
            Vector<String> content = new Vector<String>( 3, 2 );
            while ( (line = br.readLine()) != null )
            {
                content.add( line );
            }
            result.content = content.toArray( new String[]{} );
            
            // get response-headers
            Header[] allHeaders = response.getAllHeaders();
            for ( Header h : allHeaders )
            {
                result.headers.put( h.getName(), h.getValue() );
            }
            
            httpClient.getConnectionManager().shutdown();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            result = null;
        }
        
        return result;
    }

    public static OcciResponse put( String url, Hashtable<String, String> headers, Hashtable<String, String> body )
    {
        OcciResponse result = OcciResponse.newInstance();
        
        // A PUT request is similar to a POST request except that it either
        // creates a new resource or most likely
        // replaces an existing resource with a new representation.

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPut put = new HttpPut( url );

            // ADD HEADERS
            for ( Enumeration<String> keys = headers.keys(); keys.hasMoreElements(); )
            {
                String k = keys.nextElement();
                put.addHeader( k, headers.get( k ) );
            }
            
            put.setEntity( new StringEntity( body.toString() ) );

            HttpResponse response = httpClient.execute( put );

            result.code = response.getStatusLine().getStatusCode(); 

            BufferedReader br = new BufferedReader( new InputStreamReader( (response.getEntity().getContent()) ) );

            String line;
            Vector<String> content = new Vector<String>( 3, 2 );
            while ( (line = br.readLine()) != null )
            {
                content.add( line );
            }
            result.content = content.toArray( new String[]{} );
            
            // get response-headers
            Header[] allHeaders = response.getAllHeaders();
            for ( Header h : allHeaders )
            {
                result.headers.put( h.getName(), h.getValue() );
            }
            
            httpClient.getConnectionManager().shutdown();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            result = null;
        }
        
        return result;
    }

    public static OcciResponse delete( String url )
    {
        OcciResponse result = OcciResponse.newInstance();

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            // create a HttpDelete object with the full URI
            HttpDelete delete = new HttpDelete( url );
            
            // execute the DELETE request
            HttpResponse resp = httpClient.execute( delete );
            // make sure the response is a 200
            result.code = resp.getStatusLine().getStatusCode(); 
            
            // EntityUtils.consume( resp.getEntity() );
            httpClient.getConnectionManager().shutdown();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public static OcciResponse post( String url, Hashtable<String, String> headers, Hashtable<String, String> body )
    {
        OcciResponse result = OcciResponse.newInstance();
        
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost( url );

            // ADD HEADERS
            for ( Enumeration<String> keys = headers.keys(); keys.hasMoreElements(); )
            {
                String k = keys.nextElement();
                post.addHeader( k, headers.get( k ) );
            }
            
            if ( body != null && body.size() != 0 )
                post.setEntity( new StringEntity( body.toString() ) );
            else
                post.setEntity( new StringEntity( "" ) );

            HttpResponse response = httpClient.execute( post );

            result.code = response.getStatusLine().getStatusCode(); 

            BufferedReader br = new BufferedReader( new InputStreamReader( (response.getEntity().getContent()) ) );

            String line;
            Vector<String> content = new Vector<String>( 3, 2 );
            while ( (line = br.readLine()) != null )
            {
                content.add( line );
            }
            result.content = content.toArray( new String[]{} );
            
            // get response-headers
            Header[] allHeaders = response.getAllHeaders();
            for ( Header h : allHeaders )
            {
                result.headers.put( h.getName(), h.getValue() );
            }
            
            httpClient.getConnectionManager().shutdown();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            result = null;
        }
        
        return result;
    }
    
    public static OcciResponse multipartPost( String url, Hashtable<String, String> headers, 
                                              Hashtable<String, String> params, File[] files )
    {
        OcciResponse result = OcciResponse.newInstance();
        
        try
        {
            PostMethod post = new PostMethod( url );
            Vector<Part> content = new Vector<Part>( 3, 2 );
            if ( params != null )
            {
                for ( String k : params.keySet() )
                {
                    String v = params.get( k );
                    content.add( new StringPart( k, v ) );
                }
            }
            for ( File f : files )
            {
                content.add( new FilePart( /*f.getName()*/ "file", f  ) );
            };
            Part[] parts = content.toArray( new Part[]{} );
            
            // ADD HEADERS
            for ( Enumeration<String> keys = headers.keys(); keys.hasMoreElements(); )
            {
                String k = keys.nextElement();
                post.setRequestHeader( k, headers.get( k ) );
            }
            
            MultipartRequestEntity multipart = new MultipartRequestEntity( parts, post.getParams() );
            post.setRequestHeader( "Content-Type", multipart.getContentType() );
            post.setRequestEntity( multipart );
            
            HttpClient client = new HttpClient();
            client.getParams().setParameter( "http.socket.timeout", new Integer( 15* 60* 1000 ) );
            result.code       = client.executeMethod( post );
            result.content    = new String[]{ post.getResponseBodyAsString() };
            
            // get response-headers
            org.apache.commons.httpclient.Header[] allHeaders = post.getResponseHeaders();
            for ( org.apache.commons.httpclient.Header h : allHeaders )
            {
                result.headers.put( h.getName(), h.getValue() );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            result = null;
        }
        
        return result;
    }
}
