package com.metalancer.backend.common.config.security;

import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.entity.User;
import java.util.Map;
import lombok.Getter;

@Getter
public class UserAdapter extends PrincipalDetails {

    private User user;
    private AuthResponseDTO.userInfo userInfo;
    private Map<String, Object> attributes;

    public UserAdapter(User user) {
        super(user);
        this.user = user;
        this.userInfo = new AuthResponseDTO.userInfo(user);
    }

    public UserAdapter(User user, Map<String, Object> attributes) {
        super(user, attributes);
        this.user = user;
        this.attributes = attributes;
    }
}