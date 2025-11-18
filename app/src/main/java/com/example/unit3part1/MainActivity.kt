package com.example.unit3part1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {

    private lateinit var rvMovies: RecyclerView
    private val movies = mutableListOf<Movie>()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMovies = findViewById(R.id.rvMovies)

        movieAdapter = MovieAdapter(movies)
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager(this)

        fetchMovies()
    }

    private fun fetchMovies() {
        val client = AsyncHttpClient()
        val url =
            "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

        client.get(url, object : JsonHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JSON
            ) {
                val results = json.jsonObject.getJSONArray("results")
                movies.clear()

                for (i in 0 until results.length()) {
                    val movieJson = results.getJSONObject(i)
                    val title = movieJson.getString("title")
                    val overview = movieJson.getString("overview")
                    val posterPath = movieJson.getString("poster_path")

                    val movie = Movie(title, overview, posterPath)
                    movies.add(movie)
                }

                movieAdapter.notifyDataSetChanged()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("MainActivity", "Failed to fetch movies: $response", throwable)
            }
        })
    }
}
