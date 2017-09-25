package org.szelc.app.antstock.lib.chart;

import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;

import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.view.chart.ChartViewController;

/**
 *
 * @author szelc.org
 */
public class ChartMouseHandler extends Task {

    private static final Logger log = Logger.getLogger(ChartMouseHandler.class);
    private boolean isRunning = false;
    private long eventServiceJump;
    private ChartMouseHandlerCallback callback;
    private final List<MouseEvent> mouseEvents = new ArrayList();

    public ChartMouseHandler() {
    }
    
    

    public ChartMouseHandler(ChartMouseHandlerCallback callback, long eventServiceJump) {
        super();
        this.eventServiceJump = eventServiceJump;
        this.callback = callback;
    }

    public synchronized void push(MouseEvent event) {
        mouseEvents.add(event);
    }

    public synchronized MouseEvent pop() {
        int pointer = mouseEvents.size() - 1;
        if (pointer < 0) {
            //log.debug("Brak zdarzen do o");
            return null;
        }
        //HHlog.debug("POINTER [" + pointer + "]");
        MouseEvent event = mouseEvents.get(pointer);
        mouseEvents.remove(pointer);
        return event;
    }

    @Override
    protected Integer call() {
        log.debug("CALLBACK");
        isRunning = true;
        while (isRunning) {
            try {
                Thread.sleep(eventServiceJump);
                MouseEvent event = pop();
                if (event == null) {
                    continue;
                }
                log.info("CALLBACK " + callback);
                if(callback instanceof ChartViewController){
                    log.info("Call callback");
                    callback.call(event);
                    log.info("After call callback");
                }
                
                
            } catch (InterruptedException ex) {
                log.error("Errro");;
                break;
            }
        }
        return 1;
    }

}
