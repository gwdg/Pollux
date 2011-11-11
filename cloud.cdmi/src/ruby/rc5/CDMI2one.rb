#!/usr/bin/env ruby

#PATH = ENV["PATH"]
#if !PATH
#    WUBI_LOCATION = "/usr/lib/one/ruby"
#else
#    WUBI_LOCATION = PATH + "/lib/ruby"
#end

require 'getoptlong'

require 'rubygems'
require 'net/http'
require 'uri'

require './LOG.rb'
require './Misc.rb'
require './CDMIUtils.rb'
require './NonCDMIDataObject.rb'

# cdmi2one
class CDMI2one
	def initialize()
	end

	def log( str )
		LOG.echo( "CDMI2one::", "'%s'" % [str] )
	end	
	
	# Returns the exported image name.  
	def getImageExportPath( imageURI )
		log "Using '%s'"%[imageURI]
		
        begin
            (cdmi, container, image) = split( imageURI )
            
            # read export-capabilities of container
            utils = CDMIUtils.new( cdmi )
            exported = utils.getNFSInformation( container )
            if ( exported == nil )
                exported = defaultNFSExport()
            end
            result  = getNFSPath( exported )
			# -- FIXME --  Currently the server does not support query of 'objectID'
            # nonObj  = NonCDMIDataObject.new( cdmi, container )
            # imageID = nonObj.ID( image )
			imageID = image
            
			puts 'CDMI2ONE_EXPORT=%s'%[result+image]
			puts 'CDMI2ONE_IMAGE=%s'%[image]
			puts 'CDMI2ONE_IMAGE_ID=%s'%[imageID]
        rescue  # exception ?
            puts $!
            result = nil
        else
		end
	end
	
	# Returns the exported nfs-path
	def getNFSPath( exportSentence )
		if ( exportSentence == nil ) # 'exports' facility not supported
			exportSentence = defaultNFSExport()
		end
		
		begin
			log "export=%s"%exportSentence
			content = JSON.parse( exportSentence )
			if ( content != nil )
				result = content[ "nfs" ][ "exportpath" ]
				return result
			end
		rescue
		else
		end
		
        return nil
	end
	
	# Returns the three components from a CDMI-URI
	def split( uri )
	   # i.e.   URI = http://localserver:2364/Wubi/WubiOS.zip
	   imgIdx = uri.rindex('/')
	   image = "%s"%uri[ imgIdx .. -1 ]
	   srvIdx = findOccurrence( uri, "/", 3 )
	   cdmi = "%s"%uri[ 0 .. srvIdx-1 ]
	   container = "%s"%(uri[ srvIdx .. imgIdx-1 ])

	   return [cdmi, container, image]
	end
	
    def defaultNFSExport()
        return Misc.getFile( "export.txt" )
    end

	def findOccurrence( var, substring, n )
		position = -1

		if n == 1
			position = var.index( substring )
			if ( position == nil )
				position = -1
			end
			return position
		else
			i = 0
			while i < n do
				position = var.index(substring, position+1)
				if position != nil
					i += 1
				else
					break
				end
			end
			if ( position == nil )
				position = -1
			end
			return position
		end
		return position
	end

end  # EOC-cdmi2one

# ---- MAIN ------------------------------------------------------
opts = GetoptLong.new(
    [ '--image', '-i', GetoptLong::REQUIRED_ARGUMENT ],
    [ '--dest',  '-d', GetoptLong::OPTIONAL_ARGUMENT ]
)
image = ""
destination = ""

begin
    opts.each do |opt, arg|
        case opt
            when '--image'
                image = arg
            when '--dest'
                destination = arg
        end
    end
rescue Exception => e
    exit(-1)
end

image = "http://129.217.252.37:2364/Wubi/WubiOS.zip"
utils=CDMI2one.new()
utils.getImageExportPath( image )
