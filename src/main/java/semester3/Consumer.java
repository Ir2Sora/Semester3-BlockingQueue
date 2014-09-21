package semester3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

class Consumer<T> implements Runnable {

   private static final Logger logger = Logger.getLogger(Consumer.class.getName());
   private AtomicInteger index;
   private BlockingQueue<T> bridge;
   private T[] dest;

   public Consumer(BlockingQueue<T> bridge, T[] dest) {
      this.index = new AtomicInteger(0);
      this.bridge = bridge;
      this.dest = dest;
   }

   @Override
   public void run() {
      int takeIndex;
      int putIndex;
      T obj;

      try {
         while (true) {
            synchronized (this) {
               takeIndex = bridge.getTakeIndex();
               putIndex = index.getAndIncrement();
            }

            obj = bridge.take(takeIndex, 1, TimeUnit.SECONDS);

            if (obj != null) {
               dest[putIndex] = obj;
            } else {
               break;
            }
         }
      } catch (InterruptedException ex) {
         logger.log(Level.SEVERE, null, ex);
      }
   }
}
