package com.gabriel.prodmsv;

import com.gabriel.prodmsv.ServiceImpl.ProductService;
import com.gabriel.prodmsv.model.Product;
import javafx.application.Platform;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Data;
import lombok.Setter;

import java.net.URL;
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

    public TextField tfId;
    public TextField tfName;
    public TextField tfDesc;
    public ImageView productImage;
    public VBox prodman;

    Image puffy;
    Image wink;

    @FXML
    public Button createButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button closeButton;

    public static Product product;
    @FXML
    private ListView<Product> lvProducts;

    UpdateProductController updateProductController;
    DeleteProductController deleteProductController;
    CreateProductController createProductController;
    ProductService productService;

    void refresh() throws Exception {
        productService = ProductService.getService();
        Product[] products = productService.getProducts();
        lvProducts.getItems().clear();
        lvProducts.getItems().addAll(products);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ProdManController: initialize");
        disableControls();

        try {
            refresh();
            try {
                puffy = new Image(getClass().getResourceAsStream("/images/puffy.gif"));
                wink = new Image(getClass().getResourceAsStream("/images/wink.gif"));
                productImage.setImage(puffy);
            } catch(Exception ex) {
                System.out.println("Error with image: " + ex.getMessage());
            }
        } catch (Exception ex) {
            showErrorDialog("Message: " + ex.getMessage());
        }
    }

    public void disableControls() {
        tfId.editableProperty().set(false);
        tfName.editableProperty().set(false);
        tfDesc.editableProperty().set(false);
    }

    public void setControlTexts(Product product) {
        tfName.setText(product.getName());
        tfDesc.setText(product.getDescription());
    }

    public void clearControlTexts() {
        tfId.setText("");
        tfName.setText("");
        tfDesc.setText("");
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        product = lvProducts.getSelectionModel().getSelectedItem();
        if(product == null) {
            return;
        }
        tfId.setText(Integer.toString(product.getId()));
        setControlTexts(product);
        System.out.println("clicked on " + product);
    }

    public void onCreate(ActionEvent actionEvent) {
        System.out.println("ProdmanController:onNewProduct ");
        Node node = ((Node) (actionEvent.getSource()));
        Scene currentScene = node.getScene();
        Window window = currentScene.getWindow();
        window.hide();
        try {
            if(createViewScene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(SplashApp.class.getResource("create-product.fxml"));
                Parent root = fxmlLoader.load();
                createProductController = fxmlLoader.getController();
                createProductController.setStage(this.stage);
                createProductController.setParentScene(currentScene);
                createProductController.setProductService(productService);
                createProductController.setProdManController(this);
                createViewScene = new Scene(root, 300, 600);
                stage.setTitle("Manage Product");
                stage.setScene(createViewScene);
                stage.show();
            } else {
                stage.setScene(createViewScene);
                stage.show();
            }
            createProductController.clearControlTexts();
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
                FXMLLoader fxmlLoader = new FXMLLoader(SplashApp.class.getResource("update-product.fxml"));
                Parent root = fxmlLoader.load();
                updateProductController = fxmlLoader.getController();
                updateProductController.setController(this);
                updateProductController.setStage(this.stage);
                updateProductController.setParentScene(currentScene);
                updateViewScene = new Scene(root, 300, 600);
            } else {
                updateProductController.refresh();
            }
            stage.setTitle("Update Product");
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
                FXMLLoader fxmlLoader = new FXMLLoader(SplashApp.class.getResource("delete-product.fxml"));
                Parent root = fxmlLoader.load();
                deleteProductController = fxmlLoader.getController();
                deleteProductController.setController(this);
                deleteProductController.setStage(this.stage);
                deleteProductController.setParentScene(currentScene);
                deleteViewScene = new Scene(root, 300, 600);
            } else {
                deleteProductController.refresh();
            }
            stage.setTitle("Delete Product");
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

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
