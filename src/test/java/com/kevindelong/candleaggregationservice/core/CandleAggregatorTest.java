package com.kevindelong.candleaggregationservice.core;

import com.kevindelong.candleaggregationservice.store.InMemoryCandleStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandleAggregatorTest {

    @Test
    void shouldAggregateOhlcCorrectly() {

        InMemoryCandleStore store = new InMemoryCandleStore();
        CandleAggregator aggregator = new CandleAggregator(store);

        aggregator.onEvent(new BidAskEvent("AAPL", 100, 100, 60), Timeframe.ONE_MINUTE);
        aggregator.onEvent(new BidAskEvent("AAPL", 110, 110, 70), Timeframe.ONE_MINUTE);
        aggregator.onEvent(new BidAskEvent("AAPL", 90, 90, 80), Timeframe.ONE_MINUTE);

        var candle = store.getCandles("AAPL", Timeframe.ONE_MINUTE)
                .values()
                .iterator()
                .next();

        assertEquals(100.0, candle.getOpen());
        assertEquals(110.0, candle.getHigh());
        assertEquals(90.0, candle.getLow());
        assertEquals(90.0, candle.getClose());
        assertEquals(3, candle.getVolume());
    }

    @Test
    void shouldAlignTimestampToInterval() {

        InMemoryCandleStore store = new InMemoryCandleStore();
        CandleAggregator aggregator = new CandleAggregator(store);

        aggregator.onEvent(new BidAskEvent("AAPL", 100, 100, 61), Timeframe.ONE_MINUTE);

        var candles = store.getCandles("AAPL", Timeframe.ONE_MINUTE);

        assertTrue(candles.containsKey(60L));
    }

    @Test
    void shouldCreateSeparateCandlesForDifferentSymbols() {

        InMemoryCandleStore store = new InMemoryCandleStore();
        CandleAggregator aggregator = new CandleAggregator(store);

        aggregator.onEvent(new BidAskEvent("AAPL", 100, 100, 60), Timeframe.ONE_MINUTE);
        aggregator.onEvent(new BidAskEvent("GOOG", 200, 200, 60), Timeframe.ONE_MINUTE);

        assertEquals(1, store.getCandles("AAPL", Timeframe.ONE_MINUTE).size());
        assertEquals(1, store.getCandles("GOOG", Timeframe.ONE_MINUTE).size());
    }

    @Test
    void shouldCreateSeparateCandlesForDifferentIntervals() {

        InMemoryCandleStore store = new InMemoryCandleStore();
        CandleAggregator aggregator = new CandleAggregator(store);

        aggregator.onEvent(new BidAskEvent("AAPL", 100, 100, 60), Timeframe.ONE_MINUTE);
        aggregator.onEvent(new BidAskEvent("AAPL", 100, 100, 60), Timeframe.FIVE_SECONDS);

        assertEquals(1, store.getCandles("AAPL", Timeframe.ONE_MINUTE).size());
        assertEquals(1, store.getCandles("AAPL", Timeframe.FIVE_SECONDS).size());
    }
}