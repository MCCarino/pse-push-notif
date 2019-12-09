package com.pse.notif.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalTime;
import org.joda.time.Seconds;

import com.pse.notif.app.PsePushNotifier;
import com.pse.notif.types.Notifier;

/**
 * 
 * PSE Push Notification Scheduler
 * 
 * Handles timing and schedule of notification run.
 * Sets starts and stops of thread runs.
 *
 */
public class PsePushNotifScheduler {
  
  private static final long DEFAULT_NOTIF_INTERVAL = 1000L * 60L * 5L;
  private static final long DEFAULT_NOTIF_DELAY = 0L;
  private static final long ONE_SECOND = 1000L;
  
  private List<Notifier> notifiers;
  private List<Notifier> runOnceNotifs;
  
  private LocalTime marketOpen = new LocalTime(9, 30);
  private LocalTime marketClose = new LocalTime(15, 30);
  private LocalTime lunchStart = new LocalTime(12, 0);
  private LocalTime lunchEnd = new LocalTime(13, 30);
  
  private static final Logger logger = 
      LogManager.getLogger(PsePushNotifier.class);

  public PsePushNotifScheduler() {
    notifiers = new ArrayList<Notifier>();
    runOnceNotifs = new ArrayList<Notifier>();
  }
  
  public void setNotifiers(List<Notifier> notifiers) {
    this.notifiers = notifiers;
  }
  
  /**
   * Run scheduling. Currently hardcoded to run every five minutes.
   * Enhancement should be to delegate schedule task maintenance to notifiers
   * but retain timer on this scheduler. 
   * 
   * @throws Exception
   */
  public void run() throws Exception {
    getLogger().info("Running scheduler...");
    if (isBeforeOpen()) {
      getLogger().info("Waiting for market open...");
      Thread.sleep(getSleepDuration());
    } 
    
    if (!isMarketClosed()) {
      runInterval();
    } else {
      getLogger().info("Market closed. Terminating.");
    }
  }

  /**
   * Set task interval and run task on timer.
   */
  private void runInterval() {
    TimerTask notifTask = new TimerTask() {
      public void run() {
        try {
          for (Notifier notifier: notifiers) {
            notifier.doNotify();
            if (notifier.isRunOnce()) {
              runOnceNotifs.add(notifier);
            }
          }
          notifiers.removeAll(runOnceNotifs);
          if (isMarketClosed()) {
            getLogger().info("Market now closed!");
            cancel();
            System.exit(0);
          }
          if (isLunchTime()) {
            getLogger().info("Lunch Time!");
            cancel();
            runInterval();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    
    runTimer(notifTask);
  }

  /**
   * Run task on timer. Refactored for rerun during breaks.
   * 
   * @param notifTask
   */
  private void runTimer(TimerTask notifTask) {
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(notifTask, 
        getSleepDuration(), DEFAULT_NOTIF_INTERVAL);
  }

  /**
   * Set sleep time depending on breaks (pre-open, lunch break)
   * 
   * @return
   */
  protected long getSleepDuration() {
    if (isMarketOpen()) { //if current time is open market, do not delay
      return DEFAULT_NOTIF_DELAY;
    }
    
    LocalTime waitForTime = isBeforeOpen() ? marketOpen : lunchEnd;
    LocalTime now = LocalTime.now();
    Seconds seconds = Seconds.secondsBetween(now, waitForTime);
    getLogger().info("Delaying run by " + seconds.toStandardMinutes().getMinutes() + " minute/s.");
    return Long.valueOf(seconds.getSeconds() * ONE_SECOND);
  }

  protected boolean isLunchTime() {
    LocalTime now = LocalTime.now();
    return now.isAfter(lunchStart) && now.isBefore(lunchEnd);
  }

  protected boolean isBeforeOpen() {
    LocalTime now = LocalTime.now();
    return now.isBefore(marketOpen);
  }

  protected boolean isMarketClosed() {
    LocalTime now = LocalTime.now();
    return now.isAfter(marketClose);
  }
  
  protected boolean isMarketOpen() {
    LocalTime now = LocalTime.now();
    return (now.isAfter(marketOpen) && now.isBefore(lunchStart)) ||
        (now.isAfter(lunchEnd) && now.isBefore(marketClose));
  }
  
  public Logger getLogger() {
    return logger;
  }

}
