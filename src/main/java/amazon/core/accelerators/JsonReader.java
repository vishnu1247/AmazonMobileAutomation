package amazon.core.accelerators;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

public class JsonReader {
    
	private Object jsRoot = null;
	
	/*
	 * Purpose: static method to parse input jsonfile and return its content 
	 * input - filePath:  Absolute file path of .json file
	 * output: String holding the input .json file
	 * Author-Date: vishnu
	 * Reviewer-Date:
	 */
    public static String readJSONFile(File inpJSONFile) {
		String strJSON="";
		try {
	    	FileReader file = new FileReader(inpJSONFile);
			BufferedReader bfile = new BufferedReader(file);
			
			String line = null;
			while( (line = bfile.readLine()) != null) {
				strJSON += line;
			}
			bfile.close();			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
    	return strJSON;
    }
    
	/*
	 * Purpose: constructor to load json file from given location
	 * input - filePath: Absolute file path of .json file to parse & represent
	 * output: String holding the input .json file
	 * Author-Date: vishnu
	 * Reviewer-Date:
	 */
    public JsonReader(File inpJSONFile) {
    	String strJSON = JsonReader.readJSONFile(inpJSONFile);
    	jsRoot = Configuration.defaultConfiguration().jsonProvider().parse(strJSON);
    }
    
    public JsonReader(String inpJSON) {
    	String strJSON = inpJSON;
    	jsRoot = Configuration.defaultConfiguration().jsonProvider().parse(strJSON);
    }
    
    public JsonReader (JSONObject jsInp) {
    	jsRoot = Configuration.defaultConfiguration().jsonProvider().parse(jsInp.toString());;
    }
    
	/*
	 * Purpose: To evaluate json path expression to return corresponding value as string
	 * Constraints: Include only one expression with in the json path
	 * Author-date: vishnu
	 * Reviewer-date: 
	 */
    public String evaluateJSONPath(String jsPath) {
		String respValue = null;
		try { 
			Object resp = JsonPath.read(jsRoot, jsPath);				
			respValue = getResponseValue(resp);
			return String.valueOf(respValue);
			
		}catch(InvalidJsonException jsExp) {
			jsExp.printStackTrace();
		}
		return respValue;		
	}    
	
	
	/*
	 * Purpose: To get the exact value from input jsonpath response object. 
	 * 			Check if response is json array with one element & return the exact value
	 * Author-date: vishnu
	 * Reviewer-date:  
	 */
	private String getResponseValue(Object resp) {
	
		System.out.println(resp.getClass().getName());
		
		String respValue = "";					
		switch (resp.getClass().getSimpleName()) {
			case "String":
			case "Double":
			case "Integer":
			case "Boolean":
			case "LinkedHashMap":				
				respValue = String.valueOf(resp);
				break;
				
			case "JSONArray":
				List<Object> respColl = (List<Object>) resp;									
				if(respColl.size() == 1 ) {
					respValue = getResponseValue(respColl.get(0));
				}else {
					respValue = String.valueOf(respColl);
				}
				break;
				
			default:
				System.out.println("Unknown json element type " + resp.getClass());
		}
		return respValue;
	}
}
