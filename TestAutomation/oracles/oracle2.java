import org.martus.client.core.MartusUserNameAndPassword;
import org.martus.common.Exceptions.BlankUserNameException;
import org.martus.common.Exceptions.PasswordMatchedUserNameException;
import org.martus.common.Exceptions.PasswordTooShortException;

public class oracle2 {
	public static void main(String args[]){
		String [] test = args[0].split(",");
		String result; 
	    try {
			MartusUserNameAndPassword.validateUserNameAndPassword(test[0], test[1].toCharArray());
			result = "NoError";
		} catch (BlankUserNameException e) {
			result = "BlackUserNameException";
		} catch (PasswordMatchedUserNameException e) {
			result = "PasswordMatchedUserNameException";
		} catch (PasswordTooShortException e) {
			result = "PasswordTooShortException";
		}
		// System.out.println("The result is: " + result + " and the submitted was " + args[1]);
		if(result.equals(args[1])){
			System.out.println("Passed");
		}else{
			System.out.println("Failed");
		}
	}
}

