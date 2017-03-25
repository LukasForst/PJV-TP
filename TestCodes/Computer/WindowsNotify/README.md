Little util to display notification on Windows 10. It will be part of the installed package.
Util is started as a proccess with params.:

ToastNotify.exe -t "text" -h "headline" -i "icon path" -n "name of displayed app"

Mandatory switches are: -t, -h
	-h - headline - displayed bold
	-t - text
	-i - icon - adds icon to the toast
	-n - name of the app