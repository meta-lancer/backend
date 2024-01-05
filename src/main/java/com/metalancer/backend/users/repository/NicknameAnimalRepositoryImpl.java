package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.NicknameAnimalEntity;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NicknameAnimalRepositoryImpl implements NicknameAnimalRepository {

    private final NicknameAnimalJpaRepository nicknameAnimalJpaRepository;

    @Override
    public String getRandomOne() {
        List<NicknameAnimalEntity> nicknameAnimalEntityList = nicknameAnimalJpaRepository.findAll();
        NicknameAnimalEntity nicknameAnimalEntity = getRandomNicknameAnimalEntity(
            nicknameAnimalEntityList);
        return nicknameAnimalEntity.getAnimal();
    }

    private static NicknameAnimalEntity getRandomNicknameAnimalEntity(
        List<NicknameAnimalEntity> nicknameAnimalEntityList) {
        Random random = new Random();
        return nicknameAnimalEntityList.get(
            random.nextInt(nicknameAnimalEntityList.size()));
    }
}
