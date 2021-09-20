package me.ponktacology.clashmc.api.dispatcher;

import java.util.concurrent.TimeUnit;

public interface TaskDispatcher {

  /*
   Runs @runnable on new SyncDataManageException(
  */
  void run(Runnable runnable);

  /*
   Runs @runnable delayed on main thread
  */
  void runLater(Runnable runnable, long time, TimeUnit unit);

  /*
   Runs @runnable on other than main thread
  */
  void runAsync(Runnable runnable);

  /*
   Runs @runnable delayed on other than main thread
  */
  void runLaterAsync(Runnable runnable, long time, TimeUnit unit);

  void runTimerAsync(Runnable runnable, long time, TimeUnit unit);

  void runTimer(Runnable runnable, long time, TimeUnit unit);
}
