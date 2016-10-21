package testCasePackage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;

import org.martus.client.core.MartusUserNameAndPassword;

// To compile in terminal:
// javac -cp ../../project testDriver.java
// java -cp ../project/Libraries/martus.jar:../testCasesExecutables testCasePackage.testDriver

public class testDriver {
	private static ArrayList<String> lines = new ArrayList<String>();
	
//	private static void readFile(String fileName){
//		String line = null;
//		String dir = System.getProperty("user.dir");
//		try {
//            // FileReader reads text files in the default encoding.
//            FileReader fileReader = new FileReader(dir + "/testCases/" +fileName);
//
//            // Always wrap FileReader in BufferedReader.
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//            while((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
//                lines.add(line);
//            }   
//
//            // Always close files.
//            bufferedReader.close();         
//        }
//        catch(FileNotFoundException ex) {
//            System.out.println(
//                "Unable to open file '" + 
//                fileName + "'");                
//        }
//        catch(IOException ex) {
//            System.out.println(
//                "Error reading file '" 
//                + fileName + "'");                  
//            // Or we could just do this: 
//            // ex.printStackTrace();
//        }
//	}
	
	public static void main(String[] args){
		MartusUserNameAndPassword f = new MartusUserNameAndPassword();
		
		//System.out.println(f.getClass().getName().toString());
		Method methodList[] = null;
		try{
		//Class cls = Class.forName(f.getClass().getName().toString());
		Class cls2 = Class.forName("org.martus.client.core.MartusUserNameAndPassword");

		methodL ist = cls2.getDeclaredMethods();
		//Method methodList2[] = f.getClass().getDeclaredMethods();
		System.out.println("Class: + " + methodList[0].getDeclaringClass());
		for(int i = 0; i < methodList.length; i++){
			System.out.println("Method " + methodList[i].getName());
		}
		// Test case read from file will go below
		// Sample run
		char[] password = "123456789012356^%$*".toCharArray();
		if(MartusUserNameAndPassword.isWeakPassword(password) == false){
			System.out.println("False, the password is not weak");
		}
		if(MartusUserNameAndPassword.isWeakPassword(password) == true){
			System.out.println("True, the password is weak");
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		// TestCase1 Read-In
		String fileName = "testCase1.txt";
		readFile(fileName);
		if(lines.get(0).contains("A")){
			System.out.println("Method is : " + methodList[2].getName());
			// run testCase1 which is the isWeakPassword test cases
			// System.out.println(lines.get(5));
			testCase1 t = new testCase1();
			// pass answer to Oracle then print result
			System.out.println(lines.get(5));
			t.answer(lines.get(3), lines.get(4), lines.get(5));
		}
		
		
	}
}
