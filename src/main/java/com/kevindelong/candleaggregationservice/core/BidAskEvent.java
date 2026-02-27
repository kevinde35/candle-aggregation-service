package com.kevindelong.candleaggregationservice.core;

public record BidAskEvent(
        String symbol,
        double bid,
        double ask,
        long timestamp
) {
    public double midPrice() {
        return (bid + ask) / 2.0;
    }
}