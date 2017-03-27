package com.orogersilva.desafioinfoglobo.data.local.contract

import android.provider.BaseColumns

/**
 * Created by orogersilva on 3/24/2017.
 */
abstract class VideoPersistenceContract {

    // region TABLE INFO

    companion object VideoEntry : BaseColumns {

        @JvmField val TABLE_NAME = "video"

        @JvmField val COLUMN_NAME_URL = TABLE_NAME + "_url"
        @JvmField val COLUMN_NAME_NEWS_ID = TABLE_NAME + "_news_id"
    }

    // endregion
}