package com.example.restful_web_service.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import com.example.restful_web_service.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserResource {

    @Autowired
    private UserDaoService userService;

    @GetMapping(value = "/users")
    public List<User> retrieveAllUsers() {
        return userService.findAll();
    }

    @GetMapping(value = "/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        User user = userService.findOne(id);
        if (user == null)
            throw new UserNotFoundException("id-"+ id);

        EntityModel<User> resource = EntityModel.of(user);
        resource.add(linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users"));

        return resource;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<Object> createUser(@Validated @RequestBody User user) {
        User savedUser = userService.save(user);
        // CREATED
        // /users/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = userService.deleteById(id);
        if (user == null)
            throw new UserNotFoundException("id-"+ id);
    }
}
