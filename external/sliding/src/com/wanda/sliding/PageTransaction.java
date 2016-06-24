package com.wanda.sliding;

/**
 * 由于进入进出顺序不确定，需要判断一些 transaction 状态
 * 此类用于维护 transaction
 *
 *
 */
public class PageTransaction {

  /**
   * 是否允许视察动画
   */
  private boolean mAnimEnable;

  /**
   * 是否设定了进入动画
   */
  private boolean mIsEnterAnimSchedule;

  /**
   * 是否执行了进入、退出动画
   */
  private boolean mIsPerformedEnterAnim;
  private boolean mIsPerformedExitAnim;

  /**
   * 是否调用了进入、退出结束。设置此值的意义在于，可能因为某种条件进入退出动画时序问题导致并没有成功执行，
   * 但是方法调用了就代表 activity 的生命周期对儿调用完成了，需要开始新的动画对儿
   */
  private boolean mEndEnterTransaction;
  private boolean mEndExitTransaction;
  /**
   * 代表是否触发过finish
   */
  private boolean mIsFinished;

  public PageTransaction() {}

  public boolean isAnimEnable() {
    return mAnimEnable;
  }

  public void setAnimEnable(boolean isEnable) {
    mAnimEnable = isEnable;
  }

  public boolean isEnterAnimSchedule() {
    return mIsEnterAnimSchedule;
  }

  public boolean needDoExitAnim() {
    return mIsPerformedEnterAnim && (!mIsPerformedExitAnim)
        && mEndEnterTransaction && (!mEndExitTransaction);
  }

  public boolean needDoEnterAnim() {
    return (!mIsPerformedEnterAnim) && (!mIsPerformedExitAnim)
        && (!mEndEnterTransaction) && (!mEndExitTransaction);
  }

  public void scheduleEnterAnimSchedule() {
    mIsEnterAnimSchedule = true;
  }

  public void doEnterAnim(SlidingLayout slidingLayout, float enterPosition) {
    mIsPerformedEnterAnim = true;
    slidingLayout.silentSmoothSlideTo(enterPosition);
  }

  public void doExitAnim(SlidingLayout slidingLayout, float closePosition) {
    mIsPerformedExitAnim = true;
    slidingLayout.silentSmoothSlideTo(closePosition);
  }

  public boolean isTransactionEnded() {
    return mEndEnterTransaction && mEndExitTransaction;
  }

  public void endEnterTransaction() {
    mEndEnterTransaction = true;
  }

  public void endExitTransaction() {
    mEndExitTransaction = true;
  }

  public void beginNewTransaction() {
    mIsEnterAnimSchedule = false;
    mIsPerformedEnterAnim = false;
    mIsPerformedExitAnim = false;
    mEndEnterTransaction = false;
    mEndExitTransaction = false;
  }

  public boolean isFinished() {
    return mIsFinished;
  }

  public void setIsFinished(boolean isFinished) {
    mIsFinished = isFinished;
  }
}
