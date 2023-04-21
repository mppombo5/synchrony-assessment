package me.mppombo.synchronyapi.repository;

import me.mppombo.synchronyapi.models.ImgurImage;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ImgurImageRepository extends ListCrudRepository<ImgurImage, Long> {
    List<ImgurImage> findByDeletehash(String deletehash);
    List<ImgurImage> findByApiUser(String apiUser);
}
