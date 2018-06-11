package kim.charlie.kakao.plusfriend.models;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory {
	@Bean
	public RecentMessageDao recentMessageDao() {
		RecentMessageDao recentMessageDao = new RecentMessageDao();
		recentMessageDao.setDataSource(dataSource());
		return recentMessageDao;
	}
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://charlie.kim/plusfriend?autoReconnect=true&useUnicode=true&characterEncoding=utf8");
        dataSource.setUsername("plusfriend");
        dataSource.setPassword("charlie");
		
		return dataSource;
	}
}