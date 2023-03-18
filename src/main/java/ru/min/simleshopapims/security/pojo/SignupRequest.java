package ru.min.simleshopapims.security.pojo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

public class SignupRequest {

    @NotBlank
    private String username;
    @Pattern(regexp = "((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20})",
            message = "Пароль должен содержать цифру, заглавную, строчную букву. Пароль должен быть больше 6 символов")
    private String password;
    @Email
    private String email;
    private Set<String> roles;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Set<String> getRoles() {
        return roles;
    }
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
