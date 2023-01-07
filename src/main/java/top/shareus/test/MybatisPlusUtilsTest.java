package top.shareus.test;

import org.junit.jupiter.api.Test;
import top.shareus.common.domain.ArchivedFile;
import top.shareus.common.mapper.ArchivedFileMapper;
import top.shareus.util.MybatisPlusUtils;

import java.util.List;

class MybatisPlusUtilsTest {

    @Test
    void getMapper() {
        ArchivedFileMapper mapper = MybatisPlusUtils.getMapper(ArchivedFileMapper.class);
        List<ArchivedFile> archivedFiles = mapper.selectBookByName("百");
        System.out.println("archivedFiles = " + archivedFiles);
    }

    @Test
    void Query() throws InterruptedException {
        ArchivedFileMapper mapper = MybatisPlusUtils.getMapper(ArchivedFileMapper.class);
        Thread.sleep(3000);
        List<ArchivedFile> archivedFiles = mapper.selectBookByName("完成");

        System.out.println("bookInfoByName = " + archivedFiles);

        Thread.sleep(50000);
        System.out.println("mapper = " + mapper.selectBookByName("测试"));
    }
}