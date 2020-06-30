package gen.controller;

import excel.sql.Help;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HelpCenterExcel {
    public static void executeInfo() throws IOException {

        String filePath = "E:/workspace/Translated file of help center (1).xlsx";    //指定本地的数据目录
        String slqPath = "E:/workspace/helpkr2.0.sql";    //指定生成文件目录
        try {
            InputStream in = new FileInputStream(filePath);
            Workbook wb = new XSSFWorkbook(in);
            in.close();
            String sql = "";
            Sheet sheetAt = wb.getSheetAt(0);
            sql = "";
            int lastRowNum = 224;
            System.out.println("rowNum = " + lastRowNum);
            List<Help> excelBean = getExcelBean(sheetAt, lastRowNum);

            int i = 0;
            for (Help bean : excelBean) {
                if (i == 0) {
                    i++;
                } else {
                    String content = bean.getContent() == null ? "" : bean.getContent().replaceAll("'", "\"");
                    sql += String.format("UPDATE nm_help SET help_content = '%s', help_title = '%s' WHERE help_title = '%s' and language_id = %d;\n", content, bean.getKrTitle() == null ? "" : bean.getKrTitle().replaceAll("'", "\""), bean.getTitle() == null ? "" : bean.getTitle(), 3);
                }
            }
            System.out.println(sql);
            File file = new File(slqPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] bytes = sql.getBytes();
            OutputStream os = new FileOutputStream(slqPath);
            os.write(bytes);
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static List<Help> getExcelBean(Sheet sheetAt, int lastRowNum) {
        List<Help> list = new ArrayList<>();
        for (int rowNum = 0; rowNum < lastRowNum; rowNum++) {
            try {
                Help bean = new Help();    //创建一个单独Bean、
                Row row = sheetAt.getRow(rowNum);
                if (row != null) {
                    Cell cell = row.getCell(0);
                    if (cell != null) {
                        bean.setTitle(cell.getStringCellValue());
                    }
                    cell = row.getCell(1);
                    if (cell != null) {
                        bean.setKrTitle(cell.getStringCellValue());
                    }
                    cell = row.getCell(4);
                    if (cell != null) {
                        bean.setContent(cell.getStringCellValue());
                    }
                    /**
                     * 更多列在这里补充
                     */
                }
                list.add(bean);
            } catch (Exception e) {
                System.out.println("row = " + rowNum);
                e.printStackTrace();
            }
        }
        return list;
    }


    public static void main(String args[]) throws IOException {
        long startTime = System.currentTimeMillis();
        executeInfo();
        long endTime = System.currentTimeMillis();
        System.out.println("耗时为m:" + (endTime - startTime) / 1000);
    }

}
