JAVABIN=
#JAVABIN=/usr/lib/jvm/oracle-java10-jdk-amd64/bin/
#JAVANIB=/usr/lib/jvm/java-10-openjdk-amd64/bin/
#JAVABIN=/usr/lib/jvm/oracle-java9-jdk-amd64/bin/
JAVA=$(JAVABIN)java $(JAVAFLAGS)
JAVAC=$(JAVABIN)javac $(JAVACFLAGS)
JAVADOC=$(JAVABIN)javadoc $(JAVADOCFLAGS)
FIND=find
MKDIR=mkdir -p 
MAKE=make
RM=rm -rf

CLASSPATH=bin:lib/*
JAVACFLAGS= -encoding utf8 -d bin -sourcepath src -cp $(CLASSPATH) -Xlint:deprecation -Xdiags:verbose -Xlint:unchecked
JAVAFLAGS = -cp $(CLASSPATH):resource --illegal-access=deny
#JAVAFLAGS = -cp $(CLASSPATH):resource
#JAVADOCPROXY= -J-Dhttp.proxyHost=proxy.csc.titech.ac.jp -J-Dhttp.proxyPort=8080
JAVADOCPROXY=
JAVADOCFLAGS= -html5 -encoding utf-8 -charset utf-8 -package -d javadoc -sourcepath src $(JAVADOCPROXY) -link https://docs.oracle.com/javase/jp/10/docs/api -classpath $(CLASSPATH)

SERVADDR=localhost
NUMBER=`echo \`whoami\`0 | md5 | sed -e "s/[^0-9]//g" |cut -c 1-6`
PARAMETER=$(SERVADDR)

.PHONY: clean javadoc

.SUFFIXES: .class .java

.java.class:
	$(MKDIR) bin

.class:
	$(eval CLS := $(subst /,.,$(@:src/%=%)))
#	$(eval R := $(shell $(JAVAC) $@.java 2>&1))
	RES=`$(JAVAC) $@.java 2>&1` ; \
	if [ -z "$$RES" ]; then \
		$(JAVA) $(CLS)  $(PARAMETER); \
	else \
		echo "$$RES"; \
	fi;

ALL::	Main15

Main15:
	$(MAKE) src/para/$@

Main16:
	$(MAKE) src/para/$@

Main17:
	$(MAKE) src/para/$@

Main18:
	$(MAKE) src/para/$@

Main19:
	$(MAKE) src/para/$@

Main20:
	$(MAKE) src/para/$@

Main21:
	$(MAKE) src/para/$@

Main22:
	$(MAKE) src/para/$@

Main23:
	$(MAKE) src/para/$@

Main24:
	$(MAKE) src/para/$@

Game04:
	$(MAKE) src/para/$@

GameServer01:
	$(MAKE) src/para/$@

clean:
	$(RM) bin javadoc

javadoc::
	$(FIND) . -name "._*" -exec $(RM) {} \;
	$(MKDIR) javadoc
	cp -R lib/javadoc/easycl javadoc
	$(JAVADOC) para para.graphic.shape para.graphic.target para.graphic.parser para.graphic.camera para.graphic.opencl para.opencl
