package pl.assesment.github.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserRepositories {

    private String login;
    private List<GitRepository> repos;

}
