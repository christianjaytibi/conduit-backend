package com.example.conduit.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class FollowId implements Serializable {
  private UUID followerId;
  private UUID followingId;
}
