package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * V1 : Just a normal implementation of 3 different threads, including sleep time (all using runnable interface)
 */
public class App_Concurrent_Map_Thread_Executor_V1
{
    public static void main( String[] args ) throws InterruptedException {
        System.out.println( "Starting MAIN......\n\n\n" );

        Map<String, Integer> map = new ConcurrentHashMap<>(Map.of(
                "A", 1,
                "B", 2,
                "C", 3
        ));

        System.out.println("Original Map: "+map.toString());

        // Writer thread (adding few more keys)
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.execute(()->
        {
            System.out.println("Writing more stuff to the map.....\n");
            map.putIfAbsent("D",12);
            map.putIfAbsent("E",54);
        });

        // Update thread (modifying values)
        pool.execute(()->
        {
            try{
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
                Thread.currentThread().interrupt();
            }
            System.out.println("Updating stuff in the map.....\n");
            map.computeIfPresent("A", (key, value) -> value+99);
            map.computeIfPresent("D",(key, value) -> value+100);
        });


        // Reader Thread
        pool.execute(()->
        {
            System.out.println("Reading the map after changes now.....\n");
            for(int i=0;i<map.size();i++){
                System.out.println("Reader: Current Map: "+map);
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                    Thread.currentThread().interrupt();
                }
            }


        });


        pool.shutdown();
        if(!pool.awaitTermination(1, TimeUnit.SECONDS)){
            pool.shutdownNow();
        }

        System.out.println("Final Map: "+map);

    }
}
