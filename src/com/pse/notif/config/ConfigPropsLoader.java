package com.pse.notif.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.pse.notif.model.NotifProperties;

/**
 * 
 * Configuration properties loader for user-set properties
 *
 */
public class ConfigPropsLoader {
  
  public final String CONFIG_PATH = "resource/config.properties";
  public final String CONFIG_KEY_SYMBOL = "SYMBOL";
  public final String CONFIG_KEY_CUT = "CUT";
  public final String CONFIG_KEY_BREAK = "BREAK";
  public final String CONFIG_DELIMITER = ",";
  
  private Properties notifProps;

  public ConfigPropsLoader() { 
  }

  /**
   * Load properties file
   * 
   * @throws Exception
   */
  public void init() throws Exception {
    try (InputStream input = new FileInputStream(CONFIG_PATH)) {
      this.notifProps = new Properties();
      this.notifProps.load(input);
    }
  }
  
  /**
   * Set properties on notification model
   * 
   * @return
   * @throws Exception
   */
  public NotifProperties getNotifProps() throws Exception {
    NotifProperties np = new NotifProperties();
    np.setSymbols(Arrays.asList(this.notifProps.
        getProperty(CONFIG_KEY_SYMBOL).split(CONFIG_DELIMITER)));
    np.setCutPoint(getPriceList(this.notifProps.
        getProperty(CONFIG_KEY_CUT).split(CONFIG_DELIMITER)));
    np.setBreakPoint(getPriceList(this.notifProps.
        getProperty(CONFIG_KEY_BREAK).split(CONFIG_DELIMITER)));
    return np;
  }
  
  /**
   * Get list of prices from props file for tracking
   *  
   * @param prices
   * @return
   */
  public List<BigDecimal> getPriceList(String[] prices) {
    List<BigDecimal> priceList = new ArrayList<BigDecimal>();
    for (String price: prices) {
      priceList.add(new BigDecimal(price));
    }
    return priceList;
  }
  
}
