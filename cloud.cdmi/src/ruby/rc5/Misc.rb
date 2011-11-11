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

	def self.getFileAsBinary( filename )
	   file = File.open( filename, "rb" )
       return file.read
	end

	def self.getFile( filename )
	   file = File.open( filename, "r" )
       return file.read
	end

	def self.writeAsBinary( filename, content )
	   file = File.open( filename, "wb" )
       file.write content
	end
end
