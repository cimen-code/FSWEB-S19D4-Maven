package com.workintech.s19d1.dao;

import com.workintech.s19d1.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ActorDao
        extends JpaRepository<Actor, Long> {
}
