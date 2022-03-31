package com.tournoux.ws.btsocket.controller;

import com.tournoux.ws.btsocket.model.DvbtStatus;
import com.tournoux.ws.btsocket.model.RcvrSignal;
import com.tournoux.ws.btsocket.repo.RcvrSignalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/*

    Test command
     curl -X POST -d "{\"status\" : \"Testing this thinig\", \"size\" : 17}" -H "Content-Type: application/json"  http://192.168.1.54:9002/v2
 */
@RestController
public class RcvrStatusController {

    Logger myLog = LoggerFactory.getLogger(getClass());
    private final RcvrSignalRepository repo;

    public RcvrStatusController(RcvrSignalRepository repo){
        this.repo = repo;
    }

    @RequestMapping(value="/v1/{id}", method= RequestMethod.GET)
    public String getRcvrStatus(@PathVariable String id, @RequestHeader HttpHeaders headers, HttpServletRequest servlet){
    myLog.info("Got status: " + id);

        RcvrSignal signal = null;
        List<RcvrSignal> recs = repo.findAll();
        StringBuffer sb = new StringBuffer();
        for (RcvrSignal s : recs){
            sb.append("["+ signal.getPer() + "], " + "[" + signal.getSnr() + "], " + ", [" + signal.getSqi() + "], " + "[" + signal.getSsi() + "]\n");
        }
        return sb.toString();
    }

    @RequestMapping(value="/v2", method=RequestMethod.POST,consumes = "application/json", produces = "application/json")
    public DvbtStatus postRcvStatus(@RequestBody DvbtStatus iModel){
        myLog.info("Got your " + iModel.getStatus());
        iModel.setReturnStatus("Got your " + iModel.getStatus());
        return iModel;
    }

    @RequestMapping(value="/v3", method=RequestMethod.POST /*,consumes = "text/plain", produces = "text/plain"*/)
    public String postRcvStatus(@RequestBody(required = false) String iModel){

        /*
            special characters used:
            pipe = '|' = 7C hex
            comma = ',' = 2C hex
            colon = ':' = 3A hex
            plus = '+' is used to replace the space character.
        */

        /*
         %7C423.000+MHz%2C++2000+kHzMOD++%3A+DVB-T%2C++FFT++%3A+2KConst%3A+QPSK%2C++FEC++%3A+7%2F8Guard%3A+1%2F32SSI+is+100SQI+is+100SNR+is+27.09PER+is+0.00=SNR is 27.09
         */
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
        return result;
    }



}
