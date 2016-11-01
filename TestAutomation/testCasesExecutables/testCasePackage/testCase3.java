package testCasePackage;
import org.martus.clientside.PasswordHelper;

public class testCase3 {
	public static void main(String args[]){
		String [] test = args[0].split(",");
		System.out.println(test[0].toString());
		System.out.println(test[1].toString());
		PasswordHelper.getCombinedPassPhrase(test[0],test[1].toCharArray());
		
	}
}

