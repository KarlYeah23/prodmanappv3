package com.gabriel.prodmsv;

import com.gabriel.prodmsv.ServiceImpl.ContactService;
import com.gabriel.prodmsv.model.Contact;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.util.Comparator;
import lombok.Data;
import lombok.Setter;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@Data
public class ProdManController implements Initializable {
    @Setter
    Stage stage;
    @Setter
    Scene createViewScene;
    @Setter
    Scene updateViewScene;
    @Setter
    Scene deleteViewScene;

    public TextField tfFirstName;
    public TextField tfLastName;
    public TextField tfEmail;
    public TextField tfPhone;
    public ImageView contactImage;
    public VBox prodman;

    Image puffy;
    Image wink;

    @FXML
    private ComboBox<String> cbFilter;
    @FXML
    private TextField tfSearch;
    @FXML
    public Button createButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button closeButton;

    public static Contact contact;
    @FXML
    private ListView<Contact> lvContacts;

    private FilteredList<Contact> filteredContacts;
    private SortedList<Contact> sortedContacts;

    UpdateContactController updateContactController;
    DeleteContactController deleteContactController;
    CreateContactController createContactController;
    ContactService contactService;

    private ObservableList<Contact> contactList;

    void refresh() throws Exception {
        contactService = ContactService.getService();
        Contact[] contacts = contactService.getContacts(); // Updated method name
        contactList = FXCollections.observableArrayList(contacts);

        SortedList<Contact> sortedList = new SortedList<>(contactList);
        sortedList.setComparator(Comparator.comparing(Contact::getFirstName));

        lvContacts.setItems(sortedList);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ProdManController: initialize");
        disableControls();
        try {
            refresh();
            try {
                puffy = new Image(getClass().getResourceAsStream("/images/puffy.png"));
                wink = new Image(getClass().getResourceAsStream("/images/wink.png"));
                contactImage.setImage(puffy);
            } catch (Exception ex) {
                System.out.println("Error with image: " + ex.getMessage());
            }

            cbFilter.getItems().addAll("First Name", "Last Name", "Phone Number", "Email");
            cbFilter.getSelectionModel().selectFirst();
            tfSearch.setOnKeyReleased(this::onSearch);

            filteredContacts = new FilteredList<>(lvContacts.getItems(), p -> true);
            SortedList<Contact> sortedContacts = new SortedList<>(filteredContacts);
            sortedContacts.setComparator(Comparator.comparing(Contact::getFirstName));
            lvContacts.setItems(sortedContacts);

        } catch (Exception ex) {
            showErrorDialog("Message: " + ex.getMessage());
        }
    }

    public void disableControls() {
        tfFirstName.editableProperty().set(false);
        tfLastName.editableProperty().set(false);
        tfEmail.editableProperty().set(false);
        tfPhone.editableProperty().set(false);
    }

    public void setControlTexts(Contact contact) {
        tfFirstName.setText(contact.getFirstName());
        tfLastName.setText(contact.getLastName());
        tfEmail.setText(contact.getEmail());
        tfPhone.setText(contact.getPhoneNumber());
    }

    public void clearControlTexts() {
        tfFirstName.setText("");
        tfLastName.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        contact = lvContacts.getSelectionModel().getSelectedItem();
        if(contact == null) {
            return;
        }
        setControlTexts(contact);
        System.out.println("clicked on " + contact);
    }

    public void onCreate(ActionEvent actionEvent) {
        System.out.println("ProdmanController:onNewContact ");
        Node node = ((Node) (actionEvent.getSource()));
        Scene currentScene = node.getScene();
        Window window = currentScene.getWindow();
        window.hide();
        try {
            if(createViewScene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(SplashApp.class.getResource("create-contact.fxml"));
                Parent root = fxmlLoader.load();
                createContactController = fxmlLoader.getController();
                createContactController.setStage(this.stage);
                createContactController.setParentScene(currentScene);
                createContactController.setContactService(contactService);
                createContactController.setProdManController(this);
                createViewScene = new Scene(root, 300, 600);
                stage.setTitle("Manage Contact");
                stage.setScene(createViewScene);
                stage.show();
            } else {
                stage.setScene(createViewScene);
                stage.show();
            }
            createContactController.clearControlTexts();
            clearControlTexts();
        } catch(Exception ex) {
            System.out.println("ProdmanController: " + ex.getMessage());
        }
    }

    public void onUpdate(ActionEvent actionEvent) {
        System.out.println("ProdmanController:onUpdate ");
        Node node = ((Node) (actionEvent.getSource()));
        Scene currentScene = node.getScene();
        Window window = currentScene.getWindow();
        window.hide();
        try {
            if(updateViewScene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(SplashApp.class.getResource("update-contact.fxml"));
                Parent root = fxmlLoader.load();
                updateContactController = fxmlLoader.getController();
                updateContactController.setController(this);
                updateContactController.setStage(this.stage);
                updateContactController.setParentScene(currentScene);
                updateViewScene = new Scene(root, 300, 600);
            } else {
                updateContactController.refresh();
            }
            stage.setTitle("Update Contact");
            stage.setScene(updateViewScene);
            stage.show();
        } catch(Exception ex) {
            System.out.println("ProdmanController: " + ex.getMessage());
        }
    }

    public void onDelete(ActionEvent actionEvent) {
        System.out.println("ProdmanController:onDelete ");
        Node node = ((Node) (actionEvent.getSource()));
        Scene currentScene = node.getScene();
        Window window = currentScene.getWindow();
        window.hide();
        try {
            if(deleteViewScene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(SplashApp.class.getResource("delete-contact.fxml"));
                Parent root = fxmlLoader.load();
                deleteContactController = fxmlLoader.getController();
                deleteContactController.setController(this);
                deleteContactController.setStage(this.stage);
                deleteContactController.setParentScene(currentScene);
                deleteViewScene = new Scene(root, 300, 600);
            } else {
                deleteContactController.refresh();
            }
            stage.setTitle("Delete Contact");
            stage.setScene(deleteViewScene);
            stage.show();
        } catch(Exception ex) {
            System.out.println("ProdmanController: " + ex.getMessage());
        }
    }

    public void onClose(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Exit");
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                Platform.exit();
            }
        });
    }

    private void onSearch(KeyEvent event) {
        String filter = cbFilter.getValue();
        String searchText = tfSearch.getText().toLowerCase();

        filteredContacts.setPredicate(contact -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            switch (filter) {
                case "First Name":
                    return contact.getFirstName().toLowerCase().contains(searchText);
                case "Last Name":
                    return contact.getLastName().toLowerCase().contains(searchText);
                case "Phone Number":
                    return contact.getPhoneNumber().toLowerCase().contains(searchText);
                case "Email":
                    return contact.getEmail().toLowerCase().contains(searchText);
                default:
                    return true;
            }
        });
        lvContacts.setItems(filteredContacts);
    }

    private void sortListView() {
        lvContacts.getItems().sort(Comparator.comparing(Contact::getFirstName));
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
