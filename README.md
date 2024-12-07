# Microservices avec Spring Boot avec Jhipster

Ce projet met en œuvre une architecture basée sur des microservices, avec les services suivants :

- **MS1** : Service pour la gestion des clients (port : 9091).
- **MS2** : Service pour la gestion des factures (port : 9092).
- **Gateway** : Passerelle pour assurer la communication entre les microservices (port : 9090).

## Fonctionnalités

### Service MS1 : Gestion des Clients
- MS1 permet la gestion des informations sur les clients, telles que leur **nom**, **email**, **numéro de téléphone**, etc.
- API disponible via la passerelle sur le port 9090.

### Service MS2 : Gestion des Factures
- MS2 permet la gestion des factures, avec des informations comme le **montant**, la **date de facturation**, et le **client associé**.
- Les informations sur les clients sont récupérées depuis MS1 en utilisant **Feign Client**.

### Gateway
- Elle permet de router les requêtes vers MS1 ou MS2 en fonction des endpoints.

## Ports des microservices

- **Gateway** : `http://localhost:9090`
- **MS1 (Customers)** : `http://localhost:9091`
- **MS2 (Bills)** : `http://localhost:9092`
ms-tp.postman_collection.json contient la collection pour tester
  
