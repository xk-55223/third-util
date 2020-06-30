package excel.sql;

import lombok.Data;

@Data
public class MessageTemplate {
    private String id;
    private String subjectType;
    private String name;
    private String title;
    private String siteMsg;
    private String email;
    private String phone;
    private String type;
    private String newType;
    private String cat;
    private String updateInterval;
}
