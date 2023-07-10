package it.unipa.cardmanager.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Byte> {

    Role findByName(String name);
}