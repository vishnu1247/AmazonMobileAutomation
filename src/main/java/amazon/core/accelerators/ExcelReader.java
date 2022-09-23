package amazon.core.accelerators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import amazon.config.gblConstants;
import amazon.core.accelerators.ExcelReader;
/**
 * Utility to read Excel files. This file makes use of apache poi for reading excel files.
 * It supports both "xls" and "xlsx" file extension.
 *
 */
public class ExcelReader  {
	private static Cell openCell;
	private static Row openRow;
	private static Sheet openSheet;
	private static Workbook openWorkbook;
	private static Map<String, List<String>> storedData = new LinkedHashMap<String, List<String>>();
	public static ResultSet resultset;
	//protected static String TestDataPath=System.getProperty("user.dir").replace("\\", "/")+"/testdata/TestData.xlsx";
	//protected static String testDriver=System.getProperty("user.dir").replace("\\", "/")+"/testdata/Test_Driver.xlsx";
	//public static String TestDataSheetName="Data";
	private static XSSFWorkbook workbook = null;
	private static XSSFSheet sheet = null;
	private XSSFRow row   =null;
	private XSSFCell cell = null;

	/******** :::::::: Code for Keyword Driven Framework:: Start ::::::: ******/
	//public String filename = testDriver;
	public  String path;
	public  FileInputStream fis = null;
	public  FileOutputStream fileOut =null;

	/******** :::::::: Code for Keyword Driven Framework:: end ::::::: ******/

