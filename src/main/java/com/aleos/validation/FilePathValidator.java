package com.aleos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.File;

public class FilePathValidator implements ConstraintValidator<ValidFilePath, String> {

    @Override
    public boolean isValid(String filePath, ConstraintValidatorContext context) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return true;
        }
        File file = new File(filePath);
        return file.exists() && !file.isDirectory();
    }
}
