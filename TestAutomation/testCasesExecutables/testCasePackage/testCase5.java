package testCasePackage;

import org.martus.common.utilities.MartusFlexidate;

public class testCase5 {
	public static void main(String args[]){
		try{
		String[] pos = args[0].split(",");
		int pos2 = Integer.valueOf(pos[1]);
		MartusFlexidate mf = new MartusFlexidate(pos[0], pos2); 
		System.out.println( mf.getMartusFlexidateString());
		}catch(NumberFormatException e1){
			System.out.println("NumberFormatException");
		}catch(RuntimeException e2){
			System.out.println("RuntimeException");
		}
	}
}

