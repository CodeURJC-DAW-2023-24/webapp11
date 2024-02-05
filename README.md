# webapp11

# <p align="center"> Events Crafters </p>
## 📑Index
- Phase 0
   - [Team members](#team-members)
   - [Team organization](#team-organization)
   - [Entities](#entities)
   - [Type of users](#type-of-users)
   - [User requirements](#user-requirements)
   - [Charts](#chart)
   - [Advance Algorithms](#advance-algorithms)
   - [Optional feautres](#optional-features)
   
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

To organize the team, we use [ Trello](https://trello.com/b/AJC8iT3W/daw)

## 💻Entities
- Users
- Events
- Reviews
- Category
  
 The entities that will carry **images** will be the user and event ones.
 
 It should be noted that, when there are M:N relationships, a third table arises, so there will be some more entity that are not specified in the following diagram.
  
  ![*1.1 entities*](https://github.com/CodeURJC-DAW-2023-24/webapp11/blob/main/diagram.png)



## 🤖Type of users

 - **Anonymus** : type of user who has not signed up, but still has access to some funtionalities.
 - **Registered** :  type of user who has signed up, and has access to a wide variety of funcionalities.
 - **Admin** :  type of user who has control over the platform, having the most extensive permissions.
   
## 🏁User requirements

| Requierement | Anonymus| Registered | Admin |
| ------------- | ------------- | ----------- | --------- |
|   View events  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  |
|   View reviews  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  | <p align="center"> ☑️ </p>  |
|   Put reviews  | | <p align="center"> ☑️ </p>  | |
|   Sign up to an event  | | <p align="center"> ☑️ </p>  | |
|   View event record  | | <p align="center"> ☑️ </p>  | |
|   Chat  | | <p align="center"> ☑️ </p>  | |
|   Modify profile information  | | <p align="center"> ☑️ </p>  | |
|   Modify events information  | | <p align="center"> ☑️ </p>  | |
|   Delete published events  | | <p align="center"> ☑️ </p>  | |
|   View graphics  | | <p align="center"> ☑️ </p>  | <p align="center"> ☑️ </p>  |
|   Delete events (form other users)  | | | <p align="center"> ☑️ </p>  |
|   Block users  | | | <p align="center"> ☑️ </p>  |
|   Add tags  | | | <p align="center"> ☑️ </p>  |

## 🖱️ Additional Technologies
- Send emails
- Google Maps
  
## 📊Chart
- **Attendance chart**: The user will be able to visualize an attendance chart for their event
- **Category chart**: The user will be able to visualize a bar chart with the different percentaje of the tags

## 💡Advanced algorithms
- **Recommend by category**: This algorithm will allow the user to filter events  based on the most frequently used tags in the events you participate in.

## 🎯Optional features
-  **Ticket Sales Chart**: A chart will be generated for the entire ticket sales you have generated for an event
-  **Visits chart**: Reflects the visits you have had at an event.
-  **Filter by User Rating algorithm**: Filter events based on user ratings.
-  **PDF**: make a PDF with the event information when the user sing up in the events
-  **Chat**
