package ac.simons.neo4j.examples.multidatabase_movies.domain;

import static org.neo4j.springframework.data.core.schema.Relationship.Direction.*;

import lombok.Getter;

import java.util.Set;

import org.neo4j.springframework.data.core.schema.Id;
import org.neo4j.springframework.data.core.schema.Node;
import org.neo4j.springframework.data.core.schema.Property;
import org.neo4j.springframework.data.core.schema.Relationship;

@Node("Movie")
@Getter
public class MovieEntity {

	@Id
	private final String title;

	@Property("tagline")
	private final String description;

	@Relationship(type = "ACTED_IN", direction = INCOMING)
	private Set<PersonEntity> actors;

	@Relationship(type = "DIRECTED", direction = INCOMING)
	private Set<PersonEntity> directors;

	@Relationship(type = "REVIEWED", direction = INCOMING)
	private Set<PersonEntity> reviewers;

	public MovieEntity(String title, String description) {
		this.title = title;
		this.description = description;
	}
}