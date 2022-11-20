package top.shareus.util;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import top.shareus.common.domain.ArchivedFile;
import top.shareus.common.mapper.ArchivedFileMapper;

import javax.sql.DataSource;

/**
 * @author 17602
 * @date 2022/11/19
 */
public class MybatisPlusUtils {
    
    public static final SqlSessionFactory sqlSessionFactory = initSqlSessionFactory();
    
    public static void main(String[] args) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            ArchivedFileMapper mapper = session.getMapper(ArchivedFileMapper.class);
            ArchivedFile archivedFile = new ArchivedFile();
            mapper.insert(archivedFile);
        }
    }
    
    /**
     * init sql会话工厂
     *
     * @return {@code SqlSessionFactory}
     */
    public static SqlSessionFactory initSqlSessionFactory() {
        DataSource dataSource = dataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("Production", transactionFactory, dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration(environment);
        configuration.addMapper(ArchivedFileMapper.class);
        configuration.setLogImpl(StdOutImpl.class);
        return new MybatisSqlSessionFactoryBuilder().build(configuration);
    }
    
    /**
     * 数据源
     *
     * @return {@code DataSource}
     */
    public static DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://124.220.67.51:3306/mirai");
        dataSource.setUsername("root");
        dataSource.setPassword("ZJL20010516");
        return dataSource;
    }
    
    /**
     * 得到映射器
     *
     * @param tClass t类
     *
     * @return {@code T}
     */
    public static <T extends BaseMapper> T getMapper(Class<T> tClass) {
        try (SqlSession session = MybatisPlusUtils.sqlSessionFactory.openSession(true)) {
            return session.getMapper(tClass);
        } catch (Exception e) {
            LogUtils.error(e);
            throw new RuntimeException("Mybatis-Plus getMapper()获取失败");
        }
    }
}
