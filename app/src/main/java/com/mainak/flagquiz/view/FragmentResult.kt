package com.mainak.flagquiz.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.mainak.flagquiz.R
import com.mainak.flagquiz.databinding.FragmentResultBinding

class FragmentResult : Fragment() {

    private lateinit var fragmentResultBinding: FragmentResultBinding
    var correctNumber : Int= 0
    var wrongNumber : Int= 0
    var emptyNumber : Int= 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        fragmentResultBinding = FragmentResultBinding.inflate(inflater, container,false)

        // Taking all the arguments into the args
        val args : FragmentResultArgs? = arguments?.let {
            FragmentResultArgs.fromBundle(it);
        }
        args?.let{
            correctNumber = it.correct
            wrongNumber = it.wrong
            emptyNumber = it.empty
        }

        val barEntriesArrayListCorrect = ArrayList<BarEntry>()
        val barEntriesArrayListEmpty = ArrayList<BarEntry>()
        val barEntriesArrayListWrong = ArrayList<BarEntry>()
        barEntriesArrayListCorrect.add(BarEntry(0F,correctNumber.toFloat()))
        barEntriesArrayListEmpty.add(BarEntry(1F,emptyNumber.toFloat()))
        barEntriesArrayListWrong.add(BarEntry(2F,wrongNumber.toFloat()))

        val barDataSetCorrect = BarDataSet(barEntriesArrayListCorrect, "Correct Number").apply {
            color = Color.GREEN
            valueTextSize = 24F
            setValueTextColors(arrayListOf(Color.BLACK))
        }
        val barDataSetEmpty = BarDataSet(barEntriesArrayListEmpty, "Empty Number").apply {
            color = Color.BLUE
            valueTextSize = 24F
            setValueTextColors(arrayListOf(Color.BLACK))
        }
        val barDataSetWrong = BarDataSet(barEntriesArrayListWrong, "Wrong Number").apply {
            color = Color.RED
            valueTextSize = 24F
            setValueTextColors(arrayListOf(Color.BLACK))
        }

        val barData = BarData(barDataSetCorrect, barDataSetWrong, barDataSetEmpty)

        fragmentResultBinding.resultChart.data = barData

        fragmentResultBinding.buttonNextQuiz.setOnClickListener{
            this.findNavController().popBackStack(R.id.fragmentHome, false)
        }

        fragmentResultBinding.buttonExit.setOnClickListener {
            requireActivity().finish()
        }
        return fragmentResultBinding.root
    }
}