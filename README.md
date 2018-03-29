ORFpred: 
Programma dat ORFs kan voorspellen op basis van een fasta of genbank DNA sequentie.

copyright: 
Damian Bolwerk, Jonathan Feenstra, Fini De Gruyter, Lotte Houwen & Alex Janse 2018.



Afwijkingen van het ontwerp met argumentatie:

<b>Database</b><br>
- Er is gekozen voor de Oracle SQL database, i.p.v. de MySQL cytosine.nl database. Hiervoor is gekozen, omdat tijdens een gesprek met de opdrachtgever (Martijn van der Bruggen) duidelijk werd dat de cytosine.nl database niet/moeilijk bereikbaar is voor een server connectie met locale bestanden. De poort staat open, maar de firewall is nog steeds aanwezig. Oracle lost dit probleem op door zowel de server als de firewall op de juiste poort te openen.

<b>Systeemarchitectuur</b><br>
- Er zijn afwijkingen in systeemarchitectuur. De eerste afwijking is de BioJava library. Deze staat aangegeven dat het werkt tijdens het parsen van het bestand en het vinden van de ORFs (module 1 en module 2). Dit is echter ook van toepassing op module 4 (BLAST).
- Er is gebruik gemaakt van andere versies en extra libraries. BioJava bestaat uit meerdere onderdelen. De onderdelen waarvan deze applicatie gebruik maakt is BioJava-Core, BioJava-Sequencing en BioJava-ws. Deze MOETEN minstens versie 4.2.4 zijn voor -core en -ws (zie website NCBI over http problemen: https://www.ncbi.nlm.nih.gov/home/develop/https-guidance/). In deze applicatie is gebruik gemaakt van BioJava-sequencing 4.0.0, BioJava-ws 4.2.11 en BioJava-core 4.2.11.
- Er is gebruik gemaakt van een extra dependency, die niet in Maven is opgenomen (vanwege downloadproblemen). De extra dependency is nodig om met de Oracle database te kunnen werken, genaamd: ojdbc8.jar. Deze is verwerkt in de classpath in de applicatie (onder target-classes). Dit zorgt ervoor dat de gebruiker zelf geen extra installatie hoeft te verrichten.

<b>Software-architectuur (Class diagram)</b><br>
- Er zijn wat aanpassingen in de classes/packages/datatypes in de UML class diagram.<br>
- Package namen:<br>
orfpred.blast (was BLAST)<br>
orfpred.database (was Database)<br>
orfpred.file (was File)<br>
orfpred.gui (was GUI)<br>
orfpred.sequence (Sequence)<br>
(package "Frame" is er niet meer, maar verwerkt in orfpred.file) <br>
- Classes binnen packages:<br>
Hieronder een overzicht van de huidige structuur. Deze is op sommigen punten anders dan de UML class diagram.<br>
<b>orfpred.blast</b><br>
   -BLASTInputForm<br>
   -Blast<br>
   -BlastJob<br>
   -BlastJobManager<br>
   -BlastParser<br>
   -JobAlreadyInQueue<br>
   -ProgramException<br>
   -TempFile<br>
<b>orfpred.database</b><br>
   -DatabaseConnector<br>
   -DatabaseDeleter<br>
   -DatabaseLoader<br>
   -DatabaseSaver<br>
<b>orfpred.file</b><br>
   -FileHandler<br>
   -FileType<br>
<b>orfpred.gui</b><br>
   -BLASTPopUp<br>
   -DBFileChooser<br>
   -GUI<br>
   -GUIUpdater<br>
   -ORFPopUp<br>
<b>orfpred.sequence</b><br>
   -ORF<br>
   -ORFHighlighter<br>
   -ReadingFramer<br>

<b>Technische gegevens structuur (ERD)</b><br>
- Datatypen veranderingen: <br>
BLAST_RESULTAAT: Bitscore: NUMBER(10) --> NUMBER(10,1) Bitscores hebben 1 decimaal<br>
BLAST_RESULTAAT: Query, identity, positives: NUMBER(3) --> NUMBER(5,2) Er BLAST_RESULTAAT: moeten percentages in en 100,00% vijf getallen bevat en BLAST_RESULTAAT: twee decimalen<br>
BLAST_RESULTAAT: Gaps: NUMBER(2) --> INTEGER Voor de zekerheid als er veel gaps zijn<br>
ORF: Frame NUMBER(1) --> VARCHAR2(2) Dit makkelijker is voor de app<br>
- Key veranderingen<br>
SEQUENTIE: Bestand_id PF --> F<br>
ORF: maar één F i.p.v. twee F (alleen nog Seq_ID<br>
- Attributen veranderingen<br>
ORF: Sequentie_seq_id --> Seq_ID<br>
ORF: Sequentie_Bestand_bestand_id --> weg<br>
SEQUENTIE: Bestand_bestand_id --> Bestand_id<br>
BLAST_RESULTAAT: ORF_orf_id --> ORF_id<br>








