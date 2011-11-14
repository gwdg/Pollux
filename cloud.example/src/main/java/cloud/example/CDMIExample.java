package cloud.example;

import java.io.File;
import java.util.Vector;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cloud.services.cdmi.ICdmiManager;
import cloud.services.cdmi.SCDMIResponse;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class CDMIExample
{
    public CDMIExample()
    {
        init();
    }
    
    public void init()
    {
        try
        {
            ApplicationContext ctx = new ClassPathXmlApplicationContext( "META-INF/spring/module-context.xml" );
            cdmi = (ICdmiManager)ctx.getBean( "cdmi" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void uploadImage()
    {
        try
        {
            File f = new File( "d:\\debian-live-6.0.3-i386-kde-desktop.img" );
            cdmi.createNonDataObject( "http://localhost:2364", "/mr", "debianImage.img", f.getAbsolutePath() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void dumpImages()
    {
        try
        {
            Vector<SCDMIResponse> images = cdmi.filesFrom( "http://localhost:2364", "iso;zip;img" );
            for ( SCDMIResponse entry : images )
            {
                System.out.println( entry.content );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        } 
        
    }
    
    public static void main( String[] args )
    {
        CDMIExample ex = new CDMIExample();
        ex.uploadImage();
        ex.dumpImages();
    }
    
    protected ICdmiManager cdmi;
}
