package us.vicentini.spring5recipeapp.services;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface ImageService {
    Mono<Void> saveImageFile(String recipeId, FilePart filePart);
}
