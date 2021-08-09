package com.embl.ebi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Document(collection = "persons")
@Data
public class Person implements Serializable {

    @Id
    @ApiModelProperty(notes = "Unique id to identify person")
    private String id;


    @JsonProperty("first_name")
    @ApiModelProperty(notes = "First name of person")
    @NotBlank(message = "{validation.first_name}")
    private String firstName;

    @JsonProperty("last_name")
    @ApiModelProperty(notes = "Last name of person")
    private String lastName;

    @Max(value = 123, message = "{validation.age.max}")
    @Min(value = 0, message = "{validation.age.min}")
    @ApiModelProperty(notes = "Age name of person")
    private int age;

    @NotBlank(message = "{validation.favourite_colour}")
    @JsonProperty("favourite_colour")
    @ApiModelProperty(notes = "Favourite color of person")
    private String favouriteColor;


    public Person(String firstName, String lastName, int age, String favouriteColor) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.favouriteColor = favouriteColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(favouriteColor, person.favouriteColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age, favouriteColor);
    }
}
