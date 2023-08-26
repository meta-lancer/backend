package com.metalancer.backend.common.test;

import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.products.entity.Products;
import com.metalancer.backend.products.entity.ProductsCategory;
import com.metalancer.backend.products.repository.ProductsCategoryRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.users.entity.Creator;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.CreatorRepository;
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
    private final ProductsRepository productsRepository;
    private final CreatorRepository creatorRepository;
    private final ProductsCategoryRepository productsCategoryRepository;
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
        User user3 = createUserIfNotFound("aaa222@naver.com", "aaaaa11111");
        user1.setRole(Role.ROLE_USER);
        user2.setRole(Role.ROLE_ADMIN);
        user3.setRole(Role.ROLE_SELLER);

        Creator creator1 = createCreator(user2);
        Creator creator2 = createCreator(user3);

        ProductsCategory productsCategory1 = createProductsCategory("3D 프린팅", "3D printing", 1);

        createProduct(creator1, productsCategory1, "[저축 set] 귀여운 돼지저금통과 통장 지폐", 15000);
        createProduct(creator2, productsCategory1, "디테일한 커피머신 스타벅스 마스트", 7700);

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

    @Transactional
    public Creator createCreator(final User user) {
        Creator newCreator = Creator.builder().user(user).build();
        return creatorRepository.save(newCreator);
    }

    @Transactional
    public ProductsCategory createProductsCategory(final String categoryName,
        final String categoryNameEn, final int seq) {
        ProductsCategory newProductsCategory = ProductsCategory.builder().categoryName(categoryName)
            .categoryNameEn(categoryNameEn).seq(seq).build();
        return productsCategoryRepository.save(newProductsCategory);
    }

    @Transactional
    public Products createProduct(final Creator creator,
        final ProductsCategory productsCategory, final String title,
        final int price) {
        Products newProduct = Products.builder().creator(creator).productsCategory(productsCategory)
            .title(title).price(price).build();
        return productsRepository.save(newProduct);
    }
}
