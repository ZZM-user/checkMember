package top.shareus.util;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.utils.RemoteFile;

import java.io.File;

/**
 * 群上传文件 工具类
 *
 * @author zhaojl
 * @date 2023/01/08
 */
public class GroupUploadFileUtils {
    public static void uploadFile(Group group, String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        RemoteFile filesRoot = group.getFilesRoot().resolve(fileName);
        MessageReceipt<Contact> uploadAndSend = filesRoot.uploadAndSend(new File(filePath));

        if (uploadAndSend.isToGroup()) {
            LogUtils.info(filePath + " 上传至 " + group.getName() + " 成功！");
            return;
        }

        LogUtils.error(filePath + " 上传至 " + group.getName() + " 失败！");
    }
}
