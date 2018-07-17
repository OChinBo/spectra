package cn.edu.pku.service;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class DifferenceFilter extends BasicFilter {

	public DifferenceFilter() {
		super();
	}

	@Override
	public Series<Number, Number> launch(Series<Number, Number> series) {
		Series<Number, Number> outputseries = new XYChart.Series<Number, Number>();
		outputseries.setName("Difference series");
		Double x = 0.0 ;
		Double y = 0.0 ;

		this.series = series;
		fillData();

		for ( int i = 0 ; i < input.size()-1 ; i++ ) {

			x = x + (Double.parseDouble(input.get(i+1).getX()) - Double.parseDouble(input.get(i).getX()));
			y = y + (Double.parseDouble(input.get(i+1).getY()) - Double.parseDouble(input.get(i).getX()));

			outputseries.getData().add(new XYChart.Data<>(x,y));
		}
		return outputseries ;
	}


}
