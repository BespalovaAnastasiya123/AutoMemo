package org.project.repo;

import org.project.models.Note;
import org.project.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface NoteRepository extends CrudRepository<Note, Long> {
    List<Note> findByUser(User user);
}

