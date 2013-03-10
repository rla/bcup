bcup-pool
=========

Half-finished pool game server/applet. The project was started many years ago
but was then abandoned. As the technology is obsolete now (Java Applets) I am
releasing the project as Open Source.

All proprietary trademarks and graphics have been removed or replaced.

The game features pool games between 2 players and spectactors/observers.
Realistic ball physics including spin is simulated.

The server side uses classical thread per user model and uses lock-free
data structures extensively.

Both the server and the client side use annotation-based pub/sub framework.

The game was originally developed in Estonian. The English localization was added
afterwards and might be incomplete.

Building
--------

Building requires the ANT build tool and JDK 1.6. The following command builds
server and client components as .jar files:

    ant jar

Using with IDE
--------------

The project uses Lombok for code generation (getters/setters). Eclipse will need plugin to work
with the straight source: http://projectlombok.org/.
    
Running
-------

To start the server (after building):

    ./startServer.sh
    
To run clients (this will run 2 test clients):

    ant run2

There are also deployment instructions in Estonian. See doc/paigaldusjuhend.pdf.

License
-------

    The MIT License (MIT)
    Copyright (c) 2013 Raivo Laanemets
    
    Permission is hereby granted, free of charge, to any person obtaining a copy of this software
    and associated documentation files (the "Software"), to deal in the Software without restriction,
    including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
    subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all copies or substantial
    portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
    THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
