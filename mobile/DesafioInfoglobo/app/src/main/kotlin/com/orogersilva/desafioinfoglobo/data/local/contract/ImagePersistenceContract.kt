package com.orogersilva.desafioinfoglobo.data.local.contract

import android.provider.BaseColumns

/**
 * Created by orogersilva on 3/24/2017.
 */
abstract class ImagePersistenceContract {

    // region TABLE INFO

    companion object ImageEntry : BaseColumns {

        @JvmField val TABLE_NAME = "image"

        @JvmField val COLUMN_NAME_AUTHOR = TABLE_NAME + "_author"
        @JvmField val COLUMN_NAME_SOURCE = TABLE_NAME + "_source"
        @JvmField val COLUMN_NAME_CAPTION = TABLE_NAME + "_caption"
        @JvmField val COLUMN_NAME_URL = TABLE_NAME + "_url"
        @JvmField val COLUMN_NAME_NEWS_ID = TABLE_NAME + "_news_id"
    }

    // endregion
}