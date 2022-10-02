# SDET
The current SDET challenge was solved using a maven project, which only includes the corresponding dependencies to manage REST Api services.
### Dependencies
1. **REST-Assured:** This dependency was used mainly because offers the ability to manage JSON responses through Java language.
2. **TestNG:** This dependency was included in order to manage the assertions during the tests.
### Scenarios
The corresponding scenarios were covered with a previous manual testing performed through **Postman** tool, once the exploratory was completed the next step was the automation project creation.
Each test script is using Rest-Assured dependency to call the endpoints with the required parameters, in some cases the JSON response was digested to get specific values like "earth date".
The validations were included in all the scenarios(functional/no-functional) taking account some values according each case.
### Report
1. verifyPhotosApiIsReturningTheCorrectResponse1(scenario 1) - `PASSED` ![#c5f015](https://via.placeholder.com/15/c5f015/c5f015.png)
2. verifyPhotosApiIsReturningTheCorrectResponse2(scenario 2) - `PASSED` ![#c5f015](https://via.placeholder.com/15/c5f015/c5f015.png)
3. verifyPhotosApiIsReturningTheCorrectResponse3(scenario 3) - `PASSED` ![#c5f015](https://via.placeholder.com/15/c5f015/c5f015.png)
4. verifyPhotosApiIsReturningTheCorrectResponse4(scenario 4) - `FAILED` ![#f03c15](https://via.placeholder.com/15/f03c15/f03c15.png)
```
- During the testing a bug was identified and catched by automation.
- This bug was confirmed and isolated manually through Postman.
```
```
Currently there are no photos when the 'earth_date = 2006-10-27'
- Endpoint: https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=2006-10-27&api_key=KEY
- Response: 
  { "photos": [] }
    
However when the previous endpoint includes the 'sol = 1000' as parameter
- Endpoint: https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=2006-10-27&sol=1000&api_key=KEY
- Response: 
  { "photos": [
        {
            "id": 102693,
            "sol": 1000,
            "camera": {
                "id": 20,
                "name": "FHAZ",
                "rover_id": 5,
                "full_name": "Front Hazard Avoidance Camera"
            },
            "img_src": "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/fcam/FLB_486265257EDR_F0481570FHAZ00323M_.JPG",
            "earth_date": "2015-05-30",
            "rover": {
                "id": 5,
                "name": "Curiosity",
                "landing_date": "2012-08-06",
                "launch_date": "2011-11-26",
                "status": "active"
            }
        },.....
```
5. verifyPhotosApiIsReturningTheCorrectResponse5(scenario 5) - `PASSED` ![#c5f015](https://via.placeholder.com/15/c5f015/c5f015.png)
### Bug
```
- Title: The response is not according with the 'earth_date' parameter specified
- Priority: High
- Steps:
    1. Execute the following endpoint, https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=2006-10-27&sol=1000&api_key=KEY
    2. Review if the response is matching with the 'earth_date' value
- Actual Result
    The photos returned in the response are not matching with the 'earth_date' value
- Expected Result
    The photos returned should be match with the 'earth_date' value
```

