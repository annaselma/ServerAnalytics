OpenInterview Log I.Zammel
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

 
#Build
Application is configured to be build by maven build system it is enough to go to main directory where pom.xml is located and execute 
	mvn install
This will compile all the sources and run existing tests and create executable jar file , this jar file require some dependencies to be executed and are located in local maven repository.

 
#Design
La conception de l'application est faite comme : 
1/ FileReader qui est lancé depuis un seul Thread (Lecture de toute la ligne du fichier)
2/ThreadPoolExecutor qui execute et fais le  parsing des données. 
* Chaque  executor a son propre Buffers qui est réutiliser à chaque traitement de données.
* LineDecoder(t, h, h ) correspond aux trois elements que comprends une ligne ( TimeStamp==t, HOSTNAME==h)

Processor 1 and 2 are a logging processors with a bit different strategy  but the main function is to log every data that is acceptable by passed filter in to the output logger after this logger is configured in logback.xml to be a rolling appender and data is send to the file 
this processor is not storing any additional data.

Processor 3 is a CountingProcessor and is configured to Count String words passed from data at the moment it counts connections from and to certain host. Processor is using hashmap to store CountElements maximum count is Long.MAX_VALUE  the size of the hashmap can be limited and as default it is limited to 100 000 elements if it is limited if size of the map is greater than specified limit it is truncated to the specified size containing largest counts . This solution  can generate counting errors so if it is necessary to have perfect count it is recommended not to use limit. Processor is flushed every hour (Assumption : but only if there is data coming in)

#Gesion de CPU
L'utilisation de Multitread et de Sychrinized pour eviter toute exclusion mutuelle.

#Run
To execute application :

java -jar LogParser.jar filename  hostFrom hostTo
 
filename - name of the file to process 
hostFrom - source host from which data will be logged 
hostTo - destination host from which data will be logged 
