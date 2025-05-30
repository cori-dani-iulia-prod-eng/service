package ro.unibuc.hello.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateSupplier {
    private String id;

    @Size(max = 30, message = "Name must be less than 30 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Phone number should be in the format XXX-XXX-XXXX")
    private String phone;

    @Size(max = 200, message = "Address must be less than 200 characters")
    private String address;

    public UpdateSupplier() {
    }

    public UpdateSupplier(String id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getId() {
        return id;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
