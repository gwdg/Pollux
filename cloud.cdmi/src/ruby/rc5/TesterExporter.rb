require './LOG.rb'
require './Misc.rb'
require './CDMIUtils.rb'
require './CDMIContainer.rb'

class TesterExporter
     def initialize()
        @server    = "http://129.217.252.37:2364"
        @container = "/WubiExporter"
        @image     = "/home/gerard/wubi.img"
        @export    = Misc.getFile( "/home/gerard/export.txt" )
     end
     
     def create()
        # FIXME: check in runtime
        container = CDMIContainer.new( @server )
        body = {}
        body[ 'metadata' ] = JSON.parse( "{cdmi_nfs_export : true}" );
        body[ 'exports'  ] = JSON.parse( @export );
        result = container.CREATE( @container, body )
     end
     
     def export()
        utils = CDMIUtils.new( @server )
        if ( utils.isNFSSupported( @container ) )
            LOG.echo( "TesterExporter", "server '%s' supports NFS"%@server )
            LOG.echo( "TesterExporter", "using simulated export-nfs-information..." )
            info = utils.getNFSInformation( @container, true )
            LOG.echo( "TesterExporter", "nfs-information: %s"%info )
        end
     end
     
     def delete()
        container = CDMIContainer.new( @server )
        result = container.DELETE( @container )
        LOG.echo( "TesterExporter::delete", result )
	 end
	 
	 def run
	    create()
        export()
        delete()
	 end
end

begin
  test = TesterExporter.new()
  test.run()
end	
