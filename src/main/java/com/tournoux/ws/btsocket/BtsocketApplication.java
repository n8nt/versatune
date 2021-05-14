package com.tournoux.ws.btsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BtsocketApplication extends SpringBootServletInitializer {

    Logger APP_LOG = LoggerFactory.getLogger(getClass());

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(BtsocketApplication.class);
    }
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(BtsocketApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }

}
