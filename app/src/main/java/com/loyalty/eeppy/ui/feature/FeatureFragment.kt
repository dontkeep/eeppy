package com.loyalty.eeppy.ui.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.loyalty.eeppy.databinding.FragmentFeatureBinding
import com.loyalty.eeppy.model.Feature

class FeatureFragment : Fragment() {

   private var _binding: FragmentFeatureBinding? = null

   // This property is only valid between onCreateView and
   // onDestroyView.
   private val binding get() = _binding!!

   val featureList = listOf(
      Feature("Gender", "Identifies the sex of the individual (Male or Female).", "Categorical"),
      Feature("Age", "The age of the individual in years.", "Numerical"),
      Feature("Occupation", "The profession or job of the individual.", "Categorical"),Feature("Sleep Duration", "The number of hours the individual sleeps per night.", "Numerical"),
      Feature("Quality of Sleep", "A subjective rating of how well the individual sleeps (1-10, with 1 being the worst and 10 being the best).", "Numerical"),
      Feature("Physical Activity Level", "A measure of how physically active the individual is (Minutes per day).", "Numerical"),
      Feature("Stress Level", "A subjective rating of the individual's perceived stress level (1-10, with 1 being the worst and 10 being the best).", "Numerical"),
      Feature("BMI Category", "A categorization of the individual's Body Mass Index (e.g., normal, overweight).", "Categorical"),
      Feature("Systolic Blood Pressure", "The top number in a blood pressure reading, measuring the pressure in arteries when the heart beats.", "Numerical"),
      Feature("Diastolic Blood Pressure", "The bottom number in a blood pressure reading, measuring the pressure in arteries between heartbeats.", "Numerical")
   )


   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentFeatureBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      val featureRecyclerView: RecyclerView = binding.featureRecyclerView
      featureRecyclerView.adapter = FeatureAdapter(featureList)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}