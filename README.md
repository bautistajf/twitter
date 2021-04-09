# Backend

Este proyecto se generó con Java 11 y Spring Boot 2.5.7.

Se utilizó un diseño de facade, service y repositorio que es el que accede a una BD en memoria.



## Development server

Navigate to `http://localhost:8081/`. 

Se utilizó la libreria twitter4j (version 4.0.7) utilizando stream api con el listener 
para obtener los tweets filtrados por idioma, track y cantidad de followers del usuario del tweet.

Estos 2 parametros se encuentran en el fichero application.yml para configurar:
```
twitter:
  tracks: COVID
  hashTagMoreUsed: 10
  followers: 1500
  languages: es,fr,it
  debug: true
```




## Twitter services

##### Consultar los tweets
```
curl -X GET "http://localhost:8081/twitter-srv/1.0/tweets" -H "accept: application/json"
```

##### Obtener los N hashtags más usados (default 10)
```
curl -X GET "http://localhost:8081/twitter-srv/1.0/tweets/hashTagMoreUsed" -H "accept: application/json"
```

##### Obtener los tweets validados por usuario
```
curl -X GET "http://localhost:8081/twitter-srv/1.0/tweets/validadosPorUsuario/HeraldoPue" -H "accept: application/json"
```

##### Valida el tweet por ID
```
curl -X PUT "http://localhost:8081/twitter-srv/1.0/tweets/validar/2" -H "accept: application/json"
```

##### Retorna
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

