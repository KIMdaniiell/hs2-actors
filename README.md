# Actors Service
Сервис доступа к действующим лицам системы (пользователи, игроки, команды и их менеджеры) и управления информацией о них.

## Как запустить
1. Собираем jar архив с приложением:

   ``mvn clean package``

2. Пересоздаём ***образ*** и запускаем контейнер

   ``docker-compose up --build``

* предварительно должна быть создана docker сеть `hls-lab-network`
  для взаимодействия сервисов приложения:

  ``docker network create hls-lab-network``


* предварительно должна быть создана docker сеть `z-actors-internal-network`
  для взаимодействия компонентов внутри микросервиса (*БД*, *liquidbase*, *spring приложение*):

  ``docker network create z-actors-internal-network``

* иногда нужно снести БД (очищаем соответствующий volume) 