 # Display all the files within the testCase folder

for filename in testCases/*.txt; do
  echo "$filename"
done


# Pulling the testCase driver from the test case
for filename in testCases/*; do
	
	driver=$(sed -n '1p' "$filename")
	input=$(sed -n '6p' "$filename")
	echo "Testing $driver with the input of: $input"
	# Compile the driver
javac -cp ./project ./testCasesExecutables/testCasePackage/$driver.java

# Run the driver - Needs to get variable name from textfile
java -cp ./project/Libraries/martus.jar:./testCasesExecutables testCasePackage.$driver $input
	echo $variable
done


