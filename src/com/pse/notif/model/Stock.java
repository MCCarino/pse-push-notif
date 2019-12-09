package com.pse.notif.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.pse.notif.util.AppUtils;

/**
 * 
 * Stock model class
 *
 */
public class Stock {

  private String name;
  private String symbol;
  private String currency;
  private BigDecimal price;
  private BigDecimal cutPrice;
  private BigDecimal breakPrice;
  private BigDecimal percentChange;
  private long volume;
  private Date asOf;
  
  private String strJson;
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @return the symbol
   */
  public String getSymbol() {
    return symbol;
  }
  /**
   * @return the currency
   */
  public String getCurrency() {
    return currency;
  }
  /**
   * @return the price
   */
  public BigDecimal getPrice() {
    return price;
  }
  /**
   * @return the price
   */
  public BigDecimal getCutPrice() {
    return cutPrice;
  }
  /**
   * @return the price
   */
  public BigDecimal getBreakPrice() {
    return breakPrice;
  }
  /**
   * @return the value
   */
  public BigDecimal getValue() {
    return getPrice().multiply(BigDecimal.valueOf(getVolume()));
  }
  /**
   * @return the percentChange
   */
  public BigDecimal getPercentChange() {
    return percentChange;
  }
  /**
   * @return the volume
   */
  public long getVolume() {
    return volume;
  }
  /**
   * @return the asOf
   */
  public Date getAsOf() {
    return asOf;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * @param symbol the symbol to set
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  /**
   * @param currency the currency to set
   */
  public void setCurrency(String currency) {
    this.currency = currency;
  }
  /**
   * @param price the price to set
   */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }
  /**
   * @param price the cutPrice to set
   */
  public void setCutPrice(BigDecimal cutPrice) {
    this.cutPrice = cutPrice;
  }
  /**
   * @param price the breakPrice to set
   */
  public void setBreakPrice(BigDecimal breakPrice) {
    this.breakPrice = breakPrice;
  }
  /**
   * @param percentChange the percentChange to set
   */
  public void setPercentChange(BigDecimal percentChange) {
    this.percentChange = percentChange;
  }
  /**
   * @param volume the volume to set
   */
  public void setVolume(long volume) {
    this.volume = volume;
  }
  /**
   * @param asOf the asOf to set
   */
  public void setAsOf(Date asOf) {
    this.asOf = asOf;
  }
  /**
   * @return the strJson
   */
  public String getStrJson() {
    return strJson;
  }
  /**
   * @param strJson the strJson to set
   */
  public void setStrJson(String strJson) {
    this.strJson = strJson;
  }
  /**
   * @param check if strJson is not empty
   */
  public boolean hasApiData() {
    return !StringUtils.isEmpty(getStrJson());
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getSymbol());
    sb.append(": ");
    sb.append(AppUtils.getFormattedDecimal(getPrice()));
    sb.append(" (");
    sb.append(AppUtils.getFormattedDecimal(getPercentChange()));
    sb.append("%) [");
    sb.append(AppUtils.getFormattedValue(getValue()));
    sb.append("]");
    return sb.toString();
  }

}
