package com.pse.notif.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.pse.notif.model.Stock;

/**
 * 
 * Application utility class
 *
 */
public final class AppUtils {

  /**
   * Check if stock price is below cut loss point
   * 
   * @param stock
   * @return
   */
  public static boolean isBelowCut(Stock stock) {
    return stock.getCutPrice().
        compareTo(stock.getPrice()) > 0;
  }
  
  /**
   * Check if stock price is above breakout point
   * 
   * @param stock
   * @return
   */
  public static boolean isAboveBreak(Stock stock) {
    return stock.getBreakPrice().
        compareTo(stock.getPrice()) < 0;
  }
  
  /**
   * Get BigDecimal value formatted for display
   * 
   * @param stock
   * @return
   */
  public static String getFormattedDecimal(BigDecimal bd) {
    return getFormattedDecimal(bd, 2);
  }
  
  /**
   * Get BigDecimal value formatted for display
   * 
   * @param bd
   * @param scale
   * @return
   */
  public static String getFormattedDecimal(BigDecimal bd, int scale) {
    return String.format("%,.2f", 
        bd.setScale(scale, RoundingMode.HALF_EVEN)); 
  }
  
  /**
   * Get BigDecimal value formatted for display
   * 
   * Formats to the nearest hundredth place and appends the relevant unit, e.g.
   * 145592042042 -> 145.60B
   * 49456943 -> 49M
   * 599825 -> 599.83K
   * 66935 -> 66.94K
   * 284 -> 284
   * 
   * @param bd
   * @return
   */
  public static String getFormattedValue(BigDecimal bd) {
    double num = bd.doubleValue();
    
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    decimalFormat.setGroupingUsed(true);
    decimalFormat.setGroupingSize(3);
    
    long i = (long) Math.pow(10, (int)Math.max(0, Math.log10(num) - 2));
    num = num / i * i;

    if (num >= 1000000000)
       return decimalFormat.format(num / 1000000000D) + "B";
    if (num >= 1000000)
       return decimalFormat.format(num / 1000000D) + "M";
    if (num >= 1000)
       return decimalFormat.format(num / 1000D) + "K";
    return decimalFormat.format(num);
  }
}
