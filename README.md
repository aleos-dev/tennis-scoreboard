# Tennis Scoreboard 🎾 Система Подсчета Очков для Теннисного Табло

| 🚀 Добро пожаловать в проект "Теннисное табло"! Табло - пет-проект с изначальной целью, для меня, состоящей в знакомстве с JSP, Html, CSS и блочной-верстке. Но уже со старта я погрузился в проектирование модели для подсчета очков, где я также хорошо познакомился с дженериками, паттернами Стратегия и Наблюдатель и немножко попроектировал используя uml диаграммы. | ![](/.github/img/tennis-scoreboard-main.webp) |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------|

## 📖 Оглавление

💡 <b>Размышления о проекте</b>

- ⚙ [Движок для Матча](#движок-для-матча)

---

## Движок для Матча🎾

### Размышления о проектировании системы подсчёта очков в теннисных матчах

Когда я приступал к своему текущему проекту, я решил начать с самой простой задачи — реализации логики теннисного матча.
Однако создание нескольких формул и операторов условного перехода казалось слишком механическим и не вдохновляющим. Мне
хотелось чего-то более увлекательного — дизайна, отражающего все нюансы подсчёта очков в теннисе. Так и началась
декомпозиция Матча на отдельные сущности! 🎯

### Начальный дизайн и проблемы

Я начал с разработки интерфейсов и абстрактного класса, которые предполагались в качестве контейнеров для этих
сущностей. Этот подход был вдохновлён моим недавним опытом с CatalinaEngine, где я впервые оценил значимость
проектирования структур с вложенными классами, имеющими схожую функциональность. Изначально я рассматривал реализацию
двунаправленной связи, аналогичной той, что я наблюдал в CatalinaEngine, но в конечной версии проекта я отказался в
пользу однонаправленной связи Матч -> Сет -> Гейм. 🔗

Создавая этот абстрактный контейнер, я выделил отдельную сущность **Очки**, понимая, что на разных стадиях тенниса (
матчи, сеты и геймы) используются различные системы подсчёта очков. Например, сеты обычно считаются численно, тогда как
в геймах используются уникальные значения очков, такие как 15, 30 и "адвантаж". 🎾

Для унификации интерфейсов и классов `Матч`, `Сет` и `Гейм` я рассмотрел использование дженериков. Однако мой
ограниченный опыт работы с дженериками Java и их инвариантностью привёл к трудностям. Попытки применить операторы
ковариантности (`extends`) и контрвариантности (`super`) ещё больше усложнили и без того перегруженные интерфейсы. 📚

### Паттерны "Стратегия" и "Наблюдатель": свежий взгляд

После нескольких итераций я наткнулся на идею использования паттерна **Стратегия** — откровение, которое вдохнуло новую
жизнь в проект. 💡 Этот паттерн позволил инкапсулировать логику подсчёта очков в отдельных классах стратегий. Вместе с
этим решением я решил реализовать интерфейс `ScoreManager` для работы с различными представлениями очков.

Этот стратегический подход упростил сущности, представляющие этапы соревнования (`Гейм`, `Сет`, `Матч`). Первоначальная
идея вложенности контейнеров для поддержания связи между этапами игры из-за использования дженериков была достаточно
сложна в реализации. И здесь на помощь пришёл паттерн **Наблюдатель**, который позволил мне использовать альтернативное
решение для оповещений о важных событиях. 🔔

Изучив паттерн **Наблюдатель**, я обнаружил, что библиотека стандартных классов Java устарела в этом аспекте, и теперь
вместо неё используется `PropertyChangeListener`. Реализация интерфейса `PropertyChangeListener` и интерфейса
`Stage<T>`, представляющего контракт для этапов соревнования, позволила обеспечить бесперебойное взаимодействие между
этапами через такие методы, как `firePropertyChange` и `addPropertyChangeListener`.

### Окончательный дизайн

С этим новым подходом кодовая база стала более элегантной и управляемой. 🎉 Единственной оставшейся задачей было создание
фабричных классов для этапов (`Гейм`, `Сет`, `Матч`) и поставщика стратегий. Поскольку эти компоненты представляют собой
всего лишь сервисы, они были разработаны в виде утилитных классов, таких как `StrategyProvider`.

В конечном итоге, я пришёл к следующему решению: каждый этап соревнования (`Гейм`, `Сет`, `Матч`) требует следующих
типов для инициализации:

- **Тип очков (`Score<S>`)**:
    - **Описание:** Это абстракция, представляющая различные типы подсчёта очков на разных этапах игры, будь то гейм,
      сет или матч. Например, в теннисном гейме используются значения очков, такие как 15, 30 и "адвантаж", в то время
      как в сете подсчёт осуществляется в числовом формате.
    - **Роль:** Этот тип используется для представления текущего состояния очков и позволяет гибко изменять и расширять
      логику подсчёта очков для различных игровых этапов.

- **Управление очками (`ScoreManager<S>`)**:
    - **Описание:** Интерфейс или класс, отвечающий за хранение и управление очками игроков на различных этапах игры. Он
      предоставляет методы для установки, получения и обновления очков.
    - **Роль:** `ScoreManager` инкапсулирует логику управления состоянием счёта, обеспечивая модульность и упрощая
      тестирование отдельных компонентов системы.

- **Стратегия подсчёта очков (`ScoringStrategy<Stage<S>>`)**:
    - **Описание:** Интерфейс или класс, который определяет логику подсчёта очков и определения победителя на каждом
      этапе игры. Различные стратегии могут быть реализованы для различных типов игр или правил.
    - **Роль:** Используя паттерн "Стратегия", `ScoringStrategy` позволяет легко изменять и настраивать правила подсчёта
      очков без изменения основных компонентов системы, обеспечивая гибкость и расширяемость.

- **Этап игры (`Stage<T>`)**:
    - **Описание:** Интерфейс, который представляет контракт для любого этапа соревнования, будь то гейм, сет или матч.
      Он определяет основные методы для управления состоянием этапа, такие как подсчёт очков и проверка завершённости.
    - **Роль:** `Stage<T>` обеспечивает стандартный интерфейс для взаимодействия с этапами игры, упрощая внедрение новых
      типов этапов и управление их состоянием.

- **Абстрактный класс (`AbstractStage<T, S>`)**:
    - **Описание:** Базовый класс, реализующий часть функциональности, общей для всех этапов игры. Он управляет
      состоянием этапа, определяет общую логику работы с очками и поддерживает уведомления об изменениях состояния с
      помощью паттерна "Слушатель" (Observer).
    - **Роль:** `AbstractStage` служит основой для конкретных реализаций этапов игры, таких как `StandardGame`,
      `StandardSet` и `StandardMatch`. Он инкапсулирует общую функциональность и упрощает разработку новых типов этапов.

- **Фабрика для создания подэтапов (`Factory<T>`)**:
    - **Описание:** Интерфейс или класс, используемый для создания новых экземпляров подэтапов, таких, как создание
      гейма в рамках сета или сета в рамках матча.
    - **Роль:** `Factory<T>` обеспечивает создание и инициализацию подэтапов, позволяя легко менять логику создания
      компонентов и обеспечивать их правильную настройку.

![](/.github/img/tennis-scoreboard-stage-uml.png)

Внедрение шаблонов проектирования увеличило количество классов, необходимых для этого решения, но позволило добиться
модульности и гибкости в комбинации компонентов, составляющих матч.🤖

### Заключение работы с движком

Это путешествие завершилось идеей доработать и выделить пакет Match в отдельную библиотеку. 📦 Запишу себе в todo. А
впереди создание инфраструктуры для проекта с дерзким названием "Теннисное табло". 📊

---

## Архитектура и начальная настройка

### Планирование архитектуры

- Начал с создания нескольких диаграмм последовательностей для определения необходимых компонентов.

- **Диаграммы последовательностей**: Начал с того, что нарисовал несколько диаграмм последовательностей, чтобы
  определить, какие компоненты мне понадобятся.
-

## Технические требования

- Необходимо использовать Hibernate ORM. Исходя из этого для валидации выбрал реализацию Java Bean Validation -
  Hibernate Validation(JSR 380), также используемую в Spring.

### Зависимости Maven для Hibernate Validator

```xml
<!-- Hibernate Validator -->
<b>
    <dependency>
        <groupId>org.hibernate.validator</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>8.0.1.Final</version>
    </dependency>

    <!-- API Jakarta Expression Language -->
    <dependency>
        <groupId>jakarta.el</groupId>
        <artifactId>jakarta.el-api</artifactId>
        <version>6.0.1</version>
    </dependency>

    <!-- Реализация Jakarta EL -->
    <dependency>
        <groupId>org.glassfish</groupId>
        <artifactId>jakarta.el</artifactId>
        <version>4.0.2</version>
    </dependency>
</b>
```

**Jakarta Expression Language (EL)**:

- В документации сказано, что Hibernate Validator требует реализацию Jakarta Expression Language для динамического
  парсинга выражений в сообщениях. Интересно, что эту зависимость не включили по умолчанию. Видимо, это сделано в
  соответствии с принципом минимальных зависимостей, чтобы разработчики могли сами выбирать нужную реализацию EL.

#### Фабрика по умолчанию

Если нет необходимости в использовании выражений EL, можно получить `ValidationFactory` следующим образом:

```java
      @Bean
      public Validator validator(@Bean(name = "validationFactory") ValidatorFactory validationFactory) {
          return Validation.byDefaultProvider()
                  .configure()
                  .messageInterpolator(new ParameterMessageInterpolator())
                  .buildValidatorFactory();
      }
```

Таким образом, будет использоваться `ParameterMessageInterpolator` вместо интерполятора по умолчанию
`ResourceBundleMessageInterpolator`.

#### Механика Hibernate Validator

**Инициализация ValidatorFactory**: `ValidatorFactory` является тяжеловесным объектом, так как он выполняет
инициализацию и настройку инфраструктуры валидации. Это включает создание `Validator`, управление метаданными и
кэширование информации о валидационных ограничениях.

**Использование Validator**: Когда `Validator` используется для валидации объекта, он сканирует класс этого объекта на
наличие аннотаций валидации и создает метаданные для этого класса. Эти метаданные включают информацию обо всех
валидационных ограничениях, применяемых к классу.

**Кэширование метаданных**: Для улучшения производительности метаданные о валидационных ограничениях кэшируются, что
значительно ускоряет процесс валидации при последующих вызовах для того же класса.

**Валидация**: В процессе валидации `Validator` использует рефлексию для анализа всех полей, классов и методов целевого
объекта, на которых установлены аннотации валидации (например, `@NotNull`, `@Size`). Если данные не соответствуют
ожиданиям, генерируются сообщения об ошибках.

### Управление контекстом приложения

- Ранее в проекте уже использовал ServletContext для управления зависимостями, нужно что-то другое.

- Управление зависимостями: Исследовал CDI (Contexts and Dependency Injection) для управления зависимостями в Java EE.
  Решил не использовать его в этом проекте, чтобы избежать дополнительной сложности, так как Tomcat не поддерживает его
  по умолчанию.

- Решил использовать паттерн ServiceLocator через BeanFactory, реализованный в AppConfiguration с использованием
  пользовательской аннотации @Bean.

```java
      public class AppConfiguration {

          @Bean
          public MatchService matchService(@Bean(name = "matchDao") MatchRepository matchDao,
                                           @Bean(name = "playerDao") PlayerRepository playerDao) {
              return new MatchService(matchDao, playerDao);
          }
          // ...
      }
```

  Чтобы не потерять наши бины, нужно сохранить `BeanFactory` в `ServletContext`. Для этого используем интерфейс `ServletContextListener`.

```java 
// Интеграция с ServletContext
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        injectFactoryBean(sce);
    }

    private static void injectFactoryBean(ServletContextEvent sce) {
        var factory = new BeanFactory(AppConfiguration.class);
        sce.getServletContext().setAttribute(AppContextAttribute.BEAN_FACTORY.name(), factory);
    }
}
```

## Модели данных

### Улучшения в сущности игрока:
- В профиль игрока были добавлены поля для страны и иконки. Это изменение направлено на развитие навыков работы с загрузкой файлов и реализацию специфичной валидации.

### Сущность матча и управление пагинацией:
- Для поддержки пагинации в сущность матча был добавлен timestamp.
- Создана встроенная сущность `MatchInfo`, предназначенная для хранения расширенной информации о матче.

### Валидация:
- Валидация поля `country` производится в соответствии со стандартом ISO 3166-1 alpha-2, если это поле присутствует.
- Основная валидация входных данных выполняется на уровне DTO. Критическое поле `imagePath` игрока подвергается дополнительной проверке на валидность в процессе обработки данных сервисом. Остальные поля, полученные с фронтенда, остаются без изменений и не требуют дополнительной валидации.

### Настройки Hibernate:
- Конфигурации Hibernate задаются в файле `META-INF/persistence.xml`. Используется база данных H2 in-memory и пул соединений HikariCP. Миграция данных осуществляется через Flyway.

## Текущие вызовы и размышления

### Интеграция с внешним хранилищем данных:
- При попытке использования внешнего хранилища данных, такого как Redis, для управления текущими матчами, я столкнулся с фундаментальными ошибками в архитектуре матча, где функциональность и состояние были неразделимы. Это осознание привело к отказу от Redis(из-за сложностей с сериализацией) в пользу применения `ConcurrentHashMap` для управления состояниями.

### Проблемы декомпозиции:
- Процесс декомпозиции порой ведёт к неожиданному усложнению структуры проекта, что затрудняет управление его компонентами. Несмотря на это, эффективная декомпозиция позволяет удерживать фокус и облегчает работу с отдельными частями системы.

## Итоги недели

- Эта неделя оказалась насыщенной, и не все запланированное удалось выполнить. Недостаток опыта иногда затрудняет точное планирование объема работы, особенно когда приходится сталкиваться с новыми технологиями и методиками. В некоторых случаях, понимание приходит быстро, в других — требует значительного времени на изучение.

- Цель по созданию рабочей веб-инфраструктуры переносится на следующую неделю. Это даст возможность более детально проработать все аспекты и убедиться в их корректной реализации.

Снова использовал ModelMapper, и, как обычно, остался недоволен этим инструментом. Несмотря на то что я вручную задавал конфигурацию маппинга, он всё равно требовал наличие конструктора по умолчанию, что меня удивило. В итоге пришлось воспользоваться кастомным методом.

Вся неделя прошла в попытках наладить пагинацию и фильтрацию. Мне потребовалось несколько попыток, прежде чем я смог инкапсулировать логику, связанную с пагинацией, и отдельно – с фильтрацией. Даже после этого у меня было несколько неудачных попыток использования timestamp для пагинации.

Мои решения предъявили довольно высокие требования к DAO слою, что в конечном итоге привело меня к использованию Persistence.criteria. Это довольно удобный API, однако легко сделать ошибки, если не понимать его суть. Я избежал некоторых проблем с индексацией, но подозреваю, что не всех, так как не особо в нем разобрался и просто адаптировал под свой случай.

Матчи, как текущие, так и завершенные, я решил объединить под одним абстрактным классом и работать с ними через этот класс, приводя типы по мере необходимости. Пока еще я не знаком с JSP, поэтому посмотрим, к чему приведет это решение.

В целом, я не успел всё, что задумывал, но очень доволен прогрессом. Основные открытия недели – это использование Criteria API и инкапсуляция логики для работы с пагинацией и фильтрацией.

Да, и надо бы подправить апи, но как я уже понял не особо полезно на это тратить время, так как оно меняется через день.

# API Documentation

This API allows you to interact with a Tennis scoreboard. Below are the available endpoints and their usage.

## Table of Contents

- [Match API](#match-api)
    - [Get All Matches](#get-all-matches)
    - [Start New Match](#start-new-match)
- [Player API](#player-api)
    - [Get All Players](#get-all-players)
    - [Get Player by Full Name](#get-player-by-full-name)
    - [Create a New Player](#create-a-new-player)
    - [Update Player](#update-player)
    - [Delete Player](#delete-player)
- [Score API](#score-api)
    - [Get Match Scores](#get-match-scores)

## Match API

### Get All Matches

**Endpoint:** `GET /api/v1/matches`

**Description:** Retrieves a list of matches. You can filter the matches by their status (e.g., finished, ongoing) and
paginate the results.

**Query Parameters:**

- `status` (optional): Filter matches by status. Possible values are `finished`, `ongoing`, or omit for all matches.
- `playerName` (optional): Filter matches by a specific player's name. Only matches where the specified player
  participated will be returned.

**Example Requests:**

1. **Retrieve all finished matches:**

   ```
   GET /api/v1/matches?status=finished
   ```

2. **Retrieve all ongoing matches:**

   ```
   GET /api/v1/matches?status=ongoing&pageSize=10
   ```

3. **Retrieve all matches (default):**

   ```
   GET /api/v1/matches
   ```

5. **Retrieve all ongoing matches involving a specific player by name:**

   ```
   GET /api/v1/matches?status=ongoing&playerName=arthur&pageSize=10
   ```

**Response Example:**

```json
{
  "content": [
    {
      "id": "uuid-of-ongoing-match",
      "playerOne": {
        "name": "Arthur Bok",
        "country": "MY",
        "playerImageUrl": "/images/0_arthur.jpg",
        "countryImageUrl": "/images/my_flag.jpg",
        "matches": "GET /api/v1/matches?fullName=arthur%20bok"
      },
      "playerTwo": {
        "name": "Richard Gorba",
        "country": "DE",
        "playerImageUrl": "/images/1_richard.jpg",
        "countryImageUrl": "/images/de_flag.jpg",
        "matches": "GET /api/v1/matches?fullName=richard%20gorba"
      },
      "status": "ONGOING",
      "score": {
        "": "format undefined at the moment"
      },
      "startedAt": "12:34:56"
    },
    {
      "id": "uuid-of-finished-match",
      "playerOne": {
        "name": "John Doe",
        "country": "US",
        "playerImageUrl": "/images/0_john.jpg",
        "countryImageUrl": "/images/us_flag.jpg",
        "matches": "GET /api/v1/matches?fullName=john%20doe"
      },
      "playerTwo": {
        "name": "Jane Smith",
        "country": "UK",
        "playerImageUrl": "/images/1_jane.jpg",
        "countryImageUrl": "/images/uk_flag.jpg",
        "matches": "GET /api/v1/matches?fullName=jane%20smith"
      },
      "status": "FINISHED",
      "winner": {
        "name": "Jane Smith",
        "country": "UK",
        "playerImageUrl": "/images/1_jane.jpg",
        "countryImageUrl": "/images/uk_flag.jpg",
        "matches": "GET /api/v1/matches?fullName=jane%20smith"
      },
      "info": {
        "details": "Match details here"
      },
      "date": "2024-08-08"
    }
  ],
  "page": 0,
  "size": 10,
  "totalPages": 1,
  "totalItems": 2
}
```

**HTTP Response Codes:**

- `200 OK` - The request was successful, and the list of matches is returned.
- `400 Bad Request` - The request was malformed or contained invalid parameters.
- `404 Not Found` - No matches were found for the specified query.
- `500 Internal Server Error` - An unexpected error occurred, such as a database issue.

### Start New Match

**Endpoint:** `POST /api/v1/matches`

**Description:** Creates a match between two specified players. If either player does not exist in the database, they
will be automatically created. The endpoint supports specifying the type of match (best of 3 or best of 5 sets) and an
optional match date.

**Request Body:**

- `playerOneName`: The name of the first player.
- `playerTwoName`: The name of the second player.
- `type`: The type of match. Possible values are `bo3` (best of 3 sets) or `bo5` (best of 5 sets).
- `playerOneCountry` (optional): The country of the first player.
- `playerTwoCountry` (optional): The country of the second player.

**Request Example:**

```json
{
  "playerOneName": "Arthur Bok",
  "playerTwoName": "Richard Gorba",
  "type": "bo5",
  "playerOneCountry": "MY",
  "playerTwoCountry": "DE"
}
```

**Response Example:**

```json
{
  "id": 10,
  "playerOne": {
    "id": 1,
    "name": "Arthur Bok",
    "country": "MY",
    "imageUrl": "/images/1_arthur.jpg",
    "matches": "GET /api/v1/matches?fullName=arthur%20bok"
  },
  "playerTwo": {
    "id": 2,
    "name": "Richard Gorba",
    "country": "DE",
    "imageUrl": "/images/2_richard.jpg",
    "matches": "GET /api/v1/matches?fullName=richard%20gorba"
  },
  "scores": "/api/v1/matches/10/scores",
  "type": "bo5",
  "status": "ongoing",
  "date": "2024-03-21"
}
```

**HTTP Response Codes:**

- `201 Created` - The match was successfully created.
- `400 Bad Request` - The request was malformed or contained invalid parameters.
- `500 Internal Server Error` - An unexpected error occurred, such as a database issue.

## Player API

### Get All Players

**Endpoint:** `GET /api/v1/players`

**Description:** Retrieves a list of all players. You can filter players by their name or country code and paginate the
results.

**Query Parameters:**

- `name` (optional): Filter players by a partial or full name using regex.
- `country` (optional): Filter players by country code (case-sensitive).
- `page` (optional): The page number to retrieve. Default is `0`.
- `size` (optional): The number of items per page. Default is `10`.

**Example Requests:**

1. **Retrieve all players from a specific country:**

   ```
   GET /api/v1/players?country=MY
   ```

2. **Retrieve a paginated list of players:**

   ```
   GET /api/v1/players?page=3&size=5
   ```

**Response Example:**

```json
{
  "content": [
    {
      "name": "Arthur Bok",
      "country": "MY",
      "playerImageUrl": "/images/0_arthur.jpg",
      "countryImageUrl": "/images/my_flag.jpg",
      "matches": "GET /api/v1/matches?fullName=arthur%20bok"
    },
    {
      "name": "Richard Gorba",
      "country": "DE",
      "playerImageUrl": "/images/1_richard.jpg",
      "countryImageUrl": "/images/de_flag.jpg",
      "matches": "GET /api/v1/matches?fullName=richard%20gorba"
    },
    {
      "name": "Jane Smith",
      "country": "UK",
      "playerImageUrl": "/images/1_jane.jpg",
      "countryImageUrl": "/images/uk_flag.jpg",
      "matches": "GET /api/v1/matches?fullName=jane%20smith"
    }
  ],
  "page": 0,
  "size": 10,
  "totalPages": 1,
  "totalItems": 3
}
```

### Get Player by Full Name

**Endpoint:** `GET /api/v1/players/{fullName}`

**Description:** Retrieves a specific player by their full name.

**Path Parameters:**

- `fullName`: The full name of the player to retrieve.

**Example Requests:**

```
GET /api/v1/players/Arthur%20Bok
```

**Response Example:**

```json
{
  "name": "Arthur Bok",
  "country": "MY",
  "playerImageUrl": "/images/0_arthur.jpg",
  "countryImageUrl": "/images/my_flag.jpg",
  "matches": "GET /api/v1/matches?fullName=arthur%20bok"
}

```

**HTTP Response Codes:**

- `200 OK` - The player was successfully retrieved.
- `404 Not Found` - The player with the specified full name was not found.

### Create a New Player

**Endpoint:** `POST /api/v1/players`

**Request Headers:**
Content-Type: multipart/form-data

**Description:** Adds a new player to the system.

**Request Body:**
The request should be of type multipart/form-data and include the following parts:

- name: (string) The name of the player.

- country: (string) The country of the player.

- playerImage: (file) The image file for the player.

**Example using curl:**

```curl
curl -X POST http://localhost:8080/api/v1/players \
-F "name=Arthur Bok" \
-F "country=MY" \
-F "playerImage=@/path/to/arthur.jpg"

```

**Response Example:**

```json
{
  "name": "Arthur Bok",
  "country": "MY",
  "playerImageUrl": "/images/0_arthur.jpg",
  "countryImageUrl": "/images/my_flag.jpg",
  "matches": "GET /api/v1/matches?fullName=arthur%20bok"
}


```

**HTTP Response Codes:**

- `201 Created` - The player was successfully created.
- `400 Bad Request` - The request was malformed or contained invalid parameters.
- `500 Internal Server Error` - There was an error processing the request.

### Update Player

**Endpoint:** `PATCH /api/v1/players/{fullName}`

**Description:** Updates an existing player's details.

**Path Parameters:**

- `fullName`: The name of the player to update.

**Request Body:**

Fields to update:

- `name` (optional): The new name of the player.
- `country` (optional): The new country code of the player.
- `imageUrl` (optional): The new image URL of the player.

**Example using curl:**

```curl
curl -X POST http://localhost:8080/api/v1/players \
-F "name=Bob Bok" \
-F "country=MY" \
-F "playerImage=@/path/to/arthur.jpg"

```

```json
{
  "name": "Bob Bok",
  "country": "MY",
  "playerImageUrl": "/images/0_bob.jpg",
  "countryImageUrl": "/images/my_flag.jpg",
  "matches": "GET /api/v1/matches?fullName=bob%20bok"
}

```

**Response Codes:**

- `204 No Content` - The player was successfully updated.
- `400 Bad Request` - The request was malformed or contained invalid parameters.
- `404 Not Found` - The player with the specified full name was not found.
- `409 Conflict` - The player name already is taken.
- `500 Internal Server Error` - There was an error processing the request.

### Delete Player

**Endpoint:** `DELETE /api/v1/players/{fullName}`

**Description:** Removes a player from the system.

**Path Parameters:**

- `fullName`: The full name of the player to delete.

**Response Codes:**

- `204 No Content` - The player was successfully deleted.
- `400 Bad Request` - The request was malformed or contained invalid parameters.
- `404 Not Found` - The player with the specified full name was not found.
  Sure! Let's update the API documentation for the scores endpoint using the enhanced representation. This will provide
  a more comprehensive and detailed view of the match scores, especially useful for applications that need real-time
  updates or more insights into each match's progress.

Here's the revised API documentation for the scores endpoint:

---

## Score API

### Get Match Scores

**Endpoint:** `GET /api/v1/matches/{id}/scores`

**Description:** Retrieves the scores for a specific match. This endpoint provides detailed set-by-set scores for both
finished and ongoing matches. For ongoing matches, it also includes the current game score to give a more granular view
of the match's progress.

**Path Parameters:**

- **`id`**: The ID of the match to retrieve scores for.

**Query Parameters:**

- **`set`** (optional): Filter scores by a specific set (e.g., `1`, `2`, `3`).
- **`playerName`** (optional): Filter scores by player name.
- **`country`** (optional): Filter scores by country code (useful if players have identical names).

**Response Example (for finished matches):**

```json
[
  {
    "playerName": "Arthur Bok",
    "country": "MY",
    "scores": [
      {
        "set": 1,
        "games": 6
      },
      {
        "set": 2,
        "games": 4
      },
      {
        "set": 3,
        "games": 6
      }
    ],
    "matchOutcome": "winner"
  },
  {
    "playerName": "Richard Gorba",
    "country": "DE",
    "scores": [
      {
        "set": 1,
        "games": 3
      },
      {
        "set": 2,
        "games": 6
      },
      {
        "set": 3,
        "games": 1
      }
    ],
    "matchOutcome": "loser"
  }
]
```

### Explanation

- **`playerName`**: The name of the player.
- **`country`**: The country code of the player.
- **`scores`**: An array of objects representing each set played. Each object contains:
    - **`set`**: The set number.
    - **`games`**: The number of games won by the player in that set.
- **`matchOutcome`**: Indicates whether the player was the "winner" or "loser" of the match.

**Response Example (for ongoing matches):**

```json
[
  {
    "playerName": "Arthur Bok",
    "country": "MY",
    "scores": [
      {
        "set": 1,
        "games": 6
      },
      {
        "set": 2,
        "games": 4
      },
      {
        "set": 3,
        "games": 5,
        "currentGameScore": 30
      }
    ],
    "matchOutcome": "in-progress"
  },
  {
    "playerName": "Richard Gorba",
    "country": "DE",
    "scores": [
      {
        "set": 1,
        "games": 3
      },
      {
        "set": 2,
        "games": 6
      },
      {
        "set": 3,
        "games": 5,
        "currentGameScore": 40
      }
    ],
    "matchOutcome": "in-progress"
  }
]
```

### HTTP Response Codes

- **`200 OK`** - The scores were successfully retrieved.
- **`400 Bad Request`** - The request was malformed or contained invalid parameters.
- **`404 Not Found`** - The match with the specified ID was not found.
- **`500 Internal Server Error`** - An unexpected error occurred, such as a database issue.
