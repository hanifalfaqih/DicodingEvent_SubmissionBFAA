package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.setting
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.datastore.SettingPreferences
import kotlinx.coroutines.launch
class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
    fun getNotificationSettings(): LiveData<Boolean> {
        return pref.getNotificationSetting().asLiveData()
    }
    fun saveNotificationSetting(isEnabled: Boolean) {
        viewModelScope.launch {
            pref.saveNotificationSetting(isEnabled)
        }
    }
}
