# Basic Information

## 1.Private Kitchen Learning & Sharing System（Web-based Recipe Learning and Sharing Platform) 
This is a web-based food social platform,oprovides users with one-stop food learning and community communication capabilities, covering recipe browsing, publishing, search, comments, likes, etc. Suitable for home cooking enthusiasts to learn recipes, interact in a foodie community, and share homemade dishes.
## Business Scenarios
Home cooking learning, community communication for food lovers, recipe sharing and collection.

## Function Overview
- User Management : Register, login, permission control (Administrator / Regular User), personal information modification
- Recipe Management : Publish, browse, multi-condition search (title + category + difficulty), view details, edit and delete
- Category Management : Display and filter cuisine categories
- Interaction : Like recipes, post comments, like comments, delete comments 
- Data Statistics : Number of featured recipes, categories, user publications and recipe comments 
- Personal Center : View personal information, change password 
- Admin Functions : View user list, add users, delete users 

## 2.Default Login Account
The following are preset test accounts for system access.

| Username   | Password | Role             |
| ---------- | -------- | ---------------- |
| admin      | 123456   | Administrator    |
| chef_wang  | 123456   | Regular User     |
| chef_an    | 123456   | Regular User     |

## 3.Development Tools

- IntelliJ IDEA : Backend development
- Visual Studio Code : Frontend development  
- Navicat : Database management   

## 4.tech stack
### frontend tech
| Technology | Version | Function |
| ---------- | ------- | -------- |
| Vue 3      | 3.3.x   | Core frontend framework for building UI and interactive logic |
| Vite       | 4.4.x   | Build tool with fast startup and hot module replacement |
| Pinia      | 2.1.x   | State management for user login data, recipe data, etc. |
| Vue Router | 4.2.x   | Page routing management for SPA navigation |
| Axios      | 1.6.x   | HTTP client to send requests and call backend APIs |

### backend tech
The table below lists the core technologies, corresponding versions and their roles used in the frontend project.

| Technology | Version | Function |
| ---------- | ------- | -------- |
| Spring Boot | 2.7.14 | Core backend framework for providing RESTful APIs |
| Java       | 17      | Backend development language |
| MyBatis    | 2.3.0   | Persistence framework for executing database SQL operations |
| Druid      | 1.2.20  | Database connection pool for connection management |
| Maven      | 3.11.0  | Project build and dependency management |

### database tech
| Technology | Version | Function |
| ---------- | ------- | -------- |
| MySQL      | 8.0     | Relational database for data persistent storage |

### Database Design (ER Diagram)
Below is the relational model of the core data tables in the system.
The database contains 4 data tables corresponding to 4 entities, supporting four core functions: user management, recipe management, category management and comment management.
<img width="1117" height="522" alt="image" src="https://github.com/user-attachments/assets/29d761a7-e265-475b-9e6c-25ddf8080f08" />

# Project Structure
## 1.Directory Structure

```bush
PrivateKitchenSystem/
│
├── backend/                          # Backend (Spring Boot)
│   ├── pom.xml                       # Maven config
│   └── src/main/
│       ├── java/com/kitchen/backend_springbook_kichen/
│       │   ├── BackendSpringbookKichenApplication.java   # Main entry
│       │   ├── config/               # Configuration classes
│       │   ├── controller/           # Controllers (Category, Comment, Recipe, User)
│       │   ├── service/              # Business logic
│       │   │   └── impl/             # Service implementations
│       │   ├── mapper/               # Data access interfaces
│       │   ├── entity/               # Entities (Category, Comment, Recipe, User)
│       │   └── util/                 # Utility classes
│       └── resources/
│           ├── application.yml       # App config
│           └── mapper/               # MyBatis XML mappings
│
├── frontend/                         # Frontend (Vue 3 + Vite)
│   ├── index.html
│   ├── package.json
│   ├── vite.config.js
│   ├── public/images/                # Recipe images
│   └── src/
│       ├── main.js
│       ├── App.vue
│       ├── api/                      # API calls
│       ├── router/                   # Routes
│       ├── stores/                   # State management (Pinia)
│       ├── views/                    # Page views
│       │   ├── HomeView.vue
│       │   ├── RecipeListView.vue
│       │   ├── RecipeDetailView.vue
│       │   ├── LoginView.vue
│       │   └── ...
│       ├── components/               # Reusable components
│       │   ├── NavBar.vue
│       │   ├── RecipeCard.vue
│       │   ├── SearchBar.vue
│       │   └── ...
│       └── utils/                    # Helper functions
│
├── database/
│   └── private_kitchen.sql           # Full database script
│
├── .gitignore
└── README.md
```

## 2.Core Modules

| Module      | Count       | Description             |
| ----------- | ----------- | ----------------------- |
| Controller  | 4           | Handle HTTP requests   |
| Service     | 4 + 4 impl  | Business logic          |
| Mapper      | 4           | Database operations    |
| Entity      | 4           | Data models            |
| Views       | 8           | Page UI                 |
| Components  | 6           | Reusable UI components  |

