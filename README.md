# FlatSplit - Shared Expense & Flat Management App

A fully functional expense splitting, UPI payment settlement, and flat management Android application built using modern Android development practices.

## Demo

![App Demo](app/src/main/res/drawable/demo.gif)

## Features

### 👤 User & Profile
- 🔑 **Google Sign-In** using Firebase Authentication & Credential Manager API
- 💳 **UPI ID Management** save and update your personal UPI ID (`yourname@bank`) to receive instant payments from flatmates
- 👤 **Custom Avatars** dynamic color avatar generation and Google profile photo loading via Coil
- 📝 **Feedback Submission** submit in-app feedback and bug reports directly
- 📖 **User Guide** interactive guide explaining flat management, expense splitting, and settlement flows

### 🏠 Flat & Room Management
- ➕ **Create Flat** create new flat rooms with auto-generated shareable invite codes
- 🔑 **Join Flat** easily join existing flat rooms using 6-character room codes
- 🔄 **Multi-Flat Switcher** switch between multiple flats seamlessly with a top dropdown bar
- 👥 **Manage Flat Members** view flat members, manage roles (Flat Owner vs. Member), and remove members
- 🚪 **Safe Leave / Delete Flat** built-in balance checks ensuring a user cannot leave or delete a room until all debts are settled (balance is ₹0)

### 💸 Expense Management & Custom Splitting
- ➕ **Add Expense** record expenses with description, total amount, date, and payer selection
- 👥 **Flexible Custom Splitting** pick specific flat members and assign exact individual split amounts
- 🏷️ **Categorized Expenses** Groceries, Food, Utility Bills, Transport, and Others with custom iconography
- 📜 **Real-time Activity Feed** view live expense logs and transaction history for the flat

### 📊 Settlements & UPI Payments
- ⚖️ **Real-time Balance Calculation** tracks individual net balances (who owes money vs. who gets money back)
- 💡 **Smart Debt Minimization Algorithm** automatically generates the minimum required transactions between room members to settle all group debts
- 📲 **Direct UPI App Integration** trigger external UPI apps (Google Pay, PhonePe, Paytm, BHIM) with pre-filled receiver UPI ID, payee name, and exact amount
- ⏳ **Two-Way Settlement Verification**:
  - **Payment Request**: Request flatmates to pay their pending dues
  - **Payment Initiation**: Payer marks settlement as paid via UPI or Cash with transaction details
  - **Receiver Confirmation**: Receiver verifies and clicks **Confirm** or **Mark Not Received** if payment fails
- 🔔 **Home Notification Requests** interactive payment requests and settlement alert dismissals on the home dashboard

### 🎨 UI & UX
- 🎨 **Modern Dark UI** clean dark theme crafted with Jetpack Compose & Material 3
- ✨ **Shimmer & Animations** custom orbit loading animations, pulse effects, and shimmer skeleton screens for smooth asynchronous state rendering
- 📐 **Adaptive Layouts** custom screen dimension and orientation utilities for consistent UI across devices

## Tech Stack

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI framework with Material 3 design
- **MVVM & Clean Architecture** - Separation of concerns across Presentation, Domain, and Data layers
- **Dagger Hilt** - Dependency Injection framework
- **Firebase Auth & Firestore** - Secure user authentication and real-time NoSQL database
- **Coroutines & Flow** - Asynchronous programming, reactive state management, and real-time database listeners
- **UPI Deep Linking** - Native Android UPI Intent integration for seamless in-app payment redirection
- **Coil** - Asynchronous image loading library for Jetpack Compose
- **Navigation Compose** - Type-safe, single-activity app navigation
