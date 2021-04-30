package org.manuelelucchi.data;

public class StudentManager {
    private static StudentManager _instance;

    private StudentManager() {
    }

    public static StudentManager getInstance() {
        if (_instance == null) {
            _instance = new StudentManager();
        }
        return _instance;
    }

    public boolean isStudent(int id, String email) {
        return true; // Simulated
    }
}
