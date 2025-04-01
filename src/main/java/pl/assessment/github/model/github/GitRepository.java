package pl.assessment.github.model.github;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GitRepository {

    private String repositoryName;

    private List<Branch> branches;

}
