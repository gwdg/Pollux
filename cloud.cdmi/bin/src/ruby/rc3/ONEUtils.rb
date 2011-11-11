require './LOG.rb'
require './ParserUtils.rb'

class ONEUtils
   def initialize()
     @url = 'http://ls29.itmc.tu-dortmund.de:3000'
   end
	 
   def createVM( imageID, endpoint )
	# TODO
       header = {}
       header['Content-Type']     = 'text/occi'
       header['LINK']             = ( '%s;rel="http://schemas.ogf.org/occi/core#link";category="http://schemas.ogf.org/occi/infrastructure#storagelink";occi.storagelink.deviceid=%s;' % [endpoint, imageID])
       header['X-OCCI-Attribute'] = 'occi.core.title="My VM"'
       header['X-OCCI-Attribute'] = 'occi.core.summary="A short summary"'
       header['X-OCCI-Attribute'] = 'occi.compute.architecture="x64"'
       header['X-OCCI-Attribute'] = 'occi.compute.cores=1'
       header['X-OCCI-Attribute'] = 'occi.compute.memory=4'
       header['Category'        ] = 'compute; scheme="http://schemas.ogf.org/occi/infrastructure#";class="kind";'
     
      response = ''
        begin
          response = RestClient.post @url, header
        rescue  # exception ?
          response = ''
          puts $!
        else
          response = response.body
        end
  
        LOG.log( "ONEUtils", "createVM :: header %s " % header )
        LOG.log( "ONEUtils", "createVM %s : response: %s" % [imageID,response] )
  	 end
    
end  # END-OF-CLASS
