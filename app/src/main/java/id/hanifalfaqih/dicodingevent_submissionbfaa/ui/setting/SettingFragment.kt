package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.setting
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import id.hanifalfaqih.dicodingevent_submissionbfaa.databinding.FragmentSettingBinding
import id.hanifalfaqih.dicodingevent_submissionbfaa.di.Injection
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.ViewModelFactory
import id.hanifalfaqih.dicodingevent_submissionbfaa.worker.DailyReminderWorker
import java.util.concurrent.TimeUnit
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by viewModels {
        val settingPreferences = Injection.provideSettingPreferences(requireContext())
        val repository = Injection.provideEventRepository(requireContext())
        ViewModelFactory.getInstance(repository, settingPreferences)
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Snackbar.make(binding.root, "Notification permission granted", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Notification permission denied", Snackbar.LENGTH_SHORT).show()
                binding.switchNotification.isChecked = false
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupThemeSetting()
        setupNotificationSetting()
    }
    private fun setupThemeSetting() {
        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            binding.switchDarkMode.isChecked = isDarkModeActive
        }
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
    private fun setupNotificationSetting() {
        viewModel.getNotificationSettings().observe(viewLifecycleOwner) { isEnabled ->
            binding.switchNotification.isChecked = isEnabled
        }
        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        return@setOnCheckedChangeListener
                    }
                }
                startDailyReminder()
            } else {
                cancelDailyReminder()
            }
            viewModel.saveNotificationSetting(isChecked)
        }
    }
    private fun startDailyReminder() {
        val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            1, TimeUnit.DAYS
        ).build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            DAILY_REMINDER_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
    private fun cancelDailyReminder() {
        WorkManager.getInstance(requireContext()).cancelUniqueWork(DAILY_REMINDER_WORK)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val DAILY_REMINDER_WORK = "DailyReminderWork"
    }
}
