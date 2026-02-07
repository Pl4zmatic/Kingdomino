module kingdomino.g55 {
	exports cli;
	exports persistentie;
	exports utils;
	exports gui;
	exports main;
	exports audio;
	exports domein;
	exports testen;
	exports dto;
	exports exceptions;

	requires java.desktop;
	requires java.sql;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	
	opens gui to javafx.fxml;
}