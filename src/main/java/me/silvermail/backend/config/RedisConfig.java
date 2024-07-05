package me.silvermail.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    protected String redisHost;

    @Value("${spring.data.redis.port}")
    protected int redisPort;

    @Value("${spring.data.redis.password}")
    protected String redisPassword;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.getStandaloneConfiguration().setHostName(redisHost);
        connectionFactory.getStandaloneConfiguration().setPort(redisPort);
        connectionFactory.getStandaloneConfiguration().setPassword(redisPassword);
        connectionFactory.afterPropertiesSet();

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
