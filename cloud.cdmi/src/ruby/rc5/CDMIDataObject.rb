require 'rubygems'
require 'net/http'
require 'uri'

require './LOG.rb'
require './Misc.rb'
require './JSONUtils.rb'

# CDMIDataObject (RESTFul Operations: CREATE|READ|UPDATE|DELETE)
class CDMIDataObject
	def initialize( cdmiEPR, parent )
        @cdmiEPR   = cdmiEPR
        @container = parent
	end

	# Returns the associated metadata of this dataobject
	def READ( dataObject, metadataFields = nil )
		header = {}
    	header['Accept']                       = "application/cdmi-object"
    	header['X-CDMI-Specification-Version'] = '1.0.1'
			
		begin
    		url = @cdmiEPR + @container + "/" + dataObject
    		if ( metadataFields != nil )
    		url += ("?"+metadataFields)
    		end
    		
		    body = "{}"
		    uri = URI.parse(url)
		    response = Net::HTTP.start(uri.host, uri.port).send_request('GET', uri.request_uri, body, header) 
     
		rescue  # exception ?
			puts $!
			return nil
		else
            responseHeader = response
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}
            
            # @required:
            #   "mimetype=text/plain"
            responseBody = JSON.parse( response.body )
			
            Misc.getField( resultHeader, responseHeader, 'X-CDMI-Specification-Version')
            Misc.getField( resultHeader, responseHeader, 'Content-Type')
            Misc.getField( resultHeader, responseHeader, 'Location')
            
            Misc.getField( resultBody, responseBody, 'objectURI'        )
            Misc.getField( resultBody, responseBody, 'objectID'         )
            Misc.getField( resultBody, responseBody, 'objectName'       )
            Misc.getField( resultBody, responseBody, 'parentURI'        )
            Misc.getField( resultBody, responseBody, 'domainURI'        )
            Misc.getField( resultBody, responseBody, 'capabilitiesURI'  )
            Misc.getField( resultBody, responseBody, 'completionStatus' )
            Misc.getField( resultBody, responseBody, 'mimetype'         )
            Misc.getField( resultBody, responseBody, 'metadata'         )#more definitions in chapter 16 metadata
            Misc.getField( resultBody, responseBody, 'valuerange'       )
            Misc.getField( resultBody, responseBody, 'value'            )
            #Misc.getField( resultBody, responseBody, 'percentComplete'  )#optional
            
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end
	
	# Updates the metadata of this dataobject and publish it in CDMI server
	#  - it can update only the metadata, but also the content of this dataobject (when filename != nil )
	def UPDATE( newMetadata = nil, filename = nil )
		header={}
		header[ 'Accept'       ] = 'application/cdmi-object'
		header[ 'Content-Type' ] = 'application/cdmi-object'
    	header[ 'X-CDMI-Specification-Version'] = '1.0.1'
		  
		body = {}
		#optional
    	Misc.setField( body, 'mimetype' , newMetadata )
		Misc.setField( body, 'metadata' , newMetadata )
		Misc.setField( body, 'domainURI', newMetadata )
		Misc.setField( body, 'value' , newMetadata )

		if ( filename != nil )
	 	   body[ 'value' ] = Misc.getFileAsString( filename )
		end
		
		begin
	    	url = @cdmiEPR + @container + "/" + dataObject
	      	body = "{}"
	      	uri = URI.parse(url)
	      	response = Net::HTTP.start(uri.host, uri.port).send_request('PUT', uri.request_uri, body, header) 
		rescue  # exception ?
			puts $!
			result = nil
		else
            responseHeader = response
            responseBody   = JSON.parse( response.body )
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}  
			
            Misc.getField( resultHeader, responseHeader, 'Location' )
        
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end
	
	# Removes this container from CDMI server
	def DELETE( dataObject )
		header = {}
		header['X-CDMI-Specification-Version'] = '1.0.1'
		
		begin
		  	url = @cdmiEPR + @container + "/" + dataObject
		  	body = "{}"
      		uri = URI.parse(url)
      		response = Net::HTTP.start(uri.host, uri.port).send_request('DELETE', uri.request_uri, body, header) 
		rescue  # exception ?
			puts $!
			result = nil
		else
            responseHeader = response
            responseBody   = JSON.parse( response.body )
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}  
            
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end
	
	# Creates a new data object
	def CREATE( metadata = nil, filename = nil )
		header = {}
		header[ 'Accept'       ] = 'application/cdmi-object'
		header[ 'Content-Type' ] = 'application/cdmi-object'
    	header[ 'X-CDMI-Specification-Version'] = '1.0.1'
        
		#optional
		body = {}
		Misc.setField( body, 'mimetype'     , metadata )
		Misc.setField( body, 'metadata'     , metadata )
		Misc.setField( body, 'domainURI'    , metadata )
		Misc.setField( body, 'deserialize'  , metadata )
		Misc.setField( body, 'serialize'    , metadata )
		Misc.setField( body, 'copy'         , metadata )
		Misc.setField( body, 'move'         , metadata )
		Misc.setField( body, 'reference'    , metadata )
    	Misc.setField( body, 'deserializevalue'    , metadata )
    	Misc.setField( body, 'value'    , metadata )
		
		if ( filename != nil )
	 	   body[ 'value' ] = Misc.getFileAsString( filename )
		end
		
		begin
      		url = @cdmiEPR + @container + "/" + dataObject
      		body = "{}"
      		uri = URI.parse(url)
      		response = Net::HTTP.start(uri.host, uri.port).send_request('POST', uri.request_uri, body, header) 
		rescue  # exception ?
			puts $!
			result = nil
		else
            responseHeader = response
            responseBody   = JSON.parse( response.body )
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}  
            
            Misc.getField( resultHeader, responseHeader, 'Content-Type'                 )
            Misc.getField( resultHeader, responseHeader, 'X-CDMI-Specification-Version' )
            
            Misc.getField( resultBody, responseBody, 'objectURI'        )
            Misc.getField( resultBody, responseBody, 'objectID'         )
            Misc.getField( resultBody, responseBody, 'objectName'         )
            Misc.getField( resultBody, responseBody, 'parentURI'        )
            Misc.getField( resultBody, responseBody, 'domainURI'        )
            Misc.getField( resultBody, responseBody, 'capabilitiesURI'  )
            Misc.getField( resultBody, responseBody, 'completionStatus' )
            Misc.getField( resultBody, responseBody, 'mimetype'         )
            Misc.getField( resultBody, responseBody, 'metadata'         )   # TODO: more Definitions possible see chapter 16 metadata 
            Misc.getField( resultBody, responseBody, 'percentComplete'  )
            
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end

	# Returns the associated content of this dataobject (binary as string in UTF-8 format)
	def binREAD( dataObject )
		header = {}
        header['Accept']                       = "application/cdmi-object"
        header['X-CDMI-Specification-Version'] = '1.0.1'
			
		begin
		    # FIXME:  normal download ?
      		url = @cdmiEPR + @container + "/" + dataObject
      		body = "{}"
      		uri = URI.parse(url)
      		response = Net::HTTP.start(uri.host, uri.port).send_request('GET', uri.request_uri, body, header) 
		rescue  # exception ?
			puts $!
			return nil
		else
            responseHeader = response
            responseBody   = response.body
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = responseBody
            
            # TODO extract 'value' from 'response.body'
      		return [resultStatus,resultHeader,resultBody]
	    end
	    
	    return nil	
	end	
	
	def dump()
		LOG.echo( "CDMI::DataObject", "@cdmiEPR='%s'; @container='%s'" % [@cdmiEPR,@container] )
	end	
	
end  # EOC-CDMIDataObject