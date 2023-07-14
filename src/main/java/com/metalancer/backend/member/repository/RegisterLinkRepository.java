package com.metalancer.backend.member.repository;

import com.metalancer.backend.member.domain.Email;
import com.metalancer.backend.member.entity.RegisterLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisterLinkRepository extends JpaRepository<RegisterLink, Long> {
    Optional<RegisterLink> findByEmail(Email email);

    Optional<RegisterLink> findByRegisterLink(String registerLink);
}
