package com.mainak.flagquiz.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.mainak.flagquiz.R
import com.mainak.flagquiz.database.DatabaseCopyHelper
import com.mainak.flagquiz.databinding.FragmentHomeBinding


class FragmentHome : Fragment() {
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        fragmentHomeBinding.buttonStart.setOnClickListener{
            // Here this FragmentHomeDirections is automatically created because we added safeArgs for kotlin in the project
            val direction = FragmentHomeDirections.actionFragmentHomeToFragmentQuiz()
            this.findNavController().navigate(direction)
        }

        return fragmentHomeBinding.root
    }
}