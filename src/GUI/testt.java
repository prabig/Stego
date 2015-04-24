package GUI;

import java.util.ArrayList;
import java.util.Iterator;

public class testt {
	public static void main(String args[]) {
		
		System.out.println("Hello!");
		
		ArrayList obj = new ArrayList();
		ArrayList<String> myList = obj;
		
		
		for(int i=0; i<10; i++) {
			if(i<5)
				obj.add(""+i);
			else
				obj.add(i);
		}
		
		Iterator<String> iter = myList.iterator();
		
		for(;iter.hasNext();) {
			System.out.println(iter.next().charAt(0));
		}
	}
}
