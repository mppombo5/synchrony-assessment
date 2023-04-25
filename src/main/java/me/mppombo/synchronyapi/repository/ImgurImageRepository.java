package me.mppombo.synchronyapi.repository;

import me.mppombo.synchronyapi.model.ImgurImage;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ImgurImageRepository extends ListCrudRepository<ImgurImage, Long> {
    Optional<ImgurImage> findByDeletehash(String deletehash);
}
