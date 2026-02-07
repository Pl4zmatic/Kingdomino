package gui;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import audio.AudioCue;
import domein.DomeinController;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button; 
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;

public class MenuController extends BorderPane implements Initializable {

	private Stage primaryStage;
	private DomeinController dc;

	@FXML
	private ChoiceBox<String> cb_taal;
	@FXML
	private BorderPane root;
	@FXML
	private ImageView logo;
	@FXML
	private Button btn_registreerSpeler;
	@FXML
	private Button btn_speel;
	@FXML
	private Button btn_afsluiten;
	@FXML
	private Label volumeLabel;
	@FXML
	private Slider volumeSlider;

	private ChangeListener<String> taalSelectieListener;
	AudioCue myAudioCue;

	@Override
	public void initialize(URL location, ResourceBundle rb) {

		URL url = this.getClass().getResource("untitled.wav");

		try {
			myAudioCue = AudioCue.makeStereoCue(url, 4);
			myAudioCue.open();
		} catch (Exception e) {
			System.out.printf("%n%s%n", e.getMessage());
		}
		final int handle = myAudioCue.play();
		myAudioCue.setLooping(handle, -1);

		volumeSlider.setValue(myAudioCue.getVolume(handle) * 100);
		volumeSlider.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				myAudioCue.setVolume(handle, volumeSlider.getValue() / 100);

			}
		});
	}

	public MenuController(Stage primaryStage, DomeinController dc) { 
		this.dc = dc;
		this.primaryStage = primaryStage;
		KingdominoGlobals.loadFxml("menu.fxml", this);

		taalSelectieListener = (arg0, arg1, arg2) -> selectTaal();
		setTaalBox(KingdominoGlobals.getResourceBundleText(KingdominoGlobals.NL_KEY));

	}

	private void setTaalBox(String taal) {
		cb_taal.getSelectionModel().selectedItemProperty().removeListener(taalSelectieListener);

		ObservableList<String> talen = FXCollections.observableArrayList(
				Arrays.asList(new String[] { KingdominoGlobals.getResourceBundleText(KingdominoGlobals.NL_KEY),
						KingdominoGlobals.getResourceBundleText(KingdominoGlobals.EN_KEY),
						KingdominoGlobals.getResourceBundleText(KingdominoGlobals.FR_KEY) }));
		cb_taal.setItems(talen);
		cb_taal.setValue(taal);

		cb_taal.getSelectionModel().selectedItemProperty().addListener(taalSelectieListener);
	}

	private void setTaal() {
		btn_speel.setText(KingdominoGlobals.getResourceBundleText("btn_speel"));
		btn_registreerSpeler.setText(KingdominoGlobals.getResourceBundleText("btn_registreer"));
		btn_afsluiten.setText(KingdominoGlobals.getResourceBundleText("btn_afsluiten"));
	}

	public void initResizeEvents() {
		Stage stage = (Stage) this.getScene().getWindow();
		stage.widthProperty().addListener((obs, prevVal, newVal) -> resize());
		stage.heightProperty().addListener((obs, prevVal, newVal) -> resize());
	}

	public void resize() {
		KingdominoGlobals.setRootFont(root);

		double scale = this.getScene().getWidth() / KingdominoGlobals.defaultWidth;

		logo.setScaleX(scale);
		logo.setScaleY(scale);
	}

	// ============================================================================================

	@FXML
	private void registreerSpeler(ActionEvent event) {
		Stage stage = (Stage) this.getScene().getWindow();
		this.primaryStage.setWidth(this.getScene().getWindow().getWidth());
		this.primaryStage.setHeight(this.getScene().getWindow().getHeight());

		RegistratiePaginaController controller = new RegistratiePaginaController(this, dc);

		Scene scene = new Scene(controller, this.getScene().getWidth(), this.getScene().getHeight());
		stage.setScene(scene);
		stage.setTitle("Registratie");
		stage.show();

		controller.setResizeEvents();
		controller.resize();
	}

	@FXML
	private void speelSpel(ActionEvent event) {
		Stage stage = (Stage) this.getScene().getWindow();
		this.primaryStage.setWidth(this.getScene().getWindow().getWidth());
		this.primaryStage.setHeight(this.getScene().getWindow().getHeight());

		SelectieSchermController controller = new SelectieSchermController(this, dc);
		Scene scene = new Scene(controller, this.getWidth(), this.getHeight());

		stage.setTitle("Selectie");
		stage.setScene(scene);

		controller.setResizeEvents();
		controller.resize();
	}

	@FXML
	private void sluitAf(ActionEvent event) {
		this.myAudioCue.close();
		this.primaryStage.close();
	}

	private void selectTaal() {
		String cb_taalKeuze = cb_taal.getSelectionModel().getSelectedItem();
		if (cb_taalKeuze != null) {
			if (cb_taalKeuze.equals(KingdominoGlobals.getResourceBundleText(KingdominoGlobals.NL_KEY))) {
				KingdominoGlobals.taal = "nl";
				setTaalBox(KingdominoGlobals.getResourceBundleText(KingdominoGlobals.NL_KEY));
			}
			if (cb_taalKeuze.equals(KingdominoGlobals.getResourceBundleText(KingdominoGlobals.EN_KEY))) {
				KingdominoGlobals.taal = "en";
				setTaalBox(KingdominoGlobals.getResourceBundleText(KingdominoGlobals.EN_KEY));
			}
			if (cb_taalKeuze.equals(KingdominoGlobals.getResourceBundleText(KingdominoGlobals.FR_KEY))) {
				KingdominoGlobals.taal = "fr";
				setTaalBox(KingdominoGlobals.getResourceBundleText(KingdominoGlobals.FR_KEY));
			}
			setTaal();
		}
	}
}
