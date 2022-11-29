package DZ2.model;

import java.io.FileNotFoundException;

public class TestReading {
	public static void main(String[] args) {
		
		try {
			ReadingCreator readingCreator = new ReadingCreator();
			

			for(int i=0;i<readingCreator.getReadingsSize() ; ++i) {
				System.out.println(readingCreator.getReading(i));
			}
			
			
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}	
