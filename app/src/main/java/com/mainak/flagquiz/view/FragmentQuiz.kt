package com.mainak.flagquiz.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.mainak.flagquiz.R
import com.mainak.flagquiz.database.DatabaseCopyHelper
import com.mainak.flagquiz.database.FlagsDao
import com.mainak.flagquiz.databinding.FragmentQuizBinding
import com.mainak.flagquiz.model.FlagsModel

class FragmentQuiz : Fragment() {
    private lateinit var fragmentQuizBinding : FragmentQuizBinding

    private val dao = FlagsDao()

    // The list of a flag with it's details
    private var flagList = ArrayList<FlagsModel>()

    // All the other data on the elements
    private var correctNumber : Int = 0
    private var wrongNumber : Int = 0
    private var emptyNumber : Int = 0
    private var questionNumber : Int = 0

    private lateinit var correctFlag : FlagsModel

    // It contains the wrongFlags
    private var wrongFlags = ArrayList<FlagsModel>()

    private var isOptionSelected : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentQuizBinding = FragmentQuizBinding.inflate(inflater,container,false)

        val db = DatabaseCopyHelper(requireActivity())
        db.createDataBase()
        db.openDataBase()
        flagList = dao.getRandomTenRecords(db)

        Log.d("Hello", flagList.size.toString())
        for(flag in flagList){
            Log.d("flags", flag.id.toString())
            Log.d("flags", flag.countryName)
            Log.d("flags", flag.flagName)
            Log.d("flags", "*********************")
        }
        db.close()
        showData()

        fragmentQuizBinding.buttonA.setOnClickListener{
            answerControl(fragmentQuizBinding.buttonA)
        }
        fragmentQuizBinding.buttonB.setOnClickListener{
            answerControl(fragmentQuizBinding.buttonB)

        }
        fragmentQuizBinding.buttonC.setOnClickListener{
            answerControl(fragmentQuizBinding.buttonC)

        }
        fragmentQuizBinding.buttonD.setOnClickListener{
            answerControl(fragmentQuizBinding.buttonD)

        }

        fragmentQuizBinding.next.setOnClickListener {
            // Do this if the options are not selected
            if(!isOptionSelected){
                emptyNumber++
                fragmentQuizBinding.textViewEmpty.text = emptyNumber.toString()
            }

            // If all the questions are over
            if(questionNumber == 9){
                val direction = FragmentQuizDirections.actionFragmentQuizToFragmentResult2(correctNumber,wrongNumber,emptyNumber)
                this.findNavController().navigate(direction)
                this.findNavController().popBackStack(R.id.fragmentResult2, false)
                return@setOnClickListener
            }

            // Going to next Questions
            questionNumber++
            // Showing the new data
            showData()
            setButtonToInitialProperties()
            isOptionSelected = false
        }
        return fragmentQuizBinding.root
    }

    private fun showData(){
        // Setting the question number text
        fragmentQuizBinding.textViewQuestion.text = resources.getString(R.string.question_number).plus(questionNumber+1)

        // Everytime new flag is loaded the correct flag gets change
        correctFlag = flagList[questionNumber]

        // Setting the image
        fragmentQuizBinding.imageViewFlag.setImageResource(resources.getIdentifier(correctFlag.countryName, "drawable", requireActivity().packageName))

        // It sets the wrong flags
        wrongFlags = dao.getRandomThreeRecords(DatabaseCopyHelper(requireActivity()), correctFlag.id)

        // We are storing it in HashSet because HashSet stores the things inside it randomly and we are using this feature to arrange the options
        val mixOptions = HashSet<FlagsModel>()
        mixOptions.clear()
        mixOptions.add(correctFlag)
        mixOptions.addAll(wrongFlags)

        // Putting the random FlagsModel in an organized ArrayList
        val options = ArrayList<FlagsModel>()
        mixOptions.forEach {
            options.add(it)
        }

        // Setting the buttons
        fragmentQuizBinding.buttonA.text = options[0].flagName
        fragmentQuizBinding.buttonB.text = options[1].flagName
        fragmentQuizBinding.buttonC.text = options[2].flagName
        fragmentQuizBinding.buttonD.text = options[3].flagName
    }

    private fun answerControl(button : Button){
        isOptionSelected = true
        val clickedOption = button.text.toString()
        val correctAnswer = correctFlag.flagName
        if(clickedOption == correctAnswer){
            Log.d("options","$clickedOption == $correctAnswer")
            correctNumber++
            fragmentQuizBinding.textViewCorrect.text = correctNumber.toString()
            button.setBackgroundColor(Color.GREEN)
        }
        else{
            Log.d("options","$clickedOption != $correctAnswer")
            wrongNumber++
            fragmentQuizBinding.textViewWrong.text = wrongNumber.toString()
            button.setBackgroundColor(Color.RED)

            when(correctAnswer){
                fragmentQuizBinding.buttonA.text -> fragmentQuizBinding.buttonA.setBackgroundColor(Color.GREEN)
                fragmentQuizBinding.buttonB.text -> fragmentQuizBinding.buttonB.setBackgroundColor(Color.GREEN)
                fragmentQuizBinding.buttonC.text -> fragmentQuizBinding.buttonC.setBackgroundColor(Color.GREEN)
                fragmentQuizBinding.buttonD.text -> fragmentQuizBinding.buttonD.setBackgroundColor(Color.GREEN)
            }
        }

        fragmentQuizBinding.buttonA.isClickable = false
        fragmentQuizBinding.buttonB.isClickable = false
        fragmentQuizBinding.buttonC.isClickable = false
        fragmentQuizBinding.buttonD.isClickable = false
    }

    private fun setButtonToInitialProperties(){
        fragmentQuizBinding.buttonA.setBackgroundColor(Color.WHITE)
        fragmentQuizBinding.buttonB.setBackgroundColor(Color.WHITE)
        fragmentQuizBinding.buttonC.setBackgroundColor(Color.WHITE)
        fragmentQuizBinding.buttonD.setBackgroundColor(Color.WHITE)

        fragmentQuizBinding.buttonA.isClickable = true
        fragmentQuizBinding.buttonB.isClickable = true
        fragmentQuizBinding.buttonC.isClickable = true
        fragmentQuizBinding.buttonD.isClickable = true
    }
}