package ac.simons.neo4j.examples.multidatabase_movies.domain;

import lombok.Getter;

import org.neo4j.springframework.data.core.schema.Id;
import org.neo4j.springframework.data.core.schema.Node;

@Node("Person")
@Getter
public class PersonEntity {

	@Id
	private final String name;

	private final Long born;

	public PersonEntity(Long born, String name) {
		this.born = born;
		this.name = name;
	}
}
