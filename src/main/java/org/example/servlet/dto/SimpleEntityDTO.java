package org.example.servlet.dto;

import org.example.model.SimpleEntity;

import java.util.List;
import java.util.UUID;

public interface SimpleEntityDTO {

    SimpleEntity getSimpleEntityForId(UUID entityId);

    List<SimpleEntity> getAll();

}
