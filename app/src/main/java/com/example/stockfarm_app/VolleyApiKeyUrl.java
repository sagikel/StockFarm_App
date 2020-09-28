package com.example.stockfarm_app;

public class VolleyApiKeyUrl {
    private String urlPrefix;
    private String urlSuffix;
    private String urlSuffixForMany;
    private String urlSuffixForQ;
    private String apiKey;

    public VolleyApiKeyUrl() {
        urlPrefix = "https://financialmodelingprep.com/api/v3/";
        urlSuffix = "?apikey=";
        urlSuffixForMany = "?limit=100&apikey=";
        urlSuffixForQ = "?period=quarter&limit=100&apikey=";
        apiKey = "demo"; ///"136ed7203a27884b69d3b28e9d0bfb85";
    }

    public String getCorrectUrlForOne(String symbol, String type) {
        return urlPrefix + type + "/" + symbol + urlSuffix + apiKey;
    }
    public String getCorrectUrlForMany(String symbol, String type) {
        return urlPrefix + type + "/" + symbol + urlSuffixForMany + apiKey;
    }
    public String getCorrectUrlQ(String symbol, String type) {
        return urlPrefix + type + "/" + symbol + urlSuffixForQ + apiKey;
    }
    public String getCorrectUrlC(String symbol, String type) {
        return urlPrefix + "earning_call_transcript/" + symbol
                + "?quarter=" + type.substring(0, 1) + "&year=" +
                type.substring(1) + "&apikey=" + apiKey;
    }
    public String getCorrectUrlN(String symbol, String type) {
        return urlPrefix + type + "?tickers=" + symbol
                + "&" + urlSuffixForMany.substring(1) + apiKey;
    }
}
