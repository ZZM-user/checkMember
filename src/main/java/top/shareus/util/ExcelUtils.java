package top.shareus.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * excel 工具类
 *
 * @author zhaojl
 * @date 2023/01/08
 */
public class ExcelUtils {

    //    public static final String EXPORT_EXCEL_PATH = "C:\\Users\\17602\\Desktop\\excel\\";
    public static final String EXPORT_EXCEL_PATH = "/opt/download/mirai/excel/";

    public static String exportMemberDataExcel(List<?> rows, String title) {
        String fileName = title + "(" + rows.size() + ")-" + DateUtil.date().toDateStr();
        String fullPath = EXPORT_EXCEL_PATH + fileName + "-" + System.currentTimeMillis() + ".xlsx";
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(fullPath);

        //自定义标题别名
        writer.addHeaderAlias("id", "QQ");
        writer.addHeaderAlias("nameCard", "群名片");
        writer.addHeaderAlias("nick", "昵称");
        writer.addHeaderAlias("specialTitle", "头衔");
        writer.addHeaderAlias("avatarUrl", "头像");
        writer.addHeaderAlias("muted", "是否禁言");
        writer.addHeaderAlias("muteTimeRemaining", "剩余禁言时长");
        writer.addHeaderAlias("groupId", "所在群号");
        writer.addHeaderAlias("groupName", "所在群名");
        writer.addHeaderAlias("lastSpeakTime", "最后发言时间");
        writer.addHeaderAlias("joinTime", "进群时间");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("remark2", "说明");

        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);

        // 设置自动列宽
        int aliasSize = writer.getHeaderAlias().size();
        Sheet sheet = writer.getSheet();
        for (int i = 0; i < aliasSize; i++) {
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 20 / 10);
        }

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(aliasSize - 1, fileName);
        writer.merge(rows.size() + 2, rows.size() + 2, 0, aliasSize - 1, "数量：" + rows.size() + "       生成时间：" + DateUtil.now(), true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();

        LogUtils.info("导出excel: " + fullPath);
        return fullPath;
    }
}
