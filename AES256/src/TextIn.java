import java.util.Scanner;

public class TextIn {

	public static void main(String[] args) throws Exception {	
		chat c;
		c = new chat();
		
		String user, str;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		System.out.println("사용자  : ");
		user = input.nextLine();
		System.out.println("메시지 : ");
		str = input.nextLine();
		
		c.name = user;
		c.msg = str;
		String encrypted = CryptoEx.encodedText(c.name + " just said : " + c.msg,CryptoEx.key).toString();
		
		System.out.println("입력 : " + c.name + " : " + c.msg);
		System.out.println("암호화 후 : " + encrypted);
		String decrypted = CryptoEx.decodedText(encrypted, CryptoEx.key);
		System.out.println("복호화 후 : " + decrypted );
	}
	//이게 되는지 일단 테스트하는 용도
	
}


class chat{
	String name;
	String msg;
}
