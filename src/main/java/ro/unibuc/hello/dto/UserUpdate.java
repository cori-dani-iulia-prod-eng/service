package ro.unibuc.hello.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.enums.Role;

public class UserUpdate {
    private String id;

    @Size(max = 30, message = "Name must be less than 30 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Phone number should be in the format XXX-XXX-XXXX")
    private String phone;

    private Role role;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }

    public UserUpdate(String id, String name, String email, String phone, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public UserUpdate() {
    }

    public UserUpdate(UserEntity entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        phone = entity.getPhone();
        role = entity.getRole();
    }
}
