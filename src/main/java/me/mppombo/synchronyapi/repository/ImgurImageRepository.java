package me.mppombo.synchronyapi.repository;

import me.mppombo.synchronyapi.model.ApiUser;
import me.mppombo.synchronyapi.model.ImgurImage;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface ImgurImageRepository extends ListCrudRepository<ImgurImage, Long> {
    Optional<ImgurImage> findByDeletehash(String deletehash);
    Optional<ImgurImage> findByImgurId(String imgurId);
    List<ImgurImage> findByOwner(ApiUser owner);
}
