package kim.charlie.kakao.plusfriend;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CharliePlusFriendApplicationTests {
	@Resource(name="redisTemplate")
	private HashOperations<String, String, Object> hashOperations;
	
	@Before
	public void setUp() {
		hashOperations.put("test:task", "Hello", "World");
	}
	
	@Test
	public void contextLoads() {
		Assert.assertEquals(hashOperations.entries("test:task").get("Hello"), "World");
	}
}