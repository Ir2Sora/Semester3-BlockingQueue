package semester3;

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TransferTest {

   private final Random r = new Random();

   @BeforeClass
   public static void setUpClass() {
   }

   @AfterClass
   public static void tearDownClass() {
   }

   @Before
   public void setUp() {
   }

   @After
   public void tearDown() {
   }

   @Test
   public void testTransfer() throws Exception {
      int size = 100;
      Integer[] src = new Integer[size];
      Integer[] dest = new Integer[size];
      
      for (int i = 0; i < size; i++) {
         src[i] = r.nextInt(1000);
      }

      Transfer.transfer(src, dest);
      assertArrayEquals(src, dest);
   }
   
   @Test
   public void testEmptyTransfer() throws Exception {
      Integer[] src = new Integer[0];
      Integer[] dest = new Integer[0];

      Transfer.transfer(src, dest);
      assertArrayEquals(src, dest);
   }
}