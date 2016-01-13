JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        Proxy.java \
        Servidor.java \
        VerificaStrings.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class


