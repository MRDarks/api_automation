package org.gt.projects.api.core;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.JsonConfig;
import io.restassured.http.Headers;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.ValidatableResponse;

public interface PostRequest<R, S> {

    String getURL();

    Headers getJsonHeaders();

    Class<S> getResponseClazz();

    default S executeApiPostRequest(R requestBody){
        ValidatableResponse response = RestAssured.given().log().all().auth().preemptive().basic("","").
                config(
                        RestAssured.config().
                                encoderConfig(new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)).
                                jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL))
                ).
                relaxedHTTPSValidation().headers(getJsonHeaders()).body(requestBody).post(getURL()).then().log().all();

        return response.extract().as(getResponseClazz());
    }
}
