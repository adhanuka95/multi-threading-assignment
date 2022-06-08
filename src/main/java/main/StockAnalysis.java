package main;

import controller.StockAnalysisController;

public class StockAnalysis {

	public static void main(String[] args) {
		String dir = "C:/Documents/SocGen Trainings/Big Data Learning Program/Multi threading/JavaAssignment";
		StockAnalysisController sc = new StockAnalysisController(dir);
		sc.startEngine();
		sc.getAverageOpeningPricePerStock();
		sc.stopEngine();
	}

}
