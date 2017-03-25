package com.orogersilva.desafioinfoglobo.data.local.contract

import android.provider.BaseColumns

/**
 * Created by orogersilva on 3/24/2017.
 */
abstract class AuthorPersistenceContract {

    // region TABLE_INFO

    companion object AuthorEntry : BaseColumns {

        @JvmField val TABLE_NAME = "author"

        @JvmField val COLUMN_NAME_NAME = TABLE_NAME + "_name"
        @JvmField val COLUMN_NAME_NEWS_ID = TABLE_NAME + "_news_id"
    }

    // endregion
}