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
package cloud.web;

import java.util.Hashtable;
import java.util.UUID;

/**
 * This class represents the Session manager of sessions
 * in the WebApplication. This Java class provides utilities for creating, 
 * deleting and handling sessions.  
 * 
 * Note that this JavaBean supports the main WebController maintaining a
 * list of active sessions.  On each session object, the user is able to store 
 * key-value that can be restored anytime from any dynamic page in this 
 * Webapplication.
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public class SessionManager
{
    public static SessionManager getInstance()
    {
        return self;
    }
    
    public Session createSession()
    {
        try
        {
            UUID id = UUID.randomUUID();
            Session result = new Session( id.toString() );
            
            sessions.put( id.toString(), result );
            
            return result;
        }
        catch ( Exception e )
        {
        }
        
        return null;
    }
    
    public boolean checkSession( String id, String pwd )
    {
        Session s = sessions.get( id );
        if ( s != null )
        {
            // if ( pwd != null && pwd.equals( "123"+id+"abc" ) )
            if ( pwd != null && pwd.equals( "x123" ) )
            {
                return true;
            }
        }
        
        return false;
    }
    
    public Session session( String id )
    {
        return sessions.get( id );
    }

    public Session remove( String id )
    {
        return sessions.remove( id );
    }

    public class Session
    {
        public Session( String id )
        {
            properties.put( "id", id );
        }
        
        public String getId()
        {
            return properties.get( "id" );
        }
        
        public Hashtable<String,String> getProperties()
        {
            return properties;
        }
        
        public String toString()
        {
            return String.format( "ID:%s, Props:%s", properties.get( "id" ), properties );
        }
        
        public Hashtable<String,String> properties = new Hashtable<String, String>();
        
        public static final String OCCI = "server.occi";
        public static final String CDMI = "server.cdmi";
    }

    protected static final SessionManager self = new SessionManager();
    
    protected Hashtable<String,Session> sessions = new Hashtable<String, Session>();
    
    public static void main( String[] args )
    {
        System.out.println( self.createSession() );
    }
}
