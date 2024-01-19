package sample;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;


    public class LoginController {
        @FXML
        private Label loginMessageLabel;
        @FXML
        private TextField emailTextField;
        @FXML
        private PasswordField passwordPasswordField;
        @FXML
        private Button signupButton;


        public void loginButtonOnAction(ActionEvent e) {
            if (emailTextField.getText().isEmpty() == false && passwordPasswordField.getText().isEmpty() == false) {
                // loginMessageLabel.setText("Giriş Yapmayı Deneyin!");
                validateLogin();
            } else {
                loginMessageLabel.setText("Lütfen kullanıcı adı ve şifre giriniz.");
            }
        }
        @FXML
        public void validateLogin() {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            String verifyLogin = "SELECT COUNT(1) FROM customers WHERE email = '" + emailTextField.getText() + "' AND password = '" + passwordPasswordField.getText() + "'";

            try {
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(verifyLogin);

                while (queryResult.next()) {
                    if (queryResult.getInt(1) == 1) {
                        loginMessageLabel.setText("Hoşgeldiniz!");

                        // Başarılı giriş durumunda yeni bir sahne oluşturun ve gösterin
                        openSecim();
                    } else {
                        loginMessageLabel.setText("Geçersiz giriş. Lütfen tekrar deneyiniz.");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @FXML
        private void signupButtonOnAction() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
                Parent root = loader.load();

                // Yeni bir sahne oluşturun
                Scene scene = new Scene(root);

                // Mevcut pencereyi alın
                Stage stage = (Stage) signupButton.getScene().getWindow();

                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        private void openSecim() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("secim.fxml"));
                Parent root = loader.load();

                // Yeni bir sahne oluşturun
                Scene scene = new Scene(root);

                // Mevcut pencereyi alın
                Stage stage = (Stage) loginMessageLabel.getScene().getWindow();

                // Yeni sahneyi mevcut pencereye ayarlayın
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }