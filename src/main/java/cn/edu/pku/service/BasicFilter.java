package cn.edu.pku.service;

import java.util.ArrayList;

import cn.edu.pku.entity.tableViewContentEntity;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public abstract class BasicFilter {

	private String id;
	private Series<Number, Number> series;
	protected ArrayList<tableViewContentEntity> input = null ;
	protected ArrayList<tableViewContentEntity> output = null ;

	public BasicFilter(XYChart.Series<Number, Number> series){
		this.series = cloneSeries(series) ;
		this.input  = new ArrayList<tableViewContentEntity>() ;
		this.output = new ArrayList<tableViewContentEntity>() ;
	}

	abstract Series<Number, Number> launch();

	public void fillData() {
		for (int j = 0; j < series.getData().size(); j++) {
			// add data into tableViewContent
			tableViewContentEntity tmp = new tableViewContentEntity(series.getData().get(j).getXValue().toString(),
					series.getData().get(j).getYValue().toString());
			// Add tableViewContent into ObservableList<>
			input.add(tmp);
		}
	}

	public void printInput() {
		for ( int i = 0 ; i < input.size() ; i++ ) {
			System.out.println( i + 1 + ". X: " + input.get(i).getX() + ", Y: " + input.get(i).getY());
		}
	}

	public void printOutput() {
		for ( int i = 0 ; i < output.size() ; i++ ) {
			System.out.println( i + 1 + ". X: " + output.get(i).getX() + ", Y: " + output.get(i).getY());
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

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

}
