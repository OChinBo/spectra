package cn.edu.pku.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import cn.edu.pku.dao.FileDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

public class SaveController extends SplitPane implements Initializable {

	@FXML
	private LineChart<Number, Number> lineChartRange;

	@FXML
	private LineChart<Number, Number> lineChartPreview;

	@FXML
	private Slider SliderBegin;

	@FXML
	private Slider SliderEnd;

	@FXML
	private Spinner<Double> SpinnerBegin;

	@FXML
	private Spinner<Double> SpinnerEnd;

	private Series<Number, Number> series;

	private int seriesSize; // Data size of linechart

	private int beginIndex; // Begin index slider and spinner, in order to get
							// the x-axis data from xAxisSpinner

	private int endIndex; // End index slider and spinner, in order to get the
						  // x-axis data from xAxisSpinner

	private ArrayList<Double> xAxisDataArrayList; // Store the x-axis data from

	private static final Double MAXIUM = 100000000000.0 ;

	final Stage stage = new Stage();

	// Constructor
	public SaveController(XYChart.Series<Number, Number> series) {
		this.series = series;
		this.seriesSize = series.getData().size(); // set data size

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SpectrumRangeSelector.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		try {
			Parent root = loader.load();
			stage.setTitle("Save");
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		lineChartRange.getData().add(cloneSeries(this.series)); // show original linechart
		setPreview(0,seriesSize-1); // user-selected linechart


		// Set linechart
		//lineChartRange.setTitle(arg0);

		// Set linechart display
		lineChartRange.setAnimated(false);
		lineChartRange.setCreateSymbols(false);
		lineChartRange.setLegendVisible(false);

		lineChartPreview.setAnimated(false);
		lineChartPreview.setCreateSymbols(false);
		lineChartPreview.setLegendVisible(false);
		lineChartPreview.getXAxis().setAutoRanging(false);;

		// Set slider range and initial value
		SliderBegin.setMax(this.seriesSize - 1);
		SliderEnd.setMax(this.seriesSize - 1);
		SliderEnd.setValue(this.seriesSize - 1);

		// index of linechart's x-axis
		beginIndex = 0;
		endIndex = this.seriesSize - 1;

		// Setup xAxisDataArrayList contents
		xAxisDataArrayList = new ArrayList<>();
		for (int i = 0; i < seriesSize; i++) {
			xAxisDataArrayList.add(Double.parseDouble(series.getData().get(i).getXValue().toString()));
		}

		// Set spinner's textField editable
		// SpinnerBegin.setEditable(true);
		// SpinnerEnd.setEditable(true);

		// Initialize factories of begin and end
		SpinnerValueFactory<Double> factoryBegin = new SpinnerValueFactory.DoubleSpinnerValueFactory(
				xAxisDataArrayList.get(0), xAxisDataArrayList.get(xAxisDataArrayList.size() - 1), xAxisDataArrayList.get(0))
		{
			@Override
			public void decrement(int steps) {
				Double current = this.getValue();
				int index = xAxisDataArrayList.indexOf(current);

				// Prevent the out of range
				// such as, the index of slider is 0 then it can not decrement anymore
				if (index == 0){
					; // Do nothing
				} else {
					// Calculate the next x-axis of linechart point
					int newIndex = (xAxisDataArrayList.size() + index - steps) % xAxisDataArrayList.size();
					beginIndex = newIndex;
					Double newLang = xAxisDataArrayList.get(newIndex);
					this.setValue(newLang);
				}
			}

			@Override
			public void increment(int steps) {
				Double current = this.getValue();
				int index = xAxisDataArrayList.indexOf(current);

				// Prevent the out of range such as, the index of slider is the
				// last then it can not increment anymore
				if (index == xAxisDataArrayList.size() - 1){
					; // Do nothing
				} else {
					// calculate the next x-axis of linechart point
					int newIndex = (index + steps) % xAxisDataArrayList.size();
					beginIndex = newIndex;
					Double newLang = xAxisDataArrayList.get(newIndex);
					this.setValue(newLang);
				}
			}
		};

		SpinnerValueFactory<Double> factoryEnd = new SpinnerValueFactory.DoubleSpinnerValueFactory(
				xAxisDataArrayList.get(0), xAxisDataArrayList.get(xAxisDataArrayList.size() - 1), xAxisDataArrayList.get(xAxisDataArrayList.size() - 1))
		{
			@Override
			public void decrement(int steps) {
				Double current = this.getValue();
				int index = xAxisDataArrayList.indexOf(current);

				// Prevent the out of range
				// such as, the index of slider is the last then it can not decrement anymore.
				if (index == 0){
					; // Do nothing
				} else {
					// calculate the next x-axis of linechart point
					int newIndex = (xAxisDataArrayList.size() + index - steps) % xAxisDataArrayList.size();
					endIndex = newIndex;
					Double newLang = xAxisDataArrayList.get(newIndex);
					this.setValue(newLang);
				}
			}

			@Override
			public void increment(int steps) {
				Double current = this.getValue();
				int index = xAxisDataArrayList.indexOf(current);

				// Prevent the out of range such as,
				// the index of slider is the last then it can not increment anymore.
				if (index == xAxisDataArrayList.size() - 1) {
					; // Do nothing
				} else {
					// calculate the next x-axis of linechart point
					int newIndex = (index + steps) % xAxisDataArrayList.size();
					endIndex = newIndex;
					Double newLang = xAxisDataArrayList.get(newIndex);
					this.setValue(newLang);
				}
			}
		};

		SpinnerBegin.setValueFactory(factoryBegin);
		SpinnerEnd.setValueFactory(factoryEnd);
	}

	public void primaryProcess() {

		// Click up button setup
		SpinnerBegin.getEditor().setOnKeyPressed(event -> {
			switch (event.getCode()) {
				case UP:
				beginIndex = beginIndex + 1;
					System.out.println("up");
					SpinnerBegin.increment(1);
					break;
				case DOWN:
				beginIndex = beginIndex - 1;
					System.out.println("down");
					SpinnerBegin.decrement(1);
					break;
				default:
					break;
			}
		});
		SpinnerEnd.getEditor().setOnKeyPressed(event -> {
			switch (event.getCode()) {
				case UP:
				endIndex = endIndex + 1;
					SpinnerEnd.increment(1);
					System.out.println("up");
					break;
				case DOWN:
				endIndex = endIndex - 1;
					SpinnerEnd.decrement(1);
					System.out.println("down");
					break;
				default:
					break;
			}
		});

		SpinnerBegin.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {

			if (newValue.equals("")) {
				; // DO NOTHING
			} else {
				SliderBegin.setValue(beginIndex);
			}

		});

		SpinnerEnd.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {

			if (newValue.equals("")) {
				; // DO NOTHING
			} else {
				SliderEnd.setValue(endIndex);
			}

		});

