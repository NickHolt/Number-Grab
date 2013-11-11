package core;

import java.util.ArrayList;


/**
 *  A simple timer class based on elapsed wall-clock time.  May be
 *  stopped and started under program control, and provides both
 *  accumulated time and time since last started in units of msec.
 *  At any given point, a given timer has some number of properly 
 *  nested <dfn>subtimers</dfn> running.  While one subtimer is
 *  running, you can start and stop another, allowing you to time
 *  parts of some larger activity while still computing the time
 *  for the entire activity. 
 */
public class Stopwatch {

  /** A stopped timer (! isRunning ()) with 0msec accumulated time. */
  public Stopwatch () {
    reset();
  }

  /** True iff there are subtimers of THIS running. */
  public boolean isRunning () {
    return ! startingTimes.isEmpty ();
    
  }

  /** The number of nested subtimers currently running. */
  public int getRunning () {
    return startingTimes.size ();
  }

  /** Start a new subtimer at the current time. */
  public void start() {
    startingTimes.add (System.currentTimeMillis ());
  }

  /** 
   *  Stops the most recently started, still-running subtimer, returning 
   *  the time elapsed since it started in milliseconds.  Throws 
   *  IllegalStateException if !isRunning ().
   */
  public long stop() {
    int count = getRunning ();
    if (count == 0)
      throw new IllegalStateException ("no subtimer running");
    long finish = System.currentTimeMillis();
    long diff = finish - startingTimes.remove (count - 1);
    if (count == 1) 
      accum += diff;
    return diff;
  }

  /** The time in milliseconds since the latest, still-running 
   *  subtimer started.  Throws IllegalStateException if !isRunning (). */
  public long getElapsed() {
    int N = getRunning ();
    if (N > 0)
      return System.currentTimeMillis() - startingTimes.get (N-1);
    else
      throw new IllegalStateException ("no subtimer is running");
  }

  /** The total time in milliseconds that isRunning () has been true 
   *  since THIS was created or reset. */
  public long getAccum () {
    if (isRunning ())
      return accum + System.currentTimeMillis() - startingTimes.get (0);
    else
      return accum;
  }

  /** Stop all subtimers, and set accumulated time to 0. */
  public void reset() {
    startingTimes.clear ();
    accum = 0;
  }

  /** Total accumulated time that isRunning () has been true since last
   *  reset. */
  private long accum;
  /** Stack of starting times of current subtimers, with latest last. */
  private ArrayList<Long> startingTimes = new ArrayList<Long> ();
}
