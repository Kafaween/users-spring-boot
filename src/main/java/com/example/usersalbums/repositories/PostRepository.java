package com.example.usersalbums.repositories;

import com.example.usersalbums.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Posts,Long> {
    List<Posts>  findAllByUser_Username(String name);
    void deletePostsByUser_UsernameAndId(String username, Long id);
}
