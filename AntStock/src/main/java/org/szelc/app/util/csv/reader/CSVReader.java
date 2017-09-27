package org.szelc.app.util.csv.reader;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import org.szelc.app.utill.csv.model.CSVRecord;
import org.szelc.app.utill.csv.model.CSVRecords;


/**
 *
 * @author marcin.szelc
 */
public class CSVReader
{
  public static CSVRecords getRecordsFromFile(String path, String separator)
    throws FileNotFoundException
  {
    CSVRecords result = new CSVRecords(separator);
    
    Scanner scanner = new Scanner(new BufferedReader(new FileReader(path)));
    while (scanner.hasNext())
    {
      String line = scanner.nextLine();
        //System.out.println("LINE ["+line+"]");
      CSVRecord record = new CSVRecord(line, separator);
      result.addRecord(record);
    }
    return result;
  }
}