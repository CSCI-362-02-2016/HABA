package testCasePackage;

import org.martus.client.core.MartusUserNameAndPassword;
import org.martus.common.Exceptions.BlankUserNameException;
import org.martus.common.Exceptions.PasswordMatchedUserNameException;
import org.martus.common.Exceptions.PasswordTooShortException;

public class testCase2 {
	public static void main(String args[]){
		String [] test = args[0].split(",");
		System.out.println(test[0].toString());
		System.out.println(test[1].toString());
	    try {
			MartusUserNameAndPassword.validateUserNameAndPassword(test[0], test[1].toCharArray());
			System.out.println("No Error");
		} catch (BlankUserNameException e) {
			System.out.println("BlackUserNameException");
			e.printStackTrace();
		} catch (PasswordMatchedUserNameException e) {
			// TODO Auto-generated catch block
			System.out.println("PasswordMatchedUserNameException");
		} catch (PasswordTooShortException e) {
			// TODO Auto-generated catch block
			System.out.println("PasswordTooShortException");
		}
	}
}

