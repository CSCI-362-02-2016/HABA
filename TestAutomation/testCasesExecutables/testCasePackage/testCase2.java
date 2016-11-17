package testCasePackage;

import org.martus.client.core.MartusUserNameAndPassword;
import org.martus.common.Exceptions.BlankUserNameException;
import org.martus.common.Exceptions.PasswordMatchedUserNameException;
import org.martus.common.Exceptions.PasswordTooShortException;

public class testCase2 {
	public static void main(String args[]){
		if(args.length == 0) {
			System.out.println("EmptyArgument");
			System.exit(0);
		}
		String [] test = args[0].split(",");

	    try {
			MartusUserNameAndPassword.validateUserNameAndPassword(test[0], test[1].toCharArray());
			System.out.println("NoError");
		} catch (BlankUserNameException e) {
			System.out.println("BlankUserNameException");
		} catch (PasswordMatchedUserNameException e) {
			System.out.println("PasswordMatchedUserNameException");
		} catch (PasswordTooShortException e) {
			System.out.println("PasswordTooShortException");
		}
		// System.out.println("The result is: " + result + " and the submitted was " + args[1]);
	}
}

