package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class OdemebasariliController {

    @FXML
    private Button reservationsButton; // FXML dosyasındaki ilgili butonun fx:id'si ile eşleşmeli

    @FXML
    private Button gotoLoginButton;// FXML dosyasındaki ilgili butonun fx:id'si ile eşleşmeli
    @FXML
    private Button backButton7;

    @FXML
    private void initialize() {
        // "REZERVASYONLARI LİSTELE" butonuna tıklanınca yapılacak işlem
        reservationsButton.setOnAction(event -> {
            // İlgili FXML dosyasını yükleyerek yeni bir sahne oluştur
            loadScene("rezervasyonlar.fxml");
        });

        // "GİRİŞ EKRANINA DÖN" butonuna tıklanınca yapılacak işlem
        gotoLoginButton.setOnAction(event -> {
            // İlgili FXML dosyasını yükleyerek yeni bir sahne oluştur
            loadScene("login.fxml");

        });
        backButton7.setOnAction(event -> {
            // İlgili FXML dosyasını yükleyerek yeni bir sahne oluştur
            loadScene("odeme.fxml");
        });
    }

    private void loadScene(String fxmlFileName) {
        try {
            // İlgili FXML dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            // Yeni bir sahne oluştur
            Scene scene = new Scene(root);

            // Mevcut pencereyi al
            Stage stage = (Stage) reservationsButton.getScene().getWindow();

            // Yeni sahneyi mevcut pencereye ayarla
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
