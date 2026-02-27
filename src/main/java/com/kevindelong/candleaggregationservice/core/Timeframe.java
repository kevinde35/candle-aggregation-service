package com.kevindelong.candleaggregationservice.core;

public enum Timeframe {
    ONE_SECOND(1),
    FIVE_SECONDS(5),
    ONE_MINUTE(60),
    FIFTEEN_MINUTES(900),
    ONE_HOUR(3600);

    private final long seconds;

    Timeframe(long seconds) {
        this.seconds = seconds;
    }

    public long getSeconds() {
        return seconds;
    }

    public long align(long timestamp) {
        return (timestamp / seconds) * seconds;
    }
}