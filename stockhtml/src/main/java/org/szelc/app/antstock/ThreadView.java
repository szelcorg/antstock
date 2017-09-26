package org.szelc.app.antstock;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

public class ThreadView implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTask();
    }

    private void startTask(){
        Task task = new Task(){

            @Override
            protected Object call() throws Exception {
                int iterations = 0;
                while(true) {
                    iterations++;
                    if (isCancelled()) {
                        System.out.println("Break");
                        break;
                    }

                    System.out.println("Iteration " + iterations);
                    Platform.runLater(() -> {
                        /**
                        Stage dialog = new Stage();
                        dialog.initStyle(StageStyle.UTILITY);
                        Scene scene = new Scene(new Group(new Text(25, 25, "All is done!")));
                        dialog.setScene(scene);
                        dialog.showAndWait();
                        */
                        showAlert();
                    });

                    Thread.sleep(1000);
                }
                return iterations;
            }
        };

        Thread th = new Thread(task);

        th.setDaemon(true);

        th.start();

        //ExecutorService.submit(task);

    }
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private void showAlert(){

        //if(alert.isShowing()){
            alert.close();

        //}
        System.out.println("ShowAndWait");

        alert.setTitle("Buy/Sell");
        alert.setHeaderText("Communicate");
        alert.setContentText("as");
        alert.showAndWait();
    }
}
