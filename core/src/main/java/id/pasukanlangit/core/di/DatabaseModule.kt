package id.pasukanlangit.core.di

import android.content.Context
import androidx.room.Room
import net.sqlcipher.database.SQLiteDatabase
import id.pasukanlangit.core.data.source.local.room.TourismDao
import id.pasukanlangit.core.data.source.local.room.TourismDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private val passphrase: ByteArray = SQLiteDatabase.getBytes("dicoding".toCharArray())
    val factory = SupportFactory(passphrase)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): TourismDatabase =
        Room.databaseBuilder(context, TourismDatabase::class.java, "Tourism.db")
            .fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()

    @Provides
    fun provideTourismDabo(database: TourismDatabase): TourismDao = database.tourismDao()
}