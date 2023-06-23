package com.coda.paytpvdebug;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleCounter;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

@SpringBootApplication
public class PayTpvDebugApplication implements CommandLineRunner {

	private static final String[] COUNTRIES = new String[] {"MY","SG","TH", "PH", "ID", "JP", "KR", "VN"};
	private static final String[] PRODUCTS = new String[] {"product1","product2","product3", "product4", "product5", "product6", "product7", "product8"};

	private static final String[] SUCESS = new String[] {"true", "false"};

	private static final String[] LATENCY_URL = new String[] {"/url1", "/url2", "/url3", "/url4"};

	private static final String[] OBJECT_ARRAY = new String[] {"attrObject1", "attrObject2", "attrObject3", "attrObject4", "attrObject5","attrObject6", "attrObject7", "attrObject8", "attrObject9"};

	private static final String[] PC_CHANNEL = new String[] {"PC1","PC2","PC3", "PC4", "PC5", "PC6", "PC7", "PC8"};

	private static final String METRIC_NAME_COUNT_TRANSACTION="pay.transactions.count.debug";
	private static final String METRIC_NAME_HISTOGRAM_TRANSACTION="pay.transactions.latency.debug";
	private static final String METRIC_NAME_TPV="pay.transactions.tpv.debug";

	public static void main(String[] args) {
		SpringApplication.run(PayTpvDebugApplication.class, args);
	}


	static{
		System.setProperty("otel.exporter.otlp.endpoint", "http://localhost:4318/v1/metrics");

	}

	@Override
	public void run(String... args) throws Exception {

		OpenTelemetry openTelemetry = OpentelemetryConfiguration.initOpenTelemetry();

		Meter meter = openTelemetry.getMeter("io.opentelemetry.example");
		final LongCounter txnCounter = meter.counterBuilder(METRIC_NAME_COUNT_TRANSACTION).build();
		final DoubleCounter tpvCounter = meter.counterBuilder(METRIC_NAME_TPV).ofDoubles().build();
		final LongHistogram latencyHistogram = meter.histogramBuilder(METRIC_NAME_HISTOGRAM_TRANSACTION).ofLongs().setUnit("ms").build();

		 sendPayMetrics(txnCounter, tpvCounter, latencyHistogram);



//		Thread t = new Thread(()-> {sendPayMetrics(txnCounter, tpvCounter, latencyHistogram);});
//		t.run();
//		Thread t1 = new Thread(()-> {sendPayMetrics(txnCounter, tpvCounter, latencyHistogram);});
//		t1.run();
//		Thread t1 = new Thread(()-> {sendPayMetrics();});
//		t1.run();
	}