		/*
		// Get Spinner's text input and set to closest point
		SpinnerBegin.getEditor().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Double target = Double.parseDouble(SpinnerBegin.getEditor().getText());

				beginIndex = findClosestValue(target, xAxisDataArrayList.size()/2, MAXIUM, xAxisDataArrayList.size()-1) ;
				SpinnerBegin.getValueFactory().setValue(xAxisDataArrayList.get(beginIndex));
				SliderBegin.setValue(beginIndex);
			}
		});
		SpinnerEnd.getEditor().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Double target = Double.parseDouble(SpinnerEnd.getEditor().getText());

				endIndex = findClosestValue(target, xAxisDataArrayList.size()/2, MAXIUM, xAxisDataArrayList.size()-1);
				SpinnerEnd.getValueFactory().setValue(xAxisDataArrayList.get(endIndex));
				SliderEnd.setValue(endIndex);
			}
		});
        */
		/** Listener for Spinner Text*/
		/*
		SpinnerBegin.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			if (spinnerBeginTrigger) {
				spinnerBeginTrigger = false ;
				if (newValue.equals("")) {
					; // DO NOTHING
				} else {
					SliderBegin.setValue(beginIndex);
				}
			} else {
				// spinnerBeginTrigger = true ;
				beginIndex = findClosestValue(xAxisSpinner, Double.parseDouble(newValue), xAxisSpinner.size()/2, MAXIUM, xAxisSpinner.size()-1) ;
				SliderBegin.setValue(beginIndex);
			}
		});

		SpinnerEnd.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			doCommit(SpinnerEnd) ;
			if (spinnerEndTrigger) {
				spinnerEndTrigger = false ;
				System.out.println("text change");
				if (newValue.equals("")) {
					; // DO NOTHING
				} else {
					SliderEnd.setValue(endIndex);
				}
			}
			else {
				// spinnerEndTrigger = true ;
				endIndex = findClosestValue(xAxisSpinner, Double.parseDouble(newValue), xAxisSpinner.size()/2, MAXIUM, xAxisSpinner.size()-1) ;
				SliderEnd.setValue(endIndex);
			}
		});
		*/



