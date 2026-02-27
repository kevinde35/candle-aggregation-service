package com.kevindelong.candleaggregationservice.controller;

import com.kevindelong.candleaggregationservice.core.*;
import com.kevindelong.candleaggregationservice.store.InMemoryCandleStore;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/candles")
public class CandleController {
    private final CandleAggregator aggregator;
    private final InMemoryCandleStore store;

    public CandleController(CandleAggregator aggregator,
                                 InMemoryCandleStore store) {
        this.aggregator = aggregator;
        this.store = store;
    }

    @PostMapping("/events")
    public void ingest(@RequestBody BidAskEvent event,
                       @RequestParam Timeframe timeframe) {

        aggregator.onEvent(event, timeframe);
    }

    @GetMapping("/get-candles")
    public Map<Long, Candle> getCandles(@RequestParam String symbol,
                                        @RequestParam Timeframe timeframe) {

        return store.getCandles(symbol, timeframe);
    }
}