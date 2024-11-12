package com.project.meetsounds.controlErrores;

import graphql.GraphQLError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @GraphQlExceptionHandler(AliasAlreadyExistsException.class)
    public GraphQLError handleAliasAlreadyExistsException(AliasAlreadyExistsException ex) {
        return GraphQLError.newError()
                .message(ex.getMessage())
                .build();
    }

    @GraphQlExceptionHandler(EmailAlreadyExistsException.class)
    public GraphQLError handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return GraphQLError.newError()
                .message(ex.getMessage())
                .build();
    }

    @GraphQlExceptionHandler(MenorDeEdadException.class)
    public GraphQLError handleMenorDeEdadException(MenorDeEdadException ex) {
        return GraphQLError.newError()
                .message(ex.getMessage())
                .build();
    }

    @GraphQlExceptionHandler(AliasAndEmailAlreadyExistsException.class)
    public GraphQLError handleAliasAndEmailAlreadyExistsException(AliasAndEmailAlreadyExistsException ex) {
        return GraphQLError.newError()
                .message(ex.getMessage())
                .build();
    }

    @GraphQlExceptionHandler(BandaYaExisteException.class)
    public GraphQLError handleBandaYaExisteException(BandaYaExisteException ex) {
        return GraphQLError.newError()
                .message(ex.getMessage())
                .build();
    }
}
