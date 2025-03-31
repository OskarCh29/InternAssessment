package pl.assessment.github.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RepositoryResponse {

    private String name;

    private boolean fork;

    private Owner owner;

    @Data
    public static class Owner {
        private String login;
    }
}
