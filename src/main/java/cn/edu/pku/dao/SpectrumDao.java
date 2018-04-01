package cn.edu.pku.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cn.edu.pku.controllers.TabController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class SpectrumDao extends Dao {

	// the keyword of @FXML which means mark the specific target(variable) from the fxml file
	@FXML
	private javafx.scene.control.TabPane tabPane;

	public SpectrumDao() {

		;

	} // end of Constructor

	/**
	 * @param file
	 * @return XYChart.Series<Number, Number>
	 *
	 */
	public XYChart.Series<Number, Number> readFileIntoSeries(File file) {

		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("spectra Data");

		try {

			BufferedReader in = new BufferedReader(new FileReader(file));

			// Read a line once a time, until the end of the file which is null
			String tempString = null;

			// when there are no input in the file anymore
			// stop read data from the file
			while ((tempString = in.readLine()) != null) {

				if (!tempString.isEmpty() && Character.isDigit(tempString.charAt(0))) {

					// separate X-Axis and Y-Axis with ','
					String[] tmpString = tempString.split(",");
					series.getData().add(new XYChart.Data<>(Double.parseDouble(tmpString[0]),
							                                Double.parseDouble(tmpString[1]))); // add data into series
				} // end of if

			} // end of while

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		} // end of try/catch

		return series;

	} // end of readFileIntoSeries()

	/**
	 * Write file
	 * @param file
	 *
	 */
	public void writeFile(File file) {

		try {

			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			// 03/16, get the specific linechart from specific tab
			TabController tab = (TabController) tabPane.getSelectionModel().getSelectedItem() ;
			LineChart<Number, Number> linechart = tab.getLineChart() ;

			for (int i = 0; i < linechart.getData().size(); i++) {

				XYChart.Series<Number, Number> series = linechart.getData().get(i);

				for (int j = 0; j < series.getData().size(); j++) {

					// System.out.println(series.getData().get(j).getXValue() + "," + series.getData().get(j).getYValue());
					out.write(series.getData().get(j).getXValue() + "," + series.getData().get(j).getYValue());
					out.newLine();

				} // end of for

			} // end of for

			out.close();

		} catch (IOException e) {

			e.printStackTrace();

		} // end of catch

	} // end of writeFile()

	public void setTabPane( javafx.scene.control.TabPane tabPane ) {

		this.tabPane = tabPane ;

	} // end of setTabPane()

	public javafx.scene.control.TabPane getTabPane() {

		return this.tabPane ;

	} // end of getTabPane()

}
