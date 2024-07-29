package edu.pucmm.pw.dtos;

import java.math.BigDecimal;

public record LecturaRequest(
        String estacion,
        String grupo,
        String fecha,
        BigDecimal temperatura,
        BigDecimal humedad
) { }
