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

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class CDMITester
{
    public static void testContainer()
    {
        try
        {
            CDMIContainer container = new CDMIContainer( "http://129.217.252.37:2364/" );
            
            // -- EXAMPLE #1 -----------------------------------------------------------
            container.create( "/slasoi" );
            container.read  ( "/slasoi" );
            container.echo  ();
            
            Hashtable<String, String> newMetadata = new Hashtable<String, String>();
            container.update( "/slasoi", newMetadata );
            container.echo  ();
            
            container.delete( "/slasoi" );
            Thread.sleep( 1000 );
            
            // -- EXAMPLE #2 -----------------------------------------------------------
            Hashtable<String, String> initialMetadata = new Hashtable<String, String>();
            container.create( "/slasoi", initialMetadata );
            container.read  ( "/slasoi" );
            container.echo  ();
            
            container.update( "/slasoi", newMetadata );
            container.echo  ();
            
            container.delete( "/slasoi" );
            Thread.sleep( 1000 );
            
            // -- EXAMPLE #3 -----------------------------------------------------------
            initialMetadata = new Hashtable<String, String>();
            container.create( "/slasoi", initialMetadata );
            container.read  ( "/slasoi" );
            container.echo  ();
            
            container.update( "/slasoi", newMetadata );
            container.echo  ();
            
            container.delete( "/slasoi" );
            Thread.sleep( 1000 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public static void main( String[] args )
    {
        CDMITester.testContainer();
    }
}
