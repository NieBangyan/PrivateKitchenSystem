# Basic Information

## 1.Private Kitchen Learning & Sharing System（Web-based Recipe Learning and Sharing Platform) is a web_based food social platform,offers users one-stop food learning and communication services, covering recipe browsing, publishing, search, comments, likes, etc. Ideal for home cooking learning, foodie community interaction, and recipe sharing.

## 2.Default Login Account
| Username   | Password | Role             |
| ---------- | -------- | ---------------- |
| admin      | 123456   | Administrator    |
| chef_wang  | 123456   | Regular User     |
| chef_an    | 123456   | Regular User     |

## 3.Development Tools
| Tool               | Purpose                     |
| ------------------ | --------------------------- |
| IntelliJ IDEA      | Backend development         |
| Visual Studio Code | Frontend development       |
| Navicat            | Database management        |

## 4.tech stack
### frontend teck
| Technology | Version | Function |
| ---------- | ------- | -------- |
| Vue 3      | 3.3.x   | Core frontend framework for building UI and interactive logic |
| Vite       | 4.4.x   | Build tool with fast startup and hot module replacement |
| Pinia      | 2.1.x   | State management for user login data, recipe data, etc. |
| Vue Router | 4.2.x   | Page routing management for SPA navigation |
| Axios      | 1.6.x   | HTTP client to send requests and call backend APIs |

### backend teck
| Technology | Version | Function |
| ---------- | ------- | -------- |
| Spring Boot | 2.7.14 | Core backend framework for providing RESTful APIs |
| Java       | 17      | Backend development language |
| MyBatis    | 2.3.0   | Persistence framework for executing database SQL operations |
| Druid      | 1.2.20  | Database connection pool for connection management |
| Maven      | 3.x     | Project build and dependency management |

### database teck
| Technology | Version | Function |
| ---------- | ------- | -------- |
| MySQL      | 8.0     | Relational database for data persistent storage |

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
│           Database (MySQL 3006)           │
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

| Service         | Address                |
| --------------- | ---------------------- |
| Frontend Page   | http://localhost:3000  |
| Backend API     | http://localhost:8080  |
| Database        | localhost:3306         |

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

2. Press `Ctrl + `` to open the terminal.

3. Install dependencies:
```
npm install
```

4. Start the frontend:
```
npm run dev
```

5.The following terminal output means the frontend has started successfully:

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

## Quick Recovery
```bash
# 1. Make sure MySQL service is running
# 2. Start the backend in IDEA
# 3. Restart the frontend
cd frontend
rm -rf node_modules
npm install
npm run dev
