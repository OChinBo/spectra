package cn.edu.pku.util;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Window;

public class OpenFileUtils {

	/**This function help us to open file by fileChooser(), given by Window and defaultDirectory.
	 * @param window The window of IndexController.
	 * @param defaultDirectory The default directory of IndexController.
	 * @return The file we get from fileChooser().
	 * */
	public static File open(Window window, String defaultDirectory) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(defaultDirectory));

		// Assign selected file to local parameter

		/** If you want to get formats from properties, just changed the annotation of the below line.*/
		//fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("files", PropertiesUtils.readFormats()));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All", "*"));

		File sourceFile = fileChooser.showOpenDialog(window);

		return sourceFile;
	}

}
