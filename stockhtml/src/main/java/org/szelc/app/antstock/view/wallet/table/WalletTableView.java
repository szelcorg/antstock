
package org.szelc.app.antstock.view.wallet.table;

import java.io.IOException;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.szelc.app.antstock.comparator.FloatFormatComparator;
import org.szelc.app.antstock.comparator.IntegerFormatComparator;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.statistic.CompanyTransactionStatistic;

/**
 * FXML Controller class
 *
 * @author mszelc
 */
public class WalletTableView extends AnchorPane {

    private static final String FXML_FILE_COMPONENT = "WalletTableView.fxml";
    
    @FXML
    private TableView<CompanyTransactionStatistic> table;
    
     private static final int INDEX_FIELD_COMPANY = 1;
     private static final int INDEX_FIELD_NUMBER_SHARES = 2;
     private static final int INDEX_FIELD_NUMBER_AVG_PRICE_FOR_SHARE = 3;
     private static final int INDEX_FIELD_NUMBER_CURRENT_PRICE = 4;
     private static final int INDEX_FIELD_PRICE_FOR_ACTIVE_SHARE = 5;
     private static final int INDEX_FIELD_TOTAL_ACTUAL_PRICE = 6; 
     

    public WalletTableView() {
        loadComponent();
        
        table.getColumns().get(INDEX_FIELD_NUMBER_SHARES).setComparator(new IntegerFormatComparator());
        table.getColumns().get(INDEX_FIELD_NUMBER_AVG_PRICE_FOR_SHARE).setComparator(new FloatFormatComparator());
        table.getColumns().get(INDEX_FIELD_NUMBER_CURRENT_PRICE).setComparator(new FloatFormatComparator());
        table.getColumns().get(INDEX_FIELD_PRICE_FOR_ACTIVE_SHARE).setComparator(new FloatFormatComparator());
        table.getColumns().get(INDEX_FIELD_TOTAL_ACTUAL_PRICE).setComparator(new FloatFormatComparator());
        table.getColumns().get(7).setComparator(new FloatFormatComparator());
        table.getColumns().get(8).setComparator(new FloatFormatComparator());
        table.getColumns().get(9).setComparator(new IntegerFormatComparator());
        table.getColumns().get(10).setComparator(new FloatFormatComparator());
        table.getColumns().get(11).setComparator(new FloatFormatComparator());
        table.getColumns().get(12).setComparator(new FloatFormatComparator());
        table.getColumns().get(13).setComparator(new FloatFormatComparator());
        table.getColumns().get(14).setComparator(new FloatFormatComparator());
        table.getColumns().get(15).setComparator(new FloatFormatComparator());
        table.getColumns().get(16).setComparator(new FloatFormatComparator());
    }
    /**
     * Initializes the controller class.
     * 
     */
    
   private void loadComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(FXML_FILE_COMPONENT));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
        defineFieldColor();
    }
   
     public void setItems(ObservableList<CompanyTransactionStatistic> list) {
        float totalMoneyActiveShares = 0f;        
        for(CompanyTransactionStatistic cts: list){
            totalMoneyActiveShares += cts.getTotalActualPrice();
        }
        CompanyTransactionStatistic.totalCourseActiveShares = totalMoneyActiveShares;
        System.out.println("TOTAL MONEY A ["+totalMoneyActiveShares+"]");
        table.setItems(list);
        table.getColumns().get(INDEX_FIELD_TOTAL_ACTUAL_PRICE).setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(table.getColumns().get(INDEX_FIELD_TOTAL_ACTUAL_PRICE));
        table.sort();
        int i=0;
        
        Iterator<CompanyTransactionStatistic> it = table.getItems().iterator();
        while(it.hasNext()){
            CompanyTransactionStatistic cts = it.next();
            cts.setOrder(++i);            
        }
        
        
    }
     
      private void defineFieldColor() {
        
        TableColumn tableColumn = table.getColumns().get(7);

        tableColumn.setCellFactory(column -> {
            return new TableCell<DayCompanyQuote, String>() {

                @Override
                public void updateSelected(boolean arg0){
                    
                    super.updateSelected(arg0);
                    if(isSelected()){
                        this.setTextFill(Color.AQUA);
                    }
                }
                
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                     setText(item);
//                     log.info("UPDATE ITEM");
                    if (item == null || empty) {
//                        setText(null);
//                        setStyle("");
                    } else {
                        Float value = Float.valueOf(item.replace(" ", "").replace(",", ".").replace(" ", ""));
                        
                        setTextFill(Color.GREEN);
                        
                        if (value>0) {
                            getStyleClass().clear();
                            this.getStyleClass().add("changeUp");
                        } else if (value < 0){
                            getStyleClass().clear();
                            this.getStyleClass().add("changeDown");
                        }
                        else {
                            getStyleClass().clear();
                            this.getStyleClass().add("changeEq");
                        }
                        
                    }
                }
                
            };
        });
    }
    
}
