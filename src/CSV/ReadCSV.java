package CSV;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

public class ReadCSV {
	public List<String[]> readCSVFile(String path){
		List<String[]> lines = new ArrayList<>();
		
		Reader reader = null;
		CSVReader csvReader = null;
		try {
			reader = Files.newBufferedReader(Paths.get(path));
			csvReader = new CSVReader(reader);
			
			// Read headers
			String[] header = csvReader.readNext();
			System.out.println(StringUtils.join(header, ","));
			
			// Read all lines
			lines.addAll(csvReader.readAll());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.out.println("Error closing reader!!");
				}
			}
			
			if(csvReader != null) {
				try {
					csvReader.close();
				} catch (IOException e) {
					System.out.println("Error closing csv!!");
				}
			}
		}
		
		lines.stream().forEach(l -> {
			System.out.println(StringUtils.join(l, ","));
		});
		
		return lines;
	}
}
