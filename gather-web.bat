RMDIR /Q /S distweb
MKDIR distweb
MKDIR distweb\jar
MKDIR distweb\jar\files-core
MKDIR distweb\jar\files-fpml
MKDIR distweb\jar\files-core\data
MKDIR distweb\jar\files-fpml\schemas
MKDIR distweb\doc
MKDIR distweb\files-fpml\examples\fpml4-7
COPY files-core\*.* distweb\jar\files-core
ERASE distweb\jar\files-core\releases.xml
RENAME distweb\jar\files-core\releases-full.xml releases.xml
COPY files-fpml\*.* distweb\jar\files-fpml
XCOPY /S /I files-core\data distweb\jar\files-core\data
XCOPY /S /I files-fpml\data distweb\jar\files-fpml\data
XCOPY /S /I files-fpml\schemas distweb\jar\files-fpml\schemas
XCOPY /S /I bin\*.* distweb\jar
XCOPY /S /I doc distweb\doc
XCOPY /S /I files-fpml\examples\fpml4-7 distweb\files-fpml\examples\fpml4-7
XCOPY /S /I files-fpml\examples\fpml5-9 distweb\files-fpml\examples\fpml5-9
CD distweb\jar
"C:\Program Files\Java\jdk1.8.0_31\bin\jar" cvf ..\handcoded.jar *
CD ..\..
RMDIR /Q /S distweb\jar
XCOPY /S /I lib distweb\lib
XCOPY /S /I manual distweb\manual
COPY misc-web\*.* distweb
COPY license.txt distweb
COPY readme.htm distweb
pause