package ro.unibuc.hello.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "Username is mandatory")
    @Size(max = 30, message = "Username must be less than 30 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(max = 30, message = "Password must be less than 30 characters")
    private String password;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 30, message = "Name must be less than 30 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Phone number should be in the format XXX-XXX-XXXX")
    private String phone;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
