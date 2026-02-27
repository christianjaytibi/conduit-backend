package com.example.conduit.entities;

import com.example.conduit.embeddable.FollowId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {
  @EmbeddedId
  private FollowId id;

  public Follow(UUID follower, UUID following) {
    this.id = new FollowId(follower, following);
  }
}
