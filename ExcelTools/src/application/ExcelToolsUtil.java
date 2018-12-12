package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author fmbah
 * 表格读取，写入
 */
public class ExcelToolsUtil {
	
	@SuppressWarnings("unchecked")
	public static List<HashMap> readExcel(String sourceFilePath) throws Exception {
        Workbook workbook = null;

        try {
            workbook = getReadWorkBookType(sourceFilePath);
            List<HashMap> contents = new ArrayList<>();

            //获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            //第0， 1行是表名，忽略，从第三行开始读取
            for (int rowNum = 2; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                Cell cell5 = row.getCell(6);//卡号
                Cell cell1 = row.getCell(1);//姓名
                Cell cell4 = row.getCell(4);//税后金额
                if (cell1 != null && cell4 != null && cell5 != null) {
                	HashMap tmp = new HashMap();
                    tmp.put("name",  getCellStringVal(cell1).trim());
                    tmp.put("num",  getCellStringVal(cell5).trim());
                    tmp.put("money",  getCellStringVal(cell4).trim());
                    contents.add(tmp);
                }
            }
            return contents;
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }
	
	public static void writeExcel(String targetFilePath, List<? extends HashMap> contents) throws Exception {
        Workbook workbook = null;
        FileOutputStream fos = null;

        workbook = getWriteWorkBoolType(targetFilePath);

        //创建sheet
        Sheet sheet = workbook.createSheet("transorm_data");
        //在sheet第一行写出表单的各个字段名
        Row titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("序号");
        titleRow.createCell(1).setCellValue("账号");
        titleRow.createCell(2).setCellValue("户名");
        titleRow.createCell(3).setCellValue("金额");
        titleRow.createCell(4).setCellValue("跨行标识");
        titleRow.createCell(5).setCellValue("行名");
        titleRow.createCell(6).setCellValue("联行行号");
        titleRow.createCell(7).setCellValue("摘要");
        titleRow.createCell(8).setCellValue("备注");
         
        //每行的单元格一次写入
        Integer rowIndex = contents.size();
        for (int i = 0; i < rowIndex; i++) {
            //第1行作为表格列名，所以从第2行开始读取
            Row row = sheet.createRow(i + 1);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(i + 1);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue((String)contents.get(i).get("num"));
            Cell cell2 = row.createCell(2);
            cell2.setCellValue((String)contents.get(i).get("name"));
            Cell cell3 = row.createCell(3);
            cell3.setCellValue((String)contents.get(i).get("money"));
        }
       //写入到文件流中
        try {
            fos = new FileOutputStream(targetFilePath);
            workbook.write(fos);
        } catch (IOException e) {
        	Tools.logger.log(Level.SEVERE, "写入文件异常", e);
        	throw new Exception("写入文件异常");
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }

	private static Workbook getReadWorkBookType(String filePath) throws Exception {
        //xls-2003, xlsx-2007
        FileInputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (filePath.toLowerCase().endsWith("xlsx")) {
                return new XSSFWorkbook();
            } else if (filePath.toLowerCase().endsWith("xls")) {
                return new HSSFWorkbook(is);
            } else {
            	Exception e = new Exception("文件类型有误");
            	Tools.logger.log(Level.SEVERE, "文件类型有误", e);
            	throw e;
            }
        } catch (IOException e) {
        	Tools.logger.log(Level.SEVERE, "创建文件异常", e);
        	throw new Exception("创建文件异常");
        } finally {
            IOUtils.closeQuietly(is);
        }
  }
	
	private static Workbook getWriteWorkBoolType(String filePath) throws Exception{
		if (filePath.toLowerCase().endsWith("xlsx")) {
            return new XSSFWorkbook();
        } else if (filePath.toLowerCase().endsWith("xls")) {
            return new HSSFWorkbook();
        } else {
        	Exception e = new Exception("创建文件异常");
        	Tools.logger.log(Level.SEVERE, "创建文件异常", e);
        	throw e;
        }
    }

	private static String getCellStringVal(Cell cell) {
        @SuppressWarnings("deprecation")
		CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
            case NUMERIC:
                return cell.getNumericCellValue() + "";
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            default:
                return "";
        }
    }
	
	public static void main(String[] args) {
		System.out.println((double)1 / 4);
	}

}
