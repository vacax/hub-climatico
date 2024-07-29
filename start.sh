#!/usr/bin/env bash

# Variable de ambientes.
export URL_MONGO=""
export DB_NOMBRE=""
export PORT=""

# Levantando la aplicacion
./gradlew shadowjar && java -jar build/libs/hub-climatico.jar > salida.txt 2> error.txt &