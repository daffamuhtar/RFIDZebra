package com.example.rfidzebra.ui.feature.navigation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rfidzebra.R
import com.example.rfidzebra.databinding.FragmentToolsBinding

class ToolsFragment : Fragment() {

    private lateinit var binding: FragmentToolsBinding

    private var fabVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentToolsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabVisible = false

        binding.idFABAdd.setOnClickListener {

            if (!fabVisible) {

                binding.idFABHome.show()
                binding.idFABSettings.show()

                binding.idFABHome.visibility = View.VISIBLE
                binding.idFABSettings.visibility = View.VISIBLE

                binding.idFABHome.setImageDrawable(resources.getDrawable(R.drawable.ic_bluetooth_white_24dp))

                fabVisible = true
            } else {

                binding.idFABHome.hide()
                binding.idFABSettings.hide()

                binding.idFABHome.visibility = View.GONE
                binding.idFABSettings.visibility = View.GONE

                binding.idFABHome.setImageDrawable(resources.getDrawable(R.drawable.ic_bluetooth_white_24dp))

                fabVisible = false
            }
        }

        binding.idFABHome.setOnClickListener {
            Toast.makeText(context, "Home clicked..", Toast.LENGTH_LONG).show()
        }

        binding.idFABSettings.setOnClickListener {
            Toast.makeText(context, "Settings clicked..", Toast.LENGTH_LONG).show()
        }

    }

}