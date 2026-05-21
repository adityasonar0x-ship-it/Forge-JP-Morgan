@echo off
cd /d "c:\Users\mahes\OneDrive\Documents\Sigma WEB DEV\forage-midas"
set "JAVA_HOME=C:\Users\mahes\.antigravity-ide\extensions\redhat.java-1.54.0-win32-x64\jre\21.0.10-win32-x86_64"
set "PATH=%JAVA_HOME%\bin;%PATH%"
.\mvnw.cmd -Dmaven.test.skip=true clean install > build4.txt 2>&1
