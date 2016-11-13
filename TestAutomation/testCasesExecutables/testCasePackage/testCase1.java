package testCasePackage;

import org.martus.client.core.MartusUserNameAndPassword;

public class testCase1 {
	public static void main(String args[]){
		if(args.length == 0) {
			System.out.println("EmptyArgument");
			System.exit(0);
		}
		System.out.println(MartusUserNameAndPassword.isWeakPassword(args[0].toCharArray()));
	}

}
