package com.kevindelong.candleaggregationservice.core;

public enum Timeframe {

    ONE_SECOND(1, "1s"),
    FIVE_SECONDS(5, "5s"),
    ONE_MINUTE(60, "1m"),
    FIFTEEN_MINUTES(900, "15m"),
    ONE_HOUR(3600, "1h");

    private final long seconds;
    private final String code;

    Timeframe(long seconds, String code) {
        this.seconds = seconds;
        this.code = code;
    }

    public long getSeconds() {
        return seconds;
    }

    public long align(long timestamp) {
        return (timestamp / seconds) * seconds;
    }

    public static Timeframe fromCode(String code) {
        for (Timeframe tf : values()) {
            if (tf.code.equalsIgnoreCase(code)) {
                return tf;
            }
        }
        throw new IllegalArgumentException("Invalid interval: " + code);
    }
}