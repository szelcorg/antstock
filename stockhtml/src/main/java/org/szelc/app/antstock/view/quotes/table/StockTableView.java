package org.szelc.app.antstock.view.quotes.table;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 *
 * @author szelc.org
 */
public class StockTableView extends AnchorPane {

    protected void defineFieldUpEqColor(TableColumn tableColumn) {
        tableColumn.setCellFactory(column -> {
            return new TableCell<Object, String>() {
                @Override
                public void updateSelected(boolean arg0) {
                    super.updateSelected(arg0);
                    if (isSelected()) {
                        this.setTextFill(Color.AQUA);
                    }
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                    if (item == null || empty ) {
                    } else {
                        Float value = 0f;
                        try{
                            value = Float.valueOf(item.replace(" ", "").replace(",", "."));
                            setTextFill(Color.GREEN);
                            getStyleClass().clear();
                            if (value > 0 || value == 0) {
                                this.getStyleClass().add("changeUp");
                            } else {
                                setTextFill(Color.BLACK);
                                this.getStyleClass().add("changeNone");
                            }
                        }
                        catch(Exception e){
                            
                        }
                         
                    }
                }
            };
        });
    }

    protected void defineFieldDownEqColor(TableColumn tableColumn) {
        tableColumn.setCellFactory(column -> {
            return new TableCell<Object, String>() {
                @Override
                public void updateSelected(boolean arg0) {
                    super.updateSelected(arg0);
                    if (isSelected()) {
                        this.setTextFill(Color.AQUA);
                    }
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                    if (item == null || empty) {
                    } else {
                        Float value = Float.valueOf(item.replace(" ", "").replace(" ", "").replace(",", "."));
                        getStyleClass().clear();
                        if (value < 0 || value == 0) {
                            setTextFill(Color.GREEN);
                            this.getStyleClass().add("changeDown");
                         } else {
                            setTextFill(Color.BLACK);
                            this.getStyleClass().add("changeNone");
                        }
                    }
                }
            };
        });
    }

    protected void defineFieldUpDownEqColor(TableColumn tableColumn) {

        tableColumn.setCellFactory(column -> {
            return new TableCell<Object, String>() {
                @Override
                public void updateSelected(boolean arg0) {
                    super.updateSelected(arg0);
                    if (isSelected()) {
                        this.setTextFill(Color.AQUA);
                    }
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                    if (item == null || empty) {
                    } else {
                        Float value;
                        try {
                            value = Float.valueOf(item.replace(" ", "").replace(",", "").replace(" ", ""));
                        }
                        catch(NumberFormatException e){
                            System.out.println("************** NumberFormatException ["+item+"] ");
                            System.exit(0);
                            return;
                        }
                        setTextFill(Color.GREEN);
                        getStyleClass().clear();
                        if (value > 0) {
                            getStyleClass().remove("changeDown");
                            getStyleClass().remove("changeEq");
                            getStyleClass().add("changeUp");
                        } else if (value < 0) {
                             getStyleClass().remove("changeUp");
                             getStyleClass().remove("changeEq");
                            getStyleClass().add("changeDown");
                        } else {
                            getStyleClass().remove("changeUp");
                            getStyleClass().remove("changeDown");
                            getStyleClass().add("changeEq");
                        }
                    }
                }
            };
        });
    }
}
