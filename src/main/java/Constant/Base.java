package Constant;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static Constant.Endpoints.*;
import static io.restassured.RestAssured.given;

public class Base {

    public static RequestSpecification spec() {

        return given().log().all()

                .contentType(ContentType.JSON)

                .baseUri(BASE_URI)

                .basePath(API);

    }
}