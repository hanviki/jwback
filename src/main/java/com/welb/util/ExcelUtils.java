package com.welb.util;

import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtils {
    /**
     *
     * @param response
     * @param dataSource - 数据源
     * @param sheetName - sheet名称
     * @param firstRowData - 第一行数据下标
     * @param templateName - 模板名称
     * @param unusedColumns - 无需展示的列
     */
    public static<T> void exportExcel(HttpServletResponse response, Collection<T> dataSource, String sheetName, int firstRowData, String templateName, List<String> unusedColumns) {
        InputStream input = null;
        OutputStream output = null;
        try {
            //获取数据
            if (dataSource == null || dataSource.size() == 0) {
                System.out.println("无数据!");
                return;
            }
            //拿到模板文件
            try {
                input = new ClassPathResource("templates/" + templateName).getInputStream();
            } catch (Exception e) {
                input = new FileInputStream(ResourceUtils.getFile("classpath:templates/" + templateName));
            }
            //通过模板创建新的Excel
            XSSFWorkbook workbook = new XSSFWorkbook(input);
            //填充数据
            populateData(workbook, dataSource, firstRowData, unusedColumns);
            //输出Excel内容,生成Excel文件
            output = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(sheetName.getBytes(),"ISO8859-1") + ".xlsx" );
            workbook.write(output);

            System.out.println(sheetName + "导出成功!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(sheetName + "导出错误!");
        } finally {
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (output != null){
                try {
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(sheetName + "导出结束!");
        }

    }

    public static<T> void exportExcel(HttpServletResponse response, String sheetName, String templateName) {
        InputStream input = null;
        OutputStream output = null;
        try {
            //拿到模板文件
            try {
                input = new ClassPathResource("templates/" + templateName).getInputStream();
            } catch (Exception e) {
                input = new FileInputStream(ResourceUtils.getFile("classpath:templates/" + templateName));
            }
            //通过模板创建新的Excel
            XSSFWorkbook workbook = new XSSFWorkbook(input);
            //输出Excel内容,生成Excel文件
            output = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(sheetName.getBytes(),"ISO8859-1") + ".xlsx" );
            workbook.write(output);
            System.out.println(sheetName + "导出成功!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(sheetName + "导出错误!");
        } finally {
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(sheetName + "导出结束!");
        }
    }


    /**
     * 填充数据
     * @param workbook
     * @param dataSource - 数据源
     * @param firstRowData - 第一行数据下标
     * @param unusedColumns - 无需展示的列
     * @param <T>
     */
    private static<T> void populateData(XSSFWorkbook workbook, Collection<T> dataSource, int firstRowData, List<String> unusedColumns){
        //设置样式和字体
        XSSFCellStyle style = setCellStyleAndFont(workbook);

        //填充数据
        Iterator<T> it = dataSource.iterator();
        T t;
        List<Field> fieldsList;
        Field field;
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;
        XSSFRichTextString richString;
        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
        Matcher matcher;
        String fieldName;
        String getMethodName;
        Class tCls;
        Method getMethod;
        Object value;
        String textValue;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (it.hasNext()) {
            firstRowData++;
            row = sheet.createRow(firstRowData);
            t = (T)it.next();
            fieldsList = removeUnusedColumns(t.getClass().getDeclaredFields(), unusedColumns);
            for (int i = 0; i < fieldsList.size(); i++) {
                cell = row.createCell(i);
                cell.setCellStyle(style);

                field = fieldsList.get(i);
                fieldName = field.getName();
                getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    tCls = t.getClass();
                    getMethod = tCls.getMethod(getMethodName, new Class[] {});
                    value = getMethod.invoke(t, new Object[] {});
                    //判断值类型后进行强制类型转换
                    textValue = null;
                    if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Float) {
                        textValue = String.valueOf((Float) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Double) {
                        textValue = String.valueOf((Double) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Long) {
                        cell.setCellValue((Long) value);
                    }
                    if (value instanceof Boolean) {
                        textValue = "是";
                        if (!(Boolean) value) {
                            textValue = "否";
                        }
                    } else if (value instanceof Date) {
                        textValue = sdf.format((Date) value);
                    } else {
                        //其它数据类型都当作字符串简单处理
                        if (value != null) {
                            textValue = value.toString();
                        }
                    }
                    if (textValue != null) {
                        matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            //数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            richString = new XSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        //自动调整列宽(不适用中文)
        for (int k = 0; k < dataSource.size(); k++){
            sheet.autoSizeColumn(k);
        }
//        //调整中文列列宽
//        setSizeColumn(sheet, dataSource.size());

    }

    /**
     * 移除掉某些不需要在Excel中展示的列
     * @param fields - 对象的所有属性
     * @param unusedColumns - 无使用的列(属性)
     */
    public static List<Field> removeUnusedColumns(Field[] fields, List<String> unusedColumns) {
        List<Field> fieldList = new ArrayList<>(Arrays.asList(fields));
        if (unusedColumns != null && !unusedColumns.isEmpty()){
            for (String attributeName : unusedColumns) {
                for (int i = 0; i < fieldList.size(); i++){
                    if (attributeName.equals(fieldList.get(i).getName()))
                        fieldList.remove(i);
                }
            }
        }
        return fieldList;
    }

    /**
     * 设置样式和字体
     * @param workbook
     * @return
     */
    private static XSSFCellStyle setCellStyleAndFont(XSSFWorkbook workbook) {
        //生成并设置样式
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(java.awt.Color.WHITE));
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        //生成字体
        XSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        //把字体应用到当前的样式
        style.setFont(font);

        return style;
    }

    /**
     * 调整列的宽度
     * @param sheet
     * @param size
     */
    private static void setSizeColumn(XSSFSheet sheet, int size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                XSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    XSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }

            int colWidth = columnWidth * 256;
            if(colWidth<255*256){
                sheet.setColumnWidth(columnNum, colWidth < 3000 ? 3000 : colWidth);
            }else{
                sheet.setColumnWidth(columnNum,6000 );
            }
        }
    }
}
