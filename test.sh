JAVA_COMMAND="java -Dfile.encoding=UTF-8 -jar target/javagit-1.0-SNAPSHOT-jar-with-dependencies.jar"

echo test 1
$JAVA_COMMAND cat-file eec107d2c3e259ed4bdd509c7bb5346da41607f4
echo
echo test 2
$JAVA_COMMAND cat-file 6433b6766d8372901881148308f0d000c8c416f8
echo
echo test 3
$JAVA_COMMAND cat-file -p be42fc666262908364880b2c108ec02597d8b54a
echo
echo test 4
$JAVA_COMMAND cat-file -p e22339445c0e1adfaaa55945569e992b3585812f
echo
echo test 5
echo foobar | $JAVA_COMMAND hash-object
echo foobar | git hash-object --stdin
echo
echo test 6
$JAVA_COMMAND log a97362447b400e970ad0f817a19e991ef8f0cc5b
