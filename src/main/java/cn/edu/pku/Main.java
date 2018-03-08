package cn.edu.pku;
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

			/** Project Structure Example:
			 *
			 * 	src/main/java
			 * 		|
			 * 		cn.edu.pku
			 * 			|
			 * 			controllers
			 * 				|
			 * 				Screen1controller.java
			 * 				Screen2controller.java
			 * 			service
			 * 				|
			 * 				Service1.java
			 * 			dao(persist)
			 * 				|
			 * 				SaveProducts.java
			 * 			Main.java
			 *
			 * 	src/main/resources
			 * 		|
			 * 		view
			 * 			|
			 * 			screen1.fxml
			 * 			screen2.fxml
			 * 		css
			 * 			|
			 * 			style.css
			 * 		images
			 * 			img1.jpg
			 * 			img2.jpg
			 *
			 * */


			/** Remind:
			 *  current path = src/main/java/cn/edu/pku(WeAreHere)
			 * */

			final String __fxmlpath = "/view/Index.fxml";
			final String __csspath = "/css/application.css";
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
