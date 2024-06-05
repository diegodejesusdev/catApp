package org.djfactory;

import io.github.cdimascio.dotenv.Dotenv;
import org.djfactory.services.CatService;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static final Dotenv dotenv = Dotenv.configure().directory("src/main/resources/.env").load();
    public static final String API_KEY = dotenv.get("API_KEY");

    public static void main(String[] args) throws IOException {
        int optionMenu = -1;
        String[] buttons = {"1. Show Cats", "2. Show Favorites", "3. Exit"};
        do{
            String option = (String) JOptionPane.showInputDialog(null,"Java Cats", "Menu", JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
            for (int i = 0; i<buttons.length; i++){
                if (option.equals(buttons[i])){
                    optionMenu = i;
                }
            }

            switch(optionMenu){
                case 0: CatService.getCats(); break;
                case 1: CatService.showFavorite(API_KEY); break;
                default: break;

            }
        }while(optionMenu != 1);
    }
}