		/** Listener for Spinners*/
		/*
		SpinnerBegin.valueProperty().addListener(listener -> {
			beginIndex = findClosestValue(Double.parseDouble(SpinnerBegin.getEditor().getText()), xAxisDataArrayList.size()/2, MAXIUM, xAxisSpinner.size()-1) ;

			SliderBegin.setValue(beginIndex);
		});
		SpinnerEnd.valueProperty().addListener(listener -> {
			endIndex = findClosestValue(Double.parseDouble(SpinnerEnd.getEditor().getText()), xAxisDataArrayList.size()/2, MAXIUM, xAxisSpinner.size()-1) ;
			SliderEnd.setValue(endIndex);
		});*/



		// Listener of Slider's value changes
		SliderBegin.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				int begin = (int) SliderBegin.getValue();
				int end = (int) SliderEnd.getValue();

				//beginIndex = begin.intValue();
				beginIndex = begin ;

				// check begin value is bigger than end value
				// in order to prevent the situation like end value is smaller
				// than begin value
				// so always make sure the end value is bigger than begin value
				if (begin > end) {
					end = begin + 1;
					// endIndex = end.intValue();
					endIndex = end ;
					SliderEnd.setValue(end);
					SpinnerEnd.getValueFactory().setValue(xAxisDataArrayList.get(end));
				}

				SpinnerBegin.getValueFactory().setValue(xAxisDataArrayList.get(begin));

