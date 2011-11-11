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
package cloud.cdmi.internal.extensions.cdmi.restful;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIResponse;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class RestfulUtils
{
    public RestfulUtils()
    {
    }

    public static CDMIResponse get( String url, Hashtable<String, String> headers )
    {
        CDMIResponse result = CDMIResponse.newInstance();

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

            StringBuffer content = new StringBuffer();
            String line;
            while ( (line = br.readLine()) != null )
            {
                content.append( line );
            }
            result.content = content.toString();

            // get response-headers
            Header[] allHeaders = response.getAllHeaders();
            for ( Header h : allHeaders )
            {
                result.headers.put( h.getName(), h.getValue() );
            }

            // use JSON
            try
            {
                JSONObject jsonResponse = JSONObject.fromObject( result.content );

                for ( Iterator<?> keys = jsonResponse.keys(); keys.hasNext(); )
                {
                    String k = (String)keys.next();
                    // direct in response.body
                    result.body.put( k, jsonResponse.getString( k ) );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
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

    public static CDMIResponse put( String url, Hashtable<String, String> headers, Hashtable<String, String> body )
    {
        CDMIResponse result = CDMIResponse.newInstance();

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

            // ADD BODY
            JSONObject jsonBody = new JSONObject();
            for ( Enumeration<String> keys = body.keys(); keys.hasMoreElements(); )
            {
                String k = keys.nextElement();
                jsonBody.put( k, body.get( k ) );
            }
            put.setEntity( new StringEntity( jsonBody.toString() ) );

            HttpResponse response = httpClient.execute( put );

            result.code = response.getStatusLine().getStatusCode();

            BufferedReader br = new BufferedReader( new InputStreamReader( (response.getEntity().getContent()) ) );

            StringBuffer content = new StringBuffer();
            String line;
            while ( (line = br.readLine()) != null )
            {
                content.append( line );
            }
            result.content = content.toString();

            // get response-headers
            Header[] allHeaders = response.getAllHeaders();
            for ( Header h : allHeaders )
            {
                result.headers.put( h.getName(), h.getValue() );
            }

            // use JSON
            try
            {
                JSONObject jsonResponse = JSONObject.fromObject( result.content );

                for ( Iterator<?> keys = jsonResponse.keys(); keys.hasNext(); )
                {
                    String k = (String)keys.next();
                    // direct in response.body
                    result.body.put( k, jsonResponse.getString( k ) );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
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

    public static CDMIResponse delete( String url, Hashtable<String, String> headers )
    {
        CDMIResponse result = CDMIResponse.newInstance();

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            // create a HttpDelete object with the full URI
            HttpDelete delete = new HttpDelete( url );

            // ADD HEADERS
            for ( Enumeration<String> keys = headers.keys(); keys.hasMoreElements(); )
            {
                String k = keys.nextElement();
                delete.addHeader( k, headers.get( k ) );
            }

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

    public static CDMIResponse post( String url, Hashtable<String, String> headers, Hashtable<String, String> body )
    {
        CDMIResponse result = CDMIResponse.newInstance();

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

            // ADD BODY
            JSONObject jsonBody = new JSONObject();
            for ( Enumeration<String> keys = body.keys(); keys.hasMoreElements(); )
            {
                String k = keys.nextElement();
                jsonBody.put( k, body.get( k ) );
            }
            post.setEntity( new StringEntity( jsonBody.toString() ) );

            HttpResponse response = httpClient.execute( post );

            result.code = response.getStatusLine().getStatusCode();

            BufferedReader br = new BufferedReader( new InputStreamReader( (response.getEntity().getContent()) ) );

            StringBuffer content = new StringBuffer();
            String line;
            while ( (line = br.readLine()) != null )
            {
                content.append( line );
            }
            result.content = content.toString();

            // get response-headers
            Header[] allHeaders = response.getAllHeaders();
            for ( Header h : allHeaders )
            {
                result.headers.put( h.getName(), h.getValue() );
            }

            // use JSON
            try
            {
                JSONObject jsonResponse = JSONObject.fromObject( result.content );

                for ( Iterator<?> keys = jsonResponse.keys(); keys.hasNext(); )
                {
                    String k = (String)keys.next();

                    // direct in response.body
                    result.body.put( k, jsonResponse.getString( k ) );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
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

    public static CDMIResponse xput( String url, Hashtable<String, String> headers, File body )
    {
        CDMIResponse result = CDMIResponse.newInstance();

        // A PUT request is similar to a POST request except that it either
        // creates a new resource or most likely
        // replaces an existing resource with a new representation.

        try
        {
            PutMethod put = new PutMethod( url );

            // ADD HEADERS
            for ( Enumeration<String> keys = headers.keys(); keys.hasMoreElements(); )
            {
                String k = keys.nextElement();
                put.setRequestHeader( k, headers.get( k ) );
            }

            FileInputStream fis = new FileInputStream( body );
            InputStreamRequestEntity is = new InputStreamRequestEntity( fis );
            put.setRequestEntity( is );

            HttpClient client = new HttpClient();
            result.code = client.executeMethod( put );
            result.content = put.getResponseBodyAsString();

            // get response-headers
            org.apache.commons.httpclient.Header[] allHeaders = put.getResponseHeaders();
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

    public static CDMIResponse xget( String url, Hashtable<String, String> headers )
    {
        CDMIResponse result = CDMIResponse.newInstance();

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

            File file = new File( headers.get( "target" ) );
            InputStream content = response.getEntity().getContent();
            IOUtils.copy( content, new FileOutputStream( file ) );
            result.content = file.getName();

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

    public static class CustomSSLSocketFactory extends SSLSocketFactory
    {

        SSLContext sslContext = SSLContext.getInstance( "TLS" );

        public CustomSSLSocketFactory( KeyStore truststore ) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
        {
            super( truststore );

            TrustManager tm = new X509TrustManager()
            {
                @Override
                public void checkClientTrusted( java.security.cert.X509Certificate[] chain, String authType ) throws java.security.cert.CertificateException
                {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void checkServerTrusted( java.security.cert.X509Certificate[] chain, String authType ) throws java.security.cert.CertificateException
                {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };

            sslContext.init( null, new TrustManager[] { tm }, null );
        }

        @Override
        public Socket createSocket( Socket socket, String host, int port, boolean autoClose ) throws IOException, UnknownHostException
        {
            return sslContext.getSocketFactory().createSocket( socket, host, port, autoClose );
        }

        @Override
        public Socket createSocket() throws IOException
        {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
