package com.codigo.exameng6.repository;

import com.codigo.exameng6.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByNumeroDocumento(String dni);

    Optional<List<UsuarioEntity>> findByEstado(Integer statusActive);

    Optional<UsuarioEntity> findByEmail(String email);
}
