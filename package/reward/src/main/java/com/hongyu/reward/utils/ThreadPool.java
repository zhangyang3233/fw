package com.hongyu.reward.utils;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread pool.
 */
public class ThreadPool {

  private static final int CORE_THREAD_NUM = 5;
  private static final long KEEP_ALIVE_TIME = 10;
  public static final ThreadPoolExecutor MIN_PRIOR_EXECUTOR;
  public static final ThreadPoolExecutor NORMAL_PRIOR_EXECUTOR;

  public static enum Priority {
    NORMAL, LOW;
  }

  private ThreadPool() {}

  static {
    MIN_PRIOR_EXECUTOR = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
        KEEP_ALIVE_TIME, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(true),
        new AppThreadFactory(Thread.MIN_PRIORITY));
    NORMAL_PRIOR_EXECUTOR = new ThreadPoolExecutor(CORE_THREAD_NUM, Integer.MAX_VALUE,
        KEEP_ALIVE_TIME, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(true),
        new AppThreadFactory(Thread.NORM_PRIORITY));
  }

  /**
   * Executes task with given priority.
   *
   * @param runnable runnable task
   * @param priority priority
   */
  public static void execute(Runnable runnable, Priority priority) {
    if (priority == Priority.LOW) {
      MIN_PRIOR_EXECUTOR.execute(runnable);
    } else {
      NORMAL_PRIOR_EXECUTOR.execute(runnable);
    }
  }

  /**
   * Executes task with normal priority.
   *
   * @param runnable runnable task
   */
  public static void execute(Runnable runnable) {
    execute(runnable, Priority.NORMAL);
  }

  /**
   * Cancels task.
   *
   * @param runnable runnable task to cancel
   */
  public static void cancel(Runnable runnable) {
    if (runnable == null) {
      return;
    }
    MIN_PRIOR_EXECUTOR.remove(runnable);
    NORMAL_PRIOR_EXECUTOR.remove(runnable);
  }

  private static class AppThreadFactory implements ThreadFactory {
    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final int priority;

    AppThreadFactory(int priority) {
      this.priority = priority;
      group = Thread.currentThread().getThreadGroup();
      namePrefix = "pool-" + priority + "-" + POOL_NUMBER.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable runnable) {
      Thread thread = new Thread(group, runnable, namePrefix + threadNumber.getAndIncrement(), 0);
      thread.setDaemon(false);
      thread.setPriority(priority);
      return thread;
    }
  }
}
