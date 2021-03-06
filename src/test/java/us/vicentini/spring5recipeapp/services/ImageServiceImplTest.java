package us.vicentini.spring5recipeapp.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import us.vicentini.spring5recipeapp.domain.Recipe;
import us.vicentini.spring5recipeapp.repositories.RecipeReactiveRepository;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ImageServiceImplTest {

    @Mock
    private RecipeReactiveRepository recipeRepository;

    @InjectMocks
    private ImageServiceImpl imageService;


    @Disabled
    @Test
        //todo: fix this
    void saveImageFile() throws Exception {
        //given
        String id = "1";
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                                                            "Spring Framework Guru".getBytes());
        FilePart filePart = Mockito.mock(FilePart.class);

        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);


        Recipe recipe = Recipe.builder().id(id).build();
        Mono<Recipe> recipeMono = Mono.just(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeMono);
        when(recipeRepository.save(recipe)).thenReturn(recipeMono);
        when(filePart.transferTo(fileArgumentCaptor.capture())).thenReturn(Mono.empty());

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        //when
        imageService.saveImageFile(id, filePart).block();

        //then
        verify(recipeRepository).findById(id);
        verify(recipeRepository).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(recipeRepository);
    }
}
