import java.util.Vector;


public class TEST {
	public static void main(String[] argv){
		Vector<String> a = new Vector<String>();
		a.add("11");
		a.add("22");
		a.add("3");
		a.add("4");
		a.add("55");
		
		Vector<String> aa = (Vector<String>)a.clone();
		a.remove(1);
		a.remove(2);
		
		Vector<String> b = (Vector<String>)aa.clone();
		b.remove("2");
		b.remove("3");
		
		//a = (Vector<String>)aa.clone();
		System.out.println(a);
		
		
	}
}
