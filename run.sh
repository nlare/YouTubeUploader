#!/bin/bash
java -classpath :./lib/* com/uploader/AppGui -Dhttp.proxyHost=192.168.0.102 -Dhttp.proxyPort=8118 -Dhttp.nonProxyHosts="127.0.0.1|localhost"
#java -jar App.jar
