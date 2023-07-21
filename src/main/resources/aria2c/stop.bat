
set /p pid < netstat -aon|findstr  6800
taskkill /f /t /im  %pid%
pause