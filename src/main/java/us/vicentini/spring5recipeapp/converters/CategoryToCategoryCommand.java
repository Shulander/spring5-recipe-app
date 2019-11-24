package us.vicentini.spring5recipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import us.vicentini.spring5recipeapp.commands.CategoryCommand;
import us.vicentini.spring5recipeapp.domain.Category;

@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {
    @Override
    public CategoryCommand convert(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryCommand.builder()
                .id(category.getId())
                .description(category.getDescription())
                .build();
    }
}
