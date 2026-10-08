# FlatSplit - Shared Expense & Flat Management App

A fully functional expense splitting and flat management Android application built using modern Android development practices.

## Features

### 📱 User & Auth
- 🔑 **Google Sign-In** using Firebase Authentication & Credential Manager SDK
- 👤 **User Profiles** with real-time user state management and avatar display

### 🏠 Flat & Room Management
- ➕ **Create Flat** with unique room names and generated shareable invite codes
- 🔑 **Join Flat** seamlessly using invite codes
- 🔄 **Flat Switcher** easily switch between multiple flats
- 👥 **Manage Flat Members** view room members, assigned roles (Owner/Member), and manage room members
- 🚪 **Leave or Delete Flat** safe leave/delete flow with balance checks to prevent un-settled debts

### 💸 Expense Tracking & Splitting
- ➕ **Add Expenses** specify title, amount, category, and paid-by member
- 👥 **Custom Expense Splitting** pick specific members and assign split amounts
- 🏷️ **Categorized Expenses** (Grocery, Food, Bills, Transport, Other)
- 📜 **Real-time Activity Feed** track recent transactions and expense histories

### 📊 Settlements & Balances
- ⚖️ **Real-time Balance Calculation** instantly see who owes whom and net balances
- 💡 **Smart Settlement Suggestions** optimized algorithm suggesting minimum payments to clear group debts
- ✅ **Confirm Settlement** mark suggestions as settled and update balances dynamically

### 🎨 UI & UX
- 🎨 **Modern Dark UI** designed with Jetpack Compose and Material 3
- ✨ **Shimmer & Animations** custom pulse animations, shimmer loading states, and polished transitions

## Tech Stack

- **Kotlin** - Primary language
- **Jetpack Compose** - Modern declarative UI with Material 3
- **MVVM & Clean Architecture** - Clean separation of concerns (Data, Domain, Presentation)
- **Dagger Hilt** - Dependency Injection framework
- **Firebase Auth & Firestore** - Authentication & real-time cloud database
- **Coroutines & Flow** - Asynchronous programming & reactive data streams
- **Coil** - Asynchronous image loading for Compose
- **Navigation Compose** - Type-safe, declarative app navigation
