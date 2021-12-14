package com.tournoux.ws.btsocket;

import com.tournoux.ws.btsocket.pi4j.Pi4jMinimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BtsocketApplication extends SpringBootServletInitializer {

    Logger APP_LOG = LoggerFactory.getLogger(getClass());


    @Autowired
    Pi4jMinimal manager;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BtsocketApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(BtsocketApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }

    @Bean
    CommandLineRunner init() {

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {

                Pi4jMinimal manager = new Pi4jMinimal();
                try {
                    manager.manageGpios();
                    APP_LOG.info("We are running.");
                }catch(Exception e){
                    APP_LOG.error("Well, shucks, Guss, there is an error. " + e.getMessage());
                }
            }
        };
    }

}
