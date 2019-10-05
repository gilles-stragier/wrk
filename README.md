Overview
--------

This is a fork of Wrk, a command line interface to Trello.

This version has some improvements :
* Gradle is used as a build tool
* Modern java versions are supported (tested with 11)


Installation
--------------

Clone the git project to some directory, and build it :

```
$ ./gradlew installDist

```

Then include then bin directtory in your path, and define the following variables :

* `JAVA_HOME` -> (likely already set by your distro) set to the home directory of the java installation
* `WRK_HOME` -> set to the directory of where ever you untar-ed wrk (i.e., `/opt/wrk`).

Then, create file `~/.wrk/token` and save in it your Trello token.  
Finally, create file `~/.wrk/usrkey` and put your application key in it.
 
For help in creating a token see [here](https://trello.com/docs/gettingstarted/index.html#getting-a-token-from-a-user).

Usage
-------

`wrk --help`
`wrk help <command>`

Trello Id v. Wrk Id
--------

All commands work with both a Trello Id (a long unique identifier provided by Trello) or a wrk generated id.  The wrk generated id is a short id starting with `wrk` and is meant to be used to chain operations together fluidly without having to copy and paste output.  By illustration, compare the two equivalent chains of commands:

Using Trello ids:

    wrk orgs
    # copy and paste organization id from output of last command
    wrk boards in varickbergen
    # copy and paste board id from output of last command
    wrk cards in b:4feda1809693a8f66051de39
    # copy and paste board id from output of 2 commands previous
    wrk cards in b:4fed9292f29a10bc3b4077ec
    
![trello ids](https://github.com/blangel/wrk/raw/master/docs/img/trello-id.png "trello ids")

Using wrk ids:

    wrk orgs
    wrk boards in wrk1
    wrk cards in wrk5
    wrk pop
    wrk cards in wrk2

![wrk ids](https://github.com/blangel/wrk/raw/master/docs/img/wrk-id.png "wrk ids")

Color/Editor Configuration
------

To modify default color settings or the default editor see file `~/.wrk/config`
