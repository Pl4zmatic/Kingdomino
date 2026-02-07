package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import domein.DomeinController;
import domein.DominoTegel;
import dto.DominoTegelDTO;
import dto.SpelerDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SpeltafelController extends BorderPane {

	private BorderPane menu;
	private DomeinController dc;

	private Map<Integer, String> RICHTING_MAP;
	private int huidigeRichtingGetal;
	private String huidigeRichting;

	private MouseEvent prevEvent;

	private Pane[][] spelerAanBeurtGrid;

	private int aantalKoningenGezet;

	private boolean spelStart;

	@FXML
	private Button btn_terugNaarMenu;
	@FXML
	private GridPane grid_kiesKolom;
	@FXML
	private GridPane grid_main;
	@FXML
	private GridPane grid_spelerAanBeurt;
	@FXML
	private BorderPane root;
	@FXML
	private VBox vbox_eindKolom;
	@FXML
	private VBox vbox_spelerWachtrij;
	@FXML
	private VBox vbox_startKolom;
	@FXML
    private TextArea textArea_feedback;
	@FXML
    private Button btn_stopSpel;
	@FXML
    private Label lbl_score;
	@FXML
    private Label lbl_scoreGetal;
	@FXML
	private Button btn_scorebord;

	// ============================================================================================

	public SpeltafelController(BorderPane menu, DomeinController dc) {
		this.menu = menu;
		this.dc = dc;

		KingdominoGlobals.loadFxml("speltafel.fxml", this);
		setControllsSettings();
		setkiesKolom(dc.getStartKolom(), vbox_startKolom, false);
		initSpelerWachtrij();
		setTaal();
		textArea_feedback.appendText(String.format("%s: %s%n", dc.getSpelerAanBeurt().gebruikersnaam(), KingdominoGlobals.getResourceBundleText("text_zetKoning")));

		this.spelStart = true;
		this.RICHTING_MAP = Map.of(1, "rechts", 2, "onder", 3, "links", 4, "boven");
		this.huidigeRichtingGetal = 1;
		this.huidigeRichting = RICHTING_MAP.get(huidigeRichtingGetal);
		this.prevEvent = null;
		this.spelerAanBeurtGrid = new Pane[grid_spelerAanBeurt.getRowCount()][grid_spelerAanBeurt.getColumnCount()];
		this.aantalKoningenGezet = 0;
	}

	protected void setEvents() {
		this.getScene().setOnKeyPressed(event -> veranderRichting(event));
		Stage stage = (Stage) this.getScene().getWindow();
		stage.widthProperty().addListener((obs, prevVal, newVal) -> resize());
		stage.heightProperty().addListener((obs, prevVal, newVal) -> resize());
	}

	private void setkiesKolom(List<DominoTegelDTO> kolomTegels, VBox kolom, boolean wisselEindStartKolom) {
		for (int i = 0; i < kolomTegels.size(); i++) {
			StackPane tegelPane = new StackPane();
			tegelPane.setStyle(String.format(KingdominoGlobals.DOMINOTEGEL_STYLE,
					kolomTegels.get(i).index() < 10 ? "0" : "", kolomTegels.get(i).index()));
			tegelPane.setOnMouseClicked(arg0 -> kiesTegel(arg0));

			if (wisselEindStartKolom)
			{
				tegelPane.getChildren().add(maakSpelerStackPane(dc.getGekozenSpelers().get(i)));
				tegelPane.setDisable(true);
			}

			kolom.getChildren().add(tegelPane);
		}

		kolom.getChildren().forEach(node -> VBox.setVgrow(node, Priority.ALWAYS));
	}

	private void setControllsSettings() {
		Image backButtonGraphic = new Image(getClass().getResourceAsStream("/assets/misc/back-arrow.png"));
		ImageView view = new ImageView(backButtonGraphic);
		view.fitHeightProperty().set(40);
		view.fitWidthProperty().set(40);
		btn_terugNaarMenu.setGraphic(view);
		btn_terugNaarMenu.setContentDisplay(ContentDisplay.LEFT);
	}

	
	private void setSpelerWacthrij() {
			List<SpelerDTO> gekozenSpelers = dc.getGekozenSpelers();
	
			for (SpelerDTO speler : gekozenSpelers) {
				StackPane sp = maakSpelerStackPane(speler);
				if (speler.gekozenTegel() != 0) {
					if (gekozenSpelers.indexOf(speler) == 0) {
						sp.setId("sp_spelerAanBeurt");
						sp.setDisable(false);
					}
				vbox_spelerWachtrij.getChildren().remove(0);
				vbox_spelerWachtrij.getChildren().add(sp);
			}
		}
		vbox_spelerWachtrij.getChildren().forEach(node -> VBox.setVgrow(node, Priority.ALWAYS));
	}

	private void initSpelerWachtrij() {
		List<SpelerDTO> gekozenSpelers = dc.getGekozenSpelers();
		List<Integer> spelerAlInWachtrij = new ArrayList<Integer>();

		gekozenSpelers.forEach(speler -> vbox_spelerWachtrij.getChildren().add(new StackPane()));

		for (SpelerDTO speler : gekozenSpelers) {
			StackPane sp = maakSpelerStackPane(speler);

			int random;
			do {
				random = (int) (Math.random() * gekozenSpelers.size());
			} while (spelerAlInWachtrij.contains(random));

			dc.zetKoning(random + 1, speler.gebruikersnaam());

			if (random == 0) {
				sp.setId("sp_spelerAanBeurt");
				sp.setDisable(false);
			}

			vbox_spelerWachtrij.getChildren().remove(random);
			vbox_spelerWachtrij.getChildren().add(random, sp);

			spelerAlInWachtrij.add(random);
			dc.sorteerGekozenSpelers();
		}

		vbox_spelerWachtrij.getChildren().forEach(node -> VBox.setVgrow(node, Priority.ALWAYS));
	}

	
	private StackPane maakSpelerStackPane(SpelerDTO speler) {
		StackPane sp = new StackPane();
		sp.setStyle(String.format(KingdominoGlobals.STARTTEGEL_STYLE, speler.kleur()));

		Label spelerNaam = new Label(speler.gebruikersnaam());
		spelerNaam.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		spelerNaam.setAlignment(Pos.CENTER);

		sp.getChildren().add(spelerNaam);
		sp.setDisable(true);
		
		sp.setScaleX(0.6);
		sp.setScaleY(0.6);
		
		if(speler.equals(dc.getSpelerAanBeurt()))
		{
			sp.setScaleX(1);
			sp.setScaleY(1);
		}

		return sp;
	}
	

	private void setKoninkrijk(SpelerDTO speler) {
		System.out.println(speler.gebruikersnaam());
		
		for (int i = 0; i < grid_spelerAanBeurt.getRowCount(); i++) {
			for (int j = 0; j < grid_spelerAanBeurt.getColumnCount(); j++) {
				DominoTegel dt = speler.koninkrijk().getKoninkrijkDominoTegel()[i][j];

				Pane pane = spelerAanBeurtGrid[i][j];

				if (pane == null) {
					pane = new Pane();
					spelerAanBeurtGrid[i][j] = pane;
				}

				GridPane.setColumnIndex(pane, j);
				GridPane.setRowIndex(pane, i);
				pane.setRotate(0);

				if (dt != null) {
					if (dt.getVakjes().size() != 1) {
						setPaneImageVoorRichting(pane, dt.getRichting(), i, j, dt.getIndex());
						System.out.printf(" [%2s] ", String.valueOf(dt.getIndex()));
					} else {
						pane.setStyle(String.format(KingdominoGlobals.STARTTEGEL_STYLE, speler.kleur()));
						System.out.printf(" [%2s] ", 0);
					}
				} else {
					pane.setOnMouseEntered(event -> showTegel(event));
					pane.setOnMouseClicked(event -> plaatsTegel(event));
					pane.setStyle("-fx-background-image: none");
					System.out.printf(" [%2s] ", "");
				}

				if (!grid_spelerAanBeurt.getChildren().contains(pane))
					grid_spelerAanBeurt.getChildren().add(pane);

			}
			System.out.println();
		}
		System.out.println("\n\n");
	}
	

	private void setPaneImageVoorRichting(Pane pane, String richting, int gridRij, int gridKolom,
			int dominotegelIndex) {
		pane.setStyle(
				String.format(KingdominoGlobals.DOMINOTEGEL_STYLE, dominotegelIndex < 10 ? "0" : "", dominotegelIndex));

		if (richting.equals("rechts") || richting.equals("links")) {
			GridPane.setColumnSpan(pane, 2);
			GridPane.setRowSpan(pane, 1);
		} else {
			GridPane.setRowSpan(pane, 2);
			GridPane.setColumnSpan(pane, 1);
			pane.setStyle(String.format(KingdominoGlobals.DOMINOTEGEL_VERT_STYLE, dominotegelIndex < 10 ? "0" : "",
					dominotegelIndex));
		}

		if (richting.equals("links") || richting.equals("boven")) {
			Pane pane2 = null;

			if (richting.equals("links")) {
				pane2 = spelerAanBeurtGrid[gridRij][gridKolom - 1];
				pane2.setRotate(180);
				GridPane.setColumnSpan(pane2, 2);
				GridPane.setRowSpan(pane2, 1);
			} else {
				pane2 = spelerAanBeurtGrid[gridRij - 1][gridKolom];
				pane2.setRotate(180);
				GridPane.setRowSpan(pane2, 2);
				GridPane.setColumnSpan(pane2, 1);
			}

			pane2.setStyle(pane.getStyle());
			pane.setStyle("-fx-background-image: none");
		}
	}
	

	private void maakStackPaneTransparent(StackPane pane) {
		pane.getChildren().removeAll(pane.getChildren());
		pane.setStyle("-fx-background-image: none");
	}
	
	
	protected void resize()
	{
		try {
			vbox_spelerWachtrij.setMaxWidth(vbox_spelerWachtrij.getHeight() / dc.getGekozenSpelers().size());
			resizeKiesKolom(vbox_startKolom);
			resizeKiesKolom(vbox_eindKolom);
			grid_spelerAanBeurt.setMinWidth(grid_spelerAanBeurt.getHeight());
			grid_spelerAanBeurt.setMaxWidth(grid_spelerAanBeurt.getHeight());
			
		} catch (NullPointerException e) {
			
		}
		
		KingdominoGlobals.setRootFont(root);
	}
	
	private void resizeKiesKolom(VBox kolom)
	{
		kolom.setMaxWidth(vbox_spelerWachtrij.getHeight() / dc.getGekozenSpelers().size());
		kolom.getChildren().forEach(tegel -> {
			((StackPane)tegel).getChildren().forEach(speler -> {
				((StackPane)speler).setMaxWidth(kolom.getHeight() / dc.getGekozenSpelers().size());
			});
		});
	}
	
	private void setTaal()
	{
		lbl_score.setText(KingdominoGlobals.getResourceBundleText("label_score"));
		btn_stopSpel.setText(KingdominoGlobals.getResourceBundleText("btn_stop"));
		btn_terugNaarMenu.setText(KingdominoGlobals.getResourceBundleText("btn_terug"));
		btn_scorebord.setText(KingdominoGlobals.getResourceBundleText("btn_scorebord"));
	}

	// ============================================================================================

	@FXML
	private void terugNaarMenu(ActionEvent event) {
		dc.resetSpel();
		Stage stage = (Stage) this.getScene().getWindow();
		stage.setScene(menu.getScene());
	}

	private void kiesTegel(MouseEvent event) {		
		StackPane eventSource = (StackPane) event.getSource();
		zetKoning(eventSource);

		boolean isRondeOver = aantalKoningenGezet == dc.getGekozenSpelers().size();
		dc.speelRonde(isRondeOver && !spelStart);
		SpelerDTO spelerAanBeurt = dc.getSpelerAanBeurt();

		if (isRondeOver) {
			if (spelStart) {
				spelerAanBeurt = dc.getSpelerAanBeurt();
				dc.sorteerGekozenSpelers();
				setKoninkrijk(spelerAanBeurt);
				grid_spelerAanBeurt.setDisable(false);
				setkiesKolom(dc.getEindKolom(), vbox_eindKolom, false);
				textArea_feedback.appendText(String.format("%s: %s%n", dc.getSpelerAanBeurt().gebruikersnaam(), KingdominoGlobals.getResourceBundleText("text_plaatsTegel")));
			}

			if (!spelStart) {
				setkiesKolom(dc.getStartKolom(), vbox_startKolom, true);
				vbox_eindKolom.getChildren().clear();
				setkiesKolom(dc.getEindKolom(), vbox_eindKolom, false);
			}
		}
		
		if(spelStart && !isRondeOver)
			textArea_feedback.appendText(String.format("%s: %s%n", dc.getSpelerAanBeurt().gebruikersnaam(), KingdominoGlobals.getResourceBundleText("text_zetKoning")));
		
		if (!spelStart) {
			if(dc.kanGeplaatstWorden(spelerAanBeurt))
			{
				setKoninkrijk(spelerAanBeurt);
				textArea_feedback.appendText(String.format("%s: %s%n", dc.getSpelerAanBeurt().gebruikersnaam(), KingdominoGlobals.getResourceBundleText("text_plaatsTegel")));
			}
			else
			{
				textArea_feedback.appendText(String.format("%s: %s%n", dc.getSpelerAanBeurt().gebruikersnaam(), KingdominoGlobals.getResourceBundleText("text_error_geenOptiesVoorTegel")));
				dc.speelRonde(isRondeOver);
				setKoninkrijk(spelerAanBeurt);
			}
			grid_spelerAanBeurt.setDisable(false);
			vbox_eindKolom.setDisable(true);
		}

		setSpelerWacthrij();
		dc.bepaalScore(spelerAanBeurt);
		spelerAanBeurt = dc.getSpelerAanBeurt();
		lbl_scoreGetal.setText(String.valueOf(spelerAanBeurt.score().get(0)));
		resize();
	}

	private void zetKoning(StackPane eventSource) {
		SpelerDTO spelerAanBeurt = dc.getSpelerAanBeurt();
		eventSource.setDisable(true);

		if (vbox_startKolom.getChildren().contains(eventSource))
			dc.zetKoning(vbox_startKolom.getChildren().indexOf(eventSource) + 1, spelerAanBeurt.gebruikersnaam());
		else
			dc.zetKoning(vbox_eindKolom.getChildren().indexOf(eventSource) + 1, spelerAanBeurt.gebruikersnaam());

		eventSource.getChildren().add(maakSpelerStackPane(spelerAanBeurt));

		aantalKoningenGezet = (aantalKoningenGezet % dc.getGekozenSpelers().size()) + 1;
	}

	private void plaatsTegel(MouseEvent event) {
		SpelerDTO spelerAanBeurt = dc.getSpelerAanBeurt();
		Pane tegelSpook = ((Pane) event.getSource());

		try {
			if (!isBuitenBereik(tegelSpook) && !isOpTegel(tegelSpook)) {
				dc.kiesPlaatsEnRichting(spelerAanBeurt, GridPane.getRowIndex(tegelSpook) + 1,
						GridPane.getColumnIndex(tegelSpook) + 1, huidigeRichting);
				verwijderGeplaatsteTegelUitStartKolom(spelerAanBeurt);
				grid_spelerAanBeurt.setDisable(true);
				vbox_eindKolom.setDisable(false);
				spelStart = false;
				prevEvent = null;
				
				textArea_feedback.appendText(String.format("%s: %s%n", dc.getSpelerAanBeurt().gebruikersnaam(), KingdominoGlobals.getResourceBundleText("text_zetKoning")));
				System.out.println(dc.getGekozenTegel(spelerAanBeurt).toString() + " || " + dc.getGekozenTegel(spelerAanBeurt).index());
				System.out.println(huidigeRichting);
				System.out.println();
				
				if(dc.isSpelEinde())
					stopSpel(null);
				
				dc.bepaalScore(spelerAanBeurt);
				spelerAanBeurt = dc.getSpelerAanBeurt();
				lbl_scoreGetal.setText(String.valueOf(spelerAanBeurt.score().get(0)));
			}
		} catch (IllegalArgumentException e) {
			textArea_feedback.appendText(String.format("%s: %s%n", dc.getSpelerAanBeurt().gebruikersnaam(), KingdominoGlobals.getResourceBundleText("text_error_plaatsTegel")));
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			textArea_feedback.appendText(String.format("%s: %s%n", dc.getSpelerAanBeurt().gebruikersnaam(), KingdominoGlobals.getResourceBundleText("text_error_buitenKoninkrijk")));
		}
	}

	private void showTegel(MouseEvent event) {
		Pane tegelSpook = ((Pane) event.getSource());

		if (!isBuitenBereik(tegelSpook) && !isOpTegel(tegelSpook)) {
			if (prevEvent != null)
				hideTegel(prevEvent);

			prevEvent = event;
			SpelerDTO spelerAanbeurt = dc.getSpelerAanBeurt();
			int dominotegelIndex = dc.getStartKolom().get(spelerAanbeurt.gekozenTegel() - 1).index();
			setPaneImageVoorRichting(tegelSpook, huidigeRichting, GridPane.getRowIndex(tegelSpook),
					GridPane.getColumnIndex(tegelSpook), dominotegelIndex);
		}
	}
	

	private boolean isBuitenBereik(Pane tegel) {
		boolean buitenBereik = false;

		if (huidigeRichting.equals("rechts")) {
			buitenBereik = GridPane.getColumnIndex(tegel) + 1 >= grid_spelerAanBeurt.getColumnCount();
		}

		if (huidigeRichting.equals("links")) {
			buitenBereik = GridPane.getColumnIndex(tegel) - 1 < 0;
		}

		if (huidigeRichting.equals("boven")) {
			buitenBereik = GridPane.getRowIndex(tegel) - 1 < 0;
		}

		if (huidigeRichting.equals("onder")) {
			buitenBereik = GridPane.getRowIndex(tegel) + 1 >= grid_spelerAanBeurt.getRowCount();
		}

		return buitenBereik;
	}
	

	private boolean isOpTegel(Pane tegel) {
		SpelerDTO spelerAanBeurt = dc.getSpelerAanBeurt();
		boolean opTegel = true;
		int rij = GridPane.getRowIndex(tegel);
		int kolom = GridPane.getColumnIndex(tegel);

		if (spelerAanBeurt.koninkrijk().getKoninkrijk()[rij][kolom] == null) {
			if (huidigeRichting.equals("rechts")) {
				opTegel = spelerAanBeurt.koninkrijk().getKoninkrijk()[rij][kolom + 1] != null;
			}

			if (huidigeRichting.equals("links")) {
				opTegel = spelerAanBeurt.koninkrijk().getKoninkrijk()[rij][kolom - 1] != null;
			}

			if (huidigeRichting.equals("boven")) {
				opTegel = spelerAanBeurt.koninkrijk().getKoninkrijk()[rij - 1][kolom] != null;
			}

			if (huidigeRichting.equals("onder")) {
				opTegel = spelerAanBeurt.koninkrijk().getKoninkrijk()[rij + 1][kolom] != null;
			}
		}

		return opTegel;
	}

	private void hideTegel(MouseEvent event) {
		Pane tegelSpook = ((Pane) event.getSource());
		Pane spookCopy = null;

		if (huidigeRichting.equals("links"))
			spookCopy = spelerAanBeurtGrid[GridPane.getRowIndex(tegelSpook)][GridPane.getColumnIndex(tegelSpook) - 1];

		if (huidigeRichting.equals("boven"))
			spookCopy = spelerAanBeurtGrid[GridPane.getRowIndex(tegelSpook) - 1][GridPane.getColumnIndex(tegelSpook)];

		if (spookCopy != null) {
			spookCopy.setStyle("-fx-background-image: none");
			GridPane.setColumnSpan(spookCopy, 1);
			GridPane.setRowSpan(spookCopy, 1);
			spookCopy.setRotate(0);
		}

		tegelSpook.setStyle("-fx-background-image: none");
		GridPane.setColumnSpan(tegelSpook, 1);
		GridPane.setRowSpan(tegelSpook, 1);
	}
	

	private void veranderRichting(KeyEvent event) {
		if (prevEvent != null) {
			Pane eventSource = (Pane) prevEvent.getSource();
			hideTegel(prevEvent);

			if (event.getCode() == KeyCode.RIGHT)
			{
				this.huidigeRichting = "rechts";
				this.huidigeRichtingGetal = 1;
			}
			
			if (event.getCode() == KeyCode.DOWN)
			{
				this.huidigeRichting = "onder";
				this.huidigeRichtingGetal = 2;
			}
			
			if (event.getCode() == KeyCode.LEFT)
			{
				this.huidigeRichting = "links";
				this.huidigeRichtingGetal = 3;
			}
			
			if (event.getCode() == KeyCode.UP)
			{
				this.huidigeRichting = "boven";
				this.huidigeRichtingGetal = 4;
			}

			int prevRichting = this.huidigeRichtingGetal;
			if (isBuitenBereik(eventSource) || isOpTegel(eventSource)) {
				this.huidigeRichtingGetal = prevRichting;
				this.huidigeRichting = RICHTING_MAP.get(this.huidigeRichtingGetal);
			}
			
			if (event.getCode() == KeyCode.R) {
				this.huidigeRichtingGetal = this.huidigeRichtingGetal % (RICHTING_MAP.size()) + 1;
				this.huidigeRichting = RICHTING_MAP.get(this.huidigeRichtingGetal);
				
				if (isBuitenBereik(eventSource) || isOpTegel(eventSource))
				{
					for(Integer richting : RICHTING_MAP.keySet())
					{
						this.huidigeRichtingGetal = this.huidigeRichtingGetal % (RICHTING_MAP.size()) + 1;
						this.huidigeRichting = RICHTING_MAP.get(this.huidigeRichtingGetal);
						if(!isBuitenBereik(eventSource) || !isOpTegel(eventSource))
							break;
						huidigeRichtingGetal = prevRichting;
					}
				}
			}
			showTegel(prevEvent);
		}
	}
	

	private void verwijderGeplaatsteTegelUitStartKolom(SpelerDTO spelerAanBeurt) {
		StackPane tegelVanSpeler = (StackPane) vbox_startKolom.getChildren().get(spelerAanBeurt.gekozenTegel() - 1);
		maakStackPaneTransparent(tegelVanSpeler);
		if (((Pane) vbox_startKolom.getChildren().get(vbox_startKolom.getChildren().size() - 1)).getChildren()
				.isEmpty()) {
			vbox_startKolom.getChildren().removeAll(vbox_startKolom.getChildren());
		}
	}

	
	@FXML
    void stopSpel(ActionEvent event) {
		dc.stopSpel();
		for(SpelerDTO spelerdto : dc.getGekozenSpelers()) {
			dc.verhoogAantalGespeeld(spelerdto);
		}
		for(SpelerDTO spelerdto : dc.geefGewonnenSpelers()) {
			dc.verhoogAantalGewonnen(spelerdto);
		}
		toonScorebord(null);
    }
	
	@FXML
	private void toonScorebord(ActionEvent event)
	{
		Stage stage = (Stage) this.getScene().getWindow();
		
		ScorebordController controller = new ScorebordController(menu, dc, this);
		stage.setScene(new Scene(controller, this.getWidth(), this.getHeight()));
		stage.setTitle(KingdominoGlobals.getResourceBundleText("btn_scorebord"));
		
		controller.setResizeEvents();
		controller.resize();
	}
}
