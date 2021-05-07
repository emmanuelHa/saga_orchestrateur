# SAGA version Orchestrateur 
- 7 micro services (5 backend, 1 controller (client), 1 orchestrateur)
- 6 bases de données in memory (DB directory est crée au démarrage des micros service)  
- un cluster kafka
- swagger pour chaque micro service

Contrairement à la choregraphie avec un coordinateur/orchestrateur les services 
ne consomment que des commandes et renvoient des évènements à l'orchestrator 
qui se charge de l'ordonnancement et des règles métiers à appliquer.  

Le workflow  
 UI : /publish/order (command)   
 => OrderCreated => Orchestrateur => RegisterOrder => Order service => OrderRegistered  
 orderRegistered 
 => Orchestrateur => PrepareBill => Bill Service => Bill prepared
 => Orchestrateur => BookStock => Stock Service => Stockbooked or StockBookedFailed
 => Orchestrateur => CreatePayment => Payment Service => Payment created
 
 UI : /publish/payment/accept/ => AcceptPayment
 => Payment service => PaymentAccepted
 => Orchestrateur => CompleteBilling
 => Billing service => BillingCompleted
 => Orchestrateur 
...

## Pre-requis : 
install docker and docker compose :  
https://docs.docker.com/compose/install/
Lancer Zookeeper et Kafka cluster  
```
> docker-compose -f infra/src/main/docker/kafka.yml up -d
```

Pour stopper le cluster kafka :
```
> docker-compose -f infra/src/main/docker/kafka.yml down
```
Pour visualiser que les containers sont bien up :
```
> docker ps
```

Pensez à bien stopper le cluster à la fin de vos essais.  
Si au premier demarrage un des containers tombe alors stoppez puis redemarrez le tout. 
(relancer Zookeeper et Kafka cluster)

## Demo
Demarrer chacun des micro service :
```
> java -jar billing/target/billing.jar
> java -jar delivery/target/delivery.jar
> java -jar order/target/order.jar
> java -jar payment/target/payment.jar
> java -jar stock/target/stock.jar
> java -jar clientui/target/clientui.jar
> java -jar clientui/target/orchestrateur.jar
```
 
1/ Ajouter un stock en base :
http://localhost:9003/swagger-ui/#/stock-controller/ajouterCommandeUsingPOST
Exemple :
```
curl -X POST "http://localhost:9003/stock/add" -H "accept: */*" -H "Content-Type: application/json" -d "{\"id\":15,\"quantity\":10,\"transactionId\":\"87138f20-7b33-452e-a191-12fd562ee96c\"}"
```

  
2/ Faites une commande : 
http://localhost:9007/swagger-ui/#/home-controller/orderNewUsingPOST
```
curl -X POST "http://localhost:9007/publish/order?address=14%20rue%20des%20champs&quantity=2" -H "accept: */*" -d ""
```
le transactionId est crée par le traitement lui même

3/ Vérifier que le stock est bien diminué de la quantité de votre commande :
http://localhost:9001/swagger-ui/#/
curl -X GET "http://localhost:9001/stocks" -H "accept: */*"

4/ Vérifier que le payment a bien été enregistré en base :
http://localhost:9004/swagger-ui/#/payment-controller/recupererListPaiementsUsingGET 
Notez son uuid (transactionId)
 
5/ Vérifier que la livraison a bien été préparée en base :
http://localhost:9006/swagger-ui/#/delivery-controller/recupererListPaiementsUsingGET
on remarque que :
```
"paymentAccepted": false,
"shipped": false  
```
6/ Accepter le payment avec le transactionId précédent
http://localhost:9007/swagger-ui/#/home-controller/acceptPaymentUsingPOST 
 
7/ Vérifier que la livraison a bien été emballée en base :
http://localhost:9006/swagger-ui/#/delivery-controller/recupererListPaiementsUsingGET
```
"stockBooked": true,
"paymentAccepted": true,
"shipped": true,
"billingCompleted": true
```
et que le payment http://localhost:9004/swagger-ui/#/payment-controller/recupererListPaiementsUsingGET 
est au status Accepted  
Vous devriez voir SHIPPED dans les logs.


8/ Même opération que précedemment mais au lieu d'accepter le paiment, refuser un NOUVEAU paiement :
http://localhost:9007/swagger-ui/#/home-controller/refusePaymentUsingPOST 

9/ vérifier que le payment http://localhost:9004/swagger-ui/#/payment-controller/recupererListPaiementsUsingGET
 est au "status": "Refused"

#### Lecture utile: 
- https://docs.spring.io/spring-kafka/reference/html/  
- https://thepracticaldeveloper.com/spring-boot-kafka-config/

#### RAF : 
- Ajouter des TI pour le cluster Kafka avec un embedded cluster 
- Il reste des corrections à apporter pour la stabilité et la gestion des erreurs imprévues  
- Rendre explicite la conf du nombre de retry, le delai de retry, avoir une DLQ ...
D'ordre générale NE PAS DEBUGGER des clients consumers kafka

## Conclusion : l'orchestrateur est plus lisible que la version chorégraphie.  
Spring kakfa n'est peut être pas adapté pour une implémentation des Sagas en entreprise car 
encore trop bas niveau.  
Voir Axon ou bien https://github.com/eventuate-tram/eventuate-tram-core

Ce repository se base sur le repository suivant :  
https://github.com/Spaceva/MasterClassSagaPattern/ en dotnet qui utilise 
le framework massTransit https://masstransit-project.com/




