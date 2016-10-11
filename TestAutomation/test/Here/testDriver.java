package Here;
import java.lang.reflect.*;
import org.martus.client.core.MartusUserNameAndPassword;

// To compile: javac -cp /home/hugo/Desktop/MartusClient-4.4.0/martus.jar testDriver.java
// javac -cp /home/hugo/workspace/TestAutomation/project testDriver.java
// java -cp /home/hugo/Desktop/MartusClient-4.4.0/martus.jar:/home/hugo/workspace/TestAutomation/test Here.testDriver

public class testDriver {
	
	public static void main(String[] args){
		System.out.println("Hello");
		MartusUserNameAndPassword f = new MartusUserNameAndPassword();
		
		//System.out.println(f.getClass().getName().toString());
		
		try{
		//Class cls = Class.forName(f.getClass().getName().toString());
		Class cls2 = Class.forName("org.martus.client.core.MartusUserNameAndPassword");

		Method methodList[] = cls2.getDeclaredMethods();
		//Method methodList2[] = f.getClass().getDeclaredMethods();
		System.out.println(methodList[0].getDeclaringClass());
		for(int i = 0; i < methodList.length; i++){
			System.out.println(methodList[i].getName());
		}
		char[] password = "test".toCharArray();
		if(MartusUserNameAndPassword.isWeakPassword(password) == false){
			System.out.println("False");
		}
		if(MartusUserNameAndPassword.isWeakPassword(password) == true){
			System.out.println("True");
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
class Methodtest2 {
	public static int add(int a, int b){
		return a+b;
	}
}
