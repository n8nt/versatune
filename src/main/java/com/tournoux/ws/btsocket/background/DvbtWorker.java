package com.tournoux.ws.btsocket.background;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.tournoux.ws.btsocket.enums.TunerStatus;
import com.tournoux.ws.btsocket.model.RcvrSignal;
import com.tournoux.ws.btsocket.repo.RcvrSignalRepository;
import com.tournoux.ws.btsocket.screenutils.DisplayMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DvbtWorker implements Runnable {
    Logger logger = LoggerFactory.getLogger(getClass());

    private final RcvrSignalRepository repo;
    private final DisplayMessage displayMessage;

    public DvbtWorker(RcvrSignalRepository repo,
                      DisplayMessage displayMessage){
        this.repo = repo;
        this.displayMessage = displayMessage;
    }

    int O_RDONLY = 0x0000;
    int O_WRONLY = 0x0001;
    int O_RDWR =   0x0002;
    int F_SETFL =  0x0003;
    int O_CREAT  = 0x0200;
    int O_NONBLOCK = 0x0004;

    public interface StdC extends Library {
        StdC INSTANCE = Native.load("c", StdC.class);
        int mkfifo(String path);
        int fcntl(int value, int cmd, long arg);
        int close(int fd);
        int open(String path, int value);
        int read(int fd, byte[] buffer, int count);
        int write(int fd, byte[] buffer, int count);

    }

    @Override
    public void run() {
        logger.info("Running DvbtWorker");


        // System.setProperty("jna.library.path","/usr/lib/cgi-bin/jna");
        StdC libc = StdC.INSTANCE;
        int fd_status_fifo = -1;
        try{
            logger.info("Running Fifo CommandLine Runner");
            executeCommands();
            //wait to make sure Combituner starts up and fifo is ready
            Thread.sleep((5000));
            logger.info("trying to call a c library function.");

            // let us try to use JNA here to access the std c library.
            String fifoPath = "/home/pi/dvbt/knucker_status_fifo";
            byte[] buffer = new byte[1024];


            logger.info("Am just about to open the fifo");
//            fd_status_fifo = libc.open("/home/pi/knucker_status_fifo",O_RDWR);
//            logger.info("opened for RD-WR");
//            libc.close(fd_status_fifo);
            fd_status_fifo = libc.open("/home/pi/knucker_status_fifo", O_RDONLY);
            logger.info("Fifo is open and fd_status_fifo is " + fd_status_fifo);
            // Set the status fifo to be non-blocking on empty reads
            logger.info("trying to set file to O_NONBLOCK");
            int errorValue = libc.fcntl(fd_status_fifo, F_SETFL, O_NONBLOCK);
            logger.info("fcntl returned a " + errorValue);

            if (fd_status_fifo < 0)  // failed to open
            {
                logger.error("Failed to open knucker status fifo\n");
                return;
            }
            int PollCount = 0;
            byte[] inputBuffer = new byte[50];
            StringBuffer line = new StringBuffer();
            while (true) {
                int num = libc.read(fd_status_fifo, buffer, 1);
                if (logger.isTraceEnabled()) logger.trace("NUM is " + num);
                if (num < 0) {
                    Thread.sleep(500);
                    PollCount++;
                    logger.info("PollCount is: " + PollCount);
                    if (PollCount > 120) {
                        logger.info("60 seconds since we saw any data. Exiting.");
                        PollCount = 0;
                        break;
                    }
                } else {

                    try {
                        String inputString = new String(buffer, StandardCharsets.UTF_8);
                        if (logger.isTraceEnabled())
                            logger.trace("inputString is: " + inputString.substring(0, 1) + " [" + buffer[0] + "] ");
                        if (inputString.substring(0, 1).equals("\n") || buffer[0] == 10) {
                            // we have an end of line
                            logger.info(line.toString());
                            int responseStatus = processResponse(line.toString());
                            line = new StringBuffer();
                            // process this request.

                        } else {
                            line.append(inputString.substring(0, 1));
                        }
                    } catch (Exception e) {
                        logger.error("Got exception. " + e.getMessage());
                    }
                }
            }
            libc.close(fd_status_fifo);

        }catch(Exception e){
            logger.error("Error occured. " + e.getMessage());
        }finally {
            try{
                libc.close(fd_status_fifo);
            }catch(Exception e2){
                logger.error("Could not close fifo upon exception.");
            }
        }

        logger.info("we are done.");
    }


    private int processResponse(String response){
        logger.info("processResponse: ENTERED.");
        int status = -1;
        if (response.equals("[GetFamilyId] Family ID:0x4955")){
            logger.info("Initializing Tuner, Please Wait.");
            displayMessage.dsiplayMessageText("Initializing Tuner, Please Wait.");

            status = TunerStatus.INITIALIZING_TUNER.ordinal();
        }else if(response.equals("[GetChipId] chip id:AVL6862")){
            logger.info("Found Knucker Tuner");
            displayMessage.dsiplayMessageText("Found Versatune.");
            status = TunerStatus.TUNER_FOUND.ordinal();
        }else if(response.equals("[AVL_Init] AVL_Initialize Failed!")){
            logger.info("Failed to initialize tuner. Change USB Cable.");
            displayMessage.dsiplayMessageText("Failed to iniialize tuner. Change USB Cable.");
            status = TunerStatus.INITIALIZE_FAILED.ordinal();
        }else if(response.equals("[AVL_Init] ok")){
            logger.info("Tuner Initialized.");
            displayMessage.dsiplayMessageText("Tuner Initialized.");
            status = TunerStatus.TUNER_INITIALIZED.ordinal();
        }else if(response.equals("Tuner not found")){
            logger.info("Please connect a Versatune Tuner");
            displayMessage.dsiplayMessageText("Please connect a Versatune Tuner.");
            status = TunerStatus.NO_TUNER_FOUND.ordinal();
        }
        if (response.length() > 3){
            String result = processTunerInputData(response);
            if (result.length() > 3 && result.startsWith("-> ")){
                updateVlcOverlayText(result);
            }
        }
        logger.info("processResponse: Exiting");
        return status;
    }

    public void updateVlcOverlayText(String text){
        int len = text.length();
        logger.info("In updateVlcOverlayText with text: " + text);
        if ( text.startsWith("-> ")){
            String overlayText = text.substring(3);
            byte[] buffer = new byte[255];
            buffer = overlayText.getBytes(StandardCharsets.UTF_8);
            StdC libc  = StdC.INSTANCE;
            int fd = libc.open("/home/pi/bob/vlc_overlay.txt", O_RDWR | O_CREAT);
            if (overlayText.length() > 0) {
                int count = libc.write(fd, buffer, buffer.length);
                logger.info("wrote " + count + " bytes - " + overlayText);
            }
        }
    }

    public void executeCommands() throws IOException {

        File tempScript = createTempScript();
        logger.info("creating and starting the process.");
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            logger.info("Process started... " + process.info());
            process.waitFor();
        }catch (InterruptedException ie){
            logger.error("Caught interrupted exception. Not sure what to do. " + ie.getMessage());
        } finally {
            tempScript.delete();
        }
    }

    public File createTempScript() throws IOException {
        logger.info("creating the temporary script file.");
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash -x");
        printWriter.println("cd /home/pi/dvbt");

        printWriter.println("su -c '/home/pi/dvbt/dvb-t_start.sh' pi &");

        printWriter.close();

        return tempScript;
    }

    private String processTunerInputData(String iModel)
    {
        logger.info("Entering processTunerInputData");
        RcvrSignal signal = null;
        List<RcvrSignal> recs = repo.findAll();

        Optional<RcvrSignal> recRef = repo
                .findAll()
                .stream()
                .filter(p -> p.isReady() == false)
                .findFirst();
        if (! recRef.isPresent()){
            signal = new RcvrSignal("","","","");
            signal.setReady(false);
            signal.setTimestamp(System.currentTimeMillis());
        }else{
            signal = recRef.get();
        }

        String test = iModel.toUpperCase().substring(0,3);

        switch(test){
            case "SSI":
                if (signal.getSsi().isEmpty() || signal.getSsi().isBlank()){
                    signal.setSsi(iModel);

                }
                break;
            case "SQI":
                signal.setSqi(iModel);
                if (signal.getSqi().isEmpty() || signal.getSqi().isBlank()){
                    signal.setSqi(iModel);
                }
                break;
            case "SNR":
                signal.setSnr(iModel);
                if (signal.getSnr().isEmpty() || signal.getSnr().isBlank()){
                    signal.setSnr(iModel);
                }
                break;
            case "PER":
                signal.setPer(iModel);
                if (signal.getPer().isEmpty() || signal.getPer().isBlank()){
                    signal.setPer(iModel);
                }
                break;
            default:
        }
        // now check if all 4 parts are present
        if ( signal.getSsi().isEmpty() || signal.getSsi().isBlank())
            signal.setReady(false);
        else if ( signal.getSqi().isEmpty() || signal.getSqi().isBlank())
            signal.setReady(false);
        else if ( signal.getSnr().isEmpty() || signal.getSnr().isBlank())
            signal.setReady(false);
        else if ( signal.getPer().isEmpty() || signal.getPer().isBlank())
            signal.setReady(false);
        else
            signal.setReady(true);

        repo.save(signal);
        String result = "NO DATA";
        if ( signal.isReady()){
            result = "-> SSI=" +  signal.getSsi().substring(7) + "  SQI=" + signal.getSqi().substring(7) + "  SNR=" + signal.getSnr().substring(7) + "  PER=" + signal.getPer().substring(7);
            long currentTime = System.currentTimeMillis();
            List<RcvrSignal> batch = new ArrayList<>();
            for( RcvrSignal rs : recs){
                if ( rs.getTimestamp()< currentTime - 30000l){
                    batch.add(rs);
                }
            }
            if ( batch.size() > 0){
                repo.deleteInBatch(batch);
            }
        }
        logger.info("Exiting processTunerInputData: " + result);
        return result;
    }

}