				setPreview(begin, end);
			}
		});
		SliderEnd.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				int begin = (int) SliderBegin.getValue();
				int end = (int) SliderEnd.getValue();

				//endIndex = end.intValue();
				endIndex = end ;

				// check begin value is bigger than end value
				// in order to prevent the situation like end value is smaller
				// than begin value
				// so always make sure the end value is bigger than begin value
				if (begin > end) {
					begin = end - 1;
					//beginIndex = begin.intValue();
					beginIndex = begin ;
					SliderBegin.setValue(begin);
					SpinnerBegin.getValueFactory().setValue(xAxisDataArrayList.get(begin));
				}

				SpinnerEnd.getValueFactory().setValue(xAxisDataArrayList.get(end));

				setPreview(begin, end);
			}
		});

	}

	/*
	    Commit new value, checking conversion to integer,
	    restoring old valid value in case of exception
	 */


	private void doCommit(Spinner<Double> spinner) {

		if (!spinner.isEditable()) return;

	    String text = spinner.getEditor().getText();
	    SpinnerValueFactory<Double> valueFactory = spinner.getValueFactory();

	    if (valueFactory != null) {
	        StringConverter<Double> converter = valueFactory.getConverter();
	        if (converter != null) {
	            try{
	                Double value = converter.fromString(text);
	                valueFactory.setValue(value);
	            } catch(NumberFormatException nfe){
	            	spinner.getEditor().setText(converter.toString(valueFactory.getValue()));
	            }
	        }
	    }
	}

	@FXML
	private void savefileAsAction(ActionEvent ae) {
		// get current window
		Window window = stage.getScene().getWindow();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("."));

		// Set format of filter
		FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All", "*");
		FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(allFilter);
		fileChooser.getExtensionFilters().add(csvFilter);
		fileChooser.getExtensionFilters().add(txtFilter);

		File outputFile = fileChooser.showSaveDialog(window);

		if (outputFile != null) {
			FileDao fileDao = new FileDao(outputFile);
			fileDao.write(outputFile, lineChartPreview);
		}
	}

	@FXML
	private void closewindowAsAction(ActionEvent ae) {
		stage.close();
	}

	/**
	 * set Preview LineChart
	 *
	 * @param dbegin
	 * @param dend
	 */
	private void setPreview(int begin, int end) {

		// clear data of Preview linechart
		lineChartPreview.getData().clear();

//		int begin = dbegin.intValue();
//		int end = dend.intValue();

		XYChart.Series<Number, Number> tmpseries = new XYChart.Series<Number, Number>();
		for (int i = begin; i < end; i++) {
			tmpseries.getData().add(new XYChart.Data<>(this.series.getData().get(i).getXValue(),
					this.series.getData().get(i).getYValue()));
		}

		lineChartPreview.getData().add(tmpseries);
		final NumberAxis xAxis = (NumberAxis) lineChartPreview.getXAxis();

		// Prevent out of bound exception
		if(end != begin) {
			xAxis.setLowerBound(tmpseries.getData().get(0).getXValue().doubleValue());
			xAxis.setUpperBound(tmpseries.getData().get(end-begin-1).getXValue().doubleValue());
		}


	}

	/**
	 * clone series, in order not to change the original series
	 *
	 * @param source
	 * @return XYChart.Series<Number, Number>
	 */
	private XYChart.Series<Number, Number> cloneSeries(XYChart.Series<Number, Number> source) {

		XYChart.Series<Number, Number> destination = new XYChart.Series<Number, Number>();

		for (int i = 0; i < source.getData().size(); i++)
			destination.getData()
					.add(new XYChart.Data<>(source.getData().get(i).getXValue(), source.getData().get(i).getYValue()));

		return destination;

	}

	private int findClosestValue(Double target, int pivotIndex, Double lastDifference, int lastDIndex){

		// count the difference between current value and target value
		Double difference = Math.abs(target - xAxisDataArrayList.get(pivotIndex));
		System.out.println("--------------------------------");
		System.out.println("target:" + target);
		System.out.println("pivotIndex:" + pivotIndex);
		System.out.println("lastDifference:" + lastDifference);
		System.out.println("lastDIndex:" + lastDIndex);
		System.out.println("Difference:" + difference);
		System.out.println("--------------------------------");
		// base case
		if ( difference > lastDifference ) {
			//System.out.println("equal difference " + xAxis.get(pivotIndex));
			return lastDIndex;
		}
		else if ( xAxisDataArrayList.get(pivotIndex) == xAxisDataArrayList.get(lastDIndex) ) {
			//System.out.println("equal value " + xAxis.get(pivotIndex));
			return lastDIndex ;
		}
		else if ( xAxisDataArrayList.get(pivotIndex) < xAxisDataArrayList.get(lastDIndex) ) {
			//System.out.println("less than");
			return findClosestValue(target, pivotIndex/2, difference, xAxisDataArrayList.indexOf(xAxisDataArrayList.get(pivotIndex))) ;
		}
		else {
			//System.out.println("more than");
			return findClosestValue(target, (pivotIndex+xAxisDataArrayList.size())/2, difference, xAxisDataArrayList.indexOf(xAxisDataArrayList.get(pivotIndex))) ;
		}

	}

}
