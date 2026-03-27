🌱 BeejVansh: Agri-Tech Seed Marketplace
BeejVansh is a peer-to-peer (P2P) mobile marketplace designed to empower farmers by facilitating the local trade of high-quality seeds. By leveraging real-time geolocation and a secure "Handshake" connection protocol, the app bridges the gap between seed sellers and buyers while prioritizing user privacy and data reliability in rural areas.

🚀 Key Features
Distance-Aware Sorting: Uses the Haversine Formula and GPS coordinates to sort seed listings, ensuring users find the closest available resources first.

Secure Handshake Protocol: Farmer contact details remain hidden until a "Connection Request" is explicitly approved by the seller, preventing spam and protecting privacy.

Dual-Role Activity Feed: A real-time, two-way tracking system for "Requests Sent" and "Requests Received," powered by Firebase Firestore listeners.

Offline-First Capability: Built with Room Database to ensure farmers can view listings and manage their profiles even in low-connectivity environments.

Cloud Integration: Seamless image hosting via Cloudinary and secure user authentication through Firebase Auth.

🛠️ Tech Stack
Language: Kotlin

UI Framework: Jetpack Compose (Declarative UI)

Architecture: MVVM (Model-View-ViewModel)

Local Database: Room (SQLite abstraction)

Backend/BaaS: Firebase (Firestore, Authentication)

Networking/Media: Coil (Image Loading), Cloudinary (Cloud Storage)

Asynchrony: Kotlin Coroutines & Flow

🏗️ Architecture Overview
The project follows a clean, reactive architecture to separate concerns and ensure maintainability:

Presentation Layer: Jetpack Compose for a modern, responsive UI.

Domain/ViewModel Layer: Manages UI state and coordinates data flow using Kotlin Flow.

Data Layer: A Repository pattern that intelligently switches between local Room caching and Firebase remote data.

📲 How It Works
List: A seller uploads seed details (Variety, Harvest Year, Quantity) and location.

Discover: Buyers see a list of seeds sorted by distance from their current location.

Connect: The buyer "knocks" on the seller's door via a Connection Request.

Trade: Once the seller "Approves" the request, both parties can view phone numbers and connect via WhatsApp to finalize the deal.
