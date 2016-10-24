 
# Pulling the testCase driver from the test case
for filename in testCases/*; do
	
	driver=$(sed -n '7p' "$filename")
	input=$(sed -n '5p' "$filename")
	desc=$(sed -n '2p' "$filename")
	num=$(sed -n '1p' "$filename")
	oracle=$(sed -n '6p' "$filename")
	echo "This is test case number: $num" 
	echo "Testing $driver with the input of: $input"
	echo "This will be: $desc"
	# Compile the driver
javac -cp ./project ./testCasesExecutables/testCasePackage/$driver.java

# Run the driver - Needs to get variable name from textfile
java -cp ./project/Libraries/martus.jar:./testCasesExecutables testCasePackage.$driver $input $oracle
done


