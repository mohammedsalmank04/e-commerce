package com.practice.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AppRole roleName;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
