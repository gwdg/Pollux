require './LOG.rb'
require './CDMIDataObject.rb'

class TesterDataObject
     def initialize()
     end
     
     def self.readX()
        dataobject = CDMIDataObject.new( "http://129.217.252.37:2364/", "Gerd" )
        result = dataobject.READ( "myfile.text" )
        LOG.echo( "Tester", result[ 0 ] ) 
        result[ 1 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                
        result[ 2 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
     
     def self.readY()
        dataobject = CDMIDataObject.new( "http://129.217.252.37:2364/", "Gerd" )
        result = dataobject.READ( "graph.png", "mimetype;objectID;objectURI" )
        LOG.echo( "Tester", result[ 0 ] ) 
        result[ 1 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                
        result[ 2 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
     
     def self.readZ()
        dataobject = CDMIDataObject.new( "http://129.217.252.37:2364/", "Gerd" )
        result = dataobject.READ( "index.txt" )
        LOG.echo( "Tester", result[ 0 ] ) 
        result[ 1 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                
        result[ 2 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
     
     def self.create()
        dataobject = CDMIDataObject.new( "http://129.217.252.37:2364/", "WubiClub" )
        result = dataobject.CREATE( "wubi.txt" )
        LOG.echo( "Tester", result[ 0 ] ) 
        result[ 1 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                
        result[ 2 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
     
     def self.readBIN()
        dataobject = CDMIDataObject.new( "http://129.217.252.37:2364/", "Gerd" )
        result = dataobject.binREAD( "graph.png" )
        LOG.echo( "Tester", result[ 0 ] ) 
        result[ 1 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                
        result[ 2 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
end

begin
  #TesterDataObject.readX()
 # TesterDataObject.readY()
  #TesterDataObject.readZ()
  TesterDataObject.create()
  #TesterDataObject.readBIN()
end	
