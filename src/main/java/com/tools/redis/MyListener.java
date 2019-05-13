package com.tools.redis;

import io.lettuce.core.pubsub.RedisPubSubListener;

public class MyListener implements RedisPubSubListener {
    @Override
    public void message(Object channel, Object message) {

    }

    @Override
    public void message(Object pattern, Object channel, Object message) {

    }

    @Override
    public void subscribed(Object channel, long count) {

    }

    @Override
    public void psubscribed(Object pattern, long count) {

    }

    @Override
    public void unsubscribed(Object channel, long count) {

    }

    @Override
    public void punsubscribed(Object pattern, long count) {

    }
}
