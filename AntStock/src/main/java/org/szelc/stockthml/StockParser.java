package org.szelc.stockthml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StockParser {

    private static final String URL_DIVIDEND_BOSSA ="http://bossa.pl/analizy/CSP/dywidendy/";

    private static final String URL_MESSAGE_BANKIER = "http://www.bankier.pl/gielda/wiadomosci/komunikaty-spolek";

    private static String URL_MESSAGE_ONET = "http://biznes.onet.pl/gielda/komunikaty-espi-ebi/1,komunikaty-spolek.html";

    private static String URL_NEW_CONNECT_QUOTES_BANKIER = "http://www.bankier.pl/gielda/notowania/new-connect";

    private static String URL_GPW_QUOTES_BANKIER = "http://www.bankier.pl/gielda/notowania/akcje";


    private static final Logger log = Logger.getLogger(StockParser.class);

    public static List<DayCompanyQuote>displayQuotesGpwFromBankier(){
        return displayQuotesFromBankier(URL_GPW_QUOTES_BANKIER);
    }

    public List<DayCompanyQuote> displayQuotesNewConnectFromBankier(){
        return displayQuotesFromBankier(URL_NEW_CONNECT_QUOTES_BANKIER);
    }

    public static List<DayCompanyQuote> displayQuotesFromBankier(String address){
        List<DayCompanyQuote> result = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(address), 10000);
        } catch (IOException e) {
            log.error("Can't loading page ["+address+"]");
            return result;
        }
        log.info("Start parsing page ["+address+"]");
        Elements table = doc.getElementsByClass("sortTable floatingHeaderTable");
        Elements tbody = table.select("tbody");
        Elements rows = tbody.select("tr");

        for(int i=0; i<rows.size();i++){
            Element row = rows.get(i);
            Elements cols = row.select("td");

            if(cols.get(0).select("a").isEmpty()){
                continue;
            }

            String company = cols.get(0).select("a").get(0).html();
            String courseStr = cols.get(1).html();
            DayCompanyQuote dcq = new DayCompanyQuote();
            dcq.setCompanyName(company);
            Float course = -1f;
            try{
                course = Float.valueOf(courseStr.trim().replaceAll(" ","").replaceAll(",","."));
            }
            catch(NumberFormatException e){

            }
            dcq.setCourse(course);
            result.add(dcq);

            if(!cols.isEmpty()) {
               // log.info(company+" "+courseStr);//+" : "+cols.get(1).html()+" : "+cols.get(2).html()+" : "+cols.get(3).html()+" : "+cols.get(4).html()+" : "+cols.get(5).html());
            }
        }
        return result;
    }

    private String getOnetMessage(int page){
        return "http://biznes.onet.pl/gielda/komunikaty-espi-ebi/"+page+",komunikaty-spolek.html";
    }

    public List<String> displayMultiplePageMesssageOnet(int numberOfPages, boolean onlyToday, List companyFilters){
        List<String> result = new LinkedList<>();
        for(int i=1; i<=numberOfPages;i++){
            result.addAll(displayMessageOnet(i, onlyToday, companyFilters));
        }
        return result;
    }

    public List<String> displayMessageOnet(int numberPage, boolean onlyToday, List companyFilters){
        log.info("Parsing page ["+numberPage+"]");
        List<String> result = new ArrayList();
        Document doc = null;
        URL_MESSAGE_ONET = getOnetMessage(numberPage);
        try {
            doc = Jsoup.parse(new URL(URL_MESSAGE_ONET), 10000);
        } catch (IOException e) {
            log.error("Can't loading page ["+URL_MESSAGE_ONET+"]");
            return result;
        }
        //log.info("Start parsing page ["+URL_MESSAGE_ONET+"]\n");
        Elements table = doc.getElementsByClass("dataTable easyTable");
        Elements rows = table.select("tr");
        //log.info("Rows ["+rows.size()+"]");
        for(int i=0; i<rows.size();i++){
            Element row = rows.get(i);
            Elements cols = row.select("td");
            if(!cols.isEmpty()) {
                String company = cols.get(1).select("a").html();
                if(company.trim().isEmpty()){
                    company = cols.get(1).html();
                    if(company.trim().isEmpty()){
                        company = cols.get(2).select("a").html().split(":")[0];
                    }

                }

                if(!existCompany(companyFilters, company)){
                    continue;
                }
                String datetime = cols.get(0).select("span").html().replaceAll("&nbsp;", " ");
                if(onlyToday && !datetime.contains("dziś")){
                    return result;
                }
                //log.info(cols.get(0).html()+" : "+cols.get(1).html()+" : "+cols.get(2).html()+" : "+cols.get(3).html()+" : "+cols.get(4).html()+" : "+cols.get(5).html());
                log.info(datetime+" : "+company+" : "+cols.get(2).select("a").html());
                String href = cols.get(2).select("a").attr("href");
                log.info("http://biznes.onet.pl"+href+"\n");
                result.add("http://biznes.onet.pl"+href);
            }
        }

        return result;

    }

    private boolean existCompany(List<String> companyFilters, String company) {
        for(String filter: companyFilters){
            //log.info("Check filter ["+filter+"] company ["+company+"]");
            if(company.contains(filter)){
                return true;
            }
        }
        return false;
    }

    public void displayDividendBossa(){
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(URL_DIVIDEND_BOSSA), 10000);
        } catch (IOException e) {
            log.error("Can't loading page ["+URL_DIVIDEND_BOSSA+"]");
            return;
        }
        log.info("Start parsing page ["+URL_DIVIDEND_BOSSA+"]");
        Elements table = doc.getElementsByClass("cTdSc02");
        Elements rows = table.select("tr");

        for(int i=0; i<rows.size();i++){
            Element row = rows.get(i);
            Elements cols = row.select("td");
            if(!cols.isEmpty()) {
                log.info(cols.get(0).html()+" : "+cols.get(1).html()+" : "+cols.get(2).html()+" : "+cols.get(3).html()+" : "+cols.get(4).html()+" : "+cols.get(5).html());
            }
        }

    }
}
