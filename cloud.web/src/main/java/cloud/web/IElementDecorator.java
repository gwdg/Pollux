/**
 * Copyright (c) 2011, X-Juvi
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of X-Juvi
 * 	  nor the names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL X-Juvi
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
package cloud.web;

import java.util.ArrayList;
import java.util.Arrays;

import cloud.services.one.occi.IElement;
import cloud.services.one.occi.INetwork;

/**
 * This Decorator allows to display on the web the attributes of a IElement according to a set
 * of properties.  Those properties have to set via 'config' method.
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class IElementDecorator
{
    public IElementDecorator( IElement decorated )
    {
        this.decorated = decorated;
        attributes = new ArrayList<String[]>();
    }
    
    public void config( String[]caption, String[][] attrs )
    {
        this.caption = caption;
        for ( String[] pair : attrs )
        {
            attributes.add( pair );
        }
    }
    
    public String[] getCaption()
    {
        String cpt = decorated.attributes.get( caption[ 0 ] );
        if ( cpt == null ) cpt = "not available";
        String[] result = new String[]{ caption[ 1 ], cpt };
        
        return result;
    }
    
    public String[][] getValues()
    {
        String[][] result = new String[ attributes.size() ][ 2 ];
        for ( int i = 0; i < attributes.size(); i++ )
        {
            String[] pair = attributes.get( i );
            result[ i ] = new String[ 2 ];
            result[ i ][ 0 ] = pair[ 1 ];
            String value = decorated.attributes.get( pair[ 0 ] );
            result[ i ][ 1 ] = (value!=null?value:"not available");
        }
        
        return result;
    }
    
    public int getLenght()
    {
        return attributes.size();
    }
    
    public IElement getElement()
    {
        return decorated;
    }
    
    protected String[]            caption; 
    protected ArrayList<String[]> attributes; 
    protected IElement            decorated;
    
    public static void main( String[] args )
    {
        try
        {
            INetwork net = new INetwork();
            net.attributes.put( "occi.core.title" , "ABC"        );
            net.attributes.put( "occi.core.cores" , "4"          );
            net.attributes.put( "occi.core.memory", "32"         );
            net.attributes.put( "occi.core.vnc"   , "http://www" );
            
            IElementDecorator decorator = new IElementDecorator( net );
            decorator.config( new String[]
                                { "occi.core.title" , "Title:" },
                              new String[][]{
                                { "occi.core.cores" , "Cores:"       },
                                { "occi.core.memory", "Memory (MB):" },
                                { "occi.core.vnc"   , "URL (ssh):"   }
                            } );

            String[] caption = decorator.getCaption();
            System.out.println( "caption: "+ Arrays.toString( caption ) );
            
            String[][] values = decorator.getValues();
            for ( int i = 0; i < values.length; i++ )
            {
                System.out.println( "attr: "+ Arrays.toString( values[ i ] ) );
            }
        }
        catch ( Exception e )
        {
        }
    }
}
