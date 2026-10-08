# GLP - Simulation domotique

Simulation d'une maison en Java avec une interface graphique Swing. Un habitant, le « maître », vit dans une maison de 4 pièces (chambre, salle de bain, salon, cuisine) : il a des besoins (énergie, faim, soif, hygiène) et suit une routine de semaine ou de week-end, avec des imprévus tirés au hasard (grasse matinée, visite surprise, coupure de courant, journée malade, journée pressée).

Projet de Génie Logiciel (GLP) réalisé en L2 Informatique à CY Cergy Paris Université, à trois : Mariam Traore, Agnies Sadli et moi (rapport daté d'avril 2026).

## Technos

- Java (JDK 11 ou plus, testé sous JDK 17 et 21)
- Swing pour l'interface
- log4j 1.2.17 pour les logs
- JUnit pour les tests

## Lancer le projet

Les jars ne sont pas dans le dépôt : télécharger `log4j-1.2.17.jar` (et JUnit avec hamcrest pour les tests) et les mettre dans un dossier `lib/` à la racine. Les instructions détaillées pour Eclipse sont dans `readme.txt`.

Pour lancer la simulation : exécuter `src/test/TestGame.java` ou `src/gui/LanceurGUI.java`. Les constantes de la simulation (vitesse, taille des blocs, jours simulés) sont dans `src/config/SimulationConfiguration.java`. Les tests sont dans `src/tests/`.

Le rapport du projet est dans `Rapport_Domotique.pdf` (sources LaTeX dans `doc/`).

## Captures d'écran

[À COMPLÉTER]

## Ce que j'ai fait

J'ai réalisé la grande majorité du projet. Certaines parties ont été confiées à mes coéquipiers : [À COMPLÉTER : lesquelles].

## Sprites

[À COMPLÉTER : origine et licence des images du dossier `src/sprites/`]
