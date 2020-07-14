OpenInterview
=========
#Description: 

Un fichier de log contient des lignes avec du texte séparé par un espace, formatées comme ceci:


<timestamp unix> <hostname> <hostname>
 
Par exemple:
 

1366815793 quark garak

1366815795 brunt quark

1366815811 lilac garak

Chaque ligne correspond à une connexion, depuis un serveur (<hostname> à gauche) vers un autre serveur (<hostname> à droite) à un instant donné. 

Les lignes sont approximativement triées par timestamp. L'approximation est de l'ordre de 5 minutes.

Le programme doit produire à chaque heure:

* la liste des serveurs qui ont été connectés à un serveur donné durant cette heure

* la liste des serveurs auxquels un serveur donné s'est connecté durant cette heure

* le serveur qui a généré le plus de connections durant cette heure
Le nombre de lignes dans le fichier de log peut être très important. Considérez une implémentation performante vis à vis du CPU et de la mémoire. 
Si certains aspects de ce problème ne vous semblent pas clairs, n'hésitez pas à faire des hypothèses pour les clarifier, en les mentionnant dans votre réponse. 

 
#Build -- mvn install

#CodeDescription
La conception de l'application est faite comme suit: 
le main se trouve dans AnalyticsParser.
1/ FileReader est lancé depuis un seul Thread (Lecture de toute la ligne du fichier)
2/ThreadPoolExecutor execute et fais le  parsing des données. 
NB: * Chaque  executor a son propre Buffers qui est réutiliser à chaque traitement de données.
* LineDecoder(t, h, h ) correspond aux trois elements que comprends une ligne ( TimeStamp==t, HOSTNAME==h)
* La configuration de journalisation est dans : logback.xml  
2 processeurs de journalisation et un processeur qui fais un count du temps de connection depuis et vers un serveur en utlisant une hashmap pour stocker les elements.
#Gesion de CPU
L'utilisation de Multitread et de Sychrinized pour eviter toute exclusion mutuelle.

#Run
java -jar AnalyticsParser.jar NomduFichier  hostFrom hostTo
NomDuFichier : est le nom du fichier de log à analyser
hostFrom - le nom du serveur de depart dont les données à archiver 
hostTo - le nom du serveur de destination dont les données à archiver

#Générer le fichier de log
FileWrite permet de créer un log prototype

#TestUnitaire 
AnalyticsParserTest 
Le serveur qui a fait le plus de connection est  : z , nombre de connection : 235



