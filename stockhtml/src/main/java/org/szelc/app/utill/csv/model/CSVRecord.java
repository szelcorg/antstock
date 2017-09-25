package org.szelc.app.utill.csv.model;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author marcin.szelc
 */

public final class CSVRecord
{
  private static final String defaultSeparator = ";";
  private List<String> fields = new ArrayList();
  
  public CSVRecord() {}
  
  public void addField(String field)
  {
    this.fields.add(field);
  }
  
  public void addField(Float field)
  {
    this.fields.add(field.toString());
  }
  
  public void addField(Integer field)
  {
    this.fields.add(field.toString());
  }
  
  public CSVRecord(String line, String separator)
  {
    setFields(line.split(separator));
//      System.out.println("NumFields "+ getFields().size());
  }
  
  public void setFields(String[] f)
  {
    this.fields = Arrays.asList(f);
  }
  
  public List<String> getFields()
  {
    return this.fields;
  }
  
  public String getField(int num){
      if(getFields()==null || getFields().size()<(num+1)){
          return null;
      }
      return getFields().get(num);
  }
  
  public Float getFAF(int num){
      
      String v = getField(num);      
      
      try {
          float result = (v==null || v.trim().isEmpty()) ? null : NumberFormat.getInstance(Locale.getDefault()).parse(v).floatValue();
         // Log.p("num "+num+" V "+ v+ " RESULT "+result);
          return result;
          
          
      } catch (ParseException ex) {
          Logger.getLogger(CSVRecord.class.getName()).log(Level.SEVERE, null, ex);
      }
      catch (NullPointerException e){
          return 0f;
      }
      return 0f;
  }

  
    public Float getFieldAsFloat(int num) {
        String v = getField(num);

        try {
            return (v == null || v.trim().isEmpty()) ? 0f : NumberFormat.getInstance(Locale.getDefault()).parse(v).floatValue();
        } catch (ParseException ex) {
            //Logger.getLogger(CSVRecord.class.getName()).log(Level.SEVERE, null, ex);
            return 0f;
        } 
     
    }
    
    
       public Double getFieldAsDouble(int num) {
        String v = getField(num);

        try {
            return (v == null || v.trim().isEmpty()) ? 0f : NumberFormat.getInstance(Locale.getDefault()).parse(v).doubleValue();
        } catch (ParseException ex) {
            //Logger.getLogger(CSVRecord.class.getName()).log(Level.SEVERE, null, ex);
            return 0d;
        } 
     
    }
  
  public void display()
  {
    if (this.fields.isEmpty()) {
      System.out.print("[RECORD IS EMPTY])");
    }
    for (String f : this.fields) {
      System.out.print(f + ";");
    }
  }
  
  public String toString()
  {
    if (this.fields.isEmpty()) {
      return "[RECORD IS EMPTY])";
    }
    StringBuilder result = new StringBuilder(this.fields.size());
    for (String f : this.fields)
    {
      result.append(f);
      result.append(";");
    }
    return result.toString();
  }

}
