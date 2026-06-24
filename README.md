# 
## Directory Structure

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

## Core Modules

| Module | count | Description |

| Controller | 4 | Handle HTTP requests |

| Service | 4 + 4 impl | Business logic |

| Mapper | 4 | Database operations |

| Entity | 4 | Data models |

| Views | 8 | Page UI |

| Components | 6 | Reusable UI |

## Architecture Workflow Overview
```bush
Architecture Workflow Overview

┌───────────────────────────────────────────┐
│              Client Browser               │
│           http://localhost:3000           │
└───────────────────────────┬───────────────┘
                            ▼
┌───────────────────────────────────────────┐
│        Frontend (Vue 3 + Vite)            │
│  Views  │  Components  │  Stores         │
│  Axios Request → Backend API              │
└───────────────────────────┬───────────────┘
                            ▼
┌───────────────────────────────────────────┐
│        Backend (Spring Boot)              │
│  Controller:  Req → Validate → Service    │
│  Service:     Logic → Check → Mapper      │
│  Mapper:      Execute SQL → Return Res    │
└───────────────────────────┬───────────────┘
                            ▼
┌───────────────────────────────────────────┐
│           Database (MySQL)                │
│  user / category / recipe / comment       │
└───────────────────────────────────────────┘
```
