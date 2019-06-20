package com.lambdaschool.school.repository;

import com.lambdaschool.school.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.List;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long>
{
    List<Student> findByStudnameContainingIgnoreCase(String name, Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO studcourses (studid, courseid) VALUES (:studentid, :courseid)", nativeQuery = true)
    void assignStudentToCourse(long studentid, long courseid);
}
