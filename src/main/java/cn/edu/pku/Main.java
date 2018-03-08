package main.java.cn.edu.pku;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;


public class Main extends Application {


	@Override
	public void start(Stage primaryStage) {
		try {

			/**
			 * Remind:
			 * current path = src/main/java/cn/edu/pku/WeAreHere
			 * */

			final String __fxmlpath = "../../../../resources/view/Index.fxml";
			final String __csspath = "../../../../resources/css/application.css";
			final String __iconpath = "src/main/resources/images/chicken.png";

			// Load root scene
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(__fxmlpath));
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource(__csspath).toExternalForm());

			// Set icon and title
			primaryStage.getIcons().add(new Image("file:" + __iconpath));
			primaryStage.setTitle("PKU Raman Spectrum");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}




}
