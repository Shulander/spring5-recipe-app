package us.vicentini.spring5recipeapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {
    private static final String CATEGORY_ID = "1";
    private static final String CATEGORY_DESCRIPTION = "Description";

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(CATEGORY_ID)
                .description(CATEGORY_DESCRIPTION)
                .build();
    }

    @Test
    void getId() {
        assertEquals(CATEGORY_ID, category.getId());
    }

    @Test
    void getDescription() {
        assertEquals(CATEGORY_DESCRIPTION, category.getDescription());
    }
}
