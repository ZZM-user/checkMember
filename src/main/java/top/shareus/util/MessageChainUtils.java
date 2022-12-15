package top.shareus.util;

import net.mamoe.mirai.message.data.*;

/**
 * 消息链 常用工具类
 *
 * @author 17602
 * @date 2022/10/29 22:13
 **/
public class MessageChainUtils {

    /**
     * 提取消息
     *
     * @param messages 消息
     * @param builder  构建器
     */
    public static void extract(MessageChain messages, MessageChainBuilder builder) {
        for (Message message : messages) {
            if (message instanceof Image) {
                // 图片
                Image image = (Image) message;
                builder.add(image);
            } else if (message instanceof Face) {
                // 原生表情
                Face face = (Face) message;
                builder.add(face);
            } else if (message instanceof FlashImage) {
                // 闪照
                FlashImage flashImage = (FlashImage) message;
                Image image = flashImage.getImage();
                builder.add(new PlainText("【闪照】"));
                builder.add(image);
            } else if (message instanceof ForwardMessage) {
                // 合并转发消息
                ForwardMessage forwardMessage = (ForwardMessage) message;
                builder.add(forwardMessage);
            } else if (message instanceof Audio) {
                // 语音
                Audio audio = (Audio) message;
                builder.add(audio);
            } else if (message instanceof FileMessage) {
                // 文件
                FileMessage fileMessage = (FileMessage) message;
                builder.add(fileMessage);
            } else {
                // 其他消息
                builder.add(message);
            }
        }
    }

    /**
     * 获取普通文本信息
     *
     * @param messages 消息
     * @return {@link PlainText}
     */
    public static PlainText fetchPlainText(MessageChain chain) {
        return (PlainText) chain.stream().filter(PlainText.class::isInstance).findFirst().orElse(null);

    }

    /**
     * 获取闪照信息
     *
     * @param messages 消息
     * @return {@link FlashImage}
     */
    public static FlashImage fetchFlashImage(MessageChain chain) {
        return (FlashImage) chain.stream().filter(FlashImage.class::isInstance).findFirst().orElse(null);
    }

    /**
     * 获取文件信息
     *
     * @param messages 消息
     * @return {@code FileMessage}
     */
    public static FileMessage fetchFileMessage(MessageChain chain) {
        return (FileMessage) chain.stream().filter(FileMessage.class::isInstance).findFirst().orElse(null);

    }
}
