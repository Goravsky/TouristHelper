package com.example.user.touristhelper;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WebPageParser {

    private String webPageTextContent;

     /*
     Метод для парсинга веб-страницы и записи текстового содержимого страницы
     */
     public void parseUrl(String entryURL) {
         Document webPage = null;

         try {
             webPage = Jsoup.connect(entryURL).execute().parse();
         } catch (IOException e) {
             e.printStackTrace();
         }

         if (webPage != null) {
             Elements pageText = webPage.select("p");

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