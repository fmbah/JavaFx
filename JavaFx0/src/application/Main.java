package application;
	
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author scott
 */
public class Main extends Application {
	public static final Logger logger = Logger.getLogger("Log");
	public static FileHandler fileHandler;
    
	static {
		try {
			fileHandler = new FileHandler(System.getProperty("user.home") + File.separator + LocalDate.now().toString() + ".log");
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
		} catch (SecurityException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, null, e);
		}
	}
	
	@Override
    public void start(Stage primaryStage) {
        try {
            // Read file fxml and draw interface.
//            Parent root = FXMLLoader.load(getClass().getClassLoader()
//                    .getResource("MyScene.fxml"));
            
            URL location = getClass().getClassLoader().getResource("MyScene.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = fxmlLoader.load(location.openStream());
            MyController control=(MyController)fxmlLoader.getController();
            control.setPrimaryStage(primaryStage);
            
            primaryStage.setTitle("ExcelTools");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            
        } catch(Exception e) {
        	logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void main(String[] args){
        launch(args);
        logger.info("excel tools start....");
    }
}