package kim.charlie.kakao.plusfriend.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisConfig {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
}