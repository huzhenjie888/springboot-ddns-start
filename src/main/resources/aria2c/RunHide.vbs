set ws=WScript.CreateObject("WScript.Shell")
ws.run "cmd /c %cd%/aria2c/aria2c-win64.exe --conf-path=%cd%/aria2c/aria2.conf",0