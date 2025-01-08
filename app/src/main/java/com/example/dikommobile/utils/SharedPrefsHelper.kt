import android.content.Context
import android.content.SharedPreferences

object SharedPrefsHelper {

    private const val PREF_NAME = "AppSettings"
    private const val KEY_IS_USER_AUTHORIZED = "isUserAuthorized"
    private const val KEY_USER_ID = "userId" // Если нужен ID пользователя

    private lateinit var sharedPreferences: SharedPreferences

    // Инициализация SharedPreferences
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Сохранение состояния авторизации
    fun setUserAuthorized(isAuthorized: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_USER_AUTHORIZED, isAuthorized).apply()
    }

    // Получение состояния авторизации
    fun isUserAuthorized(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_USER_AUTHORIZED, false)
    }

    // Сохранение идентификатора пользователя (если нужно)
    fun setUserId(userId: String) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply()
    }

    // Получение идентификатора пользователя
    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    // Очистка всех данных
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}

