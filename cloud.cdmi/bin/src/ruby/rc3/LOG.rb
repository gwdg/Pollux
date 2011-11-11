require 'logger'

class LOG
     def initialize()
       @logger = Logger.new( File.open( './logger.log', 'a' ) )
     end

     def _log_( clazz, msg )
        _msg_ = ( "%s: '%s'" % [clazz,msg] )
        if ( @logger != nil )
          @logger.info( _msg_ )
		else
		  puts _msg_
        end
     end
     
     def self.log( clazz, msg )
        if ( @self == nil )
          @self = LOG.new()
        end
        
        @self._log_( clazz, msg )
     end
     
     def self.echo( clazz, msg )
        _msg_ = ( "%s: '%s'" % [clazz,msg] )
        puts _msg_
     end
     
     def self.dbg( clazz, msg )
        if ( true )  # change to false, if dbg information has to be hidden
          _msg_ = ( "%s: '%s'" % [clazz,msg] )
          puts _msg_
        end
     end
end

begin
  # USAGE
  # LOG.log( "ONEUtils", "Message tobe logged" )
end	
