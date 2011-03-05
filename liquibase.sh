#!/bin/bash
~/Downloads/liquibase-2/liquibase \
	--classpath=\
/Users/wiktor/.m2/repository/org/hibernate/hibernate-core/3.6.1.Final/hibernate-core-3.6.1.Final.jar:\
/Users/wiktor/.m2/repository/postgresql/postgresql/9.0-801.jdbc4/postgresql-9.0-801.jdbc4.jar:\
/Users/wiktor/.m2/repository/org/hibernate/hibernate-commons-annotations/3.2.0.Final/hibernate-commons-annotations-3.2.0.Final.jar:\
/Users/wiktor/.m2/repository/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:\
/Users/wiktor/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:\
/Users/wiktor/.m2/repository/org/liquibase/ext/liquibase-hibernate/2.0.0/liquibase-hibernate-2.0.0.jar:\
/Users/wiktor/.m2/repository/org/glassfish/javax.annotation/3.1-b21/javax.annotation-3.1-b21.jar:\
/Users/wiktor/.m2/repository/org/hibernate/javax/persistence/hibernate-jpa-2.0-api/1.0.0.Final/hibernate-jpa-2.0-api-1.0.0.Final.jar:\
/Users/wiktor/Code/X/target/classes \
	--driver=org.postgresql.Driver \
	--url=jdbc:postgresql://localhost/axir_devel \
	--username=axir \
	--password=petula87112 \
diffChangeLog \
	--referenceUrl=hibernate:src/main/resources/hibernate.cfg.xml