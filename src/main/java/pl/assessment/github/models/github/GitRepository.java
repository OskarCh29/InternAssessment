package pl.assessment.github.models.github;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GitRepository {

    private String repositoryName;

    private List<Branch> branches;
}
