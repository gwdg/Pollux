package cloud.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cloud.services.one.occi.ICompute;
import cloud.services.one.occi.IOCCIOneManager;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class OCCIExample 
{
    public OCCIExample()
    {
        init();
    }
    
    protected void init()
    {
        try
        {
            ApplicationContext ctx = new ClassPathXmlApplicationContext( "META-INF/spring/module-context.xml" );
            occi = (IOCCIOneManager)ctx.getBean( "occi-one" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void createVM()
    {
        try
        {
            ICompute vm = new ICompute();
            String URI = "http://129.217.211.147:3000";
            
            vm.content.put( ICompute.URI         , URI            );
            vm.content.put( ICompute.TITLE       , "MyVM"         );
            vm.content.put( ICompute.SUMMARY     , "MyVM-summary" );
            vm.content.put( ICompute.ARCHITECTURE, "x64"          );
            vm.content.put( ICompute.CORES       , "2"            );
            vm.content.put( ICompute.MEMORY      , "32"           );
            vm.content.put( ICompute.NETWORK     , "networkID"    );
            vm.content.put( ICompute.STORAGE     , "storageID"    );
                
            occi.createCompute( vm );
            occi.startCompute ( vm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void dumpVMs()
    {
        try
        {
            ICompute[] computes = occi.getComputes( "http://129.217.211.147:3000" );
            if ( computes != null )
                for ( ICompute s : computes )
                {
                    System.out.println( s );
                }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public static void main( String[] args )
    {
        OCCIExample ex = new OCCIExample();
        ex.createVM();
    }
    
    protected IOCCIOneManager occi;
}
