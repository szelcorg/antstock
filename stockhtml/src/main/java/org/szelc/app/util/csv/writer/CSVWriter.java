package org.szelc.app.util.csv.writer;

/**
 *
 * @author marcin.szelc
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.szelc.app.utill.csv.model.CSVRecord;
import org.szelc.app.utill.csv.model.CSVRecords;


public class CSVWriter
{
  private static final String PROP_NAME_LINE_SEPARATOR = "line.separator";
  private static final String defaultEncoding = "UTF-8";
  
  public void save(String filePath, CSVRecords records, boolean createIfNotExist)
    throws FileNotFoundException, UnsupportedEncodingException, IOException
  {
    if (records.getRecords().isEmpty())
    {
      System.out.println("No records to save in csv file"); return;
    }
    boolean createNewFile;
    if (createIfNotExist)
    {
      File f = new File(filePath);
      if (!f.exists()) {
        createNewFile = f.createNewFile();
      }
    }
    PrintWriter writer = new PrintWriter(filePath, "UTF-8");
    for (CSVRecord rec : records.getRecords())
    {
      writer.write(rec.toString());
      writer.write(System.getProperty("line.separator"));
    }
    writer.flush();
    writer.close();
  }
}
