# Candle Aggregation Service

## Overview

This service ingests bid/ask market events and aggregates them into time-aligned OHLC candles per symbol and interval.
It exposes a REST API to retrieve historical candle data in a chart-compatible format.

The implementation focuses on correctness, thread-safety, and clean architecture.

---

## Features

* Real-time event ingestion via REST
* OHLC aggregation per symbol and interval (1s, 5s, 1m, 15m, 1h)
* In-memory thread-safe storage
* `/history` endpoint returning TradingView-style response format
* Unit tests for aggregation logic
* Logging

---

## API

### Ingest Event

```http
POST /events
```

```json
{
  "symbol": "BTC-USD",
  "bid": 29500.5,
  "ask": 29501.0,
  "timestamp": 1620000000
}
```

---

### Get Historical Candles

```http
GET /history?symbol=BTC-USD&interval=1m&from=1620000000&to=1620000600
```

Response:

```json
{
  "s": "ok",
  "t": [...],
  "o": [...],
  "h": [...],
  "l": [...],
  "c": [...],
  "v": [...]
}
```

Timestamps are UNIX seconds.

---

## Non-Functional Notes

* Thread-safe aggregation using `ConcurrentHashMap.compute`
* Constant-time updates, no global locks
* Symbol and interval isolation
* In-memory storage (trade-off for simplicity and low latency)
* Logging

---

## Assumptions

* Mid-price (average of bid and ask) is used.
* Volume represents tick count.
* No persistence layer (in-memory only).

---

## Run

```bash
./mvnw spring-boot:run
```

## Test

```bash
./mvnw test
```

---

