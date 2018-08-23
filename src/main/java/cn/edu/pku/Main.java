package cn.edu.pku;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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

			// Show start up animation
//			showStartUp(primaryStage);

			// Resources Paths
			final String __indexpath = "/view/Index.fxml";
			final String __iconpath = "/images/chicken.png";

			// Load root scene
			FXMLLoader indexLoader = new FXMLLoader(getClass().getResource(__indexpath));
			Parent root = indexLoader.load();
			Scene scene = new Scene(root);

			// Set CSS Files
			String cssFile1 = this.getClass().getResource("/css/application.css").toExternalForm();
			String cssFile2 = this.getClass().getResource("/css/tab.css").toExternalForm();
			scene.getStylesheets().addAll(cssFile1,cssFile2);


			// Set icon and title
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(__iconpath)));
			primaryStage.setTitle("PKU Raman Spectrum Viewer");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		launch(args);

	}


	/** Not Finished Yet
	 * */
	private void showStartUp(Stage primaryStage){
		StackPane root = new StackPane();

		// Set collaborators icon
		Image co1 = new Image(getClass().getResourceAsStream("/images/co1.png"));
		Image co2 = new Image(getClass().getResourceAsStream("/images/co2.png"));
		Image co3 = new Image(getClass().getResourceAsStream("/images/co3.png"));
		ImageView iv1 = new ImageView();
		ImageView iv2 = new ImageView();
		ImageView iv3 = new ImageView();
		iv1.setImage(co1);
		iv2.setImage(co2);
		iv3.setImage(co3);
		root.getChildren().addAll(iv1, iv2, iv3);

		// Prepare animate
		FadeTransition ft = new FadeTransition(Duration.seconds(10), root);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();

		Scene scene = new Scene(root, 600, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
