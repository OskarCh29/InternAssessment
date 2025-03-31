package pl.assessment.github.models.github;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Branch {

    private String name;

    private String lastCommitSha;
}
