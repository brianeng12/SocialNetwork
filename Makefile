JFLAGS = -g
JC = javac
JVM= java 
FILE=

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        FindConnectController.java \
        FindConnectView.java \
        Graph.java \
        GraphADT.java \
        Main.java \
        package-info.java \
        Person.java \
        SocialNetwork.java \
        SocialNetworkADT.java

MAIN = Main 

default: classes

classes: $(CLASSES:.java=.class)

run: classes
	$(JVM) $(MAIN) $(FILE)

clean:
	$(RM) *.class
jar:
	jar -cvmf manifest.txt executable.jar .
