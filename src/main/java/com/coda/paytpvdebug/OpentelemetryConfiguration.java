package com.coda.paytpvdebug;

import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import java.time.Duration;


public class OpentelemetryConfiguration {
    /**
     * Initialize OpenTelemetry.
     *
     * @return a ready-to-use {@link OpenTelemetry} instance.
     *
     */

    private static final String OTEL_EXPORTER_OTLP_ENDPOINT = "OTEL_EXPORTER_OTLP_ENDPOINT";
    private static final String OTEL_EXPORTER_OTLP_ENDPOINT_DEFAULT = "http://nlb-otel-collector-test-bb0f3c3ce3d84746.elb.ap-southeast-1.amazonaws.com:4318";

    static OpenTelemetry initOpenTelemetry() {
        // Include required service.name resource attribute on all spans and metrics

        String OTEL_EXPORTER_ENDPOINT = System.getenv(OTEL_EXPORTER_OTLP_ENDPOINT) == null ? OTEL_EXPORTER_OTLP_ENDPOINT_DEFAULT : System.getenv(OTEL_EXPORTER_OTLP_ENDPOINT);

        Resource resource =
                Resource.getDefault()
                        .merge(Resource.builder().put(SERVICE_NAME, "OtlpExporterExample").build());

        OpenTelemetrySdk openTelemetrySdk =
                OpenTelemetrySdk.builder()
                        .setMeterProvider(
                                SdkMeterProvider.builder()
                                        .setResource(resource)
                                        .registerMetricReader(
                                                PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder()
                                                        .setEndpoint(OTEL_EXPORTER_ENDPOINT)
                                                        // .setCompression("gzip")
                                                        .build())
                                                        .setInterval(Duration.ofMillis(1000))
                                                        .build())
                                        .build())
                        .buildAndRegisterGlobal();

        Runtime.getRuntime().addShutdownHook(new Thread(openTelemetrySdk::close));

        return openTelemetrySdk;
    }
}
