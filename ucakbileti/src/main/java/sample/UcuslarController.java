package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class UcuslarController {

    @FXML
    private ListView<String> ucusListView;

    @FXML
    private Button secButton;
    @FXML
    private Button backButton3;


    // This method will be called when the "Seç" button is clicked
    @FXML
    private void handleSecButton() {
        // Perform actions based on the selected flight
        String selectedFlight = ucusListView.getSelectionModel().getSelectedItem();

        if (selectedFlight != null) {
            // Uygun bir uçuş seçildiyse, bilgiler.fxml ekranına geçiş yap
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("bilgiler.fxml"));
                Parent root = loader.load();

                // BilgilerController'ı al
                BilgilerController bilgilerController = loader.getController();


                // Yeni bir sahne oluşturun
                Scene scene = new Scene(root);

                // Mevcut pencereyi alın
                Stage stage = (Stage) secButton.getScene().getWindow();

                // Yeni sahneyi mevcut pencereye ayarlayın
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Eğer hiçbir uçuş seçilmediyse kullanıcıyı uyarabilirsiniz
            System.out.println("Lütfen bir uçuş seçin.");
        }


    }



    public void setFiltreler(String nereden, String nereye, LocalDate gidisTarihi, LocalDate donusTarihi, boolean tekYon) {

        List<String> filteredFlights = getFilteredFlights(nereden, nereye, gidisTarihi, donusTarihi, tekYon);
        ucusListView.getItems().addAll(filteredFlights);
    }


    private List<String> getFilteredFlights(String nereden, String nereye, LocalDate gidisTarihi, LocalDate donusTarihi, boolean tekYon) {
        List<String> flights = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {

            String query = "SELECT flight_id ,airline_name, departure_airport_name, arrival_airport_name, departure_date, departure_time, price " +
                    "FROM flights " +
                    "WHERE departure_airport_name = ? AND arrival_airport_name = ? AND departure_date >= ?";

            if (!tekYon) {

                query += " AND departure_date <= ?";
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nereden);
                preparedStatement.setString(2, nereye);
                preparedStatement.setDate(3, Date.valueOf(gidisTarihi));

                if (!tekYon) {
                    preparedStatement.setDate(4, Date.valueOf(donusTarihi));
                }

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Assuming your columns are of type String, adjust the types accordingly
                        String flightInfo = "Flight ID: " + resultSet.getString("flight_id") + ", Airline: " + resultSet.getString("airline_name") +
                                ", Departure Airport: " + resultSet.getString("departure_airport_name") +
                                ", Arrival Airport: " + resultSet.getString("arrival_airport_name") +
                                ", Departure Date: " + resultSet.getDate("departure_date") +
                                ", Departure Time: " + resultSet.getTime("departure_time") +  // Get the departure time
                                ", Price: " + resultSet.getDouble("price");
                        flights.add(flightInfo);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }
    @FXML
    private void initialize() {

        // "GİRİŞ EKRANINA DÖN" butonuna tıklanınca yapılacak işlem
        backButton3.setOnAction(event -> {
            // İlgili FXML dosyasını yükleyerek yeni bir sahne oluştur
            loadScene("secim.fxml");
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
            Stage stage = (Stage) backButton3.getScene().getWindow();

            // Yeni sahneyi mevcut pencereye ayarla
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}

