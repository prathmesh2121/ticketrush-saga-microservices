package com.ticketrush.inventory_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class RedisScriptConfig
{
    @Bean
    public RedisScript<Long> acquireScript()
    {
        return script("scripts/acquire.lua");
    }
    @Bean
    public RedisScript<Long> releaseScript() {
        return script("scripts/release.lua");
    }

    public RedisScript<Long> script(String path)
    {
        DefaultRedisScript<Long> s = new DefaultRedisScript<>();
        s.setLocation(new ClassPathResource(path));
        s.setResultType(Long.class);
        return s;
    }
}
