package com.pixelbuilds.lol_itsjustameme

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var memeURL: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Content Warning")
            .setMessage("Meme(s) Which Will Be Displayed In This App Are Collected From An API, Developer Is Not Responsible If Any InAppropriate Meme Appears")
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which -> })
            .setCancelable(false)
        dialog.show()
        loadMeme()
    }

    private fun loadMeme() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val memeView = findViewById<ImageView>(R.id.memeView)
        val url = "https://meme-api.herokuapp.com/gimme"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null, { response ->
                memeURL = response.getString("url")
                Glide.with(this).load(memeURL).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(memeView)
            }, {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey Buddy,Check This Meme Which I Have Got From Lol-It's Just A Meme App Via Reddit :- $memeURL"
        )
        val intentChooser = Intent.createChooser(intent, "Share This Meme Via:")
        startActivity(intentChooser)
    }
}