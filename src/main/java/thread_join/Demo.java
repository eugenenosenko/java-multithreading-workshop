package thread_join;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {

  public static void main(String[] args) throws InterruptedException {

    List<ThreadFactorial> threads =
        new ArrayList<>(
            Arrays.asList(
                new ThreadFactorial(1020L),
                new ThreadFactorial(22002L),
                new ThreadFactorial(303310L)));

    for (ThreadFactorial t : threads) {
      //            t.setDaemon(true);
      t.start();
    }

    for (ThreadFactorial t : threads) {
      t.join(500);
    }

    for (ThreadFactorial t : threads) {
      if (t.isFinished()) {
        System.out.println("Factorial for " + t.getNumber() + " is " + t.getResult());
      } else {
        System.out.println("Calculation is still on-going");
        System.out.println("Shutting down the thread " + t.getName());
        t.interrupt();
      }
    }
  }

  private static class ThreadFactorial extends Thread {
    private final long number;
    private boolean finished;
    private BigInteger result = BigInteger.ZERO;

    ThreadFactorial(long number) {
      this.setName("ThreadFactorial-");
      this.number = number;
    }

    public long getNumber() {
      return number;
    }

    public boolean isFinished() {
      return this.finished;
    }

    @Override
    public void run() {
      result = calculateFactorial(number);
      finished = true;
    }

    public BigInteger getResult() {
      return result;
    }

    public BigInteger calculateFactorial(long number) {
      BigInteger tempValue = BigInteger.ONE;
      for (long i = number; i > 0; i--) {
        if (Thread.currentThread().isInterrupted()) {
          System.out.println("Shutting down the thread");
          return BigInteger.ONE;
        }
        tempValue = tempValue.multiply(new BigInteger(String.valueOf(i)));
      }

      return tempValue;
    }
  }
}
