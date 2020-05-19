package deadlock;

import java.util.Random;

public class Demo {

  public static void main(String[] args) {
    Intersection intersection = new Intersection();
    TrainA trainA = new TrainA(intersection);
    TrainB trainB = new TrainB(intersection);

    new Thread(trainA).start();
    new Thread(trainB).start();
  }

  private static class TrainA implements Runnable {
    private Intersection intersection;
    private Random random = new Random();

    public TrainA(Intersection intersection) {
      this.intersection = intersection;
    }

    @Override
    public void run() {
      while (true) {
        long sleepingFor = random.nextInt(5);
        try {
          Thread.sleep(sleepingFor);
        } catch (InterruptedException e) {
        }
        intersection.takeRoadA();
      }
    }
  }

  private static class TrainB implements Runnable {
    private Intersection intersection;
    private Random random = new Random();

    public TrainB(Intersection intersection) {
      this.intersection = intersection;
    }

    @Override
    public void run() {
      while (true) {
        long sleepingFor = random.nextInt(5);
        try {
          Thread.sleep(sleepingFor);
        } catch (InterruptedException e) {
        }
        intersection.takeRoadB();
      }
    }
  }

  private static class Intersection {
    private final Object roadA = new Object();
    private final Object roadB = new Object();

    void takeRoadA() {
      int a = 0;
      a = a--;
      int b = a;

      synchronized (roadB) {
        System.out.println("Road A is blocked by " + Thread.currentThread().getName());

        synchronized (roadA) {
          System.out.println("Train is passing through Road B");
          try {
            Thread.sleep(1);
          } catch (InterruptedException e) {
          }
        }
      }
    }

    void takeRoadB() {
      synchronized (roadB) {
        System.out.println("Road B is blocked by " + Thread.currentThread().getName());

        synchronized (roadA) {
          System.out.println("Train is passing through Road A");
          try {
            Thread.sleep(1);
          } catch (InterruptedException e) {
          }
        }
      }
    }
  }
}
