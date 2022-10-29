package top.shareus.util;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 图片 常用工具
 *
 * @Author： 17602
 * @Date： 2022/10/29 20:28
 **/
public class ImageUtils {
    
    public static Image create(Group group, String imageUrl) {
        URL url = null;
        URLConnection connection = null;
        InputStream in = null;
        try {
            url = new URL(imageUrl);
            connection = url.openConnection();
            in = connection.getInputStream();
            
            byte[] bytes = new byte[2048];
            StringBuilder builder = new StringBuilder();
            while (in.read(bytes) != 0) {
                builder.append(bytes);
            }
            Image image = group.uploadImage(ExternalResource.create(bytes));
            return image;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
}
