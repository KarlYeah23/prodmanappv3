package com.gabriel.prodmsv;

import com.gabriel.prodmsv.ServiceImpl.ProductService;
import com.gabriel.prodmsv.model.Product;


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
public class CreateProductController implements Initializable {
    @Setter
    ProdManController prodManController;
    @FXML
    public TextField tfName;
    @FXML
    public TextField tfDesc;
    public Button btnSubmit;
    public Button btnNext;

    @Setter
    Stage stage;
    @Setter
    Scene parentScene;
    @Setter
    ProductService productService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        System.out.println("CreateProductController: initialize");

        tfName.setText("");
        tfDesc.setText("");
    }

    public void clearControlTexts(){
        tfName.setText("");
        tfDesc.setText("");
    }

    public void onNext(ActionEvent actionEvent) {
        System.out.println("CreateProductController:onBack ");
        Node node = ((Node) (actionEvent.getSource()));
        Window window = node.getScene().getWindow();
        window.hide();

        stage.setScene(parentScene);
        stage.show();
    }

    public void onSubmit(ActionEvent actionEvent) throws Exception{
        Product product = new Product();
        product.setName(tfName.getText());
        product.setDescription(tfDesc.getText());
        try{
            product = productService.create(product);
            prodManController.refresh();
            onBack(actionEvent);
        }
        catch(Exception ex){
            System.out.println("CreateProductController:onSubmit Error: " + ex.getMessage());
        }
    }

    public void onBack(ActionEvent actionEvent) {
        System.out.println("CreateProductController:onBack ");
        Node node = ((Node) (actionEvent.getSource()));
        Window window = node.getScene().getWindow();
        window.hide();

        stage.setScene(parentScene);
        stage.show();
    }
}
