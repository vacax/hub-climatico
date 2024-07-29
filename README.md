# Proyecto Hub Climático

Proyecto para recibir las peticiones climáticas de las estaciones en la clase ITT-363 Proyecto Integrador.

## Tecnologías

- Java 21
- Javalin
- HTMX 2
- MongoDB
- Morphia
- Leaflet

## Arranque

- 

## End Points

Es necesario enviar el header SEGURIDAD_TOKEN con el valor enviado.

```
curl --location 'http://localhost:7000/api/' \
--header 'SEGURIDAD_TOKEN: <<TOKEN>>' \
--header 'Content-Type: application/json' \
--data '{
    "grupo": "1",
    "estacion": "1",
    "fecha": "29/07/2024 11:41:00",
    "temperatura": 25.7,
    "humedad": 56
}'
```