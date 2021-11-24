package br.com.frontendproject.apis;

import br.com.frontendproject.utils.JsonUtils;

import java.util.List;

import static br.com.frontendproject.utils.PropertieUtils.getValue;
import static io.restassured.RestAssured.given;

public class frontendprojectServiceAPI {

    public String attendancesPost(final String atendimento) {
        final JsonUtils customers = new JsonUtils("src/main/resources/collections/attendances.json");

        final String idUrl = given().accept("*/*").contentType("application/json")
                .header("store-id", customers.getValue("header.store-id"))
                .header("user-id", customers.getValue("header.user-id"))
                .when()
                .get(getValue("api.attendances") + "/v1/attendances/" + atendimento)
                .then().statusCode(200)
                .extract().jsonPath().get("frontend.checklists[0].id");
        return idUrl;
    }

    public String getCustomersDocumentType(final String cpf, final String value) {
        final String path = "/v1/customers/" + cpf + "/attendances";
        final List<String> parametros = given()
                .param("document-type", "CPF")
                .when().get(getValue("api.attendances") + path)
                .then().statusCode(200)
                .extract().jsonPath().getList(value);
        return parametros.get(parametros.size() - 1);
    }

}
