package application;
	
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Calendar instance = Calendar.getInstance();
			instance.add(Calendar.YEAR, 1);
			Date now = new Date();
			if (now.before(instance.getTime())) {
				URL location = getClass().getClassLoader().getResource("scene.fxml");
	            FXMLLoader fxmlLoader = new FXMLLoader();
	            fxmlLoader.setLocation(location);
	            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
	            Parent root = fxmlLoader.load(location.openStream());
	            MajorController control=(MajorController)fxmlLoader.getController();
	            control.setPrimaryStage(primaryStage);
	            
	            primaryStage.setTitle("transform excel tools by fmbah");
	            primaryStage.setScene(new Scene(root));
	            primaryStage.setMaxHeight(500);
	            primaryStage.setMinHeight(500);
	            primaryStage.setMaxWidth(700);
	            primaryStage.setMinWidth(700);
	            primaryStage.show();
			} else {
	            BorderPane root = new BorderPane();
				Scene scene = new Scene(root,400,400);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.setTitle("Be overdue");
				primaryStage.show();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
