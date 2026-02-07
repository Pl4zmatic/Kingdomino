package gui;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class KingdominoGlobals {
	
	public static int rootFont = 12;
	public static int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	public static int defaultWidth = 900;
	public static int defaultHeight = 900;
	public static String taal = "nl";
	public static final String NL_KEY = "taal_nl";
	public static final String EN_KEY = "taal_en";
	public static final String FR_KEY = "taal_fr";
	public static final String DOMINOTEGEL_STYLE = "-fx-background-image: url('file:src/assets/tegels/dominotegel/tegel_%s%d_voorkant.png')";
	public static final String DOMINOTEGEL_VERT_STYLE = "-fx-background-image: url('file:src/assets/tegels/dominotegel_vert/tegel_%s%d_voorkant_vert.png')";
	public static final String STARTTEGEL_STYLE = "-fx-background-image: url('file:src/assets/tegels/starttegel/starttegel_%s.png')";
	
	
	public static void loadFxml(String fxmlPath, Object o) {
		FXMLLoader loader = new FXMLLoader(o.getClass().getResource(fxmlPath));
		loader.setRoot(o);
		loader.setController(o);

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void setRootFont(Pane root)
    {
    	if(root.getWidth() < 700)
		{
    		root.setStyle("-fx-font-size:12");
    		rootFont = 12;
		}
		
		if(root.getWidth() >= 700 && root.getWidth() < 1500)
		{
			root.setStyle("-fx-font-size:18");
			rootFont = 18;
		}
		
		if(root.getWidth() >= 1500)
		{
			root.setStyle("-fx-font-size:24");
			rootFont = 24;
		}
    }
	
	protected static String getResourceBundleText(String key)
	{
		Locale locale = new Locale(taal);
		ResourceBundle resourceBundle = ResourceBundle.getBundle("/cfg/resource_bundle", locale);
		return resourceBundle.getString(key);
	}
}
