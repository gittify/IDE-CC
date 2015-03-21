#/bin/sh
cd src
javac ./com/data/insight/WordCount.java
echo "$sourceDir"
java com.data.insight.WordCount "../wc_input" "../wc_output/wc_result.txt" "../wc_output/med_result.txt"
