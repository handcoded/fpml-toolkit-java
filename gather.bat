RMDIR /Q /S dist
MKDIR dist
MKDIR dist\files-core
MKDIR dist\files-fpml
MKDIR dist\doc
XCOPY /S /I files-core dist\files-core
XCOPY /S /I files-fpml dist\files-fpml
XCOPY /S /I doc dist\doc
CD bin
"C:\Program Files\Java\jdk1.8.0_31\bin\jar" cvf ..\dist\handcoded.jar *
CD ..
XCOPY /S /I lib dist\lib
XCOPY /S /I manual dist\manual
COPY misc-fpml\*.* dist
COPY license.txt dist
COPY readme.htm dist
pause