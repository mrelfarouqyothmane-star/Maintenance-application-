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
    
    // les variables
    private TableView<Batiment> tableView;
    private ObservableList<Batiment> batimentList;
    private BatimentDAO dao;
    
    private TextField txtNom;
    private TextField txtLocalisation;
    
    // initialisation
    public BatimentController() {
        dao = new BatimentDAO();
        batimentList = FXCollections.observableArrayList();
    }
    
    // écran de batiment
    public Scene createScene(Stage stage) { 
        BorderPane root = new BorderPane();
        root.setTop(creerHeader(stage));
        root.setCenter(creerTableau());
        root.setRight(creerFormulaire());
        root.setPadding(new Insets(10));
        
        charger(); // Charger les données
        return new Scene(root, 900, 600);
    }
    
    // header ie haut de l'écran
    private HBox creerHeader(Stage stage) {
        Label titre = new Label("Gestion des Bâtiments");
        titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        
        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> new com.maintenance.MainApp().start(stage));
        
        HBox header = new HBox(20, btnRetour, titre);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #4CAF50;");
        return header;
    }
    
    // tableau
    private TableView<Batiment> creerTableau() {
        tableView = new TableView<>();
        
        // Colonne ID
        TableColumn<Batiment, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);
        
        // Colonne Nom
        TableColumn<Batiment, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNom.setPrefWidth(300);
        
        // Colonne Localisation
        TableColumn<Batiment, String> colLoc = new TableColumn<>("Localisation");
        colLoc.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        colLoc.setPrefWidth(230);
        
        tableView.getColumns().addAll(colId, colNom, colLoc);
        tableView.setItems(batimentList);
        
        return tableView;
    }
    
    // formulaire
    private VBox creerFormulaire() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        form.setPrefWidth(300);
        
        Label titre = new Label("Formulaire");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
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
        
        form.getChildren().addAll(titre, new Label("Nom :"), txtNom, 
                                  new Label("Localisation :"), txtLocalisation,
                                  btnAjouter, btnModifier, btnSupprimer);
        
        // pour remplir le formulaire 
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, old, nouveau) -> {
            if (nouveau != null) {
                txtNom.setText(nouveau.getNom());
                txtLocalisation.setText(nouveau.getLocalisation());
            }
        });
        
        return form;
    }
    
    // pour charger les données 
    private void charger() {
        batimentList.clear();
        batimentList.addAll(dao.getAll());
    }
    
    // pour ajouter un batiment
    private void ajouter() {
        if (txtNom.getText().isEmpty() || txtLocalisation.getText().isEmpty()) {
            alert("Remplissez tous les champs :");
            return;
        }
        
        Batiment bat = new Batiment(0, txtNom.getText(), txtLocalisation.getText());
        
        if (dao.add(bat)) {
            alert("Batiment ajouté");
            charger();
            vider();
        }
    }
    
    // pour modifier un batiment
    private void modifier() {
        Batiment selected = tableView.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            alert("Sélectionnez un bâtiment");
            return;
        }
        
        selected.setNom(txtNom.getText());
        selected.setLocalisation(txtLocalisation.getText());
        
        if (dao.update(selected)) {
            alert("Modifié !");
            charger();
        }
    }
    
    // pour supprimer un batiment 
    private void supprimer() {
        Batiment selected = tableView.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            alert("Sélectionnez un bâtiment");
            return;
        }
        
        if (dao.delete(selected.getId())) {
            alert("Supprimé !");
            charger();
            vider();
        }
    }
    
    // pour valider le formulaire 
    private void vider() {
        txtNom.clear();
        txtLocalisation.clear();
    }
    
    // pour afficher un message
    private void alert(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }
}