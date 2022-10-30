package top.shareus.util;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * 图片 常用工具类
 *
 * @author 17602
 * @date 2022/10/29 20:28
 **/
public class ImageUtils {
    
    /**
     * 根据url创建Image
     *
     * @param group
     * @param imageUrl
     *
     * @return
     */
    public static Image create(Group group, String imageUrl) {
        byte[] avatar = HttpUtils.getBytesArray(imageUrl);
        if (avatar.length == 0) {
            return null;
        }
        return group.uploadImage(ExternalResource.create(avatar).toAutoCloseable());
    }
    
}
