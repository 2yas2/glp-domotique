  DOMOTIQUE — Simulation domotique
  README — Instructions d'exécution sous Eclipse


PRÉREQUIS

  - Java JDK 11 ou supérieur (testé sous JDK 17 et 21)
  - Eclipse IDE for Java Developers (2022-06 ou plus récent)
  - Bibliothèque log4j 1.2.17 (fichier .jar à télécharger)


ÉTAPE 1 — IMPORTER LE PROJET DANS ECLIPSE

  1. Lancer Eclipse.
  2. Aller dans : File > Import...
  3. Sélectionner : General > Existing Projects into Workspace
  4. Cliquer sur "Next".
  5. Cliquer sur "Browse..." et sélectionner le dossier racine
     du projet (celui qui contient le dossier "src/").
  6. Vérifier que le projet apparaît dans la liste et qu'il
     est bien coché, puis cliquer sur "Finish".


ÉTAPE 2 — CONFIGURER LE DOSSIER SOURCE

  Si Eclipse ne reconnaît pas automatiquement le dossier src/
  comme source :

  1. Clic droit sur le projet > Properties.
  2. Aller dans : Java Build Path > onglet "Source".
  3. Cliquer sur "Add Folder..." et cocher le dossier "src/".
  4. Cliquer sur "Apply and Close".


ÉTAPE 3 — AJOUTER LA DÉPENDANCE log4j

  Le projet utilise la bibliothèque log4j 1.2.17 pour la
  journalisation. Elle doit être ajoutée manuellement.

  A. Télécharger le fichier jar :
       https://repo1.maven.org/maven2/log4j/log4j/1.2.17/
       → log4j-1.2.17.jar

  B. Placer le fichier jar dans un dossier "lib/" à la racine
     du projet (créer ce dossier s'il n'existe pas).

  C. Dans Eclipse :
     1. Clic droit sur le projet > Properties.
     2. Aller dans : Java Build Path > onglet "Libraries".
     3. Cliquer sur "Add JARs..." (si le jar est dans le projet)
        ou "Add External JARs..." (s'il est ailleurs).
     4. Sélectionner le fichier log4j-1.2.17.jar.
     5. Cliquer sur "Apply and Close".


ÉTAPE 4 — VÉRIFIER LA CONFIGURATION DES LOGS

  Les fichiers de configuration log4j sont situés dans :
    src/log/log4j-html.properties
    src/log/log4j-text.properties

  Le fichier de log généré sera écrit ici :
    src/log/domotique-log.html

  Aucune modification n'est requise, la configuration est
  déjà prête.


ÉTAPE 5 — LANCER LE PROGRAMME

  Point d'entrée principal (classe main recommandée) :

    test/TestGame.java  →  package test, classe TestGame

  Pour lancer :
    1. Dans le Package Explorer, ouvrir le dossier "src/test/".
    2. Clic droit sur TestGame.java.
    3. Sélectionner : Run As > Java Application.

  Alternative : la classe LanceurGUI (package gui) possède
  aussi un main() et ouvre directement le menu de lancement.


DESCRIPTION DE L'INTERFACE

  Au lancement, une fenêtre de menu apparaît avec 3 boutons :

    ► Play     — Lance la simulation principale.
    ► Crédits  — Affiche les informations sur les auteurs.
    ► Quitter  — Ferme l'application.

  Une fois la simulation lancée, la fenêtre principale affiche :

    - La maison (38 colonnes × 22 lignes, blocs de 32px)
      avec les 4 pièces : chambre, salle de bain, salon, cuisine.
    - Un panneau de statistiques (besoins du maître).
    - Un panneau d'état des meubles.
    - Un panneau de notifications et de routine.
    - Les boutons Pause / Reprendre / Arrêter.


PARAMÈTRES DE LA SIMULATION

  Les constantes configurables sont dans :
    src/config/SimulationConfiguration.java

    GAME_SPEED          = 500   (ms entre chaque round)
    FPS                 = 144   (fréquence de rafraîchissement)
    DAYS_PER_WEEK       = 1     (jours de semaine simulés)
    DAYS_PER_WEEKEND    = 1     (jours de week-end simulés)
    BLOCK_SIZE          = 32    (taille d'un bloc en pixels)


LANCER LES TESTS UNITAIRES

  Les tests JUnit se trouvent dans : src/tests/

  Pour les exécuter tous :
    1. Clic droit sur le dossier "src/tests/".
    2. Sélectionner : Run As > JUnit Test.

  Ou lancer la suite complète via :
    src/tests/DomotiqueTestSuite.java


STRUCTURE DU PROJET

  src/
  ├── config/          Constantes de configuration
  ├── decision/        Prise de décision du maître
  ├── engine/
  │   ├── item/        Meubles, visiteurs, périphériques
  │   ├── map/         Carte, pièces, blocs
  │   ├── mobile/      Maître (entité mobile)
  │   └── state/       Besoins du maître (énergie, faim...)
  ├── gui/             Interface graphique (Swing)
  ├── log/             Journalisation (log4j)
  ├── process/
  │   ├── factory/     Fabriques (meubles, pièces, routines)
  │   ├── furniture/   Gestionnaire de meubles
  │   ├── game/        Construction de la simulation
  │   ├── imprevu/     Événements imprévus
  │   ├── mobile/      Gestionnaire principal (boucle de jeu)
  │   ├── room/        Gestionnaire des pièces
  │   ├── routine/     Plans de routine (semaine / week-end)
  │   └── service/     Services de navigation et d'interaction
  ├── test/            Classe de lancement (TestGame)
  └── tests/           Tests unitaires JUnit

