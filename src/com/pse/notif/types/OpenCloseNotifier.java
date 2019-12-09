package com.pse.notif.types;

import com.pse.notif.display.DesktopDisplayGenerator;

/**
 * 
 * Notifier implementation for listing watchlist prices 
 * on start of run.
 *
 */
public class OpenCloseNotifier extends Notifier {
  
  private final String OPEN_CLOSE_HEADER = "Stock List";
  
  public OpenCloseNotifier() {
    getLogger().info("Setting up Open-close Notifier...");
    setRunOnce(true);
  }
    
  /*
   * (non-Javadoc)
   * @see com.pse.notif.types.Notifier#doFilter()
   */
  @Override
  public void doFilter() throws Exception {
  }

  /*
   * (non-Javadoc)
   * @see com.pse.notif.types.Notifier#setDisplayGenerator()
   */
  @Override
  public void setDisplayGenerator() throws Exception {
    this.ddg = DesktopDisplayGenerator.getInstance();
    this.ddg.setDisplayHead(OPEN_CLOSE_HEADER);
    this.ddg.clearMessage();
  }
}
