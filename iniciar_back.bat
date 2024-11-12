@echo off
cd /d %~dp0

echo ==========================
echo Selecciona una opcion:
echo 1. Actualizar el backend (copilar)
echo 2. Iniciar el backend
echo ==========================
set /p opcion="Elige una opcion (1 o 2): "

if "%opcion%"=="1" (
    echo Actualizando el repositorio...
    mvn clean install
) else if "%opcion%"=="2" (
    echo Iniciando el backend...
    java -jar .\target\meetsounds-0.0.1-SNAPSHOT.jar
) else (
    echo Opcion no válida.
)

pause
