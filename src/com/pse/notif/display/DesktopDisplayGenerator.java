package com.pse.notif.display;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pse.notif.model.Stock;

/**
 * 
 * Windows desktop notification invocation using SystemTray
 *
 */
public class DesktopDisplayGenerator {
  
  public final String ICON_PATH = "resource/icon2.png";
  public final String DISPLAY_HEAD = "Stock List";
  
  private List<Stock> stocks = new ArrayList<Stock>();
  private StringBuilder message = new StringBuilder();
  private String displayHead;
  
  private static DesktopDisplayGenerator displayObj; 

  public DesktopDisplayGenerator() {
  }
  
  public static DesktopDisplayGenerator getInstance() { 
    if (displayObj == null) { 
      displayObj = new DesktopDisplayGenerator();
    }
    return displayObj; 
  }
  
  /**
   * Display notification based on this generator's
   * header and message details
   * 
   * @throws Exception
   */
  public void display() throws Exception {
    SystemTray tray = SystemTray.getSystemTray();
    Image image = Toolkit.getDefaultToolkit().createImage(ICON_PATH);
    TrayIcon trayIcon = new TrayIcon(image, getDisplayHead());
    trayIcon.setImageAutoSize(true);
    trayIcon.setToolTip(getDisplayHead());
    tray.add(trayIcon);
    trayIcon.displayMessage(getDisplayHead(), getMessage(), MessageType.INFO);
  }
  
  /**
   * Get this generator's display heading (in bold upon display)
   * @return
   */
  private String getDisplayHead() {
    return StringUtils.isEmpty(this.displayHead) ? 
         DISPLAY_HEAD : this.displayHead;
  }
  
  public void setDisplayHead(String displayHead) {
    this.displayHead = displayHead;
  }

  /**
   * Set the generator's stock models
   * 
   * @param stocks
   */
  public void setStocks(List<Stock> stocks) {
    this.stocks = stocks;
  }

  /**
   * Format this default generator's message
   * 
   * @return
   */
  private String getMessage() {
    if (!StringUtils.isEmpty(this.message)) {
      return this.message.toString();
    }
    StringBuilder sb = new StringBuilder();
    for (Stock stock: this.stocks) {
      if (stock.hasApiData()) {
        sb.append(stock.toString());
        sb.append("\n");
      }
    }
    return sb.toString();
  }
  
  /**
   * Append string to the message property
   * 
   * @param message
   */
  public void addMessage(String message) {
    this.message.append(message);
    this.message.append("\n");
  }
  
  /**
   * Reset message property
   */
  public void clearMessage() {
    this.message.setLength(0);
  }
}
