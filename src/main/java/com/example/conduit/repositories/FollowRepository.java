package com.example.conduit.repositories;

import com.example.conduit.embeddable.FollowId;
import com.example.conduit.entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
}
