package gen.controller;

import excel.sql.ExcelBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelToSql {
    public static void executeInfo() throws IOException {

        String filePath = "E:/WORKSPACE/urgent 韩国子站-邮件公共模板-已翻译0413 (1).xlsx";    //指定本地的数据目录
        String slqPath = "E:/WORKSPACE/email_template_common.sql";    //指定生成文件目录
        try {
            InputStream in = new FileInputStream(filePath);
            Workbook wb = new XSSFWorkbook(in);
            in.close();
            String sql = "";
            Sheet sheetAt = wb.getSheetAt(0);
            sql = "INSERT INTO `mm_email_template_common` (`type`, `subject`, `content`, `language`, `remark`) VALUES \n";
            int lastRowNum = 10;
            System.out.println("rowNum = " + lastRowNum);
            List<ExcelBean> excelBean = getExcelBean(sheetAt, lastRowNum);

            int i = 0;
            String pre = "<!DOCTYPE html><html lang=\\\"en\\\"><head><meta charset=\\\"UTF-8\\\"><meta http-equiv=\\\"X-UA-Compatible\\\" content=\\\"ie=edge\\\"><title></title></head><body><table width=\\\"100%\\\"><tr><td></td><td width=\\\"750\\\"><table width=\\\"750\\\" bgcolor=\\\"#f7f7f7\\\" border=\\\"0\\\" cellspacing=\\\"0\\\" cellpadding=\\\"0\\\"><tr><td colspan=\\\"3\\\">";
            String suff = "</td></tr></table></td><td width=\\\"30\\\">&nbsp;</td></tr></table></td><td></td></tr></table></body></html>";
            for (ExcelBean bean : excelBean) {
                if (i == 0) {
                    i++;
                } else {
                    sql += String.format("('%s', '%s', '%s%s%s', '%s' ,'%s')", i++, bean.getSubject(), pre, bean.getContent(), suff, 3, bean.getRemark());
                    if (i == lastRowNum) {
                        sql += ";";
                    } else {
                        sql += ",\n";
                    }
                }
            }

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

    private static List<ExcelBean> getExcelBean(Sheet sheetAt, int lastRowNum) {
        List<ExcelBean> list = new ArrayList<>();
        for (int rowNum = 0; rowNum < lastRowNum; rowNum++) {
            ExcelBean bean = new ExcelBean();    //创建一个单独Bean、
            Row row = sheetAt.getRow(rowNum);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    bean.setSubject(cell.getStringCellValue());
                }
                cell = row.getCell(3);
                if (cell != null) {
                    bean.setContent(cell.getStringCellValue());
                }
                cell = row.getCell(4);
                if (cell != null) {
                    bean.setRemark(cell.getStringCellValue());
                }
                /**
                 * 更多列在这里补充
                 */
            }
            list.add(bean);
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
