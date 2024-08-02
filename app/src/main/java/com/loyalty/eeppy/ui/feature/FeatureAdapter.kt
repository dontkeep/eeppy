package com.loyalty.eeppy.ui.feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.loyalty.eeppy.R
import com.loyalty.eeppy.model.Feature

class FeatureAdapter(private val features: List<Feature>): RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>() {
   class FeatureViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
      val nameTextView: TextView = itemView.findViewById(R.id.featureNameTextView)
      val descriptionTextView: TextView = itemView.findViewById(R.id.featureDescriptionTextView)
      val dataTypeTextView: TextView = itemView.findViewById(R.id.featureDataTypeTextView)
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
      val itemView = LayoutInflater.from(parent.context)
         .inflate(R.layout.feature_item, parent, false)
      return FeatureViewHolder(itemView)
   }

   override fun getItemCount(): Int = features.size

   override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
      val currentFeature = features[position]
      holder.nameTextView.text = currentFeature.name
      holder.descriptionTextView.text = currentFeature.description
      holder.dataTypeTextView.text = "Data Type: ${currentFeature.dataType}"
   }
}