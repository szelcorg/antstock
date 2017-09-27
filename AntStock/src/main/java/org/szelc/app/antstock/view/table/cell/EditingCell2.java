

/**
 *
 * @author szelc.org
 */
package org.szelc.app.antstock.view.table.cell;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.szelc.app.antstock.data.Transaction;

/**
 *
 * @author szelc.org
 */
public  class EditingCell2 extends TableCell<Transaction, String> {
 
        private TextField textField;
 
        public EditingCell2() {
        }
 
        @Override
        public void startEdit() {
            System.out.println("START EDIT");
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            System.out.println("CANCEL EDIT");
            super.cancelEdit();
 
            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            System.out.println("UPDATE ITEM");
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
            cancelEdit();
        }
 
       private void createTextField() {
    textField = new TextField(getString());
    textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
    textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent t) {
            if (t.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
                
                EditingCell2.this.getTableView().requestFocus();//why does it lose focus??
                EditingCell2.this.getTableView().getSelectionModel().selectBelowCell();
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
            
        }
    });

    textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent t) {
            if (t.getCode().isDigitKey()) {
//                if (CellField.isLessOrEqualOneSym()) {
//                    CellField.addSymbol(t.getText());
//                } else {
//                    CellField.setText(textField.getText());
//                }
                textField.setText(textField.getText());
                textField.deselect();
                textField.end();
                textField.positionCaret(textField.getLength() + 2);//works sometimes

            }
        }
    });
}
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
   
}