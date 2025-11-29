package com.example.leavestech.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.leavestech.domain.Teacher} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeacherDTO implements Serializable {

    private Long id;

    private String title;

    private String officeRoom;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOfficeRoom() {
        return officeRoom;
    }

    public void setOfficeRoom(String officeRoom) {
        this.officeRoom = officeRoom;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeacherDTO)) {
            return false;
        }

        TeacherDTO teacherDTO = (TeacherDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teacherDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeacherDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", officeRoom='" + getOfficeRoom() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
