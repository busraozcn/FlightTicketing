package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class BilgilerController {

    @FXML
    private TextField emailTextBox;

    @FXML
    private TextField phoneNumberTextBox;

    @FXML
    private TextField nameTextBox;

    @FXML
    private TextField lastNameTextBox;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private Button gotopayButton;
    @FXML
    private Button backButton4;
    @FXML
    private TextField tcTextBox;



    @FXML
    private void initialize() {
        genderComboBox.getItems().addAll("F", "M");
    }

    @FXML
    private void handleGotopayButton() {
        String email = emailTextBox.getText();
        String phoneNumber = phoneNumberTextBox.getText();
        String name = nameTextBox.getText();
        String lastName = lastNameTextBox.getText();
        String gender = genderComboBox.getValue();
        LocalDate birthDateLocalDate = birthDatePicker.getValue();
        String tckimlik = tcTextBox.getText();
        Date birthDate = java.sql.Date.valueOf(birthDateLocalDate.toString());

        try (Connection connection = DatabaseConnection.getConnection()) {
            String checkEmailQuery = "SELECT COUNT(*) FROM passengers WHERE email = ?";
            try (PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmailQuery)) {
                checkEmailStatement.setString(1, email);
                ResultSet resultSet = checkEmailStatement.executeQuery();

                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    showAlert("This email address is already registered.");
                    return;
                }
            }

            String queryPassengers = "INSERT INTO passengers (email, first_name, last_name, birth_date, gender, phone_number, tc_kimlik) " +
                    "VALUES (?, ?, ?, ?, ?, ?,?)";
            try (PreparedStatement preparedStatementPassengers = connection.prepareStatement(queryPassengers)) {
                preparedStatementPassengers.setString(1, email);
                preparedStatementPassengers.setString(2, name);
                preparedStatementPassengers.setString(3, lastName);
                preparedStatementPassengers.setDate(4, birthDate);
                preparedStatementPassengers.setString(5, gender);
                preparedStatementPassengers.setString(6, phoneNumber);
                preparedStatementPassengers.setString(7, tckimlik);

                int affectedRowsPassengers = preparedStatementPassengers.executeUpdate();

                if (affectedRowsPassengers > 0) {
                    // Yolcu bilgileri başarıyla eklenmiş, şimdi ödeme ekranına geçiş yap
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("odeme.fxml"));
                        Parent root = loader.load();

                        // OdemeController'ı al
                        OdemeController odemeController = loader.getController();

                        // Yeni bir sahne oluşturun
                        Scene scene = new Scene(root);

                        // Mevcut pencereyi alın
                        Stage stage = (Stage) gotopayButton.getScene().getWindow();

                        // Yeni sahneyi mevcut pencereye ayarlayın
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    showAlert("Failed to add Passenger information to passengers table.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("An error occurred while processing your request.");
        }
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

        backButton4.setOnAction(event -> {
            // İlgili FXML dosyasını yükleyerek yeni bir sahne oluştur
            loadScene();
        });
    }
    @FXML
    private void loadScene() {
        try {
            // İlgili FXML dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ucuslar.fxml"));
            Parent root = loader.load();

            // Yeni bir sahne oluştur
            Scene scene = new Scene(root);

            // Mevcut pencereyi al
            Stage stage = (Stage) backButton4.getScene().getWindow();

            // Yeni sahneyi mevcut pencereye ayarla
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}