	public void sendPayMetrics(LongCounter txnCounter, DoubleCounter tpvCounter, LongHistogram latencyHistogram) {

		int metricsCount = 0;

		while(true) {
			Attributes attributesList = null;
			Map<String, String> attributes = getMetricsAttributes();
			try {




				for(Map.Entry<String , String> entry : attributes.entrySet()) {
					Attributes attrObject = Attributes.of(stringKey(entry.getKey()), entry.getValue());
					if(attributesList==null) {
						attributesList = attrObject;
					}
					attributesList = attributesList.toBuilder().putAll(attrObject).build();

				}

				sendTransactionCountMetrics(txnCounter, attributesList);
				sendTransactionLatencyMetrics(latencyHistogram, attributesList);
				sendTPVMetrics(tpvCounter, attributesList);

				try{
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("attributesList: "+attributesList.toString());
				System.out.println("Metric Count : "+metricsCount);
			}
			metricsCount++;


		}
	}

	private void sendTransactionCountMetrics(LongCounter txnCounter, Attributes attributesList) {

		try {
			txnCounter.add(1, attributesList);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private void sendTransactionLatencyMetrics(LongHistogram latencyHistogram, Attributes attributesList) {
		try {
			long latencyTime =  generateRandomLatency();
			latencyHistogram.record(latencyTime, attributesList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendTPVMetrics(DoubleCounter tpvCounter, Attributes attributesList) {
		try {
			tpvCounter.add(generateRandomTPV(), attributesList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> getMetricsAttributes() {
		Map<String, String> attributes = new LinkedHashMap<>();
		String isSuccess = getRandomStatus();
		String product = getRandomProduct();
		String country = getRandomCountry();
		String currency = "USD";
		String pcChannnel= getRandomPC();
		String endpoint = getRandomUrl();

		attributes.put("environment", "staging");

		attributes.put("isSuccess", isSuccess);
		attributes.put("product", product);
		attributes.put("country", country);
		attributes.put("currency", currency);
		attributes.put("endpoint", endpoint);
		attributes.put("pcChannel", pcChannnel);
		attributes.put("dumyAttributeKey1", getRandomIndex());
		attributes.put("dumyAttributeKey2", "dumyAttributeValue2");
		attributes.put("dumyAttributeKey3", "dumyAttributeValue3");
		attributes.put("dumyAttributeKey4", "dumyAttributeValue4");
		attributes.put("dumyAttributeKey5", "dumyAttributeValue5");
		attributes.put("dumyAttributeKey6", "dumyAttributeValue6");
		attributes.put("dumyAttributeKey7", "dumyAttributeValue7");
		attributes.put("dumyAttributeKey8", "dumyAttributeValue8");
		attributes.put("dumyAttributeKey9", "dumyAttributeValue9");
		attributes.put("dumyAttributeKey10", "dumyAttributeValue10");
		attributes.put("dumyAttributeKey11", "dumyAttributeValue11");
		attributes.put("dumyAttributeKey12", "dumyAttributeValue12");
		attributes.put("dumyAttributeKey13", "dumyAttributeValue13");
		attributes.put("dumyAttributeKey14", "dumyAttributeValue14");
		attributes.put("dumyAttributeKey15", "dumyAttributeValue15");
		attributes.put("dumyAttributeKey16", "dumyAttributeValue16");
		attributes.put("dumyAttributeKey17", "dumyAttributeValue17");
		attributes.put("dumyAttributeKey18", "dumyAttributeValue18");
		attributes.put("dumyAttributeKey19", "dumyAttributeValue19");
//		attributes.put("dumyAttributeKey20", "dumyAttributeValue20");
//		attributes.put("dumyAttributeKey21", "dumyAttributeValue21");
//		attributes.put("dumyAttributeKey22", "dumyAttributeValue22");
//		attributes.put("dumyAttributeKey23", "dumyAttributeValue23");
//		attributes.put("dumyAttributeKey24", "dumyAttributeValue24");
//		attributes.put("dumyAttributeKey25", "dumyAttributeValue25");

		return  attributes;
	}

	private double getValues() {
		double rnd = new Random().nextDouble();
		return rnd;
	}

	private static long getValues(int rangeMin, int rangeMax) {
		Random r = new Random();
		long randomValue = rangeMin + (rangeMax - rangeMin) * r.nextLong();
		return randomValue;

	}

	private static String getRandomPC() {
		int rnd = new Random().nextInt(PC_CHANNEL.length);
		return PC_CHANNEL[rnd];

	}

	public static synchronized String getRandomCountry() {
		int rnd = new Random().nextInt(COUNTRIES.length);
		return COUNTRIES[rnd];
	}

	public static synchronized String getRandomProduct() {
		int rnd = new Random().nextInt(PRODUCTS.length);
		return PRODUCTS[rnd];
	}

	public static synchronized String getRandomStatus() {
		int rnd = new Random().nextInt(SUCESS.length);
		return SUCESS[rnd];
	}

	public static synchronized String getRandomUrl() {
		int rnd = new Random().nextInt(LATENCY_URL.length);
		return LATENCY_URL[rnd];
	}

	public static synchronized int generateRandomLatency() {
		Random objGenerator = new Random();
		return objGenerator.nextInt(30000);
	}

	public static synchronized int generateRandomTPV() {
		Random objGenerator = new Random();
		return objGenerator.nextInt(10);
	}

	public static synchronized String getRandomIndex() {
		int rnd = new Random().nextInt(OBJECT_ARRAY.length);
		return OBJECT_ARRAY[rnd];

	}
}
