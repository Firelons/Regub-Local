xcopy ..\lib\Regub_local.jar .\ /S /I /C /Y
xcopy ..\fichiers\config.properties .\fichiers /S /I /C /Y
mkdir videos

java.exe -cp Regub_local.jar;lib/mysql-connector-java-5.1.23-bin.jar TelechContratNuit

xcopy ..\videos\pause.mp4 videos\ /S /I /C /Y
rmdir /S /Q ..\videos

mkdir ..\videos
xcopy videos\*.mp4 ..\videos\ /S /I /C /Y

rmdir /S /Q videos
del /s Regub_local.jar

