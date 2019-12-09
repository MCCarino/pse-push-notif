package com.pse.notif.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * Notification properties class
 *
 */
public class NotifProperties {

  private List<String> symbols;
  private long frequency;
  private String marketOpen;
  private String marketClose;
  private List<BigDecimal> cutPoint;
  private List<BigDecimal> breakPoint;
  
  public NotifProperties() {
  }

  /**
   * @return the symbols
   */
  public List<String> getSymbols() {
    return symbols;
  }

  /**
   * @return the frequency
   */
  public long getFrequency() {
    return frequency;
  }

  /**
   * @return the marketOpen
   */
  public String getMarketOpen() {
    return marketOpen;
  }

  /**
   * @return the marketClose
   */
  public String getMarketClose() {
    return marketClose;
  }

  /**
   * @return the cutPoint
   */
  public List<BigDecimal> getCutPoint() {
    return cutPoint;
  }

  /**
   * @return the breakPoint
   */
  public List<BigDecimal> getBreakPoint() {
    return breakPoint;
  }

  /**
   * @param symbols the symbols to set
   */
  public void setSymbols(List<String> symbols) {
    this.symbols = symbols;
  }

  /**
   * @param frequency the frequency to set
   */
  public void setFrequency(long frequency) {
    this.frequency = frequency;
  }

  /**
   * @param marketOpen the marketOpen to set
   */
  public void setMarketOpen(String marketOpen) {
    this.marketOpen = marketOpen;
  }

  /**
   * @param marketClose the marketClose to set
   */
  public void setMarketClose(String marketClose) {
    this.marketClose = marketClose;
  }

  /**
   * @param cutPoint the cutPoint to set
   */
  public void setCutPoint(List<BigDecimal> cutPoint) {
    this.cutPoint = cutPoint;
  }

  /**
   * @param breakPoint the breakPoint to set
   */
  public void setBreakPoint(List<BigDecimal> breakPoint) {
    this.breakPoint = breakPoint;
  }

}
