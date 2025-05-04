package com.ak.homework.util;

/**
 * Ez az osztály a credential.json fájl adatainak Java megfelelője.
 * Két változót tartalmaz: username és password.
 * Ezekhez tartoznak getterek és setterek, amiken keresztül más kód elérheti őket.
 */
public class Credential {

    // Ez a két változó tárolja a JSON-ból beolvasott adatokat
    private String username;
    private String password;

    // Getter: visszaadja a username változó értékét
    public String getUsername() {
        return username;
    }

    // Setter: beállítja a username változó értékét
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter: visszaadja a password változó értékét
    public String getPassword() {
        return password;
    }

    // Setter: beállítja a password változó értékét
    public void setPassword(String password) {
        this.password = password;
    }
}
