package com.gabriel.prodmsv;

import com.gabriel.prodmsv.ServiceImpl.ContactService;
import com.gabriel.prodmsv.model.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class DeleteContactController implements Initializable {
    public TextField tfId;
    public TextField tfFirstName;
    public TextField tfLastName;
    public TextField tfPhoneNumber;
    public TextField tfEmail;

    @Setter
    Stage stage;
    @Setter
    Scene parentScene;
    @Setter
    ContactService contactService;
    @Setter
    ProdManController controller;

    public void refresh() {
        Contact contact = ProdManController.contact;
        tfId.setText(Integer.toString(contact.getId()));
        tfFirstName.setText(contact.getFirstName());
        tfLastName.setText(contact.getLastName());
        tfPhoneNumber.setText(contact.getPhoneNumber());
        tfEmail.setText(contact.getEmail());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("DeleteContactController: initialize");
        tfId = new TextField("");
        refresh();
    }

    public void onBack(ActionEvent actionEvent) {
        System.out.println("DeleteContactController:onBack ");
        Node node = ((Node) (actionEvent.getSource()));
        Window window = node.getScene().getWindow();
        window.hide();

        stage.setScene(parentScene);
        stage.show();
    }

    public void onSubmit(ActionEvent actionEvent) {
        try {
            Contact contact = toObject(true);
            ContactService.getService().delete(contact.getId());
            controller.refresh();
            controller.clearControlTexts();
            Node node = ((Node) (actionEvent.getSource()));
            Window window = node.getScene().getWindow();
            window.hide();
            stage.setTitle("Manage Contact");
            stage.setScene(parentScene);
            stage.show();
        } catch (Exception e) {
            String message = "Error encountered deleting contact";
            showErrorDialog(message, e.getMessage());
        }
    }

    protected Contact toObject(boolean isEdit) {
        Contact contact = new Contact();
        try {
            if (isEdit) {
                contact.setId(Integer.parseInt(tfId.getText()));
            }
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
