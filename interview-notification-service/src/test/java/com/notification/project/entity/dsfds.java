package com.notification.project.entity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTemplateTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void templateName_shouldNotBeNull() {
        NotificationTemplate template = new NotificationTemplate();
        template.setTemplateName(null);
        template.setCreatedById(123L);

        Set violations = validator.validate(template);
        assertThat(violations).isNotEmpty();
    }
}

