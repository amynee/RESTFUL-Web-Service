package com.example.restful_web_service.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import com.example.restful_web_service.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
public class UserResource {

    @Autowired
    private UserDaoService userService;
    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/get-message")
    /* public String getMessage(@RequestHeader(name = "Accept-language", required = false) Locale locale) {
        System.out.println(locale);
        return messageSource.getMessage("good.morning.message", null, locale);
    } */
    public String getMessage() {
        System.out.println(LocaleContextHolder.getLocale());
        return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
    }

    @GetMapping(value = "/all-users")
    public List<User> retrieveAllUsers() {
        return userService.findAll();
    }

    @GetMapping(value = "/users-names")
    public MappingJacksonValue  retrieveAllUsersName() {
        List<User> users = userService.findAll();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");
        FilterProvider filters = new SimpleFilterProvider().addFilter("userFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);
        return  mapping;
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
