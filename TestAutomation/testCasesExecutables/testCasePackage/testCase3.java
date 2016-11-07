package testCasePackage;
import org.martus.clientside.PasswordHelper;

public class testCase3 {
	public static void main(String args[]){
		String [] test = args[0].split(",");
		try{
			System.out.println(PasswordHelper.getCombinedPassPhrase(test[0],test[1].toCharArray()));
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("ArrayIndexOutOfBoundsException");
		}

		
	}
}

