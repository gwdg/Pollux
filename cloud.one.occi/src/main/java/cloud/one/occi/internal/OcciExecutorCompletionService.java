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

import cloud.services.one.occi.ICompute;
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
                        String linkCnt = attr.substring( attr.indexOf( "<" )+1, attr.indexOf( ">" ) );
                        
                        this.vm.getLinks().add( linkCnt );
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
                        String linkCnt = attr.substring( attr.indexOf( "<" )+1, attr.indexOf( ">" ) );
                        
                        this.storage.getLinks().add( linkCnt );
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
                        String linkCnt = attr.substring( attr.indexOf( "<" )+1, attr.indexOf( ">" ) );
                        
                        this.net.getLinks().add( linkCnt );
                    }
                }
            }
            
            return this.net;
        }
    }
}
