package parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class CsvParser implements Callable<List<List<String>>> {

	private File file;

	public CsvParser(File file) {
		this.file = file;
	}

	private List<List<String>> getDailyStockDetailList() {
		List<List<String>> records = new ArrayList<List<String>>();
		try (CSVReader csvReader = new CSVReader(new FileReader(file));) {
			String[] values = null;
			try {
				while ((values = csvReader.readNext()) != null) {
					records.add(Arrays.asList(values));
				}
			} catch (CsvValidationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return records;
	}

	@Override
	public List<List<String>> call() throws Exception {
		return getDailyStockDetailList();
	}
}
