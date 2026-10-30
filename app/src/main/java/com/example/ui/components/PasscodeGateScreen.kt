package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonRed
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun PasscodeGateScreen(
  onUnlock: (String) -> Boolean,
  hasError: Boolean,
  onClearError: () -> Unit
) {
  var passcode by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }
  val shakeOffset = remember { Animatable(0f) }
  val scope = rememberCoroutineScope()

  fun triggerShake() {
    scope.launch {
      shakeOffset.animateTo(
        targetValue = 0f,
        animationSpec = keyframes {
          durationMillis = 400
          0f at 0
          -25f at 50
          25f at 100
          -20f at 150
          20f at 200
          -10f at 250
          10f at 300
          0f at 400
        }
      )
    }
  }

  LaunchedEffect(hasError) {
    if (hasError) {
      triggerShake()
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBackground)
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .offset { IntOffset(shakeOffset.value.roundToInt(), 0) },
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // Interlocking Glowing MD Logo
      Box(
        modifier = Modifier
          .size(100.dp)
          .clip(CircleShape)
          .background(Color.Black)
          .border(2.dp, ElectricBlue, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_merdd_md),
          contentDescription = "Merdd MD Logo",
          modifier = Modifier.size(85.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "MERDD LAB",
        color = TextPrimary,
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 2.sp
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = 4.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Science,
          contentDescription = null,
          tint = CyanAccent,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "David & Mercy's Lab Hub",
          color = CyanAccent,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Enter secure laboratory passcode to decrypt research notes, JAMB test engine & simulations.",
        color = TextSecondary,
        fontSize = 13.sp,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
      )

      Spacer(modifier = Modifier.height(28.dp))

      // Passcode Input Box
      OutlinedTextField(
        value = passcode,
        onValueChange = {
          passcode = it
          if (hasError) onClearError()
        },
        placeholder = { Text("Enter lab passcode...", color = TextMuted) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = if (hasError) NeonRed else ElectricBlue
          )
        },
        trailingIcon = {
          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(
              imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
              contentDescription = "Toggle visibility",
              tint = TextMuted
            )
          }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        isError = hasError,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = ObsidianSurface,
          unfocusedContainerColor = ObsidianSurface,
          focusedBorderColor = ElectricBlue,
          unfocusedBorderColor = Color(0x33FFFFFF),
          errorBorderColor = NeonRed,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
          val success = onUnlock(passcode)
          if (!success) triggerShake()
        }),
        modifier = Modifier.fillMaxWidth(0.85f)
      )

      if (hasError) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Access Denied: Incorrect Lab Passcode",
          color = NeonRed,
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.SemiBold
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = {
          val success = onUnlock(passcode)
          if (!success) triggerShake()
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
        modifier = Modifier
          .fillMaxWidth(0.85f)
          .height(50.dp)
      ) {
        Text(
          text = "DECRYPT & ENTER LAB",
          color = Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Hint: Authorized passcode: solana",
        color = TextMuted,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace
      )
    }
  }
}
