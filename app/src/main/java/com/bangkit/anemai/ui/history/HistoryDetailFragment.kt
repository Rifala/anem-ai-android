package com.bangkit.anemai.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.transition.ChangeBounds
import com.bangkit.anemai.R
import com.bangkit.anemai.data.model.DetectionResponse
import com.bangkit.anemai.databinding.FragmentHistoryDetailBinding
import com.bumptech.glide.Glide

class HistoryDetailFragment : Fragment() {
    private lateinit var binding: FragmentHistoryDetailBinding
    private lateinit var menuProvider: MenuProvider
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryDetailBinding.inflate(layoutInflater)
        sharedElementEnterTransition = ChangeBounds().apply { duration = 750 }
        sharedElementReturnTransition = ChangeBounds().apply { duration = 750 }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val result = HistoryDetailFragmentArgs.fromBundle(arguments as Bundle).detectionHistory

        setup(result)
    }

    private fun setup(result: DetectionResponse) {
        val additionalInfo = result.informasiTambahan

        binding.apply {
            tvResult.text = getString(R.string.detection_result, result.result)
            tvDate.text = getString(R.string.detection_date, result.createdAt)
            tvAccuracy.text = getString(R.string.detection_accuracy, result.akurasi)
            tvRisk.text = additionalInfo?.risikoKomplikasi
            tvPrecaution.text = additionalInfo?.pencegahan
            tvSuggestion.text = additionalInfo?.tindakanSaran

            Glide.with(requireContext())
                .load(result.imageUrl)
                .into(imageViewDetection)
        }
    }

    private fun setupActionbar() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.history_detection_detail)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setHomeButtonEnabled(true)
        }

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    android.R.id.home -> {
                        requireActivity().supportFragmentManager.popBackStack()
                        return true
                    }
                }

                return false
            }

        }

        requireActivity().addMenuProvider(menuProvider)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(menuProvider)
    }

    override fun onResume() {
        super.onResume()
        setupActionbar()
    }
}