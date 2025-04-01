package pl.assessment.github.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import pl.assessment.github.model.github.UserRepositories;
import pl.assessment.github.model.response.ErrorResponse;
import pl.assessment.github.utils.TestUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {

    private static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(wireMockConfig().dynamicPort());

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int localPort;

    @BeforeAll
    public static void initOnce() {
        WIRE_MOCK_SERVER.start();
        configureFor("localhost", WIRE_MOCK_SERVER.port());
    }

    @BeforeEach
    public void resetMockServer() {
        WIRE_MOCK_SERVER.resetAll();
    }

    @DynamicPropertySource
    public static void dynamicPropertiesSetUp(DynamicPropertyRegistry registry) {
        registry.add("github-api.url", WIRE_MOCK_SERVER::baseUrl);
        registry.add("security.API_KEY", () -> "TestKey");
    }

    @Test
    void shouldReturnStatus200withProperResponse() throws Exception {

        var repoMockResponse = TestUtils.getJsonFromFile("/response/github_repository_200.json");
        WIRE_MOCK_SERVER.stubFor(get(urlEqualTo("/users/OskarCh29/repos"))
                        .willReturn(ok().withBody(repoMockResponse)
                                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        var branchMockResponse = TestUtils.getJsonFromFile("/response/github_branch_200.json");
        WIRE_MOCK_SERVER.stubFor(get(urlMatching("/repos/OskarCh29/.*"))
                .willReturn(ok().withBody(branchMockResponse)
                        .withHeader(CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)));

        var response = this.testRestTemplate.getForEntity("http://localhost:"+localPort+"/repos/OskarCh29", UserRepositories.class);

        assertEquals("OskarCh29",response.getBody().getLogin());
        assertEquals(1,response.getBody().getRepos().size());
        assertEquals("group-chat",response.getBody().getRepos().getFirst().getRepositoryName());
        assertEquals(1,response.getBody().getRepos().getFirst().getBranches().size());


    }

    @Test
    void shouldReturnStatus404NotFoundResponse() throws Exception {
        var responseMock = TestUtils.getJsonFromFile("/response/github_404.json");
        WIRE_MOCK_SERVER.stubFor(get(urlEqualTo("/repos/TEST"))
                .willReturn(notFound().withBody(responseMock)));
        var response = this.testRestTemplate.getForObject("http://localhost:" + localPort + "/repos/TEST", ErrorResponse.class);

        assertEquals(404, response.getStatus());
        assertTrue(response.getMessage().contains("No user found"));
    }

    @Test
    void shouldReturnStatus400BadCredentials() throws Exception {
        var responseMock = TestUtils.getJsonFromFile("/response/github_400.json");
        WIRE_MOCK_SERVER.stubFor(get(urlMatching("/.*"))
                .willReturn(badRequest().withBody(responseMock)));

        var response = this.testRestTemplate.getForObject("http://localhost:" + localPort + "/test/test", ErrorResponse.class);

        assertEquals(400, response.getStatus());
        assertTrue(response.getMessage().contains("No resource found"));
    }

}
