package com.loyalty.eeppy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class AppIntroActivity : AppIntro() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      addSlide(
         AppIntroFragment.newInstance(
            title = "Welcome!",
            description = "This app helps you learn about sleep disorders and assess your risk of insomnia.",
            imageDrawable = R.drawable.logo // Replace with your image
         )
      )

      // Slide 2
      addSlide(
         AppIntroFragment.newInstance(
            title = "How it Works",
            description = "Answer a few simple questions about your lifestyle and sleep habits.",
            imageDrawable = R.drawable.logo
         )
      )

      // Slide 3 (optional)
      addSlide(
         AppIntroFragment.newInstance(
            title = "Get Started",
            description = "Tap the button below to begin the assessment.",
            imageDrawable = R.drawable.logo
         )
      )

      // Customization (optional)
      setIndicatorColor(
         selectedIndicatorColor = Color.parseColor("#FF0000"), // Customize colors
         unselectedIndicatorColor = Color.parseColor("#FFFFFF")
      )
      isIndicatorEnabled = true
   }
   override fun onSkipPressed(currentFragment: Fragment?) {
      super.onSkipPressed(currentFragment)
      finishIntro()
   }

   override fun onDonePressed(currentFragment: Fragment?) {
      super.onDonePressed(currentFragment)
      finishIntro()
   }

   private fun finishIntro() {
      // Mark intro as seen using SharedPreferences
      val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
      prefs.edit().putBoolean("intro_seen", true).apply()

      // Start your main activity
      startActivity(Intent(this, MainActivity::class.java))
      finish()
   }
}