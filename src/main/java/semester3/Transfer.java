package semester3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Transfer {

   private static final int BRIDGE_SIZE = 100;
   private static final int NUM_PRODUCERS = 2;
   private static final int NUM_CONSUMERS = 2;

   public static <T> void transfer(T[] src, T[] dest) throws InterruptedException {
      BlockingQueue<T> bridge = new BlockingQueue<>(BRIDGE_SIZE);
      
      Producer producer = new Producer(src, bridge);
      Consumer consumer = new Consumer(bridge, dest);

      ExecutorService executor = Executors.newCachedThreadPool();
      for (int i = 0; i < NUM_PRODUCERS; i++) {
         executor.execute(producer);
      }
      for (int i = 0; i < NUM_CONSUMERS; i++) {
         executor.execute(consumer);
      }
      executor.shutdown();
      executor.awaitTermination(1, TimeUnit.MINUTES);
   }
}
