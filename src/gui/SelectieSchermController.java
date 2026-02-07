package gui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

import domein.DomeinController;
import dto.SpelerDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.Kleuren;

public class SelectieSchermController extends BorderPane{
	
	private BorderPane menu;
	private DomeinController dc;
	
	private ObservableList<SpelerDTO> alleGebruikers;
	
	private Deque<SpelerDTO> gekozenSpelers;
	private Deque<Kleuren> gekozenKleuren;
	
	private boolean isInVoegToeMode = false;
	
	@FXML
    private BorderPane root;	
	@FXML
    private Button btn_annuleer;
    @FXML
    private Button btn_verwijder;
    @FXML
    private Button btn_voegToe;
    @FXML
    private Button btn_terugNaarMenu;
    @FXML
    private GridPane grid_geselecteerdeSpelers;    
    @FXML
    private ColumnConstraints grid_imageColumn_1;
    @FXML
    private ColumnConstraints grid_imageColumn_2;
    @FXML
    private RowConstraints grid_imageRow_1;
    @FXML
    private RowConstraints grid_imageRow_2;
    @FXML
    private TableView<SpelerDTO> table_gebruikers;    
    @FXML
    private TableColumn<SpelerDTO, Integer> tableColumn_geboortejaar;
    @FXML
    private TableColumn<SpelerDTO, String> tableColumn_gebruikersnaam;    
    @FXML
    private StackPane sp_blauw;
    @FXML
    private StackPane sp_geel;
    @FXML
    private StackPane sp_groen;
    @FXML
    private StackPane sp_rood;    
    @FXML
    private Button btn_blauw;
    @FXML
    private Button btn_geel;
    @FXML
    private Button btn_groen;
    @FXML
    private Button btn_rood;    
    @FXML
    private Button btn_startSpel;
    
    //============================================================================================
	
	public SelectieSchermController(BorderPane menu, DomeinController dc)
	{
		this.menu = menu;
		this.dc = dc;
		this.gekozenSpelers = new ArrayDeque<SpelerDTO>();
		this.gekozenKleuren = new ArrayDeque<Kleuren>();
		KingdominoGlobals.loadFxml("selectieScherm.fxml", this);
		setTable();
		setTaal();
		table_gebruikers.getSelectionModel().selectedItemProperty().addListener((event) -> spelerGeselecteerd());
	}
	
	private void setTaal()
	{
		tableColumn_geboortejaar.setText(KingdominoGlobals.getResourceBundleText("label_geboortejaar"));
		tableColumn_gebruikersnaam.setText(KingdominoGlobals.getResourceBundleText("label_gebruikersnaam"));
		btn_voegToe.setText(KingdominoGlobals.getResourceBundleText("btn_voeg_toe"));
		btn_verwijder.setText(KingdominoGlobals.getResourceBundleText("btn_verwijder"));
		btn_annuleer.setText(KingdominoGlobals.getResourceBundleText("btn_annuleer"));
		btn_startSpel.setText(KingdominoGlobals.getResourceBundleText("btn_start"));
		btn_terugNaarMenu.setText(KingdominoGlobals.getResourceBundleText("btn_terug"));
		btn_blauw.setText(KingdominoGlobals.getResourceBundleText("label_speler"));
		btn_geel.setText(KingdominoGlobals.getResourceBundleText("label_speler"));
		btn_groen.setText(KingdominoGlobals.getResourceBundleText("label_speler"));
		btn_rood.setText(KingdominoGlobals.getResourceBundleText("label_speler"));
	}
	
	public void setResizeEvents()
	{
		Stage stage = (Stage) this.getScene().getWindow();
		stage.widthProperty().addListener((obs, prevVal, newVal) -> resize());
		stage.heightProperty().addListener((obs, prevVal, newVal) -> resize());
	}
	
	private void getAlleGebruikers()
	{
		List<SpelerDTO> gebruikers = dc.geefAlleGebruikers();
		this.alleGebruikers = FXCollections.observableArrayList(gebruikers);
	}
	
	private void setTable()
	{
		tableColumn_geboortejaar.setCellValueFactory(new PropertyValueFactory<SpelerDTO, Integer>("geboortejaar"));
		tableColumn_gebruikersnaam.setCellValueFactory(new PropertyValueFactory<SpelerDTO, String>("gebruikersnaam"));
		
		getAlleGebruikers();
		
		table_gebruikers.setItems(alleGebruikers);
	}
	
	
	protected void resize()
	{
		KingdominoGlobals.setRootFont(root);
		
		grid_imageRow_1.setMaxHeight(sp_blauw.getWidth());
		grid_imageRow_2.setMaxHeight(grid_imageRow_1.getMaxHeight());
	}
	
	private void spelerGeselecteerd()
	{
		if(gekozenSpelers.size() < 4)
		{
			isInVoegToeMode = true;
			btn_voegToe.setDisable(false);
		}
	}
    
