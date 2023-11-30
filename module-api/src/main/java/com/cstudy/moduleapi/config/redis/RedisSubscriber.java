package com.cstudy.moduleapi.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheManager cacheManager;
//    private final DiscordListener discordListener;

    public RedisSubscriber(final RedisTemplate<String, Object> redisTemplate, final CacheManager cacheManager, final RedisMessageListenerContainer redisMessageListener) {
        this.redisTemplate = redisTemplate;
        this.cacheManager = cacheManager;

        ChannelTopic rankingInvalidationChannel = new ChannelTopic("ranking-invalidation");
        redisMessageListener.addMessageListener(this, rankingInvalidationChannel);

        ChannelTopic sendDiscord = new ChannelTopic("sendDiscord");
        redisMessageListener.addMessageListener(this, sendDiscord);
    }

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());

        if ("ranking-invalidation".equals(channel)) {
            log.debug("Ranking with ID " + body + " has been updated!");
            redisTemplate.delete("ranking::1");
        }

//        if ("sendDiscord".equals(channel)) {
//            discordListener.sendMessage(null, body);
//        }

    }
}