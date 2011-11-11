require './LOG.rb'
require './NonCDMIDataObject.rb'

class TesterNonDataObject
     def initialize()
     end
     
     def self.read()
        dataobject = NonCDMIDataObject.new( "http://129.217.252.37:2364", "/WubiClub" )
        result = dataobject.READ( "spielregeln-rumiQ.pdf", "/home/gsikora/workspace/mrQ.pdf" )
        LOG.echo( "Tester", result[ 0 ] ) 
        result[ 1 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                
        result[ 2 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
     
     def self.create()
        dataobject = NonCDMIDataObject.new( "http://129.217.252.37:2364/", "WubiClub" )
        result = dataobject.CREATE( "GFD.185.pdf", "/home/gsikora/workspace/GFD.185.pdf", "application/pdf" )
        LOG.echo( "Tester", result[ 0 ] ) 
        result[ 1 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                
        result[ 2 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
     
     def self.delete()
        dataobject = NonCDMIDataObject.new( "http://129.217.252.37:2364/", "WubiClub" )
        result = dataobject.DELETE( "GFD.185.pdf" )
        LOG.echo( "Tester", result[ 0 ] ) 
        result[ 1 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
                
        result[ 2 ].each { |item|
          LOG.echo( "Tester", item[ 0 ]+("=%s"%item[1]) ) }
     end
     
end

begin
  #TesterNonDataObject.read()
  #TesterNonDataObject.create()
  #TesterNonDataObject.delete()
end	
