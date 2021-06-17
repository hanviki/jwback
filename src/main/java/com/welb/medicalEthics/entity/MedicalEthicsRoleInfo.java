package com.welb.medicalEthics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalEthicsRoleInfo {
    private int id;
    private String roleCode;
    private String roleName;
    private String description;
}
