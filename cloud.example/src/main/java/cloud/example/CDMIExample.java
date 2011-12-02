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
