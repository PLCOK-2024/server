package com.plcok.common.swagger;

import com.plcok.common.error.ErrorCode;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class ApiExceptionResponseCustomizer implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiExceptionResponse annotation = handlerMethod.getMethodAnnotation(ApiExceptionResponse.class);
        if (annotation != null) {
            for (ErrorCode errorCode : annotation.value()) {
                Schema<?> schema = new Schema<>()
                        .type("object")
                        .addProperties("errorCode", new StringSchema().example(errorCode.getCode()))
                        .addProperties("errorMessage", new StringSchema().example(errorCode.getMessage()));

                ApiResponse apiResponse = new ApiResponse()
                        .description(errorCode.getMessage())
                        .content(new Content().addMediaType("application/json",
                                new MediaType().schema(schema)));

                operation.getResponses().addApiResponse(String.valueOf(errorCode.getStatus()), apiResponse);
            }
        }
        return operation;
    }
}
