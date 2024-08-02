package com.loyalty.eeppy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.loyalty.eeppy.R
import com.loyalty.eeppy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

   private lateinit var binding: FragmentHomeBinding
   // This property is only valid between onCreateView and
   // onDestroyView.

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      return inflater.inflate(R.layout.fragment_home, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding = FragmentHomeBinding.bind(view)
   }
}
