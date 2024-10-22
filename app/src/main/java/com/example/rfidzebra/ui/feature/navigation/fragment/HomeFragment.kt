package com.example.rfidzebra.ui.feature.navigation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rfidzebra.databinding.FragmentHomeBinding
import com.example.rfidzebra.ui.feature.home.amb.AssetActivity
import com.example.rfidzebra.ui.feature.home.locate.AllActivity
import com.example.rfidzebra.ui.feature.home.outbound.OutboundActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigate()

    }

    private fun navigate() {

        binding.imageButtonProcurement.setOnClickListener {
            Toast.makeText(requireActivity(), "Sabar fitur masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.imageButtonAmb.setOnClickListener {
            startActivity(Intent(activity, AssetActivity::class.java))
        }
        binding.imageButtonOutbound.setOnClickListener {
            startActivity(Intent(activity, OutboundActivity::class.java))
        }
        binding.imageButtonPutaway.setOnClickListener {
            Toast.makeText(requireActivity(), "Sabar fitur masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.imageButtonOpname.setOnClickListener {
            Toast.makeText(requireActivity(), "Sabar fitur masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.imageButtonCount.setOnClickListener {
            Toast.makeText(requireActivity(), "Sabar fitur masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.imageButtonLocate.setOnClickListener {
            startActivity(Intent(activity, AllActivity::class.java))
        }

    }
}