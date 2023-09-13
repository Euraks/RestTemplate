package org.example.model;


import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SimpleEntity {
    private UUID uuid;
    private String description;
}
