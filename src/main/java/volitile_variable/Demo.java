package volitile_variable;

public class Demo {
  public static void main(String[] args) throws InterruptedException {
    VolatileTestClass volatileTestClass = new VolatileTestClass();

    Thread t =
        new Thread(
            () -> {
              for (int i = 0; i < 100000; i++) {
                volatileTestClass.increment();
              }
            });

    Thread t1 =
        new Thread(
            () -> {
              for (int i = 0; i < 100000; i++) {
                volatileTestClass.decrement();
              }
            });

    t.start();
    t1.start();

    t.join();
    t1.join();

    System.out.println("volatileTestClass.volatileLong ===> " + volatileTestClass.volatileLong);
  }

  static class VolatileTestClass {
    volatile long volatileLong = 0;

    void increment() {
      ++volatileLong;
    }

    void decrement() {
      --volatileLong;
    }
  }
}
