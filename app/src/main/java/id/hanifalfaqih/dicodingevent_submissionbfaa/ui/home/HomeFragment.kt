package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.EventItem
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.retrofit.ApiConfig
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.repository.EventRepository
import id.hanifalfaqih.dicodingevent_submissionbfaa.databinding.FragmentHomeBinding
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.ViewModelFactory
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.adapter.EventAdapter
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.adapter.HorizontalEventAdapter
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.detail.DetailActivity
import id.hanifalfaqih.dicodingevent_submissionbfaa.utils.Result

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingAdapter: HorizontalEventAdapter
    private lateinit var finishedAdapter: EventAdapter

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(EventRepository.getInstance(ApiConfig.getApiService()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top)
            insets
        }

        setupRecyclerViews()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        // Setup upcoming events horizontal RecyclerView
        upcomingAdapter = HorizontalEventAdapter { event -> navigateToDetail(event) }
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }

        // Setup finished events vertical RecyclerView
        finishedAdapter = EventAdapter { event -> navigateToDetail(event) }
        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { result ->
            handleUpcomingEventsResult(result)
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { result ->
            handleFinishedEventsResult(result)
        }
    }

    private fun handleUpcomingEventsResult(result: Result<List<EventItem>>) {
        when (result) {
            is Result.Loading -> {
                binding.progressUpcoming.visibility = View.VISIBLE
                binding.rvUpcomingEvents.visibility = View.GONE
                binding.tvUpcomingError.visibility = View.GONE
            }
            is Result.Success -> {
                binding.progressUpcoming.visibility = View.GONE
                binding.rvUpcomingEvents.visibility = View.VISIBLE
                binding.tvUpcomingError.visibility = View.GONE
                upcomingAdapter.submitList(result.data)
            }
            is Result.Error -> {
                binding.progressUpcoming.visibility = View.GONE
                binding.rvUpcomingEvents.visibility = View.GONE
                binding.tvUpcomingError.visibility = View.VISIBLE
                binding.tvUpcomingError.text = result.error
            }
        }
    }

    private fun handleFinishedEventsResult(result: Result<List<EventItem>>) {
        when (result) {
            is Result.Loading -> {
                binding.progressFinished.visibility = View.VISIBLE
                binding.rvFinishedEvents.visibility = View.GONE
                binding.tvFinishedError.visibility = View.GONE
            }
            is Result.Success -> {
                binding.progressFinished.visibility = View.GONE
                binding.rvFinishedEvents.visibility = View.VISIBLE
                binding.tvFinishedError.visibility = View.GONE
                finishedAdapter.submitList(result.data)
            }
            is Result.Error -> {
                binding.progressFinished.visibility = View.GONE
                binding.rvFinishedEvents.visibility = View.GONE
                binding.tvFinishedError.visibility = View.VISIBLE
                binding.tvFinishedError.text = result.error
            }
        }
    }

    private fun navigateToDetail(event: EventItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
