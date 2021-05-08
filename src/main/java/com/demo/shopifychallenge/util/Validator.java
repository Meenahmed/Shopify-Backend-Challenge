package com.demo.shopifychallenge.util;

import org.springframework.context.annotation.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class Validator {

    public final Pattern VALID_FILE_EXTENSION_REGEX =
            Pattern.compile("([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp|jfif))$)", Pattern.CASE_INSENSITIVE);

    public boolean validateFile(String type) {
        Matcher matcher = VALID_FILE_EXTENSION_REGEX.matcher(type);
        return matcher.find();
    }

}
