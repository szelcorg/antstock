package org.szelc.app.antstock.task;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import org.szelc.app.antstock.data.evaluate.Evaluate;
import org.szelc.app.antstock.data.messages.CompanyMessagesList;
import org.szelc.app.antstock.data.quotes.CurrentEvaluateMessages;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.logger.LOG;
import org.szelc.stockthml.MessageLoader;
import org.szelc.stockthml.StockParser;

import java.util.Collections;
import java.util.List;

public class CurrentQuotesTask extends Task<CurrentEvaluateMessages> {
    private static boolean firtStart = true;


    public CurrentQuotesTask() {

    }
    @Override
    protected CurrentEvaluateMessages call() throws Exception {

        LOG.i("RETURNNING FROM TASK");
       // Thread.sleep(5000);
        return null;
    }


    protected CurrentEvaluateMessages call2() throws Exception {
        LOG.i(" CurrentQuotesTask call");
        if (!firtStart) {
            LOG.i("NOT FIRTS - WAIT");
            Thread.sleep(15 * 60 * 1000);
        }

        LOG.i("CompanyMessagesList loading");
        if (firtStart) {
            firtStart = false;
        }
        CurrentEvaluateMessages result = task();
        LOG.i("CurrentEvalueateMessages return");
        return result;
    }


    private CurrentEvaluateMessages task2() throws InterruptedException {
        CurrentEvaluateMessages current = new CurrentEvaluateMessages();

        (new Thread(){
            public void run(){
                List<DayCompanyQuote> currentQuoteList = StockParser.displayQuotesGpwFromBankier();
            }
        }).start();

        Thread.sleep(10000);

        //Collections.sort(currentQuoteList, (a, b) -> a.getCompanyName().compareTo(b.getCompanyName()));
        LOG.i("RETURNED finish task");
        return current;

    }

    private CurrentEvaluateMessages task() {
        CurrentEvaluateMessages current = new CurrentEvaluateMessages();

        List<DayCompanyQuote> currentQuoteList = StockParser.displayQuotesGpwFromBankier();

        Collections.sort(currentQuoteList, (a, b) -> a.getCompanyName().compareTo(b.getCompanyName()));


        for (DayCompanyQuote dcqCurrent : currentQuoteList) {
            if (dcqCurrent.getCourse() == -1.0f) {
                continue;
            }
            /**
            FilteredList<Evaluate> elList = evaluateDataList.filtered(evaluate -> evaluate.getCompanyName().equals(dcqCurrent.getCompanyName()));
            if (!elList.isEmpty()) {
                Evaluate evaluate = elList.get(0);

                if (evaluate.getRequiredPriceToBuy() != 0 && dcqCurrent.getCourse() <= evaluate.getRequiredPriceToBuy()) {
                    String msg = "BUY " + dcqCurrent.getCompanyName() + " current " + dcqCurrent.getCourse() + "  required " + evaluate.getRequiredPriceToBuyStr() + " bonus percent" +
                            " " + (evaluate.getRequiredPriceToBuy() / dcqCurrent.getCourse() * 100 - 100) + "\n";
                    System.out.println(msg);
                    current.getBuySbNew().append(msg);
                }
                if (evaluate.getRequiredPriceToSell() != 0 && dcqCurrent.getCourse() >= evaluate.getRequiredPriceToSell()) {
                    String msg = "SELL " + dcqCurrent.getCompanyName() + " current " + dcqCurrent.getCourse() + "  required " + evaluate.getRequiredPriceToSellStr() + " bonus percent" +
                            " " + (dcqCurrent.getCourse() / evaluate.getRequiredPriceToSell() * 100 - 100) + "\n";
                    System.out.println(msg);
                    current.getSellSbNew().append(msg);
                }

            }
             */
        }

        /**
        if (current.getBuySbLast().toString().equals(current.getBuySbNew().toString()) && current.getSellSbLast().toString().equals(current.getSellSbNew().toString())) {
            LOG.i("***List z komunikatami są tożsame ***");
            return null;
        } else if (current.getBuySbNew().toString().isEmpty() && current.getSellSbNew().toString().isEmpty()) {
            LOG.i("***Komunikaty puste ***");
            return null;
        }

        current.setBuySbLast(new StringBuilder());
        current.getBuySbLast().append(current.getBuySbNew().toString());

        current.setSellSbLast(new StringBuilder());
        current.getSellSbLast().append(current.getSellSbNew().toString());

           */
        LOG.i("Finished loading currentQuotesTask");

        return current;
    }



}
