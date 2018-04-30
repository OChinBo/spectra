package cn.edu.pku.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import cn.edu.pku.dao.FileDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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

	private int size; // Data size of linechart

	private int beginIndex; // Begin index slider and spinner, in order to get
							// the x-axis data from xAxisSpinner

	private int endIndex; // End index slider and spinner, in order to get the
						  // x-axis data from xAxisSpinner

	private ArrayList<Double> xAxisSpinner; // Store the x-axis data from
											// linechart(series)

	private boolean spinnerBeginTrigger ;
	private boolean spinnerEndTrigger ;
	private static final Double MAXIUM = 100000000000.0 ;

	final Stage stage = new Stage();

	// Constructor
	public SaveController(XYChart.Series<Number, Number> series) {

		this.series = series;
		this.size = this.series.getData().size(); // set data size

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

		// Initialize
		spinnerBeginTrigger = false ;
		spinnerEndTrigger   = false ;

		lineChartRange.getData().add(cloneSeries(this.series)); // show original linechart
		lineChartPreview.getData().add(cloneSeries(this.series)); // user-selected linechart

		// Set linechart
		//lineChartRange.setTitle(arg0);

		// Set linechart display
		lineChartRange.setAnimated(false);
		lineChartRange.setCreateSymbols(false);
		lineChartRange.setLegendVisible(false);

		lineChartPreview.setAnimated(false);
		lineChartPreview.setCreateSymbols(false);
		lineChartPreview.setLegendVisible(false);

		// Set slider range and initial value
		SliderBegin.setMax(this.size - 1);
		SliderEnd.setMax(this.size - 1);
		SliderEnd.setValue(this.size - 1);

		// index of linechart's x-axis
		beginIndex = 0;
		endIndex = this.size - 1;

		setSpinnerArray(); // set an array for spinner

//		SpinnerBegin.setEditable(true);
//		SpinnerEnd.setEditable(true);

		SpinnerValueFactory<Double> factoryBegin = new SpinnerValueFactory.DoubleSpinnerValueFactory(
				xAxisSpinner.get(0), xAxisSpinner.get(xAxisSpinner.size() - 1), xAxisSpinner.get(0))
		{

			@Override
			public void decrement(int steps) {

				Double current = this.getValue();
				int index = xAxisSpinner.indexOf(current);

				// prevent the out of range
				// such as, the index of slider is 0 then it can not decrement
				// anymore
				if (index == 0) {
					; // DO NOTHING
				}

				else {

					// calculate the next x-axis of linechart point
					int newIndex = (xAxisSpinner.size() + index - steps) % xAxisSpinner.size();
					beginIndex = newIndex;
					Double newLang = xAxisSpinner.get(newIndex);
					this.setValue(newLang);

				}

			}

			@Override
			public void increment(int steps) {

				Double current = this.getValue();
				int index = xAxisSpinner.indexOf(current);

				// prevent the out of range such as, the index of slider is the
				// last then it can not increment anymore
				if (index == xAxisSpinner.size() - 1) {
					; // DO NOTHING
				}

				else {

					// calculate the next x-axis of linechart point
					int newIndex = (index + steps) % xAxisSpinner.size();
					beginIndex = newIndex;
					Double newLang = xAxisSpinner.get(newIndex);
					this.setValue(newLang);

				}

			}

		};

		SpinnerBegin.setValueFactory(factoryBegin);

		SpinnerValueFactory<Double> factoryEnd = new SpinnerValueFactory.DoubleSpinnerValueFactory(xAxisSpinner.get(0),
				xAxisSpinner.get(xAxisSpinner.size() - 1), xAxisSpinner.get(xAxisSpinner.size() - 1))
		{

			@Override
			public void decrement(int steps) {

				// Prevent the out of range such as,
				// the index of slider is the last then it can not
				// decrement anymore.
				Double current = this.getValue();
				int index = xAxisSpinner.indexOf(current);

				if (index == 0) {
					; // DO NOTHING
				}

				else {

					// calculate the next x-axis of linechart point
					int newIndex = (xAxisSpinner.size() + index - steps) % xAxisSpinner.size();
					endIndex = newIndex;
					Double newLang = xAxisSpinner.get(newIndex);
					this.setValue(newLang);

				}

			}

			@Override
			public void increment(int steps) {

				Double current = this.getValue();
				int index = xAxisSpinner.indexOf(current);

				// Prevent the out of range such as,
				// the index of slider is the last then it can not increment
				// anymore.
				if (index == xAxisSpinner.size() - 1) {
					; // DO NOTHING
				}

				else {

					// calculate the next x-axis of linechart point
					int newIndex = (index + steps) % xAxisSpinner.size();
					endIndex = newIndex;
					Double newLang = xAxisSpinner.get(newIndex);
					this.setValue(newLang);

				}

			}

		};

		SpinnerEnd.setValueFactory(factoryEnd);

	}

	public void primaryProcess() {

//		// Click up button of Begin Spinner
//		SpinnerBegin.getEditor().setOnKeyPressed(event -> {
//
//			switch (event.getCode()) {
//				case UP:
//					spinnerBeginTrigger = true ;
//					beginIndex = beginIndex + 1;
//					System.out.println("up");
//					SpinnerBegin.increment(1);
//					break;
//				case DOWN:
//					spinnerBeginTrigger = true ;
//					beginIndex = beginIndex - 1;
//					System.out.println("down");
//					SpinnerBegin.decrement(1);
//					break;
//				default:
//					break;
//			}
//
//		});

//		SpinnerBegin.valueProperty().addListener(listener -> {
//			beginIndex = findTheMinClosedValue(xAxisSpinner, Double.parseDouble(SpinnerBegin.getEditor().getText()), xAxisSpinner.size()/2, MAXIUM, xAxisSpinner.size()-1) ;
//			SliderBegin.setValue(beginIndex);
//		});9

//		SpinnerBegin.getEditor().setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				beginIndex = findTheMinClosedValue(xAxisSpinner, Double.parseDouble(SpinnerBegin.getEditor().getText()), xAxisSpinner.size()/2, MAXIUM, xAxisSpinner.size()-1) ;
//				SliderBegin.setValue(beginIndex);
//			}
//
//		});

		// Listen for Begin Spinner Text(Value) Change
		SpinnerBegin.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
//
//			if (spinnerBeginTrigger) {
//
//				spinnerBeginTrigger = false ;
//
				if (newValue.equals("")) {
					; // DO NOTHING
				}

				else { // !newValue.equals("")
					SliderBegin.setValue(beginIndex);
				}
//
//			}
//
//			else {
//
//				// spinnerBeginTrigger = true ;
//				beginIndex = findTheMinClosedValue(xAxisSpinner, Double.parseDouble(newValue), xAxisSpinner.size()/2, MAXIUM, xAxisSpinner.size()-1) ;
//				SliderBegin.setValue(beginIndex);
//
//			}
//
		});

