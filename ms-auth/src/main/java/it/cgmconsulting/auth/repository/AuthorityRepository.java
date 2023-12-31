package it.cgmconsulting.auth.repository;

import it.cgmconsulting.auth.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {


    Optional<Authority> findByAuthorityName(String authorityName);

    Set<Authority> findByAuthorityNameIn(Set<String> authorities);


}
