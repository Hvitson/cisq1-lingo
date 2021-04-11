# Vulnerability Analysis

## A1:2017 Injection

### Description
sql statements die geinjecteerd worden in invoervelden. 
Via deze weg proberen zij informatie uit de database op te halen / aan te passen of toe te voegen.

### Risk
Risico binnen het project is vrij klein. Er wordt niet direct geinjecteerd, er zijn tests die input valideren.
Ook wordt er gebruik gemaakt van JpaRepository wat dit soort problemen ook afvangt.
Dit probleem is niet op te lossen met autenticatie en autorisatie.
Na de autenticatie of autorisatie kan een ieder alsnog een poging wagen te injecteren

### Counter-measures
Geen directe koppeling tussen invoerveld en database, prepared statements gebruiken, 
invoer valideren / controleren, testen.

## A9:2017 Using Components with Known Vulnerabilities

### Description
oudere libaries en dependencies gebruiken die outdated zijn, bugs bevatten en/of kwestbaarheden bevatten.


### Risk
Risico binnen het project is vrij klein. de dependancies zijn up-to-date en het zijn er niet veel.
Authenticatie en autorisatie helpt alleen als deze risicos achter authenticatie en autorisatie verschuilen.

### Counter-measures
Binnen dit project wordt gebruik gemaakt van dependancy checker en dependabot. 
Dit controleert de dependancies op bugs, foute, online informatie over dependancies en of de gebruikte versies de huidige zijn.

## A5:2017 Broken Access Control

### Description
Niet goed opvangen van roles/authenticatie. Hierdoor kan je bijvoorbeeld via de url op pagina's komt waar je niet hoort te komen.
door ongeverifieerde data op niet toegestane plekken de applicatie komen.
### Risk
Binnen dit project is dit geen probleem. er wordt geen gebruik gemaakt van authenticatie en autorisatie.

### Counter-measures
genereer jwt tokens voor gebruikers die inloggen en verwijder ze bij het uitloggen, 
valideer de gegevens/rollen van gebruikers voordat ze een functie uitvoeren/use case doorlopen,
access control.