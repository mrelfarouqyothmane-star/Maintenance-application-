package com.maintenance;

import com.maintenance.controller.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        
        VBox menu = new VBox(20);
        menu.setPadding(new Insets(50));
        
        Button btnTechniciens = new Button("Gestion des Techniciens");
        btnTechniciens.setPrefSize(300, 60);
        btnTechniciens.setStyle("-fx-font-size: 18px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        btnTechniciens.setOnAction(e -> ouvrirTechniciens());
        
        Button btnBatiments = new Button("Gestion des Bâtiments");
        btnBatiments.setPrefSize(300, 60);
        btnBatiments.setStyle("-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnBatiments.setOnAction(e -> ouvrirBatiments());
        
        Button btnInterventions = new Button("Gestion des Interventions");
        btnInterventions.setPrefSize(300, 60);
        btnInterventions.setStyle("-fx-font-size: 18px; -fx-background-color: #FF9800; -fx-text-fill: white;");
        btnInterventions.setOnAction(e -> ouvrirInterventions());
        
        menu.getChildren().addAll(btnTechniciens, btnBatiments, btnInterventions);
        
        Scene scene = new Scene(menu, 500, 400);
        stage.setTitle("Maintenance App - Menu Principal");
        stage.setScene(scene);
        stage.show();
    }
    
    private void ouvrirTechniciens() {
        TechnicienController controller = new TechnicienController();
        primaryStage.setScene(controller.createScene(primaryStage));
        primaryStage.setTitle("Gestion des Techniciens");
    }
    
    private void ouvrirBatiments() {
        BatimentController controller = new BatimentController();
        primaryStage.setScene(controller.createScene(primaryStage));
        primaryStage.setTitle("Gestion des Bâtiments");
    }
    
    private void ouvrirInterventions() {
        InterventionController controller = new InterventionController();
        primaryStage.setScene(controller.createScene(primaryStage));
        primaryStage.setTitle("Gestion des Interventions");
    }

    public static void main(String[] args) {
        launch(args);
    }
}