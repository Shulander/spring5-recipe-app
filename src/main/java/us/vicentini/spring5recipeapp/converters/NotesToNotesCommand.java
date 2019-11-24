package us.vicentini.spring5recipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import us.vicentini.spring5recipeapp.commands.NotesCommand;
import us.vicentini.spring5recipeapp.domain.Notes;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {
    @Override
    public NotesCommand convert(Notes notes) {
        if (notes == null) {
            return null;
        }
        return NotesCommand.builder()
                .id(notes.getId())
                .recipeNotes(notes.getRecipeNotes())
                .build();
    }
}
