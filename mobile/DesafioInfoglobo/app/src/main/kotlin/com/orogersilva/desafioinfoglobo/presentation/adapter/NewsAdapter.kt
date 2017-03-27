package com.orogersilva.desafioinfoglobo.presentation.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.orogersilva.desafioinfoglobo.R
import com.orogersilva.desafioinfoglobo.model.News
import kotlinx.android.synthetic.main.itemview_headline.view.*
import kotlinx.android.synthetic.main.itemview_news.view.*
import kotlinx.android.synthetic.main.toolbar_news_details.view.*

/**
 * Created by orogersilva on 3/26/2017.
 */
class NewsAdapter(private val news: MutableList<News>,
                  private val newsItemListener: NewsItemListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // region PROPERTIES

    private val ITEM_TYPE_HEADLINE_CODE = 0
    private val ITEM_TYPE_NEWS_CODE = 1

    // endregion

    // region PUBLIC METHODS

    fun clearData() {

        news.clear()

        notifyDataSetChanged()
    }

    fun replaceData(news: List<News>) {

        this.news.clear()
        this.news.addAll(news)

        notifyDataSetChanged()
    }

    // endregion

    // region OVERRIDED METHODS

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        var itemViewHolder: RecyclerView.ViewHolder

        if (viewType == ITEM_TYPE_HEADLINE_CODE) {

            val view = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.itemview_headline, parent, false)

            view.tag = HeadlineItemPresenter()

            itemViewHolder = HeadLineItemViewHolder(view)

        } else {

            val view = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.itemview_news, parent, false)

            view.tag = NewsItemPresenter()

            itemViewHolder = NewsItemViewHolder(view)
        }

        return itemViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        val itemTypeCode = getItemViewType(position)

        if (itemTypeCode == ITEM_TYPE_HEADLINE_CODE) {
            (holder?.itemView?.tag as HeadlineItemPresenter).presentListItem(holder as HeadLineItemViewHolder, news[position])
        } else {
            (holder?.itemView?.tag as NewsItemPresenter).presentListItem(holder as NewsItemViewHolder, news[position])
        }
    }

    override fun getItemCount() = news.size

    override fun getItemViewType(position: Int): Int {

        if (position == 0) {
            return ITEM_TYPE_HEADLINE_CODE
        } else {
            return ITEM_TYPE_NEWS_CODE
        }
    }

    // endregion

    // region INTERFACES

    interface NewsItemListener {

        // region METHODS

        fun onNewsPressed(news: News)

        // endregion
    }

    // endregion

    // region UTILITY CLASSES

    inner class HeadLineItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemView<News> {

        // region INITIALIZER BLOCK

        init {

            itemView.itemviewHeadlineLayout.setOnClickListener {
                newsItemListener.onNewsPressed(news[adapterPosition])
            }
        }

        // endregion

        // region OVERRIDED METHODS

        override fun setItem(news: News) {

            if (news.images != null && news.images!!.size > 0) {

                Glide.with(itemView.context)
                        .load(news.images!![0].url)
                        .asBitmap()
                        .into(object : SimpleTarget<Bitmap>(250, 250) {

                            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {

                                itemView.itemviewHeadlineLayout.background = BitmapDrawable(resource)
                            }
                        })
            }

            itemView.headlineSectionTypeTextView.text = news.section.name
            itemView.headlineTitleTextView.text = news.title
        }

        // endregion
    }

    inner class NewsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemView<News> {

        // region INITIALIZER BLOCK

        init {

            itemView.itemviewNewsLayout.setOnClickListener {
                newsItemListener.onNewsPressed(news[adapterPosition])
            }
        }

        // endregion

        // region OVERRIDED METHODS

        override fun setItem(news: News) {

            if (news.images != null && news.images!!.size > 0) {

                Glide.with(itemView.context)
                        .load(news.images!![0].url)
                        .centerCrop()
                        .into(itemView.newsImageView)
            }

            itemView.newsSectionTypeTextView.text = news.section.name
            itemView.newsTitleTextView.text = news.title
        }

        // endregion
    }

    // endregion
}