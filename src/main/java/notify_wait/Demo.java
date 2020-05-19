package notify_wait;

import java.util.ArrayList;
import java.util.List;

public class Demo {

  public static final String WAITING_THREAD = "[WaitingThread]";

  public static void main(String[] args) throws InterruptedException {
    NotifyWaitSharedResource notifyWaitSharedResource = new NotifyWaitSharedResource();

    //		Thread t1 = new Thread(notifyWaitSharedResource::writeSomethingToList, "[ReaderThread]");
    //		Thread t = new Thread(notifyWaitSharedResource::rearSomethingFromList, WAITING_THREAD);
    //

    //		t1.start();
    //		t.start();
    Thread threadX =
        new Thread(
            () -> {
              try {
                while (true) {
                  Thread.sleep(100);
                  System.err.println("In threadX");

                  // Restore the interrupted status
                  // not really needed here as we know the thread is exiting
                  // but a good practice all the same. (So callers know
                  // we've been interrupted.)
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("ThreadX interrupted and exiting...");
              }
            });
    threadX.start();
    Thread.sleep(1000);
    threadX.interrupt();

    //
    //		InterruptableThread t1 = new InterruptableThread();
    //		InterruptableThread t2 = new InterruptableThread();
    //		t1.start();

    //		Thread.sleep(2000L);
    //		t1.interrupt();

  }

  static class InterruptableThread extends Thread {

    @Override
    public void finalize() {
      System.out.println("Running finalize method...");
    }

    @Override
    public void run() {
      System.out.println("Just running this method.....");
      while (!isInterrupted()) {
        try {
          System.out.println("Sleeping");
          sleep(2500L);
        } catch (InterruptedException e) {
          System.out.println("Interrupted....");
          System.out.println("Is interrupted ===> " + isInterrupted());
          interrupt();
        }
      }
      System.out.println("Finished sleeping...let's go");
    }
  }

  static class NotifyWaitSharedResource {
    private List<String> sharedList = new ArrayList<>();

    void rearSomethingFromList() {
      String name = Thread.currentThread().getName();
      synchronized (this) {
        if (name.equals(WAITING_THREAD)) {
          try {
            System.out.println("Thread => " + name + " and is waiting...");
            wait();
          } catch (InterruptedException e) {
          }
        }
      }

      System.out.println("Thread => " + name + " has woken up...");
      System.out.println("Thread => " + name + " printing the shared value");
      System.out.println(sharedList.get(0));
    }

    void writeSomethingToList() {
      String name = Thread.currentThread().getName();
      System.out.println("Thread => " + name + " is starting to write something...");

      synchronized (this) {
        System.out.println("Thread => " + name + " setting new value...");
        sharedList.add(String.valueOf(System.currentTimeMillis()));
        try {
          Thread.sleep(2500L);
        } catch (InterruptedException e) {

        }
        System.out.println("Thread => " + name + " Notifying sleeping thread to wake up....");
        notify();
      }
    }
  }
}
