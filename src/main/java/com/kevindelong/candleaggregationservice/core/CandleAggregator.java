package com.kevindelong.candleaggregationservice.core;

import com.kevindelong.candleaggregationservice.store.InMemoryCandleStore;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CandleAggregator {

    private static final Logger log =
            LoggerFactory.getLogger(CandleAggregator.class);

    private final InMemoryCandleStore store;

    public CandleAggregator(InMemoryCandleStore store) {
        this.store = store;
    }

    public void onEvent(BidAskEvent event, Timeframe timeframe) {

        long aligned = timeframe.align(event.timestamp());
        double price = event.midPrice();

        log.debug("Event: symbol={}, price={}, timestamp={}",
                event.symbol(), price, event.timestamp());

        store.updateCandle(
                event.symbol(),
                timeframe,
                aligned,
                price
        );

        log.debug("Updated candle: symbol={}, interval={}, bucket={}",
                event.symbol(), timeframe, aligned);
    }
}