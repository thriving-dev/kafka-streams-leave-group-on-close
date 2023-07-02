# kafka-streams-leave-group-on-close

![Cover image showing the Kafka log, a stopwatch, suggesting 'kafka-streams partition rebalance downtime' to be reduced from 45s to 100ms](./docs/leave-group-on-close_header.webp)

Application + docker-compose setup that was put together for blog post   
https://thriving.dev/blog/reduce-rebalance-downtime-for-stateless-kafka-streams-apps

Demo video: https://youtu.be/oMWjwslhuGY

## Get Started

### build Quarkus modules

In a new terminal (1), from repository root, exec

```bash
cd dummy-producer && ./gradlew clean build
```

In a new terminal (2), from repository root, exec

```bash
cd kstreams-stateless-logger && ./gradlew clean build
```

### Start compose stack

In a new terminal (3), from repository root, run

```bash
docker-compose up --build -d
```

### Open Grafana Dashboard

Visit http://localhost:3030/ - signin with user+password `admin:admin`.

A basic dashboard with panels for replicas, consumer lag, message rate should show as the default board.

### Simulation: Re-create containers

Streams application logs, filtered by "REBALANCING" (start one for each app)
```bash
docker logs -f kafka-streams-leave-group-on-close-kstreams-stateless-logger-1 |grep "REBALANCING"
```
```bash
docker logs -f kafka-streams-leave-group-on-close-logger-leave-group-1 |grep "REBALANCING"
```

Simultaneously restart ("re-create") containers `kafka-streams-leave-group-on-close-kstreams-stateless-logger-3` & `kafka-streams-leave-group-on-close-logger-leave-group-3` with a 15s sleep in-between.
```bash
{ docker stop kafka-streams-leave-group-on-close-kstreams-stateless-logger-3 & docker stop kafka-streams-leave-group-on-close-logger-leave-group-3 & } && \
  sleep 15 && \
  { docker start kafka-streams-leave-group-on-close-kstreams-stateless-logger-3 & docker start kafka-streams-leave-group-on-close-logger-leave-group-3 & }
```

### Cleanup
```bash
docker-compose down
```
