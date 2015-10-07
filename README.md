# 2next
F team solutions
добавлена поддержка виртуальных хостов
Для включения небходимо добавить аттрибут sitename, где указывается имя хоста
т.е. при таком виде
<app>
  	<name mode="on" sitename="flabser.com">FlabserPromo</name>
</app>
приложение будет открыватся flabser.com:38779/Provider

<app>
	<name mode="on" >FlabserPromo</name>
</app>
приложение будет открыватся http://localhost:38779/FlabserPromo/Provider
