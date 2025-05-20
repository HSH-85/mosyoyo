package com.project.msy.bookmark.repository;

import com.project.msy.bookmark.entity.Bookmark;
import com.project.msy.facility.entity.Facility;
import com.project.msy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUserAndFacility(User user, Facility facility);

    Optional<Bookmark> findByUserAndFacility(User user, Facility facility);

    List<Bookmark> findAllByUser(User user);
    void deleteAllByUser(User user);
}
