package hexlet.code.util;

import lombok.Getter;

@Getter
public enum DefaultTaskStatus {

    DRAFT("Draft", "draft"),
    TO_REVIEW("ToReview", "to_review"),
    TO_BE_FIXED("ToBeFixed", "to_be_fixed"),
    TO_PUBLISH("ToPublish", "to_publish"),
    PUBLISHED("Published", "published");

    private String name;
    private String slug;

    DefaultTaskStatus(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }
}
