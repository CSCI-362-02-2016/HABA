#Create Array to store drivers to compile list
Array=()
$count
#Create header of result table
echo "<table border ="1px">
	<tr><th>TestCase Number</th><th>Method</th><th>Input</th><th>Expected Output</th><th>Actual Output</td><th>Pass/Fail</th></tr>
	" > ./reports/report.html

# Collect all the driver names from the testCase files
for filename in testCases/*; do
	driver=$(sed -n '15p' "$filename")
	# Add individual drivers to an array with no duplicates
	if [[ "${Array[@]}" != *$driver* ]]
	    then Array+=($driver);
	fi
done

# Compile those scrapped drivers
for driver in "${Array[@]}"
do
count=$[count + 1];
  javac -d ./bin/ -cp ./project/:./project/Libraries/* ./testCasesExecutables/testCasePackage/$driver.java
	 echo -ne 'Compling drivers    			Progess:' $((100* $count/${#Array[@]}))'%\r'| sed 's/..$/&/' 
done

# Executing the testCase files and associated drivers
for filename in testCases/*; do
	
	numTest=$(sed -n '9p' "$filename")
	desc=$(sed -n '10p' "$filename")
	component=$(sed -n '11p' "$filename")
	method=$(sed -n '12p' "$filename")
	input=$(sed -n '13p' "$filename")
	expected=$(sed -n '14p' "$filename")
    driver=$(sed -n '15p' "$filename")

	# Run the driver
	resultSet=$(java -cp ./bin:./project/Libraries/martus.jar:./testCasesExecutables testCasePackage.$driver $input)

	echo "___________________________________________________"	
	echo "Test results for the Test Case Number: $numTest "
	echo "Description:		$desc"
	echo "Driver File:		$driver"
	echo "Input Arguments:	$input"
	echo "Expected Output:	$expected"
	echo "Actual Output:		$resultSet"
	
	if [ $resultSet "==" $expected ]; then
	outcome="Pass"
	echo "Oracle Result:          	$outcome"
	else
	outcome="Fail"
	echo "Oracle Result:		$outcome"
	fi
	# Add results to the table
	echo "<tr><td><a href = "file://${PWD}/testCases/testCase$numTest.txt" > $numTest </a></td><td>$method</td><td>$input</td><td>$expected</td><td>$resultSet</td><td>$outcome</td></tr>" >> ./reports/report.html

done 


# Open the reports file
xdg-open ./reports/report.html



