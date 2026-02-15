package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String userName;
    private boolean isActive;
    private Set<Role> roles;

}
