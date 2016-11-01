package testCasePackage;
import org.martus.common.utilities.MartusFlexidate;
import org.martus.util.MultiCalendar;
public class testCase4 {
	public static void main(String args[]){
		MultiCalendar cal = new MultiCalendar();
		String date = "2000-01-01";
		int range = 50;
		
		cal.setGregorian(2005, 4, 7);
		
		MartusFlexidate flexidate = new MartusFlexidate(date, range);
		System.out.println(flexidate.getBeginDate());
		System.out.println(flexidate.getEndDate());
		System.out.println(flexidate.toStoredDateFormat(cal));
	}
}
