package ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.enums.Role;

public class UserEntity {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private Role role;

    public UserEntity(String id, String name, String email, String phone, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public UserEntity(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public UserEntity(User user) {
        name = user.getName();
        email = user.getEmail();
        phone = user.getPhone();
        role = user.getRole();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phone + '\'' +
                '}';
    }
}
