package com.pse.notif.main;

import com.pse.notif.app.PsePushNotifier;

/**
 * 
 * Runner class for applications.
 * 
 * Use for configuration setup and application initialization.
 *
 */
public class Runner {
  
  private static final String LOGGER_PROPERTY = "log4j.configurationFile";
  private static final String LOGGER_CONFIG_PATH = "log4j2.xml";

  public static void main(String[] args) throws Exception {
    setupLogger();
    
    //PSE Push Notifier application
    PsePushNotifier psn = new PsePushNotifier();
    psn.setupNotif();
    psn.scheduleNotif();
  }
  
  /**
   * Setup application logger configuration.
   * 
   * @throws Exception
   */
  public static void setupLogger() throws Exception {
    System.setProperty(LOGGER_PROPERTY, LOGGER_CONFIG_PATH);
  }
}
