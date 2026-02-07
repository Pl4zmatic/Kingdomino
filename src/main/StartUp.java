package main;

import java.awt.Toolkit;

import cli.KingDominoCLI_Applicatie;
import domein.DomeinController;
import gui.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartUp extends Application {
	
	private static DomeinController dc;

	public static void main(String[] args) {
		dc = new DomeinController();
		launch(args);
		//new KingDominoCLI_Applicatie(dc).app();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MenuController menu = new MenuController(primaryStage, dc);
		
		Scene scene = new Scene(menu, KingdominoGlobals.screenHeight, KingdominoGlobals.screenHeight);
		scene.getStylesheets().add(getClass().getResource("/gui/css/menu.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Kingdomino");
		primaryStage.show();
		
		menu.initResizeEvents();
		menu.resize();
	}
}
