git # Semesterprojekt: Wetter-Webanwendung

Dieses Repository enthält das Semesterprojekt für das Modul Software-Architektur. 
Es handelt sich um eine Wetter-Webanwendung, bestehend aus einem Server-Back-End und einem Front-End, 
das im Browser ausgeführt werden kann.


Die bereits in der Datenbank enthaltenen User mit Passwort und Rolle sind:


| Nutzer / Password | Rolle |Bemerkung|
| ------ | ------ | ------ |
|admin / passwd|Administrator|Rolle ADMIN, EMPLOYEE|
|user2 / passwd|Testnutzer|Rolle USER, EMPLOYEE|
|testPremium / premiumPasswd|Testnutzer mit Premium Status|Rolle PREMIUM mit gültigen Zahlungsinformationen, Abostart zum 02. Jan. 2024 mit offener Rechnung und Guthaben,EMPLOYEE, Zahlungsinformationen: 3475298374, Balance: 10|
|testPremiumBad / premiumPasswd|Testnutzer mit Premium Status|Rolle PREMIUM mit offener Rechnung und zu wenig Guthaben, EMPLOYEE Zahlungsinformationen: 7659656959, Balance: 0|
|User1 / passwd|Manager|Rolle MANAGER, EMPLOYEE|

Alle User haben folgende Emailadresse: swaprojektarbeitwetterapp@gmail.com


- Uml Diagramme (Klassen- und Komponetediagramm) sind im Repository unter 'Diagrams' einsehbar.


- Testdrehbuch ist im Repository unter 'Testscript' zu finden.
