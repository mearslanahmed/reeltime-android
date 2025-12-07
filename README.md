# ReelTime - Movie Ticket Booking App

ReelTime is a modern and intuitive Android application for browsing movies and booking tickets. Built with Kotlin, it offers a seamless user experience with a clean UI and integrates with Firebase for real-time data handling and user authentication.

---

## Features

*   **User Authentication**: Secure login and registration using Firebase Authentication.
*   **Dynamic Home Screen**: A user-specific greeting and a curated main screen featuring banners, Top Movies, and Upcoming Movies loaded directly from Firebase.
*   **Movie Details**: A dedicated screen for each movie, showing its poster, summary, IMDb rating, cast, and more.
*   **Seat Selection**: An interactive seat selection screen where users can pick their desired seats from a grid.
*   **Ticket Generation**: Generates a virtual ticket with a unique QR code after a successful booking.
*   **My Tickets**: A list of all tickets purchased by the user, with the ability to view details or delete old tickets.
*   **Saved Movies**: Users can bookmark movies from the detail screen to a personal "Saved" list for later viewing.
*   **User Profile**: A profile screen displaying the user's name, email, and avatar, with a logout option.
*   **Modern UI**: Smooth animations and a blurred background effect for a polished user interface.

---

## Screenshots

| Home Screen | Detail Screen | Seat Selection |
| :---: | :---: | :---: |
| *Replace with your screenshot* | *Replace with your screenshot* | *Replace with your screenshot* |

| My Tickets | Saved Movies | Profile Screen |
| :---: | :---: | :---: |
| *Replace with your screenshot* | *Replace with your screenshot* | *Replace with your screenshot* |


---

## Technologies Used

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **Core**: Android SDK, AppCompat
*   **UI**: XML, View Binding, Material Components, CardView
*   **Architecture**: MVVM (Model-View-ViewModel) approach per activity
*   **Firebase Suite**:
    *   [Firebase Authentication](https://firebase.google.com/docs/auth) - For user login and registration.
    *   [Firebase Realtime Database](https://firebase.google.com/docs/database) - For storing movie data, tickets, and saved movies.
*   **Asynchronous Programming**: Coroutines for background tasks.
*   **Third-Party Libraries**:
    *   [Glide](https://github.com/bumptech/glide) - For efficient image loading and caching.
    *   [Chip Navigation Bar](https://github.com/ismaeldivita/chip-navigation-bar) - For the animated bottom navigation.
    *   [BlurView](https://github.com/Dimezis/BlurView) - For real-time background blur effects.
    *   [ZXing (Zebra Crossing)](https://github.com/zxing/zxing) - For generating QR codes on tickets.

---

## Setup and Installation

To get a local copy up and running, follow these simple steps.

### Prerequisites

*   Android Studio (latest version recommended)
*   A Google account for Firebase setup

### Installation

1.  **Clone the repository**:
    ```sh
    git clone https://github.com/mearslanahmed/reeltime-android.git
    ```
2.  **Open in Android Studio**:
    *   Open Android Studio and select `Open an existing Android Studio project`.
    *   Navigate to the cloned repository and open it.

3.  **Set up Firebase**:
    *   Go to the [Firebase Console](https://console.firebase.google.com/).
    *   Create a new project.
    *   Add an Android app to your Firebase project with the package name `com.arslan.reeltime`.
    *   Follow the setup instructions to download the `google-services.json` file.
    *   Place the downloaded `google-services.json` file in the **`app`** directory of your project (`ReelTime/app/`).
    *   Enable **Firebase Authentication** (Email/Password method) and the **Realtime Database** in the Firebase console.

4.  **Build and Run**:
    *   Let Android Studio sync the Gradle files.
    *   Build and run the app on an emulator or a physical device.

---

## Future Improvements

*   **Search Functionality**: Implement a real-time search feature for movies.
*   **Payment Gateway**: Integrate a payment system for actual ticket purchases.
*   **Movie Trailers**: Add the ability to watch movie trailers within the app.

---

## Contributing

Contributions are welcome! If you have any ideas, suggestions, or find a bug, please open an issue or submit a pull request.

---

## License

Distributed under the MIT License. See `LICENSE` file for more information.

---

## Developed by

Arslan Ahmed - [arslanahmednaseem@gmail.com](mailto:arslanahmednaseem@gmail.com) 


---

## Acknowledgments

*   [Firebase](https://firebase.google.com/)
*   [Glide](https://github.com/bumptech/glide)
*   [Chip Navigation Bar](https://github.com/ismaeldivita/chip-navigation-bar)
*   [BlurView](https://github.com/Dimezis/BlurView)
*   [ZXing](https://github.com/zxing/zxing)

