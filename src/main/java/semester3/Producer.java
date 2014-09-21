package semester3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

class Producer<T> implements Runnable {

   private static final Logger logger = Logger.getLogger(Producer.class.getName());
   private AtomicInteger index;
   private T[] src;
   private BlockingQueue<T> bridge;

   public Producer(T[] src, BlockingQueue<T> bridge) {
      this.index = new AtomicInteger(0);
      this.src = src;
      this.bridge = bridge;
   }

   @Override
   public void run() {
      int takeIndex;
      int putIndex;
      try {
         while (true) {
            synchronized (this) {
               takeIndex = index.getAndIncrement();
               putIndex = bridge.getPutIndex();
            }

            if (takeIndex < src.length) {
               bridge.put(src[takeIndex], putIndex);
            } else {
               break;
            }
         }
      } catch (InterruptedException ex) {
         logger.log(Level.SEVERE, null, ex);
      }
   }
}
