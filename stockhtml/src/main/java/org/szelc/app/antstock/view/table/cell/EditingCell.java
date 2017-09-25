package org.szelc.app.antstock.view.table.cell;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.szelc.app.antstock.view.table.TableInterface;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.log4j.Logger;

/**
 *
 * @author szelc.org
 */
public abstract class EditingCell<T1, T2> extends TableCell<T1, T2> {
    private Logger log = Logger.getLogger(EditingCell.class.toString());
    protected TextField textField;
    protected TableColumn column;
    protected TableInterface tableInt;
    protected List<String> autocompleteList;
    
    public EditingCell(TableColumn column, TableInterface tableInt) {
        this.column = column;
        this.tableInt = tableInt;              
    }

    public void setAutocompleteList(List<String> autocompleteList) {
        this.autocompleteList = autocompleteList;
    }
    
    @Override
    public void startEdit() {
        
        log.info("Start edit");
        if (!isEmpty()) {
            log.info("Notempty");
            super.startEdit();
            log.error("Selected item "+super.getTableRow().getIndex()+"");
            createTextField();
            setText(null);
            setGraphic(textField);
           textField.requestFocus();
        }
        
        
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                if (!arg2) {
                    //commitEdit is replaced with own callback
                    //commitEdit(getTextArea().getText());

                    //Update item now since otherwise, it won't get refreshed
//                    setItem(textField.getText());
                    //Example, provide TableRow and index to get Object of TableView in callback implementation
                    //commitChange.call(new TableCellChangeInfo(getTableRow(), getTableRow().getIndex(), getTextArea().getText()));
                    cancelEdit();
                }
            }
        });
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        log.info("CancelEdit");
        setText(getItem().toString());
        setGraphic(null);
    }

 
    @Override
    public void updateItem(T2 item, boolean empty) {
        super.updateItem(item, empty);        
        
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getItem().toString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
       // cancelEdit();
    }
    
    protected String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
    

    protected synchronized void createTextField() {
        log.info("CreateTextField");
        textField = new TextField(getString());
//        textField = TextFields.createClearableTextField();
//        textField.setText(getString());
//        if(autocompleteList!=null && !autocompleteList.isEmpty()){           
//            TextFields.bindAutoCompletion(textField, autocompleteList);
//        }
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
           
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().isDigitKey()) {
                    textField.deselect();
                    textField.end();
                    textField.positionCaret(textField.getLength() + 2);//works sometimes

                }
            }
        });
        
         textField.setOnKeyPressed((KeyEvent t) -> {
              log.info("KEY RELEASE");
            if (t.getCode() == KeyCode.ENTER) {      
                acceptEdit();
                tableInt.update();
                EditingCell.this.getTableView().requestFocus();
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }
    
    protected abstract boolean acceptEdit();
    
   
}
