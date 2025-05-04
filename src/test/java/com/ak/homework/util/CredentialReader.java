package com.ak.homework.util;

// A Jackson könyvtárból származó osztály, amely a JSON ↔ Java átalakításért felelős
import com.fasterxml.jackson.databind.ObjectMapper;

// Az InputStream lehetővé teszi fájl beolvasását byte-alapon (olyan, mint egy adatcsap)
import java.io.InputStream;

public class CredentialReader {

    // Ez egy statikus metódus, amit bármikor hívhatsz anélkül, hogy példányosítanál
    public static Credential readCredential() {
        try {
            // 1. Létrehozunk egy JSON olvasót
            // Ez az eszköz fogja átalakítani a JSON-t Java objektummá
            ObjectMapper mapper = new ObjectMapper();

            // 2. Megkeressük és megnyitjuk a credential.json fájlt a resources mappából
            // A getClassLoader() a Maven classpath-ján keresi meg a fájlt
            InputStream is = CredentialReader.class
                    .getClassLoader()
                    .getResourceAsStream("credential.json");

            // 3. A mapper beolvassa az InputStream-et, és létrehoz egy Credential objektumot
            return mapper.readValue(is, Credential.class);

        } catch (Exception e) {
            // 4. Ha bármi hiba történik (pl. fájl hiányzik, formátum rossz), itt jelezzük
            throw new RuntimeException("Nem sikerült beolvasni a credential.json fájlt", e);
        }
    }
}
