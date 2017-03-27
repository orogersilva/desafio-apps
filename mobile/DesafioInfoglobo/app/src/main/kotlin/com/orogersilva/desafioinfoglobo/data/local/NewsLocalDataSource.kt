package com.orogersilva.desafioinfoglobo.data.local

import com.orogersilva.desafioinfoglobo.data.NewsDataSource
import com.orogersilva.desafioinfoglobo.data.local.contract.AuthorPersistenceContract
import com.orogersilva.desafioinfoglobo.data.local.contract.ImagePersistenceContract
import com.orogersilva.desafioinfoglobo.data.local.contract.NewsPersistenceContract
import com.orogersilva.desafioinfoglobo.data.local.contract.VideoPersistenceContract
import com.orogersilva.desafioinfoglobo.model.Image
import com.orogersilva.desafioinfoglobo.model.News
import com.orogersilva.desafioinfoglobo.model.Section

/**
 * Created by orogersilva on 3/24/2017.
 */
object NewsLocalDataSource : NewsDataSource {

    // region PROPERTIES

    private var dbHelper: DbHelper? = null

    // endregion

    // region INITIALIZER / DESTRUCTOR

    fun getInstance(dbHelper: DbHelper) : NewsDataSource {

        if (this.dbHelper == null) {
            this.dbHelper = dbHelper
        }

        return this
    }

    fun destroyInstance() {

        dbHelper?.close()
        dbHelper = null
    }

    // endregion

    // region OVERRIDED METHODS

