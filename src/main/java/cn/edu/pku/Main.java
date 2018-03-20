package cn.edu.pku;

import cn.edu.pku.controllers.IndexController;
import cn.edu.pku.controllers.TabController;
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

			// Resources Paths
			final String __indexpath = "/view/Index.fxml";
			final String __csspath = "/css/application.css";
			final String __iconpath = "/images/chicken.png";

			// Load root scene
			FXMLLoader indexLoader = new FXMLLoader(getClass().getResource(__indexpath));
			Parent root = indexLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource(__csspath).toExternalForm());

			// Set icon and title
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(__iconpath)));
			primaryStage.setTitle("PKU Raman Spectrum");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		} // end of try catch

	} // end of start()

	public static void main(String[] args) {

		launch(args);

	} // end of main()

}
