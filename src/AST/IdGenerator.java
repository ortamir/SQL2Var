package AST;

public class IdGenerator {
	
	public static int num=0;
	public static String getFreshId(){
		num++;
		return "u"+num;
	}

}
