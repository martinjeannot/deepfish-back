package com.deepfish.user.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "Users", uniqueConstraints = {
    @UniqueConstraint(name = "UK_users__username", columnNames = {"username"}),
})
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractUser {

}
