package com.kevindelong.candleaggregationservice.store;

import com.kevindelong.candleaggregationservice.core.Candle;
import com.kevindelong.candleaggregationservice.core.Timeframe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryCandleStore {

    private final ConcurrentMap<String,
            ConcurrentMap<Timeframe,
                    ConcurrentMap<Long, Candle>>> store = new ConcurrentHashMap<>();

    public void updateCandle(String symbol,
                             Timeframe timeframe,
                             long alignedTimestamp,
                             double price) {

        store
                .computeIfAbsent(symbol, s -> new ConcurrentHashMap<>())
                .computeIfAbsent(timeframe, t -> new ConcurrentHashMap<>())
                .compute(alignedTimestamp, (ts, existing) -> {
                    if (existing == null) {
                        return new Candle(ts, price);
                    } else {
                        existing.update(price);
                        return existing;
                    }
                });
    }

    public Map<Long, Candle> getCandles(String symbol, Timeframe timeframe) {
        return store
                .getOrDefault(symbol, new ConcurrentHashMap<>())
                .getOrDefault(timeframe, new ConcurrentHashMap<>());
    }
}