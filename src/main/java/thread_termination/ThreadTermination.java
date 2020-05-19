package thread_termination;

import java.math.BigInteger;

public class ThreadTermination {

  public static void main(String[] args) throws InterruptedException {
    PowCalculatingThread pow =
        new PowCalculatingThread(new BigInteger("20000000"), new BigInteger("1000000"));
    pow.start();
    Thread.sleep(50);
    pow.interrupt();

    BlockingThread blockingThread = new BlockingThread();

    blockingThread.start();
    Thread.sleep(500);
    blockingThread.interrupt();
  }

  private static class PowCalculatingThread extends Thread {
    private final BigInteger power;
    private final BigInteger base;

    private PowCalculatingThread(BigInteger base, BigInteger power) {
      this.power = power;
      this.base = base;
    }

    @Override
    public void run() {
      System.out.println(base + "^" + power + " = " + pow(this.base, this.power));
    }

    public BigInteger pow(BigInteger base, BigInteger power) {
      BigInteger result = BigInteger.ONE;

      for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
        if (Thread.currentThread().isInterrupted()) {
          System.out.println("Thread interrupted");

          return BigInteger.ZERO;
        }
        result = result.multiply(base);
      }

      return result;
    }
  }

  private static class BlockingThread extends Thread {

    public BlockingThread() {
      super("BlockingThread");
    }

    @Override
    public void run() {
      while (true) {
        try {
          Thread.sleep(500000);
        } catch (InterruptedException e) {
          System.out.println(this.getName() + " was interrupted");
          return;
        }
      }
    }
  }
}
