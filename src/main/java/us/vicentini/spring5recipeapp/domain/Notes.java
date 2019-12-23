package us.vicentini.spring5recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import static lombok.AccessLevel.PRIVATE;

@Data
@EqualsAndHashCode()
@ToString()
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class Notes {
    @Id
    private String id;
    private String recipeNotes;
}
