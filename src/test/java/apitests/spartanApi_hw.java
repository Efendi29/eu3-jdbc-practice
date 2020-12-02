package apitests;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class spartanApi_hw {
    @BeforeClass
    public void beforeclass(){
        baseURI= ConfigurationReader.get("spartan_api_url");
    }

    /*
    Q1:
Given accept type is json
And path param id is 20
When user sends a get request to "/api/spartans/{id}"
Then status code is 200
And content-type is "application/json;charset=UTF-8"
And response header contains Date
And Transfer-Encoding is chunked
And response payload values match the following:
id is 20,
name is "Lothario",
gender is "Male",
phone is 7551551687
     */
    @Test
    public void testQ1(){

        Response response = given().accept(ContentType.JSON)
                .and().pathParam("id", 20)
                .when().get("/api/spartans/{id}");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json;charset=UTF-8");

        assertTrue(response.headers().hasHeaderWithName("Date"));
        assertEquals(response.header("Transfer-Encoding"),"chunked");

        JsonPath jsonPath = response.jsonPath();

        int ID = jsonPath.getInt("id");
        String NAME = jsonPath.getString("name");
        String GENDER = jsonPath.getString("gender");
        long PHONE = jsonPath.getLong("phone");

        assertEquals(ID,20);
        assertEquals(NAME,"Lothario");
        assertEquals(GENDER,"Male");
        assertEquals(PHONE,7551551687L);

    }


    /*
    Q2:
Given accept type is json
And query param gender = Female
And queary param nameContains = r
When user sends a get request to "/api/spartans/search"
Then status code is 200
And content-type is "application/json;charset=UTF-8"
And all genders are Female
And all names contains r
And size is 20
And totalPages is 1
And sorted is false
     */

    @Test
    public void testQ2(){
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("gender","Female");
        queryMap.put("nameContains","r");

        Response response = given().accept(ContentType.JSON)
                .and().queryParams(queryMap)
                .when().get("/api/spartans/search");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json;charset=UTF-8");

        JsonPath jsonPath = response.jsonPath();

        List<String> GENDERS = jsonPath.getList("content.gender");
        for (String gender : GENDERS) {
            assertEquals(gender,"Female");
        }

        List<String> NAMES = jsonPath.getList("content.name");
        for (String name : NAMES) {
           assertTrue(name.toLowerCase().contains("r"));
        }

        int totalPages = jsonPath.getInt("totalPages");
        assertEquals(totalPages,1);

        boolean SORTED = jsonPath.getBoolean("sort.sorted");
        assertFalse(SORTED);

    }

}





