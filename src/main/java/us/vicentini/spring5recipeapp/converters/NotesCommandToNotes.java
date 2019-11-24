package us.vicentini.spring5recipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import us.vicentini.spring5recipeapp.commands.NotesCommand;
import us.vicentini.spring5recipeapp.domain.Notes;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {
    @Override
    public Notes convert(NotesCommand notesCommand) {
        if (notesCommand == null) {
            return null;
        }
        return Notes.builder()
                .id(notesCommand.getId())
                .recipeNotes(notesCommand.getRecipeNotes())
                .build();
    }
}
