package com.maintenance.controller;

import com.maintenance.dao.*;
import com.maintenance.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

public class InterventionController {

    private TableView<Intervention> tableView;
    private ObservableList<Intervention> interventionList;
    private InterventionDAO interventionDAO;
    private TechnicienDAO technicienDAO;
    private BatimentDAO batimentDAO;

    private ComboBox<Technicien> cboTechnicien;
    private ComboBox<Batiment> cboBatiment;
    private DatePicker dpDate;
    private TextField txtType;
    private ComboBox<String> cboStatut;
    private TextArea txtDescription;

    public InterventionController() {
        interventionDAO = new InterventionDAO();
        technicienDAO = new TechnicienDAO();
        batimentDAO = new BatimentDAO();
        interventionList = FXCollections.observableArrayList();
    }

    public Scene createScene(Stage stage) {
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createTableView());
        root.setRight(createFormulaire());
        root.setPadding(new Insets(10));

        chargerDonnees();

        return new Scene(root, 1100, 600);
    }

    private VBox createHeader() {
        Label titre = new Label("Gestion des Interventions");
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // ajout des filtres 
        HBox filtres= new HBox(10);
        filtres.setPadding(new Insets(10,0,0,0));
        // FILTRE STATUT 
         ComboBox<String>cboStatut = new ComboBox<>();
         cboStatut.getItems().addAll("Tous", "En cours ", "Planifiee", "Terminee");
         cboStatut.setValue("Tous");
         // Filtre technicien 
         ComboBox<String> cboBat = new ComboBox<>();
         cboBat.getItems().add("Tous");
         cboBat.setValue("Tous");
      // Filtre batiment  
         ComboBox<String> cboTech = new ComboBox<>();
         cboBat.getItems().add("Tous");
         cboBat.setValue("Tous");
         filtres.getChildren().addAll(cboStatut,cboTech,cboBat);
          VBox header = new VBox(10, titre,filtres);
          header.setPadding(new Insets(15));
          header.setStyle("-fx-background-color :#FF9800;");
        

        return header;
    }

    private TableView<Intervention> createTableView() {
        tableView = new TableView<>();

        TableColumn<Intervention, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(40);

        TableColumn<Intervention, String> colTechnicien = new TableColumn<>("Technicien");
        colTechnicien.setCellValueFactory(cellData -> {
            int techId = cellData.getValue().getTechnicienId();
            Technicien tech = technicienDAO.getById(techId);
            return new javafx.beans.property.SimpleStringProperty(tech != null ? tech.getNom() : "N/A");
        });
        colTechnicien.setPrefWidth(150);

        TableColumn<Intervention, String> colBatiment = new TableColumn<>("Bâtiment");
        colBatiment.setCellValueFactory(cellData -> {
            int batId = cellData.getValue().getBatimentId();
            Batiment bat = batimentDAO.getById(batId);
            return new javafx.beans.property.SimpleStringProperty(bat != null ? bat.getNom() : "N/A");
        });
        colBatiment.setPrefWidth(120);

        TableColumn<Intervention, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateIntervention"));
        colDate.setPrefWidth(100);

        TableColumn<Intervention, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("typeIntervention"));
        colType.setPrefWidth(120);

        TableColumn<Intervention, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colStatut.setPrefWidth(100);

        TableColumn<Intervention, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.setPrefWidth(200);

        tableView.getColumns().addAll(colId, colTechnicien, colBatiment, colDate, colType, colStatut, colDescription);
        tableView.setItems(interventionList);

        return tableView;
    }

    private VBox createFormulaire() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        form.setPrefWidth(350);
        form.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ccc;");

        Label lblTitre = new Label("Formulaire Intervention");
        lblTitre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ComboBox Technicien
        cboTechnicien = new ComboBox<>();
        cboTechnicien.setPromptText("Sélectionner un technicien");
        cboTechnicien.setPrefWidth(320);
        chargerTechniciens();

        // ComboBox Bâtiment
        cboBatiment = new ComboBox<>();
        cboBatiment.setPromptText("Sélectionner un bâtiment");
        cboBatiment.setPrefWidth(320);
        chargerBatiments();

        // DatePicker
        dpDate = new DatePicker();
        dpDate.setValue(LocalDate.now());
        dpDate.setPrefWidth(320);

        // Type d'intervention
        txtType = new TextField();
        txtType.setPromptText("Ex: Réparation, Contrôle...");

        // Statut
        cboStatut = new ComboBox<>();
        cboStatut.getItems().addAll("En cours", "Planifiee", "Terminee");
        cboStatut.setValue("En cours");
        cboStatut.setPrefWidth(320);

        // Description
        txtDescription = new TextArea();
        txtDescription.setPromptText("Description de l'intervention...");
        txtDescription.setPrefHeight(80);
        txtDescription.setWrapText(true);

        // Boutons
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
                new Label("Technicien :"), cboTechnicien,
                new Label("Bâtiment :"), cboBatiment,
                new Label("Date :"), dpDate,
                new Label("Type :"), txtType,
                new Label("Statut :"), cboStatut,
                new Label("Description :"), txtDescription,
                btnAjouter, btnModifier, btnSupprimer, btnReinitialiser);

        // Listener pour remplir le formulaire lors de la sélection
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Technicien tech = technicienDAO.getById(newVal.getTechnicienId());
                Batiment bat = batimentDAO.getById(newVal.getBatimentId());

                cboTechnicien.setValue(tech);
                cboBatiment.setValue(bat);
                dpDate.setValue(newVal.getDateIntervention());
                txtType.setText(newVal.getTypeIntervention());
                cboStatut.setValue(newVal.getStatut());
                txtDescription.setText(newVal.getDescription());
            }
        });

        return form;
    }

    private void chargerTechniciens() {
        List<Technicien> techniciens = technicienDAO.getAll();
        cboTechnicien.setItems(FXCollections.observableArrayList(techniciens));
    }

    private void chargerBatiments() {
        List<Batiment> batiments = batimentDAO.getAll();
        cboBatiment.setItems(FXCollections.observableArrayList(batiments));
    }

    private void chargerDonnees() {
        interventionList.clear();
        interventionList.addAll(interventionDAO.getAll());
    }

    private void ajouter() {
        if (cboTechnicien.getValue() == null || cboBatiment.getValue() == null || txtType.getText().isEmpty()) {
            showAlert("Erreur", "Remplissez tous les champs !", Alert.AlertType.ERROR);
            return;
        }

        Intervention inter = new Intervention(
                0,
                cboTechnicien.getValue().getId(),
                cboBatiment.getValue().getId(),
                dpDate.getValue(),
                txtType.getText(),
                cboStatut.getValue(),
                txtDescription.getText());

        if (interventionDAO.add(inter)) {
            showAlert("Succès", "Intervention ajoutée !", Alert.AlertType.INFORMATION);
            chargerDonnees();
            reinitialiser();
        }
    }

    private void modifier() {
        Intervention selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Sélectionnez une intervention", Alert.AlertType.ERROR);
            return;
        }

        selected.setTechnicienId(cboTechnicien.getValue().getId());
        selected.setBatimentId(cboBatiment.getValue().getId());
        selected.setDateIntervention(dpDate.getValue());
        selected.setTypeIntervention(txtType.getText());
        selected.setStatut(cboStatut.getValue());
        selected.setDescription(txtDescription.getText());

        if (interventionDAO.update(selected)) {
            showAlert("Succès", "Intervention modifiée !", Alert.AlertType.INFORMATION);
            chargerDonnees();
            reinitialiser();
        }
    }

    private void supprimer() {
        Intervention selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Sélectionnez une intervention", Alert.AlertType.ERROR);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cette intervention ?");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (interventionDAO.delete(selected.getId())) {
                showAlert("Succès", "Supprimée !", Alert.AlertType.INFORMATION);
                chargerDonnees();
                reinitialiser();
            }
        }
    }

    private void reinitialiser() {
        cboTechnicien.setValue(null);
        cboBatiment.setValue(null);
        dpDate.setValue(LocalDate.now());
        txtType.clear();
        cboStatut.setValue("En cours");
        txtDescription.clear();
        tableView.getSelectionModel().clearSelection();
    }

    private void showAlert(String titre, String message, Alert.AlertType type) {
        new Alert(type, message, ButtonType.OK).showAndWait();
    }
}