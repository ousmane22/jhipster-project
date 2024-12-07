# Microservices avec Spring Boot

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
- La passerelle agit comme un point d'entrée unique pour les microservices.
- Elle permet de router les requêtes vers MS1 ou MS2 en fonction des endpoints.

## Exemple d'utilisation : Récupérer un client depuis une facture

Lorsque vous récupérez une facture via MS2, les informations sur le client (par exemple, **nom**, **email**, etc.) sont récupérées dynamiquement depuis MS1 à l'aide de **Feign Client**.

### Étapes :
1. Un appel est fait à l'endpoint de MS2 pour obtenir les détails d'une facture.
2. MS2 utilise Feign pour communiquer avec MS1 et récupérer les informations du client associé à la facture.
3. La réponse inclut les informations de la facture et du client.

## Ports des microservices

- **Gateway** : `http://localhost:9090`
- **MS1 (Customers)** : `http://localhost:9091`
- **MS2 (Bills)** : `http://localhost:9092`

## Exemples d'Endpoints

### 1. Récupérer une facture avec les informations du client

**Endpoint** :  
`GET http://localhost:9090/api/v1/bills/{id}`

**Réponse** :  
```json
{
    "billDate": "2024-12-06",
    "amount": 90000.0,
    "customerId": 1001,
    "customerName": "Ousmane Ndiaye",
    "customerEmail": "Ousmane@gmail.com",
    "customerPhone": "7777750177"
}
