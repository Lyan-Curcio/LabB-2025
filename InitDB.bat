@echo off
cd /d "%~dp0"

java -jar "bin\db_init-1.0-jar-with-dependencies.jar"
pause