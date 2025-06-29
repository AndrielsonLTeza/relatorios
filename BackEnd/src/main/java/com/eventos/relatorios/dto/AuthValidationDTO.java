// AuthValidationDTO.java
package com.eventos.relatorios.dto;

import lombok.Data;

@Data
public class AuthValidationDTO {
    private boolean isValid;
    private String userId;
    private String username;
    private String message;
}
