package com.example.user.touristhelper;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WebPageParser {
    private String url;
    private String webPageTextContent;
    private Document webPage;
    private Elements pageText;

     /*
     Метод для парсинга веб-страницы и записи текстового содержимого страницы
     */
     public void parseUrl(String entryURL) {
         url = entryURL;

         try {
             webPage = Jsoup.connect(url).get();         //получение HTML кода веб-страницы
         } catch (IOException e) {
             e.printStackTrace();
         }

         if (webPage != null) {
             pageText = webPage.select("p");

             if (pageText.isEmpty()) {
                 webPageTextContent = webPageTextContent + "\n" + webPage.text();
             } else {
                 webPageTextContent = webPage.title();              //запись заголовка веб-страницы

                 for (int i = 0; i < pageText.size(); i++) {
                     webPageTextContent = webPageTextContent + "\n" + pageText.get(i).text() + "\n";   //получение содержимого тегов параграфов
                 }
             }

         } else {
             webPageTextContent = "Код страницы не найден.";
         }
     }

    public String getTextContent (){
       return webPageTextContent;
    }
}