REM Constructs classpath and starts BCup server
REM Raivo Laanemets

@SET CP=dist/bcup-common.jar
@SET CP=%CP%;dist/bcup-server.jar
@SET CP=%CP%;lib/server/apache-log4j-extras-1.0.jar
@SET CP=%CP%;lib/server/backport-util-concurrent-3.1.jar
@SET CP=%CP%;lib/server/commons-collections-3.2.jar
@SET CP=%CP%;lib/server/commons-dbcp-1.2.2.jar
@SET CP=%CP%;lib/server/commons-logging-1.1.1.jar
@SET CP=%CP%;lib/server/commons-pool-1.3.jar
@SET CP=%CP%;lib/server/ehcache-1.5.0.jar
@SET CP=%CP%;lib/server/jsr107cache-1.0.jar
@SET CP=%CP%;lib/server/log4j-1.2.15.jar
@SET CP=%CP%;lib/server/mysql-connector-java-5.1.6.jar
@SET CP=%CP%;lib/server/spring-2.5.6.jar
@SET CP=%CP%;lib/server/spring-modules-cache.jar

@SET CP=%CP%;lib/client/commons-jexl-2.0.jar
@SET CP=%CP%;lib/client/gson-1.4.jar

java -cp %CP% ee.pri.bcup.server.Startup