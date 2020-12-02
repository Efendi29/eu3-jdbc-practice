package apitests;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class ordsApi_hw {

    @BeforeClass
    public void beforeclass(){
        baseURI= ConfigurationReader.get("hr_api_url");
    }

    /*
    Q1:
- Given accept type is Json
- Path param value- US
- When users sends request to /countries
- Then status code is 200
- And Content - Type is Json
- And country_id is US
- And Country_name is United States of America
- And Region_id is
     */
    @Test
    public void testQ1(){

        Response response = given().accept(ContentType.JSON)
                .and().pathParam("id","US")
                .when().get("/countries/{id}");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");

        String CountryId = response.path("country_id");
        String CountryName = response.path("country_name");
        int RegionId = response.path("region_id");

        assertEquals(RegionId,2);
        assertEquals(CountryName,"United States of America");
        assertEquals(CountryId,"US");


    }


    /*
Q2:
- Given accept type is Json
- Query param value - q={"department_id":80}
- When users sends request to /employees
- Then status code is 200
- And Content - Type is Json
- And all job_ids start with 'SA'
- And all department_ids are 80
- Count is 25
     */
    @Test
    public void Q2(){

        Response response = given().accept(ContentType.JSON)
                .and().queryParam("q", "{\"department_id\":80}")
                .when().get("/employees");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");

        JsonPath jsonPath = response.jsonPath();

        List<String> jobIds = jsonPath.getList("items.job_id");

        for (String jobId : jobIds) {
            assertTrue(jobId.startsWith("SA"));
        }

        List<Integer> departmentIds = jsonPath.getList("items.department_id");

        for (int departmentId : departmentIds) {
            assertEquals(departmentId,80);
        }


        int count = jsonPath.get("count");
        assertEquals(count,25);



    }


    /*
Q3:
- Given accept type is Json
-Query param value q= region_id 3
- When users sends request to /countries
- Then status code is 200
- And all regions_id is 3
- And count is 6
- And hasMore is false
- And Country_name are;
Australia,China,India,Japan,Malaysia,Singapore
     */
    @Test
    public void Q3(){

        Response response = given().accept(ContentType.JSON)
                .and().queryParam("q", "{\"region_id\":3}")
                .when().get("/countries");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");

        JsonPath jsonPath = response.jsonPath();

        List<Integer> regionIds = jsonPath.getList("items.region_id");
        for (int regionId : regionIds) {
            assertEquals(regionId,3);
        }

        int count = jsonPath.get("count");
        assertEquals(count,6);

        boolean hasMore = jsonPath.getBoolean("hasMore");
        assertFalse(hasMore);

        List<String> expectedCountryNames = new ArrayList<>();
        expectedCountryNames.add("Australia");
        expectedCountryNames.add("China");
        expectedCountryNames.add("India");
        expectedCountryNames.add("Japan");
        expectedCountryNames.add("Malaysia");
        expectedCountryNames.add("Singapore");


        List<String> countryNames = jsonPath.getList("items.country_name");

        for (int i = 0; i < expectedCountryNames.size(); i++) {
             assertEquals(countryNames.get(i),expectedCountryNames.get(i));
        }


    }




}
