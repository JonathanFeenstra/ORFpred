Voor het functioneren van ORFPred zijn de volgende stappen belangrijk:

- JRE van minimaal 1.8 (download van: http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html )
- NetBeans van https://netbeans.org/ of JetBrains Intellij https://www.jetbrains.com/idea/
- Maven (download van: https://maven.apache.org/ )
- Oracle Driver*:
	- Plaatst de twee bestanden in de "BESTANDEN VOOR INSTALLATIE" map in de volgende map: C:\{gebruikersnaam}\.M2 (voor Ubuntu: ~/.m2)
	  (Mocht settings.xml al aanwezig zijn dan kunt u deze vervangen of aanvullen met 
	  de inhoud van de ORFPred settings.xml bestand door het bestand te openen met bijv. Kladblok.exe van Windows of gedit van Linux)**
	- Voor Intellij: 
		- Na het openen van het ORFPred project (File-->Open...) klikt u met de rechtermuisknop op ORFPred in de projectveld
		- Vervolgens klikt u op New-->Data Scource-->{kies een willekeurige driver}-->OK-->{klik op de rode min teken links boven}-->{klik op de groene plus teken links boven}-->Oracle
		- Voer de volgende gegevens in:
			- Name: @cytosine.nl
			- Port: 1521
			- Host: cytosine.nl
			- SID: XE
			- User: owe7_pg9
			- Password: blaat1234
			- URL: jdbc:oracle:thin:@cytosine.nl:1521:XE
		- (als er staat "Update Latest Driver") dan klikt u deze aan)
		- Druk nu op Test Connection om de installatie te controleren
		- Druk als laatst op Apply en OK
	- Voor NetBeans:
		- Download de ojdbc8.jar (van http://www.oracle.com/technetwork/database/features/jdbc/jdbc-ucp-122-3110062.html ) en plaatst deze in de loacatie naar keuze
		- Open het ORFPred project (File-->Open Project...)
		- Klik op de services 
		- Rechtermuisknop op Databases --> New Connection --> {Kies de ORacle Thin Driver} -->add{ga naar de locatie van de ojdbc.jar bestand en voeg deze toe}-->Next
		- Vul hierbij dezelfe gegevens in als weergeven in de uitleg van Intellij
		- klik op Next-->Next-->Finish
		- Klik op het plusje naast Dependencies in het Project veld
		- Klik met de rechtermuisknop op ojbc8-12.2.01.jar --> Manually install artifact --> browse --> {ga naar de locatie van de ojbdc8.jar bestand en open deze}


*Oracle Driver kan pas geïnstalleerd worden na de installatie van Maven en NetBeans/Intellij
** Mocht de map niet aanwezig zijn dan kunt u controleren of u verborgen mappen kunt zien https://support.microsoft.com/nl-nl/help/14201/windows-show-hidden-files (n.v.t. bij Ubuntu)
   of controleer of uw Maven installatie gelukt is: 
	- Ga naar uw commandline/opdrachtprompt en typ: mvn
	- Druk op enter
	- Krijgt een Scanning for projects... bericht dan is uw installatie gelukt en 
          dan kunt u het best contact opnemen met de ORFPred staff en daarin uw situatie omschrijven.
	- Krijgt u een foutmelding dan kunt opnieuw proberen Maven te installeren en als dat nog steeds 
	  faalt dan kunt u contact opnemen met de ORFPred staff




Contactgegevens ORFPred Staff:
A.Janse@student.han.nl
