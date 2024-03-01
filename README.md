# webapp11

# <p align="center"> Events Crafters </p>
## 📑Index
- [Phase 0](phase-0)
   - [Team members](#team-members)
   - [Team organization](#team-organization)
   - [Entities](#entities)
   - [Types of users](#types-of-users)
   - [User requirements](#user-requirements)
   - [Charts](#charts)
   - [Advanced Algorithms](#advanced-algorithms)
   - [Optional features](#optional-features)

- [Phase 1](#phase-1)
   - [Screens](#screens)
      - [Home Screen](#home-screen)
      - [Login Screen](#login-screen)
      - [Profile Screen](#profile-screen)
      - [Event Info Screen](#event-info-screen)
      - [Change Password Screen](#change-password-screen)
      - [Create Account Screen](#create-account-screen)
      - [Create Review Screen](#create-review-screen)
      - [Create Event Screen](#create-event-screen)
   - [Screens Flow](#screens-flow)

 - [Phase 2](#phase-2)
   - [Execution instructions](#execution-instructions)
   - [Diagrams](#diagrams)
   - [Possible actions to perform in the application](#possible-actions-to-perform-in-the-application)
   - [Recommendation algorithm](#recommendation-algorithm)
   - [Members Participation](#members-participation)
   - [Sample users](#sample-users)
***   
### ♨️Phase 0
***
 

## 👷Team members
| Name  | URJC Email| GitHub nickname |
| ------------- | ------------- | ----------- |
| Lucía Domínguez Rodrigo| l.dominguez.2021@alumnos.urjc.es | [@LuciaDominguezRodrigo](https://github.com/LuciaDominguezRodrigo) |
| Marcos Jiménez Pulido  | m.jimenezp.2021@alumnos.urjc.es  | [@MarJ03](https://github.com/MarJ03) |
| Tarek Elshami Ahmed | t.elshami.2021@alumnos.urjc.es  | [@TarekElshami](https://github.com/TarekElshami) |
| Álvaro Serrano Rodrigo | a.serranor.2021@alumnos.urjc.es  | [@AlvaroS3rrano](https://github.com/AlvaroS3rrano) |
| Ángel Marqués García | a.marquesg.2021@alumnos.urjc.es  | [@AngelMarquesGarcia](https://github.com/AngelMarquesGarcia) |

## 🧰Team organization

To help organize the team and keep track of tasks, we use [Trello](https://trello.com/invite/daw358/ATTIb7617ea77657d54f9a8a40ae8c00a941ADB6A0A5)

It is important to note that through this link, access will be granted to the workspace. There are multiple boards available. The phases developed within them are indicated with their own titles.

## 💻Entities
- Users
- Events
- Reviews
- Category
  
 The entities *user* and *event* will have **images** as attributes.
 
 The following diagram shows the system's entities, and how they relate to each other. It should be noted that, when there are M:N relationships, a new table is created to keep track of its instances, so the database will hold an extra table to keep track of the *Register* relationship shown below.
  ##### img: Entity-Relationship Diagram
  ![*1.1 entities*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/img/diagram.png)



## 🤖Types of users

 - **Anonymus** : users who have not logged into a registered account. They can access basic functionalities.
 - **Registered** :  users who have logged into a registered account. They can access a wide variety of funcionalities.
 - **Admin** :  type of user who has control over the platform, having the most extensive permissions.
   
## 🏁User requirements

| Requierement | Anonymus| Registered | Admin |
| ------------- | ------------- | ----------- | --------- |
|   View events  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  |
|   View reviews  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  | <p align="center"> ☑️ </p>  |
|   Create reviews  | | <p align="center"> ☑️ </p>  | |
|   Sign up to an event  | | <p align="center"> ☑️ </p>  | |
|   View event record  | | <p align="center"> ☑️ </p>  | |
|   Modify profile information  | | <p align="center"> ☑️ </p>  | |
|   Modify event information  | | <p align="center"> ☑️ </p>  | |
|   Delete published events  | | <p align="center"> ☑️ </p>  | |
|   View graphics  | | <p align="center"> ☑️ </p>  | <p align="center"> ☑️ </p>  |
|   Delete events (from other users)  | | | <p align="center"> ☑️ </p>  |
|   Block users  | | | <p align="center"> ☑️ </p>  |
|   Create/Modify tags  | | | <p align="center"> ☑️ </p>  |

## 🖱️ Additional Technologies
- **Send emails**: users will receive emails when signing up for a new event, and in some other cases.
- **Google Maps**: events will have a Google Maps embed showing the event location and its surroundings
  
## 📊Charts
- **Attendance chart**: users will be able to visualize an attendance chart for events they have published.
- **Categories chart**: users will be able to visualize a bar chart showing how many events there are for each tag

## 💡Advanced algorithms
- **Recommend by category**: This algorithm will choose the events to be featured in a registered user's home page based on the most common tags among events the user has attended.

## 🎯Optional features
-  **Sign-ups over time**: A chart displaying how many people have signed up for an event over time.
-  **Views chart**: A chart displaying the views an event has received over time.
-  **Filter by User Rating algorithm**: An algorithm that would promote events published by users whose previous events have been rated positively.
-  **PDF integration**: allow users to download a PDF holding event information upon sign-up.
-  **Chat**: give users the possibility of speaking to each other through the app, and/or create a chatroom for each event where users can discuss it.
***
### ♨️Phase 1
***

## 🖥️Screens

### 🏠Home Screen
This screen is where the users can find a list of upcoming events. The navbar contains two dropdown buttons and a search bar. One dropdown button will be used to filter by categories, and the other one to give the user access to more personalized screens (such as their profile). It's worth that all the displayed content is responsive, making it more accessible across different screens and devices


##### img 1: home screen
![*Home Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/home.jpg)

##### img 2: home screen - user dropdown botton
![*Home Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/home1.jpg)

##### img 3: home screen - filter dropdown botton
![*Home Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/home2.jpg)


### 🧑Login Screen
This is a simple screen containing a form that allows the user to log into their account.
##### img: Login Screen
![*Login Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/login.png)

### 💁Profile Screen
This screen has different functions depending on the type of user that accesses it (unregistered users don't have access to this screen).
- For Registered Users:
  - View the events that the user has created, recently joined, or already gone to.
  - Delete the user's account.
  - Modify the account's information.
Modify the account's information.
- For Administrators:
  - View all the events that the website has hosted or is currently hosting.
  - View all the event categories available for the users.
  - Create, update, delete event categories.
  - Delete events.

##### img 1: registered user profile screen part 1
![*User Profile Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/user_profile_1.png)

##### img 2: registered user profile screen part 2
![*User Profile Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/user_profile_2.png)

##### img 3: admin profile screen
![*Admin Profile Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/admin_profile_1.png)

##### img 4: update & create categories popup
![*Update & Create Categories Popup*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/admin_popup_cat.png)


### :information_source::ticket: Event Info Screen
This screen allows users to get detailed information of an event, as well as sign up for it if event tickets aren´t sold out yet. Specifically, this screen is made up of 5 large sections, which are:
- **Event information:** contains event main information (description, maximum capacity, price, date, hour, duration and additional relevant information about the event).
- **Tickets availability:** shows how fast tickets has been sold from a week ago to actual date, as well as available tickets amount and the different ticket options for the user to choose and buy.
- **Event placement:** contains the place where the event is held, as well as a map for easier placement.
- **Event host contact:** shows event host´s main information (name, nickname and photo). Also, this section includes a link to the host full profile information page.
- **Other events based on user´s preferences:** shows different events that the user would be interested in, based on the tags of the events the user recently signed up for.

##### img: Event Info Screen
![*EventInfoScreen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/EventInfoScreen.png)

### Change Password Screen
This is a simple screen containing a form that allows users to change their password. Only users who have logged in may access it. Alternatively, it could be accessed through one-time links.
##### img: Change Password Screen
![*Login Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/changePassword.png)

### ✏️Create Account Screen
This is a simple screen containing a form that allows users to create a new account. It can only be accessed if the user is not currently logged in.
##### img: Create Account Screen
![*Login Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/register.png)

### 🖋️Create Review Screen
This is a simple screen containing a form that allows users to leave a review for an event they have attended. In order to be accessed, the user needs to have logged in, they need to have signed up for the event, and the event must be completed.
##### img: Review Screen
![*Login Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/review.png)

### ▶️Create Event Screen
This is a simple screen containing a form that allows users to create a new event. Only users who have logged in may access it.
##### img: Create Event Screen
![*Login Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/createEvent.png)


## 🗺️Screens Flow
Upon entering the application, all users are presented with the Home screen.

- 🟦 For Unregistered Users:
  - From the Home screen, they can:
    - Register by clicking on the 'Register' button, leading them to the Register screen.
    - Log in by clicking on 'Log in', directing them to the Login screen.
    - View more details about events by clicking on 'See details' for an event, taking them to the Event Info screen.
  - From the Login screen, they can:
    - Navigate to 'Forgot my password' to reach the Change Password screen.

- 🔴 For Registered Users:
  - They can navigate to their Profile by clicking on 'My Profile' in the navigation bar, which takes them to the Profile screen.
  - On the Profile screen, users can:
    - Create new events by pressing the 'Create new event' button, leading to the Create Event screen.
    - View details of events from both the Profile and Home screens.
    - Make a review of an event by clicking on 'Rate' on the Profile screen, which leads to the Review screen.

- 🟪 For Administrators:
   - View details of events from the Home screen by clicking on 'See details', similar to unregistered and registered users.
   - Access their Admin Profile by clicking on 'My Profile' in the navigation bar from the Home screen, which allows them to manage active events, registered users, and the various available categories.
   - Navigate to the Category screen to create new categories.

##### img: Screens flow
![*Login Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/screenflow.png)

***   
### ♨️Phase 2  - Web with server-generated HTML and AJAX
***

## 🔣Execution instructions

### 👟 Steps 
1. Download this repository
2. Check Requirements 
3. Configure DataBase
4. Configure IDE
5. Run Application in the IDE
6. Go to https://localhost/8443/

### 📋 Requirements
- Java: JDK 17 --> https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- MySQL: v.8.0.36.0 (Explained in DataBase Configuration)
- Maven: 4.0.0
- Spring Boot 2.4.2
- IDE (explained in IDE Configuration)

### 💾 DataBase Config
- Download MySQL v.8.0.36.0
- Select default port (port 3306)
- Create a user with name root and password "password" with DB admin as user role
- Configure MySQL Server as Windows Service
- Grant full access to the user
- Create a new Schema named EventCrafters in the server using MySQL Workbench

### 🕹️ IDE Config
- We have used IntelliJ IDEA, bt it can be possible to use other IDE´S
- Install Maven and Spring for your IDE

## 🗃️ Diagrams

### 💽 DataBase diagram

![diagram](Phase2/diagrams/bbdd.png)
*DataBase Diagram*

### 🏠  Clases and templates diagram
![diagram](Phase2/diagrams/classTemplates.svg)
*Clases and templates Diagram*

## 🌐 ScreenFlow
![scrennflow](Phase2/diagrams/Phase%202%20screenflow.png)
*Screen Flow*

## 🧭 Possible actions to perform in the application
### Login
 To log in, access the screen that displays the corresponding form (/login). Enter the username and password, and you will be redirected to the personalized home page (since the session has been added).

### Recover password 
In the login form, there is an option to recover the password. You click on the link, and an email will be sent to the email associated with your username.

### Register
Through the login screen (via the "Sign Up" link) or the registration button on the default home screen, access will be provided to a user creation form. It's worth noting that all users will have the role of "user," with only one admin (created by the implementers themselves).

### Change profile photo
On the profile page, users are given the option to change their profile picture using a button identified by a camera icon. It should be noted that the page needs to be reloaded for the new profile picture to be updated.

### Update profile info
On the profile page, you can change the information by clicking on the "Editar Perfil" button. A form will appear where the user can update their data. It's worth noting that if any of the necessary login information has been changed, the user will be redirected to the login page to access with the new credentials. It should be noted that the admin will not be given the option to change their data.

### Ban/Unban
Similarly, the administrator will have access to these functions on the profile screen. It should be noted that these options will be exclusive to the administrator.

### Create event
This function will be accessible on the profile screen. In the "Mis eventos creados" section, the user should click on the button with the "+" symbol, which will redirect them to a form where they can enter event details.

### Edit/Delete Event
When accessing the event information screen, in the "Detalles del evento" section, if the user is the creator or an admin, they will be given the option to edit/delete the event through the "Editar evento"/ "Borrar evento" button.

### Make a Review
Once you have signed up for an event and it has ended, if you enter it, you will see the option to leave a review in "Retroalimentación" section.

### Join/ Unregister an Event 
If you enter an event that is not finished and you're not already signed up, you click on the "Apuntarse" button to register for the event. In the same way, if ypu have joined an event, you can unregistered by clicking on "Desapuntarse", in the same section that "Apuntarse" button.

### Ticket
If you want to generate your event ticket, you should go to the event information page for the event you've signed up for, and click on "Ticket"

### Attendance chart
Once the event is concluded, if the user is the creator or an administrator, they should enter the number of attendees in the event information screen. After providing this information, a chart representing the percentage of attendance to the event will be displayed.

## 🚀 Recommendation algorithm
An event recommendation algorithm has been implemented. It consists of two main parts:

 - **Unregistered user**: On the main screen, they will see the most popular events (popular being defined as those with the highest number of sign-ups).

 - **Registered user**: On the main screen, they will receive event recommendations based on their favorite category (the category to which the majority of events they have signed up for belong).

## ⚙️Members Participation

### 👩‍🔧Lucía Domínguez Rodrigo

| Commit | Description |
| :----: | :---------: |
| [1º](https://github.com/CodeURJC-DAW-2023-24/webapp11/commit/226ef18e81bf9cb71bb94c7983a8ad7ee3b60e0f) | Creation of function and form for updating profile |
| [2º](https://github.com/CodeURJC-DAW-2023-24/webapp11/commit/12a31ce8fe58129ddbb61d5fd132e9e33eea812e) | Registration created |
| [3º](https://github.com/CodeURJC-DAW-2023-24/webapp11/commit/b6514421d7e1f4871e44ea3c072c21e6c850611e) | Creation of function for deleting user|
| [4º](https://github.com/CodeURJC-DAW-2023-24/webapp11/commit/cdd8d0ca50112f13dec92a6d25a7cd44be314f71#diff-ac9d3bdeb47b0ea0b9031b88d650f2b8de0210593e1dfef2000df9749f92fe85) | Updating ban/unban function |
| [5º](https://github.com/CodeURJC-DAW-2023-24/webapp11/commit/fb74dd95bb074b7392f9551e26b1e0b1d400ce1c) | Creation of Models |

### 👨‍🔧 Ángel Marqués García

| Commit | Description |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |

### 👨‍🔧 Tarek ELshami Ahmed

| Commit | Description |
| :----: | :---------: |
| [1º]() | |
| [2º]() | | 
| [3º]() | |
| [4º]() | |
| [5º]() | |

### 👨‍🔧 Álvaro Serrano Rodrigo

| Commit | Description |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |

### 👨‍🔧 Marcos Jiménez Pulido

| Commit | Description |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |


## ⚔️ Sample users
### 🔑 @admin
- username: admin
- password: adminpass
  
### 🛡️ @user
- username: user
- password: pass

### 🛡️ @user
- username: user2
- password: pass

### 🛡️ @user
- username: user3
- password: pass

