package gen.controller;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.xbill.DNS.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmailVerify {

    public static void main(String[] args) {
        // 可校验邮箱 @163.com、@126.com、@139.com
        // 不可校验邮箱 @qq.com、@189.cn、@sina.com、@gmail.com、@sohu.com

//        System.out.println(verifyEmail("xunwuy425723@163.com", "www.superbuy.com"));
//        System.out.println(verifyEmail("gd_wenlong@163.com", "www.superbuy.com"));
//        System.out.println(verifyEmail("261545asdsa961@126.com", "www.superbuy.com"));
//        System.out.println(verifyEmail("1343023210428@139.com", "www.superbuy.com"));
//        System.out.println(verifyEmail("261zxcz54aweqw5asdsa961@189.cn", "www.superbuy.com"));
//        System.out.println(verifyEmail("261zxcz54aweqw5asdsa961@sohu.com", "gd_wenlong@163.com"));
//        System.out.println(verifyEmail("261zxcz54aweqdsa961@yahoo.com", "mx3@qq.com"));
        System.out.println(checkEmail("261zxcz54aweqdsa961@sohu.com"));
//        System.out.println(verifyEmail("261zxcz54aweqdsa961@gmail.com", "gd_wenlong@163.com"));
//        System.out.println(verifyEmail("261zxcz54aweqw5asdsa961@sina.com", "www.superbuy.com"));
    }
    public static boolean checkEmail(String email) {
        if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            return false;
        }
        String host = "";
        String hostName = email.split("@")[1];
        Record[] result = null;
        SMTPClient client = new SMTPClient();
        try {
            // 查找MX记录
            Lookup lookup = new Lookup(hostName, Type.MX);
            lookup.run();
            if (lookup.getResult() != Lookup.SUCCESSFUL) {
                return false;
            } else {
                result = lookup.getAnswers();
            }
            // 连接到邮箱服务器
            for (int i = 0; i < result.length; i++) {
                host = result[i].getAdditionalName().toString();
                client.connect(host);
                if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                    client.disconnect();
                    continue;
                } else {
                    break;
                }
            }
            //以下2项自己填写快速的，有效的邮箱
            client.login("163.com");
            client.setSender("sxgkwei@163.com");
            client.addRecipient(email);
            int replyCode = client.getReplyCode();
            System.out.println("reply" + replyCode);
            System.out.println(replyCode);
            if (250 == replyCode) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
            }
        }
        return false;
    }

    public static boolean verifyEmail(String toMail, String domain) {
        if (StringUtils.isBlank(toMail) || StringUtils.isBlank(domain)) {
            return false;
        }
        if (!StringUtils.contains(toMail, '@')) {
            return false;
        }
        String host = toMail.substring(toMail.indexOf('@') + 1);
        if (host.equals(domain)) {
            return false;
        }
        Socket socket = new Socket();
        try {
            // 查找mx记录
            Record[] mxRecords = new Lookup(host, Type.MX).run();
            if (ArrayUtils.isEmpty(mxRecords)) {
                return false;
            }
            // 邮件服务器地址
            String mxHost = ((MXRecord) mxRecords[0]).getTarget().toString();
            // 优先级排序
            if (mxRecords.length > 1) {
                List<Record> arrRecords = new ArrayList<>();
                Collections.addAll(arrRecords, mxRecords);
                Collections.sort(arrRecords, (o1, o2) -> new CompareToBuilder().append(((MXRecord) o1).getPriority(), ((MXRecord) o2).getPriority()).toComparison());
                mxHost = ((MXRecord) arrRecords.get(0)).getTarget().toString();
            }
            // 开始smtp
            socket.connect(new InetSocketAddress(mxHost, 25));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // 超时时间(毫秒)
            long timeout = 6000;
            // 睡眠时间片段(50毫秒)
            int sleepSect = 50;

            // 连接(服务器是否就绪)
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 220) {
                return false;
            }

            // 握手
            bufferedWriter.write("HELO " + domain + "\r\n");
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 身份
            bufferedWriter.write("MAIL FROM: <check@" + domain + ">\r\n");
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 验证
            bufferedWriter.write("RCPT TO: <" + toMail + ">\r\n");
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 断开
            bufferedWriter.write("QUIT\r\n");
            bufferedWriter.flush();
            return true;
        } catch (NumberFormatException e) {
        } catch (TextParseException e) {
        } catch (IOException e) {
        } catch (InterruptedException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    private static int getResponseCode(long timeout, int sleepSect, BufferedReader bufferedReader) throws InterruptedException, NumberFormatException, IOException {
        int code = 0;
        for (long i = sleepSect; i < timeout; i += sleepSect) {
            Thread.sleep(sleepSect);
            if (bufferedReader.ready()) {
                String outline = bufferedReader.readLine();
                System.out.println(outline);
                code = Integer.parseInt(outline.substring(0, 3));
                break;
            }
        }
        return code;
    }
}