    override fun getAllNews(callback: NewsDataSource.LoadNewsCallback) {

        val db = dbHelper?.readableDatabase

        var projection = arrayOf(
                NewsPersistenceContract.COLUMN_NAME_ID,
                NewsPersistenceContract.COLUMN_NAME_ADVERTISING_REPORT,
                NewsPersistenceContract.COLUMN_NAME_SUBTITLE,
                NewsPersistenceContract.COLUMN_NAME_TEXT,
                NewsPersistenceContract.COLUMN_NAME_UPDATED_IN,
                NewsPersistenceContract.COLUMN_NAME_PUBLISHED_IN,
                NewsPersistenceContract.COLUMN_NAME_SECTION_NAME,
                NewsPersistenceContract.COLUMN_NAME_SECTION_URL,
                NewsPersistenceContract.COLUMN_NAME_TYPE,
                NewsPersistenceContract.COLUMN_NAME_TITLE,
                NewsPersistenceContract.COLUMN_NAME_URL,
                NewsPersistenceContract.COLUMN_NAME_ORIGINAL_URL
        )

        var c = db?.query(NewsPersistenceContract.TABLE_NAME, projection, null, null, null, null, null)

        val news = mutableListOf<News>()

        if (c != null && c.count > 0) {

            while (c.moveToNext()) {

                val id = c.getLong(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_ID))
                val advertisingReport = c.getInt(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_ADVERTISING_REPORT)) == 1
                val subtitle = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_SUBTITLE))
                val text = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_TEXT))
                val updatedIn = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_UPDATED_IN))
                val publishedIn = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_PUBLISHED_IN))
                val sectionName = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_SECTION_NAME))
                val sectionUrl = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_SECTION_URL))
                val type = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_TYPE))
                val title = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_TITLE))
                val url = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_URL))
                val originalUrl = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_ORIGINAL_URL))

                news.add(
                        News(
                                advertisingReport = advertisingReport,
                                subtitle = subtitle,
                                text = text,
                                updatedIn = updatedIn,
                                id = id,
                                publishedIn = publishedIn,
                                section = Section(sectionName, sectionUrl),
                                type = type,
                                title = title,
                                url = url,
                                originalUrl = originalUrl
                        )
                )
            }
        }

        c?.close()

        news.forEach {

            // 1. Querying author's names.

            c = db?.rawQuery(
                    "SELECT a.author_name " +
                            "FROM author as a " +
                            "WHERE a.author_news_id = ?",
                    arrayOf<String>(it.id.toString())
            )

            val authors = mutableListOf<String>()

            if (c != null && c!!.count > 0) {

                while (c!!.moveToNext()) {

                    val authorName = c?.getString(c!!.getColumnIndex(AuthorPersistenceContract.COLUMN_NAME_NAME))

                    authors.add(authorName!!)
                }
            }

            c?.close()

            // 2. Querying videos' URL's.

            c = db?.rawQuery(
                    "SELECT v.video_url " +
                            "FROM video as v " +
                            "WHERE v.video_news_id = ?",
                    arrayOf<String>(it.id.toString()
                    )
            )

            val videos = mutableListOf<String>()

            if (c != null && c!!.count > 0) {

                while (c!!.moveToNext()) {

                    val video = c?.getString(c!!.getColumnIndex(VideoPersistenceContract.COLUMN_NAME_URL))

                    videos.add(video!!)
                }
            }

            c?.close()

            // 3. Querying images.

            c = db?.rawQuery(
                    "SELECT i.image_author, i.image_source, i.image_caption, i.image_url " +
                            "FROM image as i " +
                            "WHERE i.image_news_id = ?",
                    arrayOf<String>(it.id.toString())
            )

            val images = mutableListOf<Image>()

            if (c != null && c!!.count > 0) {

                while (c!!.moveToNext()) {

                    val author = c?.getString(c!!.getColumnIndex(ImagePersistenceContract.COLUMN_NAME_AUTHOR))
                    val source = c?.getString(c!!.getColumnIndex(ImagePersistenceContract.COLUMN_NAME_SOURCE))
                    val caption = c?.getString(c!!.getColumnIndex(ImagePersistenceContract.COLUMN_NAME_CAPTION))
                    val url = c?.getString(c!!.getColumnIndex(ImagePersistenceContract.COLUMN_NAME_URL))

                    images.add(Image(author!!, source!!, caption!!, url!!))
                }
            }

            c?.close()

            it.authors = if (authors.isEmpty()) null else authors
            it.videos = if (videos.isEmpty()) null else videos
            it.images = if (images.isEmpty()) null else images
        }

        db?.close()

        if (news.isEmpty()) {
            callback.onDataNotAvailable()
        } else {
            callback.onNewsLoaded(news)
        }
    }

    override fun saveNews(news: List<News>?) {

        if (news == null) throw NullPointerException()

        val db = dbHelper?.writableDatabase

        val insertOnNewsTableSql = "INSERT INTO " + NewsPersistenceContract.TABLE_NAME + " (" +
                NewsPersistenceContract.COLUMN_NAME_ID + ", " +
                NewsPersistenceContract.COLUMN_NAME_ADVERTISING_REPORT + ", " +
                NewsPersistenceContract.COLUMN_NAME_SUBTITLE + ", " +
                NewsPersistenceContract.COLUMN_NAME_TEXT + ", " +
                NewsPersistenceContract.COLUMN_NAME_UPDATED_IN + ", " +
                NewsPersistenceContract.COLUMN_NAME_PUBLISHED_IN + ", " +
                NewsPersistenceContract.COLUMN_NAME_SECTION_NAME + ", " +
                NewsPersistenceContract.COLUMN_NAME_SECTION_URL + ", " +
                NewsPersistenceContract.COLUMN_NAME_TYPE + ", " +
                NewsPersistenceContract.COLUMN_NAME_TITLE + ", " +
                NewsPersistenceContract.COLUMN_NAME_URL + ", " +
                NewsPersistenceContract.COLUMN_NAME_ORIGINAL_URL +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"

        val insertOnAuthorTableSql = "INSERT INTO " + AuthorPersistenceContract.TABLE_NAME + " (" +
                AuthorPersistenceContract.COLUMN_NAME_NAME + ", " +
                AuthorPersistenceContract.COLUMN_NAME_NEWS_ID +
                ") VALUES (?, ?);"

        val insertOnVideoTableSql = "INSERT INTO " + VideoPersistenceContract.TABLE_NAME + " (" +
                VideoPersistenceContract.COLUMN_NAME_URL + ", " +
                VideoPersistenceContract.COLUMN_NAME_NEWS_ID +
                ") VALUES (?, ?);"

        val insertOnImageTableSQl = "INSERT INTO " + ImagePersistenceContract.TABLE_NAME + " (" +
                ImagePersistenceContract.COLUMN_NAME_AUTHOR + ", " +
                ImagePersistenceContract.COLUMN_NAME_SOURCE + ", " +
                ImagePersistenceContract.COLUMN_NAME_CAPTION + ", " +
                ImagePersistenceContract.COLUMN_NAME_URL + ", " +
                ImagePersistenceContract.COLUMN_NAME_NEWS_ID +
                ") VALUES (?, ?, ?, ?, ?);"

        try {

            val newsStatement = db?.compileStatement(insertOnNewsTableSql)
            val authorStatement = db?.compileStatement(insertOnAuthorTableSql)
            val videoStatement = db?.compileStatement(insertOnVideoTableSql)
            val imageStatement = db?.compileStatement(insertOnImageTableSQl)

            db?.beginTransaction()

            news.forEach {

                // 1. Insert on "News" table.

                newsStatement?.bindLong(1, it.id)
                newsStatement?.bindLong(2, if (it.advertisingReport) 1 else 0)

                if (it.subtitle == null) {
                    newsStatement?.bindNull(3)
                } else {
                    newsStatement?.bindString(3, it.subtitle)
                }

                if (it.text == null) {
                    newsStatement?.bindNull(4)
                } else {
                    newsStatement?.bindString(4, it.text)
                }

                newsStatement?.bindString(5, it.updatedIn)
                newsStatement?.bindString(6, it.publishedIn)
                newsStatement?.bindString(7, it.section.name)
                newsStatement?.bindString(8, it.section.url)
                newsStatement?.bindString(9, it.type)
                newsStatement?.bindString(10, it.title)
                newsStatement?.bindString(11, it.url)
                newsStatement?.bindString(12, it.originalUrl)

                newsStatement?.execute()

                // 2. Insert on "Author" table.

                if (it.authors != null) {

                    for (authorName in it.authors!!) {

                        authorStatement?.bindString(1, authorName)
                        authorStatement?.bindLong(2, it.id)

                        authorStatement?.execute()
                    }
                }

                // 3. Insert on "Video" table.

                if (it.videos != null) {

                    for (videoUrl in it.videos!!) {

                        videoStatement?.bindString(1, videoUrl)
                        videoStatement?.bindLong(2, it.id)

                        videoStatement?.execute()
                    }
                }

                // 4. Insert on "Image" table.

                if (it.images != null) {

                    for (image in it.images!!) {

                        imageStatement?.bindString(1, image.author)
                        imageStatement?.bindString(2, image.source)
                        imageStatement?.bindString(3, image.caption)
                        imageStatement?.bindString(4, image.url)
                        imageStatement?.bindLong(5, it.id)

                        imageStatement?.execute()
                    }
                }
            }

            newsStatement?.close()
            authorStatement?.close()
            videoStatement?.close()
            imageStatement?.close()

            db?.setTransactionSuccessful()

        } catch (e: Exception) {

            e.printStackTrace()

        } finally {

            db?.endTransaction()
        }

        db?.close()
    }

    override fun deleteAllNews(): Int {

        val db = dbHelper?.writableDatabase

        db?.delete(AuthorPersistenceContract.TABLE_NAME, null, null)
        db?.delete(ImagePersistenceContract.TABLE_NAME, null, null)
        db?.delete(VideoPersistenceContract.TABLE_NAME, null, null)

        val deletedRows = db?.delete(NewsPersistenceContract.NewsEntry.TABLE_NAME, null, null)

        db?.close()

        return deletedRows!!
    }

    // endregion
}