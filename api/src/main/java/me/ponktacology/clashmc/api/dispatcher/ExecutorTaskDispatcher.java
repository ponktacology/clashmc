package me.ponktacology.clashmc.api.dispatcher;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorTaskDispatcher implements TaskDispatcher {

  private final ExecutorService executor =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  private final ScheduledExecutorService scheduledExecutor =
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

  @Override
  public void run( Runnable runnable) {
    runnable.run();
  }

  @Override
  public void runLater( Runnable runnable, long time,  TimeUnit unit) {
    runLaterAsync(runnable, time, unit);
  }

  @Override
  public void runAsync( Runnable runnable) {
    executor.execute(runnable);
  }

  @Override
  public void runLaterAsync( Runnable runnable, long delay,  TimeUnit unit) {
    this.scheduledExecutor.schedule(runnable, delay, unit);
  }

  @Override
  public void runTimerAsync( Runnable runnable, long time,  TimeUnit unit) {
    this.scheduledExecutor.scheduleWithFixedDelay(runnable, 0, time, unit);
  }

  @Override
  public void runTimer( Runnable runnable, long time,  TimeUnit unit) {
    runTimerAsync(runnable, time, unit);
  }
}
