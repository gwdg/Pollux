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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import cloud.services.one.occi.ICompute;
import cloud.services.one.occi.ILink;
import cloud.services.one.occi.INetwork;
import cloud.services.one.occi.IStorage;

public class OcciExecutorCompletionService
{
    public void fetchNetworkInformation( Hashtable<String, String> headers, INetwork[] networks )
    {
        try
        {
            int poolSize = networks.length*40/100;
            if ( poolSize < 5 ) poolSize = 5;
            
            System.out.println( "Using OcciExecutorCompletionService:poolSize="+poolSize );
            ExecutorService pool = Executors.newFixedThreadPool( poolSize );
            ArrayList<OCCiNetworkInfoFetcher> al = new ArrayList<OCCiNetworkInfoFetcher>();
            
            for ( int i = 0; i < networks.length; i++ )
                al.add( new OCCiNetworkInfoFetcher( headers, networks[ i ] ) );
            
            runNetworksInParallel( pool, al );
            pool.shutdown();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void runNetworksInParallel( Executor e, Collection<? extends Callable<INetwork>> nets ) throws InterruptedException, ExecutionException
    {
        CompletionService<INetwork> ecs = new ExecutorCompletionService<INetwork>( e );
        for ( Callable<INetwork> s : nets )
            ecs.submit( s );
        int n = nets.size();
        for ( int i = 0; i < n; ++i )
        {
            INetwork r = ecs.take().get();
            if ( r != null )
            { 
                String caption = r.getAttributes().get( "occi.core.title" );
                if ( caption == null ) r.getAttributes().put( "occi.core.title", "title not available" );
            }
        }
    }
    
    public void fetchStorageInformation( Hashtable<String, String> headers, IStorage[] storages )
    {
        try
        {
            int poolSize = storages.length*40/100;
            if ( poolSize < 5 ) poolSize = 5;
            
            System.out.println( "Using OcciExecutorCompletionService:poolSize="+poolSize );
            ExecutorService pool = Executors.newFixedThreadPool( poolSize );
            ArrayList<OCCiStorageInfoFetcher> al = new ArrayList<OCCiStorageInfoFetcher>();
            
            for ( int i = 0; i < storages.length; i++ )
                al.add( new OCCiStorageInfoFetcher( headers, storages[ i ] ) );
            
            runStoragesInParallel( pool, al );
            pool.shutdown();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void runStoragesInParallel( Executor e, Collection<? extends Callable<IStorage>> storages ) throws InterruptedException, ExecutionException
    {
        CompletionService<IStorage> ecs = new ExecutorCompletionService<IStorage>( e );
        for ( Callable<IStorage> s : storages )
            ecs.submit( s );
        int n = storages.size();
        for ( int i = 0; i < n; ++i )
        {
            IStorage r = ecs.take().get();
            if ( r != null )
            { 
                String caption = r.getAttributes().get( "occi.core.title" );
                if ( caption == null ) r.getAttributes().put( "occi.core.title", "title not available" );
            }
        }
    }
    
    public void fetchVmInformation( Hashtable<String, String> headers, ICompute[] vms )
    {
        try
        {
            int poolSize = vms.length*40/100;
            if ( poolSize < 5 ) poolSize = 5;
            
            System.out.println( "Using OcciExecutorCompletionService:poolSize="+poolSize );
            ExecutorService pool = Executors.newFixedThreadPool( poolSize );
            ArrayList<OCCiVmInfoFetcher> al = new ArrayList<OCCiVmInfoFetcher>();
            
            for ( int i = 0; i < vms.length; i++ )
                al.add( new OCCiVmInfoFetcher( headers, vms[ i ] ) );
            
            runInParallel( pool, al );
            pool.shutdown();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void runInParallel( Executor e, Collection<? extends Callable<ICompute>> vms ) throws InterruptedException, ExecutionException
    {
        CompletionService<ICompute> ecs = new ExecutorCompletionService<ICompute>( e );
        for ( Callable<ICompute> s : vms )
            ecs.submit( s );
        int n = vms.size();
        for ( int i = 0; i < n; ++i )
        {
            ICompute r = ecs.take().get();
            if ( r != null )
            { 
                String caption = r.getAttributes().get( "occi.core.title" );
                if ( caption == null ) r.getAttributes().put( "occi.core.title", "title not available" );
            }
        }
    }
    
    // -- HELPER CLASSES --------------------------------------------------------------
    
    public class OCCiVmInfoFetcher implements Callable<ICompute>
    {
        Hashtable<String, String> headers;
        ICompute vm;
        
        public OCCiVmInfoFetcher( Hashtable<String, String> headers, ICompute vm )
        {
            this.headers = headers;
            this.vm      = vm;
        }
        
        public ICompute call() throws Exception
        {
            // Details about this Compute
            String URI = this.vm.getContent().get( ICompute.URI );
            
            OcciResponse inner = HttpUtils.get( URI, headers );
            if ( inner != null && inner.content != null )
            {
                for ( String attr : inner.content )
                {
                    if ( attr.contains( "-Attribute" ) )
                    {
                        String ATT = attr.substring( attr.indexOf( "Attribute" ) + 11 );
                        String[] tokens = ATT.split( "=" );
                        
                        if ( tokens.length >= 2 )
                        {
                            //remove extra """
                            tokens[ 0 ] = ATT.substring( 0, ATT.indexOf( "=" ) ).replace( "\"", "" );
                            tokens[ 1 ] = ATT.substring( ATT.indexOf( "=" )+1 ).replace( "\"", "" );
                        }
                        else if ( tokens.length == 1 )
                        {
                            tokens = new String[]{ tokens[ 0 ].replace( "\"", "" ), "" };
                        }
                        
                        this.vm.getAttributes().put( tokens[ 0 ], tokens[ 1 ] );
                    }
                    else if ( attr.startsWith( "Link" ) ) // Format[  Link: </api/compute/ea97ad10-8272-4df3-a9dc-484b0aa75902?action=start>; ... ]
                    {
                        ILink link = buildLinkFrom( attr );
                        if ( link != null )
                            this.vm.getLinks().add( link );
                    }
                }
            }
            
            return this.vm;
        }
    }
    
    public class OCCiStorageInfoFetcher implements Callable<IStorage>
    {
        Hashtable<String, String> headers;
        IStorage storage;
        
        public OCCiStorageInfoFetcher( Hashtable<String, String> headers, IStorage vm )
        {
            this.headers = headers;
            this.storage = vm;
        }
        
        public IStorage call() throws Exception
        {
            // Details about this Compute
            String URI = this.storage.getContent().get( IStorage.URI );
            
            OcciResponse inner = HttpUtils.get( URI, headers );
            if ( inner != null && inner.content != null )
            {
                for ( String attr : inner.content )
                {
                    if ( attr.contains( "-Attribute" ) )
                    {
                        String ATT = attr.substring( attr.indexOf( "Attribute" ) + 11 );
                        String[] tokens = ATT.split( "=" );
                        
                        if ( tokens.length >= 2 )
                        {
                            //remove extra """
                            tokens[ 0 ] = ATT.substring( 0, ATT.indexOf( "=" ) ).replace( "\"", "" );
                            tokens[ 1 ] = ATT.substring( ATT.indexOf( "=" )+1 ).replace( "\"", "" );
                        }
                        else if ( tokens.length == 1 )
                        {
                            tokens = new String[]{ tokens[ 0 ].replace( "\"", "" ), "" };
                        }
                        
                        this.storage.getAttributes().put( tokens[ 0 ], tokens[ 1 ] );
                    }
                    else if ( attr.startsWith( "Link" ) ) // Format[  Link: </api/compute/ea97ad10-8272-4df3-a9dc-484b0aa75902?action=start>; ... ]
                    {
                        ILink link = buildLinkFrom( attr );
                        if ( link != null )
                            this.storage.getLinks().add( link );
                    }
                }
            }
            
            return this.storage;
        }
    }
    
    public class OCCiNetworkInfoFetcher implements Callable<INetwork>
    {
        Hashtable<String, String> headers;
        INetwork net;
        
        public OCCiNetworkInfoFetcher( Hashtable<String, String> headers, INetwork net )
        {
            this.headers = headers;
            this.net     = net;
        }
        
        public INetwork call() throws Exception
        {
            // Details about this Compute
            String URI = this.net.getContent().get( ICompute.URI );
            
            OcciResponse inner = HttpUtils.get( URI, headers );
            if ( inner != null && inner.content != null )
            {
                for ( String attr : inner.content )
                {
                    if ( attr.contains( "-Attribute" ) )
                    {
                        String ATT = attr.substring( attr.indexOf( "Attribute" ) + 11 );
                        String[] tokens = ATT.split( "=" );
                        
                        if ( tokens.length >= 2 )
                        {
                            //remove extra """
                            tokens[ 0 ] = ATT.substring( 0, ATT.indexOf( "=" ) ).replace( "\"", "" );
                            tokens[ 1 ] = ATT.substring( ATT.indexOf( "=" )+1 ).replace( "\"", "" );
                        }
                        else if ( tokens.length == 1 )
                        {
                            tokens = new String[]{ tokens[ 0 ].replace( "\"", "" ), "" };
                        }
                        
                        this.net.getAttributes().put( tokens[ 0 ], tokens[ 1 ] );
                    }
                    else if ( attr.startsWith( "Link" ) ) // Format[  Link: </api/compute/ea97ad10-8272-4df3-a9dc-484b0aa75902?action=start>; ... ]
                    {
                        ILink link = buildLinkFrom( attr );
                        if ( link != null )
                            this.net.getLinks().add( link );
                    }
                }
            }
            
            return this.net;
        }
    }
    
    protected ILink buildLinkFrom( String link )
    {
        ILink result = new ILink();
        String linkCnt = link.substring( link.indexOf( "<" )+1, link.indexOf( ">" ) );
        result.put( ILink.TITLE, linkCnt );
        
        // extract set of attributes from 'link' variable
        // link example: "Link: <data attr=\"va\">abcdefg123456789;;occi.core.source=\"/compute/e1426c98-0b84-11e1-9e8e-00163e211147\";occi.core.target=\"value\";abcdefg123456789";
        String lnRE = "(occi.(?:\"[^\"]*\"|[^=;])*)=((?:\"[^\"]*\"|[^=;])*)";
        
        Pattern p = Pattern.compile( lnRE );
        Matcher m = p.matcher( link );
        
        while ( m.find() ) 
        {
            if ( m.groupCount() == 2 )
            {
                String key   = m.group( 1 );
                String value = m.group( 2 );
                result.put( key, StringUtils.removeEnd( StringUtils.removeStart( value, "\"" ), "\"" ) );
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) 
    {
        String link = "Link: <data attr=\"va\">abcdefg123456789;;occi.core.source=\"/compute/e1426c98-0b84-11e1-9e8e-00163e211147\";occi.core.target=\"value\";abcdefg123456789";
        String lnRE = "(occi.(?:\"[^\"]*\"|[^=;])*)=((?:\"[^\"]*\"|[^=;])*)";
        
        Pattern p = Pattern.compile( lnRE );
        Matcher m = p.matcher( link );
        
        while ( m.find() ) 
        {
            String matchedKey   = m.group( 1 );
            String matchedValue = m.group( 2 );
            
            int matchedFrom = m.start();
            int matchedTo = m.end();
            
            System.out.println( "matched [" + matchedKey + ":" + matchedValue + "] from " +
                                matchedFrom + " to " + matchedTo + "." );
            
        } 
    }
}
