package amazon.core.accelerators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.TextParagraph.FontAlign;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFontFormatting;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWritting {

	public static String ResultSnapshotPath="Results";
	public static String ResultFolder="";
	public static String ResultSheetPath="";	
	private XSSFSheet sheet = null;
	private XSSFRow row =null;
	private XSSFCell cell = null;
	private XSSFWorkbook workbook = null;
	private FileOutputStream fileOut=null;
	private FileInputStream fis=null;
	private String path;

	public static void fn_CreateAndFormatExcel() throws IOException{
		XSSFWorkbook WbookObj=new XSSFWorkbook();
		XSSFSheet WsheetObj=WbookObj.createSheet("Result_Sheet");
		XSSFRow FstRowObj=WsheetObj.createRow(0);
		String[] ColmnArr={"ModuleName","SubModuleName", "TestCaseName", "PageTitle","ObjectName","ExpectedValue", "ActualValue", "Status", "SnapshotLink" };
		for(int i=0; i<=ColmnArr.length-1; i++){
			XSSFCell CellObj=FstRowObj.createCell(i);
			CellObj.setCellValue(ColmnArr[i]);
			short colorindex=IndexedColors.YELLOW.getIndex();
			short fontheight=13;
			CellStyle StyleObj=fn_SetCellStyle(WbookObj, colorindex,fontheight);
			CellObj.setCellStyle(StyleObj);
			WsheetObj.autoSizeColumn(i);
		}
		String ResultFileName=System.getProperty("user.dir").replace("\\", "/")+"/logs/Results/Execution_Results";
		Date DTE=new Date();
		DateFormat DF=DateFormat.getDateTimeInstance();
		String DateVal=DF.format(DTE);
		DateVal=DateVal.replaceAll(":", "_");
		//ResultFolder=ResultFolder+"/"+fn_GetLatestFolderName(ResultFolder);
		ResultSheetPath=ResultFolder+"/"+ResultFileName+".xlsx";
		FileOutputStream FOS=new FileOutputStream(ResultSheetPath);
		WbookObj.write(FOS);
		FOS.close();	
	}

	@SuppressWarnings("static-access")
	public static CellStyle fn_SetCellStyle(Workbook WbookObj,short cell_color_index, short FontHeight){

		CellStyle CellStyleObj=WbookObj.createCellStyle();
		CellStyleObj.setFillForegroundColor(cell_color_index);
		//short fillPatern_index=CellStyleObj.SOLID_FOREGROUND;
		CellStyleObj.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font FontObj=fn_SetFontStyle(WbookObj, FontHeight);
		CellStyleObj.setFont(FontObj);
		return CellStyleObj;
	}

	public static CellStyle fn_PassStyle(Workbook WbookObj){
		CellStyle CellStyleOb=WbookObj.createCellStyle();
		short colornumber=IndexedColors.GREEN.getIndex();
		CellStyleOb.setFillForegroundColor(colornumber);
		//short fillingpattern=XSSFCellStyle.SOLID_FOREGROUND;
		CellStyleOb.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		/////*****************************************/////////
		Font FontObj=WbookObj.createFont();
		//FontObj.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		FontObj.setBold(true);
		CellStyleOb.setFont(FontObj);
		return CellStyleOb;
	}

	public static CellStyle fn_LinkStyle(Workbook WbookObj){
		CellStyle CellStyleOb=WbookObj.createCellStyle();
		/////*****************************************/////////
		Font FontObj=WbookObj.createFont();
		FontObj.setColor(IndexedColors.BLUE.getIndex());
		FontObj.setUnderline(Font.U_SINGLE);
		CellStyleOb.setFont(FontObj);
		return CellStyleOb;
	}

	public static CellStyle fn_FailStyle(Workbook WbookObj){
		CellStyle CellStyleOb=WbookObj.createCellStyle();
		short colornumber=IndexedColors.RED.getIndex();
		CellStyleOb.setFillForegroundColor(colornumber);
		CellStyleOb.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		/////*****************************************/////////
		Font FontObj=WbookObj.createFont();
		//FontObj.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		FontObj.setBold(true);
		CellStyleOb.setFont(FontObj);
		return CellStyleOb;
	}

	public static Font fn_SetFontStyle(Workbook WbookObj,short fontheight ){

		Font FontObj=WbookObj.createFont();
		FontObj.setFontHeightInPoints(fontheight);
		//FontObj.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		FontObj.setBold(true);
		return FontObj;

	}
	public static void fn_UpdateExcelResults(String[] ArrValToWrite) throws IOException, InvalidFormatException{
		FileInputStream FIS=new FileInputStream(ResultSheetPath);
		Workbook WbookObj=WorkbookFactory.create(FIS);
		Sheet WSheetObj=WbookObj.getSheet("Result_Sheet");
		int lastrownum=WSheetObj.getLastRowNum();
		int required_row=lastrownum+1;
		Row RowObj=WSheetObj.createRow(required_row);

		for(int i=0; i<=ArrValToWrite.length-1; i++){
			Cell CellObj=RowObj.createCell(i);
			if(( ArrValToWrite[7]).equals("Failed"))
			{
				Cell cellObj=null;
				if(i<8)
				{
					CellStyle styleObj=fn_FailStyle(WbookObj);
					cellObj=RowObj.createCell(i);
					cellObj.setCellValue(ArrValToWrite[i].toString()); 
					cellObj.setCellStyle(styleObj);
				}
				else
				{
					String Formula="HYPERLINK("+'"'+ArrValToWrite[8]+'"'+", "+'"'+"Snapshot"+'"'+")";
					cellObj=RowObj.createCell(i);
					cellObj.setCellFormula(Formula); 
					cellObj.setCellStyle(fn_LinkStyle(WbookObj)) ;  		 
				}
			}

			else if(( ArrValToWrite[7]).equals("Passed"))
			{
				Cell cellObj=null;
				if(i<8)
				{
					CellStyle styleObj=fn_PassStyle(WbookObj);
					cellObj=RowObj.createCell(i);
					cellObj.setCellValue(ArrValToWrite[i].toString()); 
					cellObj.setCellStyle(styleObj);
				}
				else
				{		    			  
					String Formula="HYPERLINK("+'"'+ArrValToWrite[8]+'"'+", "+'"'+"Snapshot"+'"'+")";
					cellObj=RowObj.createCell(i);
					cellObj.setCellFormula(Formula); 
					cellObj.setCellStyle(fn_LinkStyle(WbookObj)) ;  		 
				}
			}
			else
			{
				CellObj.setCellValue(ArrValToWrite[i].toString());
			}	
		}
		FileOutputStream FOS=new FileOutputStream(ResultSheetPath);
		WbookObj.write(FOS);
		FOS.close();
	}

	public static String fn_GetLatestFolderpath(String ParentDirectory)
	{
		File fileobj=new File(ParentDirectory);
		String file_path=fileobj.getAbsolutePath();
		File[] files = fileobj.listFiles();
		//Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
		String fileName=files[0].getName();
		System.out.println(fileName);
		System.out.println("file_path:"+file_path);
		return file_path;
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

	// find whether sheets exists	
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

	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data){
		try{
			FileInputStream fis = new FileInputStream(path); 
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

			
			cell.setCellValue(data);

			FileOutputStream fileOut = new FileOutputStream(path);
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

			if(cell.getCellType()==Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){

				String cellText  = String.valueOf(cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();

					Calendar cal =Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText =
							(String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.MONTH)+1 + "/" +
							cal.get(Calendar.DAY_OF_MONTH) + "/" +
							cellText;

				}
				return cellText;
			}else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
				return "";
			else 
				return String.valueOf(cell.getBooleanCellValue());
		}
		catch(Exception e){

			e.printStackTrace();
			return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
		}
	}

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

}