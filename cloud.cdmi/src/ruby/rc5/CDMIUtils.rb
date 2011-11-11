require 'rubygems'
require 'net/http'
require 'uri'

require './LOG.rb'
require './Misc.rb'
require './JSONUtils.rb'
require './CDMIContainer.rb'

# CDMIUtils (CDMI Misc Operations: GET EXPORT, ... )
class CDMIUtils
	def initialize( cdmiEPR )
        @cdmiEPR = cdmiEPR
	end

	# Returns true if the server supports exporting functionality
	def isNFSSupported( containerName ) 
		container = CDMIContainer.new( @cdmiEPR )
		caps = container.READ( containerName, "metadata:cdmi_nfs_export" )  
		if ( caps != nil )
			supported = caps[ 2 ]
			if ( supported != nil && supported == "true" )
				return true
			end
		end
		
		return false
	end
	
	# Returns the export NFS-Information related to a Container
	def getNFSInformation( containerName )
        container = CDMIContainer.new( @cdmiEPR )
        exportInfo = container.READ( containerName, "exports" )
        if ( exportInfo != nil )
			if ( exportInfo[ 2 ][ 'exports' ] != nil )  # 'exports' supported
				return exportInfo[ 2 ][ 'exports' ]
			end
        end
		
		return nil
	end
	
	def echo( str )
		LOG.echo( "CDMIUtils", "%s" % [str] )
	end	

end  # EOC-CDMIUtils
