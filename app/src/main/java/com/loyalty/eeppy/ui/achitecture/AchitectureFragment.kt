package com.loyalty.eeppy.ui.achitecture

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.loyalty.eeppy.R

class AchitectureFragment : Fragment() {

   companion object {
      fun newInstance() = AchitectureFragment()
   }

   private val viewModel: AchitectureViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      // TODO: Use the ViewModel
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      return inflater.inflate(R.layout.fragment_achitecture, container, false)
   }
}