= SecureLan
Author Thibault Daccord
:doctype: article
:encoding: utf-8
:lang: en
:toc: left
:numbered:


== introduction
Ce projet est une bibliotheque qui à pour principe d’aider la detection des applications java sur le même Lan. De plus il permet de crypter n’importe quel socket avce l’objet CrypSocket

[WARNING]
====
Ce projet est encore en version beta
====

[colophon]
== Utilisation

Pour l’appareil qui veut se jumeler (**client**)
[source,java]
System.out.println("lancement du client");
Detecteur detecteur= new Detecteur();
detecteur.lancerRecherche();
detecteur.actualiserScan();
ArrayList<Socket> coPossible = detecteur.getConnexionsPossibles();
System.out.println("le client a trouver "+coPossible.size()+ " serveur");
CryptSocket cryptSocket = new CryptSocket(coPossible);
cryptSocket.makeLink();
		
Pour l'appareil qui accepte la connexion (**serveur**)
[source,java]
System.out.println("lancement du serveur");
Detecteur detecteur= new Detecteur();
detecteur.lancerRepondeur();// cree un nouveau socket 
detecteur.actualiserScan();
ArrayList<Socket> coPossible = detecteur.getConnexionsPossibles();
System.out.println("le serveur a trouver "+coPossible.size()+ " serveur");
CryptSocket cryptSocket = new CryptSocket(coPossible);
cryptSocket.acceptLink();
