javac -cp "lib/*:." */*.java
mkdir -p SciTK
mv */*.class SciTK
mkdir -p lib_unpack
cp lib/*.jar lib_unpack
cd lib_unpack
for i in *; do jar xf $i; done
rm *.jar
cd ..
mv lib_unpack/com .
mv lib_unpack/org .
mv lib_unpack/META-INF .
cp ../LICENSE .
cp lib/LICENSE* .
jar cvf SciTK.jar SciTK com org resources LICENSE LICENSE* > jar.log
rm -r SciTK
rm -r lib_unpack
rm -r com
rm -r org
rm -r META-INF
rm LICENSE*