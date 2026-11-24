@echo off
title generacion de war
set MIRUTA=%~dp0
cd  %MIRUTA%
.\mvnw.cmd -P prod,webpack,swagger,no-liquibase clean package -DskipTests=true
