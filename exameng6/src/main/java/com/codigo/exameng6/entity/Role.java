package com.codigo.exameng6.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Role class: Entidad para los roles de usuarios.
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private int idRole;
    private String nameRole;
}
