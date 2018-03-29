ORFpred: 
Programma dat ORFs kan voorspellen op basis van een fasta of genbank DNA sequentie.

copyright: 
Damian Bolwerk, Jonathan Feenstra, Fini De Gruyter, Lotte Houwen & Alex Janse 2018.



Afwijkingen van het ontwerp met argumentatie:

Database
- Er is gekozen voor de Oracle SQL database, i.p.v. de MySQL cytosine.nl database. Hiervoor is gekozen, omdat tijdens een gesprek met de opdrachtgever (Martijn van der Bruggen) duidelijk werd dat de cytosine.nl database niet/moeilijk bereikbaar is voor een server connectie met locale bestanden. De poort staat open, maar de firewall is nog steeds aanwezig. Oracle lost dit probleem op door zowel de server als de firewall op de juiste poort te openen.

Systeemarchitectuur
- Er zijn afwijkingen in systeemarchitectuur. De eerste afwijking is de BioJava library. Deze staat aangegeven dat het werkt tijdens het parsen van het bestand en het vinden van de ORFs (module 1 en module 2). Dit is echter ook van toepassing op module 4 (BLAST).
- Er is gebruik gemaakt van andere versies en extra libraries. BioJava bestaat uit meerdere onderdelen. De onderdelen waarvan deze applicatie gebruik maakt is BioJava-Core, BioJava-Sequencing en BioJava-ws. Deze MOETEN minstens versie 4.2.4 zijn voor -core en -ws (zie website NCBI over http problemen: https://www.ncbi.nlm.nih.gov/home/develop/https-guidance/). In deze applicatie is gebruik gemaakt van BioJava-sequencing 4.0.0, BioJava-ws 4.2.11 en BioJava-core 4.2.11.
- Er is gebruik gemaakt van een extra dependency, die niet in Maven is opgenomen (vanwege downloadproblemen). De extra dependency is nodig om met de Oracle database te kunnen werken, genaamd: ojdbc8.jar. Deze is verwerkt in de classpath in de applicatie (onder target-classes). Dit zorgt ervoor dat de gebruiker zelf geen extra installatie hoeft te verrichten.

Software-architectuur (Class diagram)
- Er zijn wat aanpassingen in de classes/packages/datatypes in de UML class diagram.
- Package namen:
orfpred.blast (was BLAST)
orfpred.database (was Database)
orfpred.file (was File)
orfpred.gui (was GUI)
orfpred.sequence (Sequence)
(package "Frame" is er niet meer, maar verwerkt in orfpred.file) 
- Classes binnen packages:
Hieronder een overzicht van de huidige structuur. Deze is op sommigen punten anders dan de UML class diagram.
orfpred.blast
   -BLASTInputForm
   -Blast
   -BlastJob
   -BlastJobManager
   -BlastParser
   -JobAlreadyInQueue
   -ProgramException
   -TempFile
orfpred.database
   -DatabaseConnector
   -DatabaseDeleter
   -DatabaseLoader
   -DatabaseSaver
orfpred.file
   -FileHandler
   -FileType
orfpred.gui
   -BLASTPopUp
   -DBFileChooser
   -GUI
   -GUIUpdater
   -ORFPopUp
orfpred.sequence
   -ORF
   -ORFHighlighter
   -ReadingFramer

Technische gegevens structuur (ERD)
- Datatypen veranderingen: 
BLAST_RESULTAAT: Bitscore: NUMBER(10) --> NUMBER(10,1) Bitscores hebben 1 decimaal
BLAST_RESULTAAT: Query, identity, positives: NUMBER(3) --> NUMBER(5,2) Er BLAST_RESULTAAT: moeten percentages in en 100,00% vijf getallen bevat en BLAST_RESULTAAT: twee decimalen
BLAST_RESULTAAT: Gaps: NUMBER(2) --> INTEGER Voor de zekerheid als er veel gaps zijn
ORF: Frame NUMBER(1) --> VARCHAR2(2) Dit makkelijker is voor de app
- Key veranderingen
SEQUENTIE: Bestand_id PF --> F
ORF: maar één F i.p.v. twee F (alleen nog Seq_ID
- Attributen veranderingen
ORF: Sequentie_seq_id --> Seq_ID
ORF: Sequentie_Bestand_bestand_id --> weg
SEQUENTIE: Bestand_bestand_id --> Bestand_id
BLAST_RESULTAAT: ORF_orf_id --> ORF_id









