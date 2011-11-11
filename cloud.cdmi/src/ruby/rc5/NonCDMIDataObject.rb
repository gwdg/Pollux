require 'rubygems'
require 'net/http'
require 'uri'

require './LOG.rb'
require './Misc.rb'

# NonCDMIDataObject (RESTFul Operations: CREATE|READ|UPDATE|DELETE)
class NonCDMIDataObject
	def initialize( cdmiEPR, parent )
        @cdmiEPR   = cdmiEPR
        @container = parent
	end

	# Returns the ID of associated nonDataObject
	def ID( nonDataObjectName )
			
		begin
    		url    = @cdmiEPR + @container + nonDataObjectName + "?objectID" 
    		header = {}
		    body   = "{}"
		    uri    = URI.parse( url )
		    response = Net::HTTP.start(uri.host, uri.port).send_request('GET', uri.request_uri, body, header) 
     
		rescue  # exception ?
			puts $!
			return nil
		else
            return response.body
		end
		
		return nil
	end
	
	# Saves the content of this non-cdmi object and writes it out in 'target'
	def READ( dataObject, target )
		header = {}
			
		begin
    		url = @cdmiEPR + @container + "/" + dataObject
		    body = "{}"
		    uri = URI.parse( url )
		    response = Net::HTTP.start(uri.host, uri.port).send_request('GET', uri.request_uri, body, header) 
     
		rescue  # exception ?
			puts $!
			return nil
		else
            responseHeader = response
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}
            
            Misc.writeAsBinary( target, response.body )
			
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end
	
	# Updates the content of this non-cdmi dataobject and publish it in CDMI server
	def UPDATE( dataobjectName, filename, mimetype = nil )
		return CREATE( dataobjectName, filename, mimetype )
	end
	
	# Removes this non-cdmi object from CDMI server
	def DELETE( dataObject )
		header = {}
		
		begin
		  	url = @cdmiEPR + @container + "/" + dataObject
		  	body = "{}"
      		uri = URI.parse( url )
      		response = Net::HTTP.start(uri.host, uri.port).send_request('DELETE', uri.request_uri, body, header) 
		rescue  # exception ?
			puts $!
			result = nil
		else
            responseHeader = response
            responseBody   = ""
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}  
            
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end
	
	# Creates a new non-cdmi data object
	def CREATE( dataObject, filename, mimetype = nil )
		if ( mimetype == nil )
			mimetype = "text/plain"
		end
		header = {}
		header[ 'Content-Type' ] = mimetype
        
		body = {}
		body[ 'value' ] = Misc.getFileAsBinary( filename )
		
		begin
      		url = @cdmiEPR + @container + "/" + dataObject
      		uri = URI.parse( url )
      		response = Net::HTTP.start(uri.host, uri.port).send_request('PUT', uri.request_uri, body, header) 
			rescue  # exception ?
			puts $!
			result = nil
		else
            responseHeader = response
            responseBody   = ""
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}  
            
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end

	def dump()
		LOG.echo( "CDMI::NonDataObject", "@cdmiEPR='%s'; @container='%s'" % [@cdmiEPR,@container] )
	end	
	
end  # EOC-NonCDMIDataObject