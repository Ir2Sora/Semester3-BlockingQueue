package semester3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class BlockingQueue<T> {

   private int size;
   private AtomicInteger readIndex;
   private AtomicInteger writeIndex;
   private Semaphore[] readLocks;
   private Semaphore[] writeLocks;
   private T[] items;

   public BlockingQueue(int size) throws InterruptedException {
      this.size = size;
      this.items = (T[]) new Object[size];
      this.readIndex = new AtomicInteger();
      this.writeIndex = new AtomicInteger();
      this.readLocks = genLocks(size);
      this.writeLocks = genLocks(size);

      for (Semaphore s : readLocks) {
         s.acquire();
      }
   }

   private Semaphore[] genLocks(int size) {
      Semaphore[] locks = new Semaphore[size];

      for (int i = 0; i < size; i++) {
         locks[i] = new Semaphore(1);
      }

      return locks;
   }

   public int getPutIndex() {
      return writeIndex.getAndIncrement();
   }

   public int getTakeIndex() {
      return readIndex.getAndIncrement();
   }

   public void put(T item) throws InterruptedException {
      put(item, getPutIndex());
   }

   public void put(T item, int index) throws InterruptedException {
      index = index % size;
      writeLocks[index].acquire();
      items[index] = item;

      if (!readLocks[index].tryAcquire()) {
         readLocks[index].release();
      }
   }

   public void put(T item, long time, TimeUnit unit) throws InterruptedException {
      put(item, getPutIndex(), time, unit);
   }

   public void put(T item, int index, long time, TimeUnit unit) throws InterruptedException {
      index = index % size;
      if (!writeLocks[index].tryAcquire(time, unit)) {
         return;
      }

      items[index] = item;

      if (!readLocks[index].tryAcquire()) {
         readLocks[index].release();
      }
   }

   public T take() throws InterruptedException {
      return take(getTakeIndex());
   }

   public T take(int index) throws InterruptedException {
      index = index % size;
      readLocks[index].acquire();

      T item = items[index];

      if (!writeLocks[index].tryAcquire()) {
         writeLocks[index].release();
      }

      return item;
   }

   public T take(long time, TimeUnit unit) throws InterruptedException {
      return take(getTakeIndex(), time, unit);
   }

   public T take(int index, long time, TimeUnit unit) throws InterruptedException {
      index = index % size;
      if (!readLocks[index].tryAcquire(time, unit)) {
         return null;
      }

      T item = items[index];

      if (!writeLocks[index].tryAcquire()) {
         writeLocks[index].release();
      }

      return item;
   }
}
