package DZ2.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ReadingCreator {
	ArrayList<Reading> readings;

	public ReadingCreator() throws IllegalStateException, FileNotFoundException {
		//System.out.println("tu?");
		readFile();
//		this.readings = new CsvToBeanBuilder<Reading>(new FileReader("C:\\Code\\Eclipse\\RASSUS\\RASSUS 2. dz\\DZ_UDP\\mjerenja.csv")).withType(Reading.class).build().parse();
	}
	
	public Reading getReading(long time) {
		long current = System.currentTimeMillis();
		long delta = current - time;
		int index = (int) ((delta % 100) + 2);
		System.out.println("Vrijeme proteklo od aktivacije senzora: " + delta + ", redni broj: " + index);
		
		return readings.get(index);
	}
	
	public int getReadingsSize() {
		return readings.size();
	}
	
	public void readFile(){
		readings = new ArrayList<Reading>();
				
		String file = "mjerenja.csv";
		FileReader reader;
		
		
		try {
			reader = new FileReader(file);
			
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(reader);
			String line;
			
			while((line = br.readLine()) != null) {
				//System.out.println(line);
				line = line.replaceAll("[^0-9,]", "");
				line = line + "0";
				String[] row = line.split(",");
				
				Reading reading = new Reading(row[0], row[1], row[2], row[3], row[4], row[5]);
				readings.add(reading);
				//System.out.println(reading);
				
			}
			
			Reading reading = new Reading("0", "0", "0", "0", "0", "0");
			readings.add(reading);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
}
