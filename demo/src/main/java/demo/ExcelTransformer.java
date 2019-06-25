package demo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

public class ExcelTransformer {

	public Object transformMessage(MuleMessage muleMessage, String outputEncoding) throws TransformerException 
	{

	    Map<String,String> keys = new HashMap<>();
	    ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	    Map<String,ArrayList<Map<String, String>>> outList = new HashMap<>();
	    int i = 0, j = 0;

	    try  {

	        InputStream inp = (InputStream) muleMessage.getPayload();
	        Workbook wb = WorkbookFactory.create(inp);
	        Sheet sheet = wb.getSheetAt(0);

	        //Iterate through each rows one by one
	        Iterator<Row> rowIterator = sheet.iterator();
	        while (rowIterator.hasNext())
	        {
	            i++;

	            Row row = rowIterator.next();
	            //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();

	            if (i == 1)
	            {
	                j = 0;
	                while (cellIterator.hasNext()) 
	                {
	                    Cell cell = cellIterator.next();
	                    keys.put("f"+j, cell.getStringCellValue());
	                    j++;
	                }
	            }
	            else 
	            {
	                Map<String,String> map = new HashMap<>();

	                j = 0;
	                while (cellIterator.hasNext()) 
	                {
	                    Cell cell = cellIterator.next();

	                    String k = keys.get("f"+j);
	                    String v = cell.getStringCellValue(); 
	                    map.put(k, v);
	                    j++;
	                }

	                list.add(map);
	            }
	        }

	        inp.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {

	    }

	    outList.put("root",list);
	    return outList;

	}
}
