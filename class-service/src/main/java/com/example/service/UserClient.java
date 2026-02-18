package com.example.service;
import org.springframework.cloud.openfeign.FeignClient;
import com.example.dto.EleveResponseDto;
import com.example.dto.EnseignantResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;
import java.util.UUID;


@FeignClient(name = "users-service")
public interface UserClient {

    @GetMapping("/api/users/students/{id}/classe")
    UUID getClasseIdByStudent(@PathVariable("id") UUID studentId);

    @PutMapping("/api/users/students/{id}/classe/{classeId}")
    void assignClasseToStudent(@PathVariable("id") UUID studentId,
                               @PathVariable("classeId") UUID classeId);

    @GetMapping("/api/users/class/{classId}")
    List<EleveResponseDto> getStudentsByClasseId(@PathVariable("classId") UUID classId);

    @PutMapping("/api/users/enseignants/{id}/classe/{classeId}")
    void assignEnseignantToClasse(@PathVariable("id") UUID enseignantId,
                                  @PathVariable("classeId") UUID classeId);

    @GetMapping("/api/users/enseignants/class/{classId}")
    List<EnseignantResponseDto> getEnseignantsByClasseId(@PathVariable("classId") UUID classId);
}
