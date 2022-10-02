package smoke.tests;

import environment.Api;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.MessageFormat;
import java.util.List;

public class Photos {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Api.URI;
    }

    @DataProvider(name = "test-data")
    public Object[][] data(){
        return new Object[][] {{10, "curiosity", "1000"}};
    }

    @Test(description = "Retrieve the first '10' Mars photos made by 'Curiosity' on '1000' Martian sol",
          dataProvider = "test-data",
          groups = {"smoke"})
    void verifyPhotosApiIsReturningTheCorrectResponse1(int photosNumber, String rover, String sol){
        //Api call performed through "REST-assured" using the corresponding parameters
        Response response = RestAssured.given()
                                       .get(MessageFormat.format("/rovers/{0}/photos?sol={1}&api_key={2}", rover, sol, Api.KEY));

        //Api response managed to get only the first "10" photos
        List<Object> photos = response.jsonPath().getList("photos.img_src").subList(0, photosNumber);
        photos.forEach(photo -> System.out.println("Photo: " + photo.toString()));

        //Validation performed, taking account only "10" photos
        Assert.assertEquals(photos.size(), photosNumber);
    }

    @Test(description = "Retrieve the first '10' Mars photos made by 'Curiosity' on Earth date equal to '1000' Martian sol",
          dataProvider = "test-data",
          groups = {"smoke"})
    void verifyPhotosApiIsReturningTheCorrectResponse2(int photosNumber, String rover, String sol){
        //Api call performed through "REST-assured" using the corresponding parameters
        Response response = RestAssured.given()
                                       .get(MessageFormat.format("/rovers/{0}/photos?sol={1}&api_key={2}", rover, sol, Api.KEY));

        //Getting the "earth date" value based in the previous response
        String earthDate = response.jsonPath().getList("photos.earth_date").subList(0, 1).get(0).toString();

        //Api call performed using the previous "earth date" value
        response = RestAssured.given()
                              .get(MessageFormat.format("/rovers/{0}/photos?earthDate{1}&sol={2}&api_key={3}", rover, earthDate, sol, Api.KEY));

        //Api response managed to get only the first "10" photos
        List<Object> photos = response.jsonPath().getList("photos.earth_date").subList(0, photosNumber);

        //Validation performed, taking account only "10" photos and the "earth date" value
        Assert.assertEquals(photos.size(), photosNumber);
        photos.forEach(photo -> Assert.assertEquals(photo.toString(), earthDate));
    }

    @Test(description = "Retrieve and compare the first '10' Mars photos made by 'Curiosity' on '1000' sol and on Earth date equal to 1000 Martian sol",
          dataProvider = "test-data",
          groups = {"smoke"})
    void verifyPhotosApiIsReturningTheCorrectResponse3(int photosNumber, String rover, String sol){
        //Api call performed through "REST-assured" using the corresponding parameters
        Response response1 = RestAssured.given()
                                        .get(MessageFormat.format("/rovers/{0}/photos?sol={1}&api_key={2}", rover, sol, Api.KEY));

        //Api response managed to get only the first "10" photos
        List<Object> photos1 = response1.jsonPath().getList("photos.img_src").subList(0, photosNumber);

        //Getting the "earth date" value based in the previous response
        String earthDate = response1.jsonPath().getList("photos.earth_date").subList(0, 1).get(0).toString();

        //Validation performed, taking account only "10" photos for "response 1"
        Assert.assertEquals(photos1.size(), photosNumber);

        //Api call performed using the previous "earth date" value
        Response response2 = RestAssured.given()
                                        .get(MessageFormat.format("/rovers/{0}/photos?earthDate{1}&sol={2}&api_key={3}", rover, earthDate, sol, Api.KEY));

        //Api response managed to get only the first "10" photos
        List<Object> photos2 = response2.jsonPath().getList("photos.img_src").subList(0, photosNumber);

        //Validation performed, taking account only "10" photos for "response 2"
        Assert.assertEquals(photos2.size(), photosNumber);

        //Validation performed to compare "photos 1" and "photos 2" list
        Assert.assertEquals(photos1.equals(photos2), true);
    }

    @Test(description = "Validate that the amounts of pictures that each 'Curiosity' camera took on '1000' Mars sol is not greater than '10' times the amount taken by other cameras on the same date",
          groups = {"smoke"})
    void verifyPhotosApiIsReturningTheCorrectResponse4(){
        String curiosity = "curiosity";
        String opportunity = "opportunity";
        String spirit = "spirit";
        String sol = "1000";

        //Api call performed through "REST-assured" using "spirit" value
        Response responseSpirit = RestAssured.given()
                .get(MessageFormat.format("/rovers/{0}/photos?sol={1}&api_key={2}", spirit, sol, Api.KEY));

        //Getting the "earth date" value based in the previous response
        List<Object> photosSpirit = responseSpirit.jsonPath().getList("photos.earth_date");
        String earthDateSpirit = responseSpirit.jsonPath().getList("photos.earth_date").subList(0, 1).get(0).toString();
        System.out.println("Spirit Date: " + earthDateSpirit);
        System.out.println(photosSpirit.size());

        //Api call performed through "REST-assured" using "opportunity" value
        Response responseOpportunity = RestAssured.given()
                .get(MessageFormat.format("/rovers/{0}/photos?sol={1}&api_key={2}", opportunity, sol, Api.KEY));

        //Getting the "earth date" value based in the previous response
        List<Object> photosOpportunity = responseOpportunity.jsonPath().getList("photos.earth_date");
        String earthDateOpportunity = responseOpportunity.jsonPath().getList("photos.earth_date").subList(0, 1).get(0).toString();
        System.out.println("Opportunity Date: " + earthDateOpportunity);
        System.out.println(photosOpportunity.size());

        //Api call performed through "REST-assured" using "curiosity" and "earth date spirit" values
        Response responseCuriosity1 = RestAssured.given()
                                                 .get(MessageFormat.format("/rovers/{0}/photos?earth_date={1}&sol={2}&api_key={3}", curiosity, earthDateSpirit, sol, Api.KEY));
        List<Object> photosCuriosity1 = responseCuriosity1.jsonPath().getList("photos.earth_date");
        System.out.println("Photos Curiosity - Spirit Date: " + photosCuriosity1.size());

        //Api call performed through "REST-assured" using "curiosity" and "earth date opportunity" values
        Response responseCuriosity2 = RestAssured.given()
                                                 .get(MessageFormat.format("/rovers/{0}/photos?earth_date={1}&sol={2}&api_key={3}", curiosity, earthDateOpportunity, sol, Api.KEY));
        List<Object> photosCuriosity2 = responseCuriosity2.jsonPath().getList("photos.earth_date");
        System.out.println("Photos Curiosity - Opportunity Date: " + photosCuriosity2.size());

        //Getting photos size of "spirit" and "opportunity" types, multiplied by "10" amount
        int photosSpiritOpportunityTotal = 10 * (photosSpirit.size() + photosOpportunity.size());

        //Getting photos size of "curiosity" type, taking account the same "earth date" for "spirit" and "opportunity" values
        int photosCuriosityTotal = photosCuriosity1.size() + photosCuriosity2.size();

        //Validation performed to review if "curiosity" is less than "spirit" and "opportunity" photos
        Assert.assertEquals(photosCuriosityTotal < photosSpiritOpportunityTotal, true);
    }


    @Test(description = "Write integration tests around the core functionality, not functional tests",
          groups = {"integration", "no-functional"})
    void verifyPhotosApiIsReturningTheCorrectResponse5(){
        String curiosity = "curiosity";
        String opportunity = "opportunity";
        String spirit = "spirit";
        String sol = "1000";

        //Api call performed through "REST-assured" using "spirit" value
        Response responseSpirit = RestAssured.given()
                                             .get(MessageFormat.format("/rovers/{0}/photos?sol={1}&api_key={2}", spirit, sol, Api.KEY));

        //No functional validation, based in the "status code", "content type" and "header" values
        responseSpirit.then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .header("Access-Control-Allow-Origin", "*");

        //Getting the "earth date" value based in the previous response
        String earthDateSpirit = responseSpirit.jsonPath().getList("photos.earth_date").subList(0, 1).get(0).toString();

        //Api call performed through "REST-assured" using "opportunity" value
        Response responseOpportunity = RestAssured.given()
                                                  .get(MessageFormat.format("/rovers/{0}/photos?sol={1}&api_key={2}", opportunity, sol, Api.KEY));

        //No functional validation, based in the "status code", "content type" and "header" values
        responseOpportunity.then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .header("Access-Control-Allow-Origin", "*");

        //Getting the "earth date" value based in the previous response
        String earthDateOpportunity = responseOpportunity.jsonPath().getList("photos.earth_date").subList(0, 1).get(0).toString();

        //Api call performed through "REST-assured" using "curiosity" and "earth date spirit" values
        Response responseCuriosity1 = RestAssured.given()
                                                 .get(MessageFormat.format("/rovers/{0}/photos?earth_date={1}&sol={2}&api_key={3}", curiosity, earthDateSpirit, sol, Api.KEY));

        //No functional validation, based in the "status code", "content type" and "header" values
        responseCuriosity1.then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .header("Access-Control-Allow-Origin", "*");

        //Api call performed through "REST-assured" using "curiosity" and "earth date opportunity" values
        Response responseCuriosity2 = RestAssured.given()
                                                 .get(MessageFormat.format("/rovers/{0}/photos?earth_date={1}&sol={2}&api_key={3}", curiosity, earthDateOpportunity, sol, Api.KEY));

        //No functional validation, based in the "status code", "content type" and "header" values
        responseCuriosity2.then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .header("Access-Control-Allow-Origin", "*");
    }
}
