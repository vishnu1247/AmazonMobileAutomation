package amazon.core.accelerators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import amazon.config.gblConstants;
import amazon.core.logs.LogManager;

public class TestDataProviders {
	
	public static String testDataPath = System.getProperty("user.dir") + File.separator + gblConstants.testDataDirName;
	public static ExcelReader xlRef;
	private static CSVReader csvReader = null;


	/*
	 * Purpose: To read csv reader as a list of arguments
	 * Author-date: vishnu
	 * Reviewer date:
	 */

	/*
	 * converts each row of CSV into the String parameters of the particular test case
	 * @param filename relative path of the CSVReader
	 * @return Iterator<Object []>
	 */
    public static Iterator<Object []> getCSVData(String filename) {
		List<Object[]> testCases = new ArrayList<>();
		try {
			Reader reader = Files.newBufferedReader(Paths.get(testDataPath + File.separator + filename));
			csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

			String[] nextRecord;
			while ((nextRecord = csvReader.readNext()) != null) {
				testCases.add(nextRecord);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return testCases.iterator();
	}


	/*
	 * Purpose: To read csv reader as a Hashmap
	 * Author-date:vishnu
	 * Reviewer date:
	 */

	/*
	 * converts each row of CSV into the HashMap with the key as header column name and value as the the row value
	 * @param filename relative path of the CSVReader
	 * @return Object[][] as Hashmap
	 */
	public static Object[][] getCSVDataAsHashMap(String filename) {
		Object[][] obj = null;
		try {
			Reader reader = Files.newBufferedReader(Paths.get(testDataPath + File.separator + filename));

			csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
			int rowCounter = 0;
			while((csvReader.readNext()) != null){
				rowCounter++;
			}

			reader = Files.newBufferedReader(Paths.get(testDataPath + File.separator + filename));
			csvReader = new CSVReaderBuilder(reader).build();
			String[] nextRecord;
			List<String> header = new ArrayList<>();
			nextRecord = csvReader.readNext();
			for(String column: nextRecord){
				header.add(column);
			}

			obj = new Object[rowCounter][1];
			int rowCt= 0;
			while((nextRecord = csvReader.readNext()) != null){
				int index = 0;
				HashMap<Object, Object> dataMap = new HashMap<>();
				Iterator<String> headerIterator = header.iterator();
				while(headerIterator.hasNext() && index < nextRecord.length){
					dataMap.put(headerIterator.next(),nextRecord[index++]);
				}
				obj[rowCt++][0] = dataMap;
			}


		} catch (IOException e){
			e.printStackTrace();
		}
		return obj;
	}
	
	/*
	 *  Purpose: To read non blank rows from given excel file - sheet name
	 *  Inputs: fileName - Test data excel file & sheetName - sheetName in given excel
	 *  Output: Sigle Column data array, with rows equal to non blank excel rows (each row is hash map of col name,row value)
	 *  Author/Date: vishnu
	 *  Reviewer/Date:  
	 */
	public static synchronized Object[][] loadExcelTable(String fileName, String sheetName){
		Object[][] data = null;
		try {
			xlRef = new ExcelReader(testDataPath + File.separator + fileName);
			
			//Define an single object array with row count 
			int totalRowCnt = xlRef.getRowCount(sheetName);
			int totalColCnt = xlRef.getColumnCount(sheetName);
			data = new Object[totalRowCnt-1][1];
			
			//Capture column names
			ArrayList<String> colNames = new ArrayList<String>();
			for(int colCntr=0;colCntr<totalColCnt;colCntr++){
				colNames.add(xlRef.getCellData(sheetName, colCntr, 1).trim());
			}
			
			//Capture rows info
			for(int rCntr=0;rCntr<totalRowCnt-1; rCntr++){
				HashMap<String, String> inpRec = new HashMap<String, String>();
				for (int colCntr=0;colCntr<totalColCnt;colCntr++){				
					inpRec.put(colNames.get(colCntr), xlRef.getCellData(sheetName, colNames.get(colCntr), rCntr+2));
				}
				//Add each row into single column data array
				data[rCntr][0]=inpRec;
			}
		}catch(FileNotFoundException fnf) {
			LogManager.logException(fnf, TestDataProviders.class.getName(), "Unable to find input test data file " + fileName);
			fnf.printStackTrace();
		}catch(IOException io) {
			LogManager.logException(io, TestDataProviders.class.getName(), "Unable to read data from " + fileName + " and sheet " + sheetName);
			io.printStackTrace();
		}catch(Exception e) {
			LogManager.logException(e, TestDataProviders.class.getName(), "Exception to read data from " + fileName + " and sheet " + sheetName);
			e.printStackTrace();
		}
		return data;
	}

	/*
	 *  Purpose: To read non blank rows from given excel file - sheet name
	 *  Inputs: fileName - Test data excel file & sheetName - sheetName in given excel
	 *  Output: Sigle Column data array, with rows equal to non blank excel rows (each row is hash map of col name,row value)
	 *  Author/Date: vishnu
	 *  Reviewer/Date:  
	 */
	public static synchronized Object[][] loadExcelRows(String fileName, String sheetName, String colName, String expectedValue) {
		
		Object[][] dataArray = null; //new Object[totalRowCnt-1][1];
		
		try {
			xlRef = new ExcelReader(testDataPath + File.separator + fileName);
			
			//Define an single object array with row count 
			int totalRowCnt = xlRef.getRowCount(sheetName);
			int totalColCnt = xlRef.getColumnCount(sheetName);		
			List<HashMap<String,String>> matchingData = new ArrayList<HashMap<String,String>>();

			//Capture column names
			ArrayList<String> colNames = new ArrayList<String>();
			for(int colCntr=0;colCntr<totalColCnt;colCntr++){
				colNames.add(xlRef.getCellData(sheetName, colCntr, 1).trim());
			}
			
			//Check if the given Column Name exists in the sheet
			if(colNames.indexOf(colName) >= 0) {
				
				//Iteratively capture rows info
				for(int rCntr=0;rCntr<totalRowCnt-1; rCntr++){
					HashMap<String, String> inpRec = new HashMap<String, String>();			
					for (int colCntr=0;colCntr<totalColCnt;colCntr++){				
						inpRec.put(colNames.get(colCntr), xlRef.getCellData(sheetName, colNames.get(colCntr), rCntr+2));
					}
					
					//Add row to return array, if input column value matches input expected value  
					if(inpRec.get(colName).equalsIgnoreCase(expectedValue.trim())) {
						matchingData.add(inpRec);
					}
				}
			
				//Convert matching data into 2D Object Array
				dataArray = new Object[matchingData.size()][1];
				for(int dCntr=0;dCntr<matchingData.size();dCntr++) {
					dataArray[dCntr][0] = matchingData.get(dCntr);
				}
			}
			
		}catch(Exception e) {
			LogManager.logException(e, TestDataProviders.class.getName(), "Exception to read data from " + fileName + " and sheet " + sheetName);
			e.printStackTrace();
		}	
		return dataArray;
	}
}
