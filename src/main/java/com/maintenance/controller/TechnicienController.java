package com.maintenance.controller;

import com.maintenance.dao.TechnicienDAO;
import com.maintenance.model.Technicien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TechnicienController {

    private TableView<Technicien> tableView;
    private ObservableList<Technicien> technicienList;
    private TechnicienDAO technicienDAO;

    private TextField txtNom;
    private TextField txtQualification;
    private CheckBox chkDisponibilite;

    public TechnicienController() {
        technicienDAO = new TechnicienDAO();
        technicienList = FXCollections.observableArrayList();
    }

    public Scene createScene(Stage stage) {
        BorderPane root = new BorderPane();
        
        root.setTop(createHeader(stage)); 
        root.setCenter(createTableView());
        root.setRight(createFormulaire());
        root.setPadding(new Insets(10));

        chargerDonnees();

        return new Scene(root, 900, 600);
    }

    
    private HBox createHeader(Stage stage) {
        Label titre = new Label("Gestion des Techniciens");
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> new com.maintenance.MainApp().start(stage));
        
       
        // Action pour retourner au menu principal
        btnRetour.setOnAction(e -> new com.maintenance.MainApp().start(stage));
        
        HBox header = new HBox(20, btnRetour, titre);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2196F3;"); 
        return header;
    }

    private TableView<Technicien> createTableView() {
        tableView = new TableView<>();

        TableColumn<Technicien, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<Technicien, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNom.setPrefWidth(250);

        TableColumn<Technicien, String> colQualif = new TableColumn<>("Qualification");
        colQualif.setCellValueFactory(new PropertyValueFactory<>("qualification"));
        colQualif.setPrefWidth(180);

        TableColumn<Technicien, Boolean> colDispo = new TableColumn<>("Disponible");
        colDispo.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
        colDispo.setPrefWidth(100);

        tableView.getColumns().addAll(colId, colNom, colQualif, colDispo);
        tableView.setItems(technicienList);

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
        txtNom.setPromptText("Nom complet");

        txtQualification = new TextField();
        txtQualification.setPromptText("Qualification");

        chkDisponibilite = new CheckBox("Disponible");
        chkDisponibilite.setSelected(true);

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

        Button btnReinitialiser = new Button("Réinitialiser");
        btnReinitialiser.setMaxWidth(Double.MAX_VALUE);
        btnReinitialiser.setOnAction(e -> reinitialiser());

        form.getChildren().addAll(
                lblTitre,
                new Label("Nom :"), txtNom,
                new Label("Qualification :"), txtQualification,
                chkDisponibilite,
                btnAjouter, btnModifier, btnSupprimer, btnReinitialiser);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtNom.setText(newVal.getNom());
                txtQualification.setText(newVal.getQualification());
                chkDisponibilite.setSelected(newVal.isDisponibilite());
            }
        });

        return form;
    }

    private void chargerDonnees() {
        technicienList.clear();
        technicienList.addAll(technicienDAO.getAll());
    }

    private void ajouter() {
        if (txtNom.getText().isEmpty() || txtQualification.getText().isEmpty()) {
            showAlert("Erreur", "Remplissez tous les champs !", Alert.AlertType.ERROR);
            return;
        }

        Technicien tech = new Technicien(0, txtNom.getText(), txtQualification.getText(),
                chkDisponibilite.isSelected());

        if (technicienDAO.add(tech)) {
            showAlert("Succès", "Technicien ajouté !", Alert.AlertType.INFORMATION);
            chargerDonnees();
            reinitialiser();
        }
    }

    private void modifier() {
        Technicien selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Sélectionnez un technicien", Alert.AlertType.ERROR);
            return;
        }

        selected.setNom(txtNom.getText());
        selected.setQualification(txtQualification.getText());
        selected.setDisponibilite(chkDisponibilite.isSelected());

        if (technicienDAO.update(selected)) {
            showAlert("Succès", "Technicien modifié !", Alert.AlertType.INFORMATION);
            chargerDonnees();
            reinitialiser();
        }
    }

    private void supprimer() {
        Technicien selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Sélectionnez un technicien", Alert.AlertType.ERROR);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer " + selected.getNom() + " ?");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (technicienDAO.delete(selected.getId())) {
                showAlert("Succès", "Supprimé !", Alert.AlertType.INFORMATION);
                chargerDonnees();
                reinitialiser();
            }
        }
    }

    private void reinitialiser() {
        txtNom.clear();
        txtQualification.clear();
        chkDisponibilite.setSelected(true);
        tableView.getSelectionModel().clearSelection();
    }

    private void showAlert(String titre, String message, Alert.AlertType type) {
        new Alert(type, message, ButtonType.OK).showAndWait();
    }
}