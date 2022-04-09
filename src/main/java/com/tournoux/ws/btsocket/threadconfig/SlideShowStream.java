//package com.tournoux.ws.btsocket.threadconfig;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.LinkedBlockingDeque;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Configuration
//public class SlideShowStream {
//    Logger logger = LoggerFactory.getLogger(getClass());
//
//    /*
//    slideShowStreamExecutor:
//  corePoolSize: 4
//  maxPoolSize: 16
//  queueCapacity: 64
//  poolTimeToLive: 60
//  poolNamePrefix: slideshow
//     */
//    @Bean
//    SlideShowStream.CustomThreadPoolExecutor slideShowStreamThreadPoolExecutor(
//            @Value("${slideShowStreamExecutor.corePoolSize}") int corePoolSize,
//            @Value("${slideShowStreamExecutor.maxPoolSize}") int maxPoolSize,
//            @Value("${slideShowStreamExecutor.queueCapacity}") int queueCapacity,
//            @Value("${slideShowStreamExecutor.poolTimeToLive}") int poolTimeToLive,
//            @Value("${sliedShowStreamExecutor.poolNamePrefix}") String threadPoolNamePrefix
//    ){
//        return new SlideShowStream.CustomThreadPoolExecutor(corePoolSize,
//                maxPoolSize,
//                queueCapacity,
//                poolTimeToLive,
//                threadPoolNamePrefix);
//    }
//
//    //////////////////////////////////////
//    //                                  //
//    //  COMMON TO ALL THREADING CLASSED //
//    //                                  //
//    //////////////////////////////////////
//
//    static private class CustomThreadPoolExecutor {
//        private Logger log = LoggerFactory.getLogger(getClass());
//        private Executor executor;
//        private LinkedBlockingDeque<Runnable> executorQueue;
//        private AtomicInteger threadId = new AtomicInteger(1);
//
//        CustomThreadPoolExecutor( int corePoolSize,
//                                  int maxPoolSize,
//                                  int queueCapacity,
//                                  int timeToLiveSeconds,
//                                  String threadNamePrefix){
//            executorQueue = new LinkedBlockingDeque<>(queueCapacity);
//            executor = new ThreadPoolExecutor(corePoolSize,
//                    maxPoolSize,
//                    timeToLiveSeconds,
//                    TimeUnit.SECONDS,
//                    executorQueue,
//                    r -> {
//                            Thread workerThread = new Thread(r,
//                                                threadNamePrefix + "-" + threadId.getAndIncrement());
//                                                workerThread.setDaemon(true);
//                                                 return workerThread;
//                    },
//                    ( r,e) -> {
//                        try {
//                            log.info("adding " + threadId + " to the queue.");
//                            executorQueue.put(r);
//                        }catch(InterruptedException intX){
//                            log.info("CustomThreadPoolExecutor interrupted waiting for queue space.", intX);
//                        }
//            });
//        }
//        Executor getExecutor() { return executor;}
//        @SuppressWarnings("unused")
//        LinkedBlockingDeque<Runnable> getExecutorQueue(){return executorQueue;}
//
//    }
//    //        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//    //        executor.setCorePoolSize(4);
//    //        executor.setMaxPoolSize(4);
//    //        executor.setThreadNamePrefix("DVBT_task_executor_thread");
//    //        executor.initialize();
//    //
//    //        return executor;
//
//    static private class SimpleCustomThreadPoolTaskExecutor{
//        private Logger log = LoggerFactory.getLogger(getClass());
//        private LinkedBlockingDeque<Runnable> executorQueue;
//        private AtomicInteger threadId = new AtomicInteger(1);
//        private ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//
//        SimpleCustomThreadPoolTaskExecutor(int corePoolSize,
//                                           int maxPoolSize,
//                                           String threadPrefix){
//            executor.setCorePoolSize(corePoolSize);
//            executor.setMaxPoolSize(maxPoolSize);
//            executor.setThreadNamePrefix(threadPrefix);
//            executor.setBeanName("");
//        }
//    }
//
//
//
//
//
////        SimpleCustomThreadPoolTaskExecutor( int corePoolSize,
////                                  int maxPoolSize,
////                                  int queueCapacity,
////                                  int timeToLiveSeconds,
////                                  String threadNamePrefix){
////            executorQueue = new LinkedBlockingDeque<>(queueCapacity);
////            executor = new ThreadPoolExecutor(corePoolSize,
////                    maxPoolSize,
////                    timeToLiveSeconds,
////                    TimeUnit.SECONDS,
////                    executorQueue,
////                    r -> {
////                        Thread workerThread = new Thread(r,
////                                threadNamePrefix + "-" + threadId.getAndIncrement());
////                        workerThread.setDaemon(true);
////                        return workerThread;
////                    },
////                    ( r,e) -> {
////                        try {
////                            log.info("adding " + threadId + " to the queue.");
////                            executorQueue.put(r);
////                        }catch(InterruptedException intX){
////                            log.info("CustomThreadPoolExecutor interrupted waiting for queue space.", intX);
////                        }
////                    });
////        }
////        Executor getExecutor() { return executor;}
////        @SuppressWarnings("unused")
////        LinkedBlockingDeque<Runnable> getExecutorQueue(){return executorQueue;}
//
//    }
