package org.szelc.stockthml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.szelc.app.antstock.data.messages.CompanyMessagesList;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.financial.report.*;
import org.szelc.logger.LOG;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class StockParser {

    private static final String URL_DIVIDEND_BOSSA ="http://bossa.pl/analizy/CSP/dywidendy/";

    private static final String URL_MESSAGE_BANKIER = "http://www.bankier.pl/gielda/wiadomosci/komunikaty-spolek";

    private static String URL_MESSAGE_ONET = "http://biznes.onet.pl/gielda/komunikaty-espi-ebi/1,komunikaty-spolek.html";

    private static String URL_NEW_CONNECT_QUOTES_BANKIER = "http://www.bankier.pl/gielda/notowania/new-connect";

    private static String URL_GPW_QUOTES_BANKIER = "http://www.bankier.pl/gielda/notowania/akcje";

    private static String URL_GPW_COMPANY_CODE = "https://www.gpw.pl/wskazniki";


    private static final Logger log = Logger.getLogger(StockParser.class);

    public static List<DayCompanyQuote>displayQuotesGpwFromBankier(){
        return displayQuotesFromBankier(URL_GPW_QUOTES_BANKIER);
    }

    public List<DayCompanyQuote> displayQuotesNewConnectFromBankier(){
        return displayQuotesFromBankier(URL_NEW_CONNECT_QUOTES_BANKIER);
    }


    public static Map<String, String> getCompanyCodesMap(){
        LOG.i("Starting getCompanyCodesMap");
        Map<String, String> result = new HashMap<>();
        result.put("PLHOOP000010", "HOOP");
        result.put("PLPEKAS00017", "PEKAES");
        result.put("UNICREDIT", "UNICREDIT");
        result.put("Fortuna", "FORTUNA");
        result.put("CEZ", "CEZ");
     /*   result.put("LU0327357389", "KERNEL");*/
        result.put("KERNEL", "KERNEL");
        String address = URL_GPW_COMPANY_CODE;
        String html_id = "footable_K";
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(address), 10000);
            //LOG.i("Loaded document "+doc.text());
        } catch (IOException e) {
            log.error("Can't loading page ["+address+"]");
            return null;
        }

        Element table = doc.getElementById(html_id);
        //LOG.i("Table "+table.toString());
        Elements tbody = table.select("tbody");
        Elements rows = tbody.select("tr");
        LOG.i("rows size"+rows.size());
        for(int i=0; i<rows.size();i++){
            Element row = rows.get(i);
            Elements cols = row.select("td");
            String code = cols.get(1).html();
            String name = cols.get(2).html();
            LOG.i(""+code+" "+name);
            result.put(code, name);

        }
        return result;
    }



    private static List<Report> loadReportFromBankier(String type, List<String> companyList, int countPage){
        int totalToProcessing = companyList.size() * countPage;
        List<Report> result = new ArrayList<>();
        int numberProcessed = 0;
        int percentLast = 0;
        for(String company: companyList){
            for(int page=1; page<countPage+1; page++) {
                List<Report> reportList = loadReportFromBankier(type, company, ""+page);
                numberProcessed++;
                int newPercent = (100*numberProcessed/totalToProcessing);
                if(newPercent>percentLast) {
                    log.info("PERCENT [" + newPercent + "]");
                    percentLast = newPercent;
                }
                if(reportList!=null) {
                    result.addAll(reportList);
                }
            }
        }
        return result;
    }


    public static List<Report> loadQuartalReportFromBankier(List<String> companyList, int countPage){
        return loadReportFromBankier("kwartalny", companyList, countPage);
    }

    public static  List<Report> loadHalfReportFromBankier(List<String> companyList,int countPage){
        return loadReportFromBankier("polroczny", companyList, countPage);
    }

    public static  List<Report> loadYearReportFromBankier(List<String> companyList, int countPage){
        return loadReportFromBankier("roczny", companyList, countPage);
    }

    public static List<Report> loadReportFromBankier(String type, String company, String page){
        String address = "https://www.bankier.pl/gielda/notowania/akcje/" + company + "/wyniki-finansowe/skonsolidowany/"+type+"/standardowy/"+page;
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(address), 10000);
        } catch (IOException e) {
            log.error("Can't loading page ["+address+"]");
            e.printStackTrace();
           return null;
        }

        log.debug("Start parsing page ["+address+"]");
        Elements div = doc.getElementsByClass("boxContent boxTable");
        Elements table = div.select("table");
        Elements rows = table.select("tr");

        if(rows==null){
            return null;
        }

        Element headers;

        try {
            headers = rows.get(0);
        }
        catch(Exception e){
            return null;
        }
        List<Report> reportList = new ArrayList<>();
        for(int i=0;i<4;i++){
            String header;

            try {
                header = headers.select("strong").get(i).html();
            }
            catch(Exception e){
                return reportList;
            }
            if(header==null){
                return reportList;
            }
            reportList.add(new Report(new Company(company), ReportType.fromStr(type), header));
        }

        for(int i=1; i<rows.size();i++){
            Element row = rows.get(i);
            String fieldName = row.getElementsByClass("charts").html();
            int index = fieldName.indexOf("<");
            if(index==-1){
                return reportList;
            }
            fieldName = fieldName.substring(0, index);
            log.debug("[d:"+i+ "] ["+fieldName+"]");

           for(int col=1; col<5; col++){
               String value = row.select("td").get(col).html().replaceAll("&nbsp;", "").replaceAll(",",".");
               ReportData reportData = new ReportData(new ReportField(i, fieldName), value==null || value.isEmpty() ? 0 : Float.valueOf(value));
               reportList.get(col-1).addReportData(reportData);
           }
        }

        return reportList;
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

    public CompanyMessagesList displayMultiplePageMesssageOnet(int numberOfPages, boolean onlyToday, List companyFilters){
        CompanyMessagesList result = new CompanyMessagesList();
        for(int i=1; i<=numberOfPages;i++){
            result.addAll(displayMessageOnet(i, onlyToday, companyFilters).getMessageList());
        }
        return result;
    }

    public CompanyMessagesList displayMessageOnet(int numberPage, boolean onlyToday, List companyFilters){
        log.info("Parsing page ["+numberPage+"]");
        CompanyMessagesList result = new CompanyMessagesList();
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
                result.addMessage(company, datetime,"http://biznes.onet.pl"+href);
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
