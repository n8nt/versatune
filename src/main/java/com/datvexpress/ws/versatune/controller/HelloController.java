package com.datvexpress.ws.versatune.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class HelloController {


    final private BuildProperties buildProperties;

    public HelloController(BuildProperties buildProperties){
        this.buildProperties = buildProperties;
    }

    @RequestMapping(value="/checkMe", method= RequestMethod.GET)
    public String checkMe(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String currentDateTime = formatter.format(date);

        StringBuilder sb = new StringBuilder("Demo is running at "+ currentDateTime + "</br>");
        sb.append("Artifact:     " + buildProperties.getArtifact() + "</br");
        sb.append("GroupId:      " + buildProperties.getGroup() + "</br>");
        sb.append("Name:         " + buildProperties.getName() + "</br>");
        sb.append("Version:      " + buildProperties.getVersion() + "</br>");
        sb.append("Build Time:   " + buildProperties.getTime() + "</br>");

        return sb.toString();

    }

}
