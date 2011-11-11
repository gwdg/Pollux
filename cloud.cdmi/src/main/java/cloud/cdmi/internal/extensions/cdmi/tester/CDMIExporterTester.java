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
package cloud.cdmi.internal.extensions.cdmi.tester;

import java.io.File;
import java.util.Hashtable;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIContainer;
import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIOperation;
import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIResponse;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class CDMIExporterTester
{
    public static void testCapabilities()
    {
        try
        {
            System.out.println( "Checking readCapabilities..." );
            String result = FileUtils.readFileToString( new File( "d:\\tmp\\capabilities.txt" ) );
            System.out.println( result );
            
            try
            {
                JSONObject jsonResponse = JSONObject.fromObject( result );
                System.out.println( "NFS ? "+jsonResponse.getJSONObject( "capabilities" )
                                                                       .getString( "cdmi_export_nfs" ) );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public static void testMetadata()
    {
        try
        {
            System.out.println( "Checking readCapabilities..." );
            String result = FileUtils.readFileToString( new File( "d:\\tmp\\metadata.txt" ) );
            System.out.println( result );
            
            try
            {
                JSONObject jsonResponse = JSONObject.fromObject( result );
                System.out.println( "NFS ? "+jsonResponse.getJSONObject( "metadata" )
                                    .getString( "cdmi_nfs_export" ) );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    
    public static void testExport()
    {
        try
        {
            System.out.println( "Checking export..." );
            String result = FileUtils.readFileToString( new File( "d:\\tmp\\export.txt" ) );
            System.out.println( result );
            
            try
            {
                JSONObject jsonResponse = JSONObject.fromObject( result );
                System.out.println( "NFS path ? "+jsonResponse.getJSONObject( "exports" )
                                                                              .getJSONObject( "nfs" )
                                                                                              .getString( "exportpath" )
                                  );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public static void testCreateImage()
    {
        try
        {
            CDMIContainer container = new CDMIContainer( "http://localhost:2364" );
            
            Hashtable<String, String> headers = null;
            CDMIResponse read = null;
            
            /*
            System.out.println( "Checking CREATE..." );
            // -- EXAMPLE #1 -----------------------------------------------------------
            headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.CREATE );
            CDMIResponse create = container.create( "/Repository", headers, new Hashtable<String, String>() );
            System.out.println( "code:"   +create.code    );
            System.out.println( "headers:"+create.headers );
            System.out.println( "body:"   +create.body    );
            */
            System.out.println( "Checking UPDATE..." );
            // -- EXAMPLE #2 -----------------------------------------------------------
            headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.UPDATE );
            String exports = FileUtils.readFileToString( new File( "d:\\tmp\\export.txt" ) );

            Hashtable<String, String> body = new Hashtable<String, String>();
            body.put( "exports", exports );
            CDMIResponse update = container.update( "/Repository", headers, body );
            System.out.println( "code:"   +update.code    );
            System.out.println( "headers:"+update.headers );
            System.out.println( "body:"   +update.body    );
            
            System.out.println( "Checking READ..." );
            // -- EXAMPLE #3 -----------------------------------------------------------
            headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.READ );
            read    = container.read( "/Repository", headers );
            System.out.println( "code:"   +read.code    );
            System.out.println( "headers:"+read.headers );
            System.out.println( "body:"   +read.body    );
            
            // -- EXAMPLE #5 -----------------------------------------------------------
            /*
            System.out.println( "Checking DELETE..." );
            headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.DELETE );
            read = container.delete( "/Repository/", headers );
            System.out.println( "code:"   +read.code    );
            System.out.println( "headers:"+read.headers );
            System.out.println( "body:"   +read.body    );
            */
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public static void main( String[] args )
    {
        // CDMIExporterTester.testCapabilities();
        // CDMIExporterTester.testMetadata();
        //CDMIExporterTester.testExport();
        CDMIExporterTester.testCreateImage();
    }
}
