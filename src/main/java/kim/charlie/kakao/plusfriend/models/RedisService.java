package kim.charlie.kakao.plusfriend.models;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisService {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Resource(name="redisTemplate")
	private HashOperations<String, String, Object> hashOperations;
	
	public RedisService() {
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
	}
}