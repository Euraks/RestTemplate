package org.example;

import org.example.model.SimpleEntity;
import org.example.servlet.dto.IncomingDto;
import org.example.servlet.mapper.SimpleDtoMapper;

import java.sql.SQLException;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger( Main.class.getName() );

    public static void main(String[] args) throws SQLException {
        String description =  "description" ;
        IncomingDto incomingDto = new IncomingDto(description);
        SimpleEntity simpleEntity = SimpleDtoMapper.INSTANCE.map( incomingDto);
        System.out.println(simpleEntity);

    }
}