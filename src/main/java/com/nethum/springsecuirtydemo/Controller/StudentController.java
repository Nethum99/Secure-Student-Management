package com.nethum.springsecuirtydemo.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    List<Student> students = new ArrayList<>(List.of(
            new Student(1, "Navin", "Java"),
            new Student(2, "Sithum","BlockChain")
    ));

    @GetMapping("students")
    public List<Student> getStudents(){
        return students;
    }

    @PostMapping("add_student")
    public void addStudent(@RequestBody Student student){
        students.add(student);
    }

    @GetMapping("csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest httpServletRequest){
        return (CsrfToken) httpServletRequest.getAttribute("_csrf");
    }
}
