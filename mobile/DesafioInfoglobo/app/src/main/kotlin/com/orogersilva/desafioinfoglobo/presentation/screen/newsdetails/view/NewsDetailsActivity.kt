package com.orogersilva.desafioinfoglobo.presentation.screen.newsdetails.view

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.orogersilva.desafioinfoglobo.R
import com.orogersilva.desafioinfoglobo.model.News
import kotlinx.android.synthetic.main.activity_news_details.*
import kotlinx.android.synthetic.main.toolbar_news_details.*
import java.text.SimpleDateFormat

/**
 * Created by orogersilva on 3/26/2017.
 */
class NewsDetailsActivity : AppCompatActivity() {

    // region PROPERTIES

    private val newsDetailsImageLayoutTarget = object : SimpleTarget<Bitmap>(220, 220) {

        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {

            newsDetailsImageLayout.background = BitmapDrawable(resource)
        }
    }

    // endregion

    // region ACTIVITY LIFECYCLE METHODS

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        newsDetailsToolbar.title = ""

        setSupportActionBar(newsDetailsToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val newsDetails = intent.getParcelableExtra<News>("news_extra")

        processNewsDetails(newsDetails)
    }

    // endregion

    // region OVERRIDED METHODS

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()

        return true
    }

    // endregion

    // region UTILITY METHODS

    private fun processNewsDetails(newsDetails: News) {

        if (newsDetails.text != null) {

            newsDetailsSectionTypeTextView.text = newsDetails.section.name
            newsDetailsTitleTextView.text = newsDetails.title
            newsDetailsSubtitleTextView.text = newsDetails.subtitle
            newsDetailsAuthorsTextView.text = TextUtils.join(", ", newsDetails.authors)

            var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            val publishedInDate = dateFormat.parse(newsDetails.publishedIn)

            dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

            newsDetailsPublishedInTextView.text = dateFormat.format(publishedInDate)

            if (newsDetails.images != null && newsDetails.images!!.size > 0) {

                Glide.with(this)
                        .load(newsDetails.images!![0].url)
                        .asBitmap()
                        .into(newsDetailsImageLayoutTarget)

                newsDetailsImageCaptionTextView.text = newsDetails.images!![0].caption + " " +
                        getString(R.string.photo) + ": "  + newsDetails.images!![0].source

            } else {

                Glide.clear(newsDetailsImageLayoutTarget)

                newsDetailsImageLayout.visibility = View.GONE
            }

            newsDetailsTextView.text = newsDetails.text
        }
    }

    // endregion
}