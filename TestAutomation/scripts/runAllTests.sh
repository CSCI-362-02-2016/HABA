# 
#

# Compile the driver
javac -cp ../project /home/hugo/workspace/TestAutomation/testCasesExecutables/testCasePackage/testDriver.java

# Run the driver
java -cp ../project/Libraries/martus.jar:../testCasesExecutables testCasePackage.testDriver

