package com.orogersilva.desafioinfoglobo.data.local.contract

import android.provider.BaseColumns

/**
 * Created by orogersilva on 3/24/2017.
 */
abstract class NewsPersistenceContract {

    // region TABLE INFO

    companion object NewsEntry : BaseColumns {

        @JvmField val TABLE_NAME = "news"

        @JvmField val COLUMN_NAME_ID = TABLE_NAME + "_id"
        @JvmField val COLUMN_NAME_ADVERTISING_REPORT = TABLE_NAME + "_advertising_report"
        @JvmField val COLUMN_NAME_SUBTITLE = TABLE_NAME + "_subtitle"
        @JvmField val COLUMN_NAME_TEXT = TABLE_NAME + "_text"
        @JvmField val COLUMN_NAME_UPDATED_IN = TABLE_NAME + "_updated_in"
        @JvmField val COLUMN_NAME_PUBLISHED_IN = TABLE_NAME  + "_published_in"
        @JvmField val COLUMN_NAME_SECTION_NAME = TABLE_NAME + "_section_name"
        @JvmField val COLUMN_NAME_SECTION_URL = TABLE_NAME + "_section_url"
        @JvmField val COLUMN_NAME_TYPE = TABLE_NAME + "_type"
        @JvmField val COLUMN_NAME_TITLE = TABLE_NAME + "_title"
        @JvmField val COLUMN_NAME_URL = TABLE_NAME + "_url"
        @JvmField val COLUMN_NAME_ORIGINAL_URL = TABLE_NAME + "_original_url"
    }

    // endregion
}