package kim.charlie.kakao.plusfriend;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import kim.charlie.kakao.plusfriend.models.DaoFactory;
import kim.charlie.kakao.plusfriend.models.RecentMessageDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecentMessageDaoTest {
	RecentMessageDao recentMessageDao = null;
	
	@Before
	public void setUp() {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		recentMessageDao = context.getBean("recentMessageDao", RecentMessageDao.class);
	}
	
	@Test
	public void deleteAllTest() throws SQLException {
		
		
	}
}