package com.deepfish.user.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "Users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractUser {

}
