# webapp11

# <p align="center"> Events Crafters </p>
## 📑Index
- Phase 0
   - [Team members](#team-members)
   - [Team organization](#team-organization)
   - [Entities](#entities)
   - [Types of users](#types-of-users)
   - [User requirements](#user-requirements)
   - [Charts](#charts)
   - [Advanced Algorithms](#advanced-algorithms)
   - [Optional features](#optional-features)
   
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
