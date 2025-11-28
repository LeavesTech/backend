package com.example.leavestech.repository;

import com.example.leavestech.domain.AuthUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AuthUserRepositoryWithBagRelationships {
    Optional<AuthUser> fetchBagRelationships(Optional<AuthUser> authUser);

    List<AuthUser> fetchBagRelationships(List<AuthUser> authUsers);

    Page<AuthUser> fetchBagRelationships(Page<AuthUser> authUsers);
}
