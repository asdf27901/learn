package org.example.utils;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.charts.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.charts.XSSFChartLegend;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static Map<File,XSSFWorkbook> xssfWorkbookMap = new HashMap<>();

    private static File file;

    public static File getFile() {
        return file;
    }

    public static void setFile(File file) {
        ExcelUtils.file = file;
    }

    public static void getColumn(XSSFSheet xssfSheet, String str, int count){

        String[] aa = str.split(",");

        XSSFRow xssfRow = xssfSheet.createRow(count);

        for (int i = 0; i < aa.length; i++) {
            if (i != 5 || count == 0) xssfRow.createCell(i).setCellValue(aa[i]);
            else xssfRow.createCell(i).setCellValue(Double.parseDouble(aa[i]));
            xssfSheet.autoSizeColumn(i);
        }
    }

    public static Integer getColumn(XSSFSheet xssfSheet,int count,int times){

        XSSFRow xssfRow;

        if (times == 0 || times == 1) return count;

        if (count == 1){
            for (int i = 1; i <= times; i++) {
                xssfRow = xssfSheet.createRow(i);
                xssfRow.createCell(1).setCellValue("#!数据丢失");
                xssfRow.getCell(1).setCellStyle(setColor(0));
                xssfSheet.autoSizeColumn(1);
            }
            return count + times;

        } else {
            for (int i = 1; i < times; i++) {
                xssfRow = xssfSheet.createRow(count-1+i);
                xssfRow.createCell(1).setCellValue("#!数据丢失");
                xssfRow.getCell(1).setCellStyle(setColor(0));
                xssfSheet.autoSizeColumn(1);
            }
            return count - 1 + times;
        }
    }

    public static void getExceptionColumn(XSSFSheet xssfSheet,String str,int count){

        XSSFRow xssfRow = xssfSheet.createRow(count);
        xssfRow.createCell(0).setCellValue(str);
        xssfRow.createCell(1).setCellValue("#!GGA没有出解");
        xssfRow.getCell(0).setCellStyle(setColor(0));
        xssfRow.getCell(1).setCellStyle(setColor(0));
        xssfSheet.autoSizeColumn(0);
        xssfSheet.autoSizeColumn(1);
    }

    public static CellStyle setColor(int choose){
        XSSFCellStyle xssfCellStyle = xssfWorkbookMap.get(file).createCellStyle();
        xssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        if (choose == 0){
            xssfCellStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
            xssfCellStyle.setFillBackgroundColor(HSSFColor.ORANGE.index);
        }else if (choose == 1){
            xssfCellStyle.setFillForegroundColor(HSSFColor.RED.index);
            xssfCellStyle.setFillBackgroundColor(HSSFColor.RED.index);
        }
        return xssfCellStyle;
    }

    public static void createChart(XSSFSheet xssfSheet,int length){
        XSSFDrawing xssfDrawing = xssfSheet.createDrawingPatriarch();

        XSSFClientAnchor anchor = xssfDrawing.createAnchor(0,0,0,0,10,35,20,58);

        XSSFChart xssfChart = xssfDrawing.createChart(anchor);

        XSSFChartLegend xssfChartLegend = xssfChart.getOrCreateLegend();

        xssfChartLegend.setPosition(LegendPosition.BOTTOM);

        LineChartData data = xssfChart.getChartDataFactory().createLineChartData();
        ChartAxis chartAxis = xssfChart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
        chartAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        chartAxis.setMajorTickMark(AxisTickMark.CROSS);
        ValueAxis valueAxis = xssfChart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
        valueAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        ChartDataSource<Number> y = DataSources.fromNumericCellRange(xssfSheet,new CellRangeAddress(1,length,5,5));
        ChartDataSource<Number> x = DataSources.fromNumericCellRange(xssfSheet,new CellRangeAddress(1,length,0,0));

        LineChartSeries lineChartSeries = data.addSeries(x,y);
        lineChartSeries.setTitle("距离真值点的距离 单位/米");
        xssfChart.setTitleText("统计到打点距离折线图");
        xssfChart.plot(data, chartAxis,valueAxis);
    }

    public static void getSameGGATip(XSSFSheet xssfSheet,int count){
        XSSFRow xssfRow = xssfSheet.getRow(count);
        xssfRow.createCell(6).setCellValue("#!GGA时间相同");
        xssfRow.getCell(6).setCellStyle(setColor(0));
        xssfSheet.autoSizeColumn(1);
    }
}
