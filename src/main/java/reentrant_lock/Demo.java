package reentrant_lock;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Demo {

  public static void main(String[] args) throws InterruptedException {
    SharedResourceClass sharedResourceClass = new SharedResourceClass();
    Thread writeTread =
        new Thread(
            () -> {
              while (true) {
                sharedResourceClass.appendNewItem();
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
              }
            });

    writeTread.setDaemon(true);
    ArrayList<Thread> threads = new ArrayList<>();

    for (int i = 0; i < 2; i++) {
      Thread t =
          new Thread(
              () -> {
                while (true) {
                  sharedResourceClass.readList();
                }
              });

      t.setDaemon(true);
      threads.add(t);
    }

    writeTread.start();
    threads.forEach(Thread::start);

    System.out.println("Waiting 10 sec for program to finish");
    Thread.sleep(5000L);
  }

  static class SharedResourceClass {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private TreeSet<String> sharedList = new TreeSet<>();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    void appendNewItem() {
      notify();
      writeLock.lock();
      System.out.println("Locking down for edit");
      sharedList.add(String.valueOf(System.currentTimeMillis()));
      writeLock.unlock();
    }

    void readList() {
      if (readLock.tryLock()) {
        if (sharedList.isEmpty()) {
          System.out.println("Shared resource is empty");
        } else {
          System.out.println(sharedList.last());
        }
        readLock.unlock();
      } else {
        System.out.println("Currently another thread is making changes to the shared resource");
        try {
          Thread.sleep(550);
        } catch (InterruptedException e) {
        }
      }
    }
  }
}
