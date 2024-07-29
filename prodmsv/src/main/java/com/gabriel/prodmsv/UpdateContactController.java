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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class UpdateContactController implements Initializable {
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfPhoneNumber;
    @FXML
    private TextField tfEmail;

    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnBack;

    @Setter
    private Stage stage;
    @Setter
    private Scene parentScene;
    @Setter
    private ContactService contactService;
    @Setter
    private ProdManController controller;

    private int contactId; // To store the ID internally

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("UpdateContactController: initialize");
    }

    public void refresh() {
        Contact contact = ProdManController.contact;
        contactId = contact.getId(); // Set ID internally
        tfFirstName.setText(contact.getFirstName());
        tfLastName.setText(contact.getLastName());
        tfPhoneNumber.setText(contact.getPhoneNumber());
        tfEmail.setText(contact.getEmail());
    }

    @FXML
    public void onSubmit(ActionEvent actionEvent) {
        try {
            Contact contact = toObject();
            contact.setId(contactId); // Ensure ID is set
            contact = contactService.update(contact);
            controller.refresh();
            onBack(actionEvent);
        } catch (Exception e) {
            String message = "Error encountered updating contact";
            showErrorDialog(message, e.getMessage());
        }
    }

    @FXML
    public void onBack(ActionEvent actionEvent) {
        System.out.println("UpdateContactController:onBack ");
        Node node = ((Node) (actionEvent.getSource()));
        Window window = node.getScene().getWindow();
        window.hide();

        stage.setScene(parentScene);
        stage.show();
    }

    protected Contact toObject() {
        Contact contact = new Contact();
        try {
            contact.setFirstName(tfFirstName.getText());
            contact.setLastName(tfLastName.getText());
            contact.setPhoneNumber(tfPhoneNumber.getText());
            contact.setEmail(tfEmail.getText());
        } catch (Exception e) {
            showErrorDialog("Error", e.getMessage());
        }
        return contact;
    }

    private void showErrorDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(header);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
