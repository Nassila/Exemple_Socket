# Exemple_Socket
application Client/Serveur avec socket TCP et une autre en UDP.

1) Réalisation d'une communication client/serveur en socket TCP. Le client lit une chaine de caractères au clavier, si la chaine est égale à "quitter" alors la communication est interrompue, sinon si la chaine est differente d'une operation mathematique simple alors affichage d'un message d'erreur, sinon envoi de la chaine au serveur. le serveur fait les calculs selon l'operation demandée et renvoie le resultat au client qui l'affiche en console. utilisation de thread pour avoir la possibilité de connexion de plusieurs clients au même temps.

2) Réalisation d'une communication client/serveur en socket UDP. Le client lit une chaine au clavier et l’envoie au serveur sur le port 9876, le serveur attend un message du client et l’affiche et affiche aussi l’adresse et le port de la source du message, à la fin il renvoie un message au client.
