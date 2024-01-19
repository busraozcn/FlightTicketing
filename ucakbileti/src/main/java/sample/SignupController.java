package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;


public class SignupController {
    @FXML
    private Button signupButton2;

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField emailTextField2;
    @FXML
    private PasswordField passwordPasswordField2;
    @FXML
    private Label registirationMessageLabel;
    @FXML
    private Button backButton1;
    @FXML
    public void signupButton2OnAction(ActionEvent e) {
        registerUser();

    }
    @FXML
    public void registerUser() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String email = emailTextField2.getText();
        String password = passwordPasswordField2.getText();

        // Corrected SQL query construction
        String insertFields = "INSERT INTO customers(first_name, last_name, email, password) VALUES ('";
        String insertValues = name + "','" + surname + "','" + email + "','" + password + "')";
        String insertToRegister = insertFields + insertValues;

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);
            registirationMessageLabel.setText("Kayıt başarıyla tamamlandı!");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    @FXML
    private void initialize() {

        // "GİRİŞ EKRANINA DÖN" butonuna tıklanınca yapılacak işlem
        backButton1.setOnAction(event -> {
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
            Stage stage = (Stage) backButton1.getScene().getWindow();

            // Yeni sahneyi mevcut pencereye ayarla
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

    }
    }
}


