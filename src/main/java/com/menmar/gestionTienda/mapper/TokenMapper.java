package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.TokenDTO;
import com.menmar.gestionTienda.persistence.entity.Token;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface TokenMapper {

  Token toEntity(TokenDTO tokenDTO);

  TokenDTO toDTO(Token token);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Token partialUpdate(
      TokenDTO tokenDTO, @MappingTarget Token token);
}