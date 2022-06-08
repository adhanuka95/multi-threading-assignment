package controller;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import parser.CsvParser;
import service.StockAnalysisService;

public class StockAnalysisController {

	private List<File> filePaths;
	private String dirPath;

	protected ExecutorService parserExecutorService;
	protected ExecutorService avgCalculatorExecutorService;
	protected ExecutorService executorService;

	public StockAnalysisController(String dirPath) {
		this.dirPath = dirPath;
		listAllFiles();
	}

	public boolean startEngine() {
		parserExecutorService = Executors.newFixedThreadPool(10);
		avgCalculatorExecutorService = Executors.newFixedThreadPool(10);
		executorService = Executors.newFixedThreadPool(10);
		return true;
	}

	public boolean stopEngine() {
		parserExecutorService.shutdown();
		avgCalculatorExecutorService.shutdown();
		executorService.shutdown();
		return true;
	}

	private void listAllFiles() {
		this.filePaths = Stream.of(new File(dirPath).listFiles()).filter(file -> !file.isDirectory())
				.collect(Collectors.toList());
	}

	public void getAverageOpeningPricePerStock() {
		filePaths.parallelStream().forEach(file -> {
			Future<Double> result = null;
			result = executorService.submit(new Callable<Double>() {
				@Override
				public Double call() throws Exception {
					return doAction(file);
				}
			});
			
			try {
				System.out.println(String.format("%10s: %.2f",
						file.getName().substring(0, file.getName().lastIndexOf('.')), result.get()));
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
	}
	
	private Double doAction(File file) throws InterruptedException, ExecutionException {
		
		Future<List<List<String>>> stockDataFuture = (Future<List<List<String>>>) getFileParsed(file);
		while (!stockDataFuture.isDone()) {
		}
		Future<Double> avgPriceFuture = (Future<Double>) getAverageOpeningPrice(stockDataFuture.get());
		while (!avgPriceFuture.isDone()) {
		}
		return avgPriceFuture.get();
	}

	private Future<?> getFileParsed(final File file) {
		return parserExecutorService.submit(new CsvParser(file));
	}

	private Future<?> getAverageOpeningPrice(List<List<String>> data) {
		return avgCalculatorExecutorService.submit(new StockAnalysisService(data));
	}

}
