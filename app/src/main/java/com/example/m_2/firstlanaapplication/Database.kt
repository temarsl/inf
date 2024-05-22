package com.example.m_2.firstlanaapplication

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

//таблицу SQLite в которой будут хранится данные
@Entity(tableName = "Users")
data class User(
    @PrimaryKey val login: String,
    val password: String
)

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        fun getDatabase(context: Context): AppDatabase {

            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "Data_base"
            ).build()
        }
    }
}

// инициализирует базу данных при запуске приложения:
class App : Application() {
    val database by lazy {
        AppDatabase.getDatabase(this)
    }
}

//позволяет выполнять SQL – запросы для чтения или обновления данных.
@Dao
interface UserDao {
    @Query("SELECT COUNT(*) FROM Users WHERE login = :login AND password = :password")
    fun isUserExistByLoginAndPassword(login: String, password: String): Boolean

    @Query("SELECT COUNT(*) FROM Users WHERE login = :login")
    fun isUserExistByLogin(login: String): Boolean


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User)
}


// используется для хранения и управления данными, связанными с пользовательским интерфейсом
//  в качестве параметра базу данных AppDatabase.
class MainViewModel(private val database: AppDatabase) : ViewModel() {
    companion object {
        // определена фабрика (factory), которая является частью архитектуры Android Jetpack
        // и используется для создания экземпляров ViewModel.
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as App).database
                return MainViewModel(database) as T
            }

        }
    }

    // Добавим поле database в качестве члена класса
    private val userDao = database.userDao()

    //  Проверяет, существует ли пользователь с данным логином.
    fun login_search(login: String): Boolean {
        return runBlocking {
            withContext(Dispatchers.IO) {
                userDao.isUserExistByLogin(login)
            }
        }
    }

    // Метод для проверки логина и пароля при входе
    fun login(login: String, password: String): Boolean {
        // Выполняем операцию в корутине на фоновом потоке
        return runBlocking {
            withContext(Dispatchers.IO) {
                userDao.isUserExistByLoginAndPassword(login, password)
            }
        }
    }


    fun register(login: String, password: String): Boolean {
        return runBlocking {
            withContext(Dispatchers.IO) {
                if (userDao.isUserExistByLogin(login)) {
                    // Пользователь с таким логином уже существует
                    false
                } else {
                    // Регистрация нового пользователя
                    userDao.insertUser(User(login, password))
                    true
                }
            }
        }
    }
}
