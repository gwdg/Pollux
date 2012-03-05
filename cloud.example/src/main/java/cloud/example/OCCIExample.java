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
    
    public String createVM()
    {
        try
        {
            ICompute vm = new ICompute();
            //String URI = "http://129.217.211.147:3000"; // TUDO
            String URI = "http://134.76.9.66:3000";
            
            vm.content.put( ICompute.URI         , URI            );
            vm.content.put( ICompute.TITLE       , "MyVM"         );
            vm.content.put( ICompute.SUMMARY     , "MyVM-summary" );
            vm.content.put( ICompute.ARCHITECTURE, "x64"          );
            vm.content.put( ICompute.CORES       , "2"            );
            vm.content.put( ICompute.MEMORY      , "32"           );
            vm.content.put( ICompute.NETWORK     , "TestNetwork(627ba25a-09e8-11e1-997c-00163e211147);MAC=MACx;IP=IPx;Gateway=GWx;Allocation=ALLOx;"    );
            vm.content.put( ICompute.STORAGE     , "http://129.217.211.163:2364/derby/ttylinux.img;Mountpoint=/here;"    );
                
            return occi.createCompute( vm );
           // occi.startCompute ( vm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
		return null;
    }
    
    public void dumpVMs()
    {
        try
        {
          //  ICompute[] computes = occi.getComputes( "http://129.217.211.147:3000" );
            ICompute[] computes = occi.getComputes( "http://134.76.9.66:3000" );
            
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
        System.out.print(ex.createVM());
        
    }
    
    protected IOCCIOneManager occi;
}
