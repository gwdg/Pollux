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

import cloud.services.one.occi.INetwork;
import cloud.services.one.occi.IStorage;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class OcciOneTester
{
    public static void networkTest()
    {
        try
        {
            INetwork net = new INetwork();
            net.content.put( INetwork.URI       , "http://129.217.211.158:3000" );
            net.content.put( INetwork.TITLE     , "DukeNetworkTitle"            );
            net.content.put( INetwork.SUMMARY   , "DukeNetworkSummary"          );
            net.content.put( INetwork.ADDRESS   , "192.168.0.0/24"              );
            net.content.put( INetwork.ALLOCATION, "dynamic"                     );
            net.content.put( INetwork.VLAN      , "1"                           );
            
            System.out.println( "--  CREATING NETWORK ------------------" );
            OcciOneManagerImpl occi = new OcciOneManagerImpl();
            String res = occi.createNetwork( net );
            net.content.put( INetwork.ID, res.substring( 10 ) );
            System.out.println( "\t |- ID: '"+net.content.get( INetwork.ID )+"'" );
            
            System.out.println( "--  DELETING NETWORK ------------------" );
            String del = occi.deleteNetwork( net );
            System.out.println( "\t |- RES: '"+del );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public static void storageTest()
    {
        try
        {
            IStorage storage = new IStorage();
            storage.content.put( IStorage.URI      , "http://129.217.211.158:3000" );
            storage.content.put( IStorage.TITLE    , "DukeStorageTitle"            );
            storage.content.put( IStorage.SUMMARY  , "DukeStorageSummary"          );
            storage.content.put( IStorage.FILE     , "d:\\GFD.185.pdf"             );
            storage.content.put( IStorage.FILE_SIZE, "102400"                      );
            
            System.out.println( "--  CREATING STORAGE ------------------" );
            OcciOneManagerImpl occi = new OcciOneManagerImpl();
            String res = occi.createStorage( storage );
            storage.content.put( IStorage.ID, res.substring( 10 ) );
            System.out.println( "\t |- ID: '"+storage.content.get( IStorage.ID )+"'" );
            
            System.out.println( "--  DELETING STORAGE ------------------" );
            String del = occi.deleteStorage( storage );
            System.out.println( "\t |- RES: '"+del );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public static void main( String[] args )
    {
        // networkTest();
        // storageTest();
    }
}
