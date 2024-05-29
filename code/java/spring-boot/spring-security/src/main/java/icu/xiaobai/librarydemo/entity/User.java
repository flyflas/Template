package icu.xiaobai.librarydemo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @TableName user
 */
@Data
public class User implements Serializable, UserDetails {
    private Long userId;

    private String username;

    @JsonIgnore
    private String password;

    private Integer age;

    private String gender;

    private String email;

    private String phone;

    private Date registerDate;

    private Integer userStatus;

    private String statusReason;

    private Integer balance;

    private Date modifiedDate;

    private Integer isDeleted;

    @JsonIgnore
    private static final long serialVersionUID = 1L;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}