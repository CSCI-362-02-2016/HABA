package testCasePackage;
import org.martus.common.utilities.MartusFlexidate;
import org.martus.util.MultiCalendar;

public class testCase6 {
	public static void main(String args[]){
		try{
			System.out.println(MartusFlexidate.extractIsoDateFromStoredDate(args[0]));
		}catch(StringIndexOutOfBoundsException e){
			System.out.println("StringIndexOutOfBoundsException");
		}
	}
}
