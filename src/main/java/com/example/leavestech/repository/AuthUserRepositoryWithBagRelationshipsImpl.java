package com.example.leavestech.repository;

import com.example.leavestech.domain.AuthUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AuthUserRepositoryWithBagRelationshipsImpl implements AuthUserRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String AUTHUSERS_PARAMETER = "authUsers";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AuthUser> fetchBagRelationships(Optional<AuthUser> authUser) {
        return authUser.map(this::fetchRoles);
    }

    @Override
    public Page<AuthUser> fetchBagRelationships(Page<AuthUser> authUsers) {
        return new PageImpl<>(fetchBagRelationships(authUsers.getContent()), authUsers.getPageable(), authUsers.getTotalElements());
    }

    @Override
    public List<AuthUser> fetchBagRelationships(List<AuthUser> authUsers) {
        return Optional.of(authUsers).map(this::fetchRoles).orElse(Collections.emptyList());
    }

    AuthUser fetchRoles(AuthUser result) {
        return entityManager
            .createQuery("select authUser from AuthUser authUser left join fetch authUser.roles where authUser.id = :id", AuthUser.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AuthUser> fetchRoles(List<AuthUser> authUsers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, authUsers.size()).forEach(index -> order.put(authUsers.get(index).getId(), index));
        List<AuthUser> result = entityManager
            .createQuery(
                "select authUser from AuthUser authUser left join fetch authUser.roles where authUser in :authUsers",
                AuthUser.class
            )
            .setParameter(AUTHUSERS_PARAMETER, authUsers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
