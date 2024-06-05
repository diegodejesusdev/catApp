package org.djfactory.services;

import com.google.gson.Gson;
import okhttp3.*;
import org.djfactory.models.Cat;
import org.djfactory.models.Favorite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import static org.djfactory.Main.API_KEY;

public class CatService {

    public static void getCats() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").addHeader("x-api-key",API_KEY).get().build();
        Response response = client.newCall(request).execute();

        String json = response.body().string();
        json = json.substring(1, json.length());
        json = json.substring(0, json.length() -1);

        Gson gson = new Gson();
        Cat cat = gson.fromJson(json, Cat.class);

        Image image = null;
        try{
            URL url = new URL(cat.getUrl());
            image = ImageIO.read(url);
            ImageIcon catIcon = new ImageIcon(image);

            if (catIcon.getIconWidth() > 800){
                Image icon = catIcon.getImage();
                Image iconNew = icon.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                catIcon = new ImageIcon(iconNew);
            }

            String menu = "Options: "
                        + "\n1. Show image"
                        + "\n2. Favorite"
                        + "\n3. Exit";

            String[] buttons = {"Show image", "Favorite", "Exit"};
            String id = cat.getId();
            String option = (String) JOptionPane.showInputDialog(null, menu, id, JOptionPane.INFORMATION_MESSAGE, catIcon, buttons, buttons[0]);

            int selection = -1;
            for (int i = 0; i < buttons.length; i++){
                if (option.equals(buttons[i])){
                    selection = i;
                }
            }

            switch (selection){
                case 0: getCats(); break;
                case 1: favoriteCat(cat); break;
                default: break;
            }

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void favoriteCat(Cat cat){
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String json = "{\n\t\"image_id\":\"" + cat.getId() + "\"\n}";
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/favourites").post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println("Response: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showFavorite(String apiKey) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/favourites").get()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apiKey)
                .build();
        Response response = client.newCall(request).execute();

        String json = response.body().string();
        System.out.println("JSON recibido: " + json);
        Gson gson = new Gson();

        Favorite[] favoritesList = gson.fromJson(json, Favorite[].class);

        if (favoritesList.length > 0) {
            int min = 1;
            int max = favoritesList.length;
            int random = (int) (Math.random() * ((max - min) + 1) + min);
            int index = random - 1;

            Favorite favorite = favoritesList[index];

            if (favorite.getImage() != null && favorite.getImage().getUrl() != null) {
                try {
                    URL url = new URL(favorite.getImage().getUrl());
                    Image image = ImageIO.read(url);
                    ImageIcon catIcon = new ImageIcon(image);

                    if (catIcon.getIconWidth() > 800) {
                        Image icon = catIcon.getImage();
                        Image iconNew = icon.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                        catIcon = new ImageIcon(iconNew);
                    }

                    String menu = "Options: "
                            + "\n1. Show image"
                            + "\n2. Delete Favorite"
                            + "\n3. Exit";

                    String[] buttons = {"Show image", "Delete Favorite", "Exit"};
                    String id = favorite.getId();
                    String option = (String) JOptionPane.showInputDialog(null, menu, id, JOptionPane.INFORMATION_MESSAGE, catIcon, buttons, buttons[0]);

                    int selection = -1;
                    for (int i = 0; i < buttons.length; i++) {
                        if (option.equals(buttons[i])) {
                            selection = i;
                        }
                    }

                    switch (selection) {
                        case 0: showFavorite(apiKey); break;
                        case 1: deleteFavorite(favorite); break;
                        default: break;
                    }

                } catch (IOException e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("El campo imageCat o su URL es null");
            }
        }
    }

    public static void deleteFavorite(Favorite favorite){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/"+favorite.getId()+"")
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", favorite.getApiKey())
                    .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
