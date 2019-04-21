package com.business.exchange.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 解析工具，支持生成Excel、导出为Excel
 */
public class ExcelAnalyzer {

    public static final String EMPLOYEE_ID_KEY = "employeeID";
    private static final String EMPLOYEE_ID_COLUMN_NAME = "工号";

    public static final String USERNAME_KEY = "userName";
    private static final String USERNAME_COLUMN_NAME = "姓名";

    public static final String DEPARTMENT_KEY = "department";
    private static final String DEPARTMENT_COLUMN_NAME = "部门";

    public static final String GROUP_KEY = "group";
    private static final String GROUP_COLUMN_NAME = "领域";

    public static final String OKR_NUMBER_KEY = "okrNumber";
    private static final String OKR_NUMBER_COLUMN_NAME = "当前OKR币";

    private static final int MAX_ROW = 10000;

    //导出表格的列宽
    private static final int COLUMN_WIDTH = 10;

    /**
     * 解析Excel中的信息
     *
     * @param filePath
     * @return
     */
    public static List<Map<String, String>> analyzer(String filePath) {
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String, String>> list = null;
        String cellData = null;
        wb = readExcel(filePath);
        if (wb != null) {
            //用来存放表中数据
            list = new ArrayList<Map<String, String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rowNumber = sheet.getPhysicalNumberOfRows();
            if (rowNumber > MAX_ROW) {
                rowNumber = MAX_ROW;
            }
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int column = row.getPhysicalNumberOfCells();

            //各个属性所在列号
            int employeeIDColumnNumber = -1;
            int okrNumberColumnNumber = -1;
            int userNameColumnNumber = -1;
            int departmentColumnNumber = -1;
            int groupColumnNumber = -1;

            if (row != null) {
                for (int j = 0; j < column; j++) {
                    cellData = (String) getCellFormatValue(row.getCell(j));
                    System.out.println("cell data: " + cellData);
                    //判断各个属性所在列
                    if (EMPLOYEE_ID_COLUMN_NAME.equals(cellData) || cellData.contains(EMPLOYEE_ID_COLUMN_NAME)) {
                        employeeIDColumnNumber = j;
                    } else if (OKR_NUMBER_COLUMN_NAME.equals(cellData) || cellData.contains(OKR_NUMBER_COLUMN_NAME)) {
                        okrNumberColumnNumber = j;
                    } else if (USERNAME_COLUMN_NAME.equals(cellData) || cellData.contains(USERNAME_COLUMN_NAME)) {
                        userNameColumnNumber = j;
                    } else if (DEPARTMENT_COLUMN_NAME.equals(cellData) || cellData.contains(DEPARTMENT_COLUMN_NAME)) {
                        departmentColumnNumber = j;
                    } else if (GROUP_COLUMN_NAME.equals(cellData) || cellData.contains(GROUP_COLUMN_NAME)) {
                        groupColumnNumber = j;
                    }
                }
            }

            for (int i = 1; i < rowNumber; i++) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < column; j++) {
                        cellData = (String) getCellFormatValue(row.getCell(j));
                        System.out.println("cell data: " + cellData);

                        //将对应列属性值提取到Map中
                        if (j == employeeIDColumnNumber) {
                            map.put(EMPLOYEE_ID_KEY, cellData);
                        } else if (j == okrNumberColumnNumber) {
                            map.put(OKR_NUMBER_KEY, cellData);
                        } else if (j == userNameColumnNumber) {
                            map.put(USERNAME_KEY, cellData);
                        } else if (j == departmentColumnNumber) {
                            map.put(DEPARTMENT_KEY, cellData);
                        } else if (j == groupColumnNumber) {
                            map.put(GROUP_KEY, cellData);
                        }
                    }
                } else {
                    break;
                }
                list.add(map);
            }
        }
        return list;
    }

    //读取excel
    private static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    private static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        sheet.setDefaultColumnWidth(COLUMN_WIDTH);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                cell = row.createCell(j);
                cell.setCellValue(values[i][j]);
                cell.setCellStyle(style);
            }
        }
        return wb;
    }
}
