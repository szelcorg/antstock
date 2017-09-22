package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class Main extends Application {
    BigDecimal bg1 = new BigDecimal(2.24);
    BigDecimal bg2 = new BigDecimal(3.31);

    Logger log = Logger.getLogger(Main.class.toString());
    public Main() {
        log.info("Test message");
        log.warning("warn");

        List<String> l = List.of("Jan", "Adma", "Kami");
        Map<String, String> m = Map.of("k1", "v1", "k2", "v2");

        for(String i : l){
            log.info(i);
        }

        bg1.setScale(5, RoundingMode.HALF_UP);
        bg2.setScale(5, RoundingMode.HALF_UP);
        BigDecimal bd3 = new BigDecimal(0);
        bd3.setScale(5, RoundingMode.HALF_UP);
        bd3 = bg1.add(bg2);
        bd3.setScale(5, RoundingMode.HALF_UP);
        log.info("SUM "+bd3.setScale(2, RoundingMode.HALF_UP).doubleValue());

        DecimalFormat df = new DecimalFormat();
        df.setGroupingUsed(true);
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        Integer perc = 5;
        BigDecimal spread = new BigDecimal(2.24232223259);
        BigDecimal perc2 = spread.setScale(perc,RoundingMode.HALF_UP);

        BigInteger bi = new BigInteger("123456789");
        log.info("BI "+df.format(bi.doubleValue()));

        System.out.println(perc2);
        log.info("PERC "+perc2);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

/**
    public static void main(String[] args) {
        new Main();
        //launch(args);
    }
 */
}
