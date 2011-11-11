require 'rubygems'
require 'net/http'
require 'uri'

require './LOG.rb'
require './Misc.rb'
require './JSONUtils.rb'

# CDMI::CONTAINER (RESTFul Operations: CREATE|READ|UPDATE|DELETE)
class CDMIContainer
	def initialize( cdmiEPR )
        @cdmiEPR = cdmiEPR
	end

	# Returns the associated metadata of this container
	def READ( containerName, metadataFields = nil )
		header = {}
        header['Accept']                       = "application/cdmi-container"
        header['X-CDMI-Specification-Version'] = '1.0.1'
			
		begin
    		url = @cdmiEPR + containerName
    		if ( metadataFields != nil )
    		  url += ("/?"+metadataFields)
    		end
			body = "{}"
			uri = URI.parse(url)
			response = Net::HTTP.start(uri.host, uri.port).send_request( 'GET', uri.request_uri, body, header ) 
		rescue  # exception ?
			puts $!
			return nil
		else
            responseHeader = response
            responseBody   = JSON.parse( response.body )
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}  

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
            Misc.getField( resultBody, responseBody, 'metadata'         ) #more definitions in chapter 16
            Misc.getField( resultBody, responseBody, 'childrenrange'    )
            Misc.getField( resultBody, responseBody, 'children'         )
            Misc.getField( resultBody, responseBody, 'percentComplete'  )#optional
            Misc.getField( resultBody, responseBody, 'exports'          )#optional  
            Misc.getField( resultBody, responseBody, 'snapshots'        )#optional
		
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end
	
	# Updates the metadata of this container and publish it in CDMI server
	def UPDATE( containerName, newMetadata )
		header={}
		header[ 'Accept'       ] = 'application/cdmi-container'
		header[ 'Content-Type' ] = 'application/cdmi-container'
		header[ 'X-CDMI-Specification-Version'] = '1.0.1'
		  
		body = {}
		#optional
		Misc.setField( body, 'metadata' , newMetadata )
		Misc.setField( body, 'domainURI', newMetadata )
		Misc.setField( body, 'snapshot' , newMetadata )
		Misc.setField( body, 'exports'  , newMetadata )
		
		begin
			url = @cdmiEPR + containerName
			body = "{}"
			uri = URI.parse(url)
			response = Net::HTTP.start(uri.host, uri.port).send_request( 'PUT', uri.request_uri, body, header ) 
		rescue  # exception ?
			puts $!
			result = nil
		else
            responseHeader = response
            responseBody   = response.body
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}  
			
            Misc.getMetadataField( resultHeader, responseHeader, 'Location' )
        
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end
	
	# Removes this container from CDMI server
	def DELETE( containerName )
		header = {}
		header['X-CDMI-Specification-Version'] = '1.0.1'
		
		begin
			url = @cdmiEPR + containerName + "/"
			body = "{}"
			uri = URI.parse(url)
			response = Net::HTTP.start(uri.host, uri.port).send_request( 'DELETE', uri.request_uri, body, header )
		rescue  # exception ?
			puts $!
			result = nil
		else
            responseHeader = response
            responseBody   = response.body
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}  
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end
	
	# Creates a new container
	def CREATE( containerName, metadata = nil )
		header = {}
		header[ 'Accept'       ] = 'application/cdmi-container'
		header[ 'Content-Type' ] = 'application/cdmi-container'
		header[ 'X-CDMI-Specification-Version'] = '1.0.1'
      
		#optional
		body = {}
		Misc.setField( body, 'metadata'            , metadata )
		Misc.setField( body, 'domainURI'           , metadata )
		Misc.setField( body, 'exports'             , metadata )
		Misc.setField( body, 'deserialize'         , metadata )
		Misc.setField( body, 'copy'                , metadata )
		Misc.setField( body, 'move'                , metadata )
		Misc.setField( body, 'reference'           , metadata )
		Misc.setField( body, 'deserializevalue'    , metadata )
		
		begin
			url = @cdmiEPR + containerName
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
            Misc.getField( resultHeader, responseHeader, 'Content-Type' )
            Misc.getField( resultHeader, responseHeader, 'X-CDMI-Specification-Version' )
            
            Misc.getField( resultBody, responseBody, 'objectURI'        )
            Misc.getField( resultBody, responseBody, 'objectID'         )
            Misc.getField( resultBody, responseBody, 'objectName'       )
            Misc.getField( resultBody, responseBody, 'parentURI'        )
            Misc.getField( resultBody, responseBody, 'domainURI'        )
            Misc.getField( resultBody, responseBody, 'capabilitiesURI'  )
            Misc.getField( resultBody, responseBody, 'completionStatus' )
            Misc.getField( resultBody, responseBody, 'metadata'         )#more definitions in chapter 16
            Misc.getField( resultBody, responseBody, 'childrenrange'    )
            Misc.getField( resultBody, responseBody, 'children'         )
            #Misc.getField( resultBody, responseBody, 'exports'          )#optional
		    #Misc.getField( resultBody, responseBody, 'snapshots'        )#optional
            #Misc.getField( resultBody, responseBody, 'percentComplete'  )#optional
            
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end
	
	def dump()
		LOG.echo( "CDMIContainer", "@cdmiEPR ='%s'" % @cdmiEPR )
	end	
	
end  # EOC-CDMIContainer
