package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.hanifalfaqih.dicodingevent_submissionbfaa.databinding.FragmentFavoriteBinding
import id.hanifalfaqih.dicodingevent_submissionbfaa.di.Injection
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.ViewModelFactory
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.adapter.FavoriteEventAdapter
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.detail.DetailActivity

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(Injection.provideEventRepository(requireContext()))
    }
    private lateinit var adapter: FavoriteEventAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeFavoriteEvents()
    }

    private fun setupRecyclerView() {
        adapter = FavoriteEventAdapter(
            onItemClick = { event ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
                startActivity(intent)
            },
            onDeleteClick = { event ->
                viewModel.deleteFavorite(event)
            }
        )
        binding.rvFavoriteEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteEvents.adapter = adapter
    }

    private fun observeFavoriteEvents() {
        viewModel.favoriteEvents.observe(viewLifecycleOwner) { events ->
            if (events.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvFavoriteEvents.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.rvFavoriteEvents.visibility = View.VISIBLE
                adapter.submitList(events)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
