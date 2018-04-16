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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SaveController extends SplitPane implements Initializable {

	@FXML
	private LineChart<Number, Number> lineChartRange ;
	@FXML
	private LineChart<Number, Number> lineChartPreview ;
	@FXML
	private Slider SliderBegin ;
	@FXML
	private Slider SliderEnd ;
	@FXML
	private Spinner<Double> SpinnerBegin ;
	@FXML
	private Spinner<Double> SpinnerEnd ;

	private Series<Number, Number> series;
	private int size ;
	private int beginIndex ;
	private int endIndex ;
	private ArrayList<Double> xAxisSpinner ;
	final Stage stage = new Stage() ;

	// Constructor
	public SaveController( XYChart.Series<Number, Number> series ) {

		this.series = series ;
		this.size = this.series.getData().size();
		// Double max = Double.parseDouble(this.series.getData().get(this.series.getData().size()-1).getXValue().toString());
		// Double min = Double.parseDouble(this.series.getData().get(0).getXValue().toString());
		// xAxisSize = max.intValue() - min.intValue() ;
		// print(this.series) ;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SpectrumRangeSelector.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		try {

			Parent root = loader.load();
			stage.setTitle("Save");
			stage.setScene(new Scene(root));
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		lineChartRange.getData().add(cloneSeries(this.series)) ;
		lineChartPreview.getData().add(cloneSeries(this.series)) ;

		SliderBegin.setMax(this.size-1);
		SliderEnd.setMax(this.size-1);
		SliderEnd.setValue(this.size-1);

		beginIndex = 0 ;
		endIndex   = this.size - 1 ;

		setSpinnerArray() ;

		// SpinnerBegin.setEditable(true);
		// SpinnerEnd.setEditable(true);

		SpinnerValueFactory<Double> factoryBegin = new SpinnerValueFactory.DoubleSpinnerValueFactory(xAxisSpinner.get(0),
																									 xAxisSpinner.get(xAxisSpinner.size()-1),
																									 xAxisSpinner.get(0)){

			@Override
		    public void decrement(int steps) {

				Double current = this.getValue();
		        int idx = xAxisSpinner.indexOf(current);

				if ( idx == 0 ) {
					; // DO NOTHING
				}

				else {

					int newIdx = (xAxisSpinner.size() + idx - steps) % xAxisSpinner.size();
			        beginIndex = newIdx ;
			        Double newLang = xAxisSpinner.get(newIdx);
			        this.setValue(newLang);

				}

		    }

		    @Override
		    public void increment(int steps) {

		    	Double current = this.getValue();
		        int idx = xAxisSpinner.indexOf(current);

		    	if ( idx == xAxisSpinner.size()-1 ) {
		    		; // DO NOTHING
		    	}

		    	else {
		    		int newIdx = (idx + steps) % xAxisSpinner.size();
		    		beginIndex = newIdx ;
			        Double newLang = xAxisSpinner.get(newIdx);
			        this.setValue(newLang);
		    	}

		    }

		};

		SpinnerBegin.setValueFactory(factoryBegin);

		SpinnerValueFactory<Double> factoryEnd = new SpinnerValueFactory.DoubleSpinnerValueFactory(xAxisSpinner.get(0),
				 																				   xAxisSpinner.get(xAxisSpinner.size()-1),
				 																				   xAxisSpinner.get(xAxisSpinner.size()-1)){

			@Override
		    public void decrement(int steps) {

				Double current = this.getValue();
		        int idx = xAxisSpinner.indexOf(current);

				if ( idx == 0 ) {
					; // DO NOTHING
				}

				else {

					int newIdx = (xAxisSpinner.size() + idx - steps) % xAxisSpinner.size();
			        endIndex = newIdx ;
			        Double newLang = xAxisSpinner.get(newIdx);
			        this.setValue(newLang);

				}

		    }

		    @Override
		    public void increment(int steps) {

		    	Double current = this.getValue();
		        int idx = xAxisSpinner.indexOf(current);

		    	if ( idx == xAxisSpinner.size()-1 ) {
		    		; // DO NOTHING
		    	}

		    	else {
		    		int newIdx = (idx + steps) % xAxisSpinner.size();
			        endIndex = newIdx ;
			        Double newLang = xAxisSpinner.get(newIdx);
			        this.setValue(newLang);
		    	}

		    }

		};

		SpinnerEnd.setValueFactory(factoryEnd);

	}

	public void primaryProcess() {

		SpinnerBegin.getEditor().setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case UP:
                	beginIndex = beginIndex + 1 ;
                	SpinnerBegin.increment(1);
                    break;
                case DOWN:
                	beginIndex = beginIndex - 1 ;
                	SpinnerBegin.decrement(1);
                    break;
				default:
					break;
            }

        });

		SpinnerBegin.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {

			if( !"".equals(newValue) ) {
				SliderBegin.setValue(beginIndex);
			}

	    });

		SpinnerEnd.getEditor().setOnKeyPressed(event -> {


            switch (event.getCode()) {
                case UP:
                	endIndex = endIndex + 1 ;
                	SpinnerEnd.increment(1);
                    break;
                case DOWN:
                	endIndex = endIndex - 1 ;
                	SpinnerEnd.decrement(1);
                    break;
				default:
					break;
            }

        });

		SpinnerEnd.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {

			if ( !"".equals(newValue) ) {
				SliderEnd.setValue(endIndex);
			}

	    });

		// Listen for Slider Begin value changes
		SliderBegin.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {

		    	Double begin = SliderBegin.getValue() ;
		    	Double end   = SliderEnd.getValue() ;

		    	beginIndex = begin.intValue() ;

		    	if ( begin > end ) {
		    		end      = begin + 1 ;
		    		endIndex = end.intValue() ;
		    		SliderEnd.setValue(end);
		    		SpinnerEnd.getValueFactory().setValue(xAxisSpinner.get(endIndex));
		    	}

		    	SpinnerBegin.getValueFactory().setValue(xAxisSpinner.get(beginIndex));
				setPreview(begin, end) ;
		    }
		});

		// Listen for Slider End value changes
		SliderEnd.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {

		    	Double begin = SliderBegin.getValue() ;
		    	Double end   = SliderEnd.getValue() ;

		    	endIndex = end.intValue() ;

		    	if ( begin > end ) {
		    		begin      = end - 1 ;
		    		beginIndex = begin.intValue() ;
		    		SliderBegin.setValue(begin);
		    		SpinnerBegin.getValueFactory().setValue(xAxisSpinner.get(beginIndex));
		    	}

		    	SpinnerEnd.getValueFactory().setValue(xAxisSpinner.get(endIndex));
				setPreview(begin, end) ;
		    }
		});

	}

	@FXML
	private void savefileAsAction(ActionEvent ae) {

		// 得到當前視窗
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

		if(outputFile != null){
			FileDao fileDao = new FileDao(outputFile) ;
			fileDao.write(outputFile, lineChartPreview);
		}

	}

	@FXML
	private void closewindowAsAction(ActionEvent ae) {
		stage.close();
	}

	private void setPreview( Double dbegin, Double dend ) {

		lineChartPreview.getData().clear();

		int begin = dbegin.intValue() ;
		int end   = dend.intValue() ;

		if ( begin <= end ) {

			XYChart.Series<Number, Number> tmpseries = new XYChart.Series<Number, Number>() ;

			for ( int i = begin ; i < end ; i++ ) {

				tmpseries.getData().add( new XYChart.Data<>(
											this.series.getData().get(i).getXValue(),
											this.series.getData().get(i).getYValue()) );

			}

			lineChartPreview.getData().add(tmpseries) ;

		}

		else {
			; // DO NOTHING
		}

	}

	private XYChart.Series<Number, Number> cloneSeries(XYChart.Series<Number, Number> source) {

		XYChart.Series<Number, Number> destination = new XYChart.Series<Number, Number>() ;
		destination.setName("Spetra");

		for( int i = 0 ; i < source.getData().size() ; i++ )
			destination.getData().add(new XYChart.Data<>(
											source.getData().get(i).getXValue(),
											source.getData().get(i).getYValue())) ;

		return destination ;

	}

	private void setSpinnerArray(){

		xAxisSpinner = new ArrayList<>() ;

		for( int i = 0 ; i < size ; i++ ) {

			xAxisSpinner.add(Double.parseDouble(series.getData().get(i).getXValue().toString())) ;

		}

	}

}
