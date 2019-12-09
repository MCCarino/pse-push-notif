package com.pse.notif.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pse.notif.config.ConfigPropsLoader;
import com.pse.notif.model.NotifProperties;
import com.pse.notif.schedule.PsePushNotifScheduler;
import com.pse.notif.types.CutBreakNotifier;
import com.pse.notif.types.Notifier;
import com.pse.notif.types.OpenCloseNotifier;

/**
 * 
 * PSE Push Notifier Application
 * 
 * This application automatically creates alerts 
 * e.g. Windows desktop notifications based on matches against 
 * PSE stock attributes and user-specified criteria 
 * e.g. cut loss price, breakout price.
 * 
 * The application is timed to run at intervals and uses
 * publicly-available APIs for data gathering.
 * 
 * Application roadmap:
 * <li>Create desktop notifications of uptrend and downtrend stocks - done</li>
 * <li>Use Phisix API for data gathering - done</li>
 * <li>Time intervals for filtering stock data - done</li>
 * <li>Do realtime checking and notification</li>
 * <li>Use more elaborate API with more statistics e.g. EMA, RSI</li>
 * <li>Expand notification options e.g. email, SMS (twilio?)</li> 
 *
 */
public class PsePushNotifier {
  
  private NotifProperties props;
  private List<Notifier> notifiers;
  
  private static final Logger logger = 
      LogManager.getLogger(PsePushNotifier.class);
  
  public PsePushNotifier() {
    getLogger().info("Starting PSE Push Notification Service...");
    notifiers = new ArrayList<Notifier>();
  }
  
  /**
   * Load user configuration based on properties file
   * 
   * @throws Exception
   */
  public void loadConfig() throws Exception {
    getLogger().info("Loading configuration...");
    ConfigPropsLoader cpl = new ConfigPropsLoader();
    cpl.init();
    this.props = cpl.getNotifProps();
    getLogger().info("Finished loading configuration.");
  }
  
  public void setupNotif() throws Exception {
    loadConfig();
    addNotifiers();
  }
  
  /**
   * Add notifier classes
   */
  private void addNotifiers() {
    getLogger().info("Adding notification classes...");
    OpenCloseNotifier ocn = new OpenCloseNotifier();
    ocn.setProps(this.props);
    notifiers.add(ocn);
    
    CutBreakNotifier cbn = new CutBreakNotifier();
    cbn.setProps(this.props);
    notifiers.add(cbn);
    getLogger().info("Notifiers activated.");
  }

  /**
   * Run notification scheduler object
   * 
   * @throws Exception
   */
  public void scheduleNotif() throws Exception {
    getLogger().info("Scheduling notifications...");
    PsePushNotifScheduler ppns = new PsePushNotifScheduler();
    ppns.setNotifiers(this.notifiers);
    ppns.run();
  }
  
  public Logger getLogger() {
    return logger;
  }
}
