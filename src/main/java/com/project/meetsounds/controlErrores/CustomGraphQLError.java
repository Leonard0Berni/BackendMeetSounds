package com.project.meetsounds.controlErrores;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomGraphQLError implements GraphQLError {

    private String message;
    private List<SourceLocation> locations;

    public CustomGraphQLError(String message) {
        this.message = message;
        this.locations = null; // O puedes asignar una lista vacía
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return locations;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.DataFetchingException;
    }

    @Override
    public Map<String, Object> getExtensions() {
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("customError", message); // Puedes personalizar el mensaje aquí
        return extensions;
    }
}
