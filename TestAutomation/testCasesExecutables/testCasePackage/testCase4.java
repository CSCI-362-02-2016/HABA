package testCasePackage;
import org.martus.common.utilities.MartusFlexidate;
import org.martus.util.MultiCalendar;
import java.util.Date;

public class testCase4 {
	public static void main(String args[]){
		MultiCalendar cal = new MultiCalendar();
		if(args.length == 0) {
			System.out.println("EmptyArgument");
			System.exit(0);
		}
		if(args[0].length() == 1){
			Date date = new Date(Integer.valueOf(args[0]));
			cal.setTime(date);
			System.out.println(MartusFlexidate.toStoredDateFormat(cal));
		}else{
			try{
			String[] pos = args[0].split(",");
			int pos1 = Integer.valueOf(pos[0]);
			int pos2 = Integer.valueOf(pos[1]);
			int pos3 = Integer.valueOf(pos[2]);
			cal.setGregorian(pos1, pos2, pos3);
			System.out.println(MartusFlexidate.toStoredDateFormat(cal));
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("ArrayIndexOutOfBoundsException");
			}catch(NumberFormatException e2){
				System.out.println("NumberFormatException");
			}
		}
		
		
	}
}
