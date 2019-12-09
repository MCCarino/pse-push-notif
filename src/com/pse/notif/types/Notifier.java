package com.pse.notif.types;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pse.notif.api.PhisixApi;
import com.pse.notif.config.ConfigPropsLoader;
import com.pse.notif.display.DesktopDisplayGenerator;
import com.pse.notif.formatter.PhisixApiJsonFormatter;
import com.pse.notif.model.NotifProperties;
import com.pse.notif.model.Stock;


/**
 * Abstract class for notification
 * 
 * Target to subclass notifiers with specific logic for filtering
 * or different schedule for running.
 *
 */
public abstract class Notifier {
  
  protected NotifProperties props;
  protected List<Stock> stocks;
  protected List<Stock> notifStocks;
  protected DesktopDisplayGenerator ddg;
  
  private boolean runOnce = false;
  private boolean setupDone = false;
  
  private static final Logger logger = 
      LogManager.getLogger(Notifier.class);

  public Notifier() {
    this.stocks = new ArrayList<Stock>();
    this.notifStocks = new ArrayList<Stock>();
  }
  
  //abstract methods for implementation on sub classes
  public abstract void doFilter() throws Exception;
  public abstract void setDisplayGenerator() throws Exception;
  
  /**
   * Setup notifier object i.e. load configuration, initialize objects
   * 
   * @throws Exception
   */
  public void doSetup() throws Exception {
    if (!this.setupDone) {
      loadConfig();
      setStocks();
      setDisplayGenerator();
      this.setupDone = true;
    }
  }
  
  /**
   * Initialize Stock objects based on user-configured properties.
   * Filtering and monitoring will be based on criteria set here.
   *
   */
  private void setStocks() {
    List<String> symbols = this.props.getSymbols();
    List<BigDecimal> cutPrice = this.props.getCutPoint();
    List<BigDecimal> breakPrice = this.props.getBreakPoint();
    
    for (int i = 0; i < symbols.size(); i++) {
      Stock stock = new Stock();
      stock.setSymbol(symbols.get(i));
      stock.setCutPrice(cutPrice.get(i));
      stock.setBreakPrice(breakPrice.get(i));
      this.stocks.add(stock);
    }
  }

  public void setProps(NotifProperties props) {
    this.props = props;
  }
  
  /**
   * Load user configuration based on properties file
   * 
   * @throws Exception
   */
  public void loadConfig() throws Exception {
    if (props == null) {
      ConfigPropsLoader cpl = new ConfigPropsLoader();
      cpl.init();
      this.props = cpl.getNotifProps();
    }
  }
  
  /**
   * Main notification process
   * 
   * @throws Exception
   */
  public void doNotify() throws Exception {
    doSetup();
    doFetch();
    doFormat();
    doFilter();
    doDisplay();
  }
  
  /**
   * Perform data fetch. 
   * All API calls should be within this method.
   * 
   * @throws Exception
   */
  private void doFetch() throws Exception {
    this.notifStocks.clear();
    PhisixApi psix = PhisixApi.getInstance(this.stocks);
    psix.setLogger(logger);
    this.stocks = psix.fetchStockData();
    cleanFailedFetches();
  }
  
  /**
   * Add objects filled by fetch. Ignore everything else. 
   */
  private void cleanFailedFetches() {
    for (Stock stock: this.stocks) {
      if (stock.hasApiData()) {
        this.notifStocks.add(stock);
      }
    }
  }

  /**
   * Format data for display.
   * Only considers data with fetch results.
   */
  private void doFormat() {
    getLogger().info("Starting formatting...");
    if (!this.notifStocks.isEmpty()) {
      PhisixApiJsonFormatter psixFormat = PhisixApiJsonFormatter.getInstance();
      psixFormat.setStocks(this.notifStocks);
      this.notifStocks = psixFormat.format();
    }
    getLogger().info("Ending formatting.");
  }

  /**
   * Call display object for end-user notification.
   *  
   * @throws Exception
   */
  private void doDisplay() throws Exception {
    getLogger().info("Starting display...");
    if (!this.notifStocks.isEmpty()) {
      this.ddg.setStocks(this.notifStocks);
      this.ddg.display();
    }
    getLogger().info("Ending display.");
  }

  /**
   * A notifier not set to be repeated can set this 
   * property to true.
   * 
   * @return the runOnce
   */
  public boolean isRunOnce() {
    return runOnce;
  }

  /**
   * @param runOnce the runOnce to set
   */
  public void setRunOnce(boolean runOnce) {
    this.runOnce = runOnce;
  }
  
  /**
   * Returns this class' logger object
   * 
   * @return
   */
  public Logger getLogger() {
    return logger;
  }

}
