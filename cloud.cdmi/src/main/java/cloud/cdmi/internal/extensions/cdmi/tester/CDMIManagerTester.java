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

import java.util.Vector;

import cloud.cdmi.internal.CdmiManagerImpl;
import cloud.services.cdmi.SCDMIResponse;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class CDMIManagerTester
{
    public static void testContainer()
    {
        try
        {
            CdmiManagerImpl manager = new CdmiManagerImpl();
            // String __SERVER = "http://129.217.252.37:2364";
            String __SERVER = "http://localhost:2364";
            // --- Container Operations ---
            
            // @@ createNonDataObject
            SCDMIResponse create = manager.createNonDataObject( __SERVER, "/", "xxx.iso", "d:\\fromCDMI.pdf" );
            System.out.println( create.code    );
            System.out.println( create.headers );
            System.out.println( create.body    );
            
            // @@ public Vector<SCDMIResponse> containers( String server, String path )
            Vector<SCDMIResponse> containers = manager.containers( __SERVER, "/" );
            for ( SCDMIResponse it : containers )
            {
                System.out.println( "@CONTAINERS::"+it );
            }
            
            // @@public Vector<SCDMIResponse> filesFrom ( String server, String filter );
            Vector<SCDMIResponse> filesFrom = manager.filesFrom( __SERVER, "iso;zip;img" );
            for ( SCDMIResponse it : filesFrom )
            {
                System.out.println( "@FILES:"+it.content );
            }
            
            // @@public deleteNonDataObject
            SCDMIResponse deleted = manager.deleteNonDataObject( __SERVER, __SERVER+"/xxx.iso" );
            System.out.println( "deleted?"+deleted.code );
            
            String external = "http://129.217.252.37:2364/WubiClub/ttylinux.img";
            deleted = manager.deleteNonDataObject( __SERVER, external );
            System.out.println( "deleted?"+deleted.code );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public static void main( String[] args )
    {
        CDMIManagerTester.testContainer();
    }
}
