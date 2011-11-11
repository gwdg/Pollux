require './LOG.rb'
require './CDMIContainer.rb'

class TesterContainer
     def initialize()
     end
     
     def self.create()
        container = CDMIContainer.new( "http://129.217.252.37:2364/" )
        result = container.CREATE( "WubiClub" )
        LOG.echo( "Tester::create-(0)", result[ 0 ] ) 
        result[ 1 ].each { |item|
			LOG.echo( "Tester::create-(1)", item[ 0 ]+("=%s"%item[1]) ) }
         
        result[ 2 ].each { |item|
           LOG.echo( "Tester::create-(2)", item ) }
     end
     
     def self.readX()
        container = CDMIContainer.new( "http://129.217.252.37:2364/" )
        result = container.READ( "WubiClub" )
        LOG.echo( "Tester::readX-(0)", result[ 0 ] ) 
        result[ 1 ].each { |item|
           LOG.echo( "Tester::readX-(1)", item[ 0 ]+("=%s"%item[1]) ) }
        result[ 2 ].each { |item|
           LOG.echo( "Tester::readX-(2)", item ) }
     end
     
     def self.update()
        container = CDMIContainer.new( "http://129.217.252.37:2364/" )
        result = container.READ( "WubiClub", "objectID;objectURI" )
        LOG.echo( "Tester::update-(0)", result[ 0 ] ) 
        result[ 1 ].each { |item|
           LOG.echo( "Tester::update-(1)", item[ 0 ]+("=%s"%item[1]) ) }
         
        result[ 2 ].each { |item|
           LOG.echo( "Tester::update-(2)", item ) }
     end
     
     def self.readY()
        container = CDMIContainer.new( "http://129.217.252.37:2364/" )
        result = container.READ( "WubiClub", "objectID;objectURI" )
        LOG.echo( "Tester::readY-(0)", result[ 0 ] ) 
        result[ 1 ].each { |item|
           LOG.echo( "Tester::readY-(1)", item[ 0 ]+("=%s"%item[1]) ) }
        result[ 2 ].each { |item|
           LOG.echo( "Tester::readY-(2)", item ) }
     end
     
     def self.delete()
        container = CDMIContainer.new( "http://129.217.252.37:2364/" )
        result = container.DELETE( "WubiClub" )
        LOG.echo( "Tester::delete", result )
	 end
end

begin
  TesterContainer.create()
  TesterContainer.readX()
  TesterContainer.update()
  TesterContainer.readY()
  TesterContainer.delete()
end	
