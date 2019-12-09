package com.pse.notif.formatter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pse.notif.model.Stock;

/**
 * 
 * Handle formatting based on Phisix API data model
 * 
 * Sample data:
 * {"stock":
 *   [{"name":"Alliance Global",
 *     "price": {"currency":"PHP","amount":11.08},
 *     "percent_change":-0.36,
 *     "volume":4794200,"symbol":"AGI"}],
 *   "as_of":"2019-12-09T15:20:00+08:00"}
 *
 */
public class PhisixApiJsonFormatter {
  
  private List<Stock> stocks;
  private static PhisixApiJsonFormatter formatterObj; 
  
  private static final String PHISIX_KEY_STOCK = "stock";
  private static final String PHISIX_KEY_NAME = "name";
  private static final String PHISIX_KEY_PRICE = "price";
  private static final String PHISIX_KEY_AMOUNT = "amount";
  private static final String PHISIX_KEY_CURRENCY = "currency";
  private static final String PHISIX_KEY_PCT_CHANGE = "percent_change";
  private static final String PHISIX_KEY_VOLUME = "volume";

  public PhisixApiJsonFormatter() {
  }
  
  public static PhisixApiJsonFormatter getInstance() { 
    if (formatterObj == null) { 
      formatterObj = new PhisixApiJsonFormatter();
    }
    return formatterObj; 
  } 
  
  public void setStocks(List<Stock> stocks) {
    this.stocks = stocks;
  }
  
  public List<Stock> format() {
    for (Stock stock: this.stocks) {
      formatStock(stock);
    }
    return this.stocks;
  }

  /**
   * Set stock properties based on Phisix data
   * 
   * @param stock
   */
  private void formatStock(Stock stock) {
    //stock json retrieved from fetcher object
    String stockJson = stock.getStrJson();
    if (!StringUtils.isEmpty(stockJson)) {
      JSONObject psixObj = new JSONObject(stock.getStrJson()); 
      JSONArray psixStockArr = psixObj.getJSONArray(PHISIX_KEY_STOCK);
      for (Object psixStockObj: psixStockArr) {
        JSONObject psobj = ((JSONObject) psixStockObj);
        stock.setName(psobj.getString(PHISIX_KEY_NAME));
        
        JSONObject psobjPrice = psobj.getJSONObject(PHISIX_KEY_PRICE);
        stock.setPrice(psobjPrice.getBigDecimal(PHISIX_KEY_AMOUNT));
        stock.setCurrency(psobjPrice.getString(PHISIX_KEY_CURRENCY));
        
        stock.setPercentChange(psobj.getBigDecimal(PHISIX_KEY_PCT_CHANGE));
        stock.setVolume(psobj.getLong(PHISIX_KEY_VOLUME));
      }
    }
  }
}
