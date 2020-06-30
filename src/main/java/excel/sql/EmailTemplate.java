package excel.sql;

import lombok.Data;

/**
 * @author keith
 */
@Data
public class EmailTemplate extends ExcelBean{
    private String comCategory;
    private String category;
    private String subject;
    private String addTime;
    private String remark;
    private String enable;
    private String emailPlatform;
    private String content;
    private String usedCount;
    private String updateInterval;

}
