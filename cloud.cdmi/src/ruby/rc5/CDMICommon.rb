require 'rubygems'
require 'net/http'
require 'uri'

require './LOG.rb'
require './Misc.rb'
require './JSONUtils.rb'

# Common Operations
class CDMICommon
	def initialize( cdmiEPR )
        @cdmiEPR = cdmiEPR
	end

	# Returns the associated metadata of this container
	def READcapabilities( metadataFields = nil )
        header = {}
        header['Accept']                       = "application/cdmi-capability"
        header['X-CDMI-Specification-Version'] = '1.0.1'
			
        begin
            url = @cdmiEPR + "cdmi_capabilities"
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
            responseBody   = JSON.parse( response.body )
            resultStatus   = response.code
            resultHeader   = {}  
            resultBody     = {}  
			
            Misc.getField( resultHeader, responseHeader, 'X-CDMI-Specification-Version')
            Misc.getField( resultHeader, responseHeader, 'Content-Type')
            
            Misc.getField( resultBody, responseBody, 'objectURI'     )
            Misc.getField( resultBody, responseBody, 'objectID'      )
            Misc.getField( resultBody, responseBody, 'objectName'    )
            Misc.getField( resultBody, responseBody, 'parentURI'     )
            Misc.getField( resultBody, responseBody, 'capabilities'  )
            Misc.getField( resultBody, responseBody, 'childrenrange' )
            Misc.getField( resultBody, responseBody, 'children'      )
            
      		return [resultStatus,resultHeader,resultBody]
		end
		
		return result
	end
	
	def dump()
		LOG.echo( "CDMICommon", "@cdmiEPR ='%s'" % @cdmiEPR )
	end	
	
end  # EOC-CDMICommon