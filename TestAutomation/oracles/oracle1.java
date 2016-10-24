import org.martus.client.core.MartusUserNameAndPassword;

public class oracle1 {
	public static void main(String args[]){
		boolean expected = Boolean.valueOf(args[1]);
		if(expected == MartusUserNameAndPassword.isWeakPassword(args[0].toCharArray())){
			System.out.println("Passed");
		}else{
			System.out.println("Failed");
		}
	}
}

