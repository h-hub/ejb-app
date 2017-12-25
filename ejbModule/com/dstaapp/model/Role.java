package com.dstaapp.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Role implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "role_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Roles name;
	
	@ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

	public Integer getId() {
		return roleId;
	}

	public void setId(Integer roleId) {
		this.roleId = roleId;
	}

	public Roles getRole() {
		return name;
	}

	public void setRole(Roles name) {
		this.name = name;
	}	
	
}