	/**
	 * Creates a ExcelReader object based on filePath
	 * @param filePath File path of the file to be opened
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ExcelReader(String filePath) throws FileNotFoundException,IOException  {
		//this(new File(filePath));
		System.out.println("Spreadsheet path :- ***** "+filePath +" *****");
		this.path=filePath;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * Creates a ExcelReader object based on <code>File</code> object passed
	 * @param file <code>File</code> object of the file to be opened.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public ExcelReader(File file) {
		try {
			this.openFile(file, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ExcelReader(InputStream fileStream) throws IOException, FileNotFoundException  {
		this.openFile(fileStream, 0);
	}

	/**
	 * 
	 * @param filePath
	 * @param sheetNo
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public ExcelReader(String filePath, int sheetNo) throws IOException, FileNotFoundException  {
		this.openFile(filePath, sheetNo);
	}

	public ExcelReader(InputStream fileStream, int sheetNo) throws IOException, FileNotFoundException  {
		this.openFile(fileStream, sheetNo);
	}

	/**
	 * 
	 * @param filePath
	 * @param sheetName
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public ExcelReader(String filePath, String sheetName) throws IOException, FileNotFoundException {
		this.openFile(filePath, sheetName);
	}

	public ExcelReader(InputStream fileStream, String sheetName) throws IOException, FileNotFoundException {
		this.openFile(fileStream, sheetName);
	}

	/**
	 * 
	 * @param file
	 * @param sheetNo
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public ExcelReader(File file, int sheetNo) throws IOException, FileNotFoundException {

		try {

			this.openFile(file, sheetNo);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param file
	 * @param sheetName
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public ExcelReader(File file, String sheetName) {
		try {

			this.openFile(file, sheetName);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param file
	 * @param sheetNo
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void openFile(File file, int sheetNo) throws IOException,
	FileNotFoundException {
		openWorkbook(file);
		openSheet = openWorkbook.getSheetAt(sheetNo);
	}

	public void openFile(InputStream fileStream, int sheetNo) throws IOException,
	FileNotFoundException {
		openWorkbook(fileStream);
		openSheet = openWorkbook.getSheetAt(sheetNo);
	}

	/**
	 * 
	 * @param filePath
	 * @param sheetNo
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void openFile(String filePath, int sheetNo)
			throws FileNotFoundException, IOException {
		this.openFile(new File(filePath), sheetNo);
	}

	public void openFile(File file, String sheetName)
			throws FileNotFoundException, IOException {
		openWorkbook(file);
		openSheet = openWorkbook.getSheet(sheetName);
	}

	public void openFile(String filePath, String sheetName)
			throws FileNotFoundException, IOException {
		openWorkbook(filePath);
		openSheet = openWorkbook.getSheet(sheetName);
	}

	public void openFile(InputStream fileStream, String sheetName)
			throws FileNotFoundException, IOException {
		openWorkbook(fileStream);
		openSheet = openWorkbook.getSheet(sheetName);
	}

	public static void openWorkbook(String filePath) throws FileNotFoundException,
	IOException {
		openWorkbook(new File(filePath));
	}

	public static void openWorkbook(File file) throws FileNotFoundException,
	IOException {
		try{
			openWorkbook = WorkbookFactory.create(file);
		} catch (Exception e ){
			System.out.println(e.getMessage());
		}
	}

	public static void openWorkbook(InputStream fileStream) throws FileNotFoundException,
	IOException {
		try {
			openWorkbook = WorkbookFactory.create(fileStream);
			System.out.println(openWorkbook);
		} catch (InvalidFormatException e ){
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private void openSheet(){
		openSheet = openWorkbook.getSheetAt(0);
	}

	@SuppressWarnings("unused")
	private void openSheet(int sheetNo){
		openSheet = openWorkbook.getSheetAt(sheetNo);
	}


	/**
	 * Gets the data from the currently opened sheet based on row and column number
	 * @param row Row no. from which the value has to be fetched
	 * @param column Respective column no. in the row from which the value has to be fetched
	 * @return The data present in the respective row & column. If no value is found it returns and empty String.
	 */
	public static String getData(int row, int column) {
		String data = "";
		try {

			openRow = openSheet.getRow(row);
			openCell = openRow.getCell(column);			
			if(openCell.getCellTypeEnum() == CellType.FORMULA) {
				XSSFFormulaEvaluator eval = new XSSFFormulaEvaluator(workbook);			
				eval.evaluate(openCell);							
			}
			
			//int cellType = openCell.getCellType();
			switch (openCell.getCellTypeEnum()) {			
			case FORMULA: //0				
				if (DateUtil.isCellDateFormatted(openCell)) {
					Date dt = openCell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"dd MM yyyy HH:mm:ss");
					data = sdf.format(dt);
				} 
//				else
//					data = Long.toString(Math.round(openCell
//							.getNumericCellValue()));
				break;
			case BLANK: //1
				data = openCell.getRichStringCellValue().getString();
				break;
			case STRING: //2
				data = openCell.getRichStringCellValue().getString();
				break;
			case NUMERIC: //3
				data = openCell.getRichStringCellValue().getString();
				break;
			case BOOLEAN: //4
				data = Boolean.toString(openCell.getBooleanCellValue());
				break;
			case ERROR: //5
				data = Byte.toString(openCell.getErrorCellValue());
				break;
			default:
				data = openCell.getRichStringCellValue().getString();
			}

			if (data == null) {
				data = "";
			}
			return data;
		} catch (Exception e) {
			if (openRow == null || openCell == null || data == null) {
				data = "";
				return data;
			} else {
				System.out.println(e);
				return "";
			}
		}
	}

	/**
	 * Gets the no. of rows in the currently opened sheet
	 * @return The actual no of physical rows present
	 */
	public int getNoOfRows() {
		return openSheet.getPhysicalNumberOfRows();
	}

	/**
	 * Gets the no. of column present in the first row of the currently opened sheet.
	 * @return Return the no. of column present in the first row of the currently opened sheet.
	 */
	public int getNoOfColumn() {
		return this.getNoOfColumn(0);
	}

	/**
	 * Gets the no. of column present in the specified row of the currently opened sheet.
	 * 
	 * @param rowNo Row no. for which the no. of column have to evaluated.
	 * @return Return the no. of column present in the specified row of the currently opened sheet.
	 */
	public int getNoOfColumn(int rowNo) {
		Row rw = openSheet.getRow(rowNo);
		return rw.getPhysicalNumberOfCells();
	}

	public int getParameters() {
		Row rw = openSheet.getRow(0);
		return rw.getPhysicalNumberOfCells();
	}

	/**
	 * Stores the whole data of the currently opened sheet in a Map containing keys  
	 */
	public static void storeData() {
		try{
			int rowCount = openSheet.getPhysicalNumberOfRows();
			Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
			for (int i = 0; i < rowCount; i++) {
				Row row = openSheet.getRow(i);
				String rowKey = "";
				int cellCount = row.getPhysicalNumberOfCells();
				for (int j = 0; j < cellCount; j++) {
					String cellValue = row.getCell(j).getStringCellValue();
					if (j == 0) {
						rowKey = cellValue;
						map.put(rowKey, new ArrayList<String>());
					} else {
						map.get(rowKey).add(cellValue);
					}
				}
			}
			storedData = map;
		}catch(Exception ex){
			ex.getLocalizedMessage();};
	}

	public void storeDataBkp() {
		Row rw;
		int rowCount = openSheet.getPhysicalNumberOfRows();
		storedData.clear();
		for (int i = 0; i < rowCount - 1; i++) {
			rw = openSheet.getRow(i);
			String key = getData(0, i);
			List<String> valueList = new ArrayList<String>();
			storedData.put(key, valueList);
			for (int j = 1; j < rw.getPhysicalNumberOfCells(); j++) {
				String data = getData(j, i);
				valueList.add(data);
			}}}

	public static Map<String, List<String>> getStoredData() {
		storeData();
		return storedData;
	}

	public Map<String, List<String>> getStoredDataBkp() {
		if(storedData.isEmpty()){
			storeData();
		}
		return storedData;
	}

