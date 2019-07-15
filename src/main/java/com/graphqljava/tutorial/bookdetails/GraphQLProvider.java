package com.graphqljava.tutorial.bookdetails;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;

@Component
public class GraphQLProvider {

	private GraphQL graphQL;

	@Autowired
	GraphQLDataFetchers graphQLDataFetchers;

	@Bean
	public GraphQL GraphQLProvider() {
		return graphQL;
	}

	@PostConstruct
	public void init() throws Exception{
		URL url = Resources.getResource("schemaTest.graphql");
		String sdl = Resources.toString(url, Charsets.UTF_8);
		GraphQLSchema graphQLSchema = buildSchema(sdl);
		this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
	}

	private GraphQLSchema buildSchema(String sdl) {
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
		RuntimeWiring runtimeWiring = buildWiring();
		SchemaGenerator schemaGenerator = new SchemaGenerator();
		return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
	}

	private RuntimeWiring buildWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type(TypeRuntimeWiring.newTypeWiring("Query")
						.dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
				.type(TypeRuntimeWiring.newTypeWiring("Book")
						.dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher())
						// This line is new: we need to register the additional DataFetcher
				//		.dataFetcher("pageCount", graphQLDataFetchers.getPageCountDataFetcher())
				)
				.build();


	}
}