//		// Click up button of End Spinner
//		SpinnerEnd.getEditor().setOnKeyPressed(event -> {
//
//			switch (event.getCode()) {
//				case UP:
//					spinnerEndTrigger = true ;
//					endIndex = endIndex + 1;
//					SpinnerEnd.increment(1);
//					System.out.println("up");
//					break;
//				case DOWN:
//					spinnerEndTrigger = true ;
//					endIndex = endIndex - 1;
//					SpinnerEnd.decrement(1);
//					System.out.println("down");
//					break;
//				default:
//					break;
//			}
//
//		});

//		SpinnerEnd.valueProperty().addListener(listener -> {
//			endIndex = findTheMinClosedValue(xAxisSpinner, Double.parseDouble(SpinnerEnd.getEditor().getText()), xAxisSpinner.size()/2, MAXIUM, xAxisSpinner.size()-1) ;
//			SliderEnd.setValue(endIndex);
//		});


		/*
		    Commit new value, checking conversion to integer,
		    restoring old valid value in case of exception
		*/

		// Listen for End Spinner Text(Value) Change
		SpinnerEnd.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {

//			doCommit(SpinnerEnd) ;

//
//			if (spinnerEndTrigger) {
//
//				spinnerEndTrigger = false ;
//
//				System.out.println("text change");
//
				if (newValue.equals("")) {
					; // DO NOTHING
				}

				else { // !newValue.equals("")
					SliderEnd.setValue(endIndex);
				}
//
//			}
//
//			else {
//
//				// spinnerEndTrigger = true ;
//				endIndex = findTheMinClosedValue(xAxisSpinner, Double.parseDouble(newValue), xAxisSpinner.size()/2, MAXIUM, xAxisSpinner.size()-1) ;
//				SliderEnd.setValue(endIndex);
//
//			}
//
		});

		// Listen for Slider Begin value changes
		SliderBegin.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				Double begin = SliderBegin.getValue();
				Double end = SliderEnd.getValue();

				beginIndex = begin.intValue();

				// check begin value is bigger than end value
				// in order to prevent the situation like end value is smaller
				// than begin value
				// so always make sure the end value is bigger than begin value
				if (begin > end) {
					end = begin + 1;
					endIndex = end.intValue();
					SliderEnd.setValue(end);
					spinnerEndTrigger = true ;
					SpinnerEnd.getValueFactory().setValue(xAxisSpinner.get(endIndex));
				}

				spinnerBeginTrigger = true ;
				SpinnerBegin.getValueFactory().setValue(xAxisSpinner.get(beginIndex));
				setPreview(begin, end); // set preview linechart(user-selected
										// linechart)
			}
		});

		// Listen for Slider End value changes
		SliderEnd.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				Double begin = SliderBegin.getValue();
				Double end = SliderEnd.getValue();

				endIndex = end.intValue();

				// check begin value is bigger than end value
				// in order to prevent the situation like end value is smaller
				// than begin value
				// so always make sure the end value is bigger than begin value
				if (begin > end) {
					begin = end - 1;
					beginIndex = begin.intValue();
					SliderBegin.setValue(begin);
					spinnerBeginTrigger = true ;
					SpinnerBegin.getValueFactory().setValue(xAxisSpinner.get(beginIndex));
				}

				spinnerEndTrigger = true ;
				SpinnerEnd.getValueFactory().setValue(xAxisSpinner.get(endIndex));
				setPreview(begin, end); // set preview linechart(user-selected
										// linechart)
			}
		});

	}

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
	private void setPreview(Double dbegin, Double dend) {

		// clear data of Preview linechart
		lineChartPreview.getData().clear();

		int begin = dbegin.intValue();
		int end = dend.intValue();

		XYChart.Series<Number, Number> tmpseries = new XYChart.Series<Number, Number>();

		for (int i = begin; i < end; i++) {

			tmpseries.getData().add(new XYChart.Data<>(this.series.getData().get(i).getXValue(),
					this.series.getData().get(i).getYValue()));

		}

		final NumberAxis xAxis = (NumberAxis) lineChartPreview.getXAxis();

		lineChartPreview.getData().add(tmpseries);
		xAxis.setLowerBound(Double.parseDouble(this.series.getData().get(begin).getXValue().toString()));

	}

	/**
	 * clone series, in order not to change the original series
	 *
	 * @param source
	 * @return XYChart.Series<Number, Number>
	 */
	private XYChart.Series<Number, Number> cloneSeries(XYChart.Series<Number, Number> source) {

		XYChart.Series<Number, Number> destination = new XYChart.Series<Number, Number>();
		// destination.setName("Spetra");

		for (int i = 0; i < source.getData().size(); i++)
			destination.getData()
					.add(new XYChart.Data<>(source.getData().get(i).getXValue(), source.getData().get(i).getYValue()));

		return destination;

	}

	private void setSpinnerArray() {

		xAxisSpinner = new ArrayList<>();

		for (int i = 0; i < size; i++) {

			xAxisSpinner.add(Double.parseDouble(series.getData().get(i).getXValue().toString()));

		}

	}

	private int findTheMinClosedValue(ArrayList<Double> xAxis, Double target, int pivotIndex, Double lastDifference, int lastDIndex){

		// count the difference between current value and target value
		Double difference = Math.abs(target-xAxis.get(pivotIndex));

		// base case
		if ( difference > lastDifference ) {
			System.out.println("equal difference");
			return lastDIndex;
		}

		else if ( xAxis.get(pivotIndex) == xAxis.get(lastDIndex) ) {
			System.out.println("equal value");
			return lastDIndex ;
		}

		else if ( xAxis.get(pivotIndex) < xAxis.get(lastDIndex) ) {
			System.out.println("less than");
			return findTheMinClosedValue(xAxis, target, pivotIndex/2, difference, xAxis.indexOf(xAxis.get(pivotIndex))) ;
		}

		else {
			System.out.println("more than");
			return findTheMinClosedValue(xAxis, target, (pivotIndex+xAxis.size())/2, difference, xAxis.indexOf(xAxis.get(pivotIndex))) ;
		}

	}

}
