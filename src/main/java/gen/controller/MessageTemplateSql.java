package gen.controller;

import excel.sql.MessageTemplate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MessageTemplateSql {
    public static void executeInfo() throws IOException {

        String filePath = "E:\\workspace/4. Translated 0617(1).xlsx";    //指定本地的数据目录
        String slqPath = "E:/WORKSPACE/message_template.sql";    //指定生成文件目录
        try {
            InputStream in = new FileInputStream(filePath);
            Workbook wb = new XSSFWorkbook(in);
            in.close();
            String sql = "";
            Sheet sheetAt = wb.getSheetAt(0);
            sql = "INSERT INTO `mm_message_template` (`language`, `id`, `subject_type`, `name`, `title`, `site_msg`, `email`, `phone`, `type`, `new_type`, `cat`, `update_interval`) VALUES \n";
            int lastRowNum = 186;
            System.out.println("rowNum = " + lastRowNum);
            List<MessageTemplate> excelBean = getExcelBean(sheetAt, lastRowNum);

            int i = 0;
            for (MessageTemplate bean : excelBean) {
                if (i == 0) {
                    i++;
                } else {
                    sql += String.format("('3', '%s', '%s', '%s','%s','%s','%s','%s','%s','%s','%s','%s')", bean.getId(), bean.getSubjectType(), bean.getName().replaceAll("'", "\""), bean.getTitle().replaceAll("'", "\""), bean.getSiteMsg().replaceAll("'", "\""), bean.getEmail().replaceAll("'", "\""), bean.getPhone(), bean.getType(), bean.getNewType(), bean.getCat(), bean.getUpdateInterval());
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

    private static List<MessageTemplate> getExcelBean(Sheet sheetAt, int lastRowNum) {
        List<MessageTemplate> list = new ArrayList<>();
        for (int rowNum = 0; rowNum < lastRowNum; rowNum++) {
            MessageTemplate bean = new MessageTemplate();    //创建一个单独Bean、
            Row row = sheetAt.getRow(rowNum);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setId(cell.getStringCellValue());
                }
                cell = row.getCell(1);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setSubjectType(cell.getStringCellValue());
                }
                cell = row.getCell(2);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setName(cell.getStringCellValue());
                }

                cell = row.getCell(5);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setTitle(cell.getStringCellValue());
                }
                cell = row.getCell(7);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setSiteMsg(cell.getStringCellValue());
                }
                cell = row.getCell(9);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setEmail(cell.getStringCellValue());
                }
                cell = row.getCell(11);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setPhone(cell.getStringCellValue());
                }
                cell = row.getCell(12);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setType(cell.getStringCellValue());
                }
                cell = row.getCell(13);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setNewType(cell.getStringCellValue());
                }
                cell = row.getCell(14);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setCat(cell.getStringCellValue());
                }
                cell = row.getCell(15);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    bean.setUpdateInterval(cell.getStringCellValue());
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
