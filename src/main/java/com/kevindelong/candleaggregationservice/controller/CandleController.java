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

    @PostMapping("/post-events")
    public void ingest(@RequestBody BidAskEvent event,
                       @RequestParam Timeframe timeframe) {

        aggregator.onEvent(event, timeframe);
    }

    @GetMapping("/get-candles")
    public Map<Long, Candle> getCandles(@RequestParam String symbol,
                                        @RequestParam Timeframe timeframe) {

        return store.getCandles(symbol, timeframe);
    }

    @GetMapping("/get-history")
    public HistoryResponse history(@RequestParam String symbol,
                                   @RequestParam String interval,
                                   @RequestParam long from,
                                   @RequestParam long to) {

        Timeframe timeframe = Timeframe.fromCode(interval);

        var candles = store.getCandles(symbol, timeframe);

        var sorted = candles.entrySet().stream()
                .filter(e -> e.getKey() >= from && e.getKey() <= to)
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();

        var times = sorted.stream().map(Candle::getTime).toList();
        var opens = sorted.stream().map(Candle::getOpen).toList();
        var highs = sorted.stream().map(Candle::getHigh).toList();
        var lows = sorted.stream().map(Candle::getLow).toList();
        var closes = sorted.stream().map(Candle::getClose).toList();
        var volumes = sorted.stream().map(Candle::getVolume).toList();

        return new HistoryResponse(
                "ok",
                times,
                opens,
                highs,
                lows,
                closes,
                volumes
        );
    }
}