## 3.Architecture Workflow Overview
```bush
┌───────────────────────────────────────────┐
│              Client Browser               │
│           http://localhost:3000           │
└───────────────────────────┬───────────────┘
                            ▼
┌───────────────────────────────────────────┐
│        Frontend (Vue 3 + Vite 3000)       │
│    Views  │  Components  │  Stores        │
│      Axios Request → Backend API          │
└───────────────────────────┬───────────────┘
                            ▼
┌───────────────────────────────────────────┐
│        Backend (Spring Boot 8080)         │
│  Controller:  Req → Validate → Service    │
│  Service:     Logic → Check → Mapper      │
│  Mapper:      Execute SQL → Return Res    │
└───────────────────────────┬───────────────┘
                            ▼
┌───────────────────────────────────────────┐
│           Database (MySQL 3306)           │
│  user / category / recipe / comment       │
└───────────────────────────────────────────┘
```
## Port Division
Each component of the system runs on a separate port without conflicts.

| Component             | Port | Description                     |
| --------------------- | ---- | ------------------------------- |
| Frontend (Vue 3)      | 3000 | User interface access entry     |
| Backend (Spring Boot) | 8080 | RESTful API service             |
| Database (MySQL)      | 3306 | Data storage service            |

## Access Address

- Frontend Page : http://localhost:3000  
- Backend API : http://localhost:8080 
- Database  : localhost:3306  

## Vite Proxy Configuration (Solve Cross-Origin Issues)

Configure the following settings in `vite.config.js`:

```
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```
#

## Install JDK 17
Download link: https://adoptium.net/

Select Temurin 17 (LTS) and download the Windows installer.

Run the installer and follow the on-screen prompts to complete the installation.

Verify the installation:
```
java -version
```
The output should show `openjdk version "17.x.x" `

## Install MySQL 8.0
Download link: https://dev.mysql.com/downloads/installer/

Select Windows (x86, 32-bit), MSI Installer.

Choose Developer Default during setup, then proceed with the default configurations.

Set your root password (please keep it in mind) and click Execute to finish the installation.

Verify the installation:
```
mysql --version
```
## Install Node.js
Download link: https://nodejs.org/

Choose the LTS (Long-term Support) version.

Run the installer and follow the on-screen prompts to complete the installation.

Verify the installation:

```
node -v   # Version v16.x.x or above is required
npm -v    # Version 8.x.x or above is required
```
# Local Startup Guide

## 1.Import Database (Navicat)
1. Create a new database named `private_kitchen` with character set `utf8mb4`.

2. Execute the SQL file: `database/private_kitchen.sql`

## 2.Start Backend (IntelliJ IDEA)
1. Open the project: `D:\PrivateKitchenSystem\backend`

2. Wait for Maven dependencies to be downloaded.

3. Run the main class: `BackendSpringbookKichenApplication.java`

**Console output as below indicates the application has started successfully:**
<img width="500" alt="image" src="https://github.com/user-attachments/assets/2707c2cf-f7f3-4e51-9c81-02a52f6af6c3" />

## 3.Start Frontend (VS Code)
1. Open VSCode → File → Open Folder → Select `D:\PrivateKitchenSystem\frontend`

2. Press `Ctrl + ` to open the terminal.

3. Install dependencies:
```
npm install
```

4. Start the frontend:
```
npm run dev
```

5. The following terminal output means the frontend has started successfully:
<img width="365" height="100" alt="image" src="https://github.com/user-attachments/assets/2f2d6819-4b98-45b4-bdf8-a5f1b5785a20" />


# Common Errors & Solutions
##  1. Backend Errors
| Error | Cause | Solution |
| ----- | ----- | -------- |
| **Port 8080 was already in use** | Port occupied | Modify the port in `application.yml`, or close the program occupying the port |
| **Access denied for user** | Incorrect database password | Check the password configuration in `application-dev.yml` |
| **Unknown database** | Database does not exist | Create the `private_kitchen` database in Navicat |
| **Failed to parse mapping resource** | Syntax error in Mapper XML | Check for unclosed tags and duplicate definitions in XML files |
| **No qualifying bean** | Bean injection failure | Check `@Service`, `@Autowired` and `@MapperScan` annotations |

## 2. Frontend Errors
| Error | Cause | Solution |
| ----- | ----- | -------- |
| **Port 3000 is already in use** | Port occupied | Modify the port in `vite.config.js` |
| **Cannot find module** | Missing dependencies | Run: `rm -rf node_modules && npm install` |
| **404 Not Found** | Wrong API path | Check Vite proxy settings and confirm the backend is started |
| **Blank page** | Component syntax error | Press F12 and view errors in Console |

## 3. Database Errors
| Error | Cause | Solution |
| ----- | ----- | -------- |
| **foreign key constraint fails** | Foreign key constraint conflict | Ensure the associated data exists, e.g. valid `category_id` in category table |
| **Duplicate entry** | Duplicate data | The username already exists, please use a new one |

## 4. Quick Error Location

- Frontend page error : Browser F12 → Console 
- Frontend network request : Browser F12 → Network 
- Backend error : IDEA console logs 
- Database error : Error messages in Navicat 

## 5. Quick Recovery
1. Make sure MySQL service is running
  
2. Start the backend in IDEA
  
3. Restart the frontend
```
cd frontend
rm -rf node_modules
npm install
npm run dev
```