//	public static Map<String, List<String>> getStoredData(String workbookpath, String sheetName) {
//		openSheet(workbookpath,sheetName);
//		storedData.clear();
//		return getStoredData();
//	}

	/**
	 * @author vishnu
	 * @param fileName
	 * @return
	 */
	public static List<Map<String, String>> getStoredDataAsMap(String fileName) {
		List<Map<String, String>> listOfDataMap = new ArrayList<Map<String, String>>();

		try {
			// Create the input stream from the xlsx/xls file
			FileInputStream fis = new FileInputStream(fileName);

			// Create Workbook instance for xlsx/xls file input stream
			Workbook workbook = null;
			if (fileName.toLowerCase().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(fis);
			} else if (fileName.toLowerCase().endsWith("xls")) {
				workbook = new HSSFWorkbook(fis);
			}

			// Get the 1st sheet from the workbook
			Sheet sheet = workbook.getSheetAt(0);

			// every sheet has rows, iterate over them
			Iterator<Row> rowIterator = sheet.iterator();
			boolean firstRowFlag = true;
			Row firstRow = null;
			while (rowIterator.hasNext()) {
				HashMap<String, String> rowMap = new HashMap<String, String>();
				Row currentRow = (Row) rowIterator.next();
				if (firstRowFlag) {
					//Sets to false once it founds the first row
					firstRowFlag = false;
					// Gets the first row and save it in a variable to use it later, which are expected to be column names
					firstRow = currentRow;
					continue; //Continue with next record.
				}
				//This loop executes from second row onwards, where actual test data starts
				for (int i = 0; i < currentRow.getLastCellNum(); i++) {
					//TODO: Handle Null values, handle Cell Data Type
					//Gets the current cell value
					String currentCellValue = currentRow.getCell(i).getStringCellValue();

					//Gets the appropriate column name for the current cell
					String colName = firstRow.getCell(i).getStringCellValue();

					//Adds to the Row Map
					rowMap.put(colName, currentCellValue);
				}

				//Row Map adds to the List
				listOfDataMap.add(rowMap);
			}
			fis.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return listOfDataMap;
	}



	public static ArrayList<String> getListDataFromMap(Map<String, List<String>> excelData) {
		ArrayList<String> list=new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : excelData.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(entry.getKey())){
				list=(ArrayList<String>) entry.getValue();
			}
		}
		return list;
	}

	public int getNumberOfSheets() {
		return openWorkbook.getNumberOfSheets();
	}

	/**
	 * @author vishnu
	 * @param WbookPath
	 * @param SheetName
	 * @param RowNum
	 * @param ColumnName
	 * @return
	 * @throws IOException
	 */
	public static String fn_GetCellData(String WbookPath, String SheetName, int RowNum, String ColumnName)
			throws IOException {

		FileInputStream FISObj = new FileInputStream(WbookPath);
		@SuppressWarnings("resource")
		XSSFWorkbook WbookObj = new XSSFWorkbook(FISObj);
		XSSFSheet WSheetObj = WbookObj.getSheet(SheetName);
		XSSFRow RowObj = WSheetObj.getRow(RowNum);
		int columnNumber = fn_GetCellNumberByColumName(WSheetObj, ColumnName);
		XSSFCell CellObj = RowObj.getCell(columnNumber, MissingCellPolicy.CREATE_NULL_AS_BLANK);		
		String CellVal = null;
		if (CellObj.getCellTypeEnum() == CellType.NUMERIC) {
			Double DblCellVal = CellObj.getNumericCellValue();
			Integer intcellval = DblCellVal.intValue();
			CellVal = intcellval.toString();
		} else if (CellObj.getCellTypeEnum() == CellType.STRING ) {
			CellVal = CellObj.getStringCellValue();
		}
		return CellVal;
	}

	/**
	 * @author vishnu
	 * @param WSheetObj
	 * @param arg_ColumnName
	 * @return
	 * @throws IOException
	 */
	public static int fn_GetCellNumberByColumName(XSSFSheet WSheetObj, String ColumnName) throws IOException {

		XSSFRow FstRowObj = WSheetObj.getRow(0);
		int columnCount = FstRowObj.getLastCellNum();
		int columnNumber = 0;
		for (int i = 0; i <= columnCount - 1; i++) {
			XSSFCell cellObj = FstRowObj.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			String xl_ColumnName = cellObj.getStringCellValue();
			xl_ColumnName = xl_ColumnName.trim();
			ColumnName = ColumnName.trim();
			if (ColumnName.equalsIgnoreCase(xl_ColumnName) == true) {
				columnNumber = i;
				break;
			}
		}
		return columnNumber;
	}

	/**
	 * @author vishnu
	 * @param xl_FilePath
	 * @param SheetName
	 * @return
	 * @throws IOException
	 */
	public static int fn_GetRowCount(String xl_FilePath, String SheetName) throws IOException {

		File xlFileObj = new File(xl_FilePath);
		FileInputStream FISObj = new FileInputStream(xlFileObj);
		@SuppressWarnings("resource")
		XSSFWorkbook WbookObj = new XSSFWorkbook(FISObj);
		XSSFSheet WSheetObj = WbookObj.getSheet(SheetName);
		int rowcount = WSheetObj.getLastRowNum();
		return rowcount;
	}

	/**
	 * @author vishnu
	 * @throws IOException
	 */
	public static void getAllData(String worbookPath, String sheetName) throws IOException {
		FileInputStream FISObj = new FileInputStream(worbookPath);
		@SuppressWarnings("resource")
		XSSFWorkbook WbookObj = new XSSFWorkbook(FISObj);
		XSSFSheet WSheetObj = WbookObj.getSheet(sheetName);
		int rowcount = WSheetObj.getLastRowNum();//// it returns nuberofrows-1
		for (int i = 1; i <= rowcount; i++) {
			XSSFRow RowObj = WSheetObj.getRow(i);
			int cellcount = 0;
			try {
				cellcount = RowObj.getPhysicalNumberOfCells();//// it returns
				//// number of
				//// cells only
				//// not -1
			} catch (Exception e) {

			}
			for (int j = 0; j <= cellcount - 1; j++) {
				MissingCellPolicy MCPObj = MissingCellPolicy.CREATE_NULL_AS_BLANK;
				XSSFCell CellObj = RowObj.getCell(j, MCPObj);
				String CellVal = null;
				if ( CellObj.getCellTypeEnum() == CellType.NUMERIC ) {
					Double DblCellVal = CellObj.getNumericCellValue();
					Integer intcellval = DblCellVal.intValue();
					CellVal = intcellval.toString();
				} else if ( CellObj.getCellTypeEnum() == CellType.STRING ) {
					CellVal = CellObj.getStringCellValue();
				}
				System.out.print(CellVal + ",");
			}
		}
	}

	/**
	 * @author vishnu
	 * @param startRowNumber
	 * @param endRowNumber
	 */
	public static void storeDatafromRow(int startRowNumber, int endRowNumber) {
		try {
			Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
			int rowCount = openSheet.getPhysicalNumberOfRows();
			if(rowCount>=endRowNumber){
				String[] array = new String[100];
				Row row = openSheet.getRow(0);
				int cellCount = row.getPhysicalNumberOfCells();
				for (int j = 0; j < cellCount; j++) {
					String cellValue = row.getCell(j).getStringCellValue();
					array[j] = cellValue;
					map.put(cellValue, new ArrayList<String>());
				}
				for (int i = startRowNumber; i < endRowNumber; i++) {
					row = openSheet.getRow(i);
					cellCount = row.getPhysicalNumberOfCells();
					for (int j = 0; j < cellCount; j++) {
						MissingCellPolicy MCPObj = MissingCellPolicy.CREATE_NULL_AS_BLANK;
						XSSFCell CellObj = (XSSFCell) row.getCell(j, MCPObj);
						//int celltypenumber = CellObj.getCellType();
						String cellValue = null;
						if ( CellObj.getCellTypeEnum() == CellType.NUMERIC ) {
							Double DblCellVal = CellObj.getNumericCellValue();
							Integer intcellval = DblCellVal.intValue();
							cellValue = intcellval.toString();
						} else if ( CellObj.getCellTypeEnum() == CellType.STRING ) {
							cellValue = CellObj.getStringCellValue();
						} else if ( CellObj.getCellTypeEnum() == CellType.BOOLEAN ) {
							Boolean booleanCellValue = CellObj.getBooleanCellValue();
							cellValue = booleanCellValue.toString();
						}
						map.get(array[j]).add(cellValue);
					}
				}
			}
			storedData = map;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * to read data from sheet
	 * @author vishnu
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	public static List<String> read(XSSFSheet sheet) throws IOException {
		List<String> excelVal = new ArrayList<String>();
		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row rovVal = (Row) rowIterator.next();
			String rowVal = "";
			for (int i = 0; i < rovVal.getLastCellNum(); i++) {
				XSSFCell cellA1 = (XSSFCell) rovVal.getCell(i);
				rowVal = rowVal + cellA1.getStringCellValue() + "~";
			}
			excelVal.add(rowVal);
		}

		return excelVal;
	}

	/**
	 * @author vishnu
	 * @param excelFilePath
	 * @param sheetName
	 * @param numberOfParameters
	 * @return
	 */
	public static String[][] readParamsFromExcel(String excelFilePath, String sheetName, int numberOfParameters) {
		String[][] finalTestData = null;
		try {
			XSSFSheet sheet = (XSSFSheet) openWorkbook.getSheet(sheetName);
			List<String> excelVal = read(sheet);
			finalTestData = new String[excelVal.size()][numberOfParameters];
			int count=0;
			for (String rowVal : excelVal) {
				String[] col1DatatArray = rowVal.split("~");
				for(int i = 0; i < numberOfParameters; i++){
					finalTestData[count]=col1DatatArray;
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalTestData;
	}

	/**
	 * to read data from sheet
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	public static List<String> readData(XSSFSheet sheet) throws IOException {
		List<String> excelVal = new ArrayList<String>();
		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row rovVal = (Row) rowIterator.next();
			String rowVal = "";
			for (int i = 0; i < rovVal.getLastCellNum(); i++) {
				XSSFCell cellA1 = (XSSFCell) rovVal.getCell(i);
				rowVal = rowVal + cellA1.getStringCellValue() + "~";
			}
			excelVal.add(rowVal);
		}
		return excelVal;
	}

	/////*************************** Extra Excel Utilitties ***********************************//////

	// returns the row count in a sheet
	public int getRowCount(String sheetName){
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1)
			return 0;
		else{
			sheet = workbook.getSheetAt(index);
			int number=sheet.getLastRowNum()+1;
			return number;
		}

	}

	/******** :::::::: Code for Keyword Driven Framework:: Start ::::::: ******/

	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data){
		try{
			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);

			if(rowNum<=0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			int colNum=-1;
			if(index==-1)
				return false;


			sheet = workbook.getSheetAt(index);


			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				//System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum=i;
			}
			if(colNum==-1)
				return false;

			sheet.autoSizeColumn(colNum); 
			row = sheet.getRow(rowNum-1);
			if (row == null)
				row = sheet.createRow(rowNum-1);

			cell = row.getCell(colNum);	
			if (cell == null)
				cell = row.createCell(colNum);

			// cell style
			//CellStyle cs = workbook.createCellStyle();
			//cs.setWrapText(true);
			//cell.setCellStyle(cs);
			cell.setCellValue(data);

			fileOut = new FileOutputStream(path);

			workbook.write(fileOut);

			fileOut.close();	

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}


	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data,String url){
		//System.out.println("setCellData setCellData******************");
		try{
			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);

			if(rowNum<=0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			int colNum=-1;
			if(index==-1)
				return false;


			sheet = workbook.getSheetAt(index);
			//System.out.println("A");
			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				//System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
					colNum=i;
			}

			if(colNum==-1)
				return false;
			sheet.autoSizeColumn(colNum); //ashish
			row = sheet.getRow(rowNum-1);
			if (row == null)
				row = sheet.createRow(rowNum-1);

			cell = row.getCell(colNum);	
			if (cell == null)
				cell = row.createCell(colNum);

			cell.setCellValue(data);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();

			//cell style for hyperlinks
			//by default hypelrinks are blue and underlined
			CellStyle hlink_style = workbook.createCellStyle();
			XSSFFont hlink_font = workbook.createFont();
			hlink_font.setUnderline(XSSFFont.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			hlink_style.setFont(hlink_font);
			//hlink_style.setWrapText(true);

			//XSSFHyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
			XSSFHyperlink link = createHelper.createHyperlink(HyperlinkType.FILE);
			link.setAddress(url);
			cell.setHyperlink(link);
			cell.setCellStyle(hlink_style);

			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);

			fileOut.close();	

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// returns true if sheet is created successfully else false
	public boolean addSheet(String  sheetname){		

		FileOutputStream fileOut;
		try {
			workbook.createSheet(sheetname);	
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}


	// returns true if sheet is removed successfully else false if sheet does not exist
	public boolean removeSheet(String sheetName){		
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1)
			return false;

		FileOutputStream fileOut;
		try {
			workbook.removeSheetAt(index);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	// returns true if column is created successfully
	public boolean addColumn(String sheetName,String colName){
		//System.out.println("**************addColumn*********************");

		try{				
			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if(index==-1)
				return false;

			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			sheet=workbook.getSheetAt(index);

			row = sheet.getRow(0);
			if (row == null)
				row = sheet.createRow(0);

			//cell = row.getCell();	
			//if (cell == null)
			//System.out.println(row.getLastCellNum());
			if(row.getLastCellNum() == -1)
				cell = row.createCell(0);
			else
				cell = row.createCell(row.getLastCellNum());

			cell.setCellValue(colName);
			cell.setCellStyle(style);

			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();		    

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

		return true;
	}
	// removes a column and all the contents
	public boolean removeColumn(String sheetName, int colNum) {
		try{
			if(!isSheetExist(sheetName))
				return false;
			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);
			sheet=workbook.getSheet(sheetName);
			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			style.setFillPattern(FillPatternType.NO_FILL);
			for(int i =0;i<getRowCount(sheetName);i++){
				row=sheet.getRow(i);	
				if(row!=null){
					cell=row.getCell(colNum);
					if(cell!=null){
						cell.setCellStyle(style);
						row.removeCell(cell);
					}
				}
			}
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;

	}

	//String sheetName, String testCaseName,String keyword ,String URL,String message
	public boolean addHyperLink(String sheetName,String screenShotColName,String testCaseName,int index,String url,String message){
		//System.out.println("ADDING addHyperLink******************");

		url=url.replace('\\', '/');
		if(!isSheetExist(sheetName))
			return false;

		sheet = workbook.getSheet(sheetName);

		for(int i=2;i<=getRowCount(sheetName);i++){
			if(getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)){
				//System.out.println("**caught "+(i+index));
				setCellData(sheetName, screenShotColName, i+index, message,url);
				break;
			}
		}
		return true; 
	}

	
	/******** :::::::: Code for Keyword Driven Framework:: End ::::::: ******/

	// returns the data from a cell
	public String getCellData(String sheetName,String colName,int rowNum){
		try{
			if(rowNum <=0)
				return "";
			//System.out.println("sheet Name"+sheetName);
			int index = workbook.getSheetIndex(sheetName);
			int col_Num=-1;
			if(index==-1)
				return "";

			sheet = workbook.getSheetAt(index);
			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				//System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
					col_Num=i;
					break;
				}
			}
			if(col_Num==-1)
				return "";
			
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);
			if(row==null)
				return "";
			cell = row.getCell(col_Num);

			if(cell==null)
				return "";
			
			//System.out.println("Cell Type: " + cell.getCellTypeEnum());
			if(cell.getCellTypeEnum()==CellType.FORMULA) {
				XSSFFormulaEvaluator eval = new XSSFFormulaEvaluator(workbook);
				CellValue fomVal = eval.evaluate(cell);
				//System.out.println(cell.getCellTypeEnum());
				
				if (fomVal.getCellTypeEnum()==CellType.STRING) {
					return fomVal.getStringValue();
				}else if (fomVal.getCellTypeEnum()==CellType.NUMERIC) {
					return String.valueOf(fomVal.getNumberValue());
				}else if (fomVal.getCellTypeEnum()==CellType.BOOLEAN) {
					return String.valueOf(fomVal.getBooleanValue());
				}
			}
			
			if(cell.getCellTypeEnum()==CellType.STRING)
				return cell.getStringCellValue();
			else if(cell.getCellTypeEnum()==CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA ){
								
				String cellText  = String.valueOf(cell.getNumericCellValue());
				
				if (HSSFDateUtil.isCellDateFormatted(cell)) {					
					// format in form of MM/DD/YYYY
					Date cellVal = cell.getDateCellValue();
					cellText = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(cellVal);
				}
				return cellText;
			}else if(cell.getCellTypeEnum()==CellType.BLANK)
				return ""; 
			else 
				return String.valueOf(cell.getBooleanCellValue());

		}
		catch(Exception e){

			e.printStackTrace();
			return "row "+rowNum+" or column "+colName +" does not exist in xls";
		}
	}


	// returns number of columns in a sheet	
	public int getColumnCount(String sheetName){
		// check if sheet exists
		if(!isSheetExist(sheetName))
			return -1;

		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);

		if(row==null)
			return -1;

		return row.getLastCellNum();

	}

	public boolean isSheetExist(String sheetName){
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1){
			index=workbook.getSheetIndex(sheetName.toUpperCase());
			if(index==-1)
				return false;
			else
				return true;
		}
		else
			return true;
	}

	// returns number of columns in a sheet	
	public int getColumnCount(String sheetName, int rowNumber){
		// check if sheet exists
		if(!isSheetExist(sheetName))
			return -1;

		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(rowNumber);

		if(row==null)
			return -1;

		return row.getLastCellNum();

	}

	public int getCellRowNum(String sheetName,String colName,String cellValue){

		for(int i=2;i<=getRowCount(sheetName);i++){
			if(getCellData(sheetName,colName , i).equalsIgnoreCase(cellValue)){
				return i;
			}
		}
		return -1;
	}

	// returns the data from a cell
	public String getCellData(String sheetName,int colNum,int rowNum){
		try{
			if(rowNum <=0)
				return "";

			int index = workbook.getSheetIndex(sheetName);

			if(index==-1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);
			if(row==null)
				return "";
			cell = row.getCell(colNum);
			if(cell==null)
				return "";

			if(cell.getCellTypeEnum()==CellType.FORMULA) {
				XSSFFormulaEvaluator eval = new XSSFFormulaEvaluator(workbook);
				eval.evaluate(cell);				
			}
			
			if(cell.getCellTypeEnum()==CellType.STRING)
				return cell.getStringCellValue();
			else if(cell.getCellTypeEnum()== CellType.NUMERIC) {

				String cellText  = String.valueOf(cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of MM/dd/yyyy HH:mm
					Date cellVal = cell.getDateCellValue();
					cellText = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(cellVal);				
				}				
				return cellText;
			}else if(cell.getCellTypeEnum()==CellType.BLANK)
				return "";
			else 
				return String.valueOf(cell.getBooleanCellValue());
		}
		catch(Exception e){

			e.printStackTrace();
			return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
		}
	}


}