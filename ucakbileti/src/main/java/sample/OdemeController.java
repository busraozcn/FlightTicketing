package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OdemeController {

    @FXML
    private TextField card_numberTextBox;

    @FXML
    private ComboBox<String> expiration_monthComboBox;

    @FXML
    private ComboBox<String> expiration_yearComboBox;

    @FXML
    private TextField cvvTextBox;

    @FXML
    private Button payButton;
    @FXML
    private Button backButton5;

    @FXML
    private void initialize() {
        // ComboBox'ları doldur
        expiration_monthComboBox.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        expiration_yearComboBox.getItems().addAll("2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030");
    }

    @FXML
    private void handlePayButton() {
        String cardNumber = card_numberTextBox.getText();
        String expirationMonth = expiration_monthComboBox.getValue();
        String  expirationYear = expiration_yearComboBox.getValue();
        String cvv = cvvTextBox.getText();

        // Kart bilgilerini kontrol et ve ödeme yap
        if (performPayment(cardNumber, expirationMonth, expirationYear, cvv)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("odemebasarili.fxml"));
                Parent root = loader.load();

                // OdemeController'ı al
                OdemebasariliController OdemebasariliController = loader.getController();

                // Yeni bir sahne oluşturun
                Scene scene = new Scene(root);

                // Mevcut pencereyi alın
                Stage stage = (Stage) payButton.getScene().getWindow();

                // Yeni sahneyi mevcut pencereye ayarlayın
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Ödeme başarısız. Lütfen kart bilgilerinizi kontrol edin.");
        }
    }

    private boolean performPayment(String cardNumber, String expirationMonth, String expirationYear, String cvv) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT COUNT(1) FROM creditcards " +
                             "WHERE card_number = ? AND expiration_month = CAST(? AS VARCHAR) AND expiration_year = CAST(? AS VARCHAR) AND cvv = ?");
        ) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, expirationMonth);
            preparedStatement.setString(3, expirationYear);
            preparedStatement.setString(4, cvv);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    // Kart bilgileri doğru, ödeme başarılı
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Kart bilgileri doğru değil, ödeme başarısız
        return false;
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void initialize1() {

        backButton5.setOnAction(event -> {
            // İlgili FXML dosyasını yükleyerek yeni bir sahne oluştur
            loadScene("bilgiler.fxml");
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
            Stage stage = (Stage) backButton5.getScene().getWindow();

            // Yeni sahneyi mevcut pencereye ayarla
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
