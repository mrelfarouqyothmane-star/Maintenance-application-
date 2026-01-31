package com.maintenance.controller;

import com.maintenance.dao.BatimentDAO;
import com.maintenance.model.Batiment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BatimentController {
    
    private TableView<Batiment> tableView;
    private ObservableList<Batiment> batimentList;
    private BatimentDAO batimentDAO;
    
    private TextField txtNom;
    private TextField txtLocalisation;
    
    public BatimentController() {
        batimentDAO = new BatimentDAO();
        batimentList = FXCollections.observableArrayList();
    }
    
    public Scene createScene(Stage stage) {
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createTableView());
        root.setRight(createFormulaire());
        root.setPadding(new Insets(10));
        
        chargerDonnees();
        
        return new Scene(root, 900, 600);
    }
    
    private VBox createHeader() {
        Label titre = new Label("Gestion des Bâtiments");
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        VBox header = new VBox(titre);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #4CAF50;");
        return header;
    }
    
    private TableView<Batiment> createTableView() {
        tableView = new TableView<>();
        
        TableColumn<Batiment, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);
        
        TableColumn<Batiment, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNom.setPrefWidth(300);
        
        TableColumn<Batiment, String> colLoc = new TableColumn<>("Localisation");
        colLoc.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        colLoc.setPrefWidth(230);
        
        tableView.getColumns().addAll(colId, colNom, colLoc);
        tableView.setItems(batimentList);
        
        return tableView;
    }
    
    private VBox createFormulaire() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        form.setPrefWidth(300);
        form.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ccc;");
        
        Label lblTitre = new Label("Formulaire");
        lblTitre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        txtNom = new TextField();
        txtNom.setPromptText("Nom du bâtiment");
        
        txtLocalisation = new TextField();
        txtLocalisation.setPromptText("Localisation");
        
        Button btnAjouter = new Button("Ajouter");
        btnAjouter.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnAjouter.setMaxWidth(Double.MAX_VALUE);
        btnAjouter.setOnAction(e -> ajouter());
        
        Button btnModifier = new Button("Modifier");
        btnModifier.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        btnModifier.setMaxWidth(Double.MAX_VALUE);
        btnModifier.setOnAction(e -> modifier());
        
        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        btnSupprimer.setMaxWidth(Double.MAX_VALUE);
        btnSupprimer.setOnAction(e -> supprimer());
        
        form.getChildren().addAll(
            lblTitre,
            new Label("Nom :"), txtNom,
            new Label("Localisation :"), txtLocalisation,
            btnAjouter, btnModifier, btnSupprimer
        );
        
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtNom.setText(newVal.getNom());
                txtLocalisation.setText(newVal.getLocalisation());
            }
        });
        
        return form;
    }
    
    private void chargerDonnees() {
        batimentList.clear();
        batimentList.addAll(batimentDAO.getAll());
    }
    
    private void ajouter() {
        if (txtNom.getText().isEmpty() || txtLocalisation.getText().isEmpty()) {
            showAlert("Erreur", "Remplissez tous les champs !", Alert.AlertType.ERROR);
            return;
        }
        
        Batiment bat = new Batiment(0, txtNom.getText(), txtLocalisation.getText());
        
        if (batimentDAO.add(bat)) {
            showAlert("Succès", "Bâtiment ajouté !", Alert.AlertType.INFORMATION);
            chargerDonnees();
            txtNom.clear();
            txtLocalisation.clear();
        }
    }
    
    private void modifier() {
        Batiment selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Sélectionnez un bâtiment", Alert.AlertType.ERROR);
            return;
        }
        
        selected.setNom(txtNom.getText());
        selected.setLocalisation(txtLocalisation.getText());
        
        if (batimentDAO.update(selected)) {
            showAlert("Succès", "Bâtiment modifié !", Alert.AlertType.INFORMATION);
            chargerDonnees();
        }
    }
    
    private void supprimer() {
        Batiment selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Sélectionnez un bâtiment", Alert.AlertType.ERROR);
            return;
        }
        
        if (batimentDAO.delete(selected.getId())) {
            showAlert("Succès", "Supprimé !", Alert.AlertType.INFORMATION);
            chargerDonnees();
        }
    }
    
    private void showAlert(String titre, String message, Alert.AlertType type) {
        new Alert(type, message, ButtonType.OK).showAndWait();
    }
}