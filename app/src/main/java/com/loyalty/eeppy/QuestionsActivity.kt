package com.loyalty.eeppy

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.loyalty.eeppy.databinding.ActivityQuestionsBinding
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class QuestionsActivity : AppCompatActivity() {
   lateinit var binding: ActivityQuestionsBinding
   private val modelPath = "sleep-disorder.tflite"
   lateinit var interpreter: Interpreter
   var inputs= Array(10){""}
   val classLabels = mapOf(0 to "None",
      1 to "Sleep Apnea",
      2 to "Insomnia"
   )
   private var result = ""

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityQuestionsBinding.inflate(layoutInflater)
      setContentView(binding.root)

      //initialize autocomplete answers list from resources
      val gender = resources.getStringArray(R.array.gender)
      val occupation = resources.getStringArray(R.array.occupation)
      val bmi = resources.getStringArray(R.array.bmi)

      //set autocomplete adapter
      val genderAdapter = ArrayAdapter(this, R.layout.dropdown_item, gender)
      val occupationAdapter = ArrayAdapter(this, R.layout.dropdown_item, occupation)
      val bmiAdapter = ArrayAdapter(this, R.layout.dropdown_item, bmi)

      //create a lister for each autocomplete
      binding.autoComplete1.setAdapter(genderAdapter)
      binding.autoComplete3.setAdapter(occupationAdapter)
      binding.autoComplete5.setAdapter(bmiAdapter)

      //make a listener for each of the autocomplete so i can input the value into specific array index
      binding.autoComplete1.setOnItemClickListener { parent, view, position, id ->
         inputs[0] = gender[position]
         Log.d("inputs", inputs[0])
      }

      binding.autoComplete2.setOnClickListener {
         inputs[1] = binding.autoComplete2.text.toString()
      }

      binding.autoComplete3.setOnItemClickListener { parent, view, position, id ->
         inputs[2] = occupation[position]
         Log.d("inputs", inputs[2])
      }

      binding.question4.addOnChangeListener { slider, value, fromUser ->
         inputs[3] = value.toString()
         Log.d("inputs", inputs[3])
      }

      binding.question5.addOnChangeListener { slider, value, fromUser ->
         inputs[4] = value.toString()
         Log.d("inputs", inputs[4])
      }

      binding.autoComplete4.setOnClickListener {
         inputs[5] = binding.autoComplete4.text.toString()
         Log.d("inputs", inputs[5])
      }

      binding.question7.addOnChangeListener { slider, value, fromUser ->
         inputs[6] = value.toString()
         Log.d("inputs", inputs[6])
      }

      binding.autoComplete5.setOnItemClickListener { parent, view, position, id ->
         inputs[7] = bmi[position]
         Log.d("inputs", inputs[7])
      }

      binding.autoComplete6.setOnClickListener {
         inputs[8] = binding.autoComplete6.text.toString()
         Log.d("inputs", inputs[8])
      }

      binding.autoComplete7.setOnClickListener {
         inputs[9] = binding.autoComplete7.text.toString()
         Log.d("inputs", inputs[9])
         //show the inputs value at Log
         for (i in inputs) {
            Log.d("inputs $i", i)
         }
      }

      binding.clearBtn.setOnClickListener {
         clearAnswer()
      }

      binding.predictBtn.setOnClickListener {
         if (inputs.contains("")) {
            Snackbar.make(it, "Make sure to fill all the questions", Snackbar.LENGTH_LONG)
               .setAction("Action", null).show()
         } else {
            val floatInputs = convertValueofInputs(inputs)
            doInference(floatInputs, interpreter)
            MaterialAlertDialogBuilder(this, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered)
               .setTitle("Result")
               .setMessage("Your test result is you have $result, make sure to keep your body well rested")
               .setIcon(R.drawable.baseline_dark_mode_24)
               .setNegativeButton("Close") { dialog, _ ->
                  val intent = Intent(this, MainActivity::class.java)
                  startActivity(intent)
                  dialog.dismiss()
               }
               .setPositiveButton("Try Again") { dialog, _ ->
                  clearAnswer()
                  dialog.dismiss()
               }
               .show()
         }
      }
      initInterpreter()
      clearAnswer()
   }

   private fun doInference(floatInputs: FloatArray, interperter: Interpreter) {
      val outputs = Array(1) { FloatArray(3) }

      val featureMeans = floatArrayOf(0.501672f, 42.297659f, 4.615385f, 7.127425f, 7.311037f, 59.565217f, 5.391304f, 0.729097f, 128.752508f, 84.799331f)
      val featureStds = floatArrayOf(0.500835f, 8.786527f, 2.529536f, 0.798982f, 1.204123f, 20.737532f, 1.792244f, 0.748728f, 7.810182f, 6.221839f)

      val scaledData = standardizeData(floatInputs, featureMeans, featureStds)
      interperter.run(scaledData, outputs)

      val predictedClassIndex = argmax(outputs[0]) // Find index of max value
      val predictedLabel = classLabels[predictedClassIndex] // Get label from map

      // Display or use the predictedLabel as needed
      if (predictedLabel != null) {
         result = predictedLabel
      }
      Log.d("Prediction", "Predicted Class: $predictedLabel")
   }

   fun standardizeData(inputData: FloatArray, featureMeans: FloatArray, featureStds: FloatArray): FloatArray {
      val standardizedData = FloatArray(inputData.size)
      for (i in inputData.indices) {
         standardizedData[i] = (inputData[i] - featureMeans[i]) / featureStds[i]
      }
      return standardizedData
   }

   fun argmax(array: FloatArray): Int {
      var maxIndex = 0
      var maxValue = array[0]
      for (i in 1 until array.size) {
         if (array[i] > maxValue) {
            maxValue = array[i]
            maxIndex = i
         }
      }
      return maxIndex
   }

   fun convertValueofInputs(inputs: Array<String>): FloatArray {
      var floatInputs = FloatArray(10)
      for (i in inputs.indices) {
         if (i == 0) {
            if (inputs[i] == "Male") {
               inputs[i] = "0"
            } else {
               inputs[i] = "1"
            }
         }else if (i == 2) {
            when (inputs[i]) {
               "Software Engineer" -> inputs[i] = "0"
               "Doctor" -> inputs[i] = "1"
               "Sales Representative" -> inputs[i] = "2"
               "Teacher" -> inputs[i] = "3"
               "Nurse" -> inputs[i] = "4"
               "Engineer" -> inputs[i] = "5"
               "Accountant" -> inputs[i] = "6"
               "Scientist" -> inputs[i] = "7"
               "Lawyer" -> inputs[i] = "8"
               "Salesperson" -> inputs[i] = "9"
               "Manager" -> inputs[i] = "10"
            }
         }else if (i == 7) {
            when (inputs[i]) {
               "Overweight" -> inputs[i] = "0"
               "Normal" -> inputs[i] = "1"
            }
         }
      }

      for (i in inputs.indices) {
         floatInputs[i] = inputs[i].toFloat()
         Log.d("floatInputs", inputs[i])
      }

      return floatInputs
   }

   fun clearAnswer(){
      binding.autoComplete1.setText("")
      binding.autoComplete2.setText("")
      binding.autoComplete3.setText("")
      binding.autoComplete4.setText("")
      binding.autoComplete5.setText("")
      binding.autoComplete6.setText("")
      binding.autoComplete7.setText("")
      binding.question4.value = 0.0f
      binding.question5.value = 0.0f
      binding.question7.value = 0.0f
   }

   fun initInterpreter() {
      val options = Interpreter.Options()
      options.setNumThreads(4)
      options.setUseNNAPI(true)
      interpreter = Interpreter(loadModelFile(assets!!, modelPath), options)
   }

   private fun loadModelFile(assets: AssetManager, mModelPath: String): MappedByteBuffer {
      val fileDescriptor = assets.openFd(mModelPath)
      val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
      val fileChannel = inputStream.channel
      val startOffset = fileDescriptor.startOffset
      val declaredLength = fileDescriptor.declaredLength
      return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
   }
}