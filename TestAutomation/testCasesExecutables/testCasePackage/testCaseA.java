package testCasePackage;

import org.martus.client.core.MartusUserNameAndPassword;

public class testCaseA {
	// pass input which comes from the script file
	// Perhaps pass in the Oracle
	public boolean drive(String input){ 
		return MartusUserNameAndPassword.isWeakPassword(input.toCharArray());
		
	}
	public static void main(String args[]){
		System.out.println(MartusUserNameAndPassword.isWeakPassword(args[0].toCharArray()));
	}
}
