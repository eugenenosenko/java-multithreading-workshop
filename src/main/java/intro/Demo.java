package intro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Demo {

  private static final int MAX_PASSWORD = 2222;

  public static void main(String[] args) {
    Random random = new Random();
    Vault vault = new Vault(random.nextInt(MAX_PASSWORD));

    List<Thread> threads = new ArrayList<>();
    threads.add(new PoliceThread());
    threads.add(new DescendingHackerThread(vault));
    threads.add(new AscendingHackerThread(vault));

    threads.forEach(Thread::start);
  }

  private static class PoliceThread extends Thread {
    public PoliceThread() {
      super("PoliceThread");
    }

    @Override
    public void run() {
      for (int i = 20; i > 0; i--) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        System.out.println(i);
      }
      System.out.println("Game over. Police caught you");
      System.exit(0);
    }
  }

  private static class DescendingHackerThread extends HackerThread {

    DescendingHackerThread(Vault vault) {
      super(vault);
    }

    @Override
    public void run() {
      for (int guess = MAX_PASSWORD; guess >= 0; guess--) {
        if (vault.isCorrect(guess)) {
          System.out.println(this.getName() + " has guessed the password " + guess);
          System.exit(0);
        }
      }
    }
  }

  private static class AscendingHackerThread extends HackerThread {

    AscendingHackerThread(Vault vault) {
      super(vault);
    }

    @Override
    public void run() {
      for (int i = 0; i <= MAX_PASSWORD; i++) {
        if (vault.isCorrect(i)) {
          System.out.println(this.getName() + " guessed the password " + i);
          System.exit(0);
        }
      }
    }
  }

  private static class HackerThread extends Thread {
    protected final Vault vault;

    HackerThread(Vault vault) {
      this.vault = vault;
      this.setName(this.getClass().getSimpleName());
      this.setPriority(MAX_PRIORITY);
    }

    @Override
    public void start() {
      System.out.println("Starting thread " + this.getName());
      super.start();
    }
  }

  private static class Vault {
    private final int password;

    Vault(int password) {
      this.password = password;
    }

    boolean isCorrect(int password) {
      try {
        TimeUnit.MILLISECONDS.sleep(10);
      } catch (InterruptedException e) {

      }
      return this.password == password;
    }
  }
}
