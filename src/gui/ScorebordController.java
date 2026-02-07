package gui;

import java.util.Comparator;
import java.util.List;

import domein.DomeinController;
import domein.Speler;
import dto.SpelerDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ScorebordController extends GridPane {
	BorderPane menu;
	DomeinController dc;
	BorderPane speltafel;
	
	private int HBOX_MARGIN = 10;
	
	@FXML
	private VBox vbox_scorebord;
	@FXML
	private RowConstraints grid_row_vbox;
	@FXML
	private GridPane root;
	@FXML
	private Button btn_terug;
	
	public ScorebordController(BorderPane menu, DomeinController dc, BorderPane speltafel) {
		this.dc = dc;
		this.menu = menu;
		this.speltafel = speltafel;
		KingdominoGlobals.loadFxml("Scorebord.fxml", this);
		
		setScoreBord();
		setTaal();
	}
	
	protected void setResizeEvents() {
		Stage stage = (Stage) this.getScene().getWindow();
		stage.widthProperty().addListener((obs, prevVal, newVal) -> resize());
		stage.heightProperty().addListener((obs, prevVal, newVal) -> resize());
	}
	
	protected void resize() {
		for(Node child : vbox_scorebord.getChildren())
		{
			if(child instanceof HBox)
			{
				HBox box = (HBox)child;
				StackPane spelerImage = (StackPane) box.getChildren().get(0);
				TilePane tp = (TilePane) box.getChildren().get(1);
				
				spelerImage.setMaxWidth(spelerImage.getHeight());
				tp.setMaxWidth(vbox_scorebord.getWidth() - spelerImage.getMaxWidth());
			}
		}
		
		KingdominoGlobals.setRootFont(root);
	}

	private void setTaal()
	{
		btn_terug.setText(KingdominoGlobals.getResourceBundleText("btn_terug"));
	}
	
	private void setScoreBord()
	{
		List<SpelerDTO> spelers = dc.geefGewonnenSpelers();
		
		for (SpelerDTO speler : spelers) {
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER);
			hbox.setSpacing(HBOX_MARGIN);
			hbox.getStyleClass().add("hbox");
			VBox.setVgrow(hbox, Priority.ALWAYS);
			
			StackPane spelerImage = new StackPane();
			spelerImage.setStyle(String.format(KingdominoGlobals.STARTTEGEL_STYLE, speler.kleur()));
			HBox.setHgrow(spelerImage, Priority.ALWAYS);
			spelerImage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			HBox.setMargin(spelerImage, new Insets(HBOX_MARGIN));
			hbox.getChildren().add(spelerImage);
			
			Label spelerNaam = new Label(speler.gebruikersnaam());
			spelerNaam.setAlignment(Pos.CENTER);
			spelerNaam.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			spelerNaam.getStyleClass().add("spelerNaam");
			spelerImage.getChildren().add(spelerNaam);
			
			TilePane tp = new TilePane();
			tp.setAlignment(Pos.CENTER);
			for(int i = 0; i < speler.score().size(); i++)
			{
				VBox box = new VBox();
				Label lbl = null;
				Label lblGetal;
				
				lblGetal = new Label(String.valueOf(speler.score().get(i)));
				
				if(i == 0)
					lbl = new Label(KingdominoGlobals.getResourceBundleText("label_score"));
					
				if(i == 1)	
					lbl = new Label(KingdominoGlobals.getResourceBundleText("label_aantalVakken"));
					
				if(i == 2)
					lbl = new Label(KingdominoGlobals.getResourceBundleText("label_aantalKronen"));
					
				box.getChildren().addAll(lbl, lblGetal);
				box.setAlignment(Pos.CENTER);
				tp.getChildren().add(box);
			}
			hbox.getChildren().add(tp);
			vbox_scorebord.getChildren().add(hbox);
		}
	}
	
	@FXML
	private void terug(ActionEvent event)
	{
		dc.sorteerGekozenSpelers();
		Stage stage = (Stage) this.getScene().getWindow();
		if(dc.isSpelEinde())
			stage.setScene(menu.getScene());
		else stage.setScene(speltafel.getScene());
	}

	
}
