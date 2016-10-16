package testCasePackage;
import java.lang.reflect.*;
import org.martus.client.core.MartusUserNameAndPassword;

// To compile in terminal:
// javac -cp ../../project testDriver.java
// java -cp ../project/Libraries/martus.jar:../testCasesExecutables testCasePackage.testDriver

public class testDriver {
	
	public static void main(String[] args){
		MartusUserNameAndPassword f = new MartusUserNameAndPassword();
		
		//System.out.println(f.getClass().getName().toString());
		
		try{
		//Class cls = Class.forName(f.getClass().getName().toString());
		Class cls2 = Class.forName("org.martus.client.core.MartusUserNameAndPassword");

		Method methodList[] = cls2.getDeclaredMethods();
		//Method methodList2[] = f.getClass().getDeclaredMethods();
		System.out.println("Class: + " + methodList[0].getDeclaringClass());
		for(int i = 0; i < methodList.length; i++){
			System.out.println("Method " + methodList[i].getName());
		}
		// Test case read from file will go below
		// Sample run
		char[] password = "test".toCharArray();
		if(MartusUserNameAndPassword.isWeakPassword(password) == false){
			System.out.println("False");
		}
		if(MartusUserNameAndPassword.isWeakPassword(password) == true){
			System.out.println("True");
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
