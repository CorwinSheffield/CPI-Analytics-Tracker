# CPI Analytics Tracker

This is a Spring Boot project I made to fetch, analyze, and persist various sectors from the Consumer Price Index (CPI) listed by the Bureau of Labor Statistics. The goal of the project is to demonstrate clean design, querying, and give various real time analytics about the data captured. 

## Features
- Scheduled, parallelized ingestion of CPI data from the BLS API
- Keeps track of fetched data to avoid duplication
- Error and success logging of automated job, as well as retries for the automated process
- REST endpoints that support manual data fetching, analytics, and a full-text search of the PostGreSQL database
- Provides metrics: month-over-month, year-over-year, average, cumulative, and standard deviation for inflation

## Stack
- **Java + Spring Boot** for the service layer
- **PostgreSQL** for persistence
- **Spring Data JPA** for repositories
- **Log4j2** for logging
- **Maven** for build and execution

## Project Highlights
- Asynchronous ingestion using `Executor` framework and `CompletableFuture`
- Reactive Endpoints for large sets of data
- Layered Architecture with clear separation of concerns
- Transactional Persistence for data integrity

## How it works:
Manual requests and the scheduled ingestion job fetch data from the BLS and persist it to the database. You can also query which series are available series by name or series ID.

Users can send REST requests to the `/analytics` endpoint to receive calculations on the latest data or a chosen set of months for inflation month over month, year over year, average or cumulative, and standard deviation.

