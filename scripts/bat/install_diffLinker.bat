git rev-parse --show-toplevel > tmp
set /p root_dir= < tmp
del tmp
cd %root_dir%\JavaWorkspace\diffLinker
mvn install
