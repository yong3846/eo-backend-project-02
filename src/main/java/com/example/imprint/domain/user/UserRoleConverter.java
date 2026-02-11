package com.example.imprint.domain.user;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRole role) {
        // Enum = DB
        // ex) USER = 0
        return (role == null) ? null : role.getValue();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer dbData) {
        // DB = Enum
        // ex) 0 = USER
        return (dbData == null) ? null : UserRole.fromValue(dbData);
    }
}