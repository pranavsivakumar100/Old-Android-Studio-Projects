package com.example.project2;

import java.io.*;
import java.net.*;
import java.util.*;

public class ParagraphServer {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter port number to listen on: ");
        int portNumber = keyboard.nextInt();

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is listening on port " + portNumber);

            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        Scanner in = new Scanner(clientSocket.getInputStream());
                ) {
                    System.out.println("Client connected from " + clientSocket.getInetAddress());

                    String operation = in.nextLine();
                    System.out.println("Operation requested: " + operation);

                    int numLines = in.nextInt();
                    in.nextLine(); // Clear new line
                    List<String> paragraph = new LinkedList<>();

                    for (int i = 0; i < numLines; i++) {
                        paragraph.add(in.nextLine());
                    }

                    List<String> result = new LinkedList<>();

                    switch (operation.toLowerCase()) {
                        case "alphag":
                            result = alphag(paragraph);
                            break;
                        case "longest":
                            String longest = longest(paragraph);
                            if (longest.equals("")) {
                                result.add("no qualifying word");
                            } else {
                                String[] parts = longest.split("\n");
                                for (String part : parts) {
                                    result.add(part);
                                }
                            }
                            break;
                        default:
                            result.add("unknown operation");
                    }

                    out.println(result.size());
                    for (String line : result) {
                        out.println(line);
                    }

                    System.out.println("Response sent to client, disconnecting");
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }

    /**
     * Converts each word in the paragraph to its alphagram.
     * An alphagram is a string with letters arranged in alphabetical order.
     */
    public static List<String> alphag(List<String> par) {
        List<String> result = new LinkedList<>();

        for (String line : par) {
            StringBuilder newLine = new StringBuilder();
            String[] words = line.split("\\s+");

            for (int i = 0; i < words.length; i++) {
                String word = words[i];

                StringBuilder leadingPunct = new StringBuilder();
                StringBuilder trailingPunct = new StringBuilder();
                int start = 0;
                int end = word.length();

                while (start < end && !Character.isLetterOrDigit(word.charAt(start))) {
                    leadingPunct.append(word.charAt(start));
                    start++;
                }

                while (end > start && !Character.isLetterOrDigit(word.charAt(end - 1))) {
                    trailingPunct.insert(0, word.charAt(end - 1));
                    end--;
                }

                String cleanWord = word.substring(start, end);

                boolean containsDigit = false;
                for (char c : cleanWord.toCharArray()) {
                    if (Character.isDigit(c)) {
                        containsDigit = true;
                        break;
                    }
                }

                if (containsDigit) {
                    newLine.append(leadingPunct).append(cleanWord).append(trailingPunct);
                } else {
                    newLine.append(leadingPunct)
                            .append(alphagram(cleanWord))
                            .append(trailingPunct);
                }

                if (i < words.length - 1) {
                    newLine.append(" ");
                }
            }

            result.add(newLine.toString());
        }

        return result;
    }

    public static String alphagram(String w) {
        if (w.isEmpty()) {
            return w;
        }

        List<Character> lowercase = new ArrayList<>();
        List<Character> uppercase = new ArrayList<>();

        for (char c : w.toCharArray()) {
            if (Character.isLowerCase(c)) {
                lowercase.add(c);
            } else if (Character.isUpperCase(c)) {
                uppercase.add(c);
            }

        }

        Collections.sort(lowercase);
        Collections.sort(uppercase);

        StringBuilder result = new StringBuilder();
        for (char c : lowercase) {
            result.append(c);
        }
        for (char c : uppercase) {
            result.append(c);
        }

        return result.toString();
    }

    public static String longest(List<String> par) {
        String longestWord = "";
        int maxLength = 0;

        for (String line : par) {
            String[] words = line.split("\\s+");

            for (String word : words) {
                String cleanWord = word.replaceAll("[^a-zA-Z0-9]", "");

                if (!cleanWord.toLowerCase().contains("e")) {
                    int length = cleanWord.length();

                    if (length > maxLength) {
                        maxLength = length;
                        longestWord = cleanWord;
                    }
                }
            }
        }

        if (longestWord.isEmpty()) {
            return "";
        } else {
            return longestWord + "\n" + maxLength;
        }
    }
}