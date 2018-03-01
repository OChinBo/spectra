package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.sun.glass.ui.Window;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

public class IndexController {

	private File sourceFile;

	// @FXML
	// private javafx.scene.control.MenuItem closeButton;

	@FXML
	private void closeWindowsAction() {
		// System.exit(0);
		Platform.exit();
	}

	/***** MenuBar Buttons *****/
	@FXML
	private void openFileAction(ActionEvent ae) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");

		// Assign selected file to local parameter
		sourceFile = fileChooser.showOpenDialog(null);

		// System.out.print(sourceFile);
		readFileByLines(sourceFile);

	}

	@FXML
	private void saveAsAction(ActionEvent ae) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");

		// Set format of filter
		FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(csvFilter);
		fileChooser.getExtensionFilters().add(txtFilter);

		sourceFile = fileChooser.showSaveDialog(null);

		// System.out.print(sourceFile);
	}

	private void readFileByLines(File file) {
		//Not Completed
		try{
			BufferedReader  in = new BufferedReader(new FileReader(file));
			//Scanner in = new Scanner(new FileReader(file));

			// 一次读入一行，直到读入null为文件结束
			int line = 1;
			String tempString = null;

			//跳過第一行文檔標頭
			System.out.println(in.readLine());

			while ((tempString = in.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				line++;
			}

			in.close();

		}catch(IOException e){
			e.printStackTrace();
		}


	}

	private void showChart(){
		// Not Completed
	}
}
