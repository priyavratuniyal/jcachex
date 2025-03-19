package org.example;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * V2 : V1 but with Future and Callable interface
 */
public class App_Concurrent_Map_Thread_Executor_V3 {
    public static void main( String[] args ) throws InterruptedException {
        System.out.println( "Starting MAIN......\n\n\n" );

        Map<String, CacheEntry<Integer>> map = new ConcurrentHashMap<>(Map.of(
                "A", new CacheEntry<>(1, 500),
                "B", 2,
                "C", 3
        ));

        System.out.println("Original Map: "+map.toString());

        ExecutorService pool = Executors.newFixedThreadPool(3);

        // ----------------------------------Threads------------------------------------------


        // ----------------------------Writer thread---------------------------
        // adding few more keys
        Callable<String> writeCallable = () ->{
            map.putIfAbsent("D",12);
            map.putIfAbsent("E",54);
            return map.toString();
        };

        Future<String> writerFuture = pool.submit(writeCallable);

        try{
            String newMap = writerFuture.get();
            System.out.println("Map after adding new key-value pairs: "+newMap+"\n");
        } catch (ExecutionException e) {
            System.out.println("Error in retrieving the new map after the elements are added");
        }

        // ----------------------------Update thread----------------------------
        // (modifying values)
        Callable<Integer> c = () -> {
            int updatedValue = 0;
            if(map.computeIfPresent("A", (key, value) -> value+99)!=null) updatedValue++;
            if(map.computeIfPresent("D",(key, value) -> value+100)!=null) updatedValue++;

            return updatedValue;
        };


        Future<Integer> writeResult = pool.submit(c);

        try{
            System.out.println("Updating stuff in the map.....");
            int updatedValues = writeResult.get();
            System.out.println("Updated Number of Values: "+updatedValues);
            System.out.println("Updated Map: "+map);
        } catch (ExecutionException e) {
            Thread.currentThread().interrupt();
        }


        // ----------------------------Reader Thread----------------------------
        pool.execute(()->
        {
            System.out.println("\nReading the map after changes now.....\n");
            for(int i=0;i<map.size();i++){
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                    Thread.currentThread().interrupt();
                }
                System.out.println("Reader: Current Map: "+map);
            }


        });


        pool.shutdown();
        if(!pool.awaitTermination(1, TimeUnit.SECONDS)){
            pool.shutdownNow();
        }

        System.out.println("Final Map: "+map);

    }
}
