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

public class OcciExecutorCompletionService
{
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
                // consume VM ( r )
            }
        }
    }
    
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
}
