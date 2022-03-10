package com.example.top10app

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.math.log



class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""

    override fun toString(): String {
        return """
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            summary = $summary
            imageURL = $imageURL
        """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate")

        val downloadData = DownloadData()
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate Done")

    }

    companion object{
        private class DownloadData : AsyncTask<String, Void, String>() {
            private val TAG = "Downlowad"

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground")
                val rssFeed = downloadXML(url[0])
                if(rssFeed.isEmpty()){
                    Log.e(TAG, "doInBackground: failed")
                }
                Log.d(TAG, rssFeed)
                return rssFeed
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                Log.d(TAG, "onPostExecute")
                val parsedApplication = ParseApplication()
                parsedApplication.parse(result)
            }

            private fun downloadXML(urlPath: String?): String {
               try{
                   return  URL(urlPath).readText()
               } catch (e: Exception){
                   val errorMessage: String = when (e){
                       is MalformedURLException -> "downloadXML: Invalid URL ${e.message}"
                       is IOException -> "downloadXML: IOException reading dT ${e.message}"
                       else -> "downloadXML: Unknown Error ${e.message}"
                   }
                   Log.e(TAG, errorMessage)

               }
                return ""
            }
        }
    }
}