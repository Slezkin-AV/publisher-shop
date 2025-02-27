# Publisher-Shop
Проектная работа по курсу Microservice Architecture | OTUS

## Реализация
Реализованы сервисы:
 - user
 - billing
 - note
 - order
 - ware
 - delivery

Все сервисы реализованы на Java

Для каждого сервиса поднимается отдельная БД в поде сервиса.

## Техстек
Сервисы:			Java Spring Boot

БД:					PostgreSQL

Брокер сообщений:	Kafka


## Архитектурные паттерны
Event collaboration, Idempotence, Saga


## Установка
helm -n pub install <имя сервиса> 

или

./scripts/install.sh <имя сервиса>


## Тестирование
Используются сценарии, реализованные на Python.

Запуск:
./test/test.py <имя сценария>

Сценарии:
 - clear	- очитска БД
 - ware		- генерация товара на складе
 - order1	- успешный сценарий (создание пользователя, пополнение счета, отправка заказа)
 - order11	- предотвращение дубля запроса на создание счета
 - order2	- неуспешный сценарий (неуспешные оплата, отгрузка, доставка). Возврат денег и товара
 - order8	- предотвращение дублей в очереди сообщений

## Нагрузка
Используется Locust

Запуск:
./test/loc.sh

