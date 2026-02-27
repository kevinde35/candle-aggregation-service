package com.kevindelong.candleaggregationservice.core;

public class Candle {

    private final long time;
    private final double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    public Candle(long time, double price) {
        this.time = time;
        this.open = price;
        this.high = price;
        this.low = price;
        this.close = price;
        this.volume = 1;
    }

    public void update(double price) {
        high = Math.max(high, price);
        low = Math.min(low, price);
        close = price;
        volume++;
    }

    public long getTime() { return time; }
    public double getOpen() { return open; }
    public double getHigh() { return high; }
    public double getLow() { return low; }
    public double getClose() { return close; }
    public long getVolume() { return volume; }
}