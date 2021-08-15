package com.scaler.conduit.dtos.responses;

import com.scaler.conduit.entities.TagEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TagsResponse {
    List<String> tags;

    public static TagsResponse fromTagEntities(List<TagEntity> tagEntities) {
        return new TagsResponse(tagEntities.stream().map(tagEntity -> {
            return tagEntity.getName();
        }).collect(Collectors.toList()));
    }

}
