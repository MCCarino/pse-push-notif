package com.pse.notif.types;

import com.pse.notif.display.DesktopDisplayGenerator;
import com.pse.notif.model.Stock;
import com.pse.notif.util.AppUtils;

/**
 * 
 * Notifier implementation for checking cut and break prices,
 * producing alerts when hit.
 *
 */
public class CutBreakNotifier extends Notifier {
  
  private final String CUT_BREAK_HEADER = "Cut/Break List";
  private final String CUT_PREFIX = "Sell ";
  private final String CUT_SUFFIX = " << SELL";
  private final String BREAK_PREFIX = "Buy ";
  private final String BREAK_SUFFIX = " << BUY ";

  public CutBreakNotifier() {
    getLogger().info("Setting up Cut-break Notifier...");
  }

  /*
   * (non-Javadoc)
   * @see com.pse.notif.types.Notifier#doFilter()
   */
  @Override
  public void doFilter() throws Exception {
    getLogger().info("Filtering stocks for cut/break...");
    this.ddg.clearMessage();
    
    //check cut and break price of each stock and alert if hit
    for (Stock stock: this.stocks) {
      if (stock.hasApiData()) {
        if (AppUtils.isBelowCut(stock)) {
          this.ddg.addMessage(CUT_PREFIX + stock.toString());
          getLogger().info(stock.toString() + CUT_SUFFIX);
          continue;
        } 
        if (AppUtils.isAboveBreak(stock)) {
          this.ddg.addMessage(BREAK_PREFIX + stock.toString());
          getLogger().info(stock.toString() + BREAK_SUFFIX);
          continue;
        }
        getLogger().info(stock.toString());
      }
      this.notifStocks.remove(stock);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.pse.notif.types.Notifier#setDisplayGenerator()
   */
  @Override
  public void setDisplayGenerator() throws Exception {
    this.ddg = DesktopDisplayGenerator.getInstance();
    this.ddg.setDisplayHead(CUT_BREAK_HEADER);
    this.ddg.clearMessage();
  }
}
