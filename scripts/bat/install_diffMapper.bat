git rev-parse --show-toplevel > tmp
set /p root_dir= < tmp
del tmp
cd %root_dir%\JavaWorkspace\diffMapper
mvn install
