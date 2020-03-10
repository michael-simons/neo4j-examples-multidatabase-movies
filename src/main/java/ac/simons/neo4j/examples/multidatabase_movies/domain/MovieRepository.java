package ac.simons.neo4j.examples.multidatabase_movies.domain;

import reactor.core.publisher.Mono;

import org.neo4j.springframework.data.repository.ReactiveNeo4jRepository;

public interface MovieRepository extends ReactiveNeo4jRepository<MovieEntity, String> {

	Mono<MovieEntity> findOneByTitle(String title);
}
