package com.pse.notif.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.pse.notif.model.Stock;

/**
 * 
 * Connection class to PHISIX API
 * 
 * Manages connection objects, calls, and data fetches.
 *
 */
public class PhisixApi {
  
  //constants
  private final String BASE_URL = "http://phisix-api2.appspot.com/stocks/";
  private final String BASE_SUFFIX = ".json";
  private final String REQ_METHOD = "GET";
  private final int REQ_TIMEOUT_CONN = 5000;
  private final int REQ_TIMEOUT_READ = 5000;
  
  //private members
  private List<Stock> stocks;
  private Logger logger;
  
  //singleton instance
  private static PhisixApi apiObj; 

  public PhisixApi() {
  }
  
  public PhisixApi(List<Stock> stocks) {
    this.stocks = stocks;
  }
  
  public static PhisixApi getInstance(List<Stock> stocks) { 
    if (apiObj == null) { 
      apiObj = new PhisixApi(stocks);
    }
    apiObj.stocks = stocks;
    return apiObj; 
  }
  
  /**
   * Connect to Phisix and store the repsonse string (JSON)
   * into the Stock object
   * 
   * @return
   * @throws Exception
   */
  public List<Stock> fetchStockData() throws Exception {
    getLogger().info("Starting data fetch...");
    
    for (Stock stock: this.stocks) {
      stock.setStrJson(null); //clear stock JSON of old data
      String urlStr = BASE_URL + stock.getSymbol() + BASE_SUFFIX;
      URL url = new URL(urlStr);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      try {
        conn.setRequestMethod(REQ_METHOD);
        conn.setConnectTimeout(REQ_TIMEOUT_CONN);
        conn.setReadTimeout(REQ_TIMEOUT_READ);
        try (BufferedReader in = new BufferedReader(
              new InputStreamReader(conn.getInputStream()))) {
          String inputLine;
          StringBuilder content = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
              content.append(inputLine);
          }
          stock.setStrJson(content.toString());
        }
      } catch (IOException e) {
        getLogger().error("Cannot connect: " + urlStr);
      } finally {
        conn.disconnect();
      }      
    }
    
    getLogger().info("End data fetch.");
    return this.stocks;
  }
  
  public void setLogger(Logger logger) {
    this.logger = logger;
  }
  
  public Logger getLogger() {
    return logger;
  }
}
