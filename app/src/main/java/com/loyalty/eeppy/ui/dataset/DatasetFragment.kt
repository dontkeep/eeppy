package com.loyalty.eeppy.ui.dataset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.loyalty.eeppy.R
import com.loyalty.eeppy.databinding.FragmentDatasetBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class DatasetFragment : Fragment() {
   private var _binding: FragmentDatasetBinding? = null
   private val binding get() = _binding!!
   private var tableData: List<List<String>> = emptyList()
   private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentDatasetBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      coroutineScope.launch {
         tableData = loadCSVData()
         Log.d("DatasetFragment", "Data loaded: ${tableData.size} rows")
         displayTableData(tableData)
      }
   }

   private suspend fun loadCSVData(): List<List<String>> = withContext(Dispatchers.IO) {
      val tableDataList = mutableListOf<List<String>>()
      try {
         resources.openRawResource(R.raw.dataset).bufferedReader().useLines { lines ->
            lines.forEach { line ->
               tableDataList.add(line.split(","))
            }
         }
      } catch (e: IOException) {
         Log.e("DatasetFragment", "Error loading CSV data", e)
      }
      tableDataList
   }

   private fun displayTableData(tableData: List<List<String>>) {
      binding.tableLayout.removeAllViews()

      val maxRows = 10 // Or adjust as needed
      val rowsToShow = tableData.take(maxRows)

      for (rowData in rowsToShow) {
         val tableRow = TableRow(requireContext())
         tableRow.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT
         )

         for (column in rowData) {
            val textView = TextView(requireContext())
            textView.text = column
            textView.setPadding(8, 8, 8, 8)
            textView.setBackgroundResource(R.drawable.border_cell)
            tableRow.addView(textView)
         }

         binding.tableLayout.addView(tableRow)
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
      coroutineScope.cancel()
   }
}