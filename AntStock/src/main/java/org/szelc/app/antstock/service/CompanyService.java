package org.szelc.app.antstock.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.csv.model.CSVRecord;
import org.szelc.csv.model.CSVRecords;
import org.szelc.csv.reader.CSVReader;

/**
 *
 * @author szelc.org
 */
public class CompanyService {

    private static int COUNT = 0;
    private Logger log = Logger.getLogger(CompanyService.class.toString());

    private Set<String> companiesTransaction;
    private Set<String> companiesEvaluate;

    private Boolean test = false;

    private static List<String> companyObsoleteList = Arrays.asList(
            new String[]{
                "ZELMER", "CEDC", "EKO", "BOMI", "NOVAKBM", "BLACKLION", "NOKAUT", "YAWAL", //nie notowane
                "ARMATURA", "BAKALLAND", "MIESZKO", "JUPITER", "BIPROMET", "BNPPL", "TVN",
                "GLOBCITYHD", "IVMX", "BPHFIZSN", "DUON", "FENGHUA", "DTP", "MIDAS", "MAGELLAN",
                "NFIEMF", "ROVESE", "BANKBPH", "ZETKAMA", "BPHFIZDS", "INTSNKX07971", "DUDA", "INTSNAG11528",
                "PEKAES", "INTLPZU15610", "KOFOLA", "CAMMEDIA", "SOBIESKI", "INTSSLV16642", "TRIGONPP5",
                "INTSUSD16675", "INTSGLD16634", "POLNA",//nie notowane

                "ALMA", "MNI", "KERDOS", "IDEON", "MSXRESOUR", "MIRACULUM", "ONE2ONE", "SKOTAN", "GANT", "IDMSA", "COALENERG", "B3SYSTEM", "AGROTON", "INTERBUD", "FOTA", "RANKPROGR",
                "BIOMEDLUB", "CALATRAVA", "SADOVAYA", "PETROLINV", //bardzo słabe

                "WINVEST", "RCCRUAOPEN", "IQP", "ATLANTIS", "DRAGOWSKI", "EVEREST", "FON", "RUBICON", "PTI",//zla branza

                "IIAAV", "ARK12FRM", "DBDAIM1218", "RCSDAXOPEN", "4FUNMEDIA", "FON", "ELKOP", "RSCUGAOPEN", "REINHOLD", "TERMOREX", "IPERMFIZ", "RCSUGAOPEN", "ARK2FRN15",//niewiadomo

                "ORCOGROUP", "WIKANA", "DMWDM", "BBIDEV", "STARHEDGE",//nie interesuje mnie branza

                    "PLANETINN", //niskie obroty, niska cena, NC

                "IMCOMPANY", "KDMSHIPNG", "COALENERG",//ukraina
                "AVIAAML",//litwa
                "JJAUTO" //Dlugi brak raportow
            // IDEON //jesli sprzedam
            });

    public static boolean isObsolete(String company) {
        return companyObsoleteList.contains(company);
    }

    private void removeObsolete(Set<String> companySet) {
        companySet.removeAll(companyObsoleteList);
    }

    public Set<String> getCompanyTransactionedOrEvaluated(boolean removeObsolete) {
        Set<String> result = new HashSet();
        result.addAll(getCompanyEvaluated(removeObsolete));
        result.addAll(getCompanyTransactioned(removeObsolete));
        return result;
    }

    public Set<String> getCompanyTransactioned(boolean removeObsolete) {
        if (companiesTransaction == null) {
            companiesTransaction = getCompanySetFromFile(Settings.COMPANY_FILE_FROM_TRANSACTION);
        }
        removeObsolete(companiesTransaction);
        return companiesTransaction;
    }

    public Set<String> getCompanyEvaluated(boolean removeObsolete) {
        if (companiesEvaluate == null) {
            companiesEvaluate = getCompanySetFromFile(Settings.COMPANY_FILE_FROM_EVALUATE);
        }
        removeObsolete(companiesEvaluate);
        return companiesEvaluate;
    }

    public Set<String> getCompaniesCiagleAll(boolean removeObsolete) {
        if (test) {
            return getCompanyTransactionedOrEvaluated(removeObsolete);
        }
        Set<String> result = new HashSet();
        try {
            CSVRecords records = CSVReader.getRecordsFromFile(Settings.APP_HOME_PATH + "Storage/sesjacgl.prn", ",");
            int count = 0;
            for (CSVRecord record : records.getRecords()) {
                count++;
                String company = record.getFields().get(0);

                if (count > 500 && company.equals("INVESTORMS")) {
                    break;
                }
                //log.info("COMPANY ["+company+"]");
                if (!companyObsoleteList.contains(company)) {
                    result.add(company);
                }
            }
            log.info("********LICZBA SPOŁEK ********** " + result.size());
        } catch (FileNotFoundException ex) {
            log.error(ex);
            System.exit(0);

        }

        Set<String> observedSet = getCompanyTransactionedOrEvaluated(removeObsolete);
        for (String observed : observedSet) {
            if (!result.contains(observed)) {
                result.add(observed);
            }
        }
        return result;
    }

    public Set<String> getCompanySetFromFile(String fileName) {
        List<String> lines = null;
        File file = new File(fileName);
        boolean result = false;
        if (!file.exists()) {
            try {
                log.info("Trying create new file [" + fileName + "]");
                result = file.createNewFile();
            } catch (IOException e) {
                log.error(e);
                System.exit(0);
            }
            if (!result) {
                log.error("Can't create file [" + fileName + "]");
                System.exit(0);
            }

        }
        try {
            lines = Files.readAllLines(
                    Paths.get(fileName), Charset.defaultCharset());
        } catch (IOException ex) {
            log.error(ex);
            System.exit(0);
        }
        return new HashSet<>(lines);
    }

    public void saveCompanies(Set<String> companySet, String fileName) {

        File file = new File(fileName);
        try {
            PrintWriter pw = new PrintWriter(file);
            for (String company : companySet) {
                pw.write(company);
                pw.write("\r\n");
            }
            pw.flush();
            pw.close();
        } catch (FileNotFoundException ex) {

            log.error(ex);
            System.exit(0);
        }

    }

    public void saveCompaniesTransaction(String fileName) {

        File file = new File(fileName);
        try {
            PrintWriter pw = new PrintWriter(file);
            for (String company : getCompanyTransactioned(false)) {
                pw.write(company);
                pw.write("\n");
            }
            pw.flush();
            pw.close();
        } catch (FileNotFoundException ex) {

            log.error(ex);
            System.exit(0);
        }
    }

    public Set<String> getCompanyAllFromMstall() {
        File file = new File(Settings.QUOTE_FOLDER_FOR_UNPACK_ZIP);
        String[] fileNames = file.list();
        log.error("FILE SIZE [" + fileNames.length + "]");
        return new HashSet<>(Arrays.asList(fileNames));
    }

}
