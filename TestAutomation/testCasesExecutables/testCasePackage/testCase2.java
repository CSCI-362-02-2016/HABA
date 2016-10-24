package testCasePackage;

import org.martus.client.core.MartusUserNameAndPassword;
import org.martus.common.Exceptions.BlankUserNameException;
import org.martus.common.Exceptions.PasswordMatchedUserNameException;
import org.martus.common.Exceptions.PasswordTooShortException;

public class testCase2 {
	public static void main(String args[]){
		String [] test = args[0].split(",");
		//System.out.println(test[0].toString());
		//System.out.println(test[1].toString());
		String result; 
	    try {
			MartusUserNameAndPassword.validateUserNameAndPassword(test[0], test[1].toCharArray());
			// System.out.println("NoError");
			result = "NoError";
		} catch (BlankUserNameException e) {
			// System.out.println("BlackUserNameException");
			result = "BlankUserNameException";
		} catch (PasswordMatchedUserNameException e) {
			// System.out.println("PasswordMatchedUserNameException");
			result = "PasswordMatchedUserNameException";
		} catch (PasswordTooShortException e) {
			// System.out.println("PasswordTooShortException");
			result = "PasswordTooShortException";
		}
		// System.out.println("The result is: " + result + " and the submitted was " + args[1]);
		if(result.equals(args[1])){
			System.out.println("Oracle Test passed");
		}else{
			System.out.println("Oracle Test failed");
		}
	}
}

