package com.datvexpress.ws.versatune.background;

import com.datvexpress.ws.versatune.repo.RcvrSignalRepository;
import com.datvexpress.ws.versatune.screenutils.DisplayMessage;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.datvexpress.ws.versatune.enums.TunerStatus;
import com.datvexpress.ws.versatune.model.RcvrSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DvbtWorker implements Runnable {
    Logger logger = LoggerFactory.getLogger(getClass());

    private final RcvrSignalRepository repo;
    private final DisplayMessage displayMessage;
    private final ApplicationContext context;
    private final TaskExecutor executor;

    public DvbtWorker(RcvrSignalRepository repo,
                      DisplayMessage displayMessage,
                      ApplicationContext context,
                      TaskExecutor executor
    ){
        this.repo = repo;
        this.displayMessage = displayMessage;
        this.context = context;
        this.executor = executor;
    }
    List<String> allowedExtensions = Arrays.asList("jpeg", "mp4", "png", "jpg", "gif");

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
        int finishedButton = 1;
        boolean tunerFound = false;
        boolean tunerLocked = false;
        boolean tunerInitialized = false;
        boolean tunerStreamActive = false;
        boolean slideShowRunning = false;
        boolean displayWanted = true;       // we want to display info on screen when VLC not running
        int tunerFreq = 0;
        int tunerBW = 0;


        // System.setProperty("jna.library.path","/usr/lib/cgi-bin/jna");
        StdC libc = StdC.INSTANCE;
        int fd_status_fifo = -1;
        try{
            // need another while loop around this that determines if this tuner should be running

            logger.trace("Running Fifo CommandLine Runner");
            // this starts up VLC and Combituner for DVBT
            executeCommands();
            //wait to make sure Combituner starts up and fifo is ready
            Thread.sleep((5000));
            logger.trace("trying to call a c library function.");
            displayWanted  = true;

            // let us try to use JNA here to access the std c library.
            String fifoPath = "/home/pi/dvbt/knucker_status_fifo";
            byte[] buffer = new byte[1024];

            logger.trace("Am just about to open the fifo");

            fd_status_fifo = libc.open("/home/pi/knucker_status_fifo", O_RDONLY);
            logger.trace("Fifo is open and fd_status_fifo is " + fd_status_fifo);
            // Set the status fifo to be non-blocking on empty reads
            logger.trace("trying to set file to O_NONBLOCK");
            int errorValue = libc.fcntl(fd_status_fifo, F_SETFL, O_NONBLOCK);
            logger.trace("fcntl returned a " + errorValue);

            if (fd_status_fifo < 0)  // failed to open
            {
                logger.error("Failed to open knucker status fifo\n");
                return;
            }
            // preparing here to see if anything coming in on this tuner
            int PollCount = 0;
            byte[] inputBuffer = new byte[50];
            StringBuffer line = new StringBuffer();
            int tunerAliveHits = 0;
            tunerStreamActive = false;
            int maxZeroCount = 120;
            int currentZeroCount = 0;

            while (true) {
                int num = libc.read(fd_status_fifo, buffer, 1);
                if (logger.isTraceEnabled()) logger.trace("NUM is " + num);
                if (num < 0) {
                    Thread.sleep(500);
                    PollCount++;
                    if (logger.isTraceEnabled()) logger.trace("PollCount is: " + PollCount);
                    if (PollCount > 120) {
                        logger.info("60 seconds since we saw any data. Exiting.");
                        PollCount = 0;
                        break;
                    }
                } else if (num == 0 ){
                    if ( currentZeroCount++ >- maxZeroCount){
                        if (!slideShowRunning){
                            logger.info("Running Slide Show");
                            startSlideShow2();
                            slideShowRunning = true;
                            tunerStreamActive = false;
                        }
                    }
                } else {
                    currentZeroCount = 0;
                    try {
                        String inputString = new String(buffer, StandardCharsets.UTF_8);
                        if (logger.isTraceEnabled())
                            logger.trace("inputString is: " + inputString.substring(0, 1) + " [" + buffer[0] + "] ");
                        if (inputString.substring(0, 1).equals("\n") || buffer[0] == 10) {
                            // we have an end of line
                            logger.info(line.toString());
                            int responseStatus = processResponse(line.toString(), displayWanted);
                            line = new StringBuffer();
                            // process this request.
                            if ( responseStatus == TunerStatus.SSI_FOUND.ordinal() ||
                                 responseStatus == TunerStatus.PER_FOUND.ordinal() ||
                                 responseStatus == TunerStatus.SQI_FOUND.ordinal() ||
                                 responseStatus == TunerStatus.SNR_FOUND.ordinal() ||
                                 responseStatus == TunerStatus.MOD_FOUND.ordinal()
                            ){
                                if (!tunerStreamActive){
                                    startTunerStream2();
                                    tunerStreamActive = true;
                                    slideShowRunning = true;
                                }
                            }else if ( responseStatus == TunerStatus.SEARCH_FAILED_RESETTING_FOR_NEW_SEARCH.ordinal()){
                                if (!slideShowRunning){
                                    logger.info("Running Slide Show");
                                    startSlideShow2();
                                    slideShowRunning = true;
                                    tunerStreamActive = false;
                                }
                            }
                            if ( responseStatus == TunerStatus.TUNER_UNLOCKED.ordinal()){
                                // turn on the slide show for 10 seconds
                                if (!slideShowRunning){
                                    logger.info("Running Slide Show");
                                    startSlideShow2();
                                    slideShowRunning = true;
                                    tunerStreamActive = false;
                                }
                            }else if(responseStatus == TunerStatus.SIGNAL_LOCKED.ordinal()){
                                if (!tunerStreamActive){
                                    logger.info("restart streaming of the tuner");
                                  //  startTunerStream();
                                    startTunerStream2();
                                    tunerStreamActive = true;
                                    slideShowRunning = false;
                                }
                            }
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



    /*
            Need this to be a future and have the future return true when the stream is running
            then we can set the stream active to true.
     */
    private void startTunerStream2(){
        StartTunerStreamTask t = new StartTunerStreamTask();
        executor.execute(t);
    }



    private void startSlideShow2(){
        StartSlideShowTask t = new StartSlideShowTask();
        executor.execute(t);
    }

    private int processResponse(String response, boolean displayWanted){
        if (logger.isTraceEnabled()) logger.trace("processResponse: ENTERED.");
        int status = -1;
        String messageText = "";
        if (response.equals("[GetFamilyId] Family ID:0x4955")){
            if (logger.isTraceEnabled()) logger.trace("Initializing Tuner, Please Wait.");
            messageText = "Initializing Tuner, Please Wait.";
            status = TunerStatus.INITIALIZING_TUNER.ordinal();
        }else if(response.equals("[GetChipId] chip id:AVL6862")){
            if (logger.isTraceEnabled()) logger.trace("Found Knucker Tuner");
            messageText = "Found Versatune.";
            status = TunerStatus.TUNER_FOUND.ordinal();
        }else if(response.equals("[AVL_Init] AVL_Initialize Failed!")){
            if (logger.isTraceEnabled()) logger.trace("Failed to initialize tuner. Change USB Cable.");
            messageText = "Failed to iniialize tuner. Change USB Cable.";
            status = TunerStatus.INITIALIZE_FAILED.ordinal();
        }else if(response.equals("[AVL_Init] ok")){
            if (logger.isTraceEnabled()) logger.trace("Tuner Initialized.");
            messageText = "Tuner Initialized.";
            status = TunerStatus.TUNER_INITIALIZED.ordinal();
        }else if(response.equals("Tuner not found")){
            if (logger.isTraceEnabled()) logger.trace("Please connect a Versatune Tuner");
            messageText = "Please connect a Versatune Tuner.";
            status = TunerStatus.NO_TUNER_FOUND.ordinal();
        }else if(response.equals("locked")){
            if (logger.isTraceEnabled()) logger.trace("Tuner Locked");
            messageText = "Signal locked";
            status = TunerStatus.SIGNAL_LOCKED.ordinal();
        }else if(response.contains("=== Freq")){
            String freq = response.substring(11);
            if (logger.isTraceEnabled()) logger.trace("Tuner Frequency is: "+ freq);
            messageText = "Tuner Frequency is: "+ freq;
            status = TunerStatus.NEW_FREQ_DATA.ordinal();
        }else if(response.contains("=== Bandwidth")){
            String bandwidth = response.substring(17);
            if (logger.isTraceEnabled()) logger.trace("Tuner Bandwidth is: "+ bandwidth);
            messageText = "Tuner Bandwidth is: "+ bandwidth;
            status = TunerStatus.NEW_BW_DATA.ordinal();
        }else if(response.contains("[AVL_ChannelScan_Tx] Freq")){
            String bandwidth = response.substring(17);
            if (logger.isTraceEnabled()) logger.trace("Searching for signal");
            messageText = "Searching for signal";
            status = TunerStatus.SEARCHING_FOR_SIGNAL.ordinal();
        }else if(response.contains("[DVBTx_Channel_ScanLock_Example] DVBTx channel scan is fail,Err.")){
            String bandwidth = response.substring(17);
            if (logger.isTraceEnabled()) logger.trace("Search failed, resetting for another search");
            messageText = "Search failed, resetting for another search";
            status = TunerStatus.SEARCH_FAILED_RESETTING_FOR_NEW_SEARCH.ordinal();
        }else if(response.contains("[AVL_LockChannel_T] Freq ")){
            if (logger.isTraceEnabled()) logger.trace("Signal detected, attempting to lock");
            messageText = "Signal detected, attempting to lock";
            status = TunerStatus.SIGNAL_DETECTED_ATTEMPTING_LOCK.ordinal();
        }else if(response.contains("Unlocked")){
            if (logger.isTraceEnabled()) logger.trace("Tuner Unlocked");
            messageText = "Tuner Unlocked";
            status = TunerStatus.TUNER_UNLOCKED.ordinal();
        }else if( response.contains("[AVL_LockChannel_T] Freq is 423 MHz, Bandwidth is 2.000000 MHz, Layer Info is 1 (0 : LP; 1 : HP)")){
            messageText = "[AVL_LockChannel]";
            status = TunerStatus.TUNER_ONLINE.ordinal();
        }else if( response.contains("MOD  :")){
            messageText = response;
            status = TunerStatus.MOD_FOUND.ordinal();
        }else if( response.contains("FFT  :")){
            messageText = response;
            status = TunerStatus.MOD_FOUND.ordinal();
        }else if( response.contains("Const:")){
            messageText = response;
            status = TunerStatus.MOD_FOUND.ordinal();
        }else if( response.contains("FEC  :")){
            messageText = response;
            status = TunerStatus.FEC_FOUND.ordinal();
        }else if( response.contains("SSI is")){
            messageText = response;
            status = TunerStatus.SSI_FOUND.ordinal();
        }else if( response.contains("SQI is")){
            messageText = response;
            status = TunerStatus.SQI_FOUND.ordinal();
        }else if( response.contains("SNR is")){
            messageText = response;
            status = TunerStatus.SNR_FOUND.ordinal();
        }
        displayWanted  = false; // see if this is causing us delay
        if ( displayWanted ){
            displayMessage.dsiplayMessageText(messageText);
        }
        if (response.length() > 3){
            String result = processTunerInputData(response);
            if (result.length() > 3 && result.startsWith("-> ")){
                updateVlcOverlayText(result);
            }
        }
        if (logger.isTraceEnabled()) logger.trace("processResponse: Exiting");
        return status;
    }

    public void updateVlcOverlayText(String text){
        int len = text.length();
        if (logger.isTraceEnabled()) logger.trace("In updateVlcOverlayText with text: " + text);
        if ( text.startsWith("-> ")){
            String overlayText = text.substring(3);
            byte[] buffer = new byte[255];
            buffer = overlayText.getBytes(StandardCharsets.UTF_8);
            StdC libc  = StdC.INSTANCE;
            int fd = libc.open("/home/pi/bob/vlc_overlay.txt", O_RDWR | O_CREAT);
            if (overlayText.length() > 0) {
                int count = libc.write(fd, buffer, buffer.length);
                if (logger.isTraceEnabled()) logger.trace("wrote " + count + " bytes - " + overlayText);
            }
            libc.close(fd);
        }
    }

    public void executeCommands() throws IOException {
        File tempScript = createTempScript();
        if (logger.isTraceEnabled()) logger.trace("creating and starting the process.");
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            if (logger.isTraceEnabled()) logger.trace("Process started... " + process.info());
            process.waitFor();
        }catch (InterruptedException ie){
            logger.error("Caught interrupted exception. Not sure what to do. " + ie.getMessage());
        } finally {
            tempScript.delete();
        }
    }

    public File createTempScript() throws IOException {
        if (logger.isTraceEnabled()) logger.trace("creating the temporary script file.");
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
        if (logger.isTraceEnabled()) logger.trace("Entering processTunerInputData");
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
        if (logger.isTraceEnabled()) logger.trace("Exiting processTunerInputData: " + result);
        return result;
    }

}

