package gui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.ResourceBundle.Control;

import domein.DomeinController;
import domein.Speler;
import dto.SpelerDTO;
import exceptions.GebruikersnaamInGebruikException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegistratiePaginaController extends BorderPane{
	
	private BorderPane menu;
	private DomeinController dc;
	
	private ObservableList<SpelerDTO> alleGebruikers;
	
	@FXML
    private BorderPane root;
	@FXML
    private Button btn_sluit;
    @FXML
    private Button btn_voegToe;
    @FXML
    private GridPane mainGrid;
    @FXML
    private TextField txtField_geboortejaar;
    @FXML
    private TextField txtField_gebruikersnaam;
    @FXML
    private Label lbl_error;
    @FXML
    private Label lbl_geboortejaar;
    @FXML
    private Label lbl_gebruikersnaam;
    @FXML
    private TableView<SpelerDTO> table_gebruikers;   
    @FXML
    private TableColumn<SpelerDTO, Integer> tableColumn_geboortejaar;
    @FXML
    private TableColumn<SpelerDTO, String> tableColumn_gebruikersnaam;
    
    //============================================================================================
	
	public RegistratiePaginaController(BorderPane menu, DomeinController dc)
	{
		this.menu = menu;
		this.dc = dc;
		KingdominoGlobals.loadFxml("registratiePagina.fxml", this);
		setControllsSettings();
		setTable();
		setTaal();
	}
	
	private void setTaal()
	{
		btn_sluit.setText(KingdominoGlobals.getResourceBundleText("btn_terug"));
		btn_voegToe.setText(KingdominoGlobals.getResourceBundleText("btn_voeg_toe"));
		tableColumn_geboortejaar.setText(KingdominoGlobals.getResourceBundleText("label_geboortejaar"));
		tableColumn_gebruikersnaam.setText(KingdominoGlobals.getResourceBundleText("label_gebruikersnaam"));
		lbl_gebruikersnaam.setText(KingdominoGlobals.getResourceBundleText("label_gebruikersnaam"));
		lbl_geboortejaar.setText(KingdominoGlobals.getResourceBundleText("label_geboortejaar"));
	}
	
	private void setControllsSettings()
	{
		lbl_error.setVisible(false);
	}
	
	private void setTable()
	{
		tableColumn_geboortejaar.setCellValueFactory(new PropertyValueFactory<SpelerDTO, Integer>("geboortejaar"));
		tableColumn_gebruikersnaam.setCellValueFactory(new PropertyValueFactory<SpelerDTO, String>("gebruikersnaam"));
		
		getAlleGebruikers();
		
		table_gebruikers.setItems(alleGebruikers);
	}
	
	private void getAlleGebruikers()
	{
		List<SpelerDTO> gebruikers = dc.geefAlleGebruikers();
		this.alleGebruikers = FXCollections.observableArrayList(gebruikers);
	}
	
	public void setResizeEvents()
	{
		Stage stage = (Stage) this.getScene().getWindow();
		stage.widthProperty().addListener((obs, prevVal, newVal) -> resize());
		stage.heightProperty().addListener((obs, prevVal, newVal) -> resize());
	}
	
	public void resize()
	{
		KingdominoGlobals.setRootFont(root);
	}

	//============================================================================================

    @FXML
    void registreerSpeler(ActionEvent event) {
    	String gebruikersnaam = "";
    	lbl_error.setVisible(false);
    	
    	try {
    		gebruikersnaam = txtField_gebruikersnaam.getText();
	    	int geboortejaar = Integer.parseInt(txtField_geboortejaar.getText());
	    	
	    	dc.registreerSpeler(gebruikersnaam, geboortejaar);
		} catch (IllegalArgumentException | GebruikersnaamInGebruikException e) {
			lbl_error.setVisible(true);
			lbl_error.setText("Error!");
			
			Tooltip.install(lbl_error,
					new Tooltip(e.getMessage()));
			
			for(SpelerDTO s : alleGebruikers)
			{
				if(s.gebruikersnaam().equals(gebruikersnaam))
				{
					table_gebruikers.scrollTo(s);
					table_gebruikers.requestFocus();
					table_gebruikers.getSelectionModel().select(s);
					break;
				}
			}
			
			System.out.println(String.format("%n%s%n", e.getMessage()));
		}
    	txtField_geboortejaar.clear();
    	txtField_gebruikersnaam.clear();
    	
    	getAlleGebruikers();
    	table_gebruikers.setItems(alleGebruikers);
    }

    @FXML
    void sluitAf(ActionEvent event) {
    	Stage stage = (Stage) this.getScene().getWindow();
    	stage.setScene(menu.getScene());
    }
}
