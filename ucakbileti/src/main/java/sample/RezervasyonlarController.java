package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.DatabaseConnection;

import java.io.IOException;
import java.sql.*;

public class RezervasyonlarController {
    @FXML
    private ListView<String> rezervasyonlarListView;
    @FXML
    private Button backButton6;
    @FXML
    private Button iptalButton;


    @FXML
    private void initialize() {
        // Rezervasyonları ListView'a yükle
        loadReservations();

        // "GİRİŞ EKRANINA DÖN" butonuna tıklanınca yapılacak işlem
        backButton6.setOnAction(event -> {
            // İlgili FXML dosyasını yükleyerek yeni bir sahne oluştur
            loadScene("bilgiler.fxml");
        });
    }
    private void loadReservations() {
        // Rezervasyonları veritabanından al ve ListView'a ekle
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall("{call ListPassengers()}")) {

            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                // Rezervasyon bilgilerini ListView'a ekleyin
                StringBuilder reservationInfo = new StringBuilder();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    reservationInfo.append(columnName).append(": ").append(columnValue).append(", ");
                }

                reservationInfo.setLength(reservationInfo.length() - 2);
                rezervasyonlarListView.getItems().add(reservationInfo.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void loadScene(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) backButton6.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void initialize1() {
        // "İPTAL" butonuna tıklanınca yapılacak işlem
        iptalButton.setOnAction(event -> {
            // Seçilen satırı iptal et
            cancelReservation();
        });
    }

    private void cancelReservation() {
        // Get the selected item in the ListView
        String selectedReservation = rezervasyonlarListView.getSelectionModel().getSelectedItem();

        if (selectedReservation != null) {
            // Split the selected item to extract necessary information
            String[] reservationInfo = selectedReservation.split(", ");

            // Extract information (adjust the index based on your data structure)
            String passengerName = getValueFromInfo(reservationInfo, "first_name");
            String passengerSurname = getValueFromInfo(reservationInfo, "last_name");

            // Perform deletion from the database
            deleteReservationFromDatabase(passengerName, passengerSurname);

            // Remove the selected item from the ListView
            rezervasyonlarListView.getItems().remove(selectedReservation);
        }
    }

    private void deleteReservationFromDatabase(String name, String surname) {
        // Implement the database deletion logic here
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM passengers WHERE first_name = ? AND last_name = ?")) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);

            // Execute the delete operation
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getValueFromInfo(String[] reservationInfo, String key) {
        for (String info : reservationInfo) {
            if (info.startsWith(key)) {
                // Extract the value part after the ": "
                return info.substring(key.length() + 2);
            }
        }
        return null;
    }

    private void deleteReservationFromDatabase(String name, String surname, String seatNumber) {
        // Implement the database deletion logic here
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM passengers WHERE name = ? AND surname = ? AND seat_number = ?")) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, seatNumber);

            // Execute the delete operation
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
