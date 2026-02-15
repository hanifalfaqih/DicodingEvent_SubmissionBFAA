package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.finished

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
import id.hanifalfaqih.dicodingevent_submissionbfaa.databinding.FragmentFinishedBinding
import id.hanifalfaqih.dicodingevent_submissionbfaa.di.Injection
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.ViewModelFactory
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.adapter.EventAdapter
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.detail.DetailActivity
import id.hanifalfaqih.dicodingevent_submissionbfaa.utils.Result

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: EventAdapter

    private val viewModel: FinishedViewModel by viewModels {
        ViewModelFactory.getInstance(Injection.provideEventRepository(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top)
            insets
        }

        setupRecyclerView()
        setupSearchBar()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { event -> navigateToDetail(event) }
        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }
    }

    private fun setupSearchBar() {
        with(binding) {
            searchViewFinished.setupWithSearchBar(searchBarFinished)
            searchViewFinished
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    val query = textView.text.toString()
                    searchBarFinished.setText(query)
                    searchViewFinished.hide()
                    if (query.isNotEmpty()) {
                        viewModel.searchEvents(query)
                    }
                    false
                }
        }
    }

    private fun observeViewModel() {
        viewModel.events.observe(viewLifecycleOwner) { result -> handleEventsResult(result) }
    }

    private fun handleEventsResult(result: Result<List<EventItem>>) {
        when (result) {
            is Result.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvEvents.visibility = View.GONE
                binding.tvError.visibility = View.GONE
            }
            is Result.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.rvEvents.visibility = View.VISIBLE
                binding.tvError.visibility = View.GONE
                eventAdapter.submitList(result.data)
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.rvEvents.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = result.error
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
