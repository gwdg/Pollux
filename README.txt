/**
 * Copyright (c) 2011, Pollux
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Pollux
 * 	  nor the names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Pollux
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author         Miguel Rojas (miguel.rojas@uni-dortmund.de)
 * @version        1.0
 * @lastrevision   28.11.2011
 */

OCCI-CDMI Client :: Pollux
===========================

The OCCI-CDMI Client aims to offer a tool for testing 
an OCCI Server in association with a CDMI Client for 
providing infrastructure services through creating of 
resources such as Networks, Storages and Virtual Machines.
 
All components of this client are developed in Java 1.6
with Spring MVC to offer an extensible and flexible tool. 
This client, code-named "Pollux", can be accessed via Web
or can be reused separatelly from stand alone applications
as maven artifacts.

MAIN COMPONENTS
===============
//TODO:
OCCI Artifact
CDMI Artifact
Web Artifact

PRE-REQUISITES
==============
The Client Pollux requires following products installed in your computer:

.  Java Environment JDK 1.6   			(source:  http://www.oracle.com/technetwork/java/index.html)
.  Maven 3.0.3                			(source:  http://maven.apache.org/download.html)
.  Virgo Eclipse Server (tomcat-based)  (source:  http://www.eclipse.org/virgo/download )

All Third-party libraries required by the Client will be downloaded automatically by Maven once you 
try to compile the whole project.

BUILDING POLLUX
===============

Download
---------
The code of Pollux is stored into a Github repository
and can be downloaded from the location:
 https://github.com/gwdg/Pollux

Compilation
------------
The Pollux client is a maven-based project which makes 
use of several maven plugins for its building and deployment.

Assuming the root path of Pollux source code is %POLLUX_HOME%, following commands are available
for building the project:

* maven compile:   %POLLUX_HOME%/mvn compile
* maven install:   %POLLUX_HOME%/mvn install
* maven rebuild:   %POLLUX_HOME%/mvn clean compile install

*** Pollux deployment under Virgo:   %POLLUX_HOME%/deploy.bat

Deployment
-----------

CONFIGURATION
=============

Virgo Eclipse Server
---------------------
One of the flavors of Virgo Eclipse Server is based on Tomcat. The configuration of Tomcat web server can be located
under %VIRGO_INSTALLATION_DIRECTORY%/config/tomcat-server.xml

STARTING-UP POLLUX
==================

starting web server:   %VIRGO_INSTALLATION_DIRECTORY%/bin/startup.bat -clean 
debugging pollux:   %VIRGO_INSTALLATION_DIRECTORY%/bin/startup.bat -clean -debug

end point reference of pollux :  http://localhost:8080/cloud/app/login.htm
