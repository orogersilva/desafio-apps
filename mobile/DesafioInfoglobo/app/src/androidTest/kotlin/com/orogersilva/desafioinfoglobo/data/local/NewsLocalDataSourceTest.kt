package com.orogersilva.desafioinfoglobo.data.local

import android.content.ContentValues
import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.orogersilva.desafioinfoglobo.data.NewsDataSource
import com.orogersilva.desafioinfoglobo.data.local.contract.AuthorPersistenceContract
import com.orogersilva.desafioinfoglobo.data.local.contract.ImagePersistenceContract
import com.orogersilva.desafioinfoglobo.data.local.contract.NewsPersistenceContract
import com.orogersilva.desafioinfoglobo.data.local.contract.VideoPersistenceContract
import com.orogersilva.desafioinfoglobo.model.News
import org.junit.*
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.runner.RunWith
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * Created by orogersilva on 3/24/2017.
 */
@RunWith(AndroidJUnit4::class)
class NewsLocalDataSourceTest {

    // region SETUP / TEARDOWN CLASS METHODS

    companion object {

        private lateinit var context: Context
        private var newsLocalDataSource: NewsDataSource? = null

        @BeforeClass @JvmStatic fun setupClass() {

            context = InstrumentationRegistry.getTargetContext()

            newsLocalDataSource = NewsLocalDataSource.getInstance(DbHelper(context))
        }

        @AfterClass @JvmStatic fun teardownClass() {

            NewsLocalDataSource.destroyInstance()
            newsLocalDataSource = null
        }
    }

    // endregion

    // region TEST METHODS

    @Test fun getAllNews_whenThereAreNotNews_onDataNotAvailableWasSuccessful() {

        // ARRANGE

        val callback = object : NewsDataSource.LoadNewsCallback {

            override fun onNewsLoaded(news: List<News>) {

                // ASSERT

                fail()
            }

            override fun onDataNotAvailable() {

                // ASSERT

                return
            }
        }

        // ACT

        newsLocalDataSource?.getAllNews(callback)
    }

    @Test fun getNews_whenThereAreNews_onNewsLoadedWasSuccessful() {

        // ARRANGE

        val expectedNews = createTestData()

        val dbHelper = DbHelper(context)

        val db = dbHelper.writableDatabase

        expectedNews.forEach {

            val newsContentValues = ContentValues()

            with(newsContentValues) {

                put(NewsPersistenceContract.COLUMN_NAME_ID, it.id)
                put(NewsPersistenceContract.COLUMN_NAME_ADVERTISING_REPORT, if (it.advertisingReport) 1 else 0)
                put(NewsPersistenceContract.COLUMN_NAME_SUBTITLE, it.subtitle)
                put(NewsPersistenceContract.COLUMN_NAME_TEXT, it.text)
                put(NewsPersistenceContract.COLUMN_NAME_UPDATED_IN, it.updatedIn)
                put(NewsPersistenceContract.COLUMN_NAME_PUBLISHED_IN, it.publishedIn)
                put(NewsPersistenceContract.COLUMN_NAME_SECTION_NAME, it.section.name)
                put(NewsPersistenceContract.COLUMN_NAME_SECTION_URL, it.section.url)
                put(NewsPersistenceContract.COLUMN_NAME_TYPE, it.type)
                put(NewsPersistenceContract.COLUMN_NAME_TITLE, it.title)
                put(NewsPersistenceContract.COLUMN_NAME_URL, it.url)
                put(NewsPersistenceContract.COLUMN_NAME_ORIGINAL_URL, it.originalUrl)
            }

            db.insert(NewsPersistenceContract.NewsEntry.TABLE_NAME, null, newsContentValues)

            if (it.authors != null) {

                for (authorName in it.authors!!) {

                    val authorContentValues = ContentValues()

                    with(authorContentValues) {

                        put(AuthorPersistenceContract.COLUMN_NAME_NAME, authorName)
                        put(AuthorPersistenceContract.COLUMN_NAME_NEWS_ID, it.id)
                    }

                    db.insert(AuthorPersistenceContract.AuthorEntry.TABLE_NAME, null, authorContentValues)
                }
            }

            if (it.videos != null) {

                for (videoUrl in it.videos!!) {

                    val videoContentValues = ContentValues()

                    with(videoContentValues) {

                        put(VideoPersistenceContract.COLUMN_NAME_URL, videoUrl)
                        put(VideoPersistenceContract.COLUMN_NAME_NEWS_ID, it.id)
                    }

                    db.insert(VideoPersistenceContract.VideoEntry.TABLE_NAME, null, videoContentValues)
                }
            }

            if (it.images != null) {

                for (image in it.images!!) {

                    val imageContentValues = ContentValues()

                    with(imageContentValues) {

                        put(ImagePersistenceContract.COLUMN_NAME_AUTHOR, image.author)
                        put(ImagePersistenceContract.COLUMN_NAME_SOURCE, image.source)
                        put(ImagePersistenceContract.COLUMN_NAME_CAPTION, image.caption)
                        put(ImagePersistenceContract.COLUMN_NAME_URL, image.url)
                        put(ImagePersistenceContract.COLUMN_NAME_NEWS_ID, it.id)
                    }

                    db.insert(ImagePersistenceContract.ImageEntry.TABLE_NAME, null, imageContentValues)
                }
            }
        }

        db.close()

        val callback = object : NewsDataSource.LoadNewsCallback {

            override fun onNewsLoaded(news: List<News>) {

                // ASSERT

                expectedNews.forEach {
                    assertTrue(news.contains(it))
                }
            }

            override fun onDataNotAvailable() {

                // ASSERT

                fail()
            }
        }

        // ACT

        newsLocalDataSource?.getAllNews(callback)
    }

    // endregion

    // region TEARDOWN METHODS

    @After fun teardown() {

        newsLocalDataSource?.deleteAllNews()
    }

    // endregion

    // region UTILITY METHODS

    private fun loadJsonFromAsset(fileName: String): String? {

        var jsonStr: String?
        var inputStream: InputStream? = null

        try {

            inputStream = javaClass.classLoader.getResourceAsStream(fileName)

            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)

            jsonStr = String(buffer, Charset.forName("UTF-8"))

        } catch (ex: IOException) {

            ex.printStackTrace()

            return null

        } finally {

            inputStream?.close()
        }

        return jsonStr
    }

    private fun createTestData(): List<News> {

        val listType = object : TypeToken<List<News>>(){}.type

        val news = Gson().fromJson<List<News>>(loadJsonFromAsset("headlines.json"), listType)

        return news
    }

    // endregion
}