cp ../toolkit/SciTK.jar .
rm *.class
javac -cp "SciTK.jar:." Analysis.java
java -cp "SciTK.jar:." Analysis