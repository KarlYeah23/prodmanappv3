package com.gabriel.prodmsv;

import com.gabriel.prodmsv.ServiceImpl.ContactService;
import com.gabriel.prodmsv.model.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class CreateContactController implements Initializable {
    @Setter
    ProdManController prodManController;
    @FXML
    public TextField tfFirstName;
    @FXML
    public TextField tfLastName;
    @FXML
    public TextField tfPhoneNumber;
    @FXML
    public TextField tfEmail;
    public Button btnSubmit;
    public Button btnNext;

    @Setter
    Stage stage;
    @Setter
    Scene parentScene;
    @Setter
    ContactService contactService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("CreateContactController: initialize");

        tfFirstName.setText("");
        tfLastName.setText("");
        tfPhoneNumber.setText("");
        tfEmail.setText("");
    }

    public void clearControlTexts() {
        tfFirstName.setText("");
        tfLastName.setText("");
        tfPhoneNumber.setText("");
        tfEmail.setText("");
    }

    public void onNext(ActionEvent actionEvent) {
        System.out.println("CreateContactController:onBack ");
        Node node = ((Node) (actionEvent.getSource()));
        Window window = node.getScene().getWindow();
        window.hide();

        stage.setScene(parentScene);
        stage.show();
    }

    public void onSubmit(ActionEvent actionEvent) throws Exception {
        Contact contact = new Contact();
        contact.setFirstName(tfFirstName.getText());
        contact.setLastName(tfLastName.getText());
        contact.setPhoneNumber(tfPhoneNumber.getText());
        contact.setEmail(tfEmail.getText());
        try {
            contact = contactService.create(contact);
            prodManController.refresh();
            onBack(actionEvent);
        } catch (Exception ex) {
            System.out.println("CreateContactController:onSubmit Error: " + ex.getMessage());
        }
    }

    public void onBack(ActionEvent actionEvent) {
        System.out.println("CreateContactController:onBack ");
        Node node = ((Node) (actionEvent.getSource()));
        Window window = node.getScene().getWindow();
        window.hide();

        stage.setScene(parentScene);
        stage.show();
    }
}
