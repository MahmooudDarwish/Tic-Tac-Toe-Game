package screens.playing_screen;

import components.XOButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import models.OfflinePlayer;
import models.OfflinePlayerHolder;
import utils.constants.AppConstants;

/**
 * FXML Controller class
 *
 * @author Smart
 */
public class PlayingScreenController implements Initializable {

    private boolean flag;

    private OfflinePlayer xPlayer;
    private OfflinePlayer oPlayer;
    @FXML
    private AnchorPane AnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        OfflinePlayerHolder offlinePlayerHolder = OfflinePlayerHolder.getInstance();

        flag = true;
        xPlayer = offlinePlayerHolder.getXPlayer();
        oPlayer = offlinePlayerHolder.getOPlayer();

        GridPane gp = new GridPane();

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                ImageView x = new ImageView(AppConstants.xIconPath);
                x.setFitHeight(50);
                x.setFitWidth(50);
                ImageView o = new ImageView(AppConstants.oIconPath);
                o.setFitHeight(50);
                o.setFitWidth(50);
                Button b = new Button();
                b.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                b.setPrefSize(100, 100);
                b.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                b.setContentDisplay(ContentDisplay.CENTER);
                gp.add(b, j, i);

                b.setOnAction(e -> {
                    if (flag) {
                        flag = false;
                        b.setGraphic(x);
                    } else {
                        flag = true;
                        b.setGraphic(o);
                    }
                });
            }
        }
        gp.setLayoutX(20);
        gp.setLayoutY(60);
        XOButton record = new XOButton("Record", () -> {
        }, AppConstants.xIconPath, 100, 40, AppConstants.buttonClickedTonePath);
        record.setLayoutX(20);
        record.setLayoutY(10);
        XOButton resign = new XOButton("Resign", () -> {
        }, AppConstants.oIconPath, 100, 40, AppConstants.buttonClickedTonePath);
        resign.setLayoutX(170);
        resign.setLayoutY(10);

        Text xPlayerText = new Text("X Player: " + xPlayer.getName() + "\n his Score:");
        xPlayerText.setLayoutX(350);
        xPlayerText.setLayoutY(100);
        xPlayerText.setFont(Font.font("", FontWeight.BOLD, 16));
        Text oPlayerText;
        if (oPlayer != null) {
            oPlayerText = new Text("O Player: " + oPlayer.getName() + "\n his Score:");
        } else {
            oPlayerText = new Text("O Player: AI \n his Score:");
        }
        oPlayerText.setLayoutX(350);
        oPlayerText.setLayoutY(200);
        oPlayerText.setFont(Font.font("", FontWeight.BOLD, 16));
        AnchorPane.getChildren().addAll(gp, record, resign, xPlayerText, oPlayerText);
    }
}
