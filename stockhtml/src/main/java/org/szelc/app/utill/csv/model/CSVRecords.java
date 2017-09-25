package org.szelc.app.utill.csv.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcin.szelc
 */
public class CSVRecords
{
  private static final String NEW_LINE = "\n";
  private final List<CSVRecord> records = new ArrayList();
  private String separator;
  
  public CSVRecords(String separator)
  {
    this.separator = separator;
  }
  
  public List<CSVRecord> getRecordsRevers()
  {
    List<CSVRecord> result = new ArrayList();
    if (this.records.isEmpty()) {
      return this.records;
    }
    for (CSVRecord rec : this.records) {
      result.add(0, rec);
    }
    return result;
  }
  
  public void addRecord(CSVRecord record)
  {
    this.records.add(record);
  }
  
  public List<CSVRecord> getRecords()
  {
    return this.records;
  }
  
  public String toString()
  {
    if (this.records.isEmpty()) {
      return "CSVRecords is empty";
    }
    StringBuilder result = new StringBuilder(this.records.size());
    int id = 0;
    for (CSVRecord rec : this.records)
    {
      result.append(rec);
      result.append("\n");
    }
    return result.toString();
  }
  
  public void reverse()
  {
    List<CSVRecord> l = getRecordsRevers();
    this.records.clear();
    this.records.addAll(l);
  }
}
