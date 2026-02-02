package com.revplay.main;

import com.revplay.controller.ArtistController;
import com.revplay.controller.UserController;
import com.revplay.model.ArtistAccount;
import com.revplay.model.UserAccount;
import com.revplay.service.ArtistServiceImpl;
import com.revplay.service.IArtistService;
import com.revplay.service.IUserAccountService;
import com.revplay.service.UserAccountServiceImpl;
import com.revplay.util.JDBCUtil;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    private static IUserAccountService userService = new UserAccountServiceImpl();
    private static IArtistService artistService = new ArtistServiceImpl();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("=========================================");
            System.out.println("      Welcome to RevPlay Music App       ");
            System.out.println("=========================================");

            System.out.println("1. Listener Login");
            System.out.println("2. Artist Login");
            System.out.println("3. Register as Listener");
            System.out.println("4. Register as Artist");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        loginUser();
                        break;
                    case 2:
                        loginArtist();
                        break;
                    case 3:
                        registerUser();
                        break;
                    case 4:
                        registerArtist();
                        break;
                    case 5:
                        System.out.println("Exiting... Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                com.revplay.util.LoggerUtil.logWarning("Invalid numeric input in main menu");
            } catch (com.revplay.exception.RevPlayException e) {
                System.out.println("⚠️ An error occurred: " + e.getMessage());
                com.revplay.util.LoggerUtil.logWarning("Handled UI exception: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ An unexpected error occurred.");
                com.revplay.util.LoggerUtil.logError("Unexpected/Unhandled exception in Main", e);
            }
        }
    }

    private static void loginUser() {
        while (true) {
            System.out.println("\n--- Listener Login ---");
            System.out.println("1. Enter credentials");
            System.out.println("2. Forgot Password");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            String opt = scanner.nextLine().trim();
            if (opt.equals("0")) {
                return;
            } else if (opt.equals("2")) {
                forgotPasswordUser();
                continue;
            } else if (!opt.equals("1")) {
                System.out.println("Invalid option.");
                continue;
            }

            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            UserAccount user = userService.getUserByEmail(email);
            if (user != null && user.getPasswordHash().equals(password)) {
                System.out.println("✅ Login Successful! Welcome " + user.getFullName());
                UserController.userDashboard(user);
                return;
            } else {
                System.out.println("❌ Invalid credentials.");
            }
        }
    }

    private static void forgotPasswordUser() {
        System.out.println("\n--- Forgot Password ---");
        System.out.print("Enter your registered Email: ");
        String email = scanner.nextLine();

        UserAccount user = userService.getUserByEmail(email);
        if (user == null) {
            System.out.println("❌ No account found with this email.");
            return;
        }

        // Show hint if available
        if (user.getPasswordHint() != null && !user.getPasswordHint().isEmpty()) {
            System.out.println("💡 Password Hint: " + user.getPasswordHint());
        }

        // Show security question
        if (user.getSecurityQuestion() == null || user.getSecurityQuestion().isEmpty()) {
            System.out.println("❌ No security question set for this account.");
            return;
        }

        System.out.println("❓ Security Question: " + user.getSecurityQuestion());
        System.out.print("Your Answer: ");
        String answer = scanner.nextLine();

        if (answer.equalsIgnoreCase(user.getSecurityAnswerHash())) {
            System.out.println("✅ Answer verified!");
            System.out.print("Enter New Password: ");
            String newPass = scanner.nextLine();
            System.out.print("Confirm New Password: ");
            String confirmPass = scanner.nextLine();

            if (newPass.equals(confirmPass)) {
                user.setPasswordHash(newPass);
                if (userService.updateUserAccount(user)) {
                    System.out.println("✅ Password updated successfully! Please login with new password.");
                } else {
                    System.out.println("❌ Failed to update password.");
                }
            } else {
                System.out.println("❌ Passwords do not match.");
            }
        } else {
            System.out.println("❌ Incorrect answer.");
        }
    }

    private static void loginArtist() {
        System.out.println("\n--- Artist Login ---");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        ArtistAccount artist = artistService.getArtistByEmail(email);
        if (artist != null && artist.getPasswordHash().equals(password)) {
            System.out.println("✅ Login Successful! Welcome " + artist.getStageName());
            ArtistController.artistDashboard(artist);
        } else {
            System.out.println("❌ Invalid credentials. Please register first.");
        }
    }

    private static void registerUser() {
        System.out.println("\n--- Listener Registration ---");
        System.out.print("Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Security Question: ");
        String secQ = scanner.nextLine();
        System.out.print("Security Answer: ");
        String secA = scanner.nextLine();
        System.out.print("Password Hint: ");
        String hint = scanner.nextLine();

        UserAccount user = new UserAccount();
        user.setFullName(name);
        user.setEmail(email);
        user.setPasswordHash(pass);
        user.setPhone(phone);
        user.setStatus("ACTIVE");
        user.setSecurityQuestion(secQ);
        user.setSecurityAnswerHash(secA);
        user.setPasswordHint(hint);
        user.setCreatedAt(LocalDateTime.now());

        if (userService.addUserAccount(user)) {
            System.out.println("✅ Registration successful!");
        } else {
            System.out.println("❌ Registration failed. Email might already exist.");
        }
    }

    private static void registerArtist() {
        System.out.println("\n--- Artist Registration ---");
        System.out.print("Stage Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();
        System.out.print("Bio: ");
        String bio = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Instagram: ");
        String ig = scanner.nextLine();
        System.out.print("YouTube: ");
        String yt = scanner.nextLine();
        System.out.print("Spotify: ");
        String sp = scanner.nextLine();

        ArtistAccount artist = new ArtistAccount();
        artist.setStageName(name);
        artist.setEmail(email);
        artist.setPasswordHash(pass);
        artist.setBio(bio);
        artist.setGenre(genre);
        artist.setInstagramLink(ig);
        artist.setYoutubeLink(yt);
        artist.setSpotifyLink(sp);
        artist.setStatus("ACTIVE");
        artist.setCreatedAt(LocalDateTime.now());

        if (artistService.registerArtist(artist)) {
            System.out.println("✅ Registration successful!");
        } else {
            System.out.println("❌ Registration failed.");
        }
    }
}