package com.example.dto;

import com.example.enums.LevelClasse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClasseRequestDto {

    @NotBlank(message = "Le nom de la classe est obligatoire")
    private String name;

    @NotNull(message = "Le niveau de la classe est obligatoire")
    private LevelClasse levelClasse;

    @NotNull(message = "L'enseignant principal est obligatoire")
    private UUID enseignantPrincipal;

    private String anneeScolaire;


}
