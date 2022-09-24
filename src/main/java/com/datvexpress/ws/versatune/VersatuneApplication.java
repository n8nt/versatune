package com.datvexpress.ws.versatune;

import com.datvexpress.ws.versatune.background.DvbtWorker;
import com.datvexpress.ws.versatune.background.GpioWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;

@SpringBootApplication

public class VersatuneApplication extends SpringBootServletInitializer {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskExecutor taskExecutor;

//    @Autowired
//    DvbtThreadConfig dvbtConfig;
//
//    @Autowired
//    GpioThreadConfig gpioConfig;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VersatuneApplication.class);
    }

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(VersatuneApplication.class);
        SpringApplication app = new SpringApplication(VersatuneApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }

    @Bean
    CommandLineRunner init() {

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {

                DvbtWorker dvbtWorkeer = applicationContext.getBean(DvbtWorker.class);
                GpioWorker gpioWorker = applicationContext.getBean(GpioWorker.class);
                taskExecutor.execute(dvbtWorkeer);
                taskExecutor.execute(gpioWorker);
//                @Async(value="Dvbt-task-executor")
//                dvbtConfig.threadPoolTaskExecutor().execute(dvbtWorkeer);
//                gpioConfig.threadPoolTaskExecutor().execute(gpioWorker);


            }
        };
    }


}
