package gen.controller;

import excel.sql.EmailTemplate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelToSql {
    public static void executeInfo() throws IOException {

        String filePath = "E:\\workspace/4.수퍼바이 번역 5th(Need to complete the translation) 3,4sheet(1).xlsx";    //指定本地的数据目录
        String slqPath = "E:/WORKSPACE/email_template.sql";    //指定生成文件目录
        try {
            InputStream in = new FileInputStream(filePath);
            Workbook wb = new XSSFWorkbook(in);
            in.close();
            String sql = "";
            Sheet sheetAt = wb.getSheetAt(2);
            sql = "INSERT INTO `mm_email_template` (`language`, `com_category`, `category`, `subject`, `add_time`, `remark`, `enable`, `email_platform`, `content`) VALUES \n";
            int lastRowNum = 63;
            System.out.println("rowNum = " + lastRowNum);
            List<EmailTemplate> excelBean = getExcelBean(sheetAt, lastRowNum);

            int i = 0;
            String pre = "<!DOCTYPE html><html lang=\\\"en\\\"><head><meta charset=\\\"UTF-8\\\"><meta http-equiv=\\\"X-UA-Compatible\\\" content=\\\"ie=edge\\\"><title></title></head><body><table width=\\\"100%\\\"><tr><td></td><td width=\\\"750\\\"><table width=\\\"750\\\" bgcolor=\\\"#f7f7f7\\\" border=\\\"0\\\" cellspacing=\\\"0\\\" cellpadding=\\\"0\\\"><tr><td colspan=\\\"3\\\">";
            String suff = "</td></tr></table></td><td width=\\\"30\\\">&nbsp;</td></tr></table></td><td></td></tr></table></body></html>";
            for (EmailTemplate bean : excelBean) {
                if (i == 0) {
                    i++;
                } else {
                    sql += String.format("('3', '%s', '%s', '%s','%s','%s','%s','%s','%s%s%s')", bean.getComCategory(), bean.getCategory(), bean.getSubject(),bean.getAddTime(),bean.getRemark(), bean.getEnable(), bean.getEmailPlatform(), pre, bean.getContent().replaceAll("'", "\""), suff);
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

    private static List<EmailTemplate> getExcelBean(Sheet sheetAt, int lastRowNum) {
        List<EmailTemplate> list = new ArrayList<>();
        for (int rowNum = 0; rowNum < lastRowNum; rowNum++) {
            EmailTemplate bean = new EmailTemplate();    //创建一个单独Bean、
            Row row = sheetAt.getRow(rowNum);
            if (row != null) {
                Cell cell = row.getCell(1);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setComCategory(cell.getStringCellValue());
                }
                cell = row.getCell(2);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setCategory(cell.getStringCellValue());
                }
                cell = row.getCell(4);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setSubject(cell.getStringCellValue());
                }

                cell = row.getCell(5);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setAddTime(cell.getStringCellValue());
                }
                cell = row.getCell(6);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setRemark(cell.getStringCellValue());
                }
                cell = row.getCell(8);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setEnable(cell.getStringCellValue());
                }
                cell = row.getCell(9);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setUsedCount(cell.getStringCellValue());
                }
                cell = row.getCell(10);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setUpdateInterval(cell.getStringCellValue());
                }
                cell = row.getCell(12);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setEmailPlatform(cell.getStringCellValue());
                }
                cell = row.getCell(14);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setContent(cell.getStringCellValue());
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
