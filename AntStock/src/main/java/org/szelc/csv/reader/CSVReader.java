package org.szelc.csv.reader;


import org.szelc.csv.model.CSVRecord;
import org.szelc.csv.model.CSVRecords;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;



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