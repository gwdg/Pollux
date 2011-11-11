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
package cloud.cdmi.internal.extensions.cdmi.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;

public class CDMIUtils
{
    public static String entityAsString( HttpEntity entity )
    {
        try
        {
            BufferedReader br = new BufferedReader( new InputStreamReader( entity.getContent() ) );
            String line = "";
            StringBuffer content = new StringBuffer();
            while ( (line = br.readLine()) != null )
            {
                content.append( line );
            }
            br.close();
            
            return content.toString();
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }
    
    public static void addHeaders( HttpRequestBase request, String[] keys, Hashtable<String, String> metadata )
    {
        for ( String key : keys )
        {
            String value = metadata.get( key );
            if ( value != null ) 
                request.addHeader( key, value );
        }
    }
    
    public static String encodeBase64( String filename )
    {
        return null;
    }
    
    public static String encodeUTF8( String filename )
    {
        String result = null;
        
        try
        {
            result = FileUtils.readFileToString( new File( filename ), "UTF-8" );
        }
        catch ( Exception e )
        {
        }
        
        return result;
    }
    
    public static String[] tokensFromArray( String content, String field, String filter )
    {
        try
        {
            JSONObject jsonResponse = JSONObject.fromObject( content );
            JSONArray jsonArray = jsonResponse.getJSONArray( field );
            
            Vector<String> filterSet = new Vector<String>( 3, 2 );
            if ( filter != null )
            {
                String[] items = filter.split( ";" );
                for ( String it : items )
                {
                    filterSet.add( it );
                }
            }
            
            Vector<String> result = new Vector<String>( 3, 2 );
            for ( int i = 0; i < jsonArray.size(); i++ )
            {
                String curr = (String)jsonArray.get( i );
                String ext = FilenameUtils.getExtension( curr );
                if ( filter == null || filterSet.contains( ext ) )
                    result.add( curr );
            }
            
            return result.toArray( new String[]{} );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static void main( String[] args )
    {
        String test = "{\"children\": [\"ttylinux.img\", \"bild.png\", \"graph.png\", \"mr.img\", \"wubi.img\", \"myfile.text\", \"other.text\", \"energy.jpg\"]}";
        for ( String s : tokensFromArray( test, "children", "img;jpg" ) )
        {
            System.out.println( s );
        }
    }
    

}
