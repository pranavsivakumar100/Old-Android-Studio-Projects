package com.example.investmenttracker;

public class FinancialSecurity {
    private String tickerSymbol;
    private String name;
    private String lastSale;
    private String netChange;
    private String percentChange;
    private String volume;
    private String marketCap;
    private String country;
    private String ipoYear;
    private String industry;
    private String sector;
    private int shares;

    public FinancialSecurity(String tickerSymbol, String name, String lastSale, String netChange, String percentChange,
                             String volume, String marketCap, String country, String ipoYear, String industry, String sector, int shares) {
        this.tickerSymbol = tickerSymbol;
        this.name = name;
        this.lastSale = lastSale;
        this.netChange = netChange;
        this.percentChange = percentChange;
        this.volume = volume;
        this.marketCap = marketCap;
        this.country = country;
        this.ipoYear = ipoYear;
        this.industry = industry;
        this.sector = sector;
        this.shares = shares;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public String getName() {
        return name;
    }

    public String getLastSale() {
        return lastSale;
    }

    public String getNetChange() {
        return netChange;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public String getVolume() {
        return volume;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public String getCountry() {
        return country;
    }

    public String getIpoYear() {
        return ipoYear;
    }

    public String getIndustry() {
        return industry;
    }

    public String getSector() {
        return sector;
    }

    public int getShares() {
        return shares;
    }

    public void setShareCount(int shares) {
        this.shares = shares;
    }
}
