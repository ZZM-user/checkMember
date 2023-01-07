package top.shareus.util;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import top.shareus.common.mapper.ArchivedFileMapper;
import top.shareus.common.mapper.QueryLogMapper;

import javax.sql.DataSource;

/**
 * mybatis-plus 工具类
 *
 * @author 17602
 * @date 2022/11/19
 */
public class MybatisPlusUtils {

    private static final SqlSessionManager sqlSessionManager = initSqlSessionFactory();

    /**
     * init sql会话工厂
     *
     * @return {@code SqlSessionFactory}
     */
    private static SqlSessionManager initSqlSessionFactory() {
        DataSource dataSource = dataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("Production", transactionFactory, dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration(environment);

        // 注册mapper
        configuration.addMapper(ArchivedFileMapper.class);
        configuration.addMapper(QueryLogMapper.class);

        configuration.setLogImpl(StdOutImpl.class);
        SqlSessionFactory sessionFactory = new MybatisSqlSessionFactoryBuilder().build(configuration);
        return SqlSessionManager.newInstance(sessionFactory);
    }

    /**
     * 数据源
     *
     * @return {@code DataSource}
     */
    private static DataSource dataSource() {
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
     * @return {@code T}
     */
    public static <T> T getMapper(Class<T> tClass) {
        return sqlSessionManager.getMapper(tClass);
    }
}
