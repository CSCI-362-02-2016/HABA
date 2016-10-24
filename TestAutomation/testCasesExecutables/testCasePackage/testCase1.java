package testCasePackage;

import org.martus.client.core.MartusUserNameAndPassword;

public class testCase1 {
	// pass input which comes from the script file
	// Perhaps pass in the Oracle
	public static void main(String args[]){
		System.out.println(MartusUserNameAndPassword.isWeakPassword(args[0].toCharArray()));
		boolean value = Boolean.valueOf(args[1]);
		if(MartusUserNameAndPassword.isWeakPassword(args[0].toCharArray()) == value){
			System.out.println("Oracle Test passed");
		}else{
			System.out.println("Oracle Test failed");
		}
	}

}
