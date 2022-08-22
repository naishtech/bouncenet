package com.covyne.bouncenet.core;

import com.covyne.bouncenet.admin.RegisteredUser;
import com.covyne.bouncenet.datastore.mongo.mapping.RegisteredUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BeanMapping {

    @Mapping(target = "id", ignore = true)
    RegisteredUserDTO toDTO(RegisteredUser registeredUser);

    RegisteredUser fromDTO(RegisteredUserDTO registeredUserDTO);

}
