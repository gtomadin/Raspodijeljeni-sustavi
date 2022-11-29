package DZ2.model;

import java.util.HashSet;
import java.util.Set;

public class testMesure {
	public static void main(String[] args) {
		
		
		Set<Message> test = new HashSet<Message>();
		
		Message m = new Message(1, 1, "1", 1, "{10001=0, 10002=2}");
		Message m1 = new Message(1, 1, "1", 1, "{10001=0, 10002=2}");
		
		System.out.println(m.equals(m1));
		
		test.add(m);
		
		System.out.println("test1: " + test);
		
		boolean a = true;
		
		for(Message mes : test) {
			if(mes.equals(m1)) {
				a = false;
				break;
			}
		}
		
		if(a) {
			test.add(m1);
		}
		
		System.out.println("test2: " + test);
	}
}
