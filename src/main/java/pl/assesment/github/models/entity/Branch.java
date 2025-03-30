package pl.assesment.github.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Branch {

    private String name;
    private String lastCommitSha;
}
