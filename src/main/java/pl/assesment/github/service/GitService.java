package pl.assesment.github.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.assesment.github.exception.UserNotFoundException;
import pl.assesment.github.models.entity.Branch;
import pl.assesment.github.models.entity.GitRepository;
import pl.assesment.github.models.entity.UserRepositories;
import pl.assesment.github.models.response.BranchResponse;
import pl.assesment.github.models.response.RepositoryResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class GitService {
    @Value("${github-api.url}")
    private String url;

    private final RestTemplate restTemplate;

    public UserRepositories getUserRepositories(String username) {
        try {
            ResponseEntity<RepositoryResponse[]> response = restTemplate
                    .getForEntity(url + "/users/" + username + "/repos", RepositoryResponse[].class);
            Stream<RepositoryResponse> repoResponse = Arrays.stream(response.getBody());

            return new UserRepositories(username, getGitRepositories(repoResponse));
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException("No user found with username provided");
        }
    }

    private List<GitRepository> getGitRepositories(Stream<RepositoryResponse> responses) {
        return responses.filter(repo -> !repo.isFork()).map(
                repo -> new GitRepository(repo.getName(), getBranches(repo.getOwner().getLogin(), repo.getName()))
        ).toList();
    }

    private List<Branch> getBranches(String username, String repoName) {
        ResponseEntity<BranchResponse[]> response = restTemplate
                .getForEntity(url + "/repos/" + username + "/" + repoName + "/branches", BranchResponse[].class);
        Stream<BranchResponse> branchResponse = Arrays.stream(response.getBody());
        return branchResponse.map(
                branch -> new Branch(branch.getName(), branch.getCommit().getSha())).collect(Collectors.toList());
    }
}
