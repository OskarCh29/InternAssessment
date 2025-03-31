# Github RestAPI app
### Application for presenting user repositories, their branches and branches last commit sha

## Requirements:
* Java 21 
* Spring boot 3.4.4
* Maven 3.9.4

## Installation and pre-use configuration:
1. Clone the repository to preferred location with following command:<br>
 ```shell
 git clone https://github.com/OskarCh29/InternAssessment.git
```
****
2. Before running the application, provide your GitHub API key to proceed with requests<br>
* Proceed to `application.yaml` file located in `resources` folder in main sub-directory<br>
* In the **security** line followed with **API_KEY** provide your github api key<br>
<sub>Also could be provided as environmental variable </sub>

3. To run your application after all is set up start app with your IDE or using command:<br>
```shell
mvn spring-boot:run
```
**Be aware that for running your application with command line you need to be located in the app directory**

## How to use
Application is based on one endpoint to provide user repository information
After your app is running use the following endpoint:<br>
```shell
http://localhost:{YourLocalPort}/repos/{UsernameYouLikeToCheck}
```
Example of query: `http://localhost:8080/repos/OskarCh29`<br>
This request will return the following response:<br>
```JSON
{
    "login": "OskarCh29",
    "repos": [
        {
            "repositoryName": "group-chat",
            "branches": [
                {
                    "name": "master",
                    "lastCommitSha": "e3768620d129f8515a06c7614468e2e91d303899"
                }
            ]
        },
        {
            "repositoryName": "InternAssessment",
            "branches": [
                {
                    "name": "master",
                    "lastCommitSha": "a5dad71e6467ea2e145e7928801eb2d6e63dd429"
                }
            ]
        },
        {
            "repositoryName": "QuarkusRestAPI",
            "branches": [
                {
                    "name": "master",
                    "lastCommitSha": "308f00555713b8b1d3e067194521aab95be21387"
                }
            ]
        },
        {
            "repositoryName": "RestfulAPI",
            "branches": [
                {
                    "name": "master",
                    "lastCommitSha": "f2b965d2b826265c1894f5efbbbebc9625e5cac4"
                }
            ]
        },
        {
            "repositoryName": "SmartCommute",
            "branches": [
                {
                    "name": "master",
                    "lastCommitSha": "baf6a6dceb79cd0b08d5f8b3066cd0288cbfe027"
                }
            ]
        }
    ]
}
```

*Any other endpoint will result in a bad request*<br>

***No user found:***
```JSON
{
"status": 404,
"message": "No user found with username provided"
}
```

***Invalid endpoint or missing username***
```JSON
{
"status": 400,
"message": "No resource found at url provided"
}
```

## Testing
For testing the app with WireMock stubs for integration test proceed with the following command in app directory:<br>
```shell
mvn test
```
The test suite includes:
* Integration tests with WireMock to simulate GitHub Api responses
* Validation tests to check how application handles incorrect inputs
* Error handling for cases in which user is not-existing

