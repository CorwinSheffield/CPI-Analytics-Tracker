package com.sheffieldcorwin.analyticstracker.service;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.sheffieldcorwin.analyticstracker.dto.CPISeriesResults;
import com.sheffieldcorwin.analyticstracker.dto.CPIApiResponse;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
public class CPIClient {

	private static final Logger log = LogManager.getLogger();
	private final WebClient webClient;
	private final String apiKey;

	public CPIClient(WebClient.Builder builder, @Value("${bls.apikey}") String apiKey) {
		this.webClient = builder.baseUrl("https://api.bls.gov/publicAPI/v2").build();
		this.apiKey = apiKey;
	}

	public CPISeriesResults fetchSingleSeriesData(String seriesId) {
		CPIApiResponse response = webClient.get()
				.uri(uri -> uri.path("/timeseries/data/{seriesId}")
						.queryParam("registrationkey", apiKey)
						.build(seriesId))
				.retrieve()
				.bodyToMono(CPIApiResponse.class).block();
		
		
		if (response == null || !response.status().equals("REQUEST_SUCCEEDED")) {
			log.error("Request failed or response was null.");
			return new CPISeriesResults(List.of());
		}
	
		log.info("Status of request: {}", response.status());
		
		return response.results();
	}
	
	public CPISeriesResults fetchSingleSeriesData(String seriesId, int startYear, int endYear) {
		return fetchMultipleSeries(List.of(seriesId), startYear, endYear).block();
	}

	public Mono<CPISeriesResults> fetchLatestSingleSeriesData(String seriesId) {
		return webClient.get()
				.uri(uriBuilder -> {
					URI uri = uriBuilder.path("/timeseries/data/{seriesId}")
						.queryParam("latest", "true")
						.queryParam("registrationkey", apiKey)
						.build(seriesId);
					log.info("Requesting URI: {}" , uri );
					return uri;
				}).retrieve()
				.bodyToMono(CPIApiResponse.class)
				.retryWhen(Retry
						.backoff(4, Duration.ofHours(1))
						.filter(throwable -> throwable instanceof WebClientResponseException ex && ex.getStatusCode().is5xxServerError())
						.doBeforeRetry(signal -> log.warn("Retrieval of series {} data failed: {}", seriesId, signal.failure() )))
				.map(response -> {
					if (response == null || !"REQUEST_SUCCEEDED".equals(response.status())) {
						return new CPISeriesResults(List.of());
					}
					return response.results();
				}).onErrorReturn(new CPISeriesResults(List.of()));
	}

	public Mono<CPISeriesResults> fetchMultipleSeries(List<String> seriesIds) {
		log.info("Series requested for multi-fecth: {}",seriesIds);
		Map<String, Object> body = Map.of("seriesid", seriesIds, "registrationkey",apiKey);
		return webClient
				.post()
				.uri("/timeseries/data/")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.retrieve()
				.bodyToMono(CPIApiResponse.class).flatMap(response -> {
					if (response == null || !response.status().equals("REQUEST_SUCCEEDED")) {
						return Mono.just(new CPISeriesResults(List.of()));
					}
					return Mono.just(response.results());
				});
	}

	public Mono<CPISeriesResults> fetchMultipleSeries(List<String> seriesIds, int startYear, int endYear) {
		log.info("Series requested for multi-fecth: {}",seriesIds);
		Map<String, Object> body = Map.of("seriesid", seriesIds, "registrationkey",apiKey, "startyear",startYear,"endyear",endYear);
		return webClient
				.post()
				.uri("/timeseries/data/")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.retrieve()
				.bodyToMono(CPIApiResponse.class).flatMap(response -> {
					if (response == null || !response.status().equals("REQUEST_SUCCEEDED")) {
						return Mono.just(new CPISeriesResults(List.of()));
					}
					return Mono.just(response.results());
				});
	}

}
