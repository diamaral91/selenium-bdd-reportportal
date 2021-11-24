package br.com.frontendproject.apis;

import br.com.frontendproject.utils.JsonUtils;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import java.util.List;

import static br.com.frontendproject.utils.PropertieUtils.getValue;
import static io.restassured.RestAssured.given;

public class ChecklistAPI {

    private JsonUtils checklist;
    private JsonPath response;
    private String token;

    public ChecklistAPI() {
        this.checklist = new JsonUtils("src/main/resources/collections/checklist.json");
        this.token = this.gerarToken();
    }

    private String gerarToken() {
        return given()
                .cookie(this.checklist.getValue("token.header.cookie"))
                .header("content-type", this.checklist.getValue("token.header.content-type"))
                .header("authorization", this.checklist.getValue("token.header.authorization"))
                .formParam("grant_type", this.checklist.getValue("token.body.grant_type"))
                .formParam("username", this.checklist.getValue("token.body.username"))
                .formParam("password", this.checklist.getValue("token.body.password"))
                .when().post(getValue("api.token"))
                .then().statusCode(200)
                .extract().path("access_token");
    }

    public ChecklistAPI buscarChecklistCpf(final String cpf) {
        final String get = "/v1/checklists";
        this.response = given()
                .header("Authorization", this.token)
                .header("x-origin", "frontend")
                .param("taxId", cpf)
                .when().get(getValue("api.checklist") + get)
                .then().statusCode(200)
                .extract().jsonPath();
        return this;
    }

    public String getItemID(final String description) {
        final int itemsDescriptionSize = this.response.getList("items.description").size() - 1;
        final String size = Integer.toString(itemsDescriptionSize);
        final List<String> ids = this.response.getList("items[" + size + "].id");
        final List<String> descriptions = this.response.getList("items[" + size + "].description");
        for (int count = 0; count < descriptions.size(); count++) {
            if (description.equalsIgnoreCase(descriptions.get(count))) {
                return ids.get(count);
            }
        }
        return description + " nao encontrado!";
    }

    public ChecklistAPI concluirCheckList(final String codUrl, final String itemId, final String description) {
        final String post = "/v1/checklists/" + codUrl + "/item/" + itemId + "/complete";
        given().accept("*/*").contentType(ContentType.JSON)
                .header("x-origin", "frontend")
                .header("Authorization", this.token)
                .body(this.checklist.getValue(description))
                .when().post(getValue("api.checklist") + post)
                .then().statusCode(200);
        return this;
    }

}
