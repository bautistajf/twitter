# Backend

Este proyecto se generó con Java 11 y Spring Boot 2.5.7.

Se utilizó un diseño de facade, service y repositorio que es el que accede a una BD en memoria.



## Development server

Navigate to `http://localhost:8081/`. 

Se utilizó la libreria twitter4j (version 4.0.7) utilizando stream api con el listener 
para obtener los tweets filtrados por idioma (parametro languages: es, fr, it), 
track (tracks: COVID) y cantidad de followers del usuario del tweet (folloers: 1500).

Estos parametros se encuentran en el fichero application.yml si hay que modificar los 
valores default:
```
twitter:
  tracks: COVID
  hashTagMoreUsed: 10
  followers: 1500
  languages: es,fr,it
  debug: true
```

El listener en su evento OnStatus, almacena en una BD en memoria los tweets filtrados por idioma y por los usuarios
que tengan mas de 1500 o la cantidad de followers configurados.

## Twitter services

##### Consultar los tweets
Retorna todos los tweets que se encuentran almacenados en la BD en memoria.
```
curl -X GET "http://localhost:8081/twitter-srv/1.0/tweets" -H "accept: application/json"
```
##### Respuesta
```
[
  {
    "id": 1,
    "user": "ManuelFranois1",
    "text": "RT @tcabarrus: #JCastex repart à l'offensive: les Français les plus réticents doivent comprendre que s'il y a un risque sur 1000 d'attraper…",
    "validation": false
  },
  {
    "id": 2,
    "user": "tonnirubio88",
    "text": "Otro ejemplo más.\nConsecuencia del efecto de medios que dedican más tiempo a asustar sobre efectos secundarios de la vacuna que sobre el Covid y sus secuelas",
    "validation": false
  }
]
```


##### Obtener los N hashtags más usados (default 10)
Retorna los 10 hashtags mas usados que hay en internet actualmente. 
```
curl -X GET "http://localhost:8081/twitter-srv/1.0/tweets/hashTagMoreUsed" -H "accept: application/json"
```

##### Respuesta
```
[
  "#128MilyarDolarNerede",
  "#MUSICFAIR",
  "心操くん",
  "#チーム藤井VSチーム稲葉",
  "Malang",
  "#えぺまつり",
  "#SelahattinDemirtas",
  "引き分け",
  "わけほー",
  "まりほー"
]
```


##### Obtener los tweets validados por usuario
Obtiene los tweets validados por usuario. Para esta funcionalidad hay que enviar el nombre de usuario
en el endpoint.
```
curl -X GET "http://localhost:8081/twitter-srv/1.0/tweets/validadosPorUsuario/HeraldoPue" -H "accept: application/json"
```

##### Respuesta
```
[
  {
    "id": 2,
    "user": "tonnirubio88",
    "text": "Otro ejemplo más.\nConsecuencia del efecto de medios que dedican más tiempo a asustar sobre efectos secundarios de la vacuna que sobre el Covid y sus secuelas",
    "validation": true
  }
]
```

##### Obtener los tweets validados por usuario agrupados por usuario
Obtiene los tweets validados agrupados por usuario
```
curl -X GET "http://localhost:8081/twitter-srv/1.0/tweets/allTweetsValidatedByUsers" -H "accept: application/json" -H "X-XSRF-TOKEN: 1eb4dag4um5dhg42pleaatp6am"
```

##### Respuesta
Retorna por cada usuarios los tweets validados.
```
{
  "_AJamaisLes1ers": {
    "id": 4,
    "user": "_AJamaisLes1ers",
    "text": "RT @Toooto_13: Le match de Thauvin à montrer dans tous les lieux de vaccination du Covid 19",
    "validation": true
  },
  "veronicafoxley1": {
    "id": 25,
    "user": "veronicafoxley1",
    "text": "RT @Stephduchili: EEUU: Debra Hunter de Florida que tosió a propósito a la cara de una paciente con cáncer en una tienda fue sentenciada a…",
    "validation": true
  }
}
```



##### Valida el tweet por ID
Cambia el estado de validacion a true a un tweet.
```
curl -X PUT "http://localhost:8081/twitter-srv/1.0/tweets/validar/2" -H "accept: application/json"
```

##### Respuesta
```
{
  "id": 2,
  "user": "Enrique_Rojas1",
  "text": "REPORTE SEMANAL COVID-MLB\nEn la semana, 2,494 tests, con 4 nuevos positivos (1 jugador MLB, 1 en sitio alterno, 2 empleados) para un 0.03%.\n Desde entrenamientos, 99,599 pruebas, 25 positivos (17 jugadores) para un 0.03%.",
  "localization": "Orlando, Florida",
  "validation": true
}
``` 

## Swagger
http://localhost:8081/twitter-srv/swagger-ui.html#/

## Management
http://localhost:8082/twitter-srv/management


## Docker pull and run

```
docker pull bautistajf/twitter_solution:latest

docker run -p 8081:8081 -p 8082:8082 bautistajf/twitter_solution      .
```

