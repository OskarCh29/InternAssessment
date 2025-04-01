package pl.assessment.github.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchResponse {

    private String name;

    private Commit commit;

    @Data
    public static class Commit {
        private String sha;
    }

}
