package com.welb.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.welb.sysBase.util.Constant.DATE_TIME_FORMAT_PATTERN;

public class ObjectExcelRead {

    public static List<Object> readExcelForSpecDateFormat(String filePath, String fileName, int startRow, int startCol, int sheetNum, String dateFormat){
        List<Object> varList = new ArrayList<Object>();

        try {
            File target = new File(filePath, fileName);
            FileInputStream fi = new FileInputStream(target);

            Workbook wb;
            //根据文件后缀（xls/xlsx）进行判断
            String[] split = fileName.split("\\.");
            if ( "xls".equals(split[1])){
                wb = new HSSFWorkbook(fi);
            }else if ("xlsx".equals(split[1])){
                wb = new XSSFWorkbook(fi);
            }else {
                System.out.println("文件类型错误!");
                return null;
            }

            //开始解析
            Sheet sheet = wb.getSheetAt(sheetNum);     //读取sheet 0
            int rowNum = sheet.getLastRowNum() + 1; // 取得最后一行的行号

            for (int i = startRow; i < rowNum; i++) { // 行循环开始

                PageData varpd = new PageData();
                Row row = sheet.getRow(i); // 行
                int cellNum = row.getLastCellNum(); // 每行的最后一个单元格位置

                for (int j = startCol; j < cellNum; j++) { // 列循环开始

                    Cell cell = row.getCell(Short.parseShort(j + ""));
                    String cellValue = null;
                    if (null != cell) {
                        switch (cell.getCellType()) { // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                            case 0:
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    Date date = cell.getDateCellValue();
                                    cellValue = DateFormatUtils.format(date, dateFormat);
                                } else {
                                    DecimalFormat df = new DecimalFormat("#.###");
                                    cellValue = df.format(cell.getNumericCellValue());
                                }
                                break;
                            case 1:
                                cellValue = cell.getStringCellValue();
                                break;
                            case 2:
                                cellValue = cell.getNumericCellValue() + "";
                                //cellValue = String.valueOf(cell.getDateCellValue());
                                break;
                            case 3:
                                cellValue = "";
                                break;
                            case 4:
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case 5:
                                cellValue = String.valueOf(cell.getErrorCellValue());
                                break;
                        }
                    } else {
                        cellValue = "";
                    }

                    varpd.put("var" + j, cellValue);

                }
                varList.add(varpd);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return varList;
    }

    /**
     * @param filePath //文件路径
     * @param fileName //文件名
     * @param startRow //开始行号
     * @param startCol //开始列号
     * @param sheetNum //sheet
     * @return list
     */
    public static List<Object> readExcel(String filePath, String fileName, int startRow, int startCol, int sheetNum) {
        return readExcelForSpecDateFormat(filePath, fileName, startRow, startCol, sheetNum, DATE_TIME_FORMAT_PATTERN);
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    // 删除文件
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    // 删除文件夹
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }
}
