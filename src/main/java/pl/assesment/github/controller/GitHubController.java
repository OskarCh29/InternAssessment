package pl.assesment.github.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.assesment.github.models.entity.UserRepositories;
import pl.assesment.github.service.GitService;

@RestController
@RequiredArgsConstructor
public class GitHubController {

    private final GitService gitRepoService;

    @RequestMapping(path = "/repos/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRepositories> getUserRepository(@PathVariable(required = true) @Valid  String username) {
        return ResponseEntity.ok(gitRepoService.getUserRepositories(username));
    }

}
