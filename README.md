# Lovevery Code Challenge

Build a native Android application that can POST to and GET messages from a service that has a REST interface.

## Design

The app has three main screens:
 1.⁠ ⁠*Messages List Screen:* Displays a list of messages with the ability to filter by author.
 2.⁠ ⁠*Compose Message Screen:* Allows users to compose and send messages with a user, subject, and message content.
 3.⁠ ⁠*Messages by User:* Search by user and displays a list of messages including the author, subject, and message text.

*Overall Approach:*
The app follows the Model-View-ViewModel (MVI) architecture and uses the Repository pattern for data management. It leverages the Kotlin Flow API for handling asynchronous operations and Retrofit for making network requests. Additionally, it implements the Inversion of Control (IoC) pattern to decouple the UI layer from the data access layer.

*Design Decisions and Trade-offs:*

 1.⁠ ⁠*MVI Architecture:*
   - The app follows the MVI architecture, which separates the UI into three components: Model, View, and ViewModel. This architecture ensures a clear separation of concerns and facilitates testing and maintainability.

2.⁠ ⁠*Repository Pattern:*
   - The repository layer abstracts data access and retrieval from the ViewModel. This separation allows for flexibility in data sources, such as a local database and remote server, and promotes testability.

 3.⁠ ⁠*Dependency Injection with Hilt:*
   - Hilt is used for dependency injection, which simplifies the management of dependencies and provides a clean way to inject ViewModel, Repository, and other dependencies into Composables.

 4.⁠ ⁠*Different Models for UI and Repository:*
   - The app uses different models for the UI and repository layers. This separation of concerns ensures that the UI models are tailored for presentation and user interaction, while the repository models are designed for data retrieval and storage.

 5.⁠ ⁠*IoC Pattern with MessageRepository Interface:*
   - The app implements the Inversion of Control (IoC) pattern by defining a ⁠ MessageRepository ⁠ interface in the UI package and providing its implementation in the repository package. This decouples the UI layer from the specific data source and allows for easy substitution of data sources in the future.

*Conclusion:*
This app is designed to provide a simple and user-friendly message board experience while adhering to modern Android development practices. The implementation of the Inversion of Control pattern, use of different models for UI and repository, and potential introduction of a use case layer demonstrate a commitment to separation of concerns and maintainability. These design decisions pave the way for future enhancements and improvements in the app's architecture and functionality.

## Testing Plan

  *Objective:*
The basic test plan aims to cover fundamental test scenarios and inputs to verify the core functionality of the MessageBoard app. Additionally, it outlines how testing can be automated for efficiency.

*I. Functional Testing:*

*1. Data Retrieval:*
   - *Description:* Verify that the app correctly retrieves messages by author and all messages.
   - *Inputs:* Author names (e.g., "dan," "bob"), no author specified.
   - *Automation:* Automated tests using JUnit and Mocking Framework to simulate data retrieval with predefined author values and without an author specified.

*2. Message Sending:*
   - *Description:* Ensure that the app can send messages successfully.
   - *Inputs:* User, subject, and message content.
   - *Automation:* Automated tests using JUnit and Mocking Framework to simulate message sending with various input combinations.

*3. UI Component Validation:*
   - *Description:* Validate UI components for correctness and responsiveness.
   - *Inputs:* Interaction with UI elements (e.g., buttons, text fields).
   - *Automation:* Automated UI tests using Espresso and AndroidJUnitRunner to interact with UI components and validate their behavior.


## Automation Testing

 - *Description:* Automation can be achieved using various testing frameworks and tools.
   - *Inputs:* Test scripts, test data, test environment configurations.
   - *Automation Approach:*
     - For UI testing: Use Espresso and AndroidJUnitRunner for UI component validation and end-to-end testing.
     - For unit testing: Utilize JUnit and Mocking Framework to test individual functions and components.
     - For performance testing: Employ network emulators like OkHttp MockWebServer to simulate network conditions.
     - For regression testing: Re-run existing test cases using automated test scripts.
     - For test reporting: Leverage testing framework reporting plugins (e.g., Allure) to generate automated test reports.
