package hexlet.code.util;

import lombok.Getter;

@Getter
public enum DefaultLabel {

    FEATURE("Feature"),
    BUG("Bug");

    private String name;

    DefaultLabel(String name) {
        this.name = name;
    }
}
