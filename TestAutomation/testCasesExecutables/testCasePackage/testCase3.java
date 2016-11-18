package testCasePackage;
import org.martus.clientside.PasswordHelper;
import java.util.InputMismatchException;

public class testCase3 {
	public static void main(String args[]){
		String [] test = args[0].split(",");
		if(args.length == 0) {
			System.out.println("EmptyArgument");
			System.exit(0);
		}
		try{
			System.out.println(PasswordHelper.getCombinedPassPhrase(test[0],test[1].toCharArray()));
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("ArrayIndexOutOfBoundsException");
		}catch(InputMismatchException e1){
			System.out.println("InputMismatchException");
		}

		
	}
}

