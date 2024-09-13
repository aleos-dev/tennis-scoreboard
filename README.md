
# Tennis Scoreboard 🎾

| 🚀 Welcome to the Tennis Scoreboard project! This pet project has significantly expanded my understanding of server-side rendering (SSR) and foundational web technologies like HTML, CSS, and Flexbox. Let me tell you, working with CSS layouts was like therapy—tough and frustrating at times, but part of the learning process. <br/><br>With this scoreboard, you can create, manage, and review tennis matches (both ongoing and finished). The project also includes functionality for editing player profiles. Plus, pagination is here so you don’t have to endlessly scroll through match results like you're doomscrolling on social media. Implementing pagination and filtering was a valuable experience.<br/><br/>I’ve achieved my goals with this project, and I’m excited to take on the next challenge! | ![](/.github/img/tennis-scoreboard-main.webp) |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------|

### 🚀 Live Demo

`Visit the live version of our project here:` [Tennis Scoreboard](https://tennis-scoreboard.ale-os.com).

### 📚 Learn More

`If you are interested in learning more about dev stuff, you may find my article helpful, available here:` [My blog](https://ale-os.com/)

## 📖 Index

💡 [My Goals (Beyond the Technical Task)](#main-goals)

💡 [Technologies and Frameworks](#technologies-and-frameworks)

📖 [Reflection on the project](#reflection-on-the-project)

- ⚙ [Match logic](#match-logic)
- [Validation](#hibernate-validator)
- [Application Context Management](#application-context-management)
- [Pagination and Filtration](#pagination-and-filtration)
- [The Pattern used](#the-patterns-used)

🛠 [Deployment](#deployment)

🌱 [How to run the app on the local machine with Docker](#how-to-run-app-on-the-local-machine)

📋 [Project Requirements](https://zhukovsd.github.io/java-backend-learning-course/projects/tennis-scoreboard/)

💬 [Share your feedback](#share-your-feedback)

🙌 [Acknowledgments](#acknowledgments)

## Main Goals

```
- Gain practical experience with Server-Side Rendering (SSR) using Java Server Pages (JSP).
- Get acquainted with HTML and CSS.
- Implement input validation using the Jakarta Validation Library.
- Enhance my understanding of typical application architecture.
- Select and practically apply some of the design patterns.
```

## Technologies and Frameworks
<table align="center">
  <tr>
      <td valign="middle">
          <img src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" width="50"><br>
            Java
    </td>
    <td valign="middle">
          <img src="https://github.com/onemarc/tech-icons/blob/main/icons/apachemaven.svg" width="50"><br>
            Maven
    </td>
    <td valign="middle">
          <img src="https://github.com/onemarc/tech-icons/blob/main/icons%232/flyway.svg" width="50"><br>
            Flyway
    </td>
    <td valign="middle">
        <img src="https://github.com/onemarc/tech-icons/blob/main/icons/hibernate-dark.svg" width="50"><br>
          Hibernate
    </td>
    <td valign="middle">
       <img src="https://github.com/onemarc/tech-icons/blob/main/icons/apachetomcat.svg" width="50"><br>
          Tomcat
    </td>
        <td valign="middle">
       <img src="https://user-images.githubusercontent.com/25181517/117533873-484d4480-afef-11eb-9fad-67c8605e3592.png" width="50"><br>
          JUnit
    </td>
    <td valign="middle">
        <img src="https://github.com/onemarc/tech-icons/blob/main/icons/jsp.svg" width="50"><br>
          JSP
    </td>
    <td valign="middle">
       <img src="https://github.com/onemarc/tech-icons/blob/main/icons/glassfish.svg" width="50"><br>
          Glassfish
    </td>
    <td valign="middle">
        <img src="https://github.com/onemarc/tech-icons/blob/main/icons/html.svg" width="50"><br>
          HTML
    </td>
    <td valign="middle">
        <img src="https://github.com/onemarc/tech-icons/blob/main/icons/css.svg" width="50"><br>
          CSS
    </td>
    <td valign="middle">
        <img src="https://user-images.githubusercontent.com/25181517/117207330-263ba280-adf4-11eb-9b97-0ac5b40bc3be.png" width="50"><br>
          Docker
    </td>
    <td valign="middle">
        <img src="https://user-images.githubusercontent.com/25181517/183868728-b2e11072-00a5-47e2-8a4e-4ebbb2b8c554.png" width="50"><br>
          CI/CD
    </td>
    <td valign="middle">
          <img src="https://github.com/h2database/h2database/blob/master/h2/src/docsrc/images/h2-logo.png" width="50"><br>
            H2
     </td>
  </tr>
</table>

- 🛠 **Maven**: A build and dependency management tool.
- 🗄 **H2 Database**: In-memory database.
- 📦 **Flyway**: Database migration and version control.
- 🏗 **Hibernate**: ORM framework to manage database interactions using Java objects.
    - 🔗 **Hibernate HikariCP**: Database connection pooling.
    - ✅ **Hibernate Validator**: Jakarta Bean Validation for input validation.
- 🔄 **ModelMapper**: Object-to-DTO mapping.
- 🎨 **HTML, CSS**: For providing the user interface and styling for the application.
- 💻 **Server-Side Rendering**:
    - 📝 **JSP (JavaServer Pages)**: Server-side templating for dynamic HTML.
    - 📚 **Standard APIs** for building dynamic web content.
        - ✨ **Jakarta Expression Language (EL) API**: Expression evaluation within JSP.
        - 🔖 **Jakarta Servlet Tag Library (JSTL) APIs**: Provides standard tags for common web tasks (e.g., loops,
          conditions, and formatting).
    - 🏗 **GlassFish (by Sun Microsystems)**: The reference implementation of Jakarta EE, including Jakarta EL and JSTL
      APIs.
- 🌐 **Servlet API**: For handling client-server communication.
- 🚀 **Tomcat**: A servlet container that serves as the implementation of the Servlet API and hosts the web application.

  ---

## Reflection on the project

---

### Match logic

The com.aleos.match package encapsulates the object-oriented logic for tennis match progression, including games, sets,
and entire matches. At the heart of this package is the AbstractStage class, which serves as the foundational template
for all match stages. This class implements the Template Method design pattern, providing a structured framework that
standardizes stage behavior while allowing flexibility through polymorphism and inheritance. Concrete implementations of
each stage—StandardGame, StandardSet, and StandardMatch—extend AbstractStage to apply specific scoring strategies and
manage state transitions specific to tennis.

> In attempting to use Redis for match management, I encountered fundamental architectural flaws where functionality and
> state were inseparable, leading me to switch to ConcurrentHashMap for the ongoing match state management due to easier
> handling and serialization challenges.

----

### Hibernate Validator

Using Hibernate ORM mandates a validation mechanism that adheres to the Java Bean Validation standard. For this, the
project utilizes Hibernate Validator (JSR 380), which is also commonly used in Spring applications.

#### Maven Dependencies for Hibernate Validator

```xml

<dependencies>
    <!-- Hibernate Validator -->
    <dependency>
        <groupId>org.hibernate.validator</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>8.0.1.Final</version>
    </dependency>

    <!-- Jakarta Expression Language API -->
    <dependency>
        <groupId>jakarta.el</groupId>
        <artifactId>jakarta.el-api</artifactId>
        <version>6.0.1</version>
    </dependency>

    <!-- Jakarta EL Implementation -->
    <dependency>
        <groupId>org.glassfish</groupId>
        <artifactId>jakarta.el</artifactId>
        <version>4.0.2</version>
    </dependency>
</dependencies>
```

**Jakarta Expression Language (EL)**:

Hibernate Validator requires an implementation of the Jakarta Expression Language for dynamically parsing expressions in
validation messages. Notably, this dependency is not included by default, likely in adherence to the principle of
minimal dependencies, allowing developers to choose their preferred EL implementation.

#### Default Factory Setup

If EL expressions are not needed, the `ValidationFactory` can be obtained as follows:

```java

@Bean
public Validator validator(@Bean(name = "validationFactory") ValidatorFactory validationFactory) {
    return Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();
}
```

This configuration uses `ParameterMessageInterpolator` instead of the default `ResourceBundleMessageInterpolator`.

#### Mechanics of Hibernate Validator

- **ValidatorFactory Initialization**: `ValidatorFactory` is a heavyweight object as it initializes and configures the
  validation infrastructure. This includes creating `Validator` instances, managing metadata, and caching information
  about validation constraints.

- **Using Validator**: When a `Validator` is used to validate an object, it scans the class for validation annotations
  and generates metadata for it. This metadata includes all validation constraints applicable to the class.

- **Metadata Caching**: To improve performance, metadata about validation constraints are cached, speeding up the
  validation process for subsequent validations of the same class.

- **Validation Process**: During validation, `Validator` uses reflection to analyze all fields, classes, and methods of
  the target object that bear validation annotations (e.g., `@NotNull`, `@Size`). If data does not meet the expected
  constraints, error messages are generated.

---

### Application Context Management

- **Implemented the Service Locator pattern via `BeanFactory`**, set up in `AppConfiguration` using custom `@Bean`
  annotations.

```java
public class AppConfiguration {

    @Bean
    public MatchService matchService(@Bean(name = "matchDao") MatchRepository matchDao,
                                     @Bean(name = "playerDao") PlayerRepository playerDao) {
        return new MatchService(matchDao, playerDao);
    }
    // Additional configuration methods...
}
```

- To ensure the beans are not lost, `BeanFactory` must be preserved within `ServletContext`. This is accomplished using
  the `ServletContextListener` interface.

```java
// Integration with ServletContext
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

---

### Pagination and Filtration

Implementing pagination and filtering turned out to be more challenging than initially anticipated. Initially, I found
myself confused by the multitude of parameters; to resolve this, I encapsulated them into a pair of objects for
filtration and pagination. However, distinguishing which parameters related to which aspect proved tricky. After several
iterations, I managed to refine the classes MatchFilterCriteria and PageableInfo into functional components.

The subsequent challenge was maintaining a functional repository layer during these evolutionary changes. This led me to
explore the Criteria API, which ultimately resolved all my issues related to retrieving the correct data based on
pagination and filters effectively.

```
Key Insights:

Indexing for Filtering Fields: It's crucial to implement indexes on filtering fields to enhance query performance and ensure efficient data retrieval.
Avoiding Offset in Pagination: Current observations suggest that using an offset in database queries can be counterproductive. It's more effective to construct pagination solutions based on specific WHERE clauses. This approach facilitates the use of indexes, whereas using offsets can lead to sequential processing that heavily loads the database.
```

---

### The Patterns used

- Factory Pattern (StageFactory)
- Strategy Pattern (ScoringStrategy<T extends Stage>)
- Observer Pattern (AbstractStage<T extends Stage>)
- Template Method Pattern (AbstractStage<T extends Stage)
- Service Locator Pattern (BeanFactory)

---

### Java Server Page

Java Server Pages (JSP) is a technology that mixes HTML, Expression Language (EL), and Java scriptlets, facilitating the
creation of dynamically generated web pages. Although JSP allows for scriptlets—Java code embedded directly within the
HTML—it is generally considered poor practice to mix presentation with logic extensively. Instead, the use of JSP tag
libraries is recommended. These libraries offer a concise way to embed logic into HTML using custom tags, reducing the
need for scriptlets.

JSPs serve as templates that are interpreted and compiled by the Tomcat server's JSP engine, Jasper. During this
process, HTML code is treated as strings, while EL expressions and custom tags are converted into executable Java code
within the generated servlet. This transformation allows dynamic content to be seamlessly integrated and delivered as
part of static HTML, ready for client-side rendering.

This architecture ensures that JSP remains a powerful yet manageable approach for generating dynamic web content, albeit
with considerations for best practices in web development.

---

### Hyper-Text-Markup-Language

HTML (HyperText Markup Language) is the standard markup language used to create and structure content on the web. It
forms the backbone of any web page, defining the skeletal layout and assembly of various page elements. HTML uses a
series of elements to encapsulate different types of content, ensuring web browsers know how to display each element
correctly.

Elements are defined by tags. These elements can be nested, allowing for complex web page structures. Attributes within
the tags provide additional settings or properties for the elements, such as setting a hyperlink’s destination with the
href attribute in an  <a/> tag.

HTML documents are essentially a hierarchy of elements, forming what is known as the DOM (Document Object Model), which
scripts like JavaScript can manipulate to dynamically change the displayed content. This makes HTML not just a static
skeleton but a dynamic foundation that interacts with user actions and scripting languages to create the rich,
interactive web experiences familiar today.

By defining the content structure, HTML lays the groundwork for CSS and JavaScript, which respectively style and add
interactivity to the web content, demonstrating HTML’s pivotal role in web development.

---

### Cascading Style Sheets

While HTML forms the hierarchical structure of web elements, CSS acts as their stylist, enabling the separation of
presentation from content. This separation enhances accessibility and flexibility, allowing for detailed control over
the layout, colors, and fonts without altering the HTML.

CSS operates by applying specific rules to targeted elements, reminiscent of inheritance in object-oriented
programming (OOP), where properties are dynamically determined based on a set of cascading rules. These rules dictate
how elements are styled and are applied based on criteria such as specificity, importance, and the source order of the
CSS declarations.

Mastering CSS involves understanding its complexity and the nuances of how styles are applied, which can be a
significant investment of time. My beginner attempting to use CSS cold only grasp the basics.

--- 

### Conclusion

Despite the errors I made, this project provided a wealth of new experiences and the opportunity to refine technologies
I was already familiar with. It was undoubtedly worth the time invested, and it stands out as one of the best projects I
have personally undertaken to date. This experience not only deepened my technical skills but also reinforced the value
of learning through practical application, making it a rewarding endeavor in my development journey.

---

## Deployment

### Overview

The deployment process for the Tennis-Scoreboard application is automated using GitHub Actions and follows a CI/CD
pipeline. The process ensures that the application is built, tested, and deployed.

The pipeline consists of two main stages:

1. Release Build Process
2. Deployment to Remote Server

Each of these stages is executed through GitHub Actions workflows configured in release.yml and deploy.yml.

#### Release Build Process

The release process is triggered when a tag is pushed to the repository. 

- Trigger: The process starts when a Git tag is pushed (on: push: tags: '*').
- Build and Packaging:
  - The source code is checked out from the repository.
  - The JDK 21 (Eclipse Temurin) environment is set up.
  - The Maven build tool is used to compile and package the application into a .war file.
  - After the build, the .war file (tennis-scoreboard.war) is copied to a designated directory.
  - This .war file is uploaded as a build artifact to GitHub for further processing.
  
- Release: 
  - Once the artifact is uploaded, the second stage of the job downloads the .war file.
  - The ncipollo/release-action GitHub action is used to publish a release in the GitHub repository. The artifact is
      attached to the release, and this release is accessible for deployment purposes.

#### Deployment to Remote Server
The deployment is handled through the deploy.yml file, which is triggered when the release process is completed (on:
   workflow_run).

- SSH into Remote Server: The workflow uses appleboy/ssh-action to remotely connect to the server where the application is hosted. 
- Tomcat Restart: The running Tomcat server is stopped using the shutdown.sh script.
- Backup the Current WAR File: If an existing WAR file is found, it is backed up by renaming it (e.g., tennis-scoreboard.war.bak). 
- Download Latest Release: The workflow dynamically retrieves the latest release from the GitHub repository using the GitHub API. The downloaded WAR file is placed in the Tomcat webapps directory.
- Set File Permissions: The file permissions are adjusted so that Tomcat can read and execute the new WAR file.
- Start Tomcat: The Tomcat server is restarted using the startup.sh script to deploy the new version of the application.

#### Environment Configuration
1. Front-End Server (Apache2):
The application is hosted on a remote server where Apache2 acts as the front-end web server. Apache handles all incoming HTTP (port 80) and HTTPS (port 443) traffic and serves as a proxy to the Tomcat server. Apache is responsible for managing SSL certificates and encrypting traffic.

2. Reverse Proxy to Tomcat:
Apache2 is configured to proxy all incoming requests for the subdomain (tennis-scoreboard.ale-os.com) to a Tomcat server running in the background on the same machine. Apache forwards the requests to Tomcat, which is bound to localhost. Tomcat handles the application logic and serves the content back to Apache, which then returns the response to the client.

3. Tomcat Configuration:
Tomcat runs as a background service on the remote server and is configured to serve the application via localhost. The WAR file is deployed in Tomcat's webapps directory, and Apache2 forwards requests to Tomcat using proxy directives. Tomcat does not handle SSL directly; instead, Apache takes care of encrypting the communication and proxies the traffic securely to Tomcat.

4. SSL Configuration:
SSL certificates are managed by Apache2 using Let's Encrypt. All HTTPS traffic is terminated at Apache, and the requests are then proxied to Tomcat over unencrypted HTTP.

#### Secrets Management
Several secrets are used within the CI/CD pipeline to ensure security:

- *SSH Keys*: The deployment process uses SSH keys (secrets.SSH_AWS) to connect securely to the remote server.
- *GitHub Token*: The release action uses a GitHub token (secrets.FOR_RELEASE_TOKEN) to create and upload releases.
- *Username and Host*: The username and host details for the remote server are stored securely in GitHub secrets (
secrets.USERNAME, secrets.REMOTE_HOST).

#### Conclusion
   This CI/CD pipeline automates the build, release, and deployment process for the Tennis-Scoreboard application. Using
   GitHub Actions, Maven, and Tomcat, the system ensures that each new release is built and deployed to production with
   minimal manual intervention.
---

## How to run app on the local machine

Follow these steps to get the project up and running on your local machine.

### Prerequisites

Before you begin, ensure you have the following software installed and available:

- **Docker:** [Download Docker](https://www.docker.com/products/docker-desktop)

Ensure that port **8080** on localhost are free for use.

### Installation

1. **Download and Extract the Project:**

- Download the project ZIP file from the repository.

- Unzip the file to your desired directory.

2. **Navigate to the Project Directory:**

    ```sh
    cd yourprojectdirectory
    ```

### Running the Project

1. **Build and Run the Docker Containers:**

    ```sh
    docker build -t tennis-scoreboard .
    docker run -p 8080:8080 --name tennis-scoreboard tennis-scoreboard
    ```

### Basic Usage

- **Access the Frontend:**

  Once the project is running, you can access the frontend at:

    ```sh
    http://localhost:8080/
    ```
  
## Share your feedback
I am continuously looking to refine my understanding and implementation of programming. If you have insights,
critiques, or advice—or if you wish to discuss any aspect of this project further—I warmly welcome your
contributions. Please feel free to [open an issue](https://github.com/aleos-dev/tennis-scoreboard/issues) to share
your
thoughts.

## Acknowledgments

`I want to express my gratitude to the author of the `[technical requirements](https://zhukovsd.github.io/java-backend-learning-course/projects/currency-exchange/)` for this project, `[S. Zhukov](https://t.me/zhukovsd_it_mentor)`. I would also like to thank [A. Shinchik](https://github.com/shinchik17), a fellow developer, for assisting with the testing of the first working version of this application.

`As a backend developer, I used to think handling millions of requests per second was hard... then I tried aligning a div in CSS.`

`When I started working on the frontend, I thought async requests were tough. Then I met my true enemy: browser compatibility.`
