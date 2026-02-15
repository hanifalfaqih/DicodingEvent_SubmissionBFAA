package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.detail

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import id.hanifalfaqih.dicodingevent_submissionbfaa.R
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.EventItem
import id.hanifalfaqih.dicodingevent_submissionbfaa.databinding.ActivityDetailBinding
import id.hanifalfaqih.dicodingevent_submissionbfaa.di.Injection
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.ViewModelFactory
import id.hanifalfaqih.dicodingevent_submissionbfaa.utils.Result
import id.hanifalfaqih.dicodingevent_submissionbfaa.utils.loadImage

class DetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private var isFavorite = false
    private var currentEvent: EventItem? = null

    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(Injection.provideEventRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        if (eventId != -1) {
            viewModel.loadEventDetail(eventId)
            observeViewModel(eventId)
        }
    }

    private fun observeViewModel(eventId: Int) {
        viewModel.eventDetail.observe(this) { result -> handleEventDetailResult(result) }

        viewModel.isFavorite(eventId).observe(this) { favorite ->
            isFavorite = favorite
            updateFavoriteButton()
        }
    }

    private fun handleEventDetailResult(result: Result<EventItem>) {
        when (result) {
            is Result.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.layoutContent.visibility = View.GONE
                binding.tvError.visibility = View.GONE
            }

            is Result.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.layoutContent.visibility = View.VISIBLE
                binding.tvError.visibility = View.GONE
                currentEvent = result.data
                displayEventDetail(result.data)
            }

            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.layoutContent.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = result.error
            }
        }
    }

    private fun displayEventDetail(event: EventItem) {
        binding.apply {
            ivEventImage.loadImage(event.mediaCover)

            tvEventName.text = event.name
            tvEventOwner.text = getString(R.string.owner_format, event.ownerName)
            tvEventTime.text = getString(R.string.time_format, event.beginTime)

            val remainingQuota = event.quota - event.registrants
            tvEventQuota.text = getString(R.string.quota_format, remainingQuota)

            val description =
                Html.fromHtml(event.description, Html.FROM_HTML_MODE_LEGACY)
            tvEventDescription.text = description

            btnRegister.setOnClickListener {
                openEventLink(event.link)
            }

            fabFavorite.setOnClickListener {
                if (isFavorite) {
                    viewModel.removeFromFavorite(event.id)
                } else {
                    viewModel.addToFavorite(event)
                }
            }
        }
    }

    private fun updateFavoriteButton() {
        if (isFavorite) {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_favorite)
            )
        } else {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_border)
            )
        }
    }

    private fun openEventLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
