package com.mars.phms.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "tb_role")
public class PhRole implements GrantedAuthority {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;


    /**
     * @return id
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id 要设置的 id
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return authority
     */
    public String getAuthority() {
        return authority;
    }
    /**
     * @param authority 要设置的 authority
     */
    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
