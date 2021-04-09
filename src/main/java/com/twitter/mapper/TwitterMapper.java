package com.twitter.mapper;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.ReportingPolicy.ERROR;

import com.twitter.dto.TweetDTO;
import com.twitter.entity.TweetEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    nullValueMappingStrategy = RETURN_DEFAULT,
    unmappedTargetPolicy = ERROR
)
public interface TwitterMapper {

    TweetEntity toEntity(TweetDTO dto);

    TweetDTO toDto(TweetEntity entity);

    List<TweetDTO> toDTOList(List<TweetEntity> source);
}
