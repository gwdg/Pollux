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
package cloud.cdmi.internal.extensions.cdmi.tester;

import java.util.Hashtable;

import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIContainer;
import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIOperation;
import cloud.cdmi.internal.extensions.cdmi.datatypes.CDMIResponse;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class CDMIContainerTester
{
    public static void testContainer()
    {
        try
        {
            //CDMIContainer container = new CDMIContainer( "http://129.217.252.37:2364" );
            //CDMIContainer container = new CDMIContainer( "http://129.217.145.206:2364" );
            //CDMIContainer container = new CDMIContainer( "https://207.61.255.243:18080/CDMI" );
            CDMIContainer container = new CDMIContainer( "http://localhost:2364" );
            //CDMIContainer container = new CDMIContainer( "http://snia:dc00112233@188.165.248.200:80" );
            Hashtable<String, String> headers = null;
            CDMIResponse read = null;
            
            System.out.println( "Checking ROOT READ..." );
            // -- EXAMPLE #0 -----------------------------------------------------------
            headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.READ );
            read    = container.read( "/", headers );
            System.out.println( "code:"   +read.code    );
            System.out.println( "headers:"+read.headers );
            System.out.println( "body:"   +read.body    );
            
            System.out.println( "Checking CREATE..." );
            // -- EXAMPLE #1 -----------------------------------------------------------
            headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.CREATE );
            CDMIResponse create = container.create( "/Duitama", headers, new Hashtable<String, String>() );
            System.out.println( "code:"   +create.code    );
            System.out.println( "headers:"+create.headers );
            System.out.println( "body:"   +create.body    );
            
            System.out.println( "Checking UPDATE..." );
            // -- EXAMPLE #2 -----------------------------------------------------------
            headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.UPDATE );
            Hashtable<String, String> body = new Hashtable<String, String>();
            body.put( "snapshot", "wubifans" );
            CDMIResponse update = container.update( "/Duitama", headers, body );
            System.out.println( "code:"   +update.code    );
            System.out.println( "headers:"+update.headers );
            System.out.println( "body:"   +update.body    );
            
            System.out.println( "Checking READ..." );
            // -- EXAMPLE #3 -----------------------------------------------------------
            headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.READ );
            read    = container.read( "/Duitama", headers );
            System.out.println( "code:"   +read.code    );
            System.out.println( "headers:"+read.headers );
            System.out.println( "body:"   +read.body    );
            
            // -- EXAMPLE #4 -----------------------------------------------------------
            System.out.println( "Checking READ + filters..." );
            String filter = "objectID;objectURI";
            headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.READ );
            read = container.read( "/Duitama", headers, filter );
            System.out.println( "code:"   +read.code    );
            System.out.println( "headers:"+read.headers );
            System.out.println( "body:"   +read.body    );

            // -- EXAMPLE #5 -----------------------------------------------------------
            System.out.println( "Checking DELETE..." );
            headers = CDMIContainer.Adapter.defaultHeaders( CDMIOperation.DELETE );
            read = container.delete( "/Duitama/", headers );
            System.out.println( "code:"   +read.code    );
            System.out.println( "headers:"+read.headers );
            System.out.println( "body:"   +read.body    );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public static void main( String[] args )
    {
        CDMIContainerTester.testContainer();
    }
}
