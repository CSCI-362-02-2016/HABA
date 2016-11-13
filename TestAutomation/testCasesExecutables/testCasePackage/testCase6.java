package testCasePackage;
import org.martus.common.utilities.MartusFlexidate;
import org.martus.util.MultiCalendar;

public class testCase6 {
	public static void main(String args[]){
		if(args.length == 0) {
			System.out.println("EmptyArgument");
			System.exit(0);
		}
		try{
			System.out.println(MartusFlexidate.extractIsoDateFromStoredDate(args[0]));
		}catch(StringIndexOutOfBoundsException e){
			System.out.println("StringIndexOutOfBoundsException");
		}
	}
}
