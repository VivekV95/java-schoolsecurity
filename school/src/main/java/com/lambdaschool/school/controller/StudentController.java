package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.service.StudentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    // Please note there is no way to add students to course yet!

    @ApiOperation(value = "Return all Students"
            , response = Student.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @GetMapping(value = "/students", produces = {"application/json"})
    public ResponseEntity<?> listAllStudents(@PageableDefault(size = 5) Pageable pageable) {
        List<Student> myStudents = studentService.findAll(pageable);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieves a student associated with the studentid"
            , response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Student Found", response = Student.class),
            @ApiResponse(code = 404, message = "Student Not Found", response = ErrorDetail.class)
    })
    @GetMapping(value = "/Student/{studentid}",
            produces = {"application/json"})
    public ResponseEntity<?> getStudentById(
            @ApiParam(value = "Student Id", required = true, example = "1")
            @PathVariable
                    Long studentid) {
        Student r = studentService.findStudentById(studentid);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieves list of Students whose names contain the name parameter"
            , response = Student.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Students Found",
                    response = Student.class, responseContainer = "List")
    })
    @GetMapping(value = "/student/namelike/{name}",
            produces = {"application/json"})
    public ResponseEntity<?> getStudentByNameContaining(
            @ApiParam(value = "Name to search", required = true, example = "John")
            @PathVariable String name,
            @PageableDefault(size = 5) Pageable pageable) {
        List<Student> myStudents = studentService.findStudentByNameLike(name, pageable);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new Student",
            notes = "The newly created student id will be sent in the location header",
            response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student successfully created", response = void.class),
            @ApiResponse(code = 500, message = "Error creating the new Student", response = ErrorDetail.class)
    })
    @PostMapping(value = "/student",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<?> addNewStudent(@ApiParam(value = "New Student to be created", required = true)
                                           @Valid
                                           @RequestBody
                                                   Student newStudent) throws URISyntaxException {
        newStudent = studentService.save(newStudent);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Studentid}").buildAndExpand(newStudent.getStudid()).toUri();
        responseHeaders.setLocation(newStudentURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Updates and returns existing student", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Student successfully updated", response = Student.class),
            @ApiResponse(code = 500, message = "Error updating the Student", response = ErrorDetail.class),
            @ApiResponse(code = 404, message = "Student Not Found", response = ErrorDetail.class)
    })
    @PutMapping(value = "/student/{studentid}")
    public ResponseEntity<?> updateStudent(
            @ApiParam(value = "The Student to be updated", required = true)
            @Valid
            @RequestBody
                    Student updateStudent,
            @ApiParam(value = "Student Id", required = true, example = "1")
            @PathVariable
                    long studentid) {
        Student student = studentService.update(updateStudent, studentid);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @ApiOperation(value = "Deletes a Student based on studentid", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Student successfully deleted", response = void.class),
            @ApiResponse(code = 500, message = "Error deleting the Student", response = ErrorDetail.class),
            @ApiResponse(code = 404, message = "Student Not Found", response = ErrorDetail.class)
    })
    @DeleteMapping("/Student/{Studentid}")
    public ResponseEntity<?> deleteStudentById(
            @ApiParam(value = "Student Id", required = true, example = "1")
            @PathVariable
                    long Studentid) {
        studentService.delete(Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Assigns a Student to a Course", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Student successfully assigned to a Course", response = void.class),
            @ApiResponse(code = 500, message = "Error assigning the Student", response = ErrorDetail.class),
            @ApiResponse(code = 404, message = "Student or Course Not Found", response = ErrorDetail.class)
    })
    @PutMapping(value = "/student/assignCourse/{studentid}/{courseid}",
            produces = {"application/json"})
    public ResponseEntity<?> assignStudentToCourse(@PathVariable
                                                   @ApiParam(value = "Student Id", required = true,
                                                           example = "1") long studentid,
                                                   @PathVariable
                                                   @ApiParam(value = "Course Id", required = true,
                                                           example = "1") long courseid) {
        studentService.assignStudentToCourse(studentid, courseid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
