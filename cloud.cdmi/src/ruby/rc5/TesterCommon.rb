require './LOG.rb'
require './CDMICommon.rb'

class TesterCommon
     def initialize()
     end
     
     def self.readCapabilities()
        common = CDMICommon.new( "http://129.217.252.37:2364/" )
        result = common.READcapabilities()
        LOG.echo( "Tester", result[ 0 ] ) 
        result[ 1 ].each { |item|
           LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                
        result[ 2 ].each { |item|
           LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
     
     def self.readCapabilitiesWithParams()
        common = CDMICommon.new( "http://129.217.252.37:2364/" )
        result = common.READcapabilities( "objectURI;capabilities" )
        LOG.echo( "Tester", result[ 0 ] ) 
        result[ 1 ].each { |item|
           LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                       
        result[ 2 ].each { |item|
           LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
     
     def self.readCapabilitiesWithField()
        common = CDMICommon.new( "http://129.217.252.37:2364/" )
        result = common.READcapabilities( "objectURI" )
        LOG.echo( "Tester", result[ 0 ] )
        result[ 1 ].each { |item|
           LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                       
        result[ 2 ].each { |item|
           LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
end

begin
  TesterCommon.readCapabilities()
  #TesterCommon.readCapabilitiesWithParams()
  #TesterCommon.readCapabilitiesWithField()
end	
