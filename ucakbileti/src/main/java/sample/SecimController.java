package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class SecimController {

    @FXML
    private ComboBox<String> neredenComboBox;

    @FXML
    private ComboBox<String> nereyeComboBox;

    @FXML
    private DatePicker gidisDatePicker;

    @FXML
    private Button biletbulButton;
    @FXML
    private Button backButton2;

    @FXML
    public void initialize() {
        // ComboBox'ları veritabanından gelen verilerle doldur
        populateComboBox(neredenComboBox, "Airports", "airport_name");
        populateComboBox(nereyeComboBox, "Airports", "airport_name");

        // Diğer kontrolleri başlangıç değerleriyle veya gerekli ayarlamalarla doldur veya yapılandır...
    }

    private void populateComboBox(ComboBox<String> comboBox, String tableName, String columnName) {
        DatabaseConnection connectNow = new DatabaseConnection();
        try (Connection connectDB = connectNow.getConnection()) {
            String query = "SELECT " + columnName + " FROM " + tableName;

            try (PreparedStatement preparedStatement = connectDB.prepareStatement(query)) {
                preparedStatement.execute();
                while (preparedStatement.getResultSet().next()) {
                    comboBox.getItems().add(preparedStatement.getResultSet().getString(columnName));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @FXML
    private void handleBiletBulButton() {
        // Bilet Bul butonuna basıldığında yapılacak işlemler
        if (checkDepartureDate()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ucuslar.fxml"));
                Parent root = loader.load();

                UcuslarController ucuslarController = loader.getController();
                ucuslarController.setFiltreler(neredenComboBox.getValue(), nereyeComboBox.getValue(),
                        gidisDatePicker.getValue(), null, true);

                // Yeni bir sahne oluşturun
                Scene scene = new Scene(root);

                // Mevcut pencereyi alın
                Stage stage = (Stage) biletbulButton.getScene().getWindow();

                // Yeni sahneyi mevcut pencereye ayarlayın
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDepartureDate() {
        // Tarih kontrolü burada gerçekleştirilecek
        LocalDate selectedDate = gidisDatePicker.getValue();
        LocalDate currentDate = LocalDate.now();

        if (selectedDate.isBefore(currentDate)) {
            showAlert("Geçmiş bir tarih seçilemez.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void initialize1() {

        // "GİRİŞ EKRANINA DÖN" butonuna tıklanınca yapılacak işlem
        backButton2.setOnAction(event -> {
            // İlgili FXML dosyasını yükleyerek yeni bir sahne oluştur
            loadScene("login.fxml");
        });
    }
    @FXML
    private void loadScene(String fxmlFileName) {
        try {
            // İlgili FXML dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            // Yeni bir sahne oluştur
            Scene scene = new Scene(root);

            // Mevcut pencereyi al
            Stage stage = (Stage) backButton2.getScene().getWindow();

            // Yeni sahneyi mevcut pencereye ayarla
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}