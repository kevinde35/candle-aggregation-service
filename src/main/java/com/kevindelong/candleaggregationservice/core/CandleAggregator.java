package com.kevindelong.candleaggregationservice.core;

import com.kevindelong.candleaggregationservice.store.InMemoryCandleStore;
import org.springframework.stereotype.Service;

@Service
public class CandleAggregator {

    private final InMemoryCandleStore store;

    public CandleAggregator(InMemoryCandleStore store) {
        this.store = store;
    }

    public void onEvent(BidAskEvent event, Timeframe timeframe) {

        long aligned = timeframe.align(event.timestamp());
        double price = event.midPrice();

        store.updateCandle(
                event.symbol(),
                timeframe,
                aligned,
                price
        );
    }
}