    //============================================================================================
    
    @FXML
    private void terugNaarMenu(ActionEvent event) {
    	Stage stage = (Stage) this.getScene().getWindow();
    	stage.setScene(menu.getScene());
    }
    
    @FXML
    private void startSpel(ActionEvent event)
    {
    	dc.voegSpelersToeEnMaakSpel(new ArrayList<SpelerDTO>(gekozenSpelers), new ArrayList<Kleuren>(gekozenKleuren));
    	dc.zetSpelOp(gekozenSpelers.size());
    	
    	SpeltafelController stc = new SpeltafelController(menu, dc);
    	
    	Stage stage = (Stage) this.getScene().getWindow();
    	Scene scene = new Scene(stc, this.getWidth(), this.getHeight());
    	stage.setTitle("Kingdomino");
    	stage.setScene(scene);
    	stc.setEvents();
    	stc.resize();
    }
    
    @FXML
    void verwijderSpeler(ActionEvent event) {
    	btn_annuleer.setOnAction(e -> annuleerVerwijder());
    }

    @FXML
    void voegSpelerToe(ActionEvent event) {
    	SpelerDTO gekozenSpeler = table_gebruikers.getSelectionModel().getSelectedItem();
    	this.gekozenSpelers.offer(gekozenSpeler);
    	
    	btn_annuleer.setOnAction(e -> annuleerVoegToe());
    	
    	alleGebruikers.remove(gekozenSpeler);
    	table_gebruikers.setItems(alleGebruikers);
    	
    	setDisables();
    	table_gebruikers.setDisable(true);
    	btn_voegToe.setDisable(true);
    	btn_annuleer.setDisable(false);
    }
    
    private void setDisables()
    {
    	for(Node s : grid_geselecteerdeSpelers.getChildren())
    	{
    		if(s instanceof StackPane)
    		{
    			s.setDisable(!s.isDisabled());
    		}
    	}
    	
    	if(gekozenKleuren.size() >= 3) btn_startSpel.setDisable(false);
    	table_gebruikers.setDisable(false);
    	btn_annuleer.setDisable(true);
    }
    
    
    private void annuleerVoegToe() {
    	alleGebruikers.add(gekozenSpelers.pollLast());
    	gekozenKleuren.pollLast();
    	
    	table_gebruikers.setItems(alleGebruikers);
    	
    	setDisables();
    	table_gebruikers.setDisable(false);
    }
    
    private void annuleerVerwijder()
    {
    	
    }

    @FXML
    void selecteerBlauw(ActionEvent event) {
    	verwijderGeselecteerdeSpeler(btn_rood, sp_rood, Kleuren.ROOS);
    	selecteerKleur(btn_blauw, sp_blauw, Kleuren.BLAUW);
    }

    @FXML
    void selecteerGeel(ActionEvent event) {
    	verwijderGeselecteerdeSpeler(btn_rood, sp_rood, Kleuren.ROOS);
    	selecteerKleur(btn_geel, sp_geel, Kleuren.GEEL);
    }

    @FXML
    void selecteerGroen(ActionEvent event) {
    	verwijderGeselecteerdeSpeler(btn_rood, sp_rood, Kleuren.ROOS);
    	selecteerKleur(btn_groen, sp_groen, Kleuren.GROEN);
    }

    @FXML
    void selecteerRood(ActionEvent event) {
    	verwijderGeselecteerdeSpeler(btn_rood, sp_rood, Kleuren.ROOS);
    	selecteerKleur(btn_rood, sp_rood, Kleuren.ROOS);
    }
    
    private void selecteerKleur(Button btn, StackPane sp, Kleuren kleur)
    {
    	if(isInVoegToeMode)
    	{
    		btn.setText(gekozenSpelers.peekLast().gebruikersnaam());
        	this.gekozenKleuren.offer(kleur);
        	
        	setDisables();
        	sp.setDisable(false);
        	
        	btn.setStyle("-fx-background-color: rgba(255,255,255, 0.8)");
        	table_gebruikers.getSelectionModel().clearSelection();
        	isInVoegToeMode = false;
    	}
    }
    
    private void verwijderGeselecteerdeSpeler(Button btn, StackPane sp, Kleuren kleur)
    {
    	if(!isInVoegToeMode)
    	{
    		SpelerDTO teVerwijderenSpeler = gekozenSpelers.stream()
    				.filter(speler -> speler.gebruikersnaam()
    						.equals(btn.getText()))
    				.findFirst()
    				.get();
    		alleGebruikers.add(teVerwijderenSpeler);
			gekozenSpelers.remove(teVerwijderenSpeler);
			table_gebruikers.setItems(alleGebruikers);
    		btn.setText("Speler");
    	}
    }
}
