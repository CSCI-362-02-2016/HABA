#Create header of result table
echo "<table border ="1px">
	<tr><th>TestCase Number</th><th>Method</th><th>Input</th><th>Expected Output</th><th>Actual Output</td><th>Oracle Pass/Fail</th></tr>
	" > ./reports/report.html

# Pulling the testCase driver from the test case
for filename in testCases/*; do
	
	numTest=$(sed -n '9p' "$filename")
	desc=$(sed -n '10p' "$filename")
	component=$(sed -n '11p' "$filename")
	method=$(sed -n '12p' "$filename")
	input=$(sed -n '13p' "$filename")
	expected=$(sed -n '14p' "$filename")
	driver=$(sed -n '15p' "$filename")
	# oracle=$(sed -n '16p' "$filename")

	# Compile the driver
	# Perhaps should only compile the files once
	javac -cp ./project ./testCasesExecutables/testCasePackage/$driver.java
	# javac -cp ./project ./oracles/$oracle.java


	# Run the driver
	resultSet=$(java -cp ./project/Libraries/martus.jar:./testCasesExecutables testCasePackage.$driver $input)
	# resultSet2=$(java -cp ./project/Libraries/martus.jar:./oracles $oracle $input $expected)

	# Run Oracle and store the results	

	echo "___________________________________________________"	
	echo "Test results for the Test Case Number: $numTest"
	echo "Description:		$desc"
	echo "Driver File:		$driver"
	echo "Oracle File:		$oracle"
	echo "Input Arguments:	$input"
	echo "Expected Output:	$expected"
	echo "Actual Output:		$resultSet"
	
	if [ $resultSet "==" $expected ]; then
	outcome="pass"
	echo "Oracle Result:          	$outcome"
	else
	outcome="fail"
	echo "Oracle Result:		$outcome"
	
	# Add results to the table
	echo "<tr><td>$numTest</td><td>$method</td><td>$input</td><td>$expected</td><td>$resultSet</td><td>$outcome</td></tr>" >> ./reports/report.html

done 

# Open the reports file

xdg-open ./reports/report.html

