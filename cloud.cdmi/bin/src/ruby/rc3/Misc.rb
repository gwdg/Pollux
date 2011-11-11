require 'json'

class Misc

	def self.setField( target, field, metadata )
	    if ( metadata == nil ) 
	       return
	    end
	    
		value = metadata[ field ]
		if ( value != nil )
			target[ field ] = value
		end
	end

	def self.getField( target, source, field )
	    if ( source == nil ) 
	       return
	    end
	    
		value = source[ field ]
		if ( value != nil )
			target[ field ] = value
		end
	end

	def self.getFileAsString( filename )
	   file = File.open( filename, "rb" )
       return file.read
	end
end
