package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.LocationDto;
import org.go.together.model.GroupLocation;
import org.go.together.model.Location;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupLocationMapper implements Mapper<GroupLocationDto, GroupLocation> {
    private final Mapper<LocationDto, Location> locationMapper;

    @Override
    public GroupLocationDto entityToDto(UUID requestId, GroupLocation entity) {
        GroupLocationDto groupLocationDto = new GroupLocationDto();
        groupLocationDto.setId(entity.getId());
        groupLocationDto.setGroupId(entity.getGroupId());
        groupLocationDto.setCategory(entity.getCategory());
        groupLocationDto.setLocations(entity.getLocations().stream()
                .map(location -> locationMapper.entityToDto(requestId, location))
                .collect(Collectors.toSet()));
        return groupLocationDto;
    }

    @Override
    public GroupLocation dtoToEntity(GroupLocationDto dto) {
        GroupLocation groupLocation = new GroupLocation();
        groupLocation.setId(dto.getId());
        groupLocation.setGroupId(dto.getGroupId());
        groupLocation.setCategory(dto.getCategory());
        groupLocation.setLocations(dto.getLocations().stream()
                .map(locationMapper::dtoToEntity)
                .collect(Collectors.toSet()));
        return groupLocation;
    }
}
