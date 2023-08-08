package com.metalancer.backend.common.test;

import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupSecurityResources();
        User user1 = createUserIfNotFound("aaa@naver.com", "aaaaa11111");
        User user2 = createUserIfNotFound("aaa111@naver.com", "aaaaa11111");
        user1.setRole(Role.ROLE_USER);
        user2.setRole(Role.ROLE_ADMIN);

        alreadySetup = true;
    }

    private void setupSecurityResources() {

    }

    @Transactional
    public User createUserIfNotFound(final String email,
        final String password) {

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            User newUser = User.builder().email(email).password(passwordEncoder.encode(password))
                .loginType(LoginType.NORMAL)
                .username(LoginType.NORMAL.getProvider() + "_" + UUID.randomUUID()
                    .toString().substring(0, 8)).build();

            return userRepository.save(newUser);
        }
        return user.get();
    }

}
