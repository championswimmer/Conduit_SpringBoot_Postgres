package com.scaler.conduit.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "tags")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TagEntity extends BaseEntity {
    private String name;
}
