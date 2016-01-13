xcopy ..\lib\Regub_local.jar .\ /S /I /C /Y
xcopy ..\fichiers\config.properties .\fichiers /S /I /C /Y

mkdir logs
cd logs
mkdir diffusions
cd ..
xcopy ..\logs\diffusions\* .\logs\diffusions /S /I /C /Y

start java.exe -cp Regub_local.jar;lib/mysql-connector-java-5.1.23-bin.jar EnregistrerDiffusions

rd /s /q logs
del /s Regub_local.jar