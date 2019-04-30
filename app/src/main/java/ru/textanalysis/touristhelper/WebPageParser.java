package ru.textanalysis.touristhelper;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class WebPageParser extends AsyncTask<String, Void, ArrayList<String>> {

    private GetTextListener getTextListener;

    public WebPageParser(GetTextListener listener){
        this.getTextListener = listener;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        return parseUrl(strings[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        getTextListener.onTextProcessStarted();
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {
        super.onPostExecute(s);

        getTextListener.onTextComplete(s);
    }

     /*
     Метод для парсинга веб-страницы и записи текстового содержимого страницы
     */
     public ArrayList<String> parseUrl(String entryURL) {
         Document webPage = null;
         String webPageTextContent = "";

         try {
             webPage = Jsoup.connect(entryURL).execute().parse();
         } catch (IOException e) {
             e.printStackTrace();
         }

         String lang = webPage.select("html").attr("lang");
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

         ArrayList<String> result = new ArrayList<String>();
         result.add(webPageTextContent);
         result.add(lang);

         return result;
     }
}