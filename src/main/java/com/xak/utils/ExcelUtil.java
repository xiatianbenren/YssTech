package com.xak.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 工具所需依赖：
 * writer: xiaankang
 * date: 2019/12/29.
 *
 * <dependency>
 *       <groupId>org.apache.poi</groupId>
 *       <artifactId>poi</artifactId>
 *       <version>3.10.1</version>
 *     </dependency>
 *     <dependency>
 *       <groupId>org.apache.poi</groupId>
 *       <artifactId>poi-ooxml</artifactId>
 *       <version>3.10.1</version>
 *     </dependency>
 *     <dependency>
 *       <groupId>org.apache.poi</groupId>
 *       <artifactId>poi-ooxml-schemas</artifactId>
 *       <version>3.10.1</version>
 *     </dependency>
 */
public class ExcelUtil {

    //测试成功
   /* public static void main(String[] args) throws Exception {
        String file="F:\\org_unit.xls";
        List<Integer> list=new ArrayList<>();
        list.add(0);
        list.add(1);
        readExcel(file,list);
    }*/

    /**
     * 解析Excel
     * @param absoluteFileName excel 绝对路径
     * @param sheetIndexs 指定解析sheet序号（从0开始）
     * @throws Exception
     */
    public static void readExcel(String absoluteFileName, List<Integer> sheetIndexs)throws Exception{
        //创建对象
        Workbook workbook=createWorkbook(absoluteFileName);
        for (int i = 0; i < sheetIndexs.size(); i++) {
            //获取目标sheet索引
            Integer sheetIndex = sheetIndexs.get(i);
            //解析sheet
            dealSheet(workbook,sheetIndex);
            System.out.println();
            System.out.println("//////////////第 "+i+" 个sheet表解析完成///////////////");
            System.out.println();
        }
        workbook=null;
    }

    /**
     * 解析指定sheet
     * @param workbook
     * @param sheetIndex
     */
    private static void dealSheet(Workbook workbook,Integer sheetIndex){
        //获取工作表
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            //获取行,行号作为参数传递给getRow方法,第一行从0开始计算
            Row row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                String value = fetchType2getValue(cell)+"";
                //TODO:out put strategy
                System.out.print(value+"  ");
                cell=null;
            }
            System.out.println();
            row=null;
        }
    }

    /**
     * 根据单元格类型获取其中的数据
     * @param cell
     * @return
     */
    private static Object fetchType2getValue(Cell cell){
        if (cell == null) {
            return "";
        }
        int type = cell.getCellType();
        switch (type){
            case Cell.CELL_TYPE_NUMERIC:
                String value = cell.getNumericCellValue() + "";
                if(HSSFDateUtil.isCellDateFormatted(cell)){
                    Date dateCellValue = cell.getDateCellValue();
                    if (dateCellValue != null) {
                        value = new SimpleDateFormat("yyyy-mm-dd").format(dateCellValue);
                    }else{
                        value="";
                    }
                }else{
                    return new DecimalFormat("0").format(cell.getNumericCellValue());
                }
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula()+"";
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue()+"";
            case Cell.CELL_TYPE_ERROR:
                return "ERROR:Illegle words";
            default:
                return "";
        }
    }

    /**
     * 创建对象
     * @param absoluteFileName
     * @return
     */
    private static Workbook createWorkbook(String absoluteFileName){
        FileInputStream fis =null;
        try {
             fis = new FileInputStream(new File(absoluteFileName));
            if(absoluteFileName.endsWith(".xls")){
                return new HSSFWorkbook(fis);
            }else if (absoluteFileName.endsWith(".xlsx")){
                return new XSSFWorkbook(fis);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("文件无法创建");
    }
}
