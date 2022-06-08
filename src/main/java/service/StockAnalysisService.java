package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class StockAnalysisService implements Callable<Double> {

	private List<List<String>> stockDetailsList;

	public StockAnalysisService(List<List<String>> stockDetailsList) {
		this.stockDetailsList = stockDetailsList;
	}

	public double getAverageOpeningPrice(List<List<String>> stockDetailsList) {
		stockDetailsList.remove(0);
		Optional<BigDecimal> opSum = stockDetailsList.parallelStream().map(e -> e.get(4)).map(BigDecimal::new)
				.reduce(BigDecimal::add);

		return (opSum.get().doubleValue()) / stockDetailsList.size();
	}

	@Override
	public Double call() throws Exception {
		return Double.valueOf(getAverageOpeningPrice(stockDetailsList));
	}
}
