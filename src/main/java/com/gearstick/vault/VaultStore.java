package com.gearstick.vault;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * VaultStore
 * 
 * @implNote every vault is saved as a file as .vault extension
 * @implNote new vaults are saved as [vault.name].vault
 * @implNote on app start, vaults are loaded from the vaults folder
 * 
 * @implSpec vaults can be exported or imported // TODO: zip format?
 * 
 * @implNote vault login strategies:
 *           1. password (masterpass)
 *           2. TODO: biometrics
 *           3. TODO: 2FA
 * 
 * @implNote vault registration strategies:
 *           - password (masterpass) and vault name
 */
public class VaultStore {
    public static final String FOLDER = "vaults/";
    public static final String EXTENSION = ".vault";

    public static HashMap<String, Vault> vaults = new HashMap<>();

    public static void saveVaultToFolder(Vault vault) {
        new File(FOLDER).mkdirs();
        try {
            FileOutputStream fileOut = new FileOutputStream(FOLDER + vault.name + EXTENSION);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(vault);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadVaults() {
        File dir = new File(FOLDER);
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(EXTENSION)) {
                loadVault(file.getName());
            }
        }

    }

    public static void loadVault(String nameWithExtension) {
        try {
            FileInputStream fileIn = new FileInputStream(FOLDER + nameWithExtension);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            Vault vault = (Vault) in.readObject();
            in.close();
            fileIn.close();

            vaults.put(vault.name, vault);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // old version of vault
            // delete vault
            deleteVault(nameWithExtension);
            e.printStackTrace();
        }
    }

    public static void deleteVault(String nameWithExtension) {
        File file = new File(FOLDER + nameWithExtension);
        file.delete();
    }

    public static Vault createVault(Vault vault) {
        saveVaultToFolder(vault);
        vaults.put(vault.name, vault);
        return vault;
    }
}
