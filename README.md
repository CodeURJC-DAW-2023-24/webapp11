# webapp11

# <p align="center"> Events Crafters </p>
## 📑Index
- [Phase 0](#phase-0)
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
      - [Login](#login)
      - [Profile Screen](#profile-screen)
      - [Event Screen](#event-screen)
      - [Change Paswword Screen](#change-password-screen)
      - [Create an account Screen](#create-an-account-screen)
   - [Screens Flow](#screens-flow)
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

To help organize the team and keep track of tasks, we use [Trello](https://trello.com/b/AJC8iT3W/daw)

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

### 🏠 Home Screen
This screen is where the users can find a list of upcoming events. The navbar contains two dropdown buttons and a search bar. One dropdown button will be used to filter by categories, and the other one to give the user access to more personalized screens (such as their profile). It's worth that all the displayed content is responsive, making it more accessible across different screens and devices


##### img 1: home screen
![*Home Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/home.jpg)

##### img 2: home screen - user dropdown botton
![*Home Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/home1.jpg)

##### img 3: home screen - filter dropdown botton
![*Home Screen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/home2.jpg)

### 💁 Profile Screen
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

### :information_source::ticket: Event Info Screen
This screen allows users to get detailed information of an event, as well as sign up for it if event tickets aren´t sold out yet. Specifically, this screen is made up of 5 large sections, which are:
- **Event information:** contains event main information (description, maximum capacity, price, date, hour, duration and additional relevant information about the event).
- **Tickets availability:** shows how fast tickets has been sold from a week ago to actual date, as well as available tickets amount and the different ticket options for the user to choose and buy.
- **Event placement:** contains the place where the event is held, as well as a map for easier placement.
- **Event host contact:** shows event host´s main information (name, nickname and photo). Also, this section includes a link to the host full profile information page.
- **Other events based on user´s preferences:** shows different events that the user would be interested in, based on the tags of the events the user recently signed up for.

##### img: Event Info Screen
![*EventInfoScreen*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/Phase1/layout%20design/readmeimg/EventInfoScreen.png)



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
![*Screens flow*](https://github.com/CodeURJC-DAW-2023-24/webapp11/assets/117302441/e8512eb5-4e53-48d2-a4de-3a2b1be5c6a0)
