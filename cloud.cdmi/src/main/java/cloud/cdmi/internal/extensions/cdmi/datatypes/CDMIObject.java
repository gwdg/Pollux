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
package cloud.cdmi.internal.extensions.cdmi.datatypes;

/**
 * 
 * @author Miguel Rojas (email.miguel.rojas@googlemail.com)
 *
 */
public interface CDMIObject
{
    public RequestHeaders  requestHeaders (); 
    public RequestBody     requestBody    ();
    
    public ResponseHeaders responseHeaders(); 
    public ResponseBody    responseBody   (); 
    
    public static interface RequestHeaders
    {
        public String[] optional ( CDMIOperation op );
        public String[] mandatory( CDMIOperation op );
    }
    
    public static interface RequestBody
    {
        public String[] optional ( CDMIOperation op );
        public String[] mandatory( CDMIOperation op );
    }
    
    public static interface ResponseHeaders
    {
        public String[] optional ( CDMIOperation op );
        public String[] mandatory( CDMIOperation op );
    }
    
    public static interface ResponseBody
    {
        public String[] optional ( CDMIOperation op );
        public String[] mandatory( CDMIOperation op );
    }
    
    public static enum MandatoryOptional
    {
        MANDATORY, OPTIONAL
    }
}
